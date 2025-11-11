package medlink.prescription.prescription.controller;

import lombok.RequiredArgsConstructor;
import medlink.prescription.prescription.dto.PrescriptionStatusUpdateRequest;
import medlink.prescription.prescription.dto.PrescriptionSummary;
import medlink.prescription.prescription.service.PrescriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 처방전 REST API 컨트롤러.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/prescriptions")
public class PrescriptionApiController {

    private final PrescriptionService prescriptionService;

    @GetMapping
    public ResponseEntity<List<PrescriptionSummary>> getPrescriptions(
            @RequestParam(required = false) Long memberId
    ) {
        return ResponseEntity.ok(prescriptionService.getPrescriptions(memberId));
    }

    @PatchMapping("/{prescriptionId}/status")
    public ResponseEntity<Map<String, Boolean>> updateStatus(
            @PathVariable Long prescriptionId,
            @RequestBody PrescriptionStatusUpdateRequest request
    ) {
        prescriptionService.updateStatus(prescriptionId, request);
        return ResponseEntity.ok(Map.of("success", Boolean.TRUE));
    }
}


