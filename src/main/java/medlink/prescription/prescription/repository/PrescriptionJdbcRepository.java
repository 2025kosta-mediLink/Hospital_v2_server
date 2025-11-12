package medlink.prescription.prescription.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import medlink.prescription.prescription.dto.PrescriptionStatusUpdateRequest;
import medlink.prescription.prescription.dto.PrescriptionSummary;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PrescriptionJdbcRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @SuppressWarnings("NullableProblems")
    public List<PrescriptionSummary> findByMemberId(Long memberId) {
        String sql = """
                SELECT 
                    p.prescription_id,
                    dep.name AS department_name,
                    doc.name AS doctor_name,
                    p.issued_at AS treatment_date,
                    COALESCE(pp.status, 'START') AS status,
                    COALESCE(pp.assigned_pharmacist, p.pharmacy_name) AS pharmacy_name,
                    COALESCE(ph.pickup_at, p.completed_date) AS completed_date,
                    p.completed,
                    CASE 
                        WHEN p.completed = true THEN FALSE
                        WHEN pp.status IS NULL OR pp.status = 'START' THEN TRUE
                        ELSE FALSE
                    END AS can_select,
                    p.created_at,
                    p.updated_at
                FROM prescription p
                JOIN reception r ON p.reception_id = r.reception_id
                JOIN doctor doc ON r.doctor_id = doc.doctor_id
                JOIN department dep ON doc.department_id = dep.department_id
                LEFT JOIN pharmacy_prescription pp ON p.prescription_id = pp.prescription_id
                LEFT JOIN pickup_history ph ON pp.pharmacy_prescription_id = ph.pharmacy_prescription_id
                WHERE r.member_id = :memberId
                    AND r.status = 'DONE'
                ORDER BY p.issued_at DESC
                """;

        return jdbcTemplate.query(sql, new MapSqlParameterSource("memberId", memberId), new PrescriptionRowMapper());
    }

    public void updateStatus(Long prescriptionId, PrescriptionStatusUpdateRequest request) {
        if (request == null) {
            log.warn("PrescriptionStatusUpdateRequest is null for prescriptionId={}", prescriptionId);
            return;
        }

        if (request.status() != null) {
            String updatePharmacyPrescription = """
                    UPDATE pharmacy_prescription
                    SET status = :status,
                        updated_at = NOW()
                    WHERE prescription_id = :prescriptionId
                    ORDER BY updated_at DESC
                    LIMIT 1
                    """;

            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("status", request.status())
                    .addValue("prescriptionId", prescriptionId);

            jdbcTemplate.update(updatePharmacyPrescription, params);
        }

        if (request.pharmacyName() != null || request.completedAt() != null) {
            String updatePrescriptionSql = """
                    UPDATE prescription 
                    SET pharmacy_name = COALESCE(:pharmacyName, pharmacy_name),
                        completed_date = COALESCE(:completedAt, completed_date),
                        completed = CASE WHEN :completedAt IS NULL THEN completed ELSE TRUE END,
                        updated_at = NOW()
                    WHERE prescription_id = :prescriptionId
                    """;

            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("pharmacyName", request.pharmacyName())
                    .addValue("completedAt", request.completedAt())
                    .addValue("prescriptionId", prescriptionId);

            jdbcTemplate.update(updatePrescriptionSql, params);
        }

        if (request.completedAt() != null) {
            String insertPickupHistory = """
                    INSERT INTO pickup_history (pharmacy_prescription_id, pickup_at, verified_by, created_at)
                    SELECT pp.pharmacy_prescription_id,
                           :pickupAt,
                           :verifiedBy,
                           NOW()
                    FROM pharmacy_prescription pp
                    WHERE pp.prescription_id = :prescriptionId
                    ORDER BY pp.updated_at DESC
                    LIMIT 1
                    """;

            Map<String, Object> params = new HashMap<>();
            params.put("pickupAt", request.completedAt());
            params.put("verifiedBy", Optional.ofNullable(request.pharmacyName()).orElse("SYSTEM"));
            params.put("prescriptionId", prescriptionId);

            jdbcTemplate.update(insertPickupHistory, params);
        }
    }

    private static class PrescriptionRowMapper implements RowMapper<PrescriptionSummary> {

        @Override
        public PrescriptionSummary mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
            Timestamp completedTimestamp = rs.getTimestamp("completed_date");
            LocalDateTime completedAt = completedTimestamp != null ? completedTimestamp.toLocalDateTime() : null;

            Timestamp treatmentTimestamp = rs.getTimestamp("treatment_date");
            String treatmentDate = null;
            if (treatmentTimestamp != null) {
                treatmentDate = treatmentTimestamp.toLocalDateTime().toLocalDate().toString();
            }

            return new PrescriptionSummary(
                    rs.getLong("prescription_id"),
                    rs.getString("department_name"),
                    rs.getString("doctor_name"),
                    treatmentDate,
                    rs.getString("status"),
                    rs.getString("pharmacy_name"),
                    completedAt,
                    rs.getBoolean("can_select"),
                    rs.getBoolean("completed")
            );
        }
    }
}

 