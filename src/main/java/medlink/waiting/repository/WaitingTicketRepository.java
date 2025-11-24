package medlink.waiting.repository;

import medlink.waiting.entity.WaitingTicket;
import medlink.waiting.enums.WaitingTicketStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface WaitingTicketRepository extends JpaRepository<WaitingTicket, Long> {

  Optional<WaitingTicket> findTopByReception_ReceptionIdOrderByTicketIdDesc(Long receptionId);

  @Query(value = """
      select coalesce(max(w.queue_no),0) from waiting_ticket w
      join reception r on r.reception_id = w.reception_id
      where r.doctor_id = :doctorId
        and w.created_at >= :start and w.created_at < :end
      """, nativeQuery = true)
  int findTodayMaxQueueNo(@Param("doctorId") Long doctorId,
                          @Param("start") LocalDateTime start,
                          @Param("end") LocalDateTime end);

  // 오늘의 대기 목록 조회
  @Query("""
        select w from WaitingTicket w
        join fetch w.reception r
        join fetch r.member m
        where r.doctor.doctorId = :doctorId
          and w.createdAt >= :start
          and w.createdAt < :end
        order by w.queueNo asc
    """)
  List<WaitingTicket> findTodayWaitingList(@Param("doctorId") Long doctorId,
                                           @Param("start") LocalDateTime start,
                                           @Param("end") LocalDateTime end);

  // 다음 대기자 조회 (WAITING 상태, 가장 작은 queueNo)
  @Query("""
        select w from WaitingTicket w
        join fetch w.reception r
        where r.doctor.doctorId = :doctorId
          and w.status = 'WAITING'
          and w.createdAt >= :start
        order by w.queueNo asc
        limit 1
    """)
  Optional<WaitingTicket> findNextWaitingTicket(@Param("doctorId") Long doctorId,
                                                @Param("start") LocalDateTime start);

  // 현재 대기 인원 카운트
  @Query("""
        select count(w) from WaitingTicket w
        join w.reception r
        where r.doctor.doctorId = :doctorId
          and w.queueNo < :currentQueueNo
          and w.status = 'WAITING'
          and w.createdAt >= :start
    """)
  int countWaitingBefore(@Param("doctorId") Long doctorId,
                         @Param("currentQueueNo") Integer currentQueueNo,
                         @Param("start") LocalDateTime start);
}