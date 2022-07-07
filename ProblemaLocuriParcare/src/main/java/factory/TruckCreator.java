package factory;

import parking.Driver;
import vehicles.Truck;
import vehicles.Vehicle;

public class TruckCreator extends VehicleCreator {
    @Override
    public Vehicle getVehicle(Driver driver, String color, int price, boolean electric) {
        return new Truck(driver, color, price, electric);
    }
}
