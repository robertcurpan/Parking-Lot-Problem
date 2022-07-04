package factory;

import parking.Driver;
import vehicles.Vehicle;

public abstract class VehicleCreator {
    public abstract Vehicle getVehicle(int vehicleId, Driver driver, String color, int price, boolean electric);
}
