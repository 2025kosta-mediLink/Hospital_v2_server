package medlink.prescription.pharmacy.controller;

import lombok.RequiredArgsConstructor;
import medlink.prescription.pharmacy.dto.PharmacySummary;
import medlink.prescription.pharmacy.service.PharmacyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/prescriptions/pharmacies")
public class PharmacyApiController {

    private final PharmacyService pharmacyService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> search(
            @RequestParam(defaultValue = "37.5665") double latitude,
            @RequestParam(defaultValue = "126.9780") double longitude,
            @RequestParam(defaultValue = "2000") int radius
    ) {
        List<PharmacySummary> items = pharmacyService.searchNearby(latitude, longitude, radius);
        return ResponseEntity.ok(Map.of(
                "center", Map.of("latitude", latitude, "longitude", longitude),
                "radius", radius,
                "items", items
        ));
    }

    @GetMapping("/{pharmacyId}")
    public ResponseEntity<PharmacySummary> detail(@PathVariable String pharmacyId) {
        return pharmacyService.getDetail(pharmacyId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_IMPLEMENTED,
                        "약국 상세 정보는 외부 API에서 직접 조회하세요."
                ));
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> send(
            @RequestBody Map<String, Object> payload
    ) {
        String pharmacyId = (String) payload.get("pharmacyId");
        @SuppressWarnings("unchecked")
        List<Integer> prescriptions = (List<Integer>) payload.getOrDefault("prescriptionIds", List.of());

        var ids = prescriptions.stream()
                .map(Integer::longValue)
                .toList();

        String dispensingId = pharmacyService.sendPrescription(pharmacyId, ids);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "dispensingId", dispensingId
        ));
    }
}

 