package medlink.reception;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import medlink.common.exception.ErrorStatus;
import medlink.common.exception.GlobalException;
import medlink.doctor.entity.Doctor;
import medlink.doctor.repository.DoctorRepository;
import medlink.member.entity.Member;
import medlink.member.repository.MemberRepository;
import medlink.reception.dto.request.ReceptionCreateRequest;
import medlink.reception.dto.response.ReceptionDetailResponse;
import medlink.reception.dto.response.ReceptionIdResponse;
import medlink.reception.dto.response.ReceptionListItemResponse;
import medlink.reception.entity.Reception;
import medlink.reception.entity.ReceptionSymptom;
import medlink.reception.enums.ReceptionStatus;
import medlink.reception.enums.ReceptionType;
import medlink.reception.repository.ReceptionRepository;
import medlink.reception.repository.ReceptionSymptomRepository;
import medlink.symptom.entity.Symptom;
import medlink.symptom.repository.SymptomRepository;
import medlink.waiting.entity.WaitingTicket;
import medlink.waiting.enums.WaitingTicketStatus;
import medlink.waiting.repository.WaitingTicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private final EntityManager em;

    /**
     * 접수 생성
     */
    @Transactional
    public ReceptionIdResponse createReception(String uuid, ReceptionCreateRequest req) {
        if (Boolean.FALSE.equals(req.getConsentNotice())) {
            throw new GlobalException(ErrorStatus.BAD_REQUEST, "consentNotice required");
        }

        Member member = memberRepository.findByUuid(uuid)
                .orElseThrow(() -> new GlobalException(ErrorStatus.UNAUTHORIZED, "member not found"));

        Doctor doctor = doctorRepository.findById(req.getDoctorId())
                .orElseThrow(() -> new GlobalException(ErrorStatus.NOT_FOUND, "doctor not found"));

        // Reception
        Reception reception = Reception.builder()
                .member(member)
                .doctor(doctor)
                .receptionNo(generateReceptionNo())
                .type(ReceptionType.DIRECT)
                .status(ReceptionStatus.WAITING)
                .consentNotice(true)
                .consentAt(LocalDateTime.now())
                .noteToDoctor(req.getNoteToDoctor())
                .build();
        receptionRepository.save(reception);

        // Symptoms
        List<Long> ids = req.getSymptomIds().stream().distinct().toList();
        List<Symptom> symptoms = symptomRepository.findAllById(ids);
        if (symptoms.size() != ids.size()) {
            throw new GlobalException(ErrorStatus.BAD_REQUEST, "invalid symptomIds");
        }
        List<ReceptionSymptom> links = symptoms.stream()
                .map(s -> ReceptionSymptom.builder()
                        .reception(reception)
                        .symptom(s)
                        .createdAt(LocalDateTime.now())
                        .build())
                .toList();
        receptionSymptomRepository.saveAll(links);

        // Waiting ticket (policy)
        WaitingTicket ticket = WaitingTicket.builder()
                .reception(reception)
                .queueNo(assignQueueNoForToday(doctor.getDoctorId()))
                .status(WaitingTicketStatus.WAITING)
                .build();
        waitingTicketRepository.save(ticket);

        return ReceptionIdResponse.of(reception.getReceptionId());
    }

    /** 접수 상세 */
    @Transactional(readOnly = true)
    public ReceptionDetailResponse getReceptionDetail(Long id) {
        Reception r = receptionRepository.findById(id)
                .orElseThrow(() -> new GlobalException(ErrorStatus.NOT_FOUND, "reception not found"));
        var symptoms = receptionSymptomRepository.findSymptomNames(id);
        var ticketOpt = waitingTicketRepository.findTopByReception_ReceptionIdOrderByTicketIdDesc(id);
        return ReceptionDetailResponse.from(r, symptoms, ticketOpt);
    }

    /** 내 접수 목록 */
    @Transactional(readOnly = true)
    public List<ReceptionListItemResponse> getMyReceptions(String uuid, String status, String month, String from, String to) {
        var range = toRange(month, from, to);
        return receptionRepository.searchMine(
                        uuid,
                        status == null ? "ALL" : status,
                        range.from, range.to)
                .stream()
                .map(ReceptionListItemResponse::from)
                .toList();
    }


    /** 접수 취소 */
    @Transactional
    public void cancelReception(Long id, String uuid, String reason) {
        Reception r = receptionRepository.findById(id)
                .orElseThrow(() -> new GlobalException(ErrorStatus.NOT_FOUND, "reception not found"));

        if (!uuid.equals(r.getMember().getUuid())) {
            throw new GlobalException(ErrorStatus.FORBIDDEN, "not owner");
        }
        if (r.getStatus() == ReceptionStatus.DONE || r.getStatus() == ReceptionStatus.CANCELLED) {
            throw new GlobalException(ErrorStatus.BAD_REQUEST, "already finalized");
//            throw new GlobalException(ErrorStatus.CONFLICT, "already finalized");
        }
        // (옵션) 처방 존재 여부 검사: PrescriptionRepository.existsByReception_ReceptionId(id)

        r.setStatus(ReceptionStatus.CANCELLED);

        waitingTicketRepository.findTopByReception_ReceptionIdOrderByTicketIdDesc(id)
                .ifPresent(t -> {
                    switch (t.getStatus()) {
                        case WAITING, CALLED -> t.setStatus(WaitingTicketStatus.CANCELLED);
                        default -> {}
                    }
                });

        // (옵션) 알림 기록 등
    }

    // ===== util =====

    private String generateReceptionNo() {
        String prefix = "RCN-" + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE) + "-";
        for (int i = 0; i < 20; i++) {
            String candidate = prefix + String.format("%04d", new Random().nextInt(10000));
            if (!receptionRepository.existsByReceptionNo(candidate)) return candidate;
        }
//        throw new GlobalException(ErrorStatus.SERVER_ERROR, "cannot generate receptionNo");
        throw new GlobalException(ErrorStatus.BAD_REQUEST, "cannot generate receptionNo");
    }

    private int assignQueueNoForToday(Long doctorId) {
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.plusDays(1).atStartOfDay();
        int max = waitingTicketRepository.findTodayMaxQueueNo(doctorId, start, end);
        return max + 1;
    }

    private static class Range { final LocalDateTime from, to; Range(LocalDateTime f, LocalDateTime t){ this.from=f; this.to=t; } }
    private Range toRange(String month, String from, String to) {
        if (month != null && !month.isBlank()) {
            var ym = java.time.YearMonth.parse(month);
            return new Range(ym.atDay(1).atStartOfDay(), ym.atEndOfMonth().plusDays(1).atStartOfDay());
        }
        LocalDateTime f = (from == null || from.isBlank()) ? null : LocalDate.parse(from).atStartOfDay();
        LocalDateTime t = (to   == null || to.isBlank())   ? null : LocalDate.parse(to).plusDays(1).atStartOfDay();
        return new Range(f, t);
    }
}
