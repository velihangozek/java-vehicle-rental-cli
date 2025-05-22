package org.velihangozek.javarentalcli.dao;

import org.velihangozek.javarentalcli.model.Vehicle;
import org.velihangozek.javarentalcli.model.enums.VehicleType;

import java.util.List;
import java.util.Optional;

public interface VehicleDao {
    /**
     * Inserts a new vehicle and returns the generated ID.
     */
    int save(Vehicle vehicle) throws Exception;

    /**
     * Finds a vehicle by its ID.
     */
    Optional<Vehicle> findById(int id) throws Exception;

    /**
     * Returns all vehicles in the system.
     */
    List<Vehicle> findAll() throws Exception;

    /**
     * Returns vehicles filtered by type (CAR, HELICOPTER, MOTORCYCLE).
     */
    List<Vehicle> findByType(VehicleType type) throws Exception;

    /**
     * Updates an existing vehicle; returns true if any row was affected.
     */
    boolean update(Vehicle vehicle) throws Exception;

    /**
     * Deletes a vehicle by ID; returns true if deletion succeeded.
     */
    boolean delete(int id) throws Exception;
}
