package org.velihangozek.javarentalcli.dao;

import org.velihangozek.javarentalcli.model.Rental;
import org.velihangozek.javarentalcli.model.RentalDetail;
import org.velihangozek.javarentalcli.model.RentalFullDetail;

import java.util.List;
import java.util.Optional;

public interface RentalDao {
    /**
     * Inserts a new rental record and returns the generated ID.
     */
    int save(Rental rental);

    /**
     * Finds a rental by its ID.
     */
    Optional<Rental> findById(int id);

    /**
     * Returns all rentals.
     */
    List<Rental> findAll();

    /**
     * Returns all rentals for a given user.
     */
    List<Rental> findByUserId(int userId);

    /**
     * Returns all rentals for a given vehicle.
     */
    List<Rental> findByVehicleId(int vehicleId);

    /**
     * Updates an existing rental; returns true if any row was affected.
     */
    boolean update(Rental rental);

    /**
     * Deletes a rental by ID; returns true if deletion succeeded.
     */
    boolean delete(int id);

    /**
     * Returns a joined view of this user’s rentals with vehicle brand/model.
     */
    List<RentalDetail> findDetailsByUserId(int userId);

    /**
     * Fetches a joined record of one rental with user & vehicle info.
     */
    RentalFullDetail findFullDetailById(int rentalId);
}
