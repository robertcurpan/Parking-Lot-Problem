package structures;

import vehicles.Vehicle;

public class Ticket {
    private int spotId;
    private Vehicle vehicle;

    public Ticket(int spotId, Vehicle vehicle) { this.spotId = spotId; this.vehicle = vehicle; }

    public int getSpotId() { return spotId; }
    public Vehicle getVehicle() { return vehicle; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }
}
