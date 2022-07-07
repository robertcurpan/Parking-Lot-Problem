package factory;

import parking.Driver;
import vehicles.Vehicle;

public abstract class VehicleCreator {
    public abstract Vehicle getVehicle(Driver driver, String color, int price, boolean electric);
}
