package medlink.waiting.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import medlink.common.exception.ErrorStatus;
import medlink.common.exception.GlobalException;
import medlink.doctor.entity.Doctor;
import medlink.doctor.repository.DoctorRepository;
import medlink.member.entity.Member;
import medlink.member.repository.MemberRepository;
import medlink.reception.entity.Reception;
import medlink.reception.enums.ReceptionStatus;
import medlink.reception.repository.ReceptionRepository;
import medlink.waiting.dto.request.CallPatientRequest;
import medlink.waiting.dto.request.UpdateWaitingStatusRequest;
import medlink.waiting.dto.response.WaitingListResponse;
import medlink.waiting.dto.response.WaitingTicketResponse;
import medlink.waiting.entity.WaitingTicket;
import medlink.waiting.enums.WaitingTicketStatus;
import medlink.waiting.repository.WaitingTicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WaitingTicketService {

  private final WaitingTicketRepository waitingTicketRepository;
  private final ReceptionRepository receptionRepository;
  private final DoctorRepository doctorRepository;
  private final MemberRepository memberRepository;

  /**
   * 의사별 오늘의 대기 목록 조회
   */
  @Transactional(readOnly = true)
  public List<WaitingListResponse> getTodayWaitingList(Long doctorId, String uuid) {
    // 의사 권한 검증 (실제로는 의사 엔티티에 uuid가 있어야 함)
    Doctor doctor = doctorRepository.findById(doctorId)
        .orElseThrow(() -> new GlobalException(ErrorStatus.DOCTOR_NOT_FOUND));

    LocalDate today = LocalDate.now();
    LocalDateTime start = today.atStartOfDay();
    LocalDateTime end = today.plusDays(1).atStartOfDay();

    List<WaitingTicket> tickets = waitingTicketRepository.findTodayWaitingList(doctorId, start, end);

    return tickets.stream()
        .map(WaitingListResponse::from)
        .toList();
  }

  /**
   * 환자 호출
   */
  @Transactional
  public WaitingTicketResponse callPatient(CallPatientRequest request, String uuid) {
    WaitingTicket ticket = waitingTicketRepository.findById(request.getTicketId())
        .orElseThrow(() -> new GlobalException(ErrorStatus.WAITING_TICKET_NOT_FOUND));

    // 권한 검증: 담당 의사만 호출 가능
    // (실제로는 Doctor 엔티티에 uuid 필드가 있어야 함)

    // 대기 중인 상태만 호출 가능
    if (ticket.getStatus() != WaitingTicketStatus.WAITING) {
      throw new GlobalException(ErrorStatus.WAITING_TICKET_NOT_WAITING);
    }

    // 호출 처리
    ticket.call();

    // 접수 상태도 업데이트
    Reception reception = ticket.getReception();
    if (reception.getStatus() == ReceptionStatus.WAITING) {
      reception.setStatus(ReceptionStatus.IN_SERVICE);
    }

    // TODO: 환자에게 알림 전송 (FCM, WebSocket 등)
    log.info("환자 호출: 대기번호 {}, 환자명 {}",
        ticket.getQueueNo(),
        reception.getMember().getName());

    return WaitingTicketResponse.from(ticket);
  }

  /**
   * 대기 상태 업데이트
   */
  @Transactional
  public void updateWaitingStatus(Long ticketId, UpdateWaitingStatusRequest request, String uuid) {
    WaitingTicket ticket = waitingTicketRepository.findById(ticketId)
        .orElseThrow(() -> new GlobalException(ErrorStatus.WAITING_TICKET_NOT_FOUND));

    // 권한 검증

    WaitingTicketStatus newStatus = WaitingTicketStatus.valueOf(request.getStatus());
    ticket.updateStatus(newStatus);

    // 접수 상태도 동기화
    Reception reception = ticket.getReception();
    switch (newStatus) {
      case IN_SERVICE:
        reception.setStatus(ReceptionStatus.IN_SERVICE);
        break;
      case DONE:
        reception.setStatus(ReceptionStatus.DONE);
        break;
      case CANCELLED:
        reception.setStatus(ReceptionStatus.CANCELLED);
        break;
    }
  }

  /**
   * 환자가 본인 대기 현황 조회
   */
  @Transactional(readOnly = true)
  public WaitingTicketResponse getWaitingStatus(Long receptionId, String uuid) {
    Member member = memberRepository.findByUuid(uuid)
        .orElseThrow(() -> new GlobalException(ErrorStatus.MEMBER_NOT_FOUND));

    Reception reception = receptionRepository.findById(receptionId)
        .orElseThrow(() -> new GlobalException(ErrorStatus.RECEPTION_NOT_FOUND));

    // 본인 확인
    if (!reception.getMember().getMemberId().equals(member.getMemberId())) {
      throw new GlobalException(ErrorStatus.RECEPTION_ACCESS_FORBIDDEN);
    }

    WaitingTicket ticket = waitingTicketRepository
        .findTopByReception_ReceptionIdOrderByTicketIdDesc(receptionId)
        .orElseThrow(() -> new GlobalException(ErrorStatus.WAITING_TICKET_NOT_FOUND));

    // 앞에 대기 중인 인원 수 계산
    int waitingCount = waitingTicketRepository.countWaitingBefore(
        reception.getDoctor().getDoctorId(),
        ticket.getQueueNo(),
        LocalDate.now().atStartOfDay()
    );

    // 예상 대기 시간 업데이트
    ticket.updateEstimatedWaitTime(waitingCount);

    return WaitingTicketResponse.from(ticket, waitingCount);
  }

  /**
   * 다음 대기자 자동 호출
   */
  @Transactional
  public WaitingTicketResponse callNextPatient(Long doctorId, String uuid) {
    // 의사 확인
    Doctor doctor = doctorRepository.findById(doctorId)
        .orElseThrow(() -> new GlobalException(ErrorStatus.DOCTOR_NOT_FOUND));

    // 오늘 날짜의 다음 대기자 조회
    LocalDate today = LocalDate.now();
    WaitingTicket nextTicket = waitingTicketRepository
        .findNextWaitingTicket(doctorId, today.atStartOfDay())
        .orElseThrow(() -> new GlobalException(ErrorStatus.NO_WAITING_PATIENTS));

    // 호출 처리
    nextTicket.call();

    Reception reception = nextTicket.getReception();
    if (reception.getStatus() == ReceptionStatus.WAITING) {
      reception.setStatus(ReceptionStatus.IN_SERVICE);
    }

    log.info("다음 환자 호출: 대기번호 {}, 환자명 {}",
        nextTicket.getQueueNo(),
        reception.getMember().getName());

    return WaitingTicketResponse.from(nextTicket);
  }
}