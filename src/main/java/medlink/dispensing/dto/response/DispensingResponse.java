package medlink.dispensing.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 조제 현황 DTO.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class DispensingResponse {

    private String dispensingId;
    private String pharmacyName;
    private String pharmacyAddress;
    private String pharmacyPhone;
    private double pharmacyLatitude;
    private double pharmacyLongitude;
    private String status;
    private String dispenserName;
    private String receivedAt;
    private String estimatedCompletionTime;
    private String completedAt;
    private String prescriptionDetails;
    private String qrCode;

    public static DispensingResponse from(Row row) {
        return DispensingResponse.builder()
                .dispensingId(row.dispensingId())
                .pharmacyName(row.pharmacyName())
                .pharmacyAddress(row.pharmacyAddress())
                .pharmacyPhone(row.pharmacyPhone())
                .pharmacyLatitude(row.pharmacyLatitude())
                .pharmacyLongitude(row.pharmacyLongitude())
                .status(row.status())
                .dispenserName(row.dispenserName())
                .receivedAt(row.receivedAt())
                .estimatedCompletionTime(row.estimatedCompletionTime())
                .completedAt(row.completedAt())
                .prescriptionDetails(row.prescriptionDetails())
                .qrCode(row.qrCode())
                .build();
    }

    public record Row(
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
    ) {}
}


