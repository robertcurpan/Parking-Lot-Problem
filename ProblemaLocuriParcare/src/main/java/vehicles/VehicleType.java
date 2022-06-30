package vehicles;

public enum VehicleType {
    MOTORCYCLE("Motorcycle"), CAR("Car"), TRUCK("Truck");

    private String vehicleTypeName;

    VehicleType(String vehicleTypeName) {
        this.vehicleTypeName = vehicleTypeName;
    }

    public String getVehicleTypeName() { return vehicleTypeName; }

    public static VehicleType getVehicleTypeByName(String vehicleTypeName) {
        VehicleType vehicleType = null;
        switch(vehicleTypeName) {
            case "Motorcycle": vehicleType = MOTORCYCLE; break;
            case "Car": vehicleType = CAR; break;
            case "Truck": vehicleType = TRUCK; break;
        }

        return vehicleType;
    }

}
