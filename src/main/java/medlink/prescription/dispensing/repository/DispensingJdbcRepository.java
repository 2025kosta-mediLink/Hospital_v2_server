package medlink.prescription.dispensing.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import medlink.prescription.dispensing.dto.DispensingStatus;
import org.springframework.dao.DataAccessException;
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
    public Optional<DispensingStatus> findById(String dispensingId) {
        String sql = """
                SELECT
                    CAST(pp.pharmacy_prescription_id AS CHAR) AS dispensing_id,
                    COALESCE(ph.pharmacy_name, '선택한 약국') AS pharmacy_name,
                    ph.address AS pharmacy_address,
                    ph.phone_number AS pharmacy_phone,
                    ph.latitude AS pharmacy_latitude,
                    ph.longitude AS pharmacy_longitude,
                    pp.status,
                    pp.assigned_pharmacist AS dispenser_name,
                    pp.created_at AS received_at,
                    pp.expected_finish_time AS estimated_completion_time,
                    COALESCE(phist.pickup_at, pp.completed_at) AS completed_at,
                    GROUP_CONCAT(DISTINCT pr.content ORDER BY pr.prescription_id SEPARATOR ', ') AS prescription_details,
                    pp.qr_code
                FROM pharmacy_prescription pp
                LEFT JOIN pharmacies ph ON pp.pharmacy_id = ph.pharmacy_id
                LEFT JOIN pickup_history phist ON phist.pharmacy_prescription_id = pp.pharmacy_prescription_id
                LEFT JOIN prescription pr ON pr.prescription_id = pp.prescription_id
                WHERE pp.pharmacy_prescription_id = :dispensingId
                GROUP BY pp.pharmacy_prescription_id,
                         ph.pharmacy_name,
                         ph.address,
                         ph.phone_number,
                         ph.latitude,
                         ph.longitude,
                         pp.status,
                         pp.assigned_pharmacist,
                         pp.created_at,
                         pp.expected_finish_time,
                         phist.pickup_at,
                         pp.completed_at,
                         pp.qr_code
                """;

        List<DispensingStatus> result = jdbcTemplate.query(
                sql,
                new MapSqlParameterSource("dispensingId", dispensingId),
                new DispensingRowMapper()
        );

        return result.stream().findFirst();
    }

    public void markCompleted(String dispensingId, LocalDateTime completedAt) {
        String updateSql = """
                UPDATE pharmacy_prescription
                SET status = 'RECEIVED_BY_USER',
                    completed_at = :completedAt,
                    updated_at = NOW()
                WHERE pharmacy_prescription_id = :dispensingId
                """;

        jdbcTemplate.update(updateSql, new MapSqlParameterSource()
                .addValue("completedAt", completedAt)
                .addValue("dispensingId", dispensingId));

        String insertHistory = """
                INSERT INTO pickup_history (pharmacy_prescription_id, pickup_at, verified_by, created_at)
                VALUES (:dispensingId, :completedAt, '사용자', NOW())
                ON DUPLICATE KEY UPDATE pickup_at = VALUES(pickup_at),
                                        verified_by = VALUES(verified_by)
                """;

        try {
            jdbcTemplate.update(insertHistory, new MapSqlParameterSource()
                    .addValue("dispensingId", dispensingId)
                    .addValue("completedAt", completedAt));
        } catch (DataAccessException ex) {
            log.warn("Failed to upsert pickup_history for dispensingId={}", dispensingId, ex);
        }
    }

    private static class DispensingRowMapper implements RowMapper<DispensingStatus> {
        @Override
        public DispensingStatus mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
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

            return new DispensingStatus(
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
        }
    }
}

 