package org.velihangozek.javarentalcli;

import org.velihangozek.javarentalcli.exception.VeloAuthenticationException;
import org.velihangozek.javarentalcli.exception.VeloBusinessRuleException;
import org.velihangozek.javarentalcli.exception.VeloDataAccessException;
import org.velihangozek.javarentalcli.model.Rental;
import org.velihangozek.javarentalcli.model.RentalDetail;
import org.velihangozek.javarentalcli.model.User;
import org.velihangozek.javarentalcli.model.Vehicle;
import org.velihangozek.javarentalcli.model.enums.PeriodType;
import org.velihangozek.javarentalcli.model.enums.VehicleType;
import org.velihangozek.javarentalcli.service.RentalService;
import org.velihangozek.javarentalcli.service.UserService;
import org.velihangozek.javarentalcli.service.VehicleService;
import org.velihangozek.javarentalcli.service.impl.RentalServiceImpl;
import org.velihangozek.javarentalcli.service.impl.UserServiceImpl;
import org.velihangozek.javarentalcli.service.impl.VehicleServiceImpl;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class VeloRentalMain {

    private static final int PAGE_SIZE = 5;
    private static final Scanner scan = new Scanner(System.in);
    private static final UserService userService = new UserServiceImpl();
    private static final VehicleService vehicleService = new VehicleServiceImpl();
    private static final RentalService rentalService = new RentalServiceImpl();

    public static void main(String[] args) {

        while (true) {

            System.out.println("\n=== Welcome to the Velo Rental System :) ===\n");
            System.out.println("1) Register");
            System.out.println("2) Login");
            System.out.println("0) Exit");
            System.out.print("\n> ");

            String choice = scan.nextLine();
            switch (choice) {
                case "1" -> doRegister();
                case "2" -> {
                    try {
                        User u = doLogin();
                        menuFor(u);
                    } catch (VeloAuthenticationException ex) {
                        System.err.println("Login failed: " + ex.getMessage());
                    } catch (VeloDataAccessException dae) {
                        System.err.println("Database error: " + dae.getMessage());
                    }
                }
                case "0" -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void doRegister() {
        try {
            System.out.print("Email: ");
            String email = scan.nextLine();
            System.out.print("Password: ");
            String pw = scan.nextLine();
            System.out.print("Role (ADMIN/CUSTOMER): ");
            String role = scan.nextLine().toUpperCase();
            System.out.print("Birthdate (YYYY-MM-DD): ");
            LocalDate bd = LocalDate.parse(scan.nextLine());
            System.out.print("Corporate? (true/false): ");
            boolean corp = Boolean.parseBoolean(scan.nextLine());

            User u = userService.register(email, pw, role, bd, corp);
            System.out.println("Registered: " + u);
        } catch (VeloBusinessRuleException bre) {
            System.err.println("Registration error: " + bre.getMessage());
        } catch (VeloDataAccessException dae) {
            System.err.println("Database error: " + dae.getMessage());
        }

    }

    private static User doLogin() throws VeloAuthenticationException {

        System.out.print("Email: ");
        String email = scan.nextLine();
        System.out.print("Password: ");
        String pw = scan.nextLine();

        User u = userService.login(email, pw);
        System.out.println("Login OK");
        return u;
    }

    private static void menuFor(User u) {
        try {
            System.out.println("\nWelcome, " + u.getEmail() + " [" + u.getRole() + "]");
            if ("ADMIN".equals(u.getRole())) adminMenu();
            else customerMenu(u);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void adminMenu() {
        while (true) {
            System.out.println("\n--- Admin Menu ---\n");
            System.out.println("1) Add vehicle");
            System.out.println("2) List vehicles");
            System.out.println("0) Logout");
            System.out.print("> ");
            switch (scan.nextLine()) {
                case "1" -> addVehicle();
                case "2" -> listAllVehicles();
                case "0" -> {
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void customerMenu(User u) {
        while (true) {
            System.out.println("\n--- Customer Menu ---\n");
            System.out.println("1) List all vehicles");
            System.out.println("2) Filter by type");
            System.out.println("3) Search vehicles");
            System.out.println("4) Rent vehicle");
            System.out.println("5) My rentals");
            System.out.println("0) Logout");
            System.out.print("> ");
            switch (scan.nextLine()) {
                case "1" -> listAllVehicles();
                case "2" -> filterByType();
                case "3" -> searchVehicles();
                case "4" -> rentVehicle(u);
                case "5" -> listRentals(u);
                case "0" -> {
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    // Admin helper
    private static void addVehicle() {
        try {
            System.out.print("Type (CAR/HELICOPTER/MOTORCYCLE): ");
            VehicleType type = VehicleType.valueOf(scan.nextLine().toUpperCase());
            System.out.print("Brand: ");
            String brand = scan.nextLine();
            System.out.print("Model: ");
            String model = scan.nextLine();
            System.out.print("Purchase price: ");
            long price = Long.parseLong(scan.nextLine());
            System.out.print("Rate hourly: ");
            double rh = Double.parseDouble(scan.nextLine());
            System.out.print("Rate daily: ");
            double rd = Double.parseDouble(scan.nextLine());
            System.out.print("Rate weekly: ");
            double rw = Double.parseDouble(scan.nextLine());
            System.out.print("Rate monthly: ");
            double rm = Double.parseDouble(scan.nextLine());

            Vehicle v = vehicleService.addVehicle(type, brand, model, price, rh, rd, rw, rm);
            System.out.println("Added vehicle: " + v);
        } catch (VeloBusinessRuleException bre) {
            System.err.println("Cannot add: " + bre.getMessage());
        } catch (VeloDataAccessException dae) {
            System.err.println("Database error: " + dae.getMessage());
        }
    }

    // Shared helpers
    private static void listAllVehicles() {
        try {
            List<Vehicle> all = vehicleService.listAll();
            // all.forEach(System.out::println);
            paginateVehicles(all);
        } catch (VeloBusinessRuleException bre) {
            System.err.println("Cannot list: " + bre.getMessage());
        } catch (VeloDataAccessException dae) {
            System.err.println("Database error: " + dae.getMessage());
        }
    }

    private static void filterByType() {
        try {
            System.out.print("Type to filter (CAR/HELICOPTER/MOTORCYCLE): ");
            String raw = scan.nextLine().trim();
            VehicleType type = parseEnum(VehicleType.class, raw);
            List<Vehicle> byType = vehicleService.listByType(type);
            paginateVehicles(byType);
        } catch (VeloBusinessRuleException bre) {
            System.err.println("Cannot filter: " + bre.getMessage());
        } catch (VeloDataAccessException dae) {
            System.err.println("Database error: " + dae.getMessage());
        }
    }

    private static void searchVehicles() {
        System.out.print("Enter keyword to search (brand/model): ");
        String keyword = scan.nextLine().trim();
        try {
            List<Vehicle> results = vehicleService.search(keyword);
            paginateVehicles(results);
        } catch (VeloBusinessRuleException bre) {
            System.err.println("Cannot search: " + bre.getMessage());
        } catch (VeloDataAccessException dae) {
            System.err.println("Database error: " + dae.getMessage());
        }
    }


    // Customer helper
    private static void rentVehicle(User u) {
        try {
            System.out.print("Vehicle ID: ");
            int vid = Integer.parseInt(scan.nextLine());

            System.out.print("Period (HOURLY/DAILY/WEEKLY/MONTHLY): ");
            PeriodType period = parseEnum(PeriodType.class, scan.nextLine());

            System.out.print("Quantity: ");
            int qty = Integer.parseInt(scan.nextLine());

            Rental r = rentalService.rent(u, vid, period, qty);
            var detail = rentalService.getFullDetail(r.getId());
            System.out.println("Rental created: " + detail);

        } catch (VeloBusinessRuleException bre) {
            // catches both bad enum *and* business‐rule errors
            System.err.println("Cannot rent: " + bre.getMessage());
        } catch (VeloDataAccessException dae) {
            System.err.println("Database error: " + dae.getMessage());
        } catch (NumberFormatException nfe) {
            System.err.println("Please enter a valid integer number.");
        }
    }

    private static void listRentals(User u) {
        try {
            List<RentalDetail> details = rentalService.listRentalDetailsByUser(u);
            if (details.isEmpty()) {
                System.out.println("You have no past rentals.");
            } else {
                details.forEach(System.out::println);
            }
        } catch (VeloBusinessRuleException bre) {
            System.err.println("Cannot list rentals: " + bre.getMessage());
        } catch (VeloDataAccessException dae) {
            System.err.println("Database error: " + dae.getMessage());
        }
    }

    /**
     * Paginate any list of Vehicles in the console.
     */
    private static void paginateVehicles(List<Vehicle> vehicles) {
        if (vehicles.isEmpty()) {
            System.out.println("No vehicles found.");
            return;
        }
        int total = vehicles.size();
        int pages = (total + PAGE_SIZE - 1) / PAGE_SIZE;
        int current = 0;

        while (true) {
            int start = current * PAGE_SIZE;
            int end = Math.min(start + PAGE_SIZE, total);
            System.out.printf("-- Vehicles %d–%d of %d --%n", start + 1, end, total);
            //vehicles.subList(start, end).forEach(System.out::println);

            System.out.printf("%-4s %-12s %-15s %-15s %8s %8s %8s %10s %12s %10s%n",
                    "ID", "Type", "Brand", "Model",
                    "Hourly", "Daily", "Weekly", "Monthly",
                    "Purchase", "Deposit"
            );
            System.out.println("------------------------------------------------------------------------------------------------");
            for (Vehicle v : vehicles.subList(start, end)) {
                // compute deposit only for purchasePrice > 2M
                String depoStr = v.getPurchasePrice() > 2_000_000
                        ? String.format("%.2f", v.getPurchasePrice() * 0.10)
                        : "-";

                System.out.printf("%-4d %-12s %-15s %-15s %8.2f %8.2f %8.2f %10.2f %12d %10s%n",
                        v.getId(),
                        v.getType(),
                        v.getBrand(),
                        v.getModel(),
                        v.getRateHourly(),
                        v.getRateDaily(),
                        v.getRateWeekly(),
                        v.getRateMonthly(),
                        v.getPurchasePrice(),
                        depoStr
                );
            }

            // Menu
            System.out.printf("[n]ext page (%d/%d), [p]revious, [x]exit: ", current + 1, pages);
            String choice = scan.nextLine().trim().toLowerCase();
            if ("n".equals(choice) && current < pages - 1) {
                current++;
            } else if ("p".equals(choice) && current > 0) {
                current--;
            } else if ("x".equals(choice)) {
                break;
            } else {
                System.out.println("Invalid or no more pages.");
            }
        }
    }

    private static <E extends Enum<E>> E parseEnum(Class<E> enumClass, String raw) {
        try {
            return Enum.valueOf(enumClass, raw.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new VeloBusinessRuleException(
                    "Invalid " + enumClass.getSimpleName() +
                            " \"" + raw + "\". Valid values: " +
                            Arrays.toString(enumClass.getEnumConstants())
            );
        }
    }
}