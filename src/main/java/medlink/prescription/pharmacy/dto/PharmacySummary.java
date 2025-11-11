package medlink.prescription.pharmacy.dto;

/**
 * 약국 정보 DTO (외부 API 응답을 단순 전달하는 목적).
 */
public record PharmacySummary(
        String pharmacyId,
        String name,
        String address,
        String phoneNumber,
        Double latitude,
        Double longitude,
        Double distanceMeters,
        String operatingHours,
        Boolean open,
        Double rating,
        String status
) {
}

 