package org.velihangozek.javarentalcli;

import org.velihangozek.javarentalcli.model.Rental;
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
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class VeloRentalMain {

    private static final Scanner scan = new Scanner(System.in);
    private static final UserService userService = new UserServiceImpl();
    private static final VehicleService vehicleService = new VehicleServiceImpl();
    private static final RentalService rentalService = new RentalServiceImpl();

    public static void main(String[] args) throws Exception {

        while (true) {

            System.out.println("\n=== Welcome to the Velo Rental System :) ===\n");
            System.out.println("1) Register");
            System.out.println("2) Login");
            System.out.println("0) Exit");
            System.out.print("\n> ");

            switch (scan.nextLine()) {
                case "1" -> doRegister();
                case "2" -> {
                    Optional<User> optUser = doLogin();
                    optUser.ifPresent(VeloRentalMain::menuFor);
                }
                case "0" -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void doRegister() throws Exception {

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
    }

    private static Optional<User> doLogin() throws Exception {

        System.out.print("Email: ");
        String email = scan.nextLine();
        System.out.print("Password: ");
        String pw = scan.nextLine();

        Optional<User> opt = userService.login(email, pw);
        System.out.println(opt.isPresent() ? "Login OK" : "Login failed");
        return opt;
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

    private static void adminMenu() throws Exception {
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

    private static void customerMenu(User u) throws Exception {
        while (true) {
            System.out.println("\n--- Customer Menu ---\n");
            System.out.println("1) List all vehicles");
            System.out.println("2) Filter by type");
            System.out.println("3) Rent vehicle");
            System.out.println("4) My rentals");
            System.out.println("0) Logout");
            System.out.print("> ");
            switch (scan.nextLine()) {
                case "1" -> listAllVehicles();
                case "2" -> filterByType();
                case "3" -> rentVehicle(u);
                case "4" -> listRentals(u);
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
        } catch (Exception e) {
            System.err.println("Failed to add vehicle: " + e.getMessage());
        }
    }

    // Shared helpers
    private static void listAllVehicles() {
        try {
            List<Vehicle> all = vehicleService.listAll();
            all.forEach(System.out::println);
        } catch (Exception e) {
            System.err.println("Error listing vehicles: " + e.getMessage());
        }
    }

    private static void filterByType() {
        try {
            System.out.print("Type to filter (CAR/HELICOPTER/MOTORCYCLE): ");
            VehicleType type = VehicleType.valueOf(scan.nextLine().toUpperCase());
            vehicleService.listByType(type)
                    .forEach(System.out::println);
        } catch (Exception e) {
            System.err.println("Error filtering: " + e.getMessage());
        }
    }

    // Customer helper
    private static void rentVehicle(User u) {
        try {
            System.out.print("Vehicle ID: ");
            int vid = Integer.parseInt(scan.nextLine());
            System.out.print("Period (HOURLY/DAILY/WEEKLY/MONTHLY): ");
            PeriodType p = PeriodType.valueOf(scan.nextLine().toUpperCase());
            System.out.print("Quantity: ");
            int qty = Integer.parseInt(scan.nextLine());

            Rental r = rentalService.rent(u, vid, p, qty);
            System.out.println("Rental created: " + r);
        } catch (Exception e) {
            System.err.println("Failed to rent: " + e.getMessage());
        }
    }

    private static void listRentals(User u) {
        try {
            rentalService.listRentalsByUser(u)
                    .forEach(System.out::println);
        } catch (Exception e) {
            System.err.println("Error listing rentals: " + e.getMessage());
        }
    }
}