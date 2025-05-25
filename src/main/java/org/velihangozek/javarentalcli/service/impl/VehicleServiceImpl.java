package org.velihangozek.javarentalcli.service.impl;

import org.velihangozek.javarentalcli.dao.VehicleDao;
import org.velihangozek.javarentalcli.dao.impl.VehicleDaoImpl;
import org.velihangozek.javarentalcli.exception.VeloBusinessRuleException;
import org.velihangozek.javarentalcli.model.Vehicle;
import org.velihangozek.javarentalcli.model.enums.VehicleType;
import org.velihangozek.javarentalcli.service.VehicleService;

import java.util.List;

public class VehicleServiceImpl implements VehicleService {
    private final VehicleDao vehicleDao = new VehicleDaoImpl();

    @Override
    public Vehicle addVehicle(VehicleType type,
                              String brand,
                              String model,
                              long purchasePrice,
                              double rateHourly,
                              double rateDaily,
                              double rateWeekly,
                              double rateMonthly) {
        Vehicle v = new Vehicle(null, type, brand, model,
                purchasePrice, rateHourly,
                rateDaily, rateWeekly, rateMonthly);
        int id = vehicleDao.save(v);
        v.setId(id);
        return v;
    }

    @Override
    public List<Vehicle> listAll() {
        return vehicleDao.findAll();
    }

    @Override
    public List<Vehicle> listByType(VehicleType type) {
        return vehicleDao.findByType(type);
    }

    @Override
    public Vehicle findById(int id) {
        try {
            return vehicleDao.findById(id)
                    .orElseThrow(()
                            -> new VeloBusinessRuleException("No vehicle found with ID " + id));
        } catch (Exception e) {
            throw new VeloBusinessRuleException(
                    "Failed to look up vehicle ID " + id + ": " + e.getMessage());
        }

    }

    @Override
    public List<Vehicle> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return listAll();
        }
        return vehicleDao.findByKeyword(keyword);
    }
}
