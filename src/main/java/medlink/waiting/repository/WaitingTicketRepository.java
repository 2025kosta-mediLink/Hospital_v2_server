package medlink.waiting.repository;

import medlink.waiting.entity.WaitingTicket;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface WaitingTicketRepository extends JpaRepository<WaitingTicket, Long> {

    Optional<WaitingTicket> findTopByReception_ReceptionIdOrderByTicketIdDesc(Long receptionId);

    // 오늘 날짜 범위의 최대 queue_no 조회 (동시성 최소화용)
    @Query(value = """
      select coalesce(max(w.queue_no),0) from waiting_ticket w
      join reception r on r.reception_id = w.reception_id
      where r.doctor_id = :doctorId
        and w.created_at >= :start and w.created_at < :end
      """, nativeQuery = true)
    int findTodayMaxQueueNo(@Param("doctorId") Long doctorId,
                            @Param("start") LocalDateTime start,
                            @Param("end") LocalDateTime end);
}
