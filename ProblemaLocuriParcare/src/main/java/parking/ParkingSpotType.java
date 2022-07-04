package parking;

import vehicles.VehicleType;

public enum ParkingSpotType {
    SMALL("Small"), MEDIUM("Medium"), LARGE("Large");

    private String parkingSpotTypeName;

    ParkingSpotType(String parkingSpotTypeName) { this.parkingSpotTypeName = parkingSpotTypeName; }

    public String getParkingSpotTypeName() { return parkingSpotTypeName; }

    public static ParkingSpotType getParkingSpotTypeByName(String parkingSpotTypeName) {
        ParkingSpotType parkingSpotType = null;
        switch(parkingSpotTypeName) {
            case "Small": parkingSpotType = SMALL; break;
            case "Medium": parkingSpotType = MEDIUM; break;
            case "Large": parkingSpotType = LARGE; break;
        }

        return parkingSpotType;
    }

    public static ParkingSpotType getSmallestFittingParkingSpotTypeFromVehicleType(VehicleType vehicleType) {
        ParkingSpotType parkingSpotType = null;
        switch(vehicleType) {
            case MOTORCYCLE: parkingSpotType = SMALL; break;
            case CAR: parkingSpotType = MEDIUM; break;
            case TRUCK: parkingSpotType = LARGE; break;
        }

        return parkingSpotType;
    }

}