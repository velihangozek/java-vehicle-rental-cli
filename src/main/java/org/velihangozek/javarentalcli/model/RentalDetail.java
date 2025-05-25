package org.velihangozek.javarentalcli.model;

import java.time.LocalDateTime;

public class RentalDetail {
    private int rentalId;
    private String brand;
    private String model;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double totalPrice;
    private Double deposit;

    public RentalDetail() { }

    // Getters & setters
    public int getRentalId() { return rentalId; }
    public void setRentalId(int rentalId) { this.rentalId = rentalId; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public Double getDeposit() { return deposit; }
    public void setDeposit(Double deposit) { this.deposit = deposit; }

    @Override
    public String toString() {
        return String.format(
                "Rental #%d — %s %s from %s to %s, total ₺%.2f%s",
                rentalId, brand, model,
                startTime, endTime,
                totalPrice,
                deposit != null ? String.format(", deposit ₺%.2f", deposit) : ""
        );
    }
}
