package parking;

import vehicles.Vehicle;

public class Driver {
    private int id = 0;
    private String name;
    private Vehicle vehicle;
    private boolean vipStatus;

    public Driver(String name, Vehicle vehicle, boolean vipStatus) {
        this.name = name;
        this.vehicle = vehicle;
        this.vipStatus = vipStatus;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getName() {
        return name;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public boolean getVipStatus() {
        return vipStatus;
    }

    @Override
    public String toString() {
        String vipStatus = getVipStatus() ? "VIP" : "NonVIP";
        String isElectric = vehicle.isElectric() ? "Electric vehicle" : "Non-electric vehicle";

        return String.format("(%s, %s, %s, %s)", getName(), getVehicle().getDescription(), vipStatus, isElectric);
    }

    @Override
    public boolean equals(Object object) {
        if(object instanceof Driver) {
            Driver driver = (Driver) object;
            return this.name.equals(driver.name) && this.vipStatus == driver.vipStatus && this.vehicle.equals(driver.vehicle);
        }
        return false;
    }

}
