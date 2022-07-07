package strategy;

import parking.ParkingSpotType;
import vehicles.VehicleType;

public class TicketGeneratorUtil {

    public static ParkingSpotType getSmallestFittingParkingSpotTypeFromVehicleType(VehicleType vehicleType) {
        ParkingSpotType parkingSpotType = null;
        switch(vehicleType) {
            case MOTORCYCLE: parkingSpotType = ParkingSpotType.SMALL; break;
            case CAR: parkingSpotType = ParkingSpotType.MEDIUM; break;
            case TRUCK: parkingSpotType = ParkingSpotType.LARGE; break;
        }

        return parkingSpotType;
    }
}
