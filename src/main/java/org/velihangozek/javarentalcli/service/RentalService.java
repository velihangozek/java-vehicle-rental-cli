package org.velihangozek.javarentalcli.service;

import org.velihangozek.javarentalcli.model.Rental;
import org.velihangozek.javarentalcli.model.RentalDetail;
import org.velihangozek.javarentalcli.model.RentalFullDetail;
import org.velihangozek.javarentalcli.model.User;
import org.velihangozek.javarentalcli.model.enums.PeriodType;

import java.util.List;

public interface RentalService {
    /**
     * Creates a rental for a user, applying business rules (age checks,
     * corporate constraints, deposit). Returns the Rental with generated ID.
     */
    Rental rent(User user, int vehicleId, PeriodType period, int quantity);

    /** Lists all rentals by a specific user. */
    List<Rental> listRentalsByUser(User user);

    List<RentalDetail> listRentalDetailsByUser(User user);

    RentalFullDetail getFullDetail(int rentalId);
}