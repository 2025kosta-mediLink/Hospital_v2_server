package medlink.reception.repository;

import medlink.reception.entity.Reception;
import medlink.reception.enums.ReceptionStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReceptionRepository extends JpaRepository<Reception, Long> {

  boolean existsByReceptionNo(String receptionNo);

  boolean existsByReservation_ReservationId(Long reservationId);

  @EntityGraph(attributePaths = {"member","doctor","doctor.department"})
  Optional<Reception> findById(Long id);

  @Query("""
      select r from Reception r
      join r.member m
      where m.uuid = :uuid
        and (:status is null or r.status = :status)
        and (:from is null or r.createdAt >= :from)
        and (:to   is null or r.createdAt <  :to)
      order by r.createdAt desc, r.receptionId desc
    """)
  List<Reception> searchMine(@Param("uuid") String uuid,
                             @Param("status") ReceptionStatus status,
                             @Param("from") LocalDateTime from,
                             @Param("to") LocalDateTime to);
}
