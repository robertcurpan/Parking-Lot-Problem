package structures;

public class ParkingSpotIdAndVehicleTypeId
{
    private int spotId;
    private int vehicleTypeId;

    public ParkingSpotIdAndVehicleTypeId()
    {
        spotId = -1;
        vehicleTypeId = -1;
    }

    public ParkingSpotIdAndVehicleTypeId(int spotId, int vehicleTypeId)
    {
        this.spotId = spotId;
        this.vehicleTypeId = vehicleTypeId;
    }

    public int getParkingSpotId() { return spotId; }
    public int getVehicleTypeId() { return vehicleTypeId; }
}
