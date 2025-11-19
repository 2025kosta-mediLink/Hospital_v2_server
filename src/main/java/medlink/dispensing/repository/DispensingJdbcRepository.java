package medlink.dispensing.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import medlink.dispensing.dto.response.DispensingResponse;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DispensingJdbcRepository {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @SuppressWarnings("NullableProblems")
    public Optional<DispensingResponse> findById(String dispensingId) {
        try {
            // dispensingId를 BIGINT로 변환
            Long pharmacyPrescriptionId = Long.parseLong(dispensingId);
            
            String sql = """
                    SELECT
                        CAST(pp.pharmacy_prescription_id AS CHAR) AS dispensing_id,
                        COALESCE(pp.pharmacy_name, '선택한 약국') AS pharmacy_name,
                        NULL AS pharmacy_address,
                        NULL AS pharmacy_phone,
                        NULL AS pharmacy_latitude,
                        NULL AS pharmacy_longitude,
                        pp.status,
                        pp.assigned_pharmacist AS dispenser_name,
                        pp.created_at AS received_at,
                        pp.expected_finish_time AS estimated_completion_time,
                        phist.pickup_at AS completed_at,
                        GROUP_CONCAT(DISTINCT pr.content ORDER BY pr.prescription_id SEPARATOR ', ') AS prescription_details,
                        NULL AS qr_code
                    FROM pharmacy_prescription pp
                    LEFT JOIN pickup_history phist ON phist.pharmacy_prescription_id = pp.pharmacy_prescription_id
                    LEFT JOIN prescription pr ON pr.prescription_id = pp.prescription_id
                    WHERE pp.pharmacy_prescription_id = :pharmacyPrescriptionId
                    GROUP BY pp.pharmacy_prescription_id,
                             pp.pharmacy_name,
                             pp.status,
                             pp.assigned_pharmacist,
                             pp.created_at,
                             pp.expected_finish_time,
                             phist.pickup_at
                    """;

            List<DispensingResponse> result = jdbcTemplate.query(
                    sql,
                    new MapSqlParameterSource("pharmacyPrescriptionId", pharmacyPrescriptionId),
                    new DispensingRowMapper()
            );

            return result.stream().findFirst();
        } catch (NumberFormatException e) {
            log.error("Invalid dispensingId format: {}", dispensingId, e);
            return Optional.empty();
        }
    }

    public void markCompleted(String dispensingId, LocalDateTime completedAt) {
        try {
            // dispensingId를 BIGINT로 변환
            Long pharmacyPrescriptionId = Long.parseLong(dispensingId);
            
            String updateSql = """
                    UPDATE pharmacy_prescription
                    SET status = 'RECEIVED_BY_USER',
                        updated_at = NOW()
                    WHERE pharmacy_prescription_id = :pharmacyPrescriptionId
                    """;

            int updatedRows = jdbcTemplate.update(updateSql, new MapSqlParameterSource()
                    .addValue("pharmacyPrescriptionId", pharmacyPrescriptionId));
            
            if (updatedRows == 0) {
                log.warn("No pharmacy_prescription record found with id: {}", pharmacyPrescriptionId);
                throw new IllegalArgumentException("조제 정보를 찾을 수 없습니다. dispensingId=" + dispensingId);
            }
            
            log.info("Successfully marked pharmacy_prescription {} as RECEIVED_BY_USER", pharmacyPrescriptionId);
        } catch (NumberFormatException e) {
            log.error("Invalid dispensingId format: {}", dispensingId, e);
            throw new IllegalArgumentException("잘못된 조제 ID 형식입니다. dispensingId=" + dispensingId, e);
        }
    }

    private static class DispensingRowMapper implements RowMapper<DispensingResponse> {
        @Override
        public DispensingResponse mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
            String receivedAt = Optional.ofNullable(rs.getTimestamp("received_at"))
                    .map(Timestamp::toLocalDateTime)
                    .map(dt -> dt.format(TIME_FORMATTER))
                    .orElse(null);

            String estimatedCompletion = Optional.ofNullable(rs.getTimestamp("estimated_completion_time"))
                    .map(Timestamp::toLocalDateTime)
                    .map(dt -> dt.format(TIME_FORMATTER))
                    .orElse(null);

            String completedAt = Optional.ofNullable(rs.getTimestamp("completed_at"))
                    .map(Timestamp::toLocalDateTime)
                    .map(dt -> dt.format(TIME_FORMATTER))
                    .orElse(null);

            DispensingResponse.Row row = new DispensingResponse.Row(
                    rs.getString("dispensing_id"),
                    rs.getString("pharmacy_name"),
                    rs.getString("pharmacy_address"),
                    rs.getString("pharmacy_phone"),
                    rs.getDouble("pharmacy_latitude"),
                    rs.getDouble("pharmacy_longitude"),
                    rs.getString("status"),
                    rs.getString("dispenser_name"),
                    receivedAt,
                    estimatedCompletion,
                    completedAt,
                    rs.getString("prescription_details"),
                    rs.getString("qr_code")
            );
            return DispensingResponse.from(row);
        }
    }
}


