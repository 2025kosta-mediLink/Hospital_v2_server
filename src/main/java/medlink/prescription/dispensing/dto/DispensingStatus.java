package medlink.prescription.dispensing.dto;

/**
 * 조제 현황 DTO.
 */
public record DispensingStatus(
        String dispensingId,
        String pharmacyName,
        String pharmacyAddress,
        String pharmacyPhone,
        double pharmacyLatitude,
        double pharmacyLongitude,
        String status,
        String dispenserName,
        String receivedAt,
        String estimatedCompletionTime,
        String completedAt,
        String prescriptionDetails,
        String qrCode
) {
}


