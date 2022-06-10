package parking;

import vehicles.Vehicle;

public class Driver {
    private String name;
    private Vehicle vehicle;
    private boolean vipStatus;

    public Driver(String name, Vehicle vehicle, boolean vipStatus) {
        this.name = name;
        this.vehicle = vehicle;
        this.vipStatus = vipStatus;
    }

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

}
