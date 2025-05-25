package org.velihangozek.javarentalcli.dao.impl;

import org.velihangozek.javarentalcli.dao.RentalDao;
import org.velihangozek.javarentalcli.exception.VeloBusinessRuleException;
import org.velihangozek.javarentalcli.exception.VeloDataAccessException;
import org.velihangozek.javarentalcli.model.Rental;
import org.velihangozek.javarentalcli.model.RentalDetail;
import org.velihangozek.javarentalcli.model.RentalFullDetail;
import org.velihangozek.javarentalcli.util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RentalDaoImpl implements RentalDao {

    @Override
    public int save(Rental r) {

        String sql = """
                INSERT INTO rentals
                  (user_id, vehicle_id, start_time, end_time, total_price, deposit)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, r.getUserId());
            ps.setInt(2, r.getVehicleId());
            ps.setTimestamp(3, Timestamp.valueOf(r.getStartTime()));
            ps.setTimestamp(4, Timestamp.valueOf(r.getEndTime()));
            ps.setDouble(5, r.getTotalPrice());

            if (r.getDeposit() != null) {
                ps.setDouble(6, r.getDeposit());
            } else {
                ps.setNull(6, Types.NUMERIC);
            }

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {

                if (keys.next()) return keys.getInt(1);
                else throw new VeloDataAccessException("Rental insert failed, no ID returned.", null);
            }
        } catch (SQLException e) {
            throw new VeloDataAccessException("Rental insert failed.", e);
        }
    }

    /**
     * Saves a rental using the provided Connection (participating in its transaction).
     */
    public int save(Rental r, Connection conn) throws SQLException {
        String sql = """
                INSERT INTO rentals
                  (user_id, vehicle_id, start_time, end_time, total_price, deposit)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, r.getUserId());
            ps.setInt(2, r.getVehicleId());
            ps.setTimestamp(3, Timestamp.valueOf(r.getStartTime()));
            ps.setTimestamp(4, Timestamp.valueOf(r.getEndTime()));
            ps.setDouble(5, r.getTotalPrice());
            if (r.getDeposit() != null) ps.setDouble(6, r.getDeposit());
            else ps.setNull(6, Types.NUMERIC);
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
                else throw new SQLException("Insert succeeded but no ID returned");
            }
        } // no catch here—service will handle SQLExceptions
    }

    @Override
    public Optional<Rental> findById(int id) {

        String sql = "SELECT * FROM rentals WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();

            }
        } catch (SQLException e) {
            throw new VeloDataAccessException("Rental findById failed.", e);
        }
    }

    @Override
    public List<Rental> findAll() {
        return findBySql("SELECT * FROM rentals ORDER BY id");
    }

    @Override
    public List<Rental> findByUserId(int userId) {
        return findBySql("SELECT * FROM rentals WHERE user_id = ? ORDER BY start_time", userId);
    }

    @Override
    public List<Rental> findByVehicleId(int vehicleId) {
        return findBySql("SELECT * FROM rentals WHERE vehicle_id = ? ORDER BY start_time", vehicleId);
    }

    @Override
    public boolean update(Rental r) {

        String sql = """
                UPDATE rentals
                   SET user_id = ?, vehicle_id = ?, start_time = ?, end_time = ?, 
                       total_price = ?, deposit = ?
                 WHERE id = ?
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, r.getUserId());
            ps.setInt(2, r.getVehicleId());
            ps.setTimestamp(3, Timestamp.valueOf(r.getStartTime()));
            ps.setTimestamp(4, Timestamp.valueOf(r.getEndTime()));
            ps.setDouble(5, r.getTotalPrice());

            if (r.getDeposit() != null) {
                ps.setDouble(6, r.getDeposit());
            } else {
                ps.setNull(6, Types.NUMERIC);
            }

            ps.setInt(7, r.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new VeloDataAccessException("Rental update failed.", e);
        }
    }

    @Override
    public boolean delete(int id) {

        String sql = "DELETE FROM rentals WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new VeloDataAccessException("Rental delete failed.", e);
        }
    }

    @Override
    public List<RentalDetail> findDetailsByUserId(int userId) {
        String sql = """
                SELECT
                  r.id        AS rental_id,
                  v.brand,
                  v.model,
                  r.start_time,
                  r.end_time,
                  r.total_price,
                  r.deposit
                FROM rentals r
                JOIN vehicles v ON r.vehicle_id = v.id
                WHERE r.user_id = ?
                ORDER BY r.start_time
                """;

        try (var conn = DBConnection.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (var rs = ps.executeQuery()) {
                List<RentalDetail> list = new ArrayList<>();
                while (rs.next()) {
                    RentalDetail d = new RentalDetail();
                    d.setRentalId(rs.getInt("rental_id"));
                    d.setBrand(rs.getString("brand"));
                    d.setModel(rs.getString("model"));
                    d.setStartTime(rs.getTimestamp("start_time").toLocalDateTime());
                    d.setEndTime(rs.getTimestamp("end_time").toLocalDateTime());
                    d.setTotalPrice(rs.getDouble("total_price"));
                    double dep = rs.getDouble("deposit");
                    if (!rs.wasNull()) {
                        d.setDeposit(dep);
                    }
                    list.add(d);
                }
                return list;
            }
        } catch (SQLException e) {
            throw new VeloDataAccessException("Failed to load rental details", e);
        }
    }

    // in RentalDaoImpl.java
    @Override
    public RentalFullDetail findFullDetailById(int id) {
        String sql = """
                SELECT
                  r.id          AS rental_id,
                  u.email       AS user_email,
                  v.brand       AS vehicle_brand,
                  v.model       AS vehicle_model,
                  r.start_time,
                  r.end_time,
                  r.total_price,
                  r.deposit
                FROM rentals r
                JOIN users    u ON r.user_id    = u.id
                JOIN vehicles v ON r.vehicle_id = v.id
                WHERE r.id = ?
                """;
        try (var conn = DBConnection.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (var rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new VeloBusinessRuleException("Rental not found: #" + id);
                }
                RentalFullDetail d = new RentalFullDetail();
                d.setRentalId(rs.getInt("rental_id"));
                d.setUserEmail(rs.getString("user_email"));
                d.setVehicleBrand(rs.getString("vehicle_brand"));
                d.setVehicleModel(rs.getString("vehicle_model"));
                d.setStartTime(rs.getTimestamp("start_time").toLocalDateTime());
                d.setEndTime(rs.getTimestamp("end_time").toLocalDateTime());
                d.setTotalPrice(rs.getDouble("total_price"));
                double dep = rs.getDouble("deposit");
                if (!rs.wasNull()) d.setDeposit(dep);
                return d;
            }
        } catch (SQLException e) {
            throw new VeloDataAccessException("Failed to load full rental detail", e);
        }
    }


    /* Helpers */

    private List<Rental> findBySql(String sql, Object... params) {

        List<Rental> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new VeloDataAccessException("Rental findBySql failed.", e);
        }
        return list;
    }

    private Rental mapRow(ResultSet rs) {

        try {
            Rental r = new Rental();

            r.setId(rs.getInt("id"));
            r.setUserId(rs.getInt("user_id"));
            r.setVehicleId(rs.getInt("vehicle_id"));
            r.setStartTime(rs.getTimestamp("start_time").toLocalDateTime());
            r.setEndTime(rs.getTimestamp("end_time").toLocalDateTime());
            r.setTotalPrice(rs.getDouble("total_price"));
            double d = rs.getDouble("deposit");

            if (!rs.wasNull()) {
                r.setDeposit(d);
            }

            return r;
        } catch (SQLException e) {
            throw new VeloDataAccessException("Rental mapRow failed.", e);
        }

    }
}
