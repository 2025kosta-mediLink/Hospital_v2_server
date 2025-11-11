package medlink.prescription.dispensing.service;

import java.time.LocalDateTime;

import lombok.RequiredArgsConstructor;
import medlink.prescription.dispensing.dto.DispensingStatus;
import medlink.prescription.dispensing.repository.DispensingJdbcRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DispensingService {

    private final DispensingJdbcRepository dispensingJdbcRepository;

    @Transactional(readOnly = true)
    public DispensingStatus getStatus(String dispensingId) {
        return dispensingJdbcRepository.findById(dispensingId)
                .orElseThrow(() -> new IllegalArgumentException("조제 정보를 찾을 수 없습니다. dispensingId=" + dispensingId));
    }

    @Transactional
    public void completeReceipt(String dispensingId, LocalDateTime receivedAt) {
        LocalDateTime completedAt = receivedAt != null ? receivedAt : LocalDateTime.now();
        dispensingJdbcRepository.markCompleted(dispensingId, completedAt);
    }
}


