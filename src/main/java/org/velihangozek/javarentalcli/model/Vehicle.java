package org.velihangozek.javarentalcli.model;

import org.velihangozek.javarentalcli.model.enums.VehicleType;

import java.util.Objects;

public class Vehicle {
    private Integer id;
    private VehicleType type;       // enum below
    private String brand;
    private String model;
    private long purchasePrice;
    private double rateHourly;
    private double rateDaily;
    private double rateWeekly;
    private double rateMonthly;

    public Vehicle() {
    }

    public Vehicle(Integer id, VehicleType type, String brand, String model,
                   long purchasePrice, double rateHourly, double rateDaily,
                   double rateWeekly, double rateMonthly) {
        this.id = id;
        this.type = type;
        this.brand = brand;
        this.model = model;
        this.purchasePrice = purchasePrice;
        this.rateHourly = rateHourly;
        this.rateDaily = rateDaily;
        this.rateWeekly = rateWeekly;
        this.rateMonthly = rateMonthly;
    }

    // Getters & setters...
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public VehicleType getType() {
        return type;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public long getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(long purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public double getRateHourly() {
        return rateHourly;
    }

    public void setRateHourly(double rateHourly) {
        this.rateHourly = rateHourly;
    }

    public double getRateDaily() {
        return rateDaily;
    }

    public void setRateDaily(double rateDaily) {
        this.rateDaily = rateDaily;
    }

    public double getRateWeekly() {
        return rateWeekly;
    }

    public void setRateWeekly(double rateWeekly) {
        this.rateWeekly = rateWeekly;
    }

    public double getRateMonthly() {
        return rateMonthly;
    }

    public void setRateMonthly(double rateMonthly) {
        this.rateMonthly = rateMonthly;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vehicle)) return false;
        Vehicle that = (Vehicle) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", type=" + type +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                '}';
    }

}
