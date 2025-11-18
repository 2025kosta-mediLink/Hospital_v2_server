package medlink.pharmacy.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PharmacySendRequest {
	private String pharmacyId;
	private String pharmacyName;
	private List<Long> prescriptionIds;
}