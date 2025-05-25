package org.velihangozek.javarentalcli.service.impl;

import org.velihangozek.javarentalcli.dao.RentalDao;
import org.velihangozek.javarentalcli.dao.VehicleDao;
import org.velihangozek.javarentalcli.dao.impl.RentalDaoImpl;
import org.velihangozek.javarentalcli.dao.impl.VehicleDaoImpl;
import org.velihangozek.javarentalcli.exception.VeloBusinessRuleException;
import org.velihangozek.javarentalcli.exception.VeloDataAccessException;
import org.velihangozek.javarentalcli.model.*;
import org.velihangozek.javarentalcli.model.enums.PeriodType;
import org.velihangozek.javarentalcli.service.RentalService;
import org.velihangozek.javarentalcli.util.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class RentalServiceImpl implements RentalService {
    private final RentalDao rentalDao = new RentalDaoImpl();
    private final RentalDaoImpl rentalDaoTransactional = new RentalDaoImpl();
    private final VehicleDao vehicleDao = new VehicleDaoImpl();
    private final VehicleDaoImpl vehicleDaoTransactional = new VehicleDaoImpl();

    @Override
    public Rental rent(User user,
                       int vehicleId,
                       PeriodType period,
                       int quantity) {
        Connection conn = null;

        try {

            // 1. Acquire one Connection outside the header - TRANSACTIONAL
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // 2. Use the DAO overloads that take this same Connection - Load vehicle using this same Connection - Fetch vehicle
            Vehicle v = vehicleDaoTransactional
                    .findById(vehicleId, conn)
                    .orElseThrow(() -> new VeloBusinessRuleException(
                            "Vehicle ID " + vehicleId + " not found"));

            // 2. Compute start/end times
            LocalDateTime start = LocalDateTime.now();
            LocalDateTime end = switch (period) {
                case HOURLY -> start.plus(quantity, ChronoUnit.HOURS);
                case DAILY -> start.plus(quantity, ChronoUnit.DAYS);
                case WEEKLY -> start.plus(quantity * 7L, ChronoUnit.DAYS);
                case MONTHLY -> start.plus(quantity, ChronoUnit.MONTHS);
            };

            // 3. Corporate must rent ≥ 30 days in total
            if (user.isCorporate()) {
                long days = ChronoUnit.DAYS.between(start, end);
                if (days < 30) {
                    throw new VeloBusinessRuleException(
                            "Corporate users must rent for at least 1 month (30 days)."
                    );
                }
            }

            // 4. Age & purchase-price rule
            if (v.getPurchasePrice() > 2_000_000L) {
                int age = (int) ChronoUnit.YEARS.between(
                        user.getBirthdate(), LocalDateTime.now().toLocalDate().atStartOfDay());
                if (age <= 30) {
                    throw new VeloBusinessRuleException(
                            "Users ≤30 cannot rent vehicles >2M TL");
                }
            }

            // 5. Calculate deposit (10% of purchasePrice if >2M)
            Double deposit = null;
            if (v.getPurchasePrice() > 2_000_000L) {
                deposit = v.getPurchasePrice() * 0.10;
            }

            // 6. Compute total price
            double rate = switch (period) {
                case HOURLY -> v.getRateHourly();
                case DAILY -> v.getRateDaily();
                case WEEKLY -> v.getRateWeekly();
                case MONTHLY -> v.getRateMonthly();
            };
            double total = rate * quantity;

            // 7. Persist within this same transaction
            Rental r = new Rental(
                    null,
                    user.getId(),
                    vehicleId,
                    start,
                    end,
                    total,
                    deposit);

            int id = rentalDaoTransactional.save(r, conn);
            r.setId(id);

            // 8. Commit (all‐or‐nothing)
            conn.commit();
            return r;

        } catch (SQLException e) {
            // roll back on any SQL error
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (Exception ignored) {
                }
            }
            throw new VeloDataAccessException("Transaction failed", e);

        } catch (RuntimeException bre) {
            // roll back on business‐rule failures as well
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (Exception ignored) {
                }
            }
            throw bre;

        } finally {
            // always close
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }

    @Override
    public List<Rental> listRentalsByUser(User user) {
        return rentalDao.findByUserId(user.getId());
    }

    @Override
    public List<RentalDetail> listRentalDetailsByUser(User user) {
        return rentalDao.findDetailsByUserId(user.getId());
    }

    public RentalFullDetail getFullDetail(int rentalId) {
        return rentalDao.findFullDetailById(rentalId);
    }
}
