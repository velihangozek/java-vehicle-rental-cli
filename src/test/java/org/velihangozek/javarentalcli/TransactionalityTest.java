package org.velihangozek.javarentalcli;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.velihangozek.javarentalcli.exception.VeloBusinessRuleException;
import org.velihangozek.javarentalcli.model.User;
import org.velihangozek.javarentalcli.model.enums.PeriodType;
import org.velihangozek.javarentalcli.service.RentalService;
import org.velihangozek.javarentalcli.service.UserService;
import org.velihangozek.javarentalcli.service.VehicleService;
import org.velihangozek.javarentalcli.service.impl.RentalServiceImpl;
import org.velihangozek.javarentalcli.service.impl.UserServiceImpl;
import org.velihangozek.javarentalcli.service.impl.VehicleServiceImpl;

import java.time.LocalDate;

public class TransactionalityTest {

    static UserService userService;
    static VehicleService vehicleService;
    static RentalService rentalService;

    @BeforeAll
    static void setup() throws Exception {
        userService = new UserServiceImpl();
        vehicleService = new VehicleServiceImpl();
        rentalService = new RentalServiceImpl();
        // make sure you have at least one corporate user and one expensive vehicle in your DB
    }

    @Test
    void corporateUnder30DaysShouldRollback() {
        // 1. Create or fetch a corporate user
        User corp = userService.register(
                "corp-test@example.com", "pw", "CUSTOMER", LocalDate.now().minusYears(40), true
        );
        // 2. Create or fetch a vehicle > 2M so deposit rule applies
        var v = vehicleService.addVehicle(
                /* type */   org.velihangozek.javarentalcli.model.enums.VehicleType.CAR,
                /* brand */  "TestBrand",
                /* model */  "Expensive",
                /* purchasePrice= */ 2_500_000L,
                /* rates */  200, 1200, 6000, 20000
        );

        int before = rentalService.listRentalsByUser(corp).size();

        // 3. Attempt a rental that violates the 30-day minimum (e.g., 10 days)
        VeloBusinessRuleException ex = assertThrows(
                VeloBusinessRuleException.class,
                () -> rentalService.rent(corp, v.getId(), PeriodType.DAILY, 10)
        );
        assertTrue(ex.getMessage().contains("at least 1 month"),
                "Expected corporate‐minimum‐days rule");

        // 4. Verify no new rental was inserted
        int after = rentalService.listRentalsByUser(corp).size();
        assertEquals(before, after, "Transaction should have rolled back with no new record");
    }
}
