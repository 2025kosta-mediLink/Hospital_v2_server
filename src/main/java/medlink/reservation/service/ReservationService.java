package medlink.reservation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import medlink.common.exception.ErrorStatus;
import medlink.common.exception.GlobalException;
import medlink.doctor.entity.Doctor;
import medlink.doctor.entity.DoctorWeeklySchedule;
import medlink.doctor.service.DoctorService;
import medlink.member.entity.Member;
import medlink.member.service.MemberService;
import medlink.reservation.dto.request.ReservationRequest;
import medlink.reservation.dto.response.ReservationListResponse;
import medlink.reservation.dto.response.ReservationResponse;
import medlink.reservation.dto.response.ReservationTimesResponse;
import medlink.reservation.dto.response.TodayReservationListResponse;
import medlink.reservation.entity.Reservation;
import medlink.reservation.enums.ReservationStatus;
import medlink.reservation.repository.ReservationRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationService {

  private final DoctorService doctorService;
  private final MemberService memberService;
  private final ReservationRepository reservationRepository;

  private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("H:mm");
  private static final LocalTime AM_START = LocalTime.of(9, 0);
  private static final LocalTime AM_END = LocalTime.of(12, 0);
  private static final LocalTime PM_START = LocalTime.of(13, 0);
  private static final LocalTime PM_END = LocalTime.of(18, 0);

  @Transactional(readOnly = true)
  public ReservationTimesResponse getAvailableReservationTimes(Long doctorId, LocalDate date) {
    // date가 오늘 ~ 한 달 후에 해당하는지 검증
    validateDateInOneMonth(date);

    Doctor doctor = doctorService.getDoctorById(doctorId);

    // 1) 의사 휴일 체크 - 휴진일이면 빈 리스트(null) 반환
    if (doctorService.isDoctorExceptionDay(doctor, date)) {
      log.info("Doctor {} is on exception day at {}", doctorId, date);
      return ReservationTimesResponse.of(null, null);
    }

    // 2) 주어진 날짜의 요일을 기반으로 스케줄 조회 - 스케줄 없으면 빈 리스트 반환
    int dayOfWeek = date.getDayOfWeek().getValue(); // 1~7 (월~일)
    Optional<DoctorWeeklySchedule> scheduleOpt =
        doctorService.findDoctorWeeklyScheduleByDayOfWeek(doctor, dayOfWeek);

    if (scheduleOpt.isEmpty()) {
      log.info("Doctor {} has no schedule on day {}", doctorId, dayOfWeek);
      return ReservationTimesResponse.of(null, null);
    }

    DoctorWeeklySchedule schedule = scheduleOpt.get();

    // 3) 예약된 시간 가져오기
    Set<LocalTime> reservedTimes = getReservedTimes(doctor, date);

    // 4) 오늘인 경우, 현재 시간 이후로만 예약 가능하게 판단할 기준
    // makeAvailableReservationSlots 에서 하지 않은 이유는 am,pm 에서 공통으로 쓰이기 때문
    boolean isToday = date.isEqual(LocalDate.now());
    LocalTime now = LocalTime.now();
    LocalTime todayNow = isToday ? now : null;

    // 5) 가능한 시간대 생성 (오전 9:00~11:30, 오후 13:00~17:30)
    List<String> availableAmTimes = schedule.isAmFlag() ?
        makeAvailableReservationSlots(
            AM_START, AM_END, reservedTimes, todayNow)
        : null;
    List<String> availablePmTimes = schedule.isPmFlag() ?
        makeAvailableReservationSlots(
            PM_START, PM_END, reservedTimes, todayNow)
        : null;

    return ReservationTimesResponse.of(availableAmTimes, availablePmTimes);
  }

  @Transactional
  public Long createReservation(ReservationRequest request, String uuid) {
    // 초/나노 정리
    LocalDateTime appointmentAt = request.getReservationTime()
        .withSecond(0)
        .withNano(0);

    Member member = memberService.getMemberByUuid(uuid);
    Doctor doctor = doctorService.getDoctorById(request.getDoctorId());

    // 2) 이미 예약된 시간인지 체크
    boolean exists = reservationRepository.existsByDoctorAndAppointmentAtAndStatus(
        doctor,
        appointmentAt,
        ReservationStatus.RESERVED
    );
    if (exists) {
      throw new GlobalException(ErrorStatus.RESERVATION_TIME_ALREADY_RESERVED);
    }
    // DB 유니크 제약 걸어놔서, 겹치면 에러
    String reservationNo = makeReservationNo();

    // 3) 예약 엔티티 생성 & 저장
    Reservation reservation = Reservation.of(
        doctor,
        member,
        reservationNo,
        appointmentAt,
        ReservationStatus.RESERVED
    );

    Reservation saved = reservationRepository.save(reservation);
    return saved.getReservationId();
  }

  @Transactional(readOnly = true)
  public ReservationResponse getReservation(Long reservationId, String uuid) {
    Reservation reservation = reservationRepository.findById(reservationId)
        .orElseThrow(() -> new GlobalException(ErrorStatus.RESERVATION_NOT_FOUND));

    // 예약한 사용자와 조회하는 사용자가 같은지 검증
    if (!reservation.getMember().getUuid().equals(uuid)) {
      throw new GlobalException(ErrorStatus.RESERVATION_ACCESS_FORBIDDEN);
    }
    return ReservationResponse.from(reservation);
  }

  @Transactional(readOnly = true)
  public List<ReservationListResponse> getReservationList(
      String uuid,
      Integer year,
      Integer month,
      ReservationStatus status
  ) {
    Member member = memberService.getMemberByUuid(uuid);

    // 1) year/month 로 월 범위 설정
    LocalDateTime startAt = null;
    LocalDateTime endAt = null;
    if (year != null && month != null) {
      LocalDate startDate = LocalDate.of(year, month, 1);
      LocalDate endDate = startDate.plusMonths(1);  // 다음 달 1일
      startAt = startDate.atStartOfDay();
      endAt = endDate.atStartOfDay();
    }

    List<Reservation> reservations =
        reservationRepository.findAllByMemberAndFilters(
            member, startAt, endAt, status,
            Sort.by(Sort.Direction.DESC, "appointmentAt"));

    if (reservations.isEmpty()) {
      throw new GlobalException(ErrorStatus.RESERVATION_NOT_REGISTERED);
    }

    return reservations.stream()
        .map(ReservationListResponse::from)
        .toList();
  }


  /**
   * 해당 날짜에 예약된 시간대 조회
   */
  @Transactional(readOnly = true)
  protected Set<LocalTime> getReservedTimes(Doctor doctor, LocalDate date) {
    // 오늘 예약된 시간대 가져오기
    List<Reservation> reservations = reservationRepository.findAllByDoctorAndDateAndStatus(
        doctor, date.atStartOfDay(), date.plusDays(1).atStartOfDay(), ReservationStatus.RESERVED);

    if (reservations.isEmpty()) return Collections.emptySet();

    Set<LocalTime> reservedTimeSet = new HashSet<>(reservations.size());
    reservations.forEach(reservation ->
        reservedTimeSet.add(reservation.getAppointmentAt().toLocalTime()));

    return reservedTimeSet;
  }

  @Transactional(readOnly = true)
  public List<TodayReservationListResponse> getTodayReservations(String uuid) {
    Member member = memberService.getMemberByUuid(uuid);

    LocalDate today = LocalDate.now();  // 서버 기준 오늘
    LocalDateTime startAt = today.atStartOfDay();
    LocalDateTime endAt = today.plusDays(1).atStartOfDay();

    List<Reservation> reservations =
        reservationRepository.findAllByMemberAndFilters(
            member,
            startAt,
            endAt,
            ReservationStatus.RESERVED,
            Sort.by(Sort.Direction.ASC, "appointmentAt")
        );

    if (reservations.isEmpty()) {
      throw new GlobalException(ErrorStatus.RESERVATION_NOT_REGISTERED);
    }

    return reservations.stream()
        .map(TodayReservationListResponse::from)
        .toList();
  }

  @Transactional
  public void cancelReservation(Long reservationId, String uuid) {
    // 1) 로그인 회원 조회
    Member member = memberService.getMemberByUuid(uuid);

    // 2) 예약 조회
    Reservation reservation = reservationRepository.findById(reservationId)
        .orElseThrow(() -> new GlobalException(ErrorStatus.RESERVATION_NOT_FOUND));

    // 3) 내 예약인지 검증 (memberId 기준)
    if (!reservation.getMember().getMemberId().equals(member.getMemberId())) {
      throw new GlobalException(ErrorStatus.RESERVATION_ACCESS_DENIED);
    }

    // 4) 상태 검사: RESERVED 인 경우에만 취소 가능
    if (reservation.getStatus() != ReservationStatus.RESERVED) {
      throw new GlobalException(ErrorStatus.RESERVATION_CANNOT_CANCEL);
    }

    // 5) 취소 처리
    Reservation canceledReservation = Reservation.updateStatus(
        reservation, ReservationStatus.CANCELLED);
    reservationRepository.save(canceledReservation);
  }

  /**
   * 예약된 시간을 제외한 가능한 30분 간격 슬롯을 생성
   */
  // 항상 startTime, endTime 은 같은 날짜 내에 있다고 가정
  private List<String> makeAvailableReservationSlots(
      LocalTime startTime, LocalTime endTime,
      Set<LocalTime> reservedTimes, LocalTime todayNow) {
    List<String> availableSlots = new ArrayList<>();
    LocalTime cursor = startTime;
    // 오늘인 경우, 현재 시간 이후의 '다음 30분 눈금'를 cursor로 설정
    // -> 불필요한 계산 방지
    if (todayNow != null && cursor.isBefore(todayNow)) {
      cursor = roundUpToNext30(todayNow);
    }
    // cursor가 endTime보다 이전 시간인지 비교
    // => startTime ~ endTime (endTime은 포함하지 않음)
    while (cursor.isBefore(endTime)) {
      // 예약된 시간이 없거나(모두 추가), 예약된 시간에 포함되지 않는 경우에 추가
      if (reservedTimes.isEmpty() || !reservedTimes.contains(cursor)) {
        availableSlots.add(cursor.format(TIME_FMT));
      }
      cursor = cursor.plusMinutes(30); // 30분 간격으로 증가
    }
    return availableSlots.isEmpty() ? null : availableSlots;
  }

  /**
   * 주어진 시간 t를 기준으로, 다음 30분 눈금으로 올림
   */
  private static LocalTime roundUpToNext30(LocalTime t) {
    int m = t.getMinute();
    // 다음 30분 그리드까지 더해야 할 분 계산
    // - 1~29분이면 (30 - m)분 추가 → 예: 10:07 → +23분(=add) → 10:30
    int add = (m == 0 || m == 30) ? 0 : (m < 30 ? 30 - m : 60 - m);
    // 초, 나노초는 0으로 맞춤
    return t.plusMinutes(add).withSecond(0).withNano(0);
  }

  /**
   * date가 오늘부터 한 달 후 사이에 있는지 검증
   */
  private void validateDateInOneMonth(LocalDate date) {
    LocalDate today = LocalDate.now();
    LocalDate oneMonthLater = today.plusMonths(1);

    if (date.isBefore(today) || date.isAfter(oneMonthLater)) {
      throw new GlobalException(ErrorStatus.RESERVATION_DATE_OUT_OF_RANGE);
    }
  }

  /**
   * 예약번호 생성: RES-YYYYMMDD-####
   */
  private String makeReservationNo() {
    return "RES-" + LocalDate.now().toString().replace("-", "")
        + "-" + String.format("%04d", new Random().nextInt(10000));
  }
}
