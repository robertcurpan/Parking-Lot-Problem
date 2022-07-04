package vehicles;

import parking.Driver;

public class Motorcycle extends Vehicle
{

    public Motorcycle(int vehicleId, Driver driver, String color, int price, boolean electric)
    {
        super(vehicleId, VehicleType.MOTORCYCLE, driver, color, price, electric);
    }

    @Override
    public String getDescription()
    {
        return "The motorcycle is " + color + " and it costs " + price + " euro!" + " [Driver: " + driver.toString() + "]";
    }
}
