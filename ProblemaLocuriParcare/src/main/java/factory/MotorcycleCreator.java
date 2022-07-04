package factory;

import parking.Driver;
import vehicles.Motorcycle;
import vehicles.Vehicle;

public class MotorcycleCreator extends VehicleCreator {
    @Override
    public Vehicle getVehicle(int vehicleId, Driver driver, String color, int price, boolean electric) {
        return new Motorcycle(vehicleId, driver, color, price, electric);
    }
}
