package medlink.prescription.service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import medlink.prescription.dto.request.PrescriptionStatusUpdateRequest;
import medlink.prescription.dto.response.PrescriptionResponse;
import medlink.prescription.repository.PrescriptionJdbcRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PrescriptionService {

    private final PrescriptionJdbcRepository prescriptionJdbcRepository;

    @Transactional(readOnly = true)
    public List<PrescriptionResponse> getPrescriptions(Long memberId) {
        if (memberId == null) {
            throw new IllegalArgumentException("memberId must not be null");
        }
        return prescriptionJdbcRepository.findByMemberId(memberId);
    }

    @Transactional
    public void updateStatus(Long prescriptionId, PrescriptionStatusUpdateRequest request) {
        if (prescriptionId == null) {
            throw new IllegalArgumentException("prescriptionId must not be null");
        }
        prescriptionJdbcRepository.updateStatus(prescriptionId, request);
    }
}


