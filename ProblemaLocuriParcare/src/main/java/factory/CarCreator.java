package factory;

import parking.Driver;
import vehicles.Car;
import vehicles.Vehicle;

public class CarCreator extends VehicleCreator {
    @Override
    public Vehicle getVehicle(int vehicleId, Driver driver, String color, int price, boolean electric) {
        return new Car(vehicleId, driver, color, price, electric);
    }
}
