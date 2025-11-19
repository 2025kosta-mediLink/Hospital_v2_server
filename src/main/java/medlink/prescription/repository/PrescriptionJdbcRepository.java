package medlink.prescription.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import medlink.prescription.dto.request.PrescriptionStatusUpdateRequest;
import medlink.prescription.dto.response.PrescriptionResponse;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PrescriptionJdbcRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    /**
     * 처방전이 없는 접수내역에 대해 처방전을 생성
     * @param receptionId 접수내역 ID
     * @return 생성된 처방전 ID
     */
    public Long createPrescriptionForReception(Long receptionId) {
        String sql = """
                INSERT INTO prescription (reception_id, doctor_id, issued_at, content, pharmacy_name, completed_date, completed, created_at, updated_at)
                SELECT 
                    r.reception_id,
                    r.doctor_id,
                    COALESCE(r.updated_at, r.created_at) AS issued_at,
                    NULL AS content,
                    NULL AS pharmacy_name,
                    NULL AS completed_date,
                    FALSE AS completed,
                    NOW() AS created_at,
                    NOW() AS updated_at
                FROM reception r
                WHERE r.reception_id = :receptionId
                    AND r.status = 'DONE'
                    AND NOT EXISTS (
                        SELECT 1 FROM prescription p 
                        WHERE p.reception_id = r.reception_id
                    )
                """;
        
        MapSqlParameterSource params = new MapSqlParameterSource("receptionId", receptionId);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, params, keyHolder, new String[]{"prescription_id"});
        
        Number key = keyHolder.getKey();
        if (key == null) {
            throw new RuntimeException("Failed to create prescription for reception: " + receptionId);
        }
        
        return key.longValue();
    }

    /**
     * prescriptionId로 처방전 존재 여부 확인
     */
    public boolean existsPrescription(Long prescriptionId) {
        String sql = """
                SELECT COUNT(*) 
                FROM prescription 
                WHERE prescription_id = :prescriptionId
                """;
        
        MapSqlParameterSource params = new MapSqlParameterSource("prescriptionId", prescriptionId);
        Integer count = jdbcTemplate.queryForObject(sql, params, Integer.class);
        return count != null && count > 0;
    }

    /**
     * receptionId로 처방전 ID 조회 (없으면 null)
     */
    public Long findPrescriptionIdByReceptionId(Long receptionId) {
        String sql = """
                SELECT prescription_id 
                FROM prescription 
                WHERE reception_id = :receptionId
                LIMIT 1
                """;
        
        MapSqlParameterSource params = new MapSqlParameterSource("receptionId", receptionId);
        List<Long> results = jdbcTemplate.query(sql, params, (rs, rowNum) -> rs.getLong("prescription_id"));
        return results.isEmpty() ? null : results.get(0);
    }

    @SuppressWarnings("NullableProblems")
    public List<PrescriptionResponse> findByMemberId(Long memberId) {
        String sql = """
                SELECT 
                    p.prescription_id,
                    r.reception_id,
                    dep.name AS department_name,
                    doc.name AS doctor_name,
                    COALESCE(p.issued_at, COALESCE(r.updated_at, r.created_at)) AS treatment_date,
                    COALESCE(MAX(pp.status), 'START') AS status,
                    COALESCE(MAX(pp.pharmacy_name), MAX(p.pharmacy_name), MAX(pp.assigned_pharmacist)) AS pharmacy_name,
                    COALESCE(MAX(ph.pickup_at), MAX(p.completed_date)) AS completed_date,
                    MAX(ph.pickup_at) AS received_at,
                    COALESCE(MAX(p.completed), FALSE) AS completed,
                    CASE 
                        WHEN MAX(p.completed) = true THEN FALSE
                        WHEN MAX(ph.pickup_at) IS NOT NULL THEN FALSE
                        WHEN p.prescription_id IS NULL THEN TRUE
                        WHEN MAX(pp.status) IS NULL OR MAX(pp.status) = 'START' THEN TRUE
                        ELSE FALSE
                    END AS can_select,
                    COALESCE(MAX(p.created_at), r.created_at) AS created_at,
                    COALESCE(MAX(p.updated_at), r.updated_at) AS updated_at
                FROM reception r
                JOIN doctor doc ON r.doctor_id = doc.doctor_id
                JOIN department dep ON doc.department_id = dep.department_id
                LEFT JOIN prescription p ON p.reception_id = r.reception_id
                LEFT JOIN pharmacy_prescription pp ON p.prescription_id = pp.prescription_id
                LEFT JOIN pickup_history ph ON pp.pharmacy_prescription_id = ph.pharmacy_prescription_id
                WHERE r.member_id = :memberId
                    AND r.status = 'DONE'
                GROUP BY p.prescription_id, r.reception_id, dep.name, doc.name, r.updated_at, r.created_at
                ORDER BY COALESCE(p.issued_at, COALESCE(r.updated_at, r.created_at)) DESC
                """;

        return jdbcTemplate.query(sql, new MapSqlParameterSource("memberId", memberId), new PrescriptionRowMapper());
    }

    public void updateStatus(Long prescriptionId, PrescriptionStatusUpdateRequest request) {
        if (request == null) {
            log.warn("PrescriptionStatusUpdateRequest is null for prescriptionId={}", prescriptionId);
            return;
        }

        try {
            if (request.getStatus() != null) {
                String updatePharmacyPrescription = """
                        UPDATE pharmacy_prescription
                        SET status = :status,
                            updated_at = NOW()
                        WHERE prescription_id = :prescriptionId
                        ORDER BY updated_at DESC
                        LIMIT 1
                        """;

                MapSqlParameterSource params = new MapSqlParameterSource()
                        .addValue("status", request.getStatus())
                        .addValue("prescriptionId", prescriptionId);

                int updatedRows = jdbcTemplate.update(updatePharmacyPrescription, params);
                log.info("Updated pharmacy_prescription status for prescriptionId={}, updatedRows={}", prescriptionId, updatedRows);
            }

            if (request.getPharmacyName() != null || request.getCompletedAt() != null) {
                String updatePrescriptionSql = """
                        UPDATE prescription 
                        SET pharmacy_name = COALESCE(:pharmacyName, pharmacy_name),
                            completed_date = COALESCE(:completedAt, completed_date),
                            completed = CASE WHEN :completedAt IS NULL THEN completed ELSE TRUE END,
                            updated_at = NOW()
                        WHERE prescription_id = :prescriptionId
                        """;

                Timestamp completedAtTimestamp = null;
                if (request.getCompletedAt() != null) {
                    try {
                        completedAtTimestamp = Timestamp.valueOf(request.getCompletedAt());
                    } catch (Exception e) {
                        log.error("Failed to convert completedAt to Timestamp: {}", request.getCompletedAt(), e);
                        throw new IllegalArgumentException("Invalid completedAt format: " + request.getCompletedAt(), e);
                    }
                }

                MapSqlParameterSource params = new MapSqlParameterSource()
                        .addValue("pharmacyName", request.getPharmacyName())
                        .addValue("completedAt", completedAtTimestamp)
                        .addValue("prescriptionId", prescriptionId);

                int updatedRows = jdbcTemplate.update(updatePrescriptionSql, params);
                log.info("Updated prescription for prescriptionId={}, updatedRows={}", prescriptionId, updatedRows);
            }

            if (request.getCompletedAt() != null) {
                String insertPickupHistory = """
                        INSERT INTO pickup_history (pharmacy_prescription_id, member_id, pickup_at, status, verified_by, created_at)
                        SELECT pp.pharmacy_prescription_id,
                               r.member_id,
                               :pickupAt,
                               'PICKED_UP',
                               :verifiedBy,
                               NOW()
                        FROM pharmacy_prescription pp
                        JOIN prescription p ON pp.prescription_id = p.prescription_id
                        JOIN reception r ON p.reception_id = r.reception_id
                        WHERE pp.prescription_id = :prescriptionId
                        ORDER BY pp.updated_at DESC
                        LIMIT 1
                        """;

                Timestamp pickupAtTimestamp;
                try {
                    pickupAtTimestamp = Timestamp.valueOf(request.getCompletedAt());
                } catch (Exception e) {
                    log.error("Failed to convert completedAt to Timestamp for pickup_history: {}", request.getCompletedAt(), e);
                    throw new IllegalArgumentException("Invalid completedAt format: " + request.getCompletedAt(), e);
                }

                MapSqlParameterSource params = new MapSqlParameterSource()
                        .addValue("pickupAt", pickupAtTimestamp)
                        .addValue("verifiedBy", Optional.ofNullable(request.getPharmacyName()).orElse("SYSTEM"))
                        .addValue("prescriptionId", prescriptionId);

                int insertedRows = jdbcTemplate.update(insertPickupHistory, params);
                log.info("Inserted pickup_history for prescriptionId={}, insertedRows={}", prescriptionId, insertedRows);
            }
        } catch (Exception e) {
            log.error("Failed to update prescription status for prescriptionId={}", prescriptionId, e);
            throw new RuntimeException("처방전 상태 업데이트 실패: " + e.getMessage(), e);
        }
    }

    private static class PrescriptionRowMapper implements RowMapper<PrescriptionResponse> {

        @Override
        public PrescriptionResponse mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
            // prescription_id가 null인 경우 처방전이 없는 접수내역
            Long prescriptionId = rs.getObject("prescription_id", Long.class);
            
            Timestamp completedTimestamp = rs.getTimestamp("completed_date");
            LocalDateTime completedAt = completedTimestamp != null ? completedTimestamp.toLocalDateTime() : null;

            Timestamp receivedTimestamp = rs.getTimestamp("received_at");
            LocalDateTime receivedAt = receivedTimestamp != null ? receivedTimestamp.toLocalDateTime() : null;

            Timestamp treatmentTimestamp = rs.getTimestamp("treatment_date");
            String treatmentDate = null;
            if (treatmentTimestamp != null) {
                treatmentDate = treatmentTimestamp.toLocalDateTime().toLocalDate().toString();
            }

            Long receptionId = rs.getObject("reception_id", Long.class);
            
            PrescriptionResponse.Row row = new PrescriptionResponse.Row(
                    prescriptionId,
                    receptionId,
                    rs.getString("department_name"),
                    rs.getString("doctor_name"),
                    treatmentDate,
                    rs.getString("status"),
                    rs.getString("pharmacy_name"),
                    completedAt,
                    receivedAt,
                    rs.getBoolean("can_select"),
                    rs.getBoolean("completed")
            );
            return PrescriptionResponse.from(row);
        }
    }
}


