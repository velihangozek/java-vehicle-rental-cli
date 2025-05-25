package org.velihangozek.javarentalcli.dao;

import org.velihangozek.javarentalcli.model.Vehicle;
import org.velihangozek.javarentalcli.model.enums.VehicleType;

import java.util.List;
import java.util.Optional;

public interface VehicleDao {
    /**
     * Inserts a new vehicle and returns the generated ID.
     */
    int save(Vehicle vehicle);

    /**
     * Finds a vehicle by its ID.
     */
    Optional<Vehicle> findById(int id);

    /**
     * Returns all vehicles in the system.
     */
    List<Vehicle> findAll();

    /**
     * Returns vehicles filtered by type (CAR, HELICOPTER, MOTORCYCLE).
     */
    List<Vehicle> findByType(VehicleType type);

    /**
     * Updates an existing vehicle; returns true if any row was affected.
     */
    boolean update(Vehicle vehicle);

    /**
     * Deletes a vehicle by ID; returns true if deletion succeeded.
     */
    boolean delete(int id);

    /**
     * Finds vehicles whose brand or model contains the given keyword (case-insensitive).
     */
    List<Vehicle> findByKeyword(String keyword);
}
