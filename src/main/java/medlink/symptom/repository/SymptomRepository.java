package medlink.symptom.repository;

import medlink.symptom.entity.Symptom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SymptomRepository extends JpaRepository<Symptom, Long> {
  List<Symptom> findAllByOrderBySymptomIdAsc();
}
