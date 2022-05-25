package structures;

import vehicles.VehicleType;

public class ParkingSpotIdAndVehicleTypeId
{
    private int spotId;
    private int vehicleTypeId;

    public ParkingSpotIdAndVehicleTypeId()
    {

    }

    public ParkingSpotIdAndVehicleTypeId(int spotId, int vehicleTypeId)
    {
        this.spotId = spotId;
        this.vehicleTypeId = vehicleTypeId;
    }

    public int getParkingSpotId() { return spotId; }
    public int getVehicleTypeId() { return vehicleTypeId; }
}
