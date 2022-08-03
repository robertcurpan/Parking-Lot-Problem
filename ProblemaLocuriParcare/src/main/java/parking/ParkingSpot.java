package parking;

import java.util.UUID;

public class ParkingSpot
{
    private int id;
    private UUID vehicleId;
    private ParkingSpotType spotType;
    private boolean electric;
    private int version;

    public ParkingSpot() {

    }

    public ParkingSpot(int id, UUID vehicleId, ParkingSpotType spotType, boolean electric, int version)
    {
        this.id = id;
        this.vehicleId = vehicleId;
        this.spotType = spotType;
        this.electric = electric;
        this.version = version;
    }

    public int getId() { return this.id; }
    public UUID getVehicleId() { return this.vehicleId; }
    public boolean getElectric() { return this.electric; }
    public ParkingSpotType getSpotType() { return this.spotType; }
    public int getVersion() { return this.version; }


    public void setId(int id) { this.id = id; }
    public void setVehicleId(UUID vehicleId) { this.vehicleId = vehicleId; }
    public void setSpotType(ParkingSpotType spotType) { this.spotType = spotType; }
    public void setElectric(boolean electric) { this.electric = electric; }
    public void setVersion(int version) { this.version = version; }

    @Override
    public String toString()
    {
        return "Spot with id: " + id + "[" + spotType.toString() + " (vehicleId = " + vehicleId + ") ], electric: " + electric;
    }

}
