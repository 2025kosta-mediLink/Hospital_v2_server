package medlink.pharmacy.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 약국 정보 DTO (외부 API 응답을 단순 전달하는 목적).
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class PharmacyResponse {

    private String pharmacyId;
    private String name;
    private String address;
    private String phoneNumber;
    private Double latitude;
    private Double longitude;
    private Double distanceMeters;
    private String operatingHours;
    private Boolean open;
    private Double rating;
    private String status;

    public static PharmacyResponse from(Row row) {
        return PharmacyResponse.builder()
                .pharmacyId(row.pharmacyId())
                .name(row.name())
                .address(row.address())
                .phoneNumber(row.phoneNumber())
                .latitude(row.latitude())
                .longitude(row.longitude())
                .distanceMeters(row.distanceMeters())
                .operatingHours(row.operatingHours())
                .open(row.open())
                .rating(row.rating())
                .status(row.status())
                .build();
    }

    public record Row(
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
    ) {}
}


