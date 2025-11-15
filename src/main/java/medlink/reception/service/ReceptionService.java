package medlink.reception.service;

import lombok.RequiredArgsConstructor;
import medlink.common.exception.ErrorStatus;
import medlink.common.exception.GlobalException;
import medlink.doctor.entity.Doctor;
import medlink.doctor.repository.DoctorRepository;
import medlink.member.entity.Member;
import medlink.member.repository.MemberRepository;
import medlink.reception.dto.request.ReceptionCreateRequest;
import medlink.reception.dto.request.ReceptionFromReservationCreateRequest;
import medlink.reception.dto.response.ReceptionDetailResponse;
import medlink.reception.dto.response.ReceptionIdResponse;
import medlink.reception.dto.response.ReceptionListItemResponse;
import medlink.reception.entity.Reception;
import medlink.reception.entity.ReceptionSymptom;
import medlink.reception.enums.ReceptionStatus;
import medlink.reception.enums.ReceptionType;
import medlink.reception.repository.ReceptionRepository;
import medlink.reception.repository.ReceptionSymptomRepository;
import medlink.reservation.entity.Reservation;
import medlink.reservation.enums.ReservationStatus;
import medlink.reservation.repository.ReservationRepository;
import medlink.symptom.entity.Symptom;
import medlink.symptom.repository.SymptomRepository;
import medlink.waiting.repository.WaitingTicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ReceptionService {

  private final MemberRepository memberRepository;
  private final DoctorRepository doctorRepository;
  private final SymptomRepository symptomRepository;
  private final ReceptionRepository receptionRepository;
  private final ReceptionSymptomRepository receptionSymptomRepository;
  private final WaitingTicketRepository waitingTicketRepository;
  private final ReservationRepository reservationRepository;

  /**
   * 접수 생성
   */
  @Transactional
  public ReceptionIdResponse createReception(String uuid, ReceptionCreateRequest receptionCreateRequest) {
    if (Boolean.FALSE.equals(receptionCreateRequest.getConsentNotice())) {
      throw new GlobalException(ErrorStatus.RECEPTION_CONSENT_REQUIRED);
    }

    Member member = memberRepository.findByUuid(uuid)
        .orElseThrow(() -> new GlobalException(ErrorStatus.MEMBER_NOT_FOUND));

    Doctor doctor = doctorRepository.findById(receptionCreateRequest.getDoctorId())
        .orElseThrow(() -> new GlobalException(ErrorStatus.DOCTOR_NOT_FOUND));

    // Reception
    Reception reception = Reception.builder()
        .member(member)
        .doctor(doctor)
        .receptionNo(generateReceptionNo())
        .type(ReceptionType.DIRECT)
        .status(ReceptionStatus.WAITING)
        .consentNotice(true)
        .consentAt(LocalDateTime.now())
        .noteToDoctor(receptionCreateRequest.getNoteToDoctor())
        .build();
    receptionRepository.save(reception);

    // Symptoms
    List<Long> ids = receptionCreateRequest.getSymptomIds().stream().distinct().toList();
    List<Symptom> symptoms = symptomRepository.findAllById(ids);
    if (symptoms.size() != ids.size()) {
      throw new GlobalException(ErrorStatus.RECEPTION_INVALID_SYMPTOM);
    }
    List<ReceptionSymptom> links = symptoms.stream()
        .map(s -> ReceptionSymptom.builder()
            .reception(reception)
            .symptom(s)
            .createdAt(LocalDateTime.now())
            .build())
        .toList();
    receptionSymptomRepository.saveAll(links);

// Waiting ticket (추후 구현 예정)
// WaitingTicket ticket = WaitingTicket.builder()
//         .reception(reception)
//         .queueNo(assignQueueNoForToday(doctor.getDoctorId()))
//         .status(WaitingTicketStatus.WAITING)
//         .build();
// waitingTicketRepository.save(ticket);

    return ReceptionIdResponse.of(reception.getReceptionId());
  }

  /**
   * 접수 상세
   */
  @Transactional(readOnly = true)
  public ReceptionDetailResponse getReceptionDetail(Long id) {
    Reception r = receptionRepository.findById(id)
        .orElseThrow(() -> new GlobalException(ErrorStatus.RECEPTION_NOT_FOUND));
    var symptoms = receptionSymptomRepository.findSymptomNames(id);
    var ticketOpt = waitingTicketRepository.findTopByReception_ReceptionIdOrderByTicketIdDesc(id);
    return ReceptionDetailResponse.from(r, symptoms, ticketOpt);
  }

  /**
   * 나의 접수 목록 조회 (상태/기간 필터)
   */
  @Transactional(readOnly = true)
  public List<ReceptionListItemResponse> getMyReceptionList(String uuid,
                                                            Integer year,
                                                            Integer month,
                                                            ReceptionStatus status) {

    LocalDateTime from = null;
    LocalDateTime to = null;

    // year, month 둘 다 없으면 전체 기간
    if (year != null && month != null) {
      YearMonth ym;
      try {
        ym = YearMonth.of(year, month); // 2025, 11 → 2025-11
      } catch (Exception e) {
        throw new GlobalException(ErrorStatus.BAD_REQUEST, "invalid year/month");
      }
      from = ym.atDay(1).atStartOfDay();                // 2025-11-01T00:00
      to = ym.plusMonths(1).atDay(1).atStartOfDay();  // 2025-12-01T00:00
    }

    // status == null 이면 전체 상태
    ReceptionStatus filterStatus = status; // null 허용

    return receptionRepository.searchMine(uuid, filterStatus, from, to)
        .stream()
        .map(ReceptionListItemResponse::from)
        .toList();
  }


  /**
   * 접수 취소
   */
  @Transactional
  public void cancelReception(Long id, String uuid, String reason) {
    Reception r = receptionRepository.findById(id)
        .orElseThrow(() -> new GlobalException(ErrorStatus.RECEPTION_NOT_FOUND));

    if (!uuid.equals(r.getMember().getUuid())) {
      throw new GlobalException(ErrorStatus.RECEPTION_ACCESS_FORBIDDEN);
    }
    if (r.getStatus() == ReceptionStatus.DONE || r.getStatus() == ReceptionStatus.CANCELLED) {
      throw new GlobalException(ErrorStatus.RECEPTION_ALREADY_FINALIZED);
    }
    // (옵션) 처방 존재 여부 검사: PrescriptionRepository.existsByReception_ReceptionId(id)

    r.setStatus(ReceptionStatus.CANCELLED);

// 대기표 상태 변경 (추후 대기표 기능 도입 시 활성화)
// waitingTicketRepository.findTopByReception_ReceptionIdOrderByTicketIdDesc(id)
//         .ifPresent(t -> {
//             switch (t.getStatus()) {
//                 case WAITING, CALLED -> t.setStatus(WaitingTicketStatus.CANCELLED);
//                 default -> {}
//             }
//         });

    // (옵션) 알림 기록 등
  }

  /**
   * 예약을 기반으로 접수 생성
   */
  @Transactional
  public Long createReceptionFromReservation(
      String uuid,
      ReceptionFromReservationCreateRequest request
  ) {
    // 0) 동의 체크 검증 (DIRECT와 정책 맞추기)
    if (Boolean.FALSE.equals(request.getConsentNotice())) {
      throw new GlobalException(ErrorStatus.RECEPTION_CONSENT_REQUIRED);
    }

    // 1) 로그인 회원 조회 (uuid 기준)
    Member member = memberRepository.findByUuid(uuid)
        .orElseThrow(() -> new GlobalException(ErrorStatus.MEMBER_NOT_FOUND));

    // 2) 예약 조회
    Reservation reservation = reservationRepository.findById(request.getReservationId())
        .orElseThrow(() -> new GlobalException(ErrorStatus.RESERVATION_NOT_FOUND));

    // 3) 예약 주인인지 검증 (방어 로직)
    if (!reservation.getMember().getMemberId().equals(member.getMemberId())) {
      throw new GlobalException(ErrorStatus.RESERVATION_ACCESS_FORBIDDEN);
    }

    // 4) 예약 상태 체크 (RESERVED만 접수 가능)
    if (reservation.getStatus() != ReservationStatus.RESERVED) {
      throw new GlobalException(ErrorStatus.RESERVATION_NOT_AVAILABLE_FOR_RECEPTION);
    }

    // 5) 오늘 예약인지 체크
    if (!reservation.getAppointmentAt().toLocalDate().equals(LocalDate.now())) {
      throw new GlobalException(ErrorStatus.RESERVATION_NOT_TODAY);
    }

    // 6) 이미 이 예약으로 접수된 내역이 있는지 방지
    if (receptionRepository.existsByReservation_ReservationId(reservation.getReservationId())) {
      throw new GlobalException(ErrorStatus.RECEPTION_ALREADY_EXISTS);
    }

    // 7) 접수번호 생성
    String receptionNo = generateReceptionNo();
    LocalDateTime now = LocalDateTime.now();

    // 8) 의사 조회 (예약이 doctor를 들고 있으면 그대로 사용)
    Doctor doctor = reservation.getDoctor();
    if (doctor == null) {
      doctor = doctorRepository.findById(reservation.getDoctor().getDoctorId())
          .orElseThrow(() -> new GlobalException(ErrorStatus.DOCTOR_NOT_FOUND));
    }

    // 9) Reception 엔티티 생성
    Reception reception = Reception.builder()
        .reservation(reservation)
        .member(member)
        .doctor(doctor)
        .receptionNo(receptionNo)
        .type(ReceptionType.RESERVATION)   // 예약 기반 접수
        .status(ReceptionStatus.WAITING)
        .noteToDoctor(request.getNoteToDoctor())
        .consentNotice(request.getConsentNotice())
        .consentAt(now)
        .build();
    receptionRepository.save(reception);

    // 10) Symptoms 연결 (DIRECT 로직 복붙 + req 타입만 변경)
    List<Long> ids = request.getSymptomIds().stream().distinct().toList();
    List<Symptom> symptoms = symptomRepository.findAllById(ids);
    if (symptoms.size() != ids.size()) {
      throw new GlobalException(ErrorStatus.RECEPTION_INVALID_SYMPTOM);
    }
    List<ReceptionSymptom> links = symptoms.stream()
        .map(s -> ReceptionSymptom.builder()
            .reception(reception)
            .symptom(s)
            .createdAt(now)
            .build())
        .toList();
    receptionSymptomRepository.saveAll(links);

    // 11) 필요하면 예약 상태 변경 (예: CHECKED_IN 등)
    // reservation.updateStatus(ReservationStatus.CHECKED_IN);

    return reception.getReceptionId();
  }

  // ===== util =====

  private String generateReceptionNo() {
    String prefix = "RCN-" + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE) + "-";
    for (int i = 0; i < 20; i++) {
      String candidate = prefix + String.format("%04d", new Random().nextInt(10000));
      if (!receptionRepository.existsByReceptionNo(candidate)) return candidate;
    }
    throw new GlobalException(ErrorStatus.RECEPTION_NO_GENERATION_FAILED);
  }

// 대기표 관련 Util (추후 대기표 기능 도입 시 활성화)
//  private int assignQueueNoForToday(Long doctorId) {
//    LocalDate today = LocalDate.now();
//    LocalDateTime start = today.atStartOfDay();
//    LocalDateTime end = today.plusDays(1).atStartOfDay();
//    int max = waitingTicketRepository.findTodayMaxQueueNo(doctorId, start, end);
//    return max + 1;
//  }

//  private static class Range {
//    final LocalDateTime from, to;
//
//    Range(LocalDateTime f, LocalDateTime t) {
//      this.from = f;
//      this.to = t;
//    }
//  }

//  private Range toRange(String month, String from, String to) {
//    if (month != null && !month.isBlank()) {
//      var ym = java.time.YearMonth.parse(month);
//      return new Range(ym.atDay(1).atStartOfDay(), ym.atEndOfMonth().plusDays(1).atStartOfDay());
//    }
//    LocalDateTime f = (from == null || from.isBlank()) ? null : LocalDate.parse(from).atStartOfDay();
//    LocalDateTime t = (to == null || to.isBlank()) ? null : LocalDate.parse(to).plusDays(1).atStartOfDay();
//    return new Range(f, t);
//  }
}
