package medlink.reception.repository;

import medlink.reception.entity.ReceptionSymptom;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReceptionSymptomRepository extends JpaRepository<ReceptionSymptom, Long> {
    @Query("""
      select rs.symptom.name from ReceptionSymptom rs
      where rs.reception.receptionId = :id
    """)
    List<String> findSymptomNames(@Param("id") Long receptionId);
}