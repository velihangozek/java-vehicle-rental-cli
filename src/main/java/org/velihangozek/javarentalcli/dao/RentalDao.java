package org.velihangozek.javarentalcli.dao;

import org.velihangozek.javarentalcli.model.Rental;

import java.util.List;
import java.util.Optional;

public interface RentalDao {
    /**
     * Inserts a new rental record and returns the generated ID.
     */
    int save(Rental rental) throws Exception;

    /**
     * Finds a rental by its ID.
     */
    Optional<Rental> findById(int id) throws Exception;

    /**
     * Returns all rentals.
     */
    List<Rental> findAll() throws Exception;

    /**
     * Returns all rentals for a given user.
     */
    List<Rental> findByUserId(int userId) throws Exception;

    /**
     * Returns all rentals for a given vehicle.
     */
    List<Rental> findByVehicleId(int vehicleId) throws Exception;

    /**
     * Updates an existing rental; returns true if any row was affected.
     */
    boolean update(Rental rental) throws Exception;

    /**
     * Deletes a rental by ID; returns true if deletion succeeded.
     */
    boolean delete(int id) throws Exception;
}
