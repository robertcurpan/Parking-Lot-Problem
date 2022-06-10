package parking;

import vehicles.VehicleType;

public class ParkingSpot
{
    private int id;
    private VehicleType spotType; // loc pt masina, motocicleta etc.
    private boolean free;
    private boolean electric;

    public ParkingSpot(int id, VehicleType spotType, boolean free, boolean electric)
    {
        this.id = id;
        this.spotType = spotType;
        this.free = free;
        this.electric = electric;
    }

    public int getId() { return this.id; }
    public boolean isFree() { return this.free; }
    public boolean hasElectricCharger() { return this.electric; }
    public VehicleType getSpotType() { return this.spotType; }

    public void setId(int id) { this.id = id; }
    public void setFree(boolean free) { this.free = free; }
    public void setElectric(boolean electric) { this.electric = electric; }
    public void setSpotType(VehicleType spotType) { this.spotType = spotType; }

    @Override
    public String toString()
    {
        return "Spot with id: " + id + "[" + spotType + "] -> free: " + free + ", electric: " + electric;
    }

}
