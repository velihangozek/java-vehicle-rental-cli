package org.velihangozek.javarentalcli.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Rental {
    private Integer id;
    private Integer userId;
    private Integer vehicleId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double totalPrice;
    private Double deposit;

    public Rental() { }

    public Rental(Integer id, Integer userId, Integer vehicleId,
                  LocalDateTime startTime, LocalDateTime endTime,
                  double totalPrice, Double deposit) {
        this.id = id;
        this.userId = userId;
        this.vehicleId = vehicleId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalPrice = totalPrice;
        this.deposit = deposit;
    }

    // Getters & setters...
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public Integer getVehicleId() { return vehicleId; }
    public void setVehicleId(Integer vehicleId) { this.vehicleId = vehicleId; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public Double getDeposit() { return deposit; }
    public void setDeposit(Double deposit) { this.deposit = deposit; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rental)) return false;
        Rental that = (Rental) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Rental{" +
                "id=" + id +
                ", userId=" + userId +
                ", vehicleId=" + vehicleId +
                ", start=" + startTime +
                ", end=" + endTime +
                ", total=" + totalPrice +
                '}';
    }
}
