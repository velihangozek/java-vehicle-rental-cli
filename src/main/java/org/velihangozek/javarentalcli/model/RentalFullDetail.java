// src/main/java/org/velihangozek/javarentalcli/model/RentalFullDetail.java
package org.velihangozek.javarentalcli.model;

import java.time.LocalDateTime;

public class RentalFullDetail {
    private int rentalId;
    private String userEmail;
    private String vehicleBrand;
    private String vehicleModel;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double totalPrice;
    private Double deposit;

    public RentalFullDetail() {}

    public int getRentalId() {
        return rentalId;
    }

    public void setRentalId(int rentalId) {
        this.rentalId = rentalId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getVehicleBrand() {
        return vehicleBrand;
    }

    public void setVehicleBrand(String vehicleBrand) {
        this.vehicleBrand = vehicleBrand;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getDeposit() {
        return deposit;
    }

    public void setDeposit(Double deposit) {
        this.deposit = deposit;
    }

    @Override
    public String toString() {
        return String.format(
                "Rental #%d by %s — %s %s from %s to %s (₺%.2f%s)",
                rentalId, userEmail,
                vehicleBrand, vehicleModel,
                startTime, endTime,
                totalPrice,
                deposit != null ? String.format(", deposit ₺%.2f", deposit) : ""
        );
    }
}
