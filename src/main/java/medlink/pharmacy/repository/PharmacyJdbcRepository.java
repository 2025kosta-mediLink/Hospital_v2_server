package medlink.pharmacy.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PharmacyJdbcRepository {

	private final NamedParameterJdbcTemplate jdbcTemplate;

	/**
	 * pharmacy_prescription 레코드를 생성하고 생성된 ID를 반환한다.
	 */
	public Long createPharmacyPrescription(String pharmacyId, String pharmacyName, Long prescriptionId, String status) {
		String sql = """
			INSERT INTO pharmacy_prescription 
			(pharmacy_id, prescription_id, pharmacy_name, status, created_at, updated_at)
			VALUES 
			(:pharmacyId, :prescriptionId, :pharmacyName, :status, NOW(), NOW())
			""";

		Long pharmacyIdLong = null;
		if (pharmacyId != null && !pharmacyId.isEmpty()) {
			try {
				pharmacyIdLong = Long.parseLong(pharmacyId);
			} catch (NumberFormatException e) {
				log.warn("Invalid pharmacyId format: {}, using NULL", pharmacyId);
			}
		}

		MapSqlParameterSource params = new MapSqlParameterSource()
			.addValue("pharmacyId", pharmacyIdLong)
			.addValue("pharmacyName", pharmacyName)
			.addValue("prescriptionId", prescriptionId)
			.addValue("status", status);

		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(sql, params, keyHolder, new String[]{"pharmacy_prescription_id"});

		Number key = keyHolder.getKey();
		if (key == null) {
			throw new RuntimeException("Failed to create pharmacy_prescription record");
		}

		return key.longValue();
	}
}

