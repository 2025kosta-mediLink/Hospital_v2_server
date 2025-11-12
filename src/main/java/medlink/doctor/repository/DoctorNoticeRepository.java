package medlink.doctor.repository;

import medlink.doctor.entity.DoctorNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DoctorNoticeRepository extends JpaRepository<DoctorNotice, Long> {

    /** 현재 시간 기준, 공지 시작일 ~ 종료일 사이에 해당하는 의사 공지 조회 (우선 순위로 정렬) */
    @Query("SELECT dn FROM DoctorNotice dn WHERE dn.doctor.doctorId = :doctorId " +
            "AND dn.startsAt <= :currentTime AND dn.endsAt >= :currentTime " +
            "ORDER BY dn.priority")
    List<DoctorNotice> findActiveNoticesByDoctorId(Long doctorId, LocalDateTime currentTime);
}
