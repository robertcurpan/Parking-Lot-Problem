package factory;

import vehicles.VehicleType;

public class VehicleCreatorGenerator {

    public static VehicleCreator getVehicleCreator(VehicleType vehicleType) {
        VehicleCreator vehicleCreator = null;
        switch(vehicleType)
        {
            case MOTORCYCLE: vehicleCreator = new MotorcycleCreator(); break;
            case CAR: vehicleCreator = new CarCreator(); break;
            case TRUCK: vehicleCreator = new TruckCreator(); break;
            default: break;
        }

        return vehicleCreator;
    }
}
