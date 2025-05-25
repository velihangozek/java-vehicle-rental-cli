package org.velihangozek.javarentalcli.service;

import org.velihangozek.javarentalcli.model.Vehicle;
import org.velihangozek.javarentalcli.model.enums.VehicleType;

import java.util.List;

public interface VehicleService {
    Vehicle addVehicle(VehicleType type, String brand, String model,
                       long purchasePrice, double rateHourly,
                       double rateDaily, double rateWeekly,
                       double rateMonthly);

    List<Vehicle> listAll();

    List<Vehicle> listByType(VehicleType type);

    Vehicle findById(int id);

    /**
     * Searches vehicles by a free-text keyword against brand & model.
     */
    List<Vehicle> search(String keyword);

}