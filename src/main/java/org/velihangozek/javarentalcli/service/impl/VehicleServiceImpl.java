package org.velihangozek.javarentalcli.service.impl;

import org.velihangozek.javarentalcli.dao.VehicleDao;
import org.velihangozek.javarentalcli.dao.impl.VehicleDaoImpl;
import org.velihangozek.javarentalcli.model.Vehicle;
import org.velihangozek.javarentalcli.model.enums.VehicleType;
import org.velihangozek.javarentalcli.service.VehicleService;

import java.util.List;
import java.util.Optional;

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
                              double rateMonthly) throws Exception {
        Vehicle v = new Vehicle(null, type, brand, model,
                purchasePrice, rateHourly,
                rateDaily, rateWeekly, rateMonthly);
        int id = vehicleDao.save(v);
        v.setId(id);
        return v;
    }

    @Override
    public List<Vehicle> listAll() throws Exception {
        return vehicleDao.findAll();
    }

    @Override
    public List<Vehicle> listByType(VehicleType type) throws Exception {
        return vehicleDao.findByType(type);
    }

    @Override
    public Optional<Vehicle> findById(int id) throws Exception {
        return vehicleDao.findById(id);
    }
}
