package vehicles;

import parking.Driver;

public class Truck extends Vehicle
{

    public Truck(int vehicleId, Driver driver, String color, int price, boolean electric)
    {
        super(vehicleId, VehicleType.TRUCK, driver, color, price, electric);
    }

    @Override
    public String getDescription()
    {
        return "The truck is " + color + " and it costs " + price + " euro!" + " [Driver: " + driver.toString() + "]";
    }
}
