package org.velihangozek.javarentalcli.dao.impl;

import org.velihangozek.javarentalcli.dao.VehicleDao;
import org.velihangozek.javarentalcli.model.Vehicle;
import org.velihangozek.javarentalcli.model.enums.VehicleType;
import org.velihangozek.javarentalcli.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VehicleDaoImpl implements VehicleDao {

    @Override
    public int save(Vehicle v) throws Exception {

        String sql = """
                INSERT INTO vehicles
                  (type, brand, model, purchase_price,
                   rate_hourly, rate_daily, rate_weekly, rate_monthly)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setObject(1, v.getType().name(), Types.OTHER);

            ps.setString(2, v.getBrand());
            ps.setString(3, v.getModel());
            ps.setLong(4, v.getPurchasePrice());
            ps.setDouble(5, v.getRateHourly());
            ps.setDouble(6, v.getRateDaily());
            ps.setDouble(7, v.getRateWeekly());
            ps.setDouble(8, v.getRateMonthly());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {

                if (keys.next()) return keys.getInt(1);
                else throw new SQLException("Vehicle insert failed, no ID returned.");

            }
        }
    }

    @Override
    public Optional<Vehicle> findById(int id) throws Exception {

        String sql = "SELECT * FROM vehicles WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();

            }
        }
    }

    @Override
    public List<Vehicle> findAll() throws Exception {

        String sql = "SELECT * FROM vehicles ORDER BY id";

        List<Vehicle> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    @Override
    public List<Vehicle> findByType(VehicleType type) throws Exception {

        String sql = "SELECT * FROM vehicles WHERE type = ? ORDER BY id";

        List<Vehicle> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, type.name(), Types.OTHER);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) list.add(mapRow(rs));

            }
        }
        return list;
    }

    @Override
    public boolean update(Vehicle v) throws Exception {

        String sql = """
                UPDATE vehicles
                   SET type = ?, brand = ?, model = ?, purchase_price = ?,
                       rate_hourly = ?, rate_daily = ?, rate_weekly = ?, rate_monthly = ?
                 WHERE id = ?
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, v.getType().name());

            ps.setString(2, v.getBrand());
            ps.setString(3, v.getModel());
            ps.setLong(4, v.getPurchasePrice());
            ps.setDouble(5, v.getRateHourly());
            ps.setDouble(6, v.getRateDaily());
            ps.setDouble(7, v.getRateWeekly());
            ps.setDouble(8, v.getRateMonthly());
            ps.setInt(9, v.getId());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws Exception {

        String sql = "DELETE FROM vehicles WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;
        }
    }

    private Vehicle mapRow(ResultSet rs) throws SQLException {

        Vehicle v = new Vehicle();

        v.setId(rs.getInt("id"));
        v.setType(VehicleType.valueOf(rs.getString("type")));
        v.setBrand(rs.getString("brand"));
        v.setModel(rs.getString("model"));
        v.setPurchasePrice(rs.getLong("purchase_price"));
        v.setRateHourly(rs.getDouble("rate_hourly"));
        v.setRateDaily(rs.getDouble("rate_daily"));
        v.setRateWeekly(rs.getDouble("rate_weekly"));
        v.setRateMonthly(rs.getDouble("rate_monthly"));

        return v;
    }
}
