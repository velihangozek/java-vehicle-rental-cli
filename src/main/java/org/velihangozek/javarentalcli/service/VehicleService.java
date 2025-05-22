package org.velihangozek.javarentalcli.service;

import org.velihangozek.javarentalcli.model.Vehicle;
import org.velihangozek.javarentalcli.model.enums.VehicleType;

import java.util.List;
import java.util.Optional;

public interface VehicleService {
    Vehicle addVehicle(VehicleType type, String brand, String model,
                       long purchasePrice, double rateHourly,
                       double rateDaily, double rateWeekly,
                       double rateMonthly) throws Exception;

    List<Vehicle> listAll() throws Exception;

    List<Vehicle> listByType(VehicleType type) throws Exception;

    Optional<Vehicle> findById(int id) throws Exception;
}