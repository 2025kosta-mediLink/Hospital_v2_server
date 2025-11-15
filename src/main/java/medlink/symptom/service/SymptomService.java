package medlink.symptom.service;

import lombok.RequiredArgsConstructor;
import medlink.symptom.dto.SymptomResponse;
import medlink.symptom.repository.SymptomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SymptomService {
  private final SymptomRepository symptomRepository;

  public List<SymptomResponse> getSymptoms() {
    return symptomRepository.findAllByOrderBySymptomIdAsc()
        .stream()
        .map(SymptomResponse::from)
        .toList();
  }
}
