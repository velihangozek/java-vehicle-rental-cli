package org.velihangozek.javarentalcli.service.impl;

import org.velihangozek.javarentalcli.dao.RentalDao;
import org.velihangozek.javarentalcli.dao.VehicleDao;
import org.velihangozek.javarentalcli.dao.impl.RentalDaoImpl;
import org.velihangozek.javarentalcli.dao.impl.VehicleDaoImpl;
import org.velihangozek.javarentalcli.model.Rental;
import org.velihangozek.javarentalcli.model.User;
import org.velihangozek.javarentalcli.model.Vehicle;
import org.velihangozek.javarentalcli.model.enums.PeriodType;
import org.velihangozek.javarentalcli.service.RentalService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

public class RentalServiceImpl implements RentalService {
    private final RentalDao rentalDao = new RentalDaoImpl();
    private final VehicleDao vehicleDao = new VehicleDaoImpl();

    @Override
    public Rental rent(User user,
                       int vehicleId,
                       PeriodType period,
                       int quantity) throws Exception {
        // 1. Fetch vehicle
        Vehicle v = vehicleDao.findById(vehicleId)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found"));

        // 2. Corporate must rent ≥1 month
        if (user.isCorporate() &&
                (period != PeriodType.MONTHLY || quantity < 1)) {
            throw new IllegalArgumentException(
                    "Corporate users must rent at least 1 MONTHLY period");
        }

        // 3. Age & purchase-price rule
        if (v.getPurchasePrice() > 2_000_000L) {
            int age = (int) ChronoUnit.YEARS.between(
                    user.getBirthdate(), LocalDateTime.now().toLocalDate().atStartOfDay());
            if (age <= 30) {
                throw new IllegalArgumentException(
                        "Users ≤30 cannot rent vehicles >2M TL");
            }
        }

        // 4. Calculate deposit (10% of purchasePrice if >2M)
        Double deposit = null;
        if (v.getPurchasePrice() > 2_000_000L) {
            deposit = v.getPurchasePrice() * 0.10;
        }

        // 5. Compute start/end times
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = switch (period) {
            case HOURLY -> start.plus(quantity, ChronoUnit.HOURS);
            case DAILY -> start.plus(quantity, ChronoUnit.DAYS);
            case WEEKLY -> start.plus(quantity * 7L, ChronoUnit.DAYS);
            case MONTHLY -> start.plus(quantity, ChronoUnit.MONTHS);
        };

        // 6. Compute total price
        double rate = switch (period) {
            case HOURLY -> v.getRateHourly();
            case DAILY -> v.getRateDaily();
            case WEEKLY -> v.getRateWeekly();
            case MONTHLY -> v.getRateMonthly();
        };
        double total = rate * quantity;

        // 7. Persist
        Rental r = new Rental(null,
                user.getId(),
                vehicleId,
                start,
                end,
                total,
                deposit);
        int id = rentalDao.save(r);
        r.setId(id);

        return r;
    }

    @Override
    public List<Rental> listRentalsByUser(User user) throws Exception {
        return rentalDao.findByUserId(user.getId());
    }
}
