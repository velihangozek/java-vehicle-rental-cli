package org.velihangozek.javarentalcli.dao.impl;

import org.velihangozek.javarentalcli.dao.RentalDao;
import org.velihangozek.javarentalcli.model.Rental;
import org.velihangozek.javarentalcli.util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RentalDaoImpl implements RentalDao {

    @Override
    public int save(Rental r) throws Exception {

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
                else throw new SQLException("Rental insert failed, no ID returned.");
            }
        }
    }

    @Override
    public Optional<Rental> findById(int id) throws Exception {

        String sql = "SELECT * FROM rentals WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();

            }
        }
    }

    @Override
    public List<Rental> findAll() throws Exception {
        return findBySql("SELECT * FROM rentals ORDER BY id");
    }

    @Override
    public List<Rental> findByUserId(int userId) throws Exception {
        return findBySql("SELECT * FROM rentals WHERE user_id = ? ORDER BY start_time", userId);
    }

    @Override
    public List<Rental> findByVehicleId(int vehicleId) throws Exception {
        return findBySql("SELECT * FROM rentals WHERE vehicle_id = ? ORDER BY start_time", vehicleId);
    }

    @Override
    public boolean update(Rental r) throws Exception {

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
        }
    }

    @Override
    public boolean delete(int id) throws Exception {

        String sql = "DELETE FROM rentals WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;
        }
    }

    /* Helpers */

    private List<Rental> findBySql(String sql, Object... params) throws Exception {

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
        }
        return list;
    }

    private Rental mapRow(ResultSet rs) throws SQLException {

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
    }
}
