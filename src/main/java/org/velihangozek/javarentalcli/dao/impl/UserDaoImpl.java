package org.velihangozek.javarentalcli.dao.impl;

import org.velihangozek.javarentalcli.dao.UserDao;
import org.velihangozek.javarentalcli.exception.VeloDataAccessException;
import org.velihangozek.javarentalcli.model.User;
import org.velihangozek.javarentalcli.util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDaoImpl implements UserDao {

    @Override
    public int save(User user) {

        String sql = """
                INSERT INTO users (email, password_hash, role, birthdate, is_corporate)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPasswordHash());
            ps.setString(3, user.getRole());

            if (user.getBirthdate() != null) {
                ps.setDate(4, Date.valueOf(user.getBirthdate()));
            } else {
                ps.setNull(4, Types.DATE);
            }

            ps.setBoolean(5, user.isCorporate());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {

                if (keys.next()) {
                    return keys.getInt(1);
                } else {
                    throw new VeloDataAccessException("User insert failed, no ID obtained.", null);
                }
            }
        } catch (SQLException e) {
            throw new VeloDataAccessException("User insert failed.", e);
        }
    }

    @Override
    public Optional<User> findById(int id) {

        String sql = "SELECT * FROM users WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();

            }
        } catch (SQLException e) {
            throw new VeloDataAccessException("User findById failed.", e);
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {

        String sql = "SELECT * FROM users WHERE email = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {

                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();

            }
        } catch (SQLException e) {
            throw new VeloDataAccessException("User findByEmail failed.", e);
        }
    }

    @Override
    public List<User> findAll() {

        String sql = "SELECT * FROM users ORDER BY id";

        List<User> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }

        } catch (SQLException e) {
            throw new VeloDataAccessException("User findAll failed.", e);
        }
        return list;
    }

    @Override
    public boolean update(User user) {

        String sql = """
                UPDATE users
                   SET email = ?, password_hash = ?, role = ?, birthdate = ?, is_corporate = ?
                 WHERE id = ?
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPasswordHash());
            ps.setString(3, user.getRole());

            if (user.getBirthdate() != null) {
                ps.setDate(4, Date.valueOf(user.getBirthdate()));
            } else {
                ps.setNull(4, Types.DATE);
            }

            ps.setBoolean(5, user.isCorporate());
            ps.setInt(6, user.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new VeloDataAccessException("User update failed.", e);
        }
    }

    @Override
    public boolean delete(int id) {

        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new VeloDataAccessException("User delete failed.", e);
        }
    }

    private User mapRow(ResultSet rs) {

        try {
            User u = new User();

            u.setId(rs.getInt("id"));
            u.setEmail(rs.getString("email"));
            u.setPasswordHash(rs.getString("password_hash"));
            u.setRole(rs.getString("role"));
            Date bd = rs.getDate("birthdate");

            if (bd != null) {
                u.setBirthdate(bd.toLocalDate());
            }

            u.setCorporate(rs.getBoolean("is_corporate"));

            return u;
        } catch (SQLException e) {
            throw new VeloDataAccessException("User mapRow failed.", e);
        }

    }
}
