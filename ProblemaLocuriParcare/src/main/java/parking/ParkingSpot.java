package parking;

import vehicles.VehicleType;

public class ParkingSpot
{
    private int id;
    private int vehicleId;
    private ParkingSpotType spotType;
    private boolean free;
    private boolean electric;

    public ParkingSpot(int id, int vehicleId, ParkingSpotType spotType, boolean free, boolean electric)
    {
        this.id = id;
        this.vehicleId = vehicleId;
        this.spotType = spotType;
        this.free = free;
        this.electric = electric;
    }

    public int getId() { return this.id; }
    public int getVehicleId() { return this.vehicleId; }
    public boolean getFree() { return this.free; }
    public boolean getElectricCharger() { return this.electric; }
    public ParkingSpotType getSpotType() { return this.spotType; }

    public void setFree(boolean free) { this.free = free; }
    public void setElectric(boolean electric) { this.electric = electric; }
    public void setVehicleId(int vehicleId) { this.vehicleId = vehicleId; }

    @Override
    public String toString()
    {
        return "Spot with id: " + id + "[" + spotType.getParkingSpotTypeName() + " (vehicleId = " + vehicleId + ") ] -> free: " + free + ", electric: " + electric;
    }

}
