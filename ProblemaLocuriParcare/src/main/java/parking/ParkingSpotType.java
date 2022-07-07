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

}