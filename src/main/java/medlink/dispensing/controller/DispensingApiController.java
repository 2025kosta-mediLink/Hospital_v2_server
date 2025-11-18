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
@RequestMapping("/api/prescriptions/dispensing")
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
        String receivedAtString = payload.getOrDefault("receivedAt", LocalDateTime.now().toString());
        dispensingService.completeReceipt(dispensingId, LocalDateTime.parse(receivedAtString));
        return ResponseEntity.ok(Map.of("success", Boolean.TRUE));
    }
}


