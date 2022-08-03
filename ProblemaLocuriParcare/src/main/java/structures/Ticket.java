package structures;

import parking.ParkingSpot;
import vehicles.Vehicle;

public class Ticket {
    private ParkingSpot parkingSpot;
    private Vehicle vehicle;

    public Ticket(ParkingSpot parkingSpot, Vehicle vehicle) { this.parkingSpot = parkingSpot; this.vehicle = vehicle; }

    public ParkingSpot getParkingSpot() { return parkingSpot; }
    public Vehicle getVehicle() { return vehicle; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }
}
