package medlink.dispensing.controller;

import lombok.RequiredArgsConstructor;
import medlink.dispensing.dto.response.DispensingResponse;
import medlink.dispensing.service.DispensingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/prescriptions/dispensing")
public class DispensingApiController {

    private final DispensingService dispensingService;

    @GetMapping("/{dispensingId}")
    public ResponseEntity<DispensingResponse> getStatus(@PathVariable String dispensingId) {
        return ResponseEntity.ok(dispensingService.getStatus(dispensingId));
    }

    @PostMapping("/{dispensingId}")
    public ResponseEntity<Map<String, Boolean>> complete(
            @PathVariable String dispensingId,
            @RequestBody Map<String, String> payload
    ) {
        try {
            LocalDateTime receivedAt;
            if (payload != null && payload.containsKey("receivedAt") && payload.get("receivedAt") != null) {
                String receivedAtString = payload.get("receivedAt");
                // ISO 형식 (예: 2024-01-01T12:00:00.000Z) 또는 일반 형식 파싱
                try {
                    receivedAt = LocalDateTime.parse(receivedAtString.replace("Z", "").replace("z", ""));
                } catch (Exception e) {
                    // 파싱 실패 시 현재 시간 사용
                    receivedAt = LocalDateTime.now();
                }
            } else {
                receivedAt = LocalDateTime.now();
            }
            
            dispensingService.completeReceipt(dispensingId, receivedAt);
            return ResponseEntity.ok(Map.of("success", Boolean.TRUE));
        } catch (Exception e) {
            throw new RuntimeException("수령 완료 처리 중 오류 발생: " + e.getMessage(), e);
        }
    }
}


