package vehicles;

import parking.Driver;

public class Car extends Vehicle
{
    public Car(int vehicleId, Driver driver, String color, int price, boolean electric)
    {
        super(vehicleId, VehicleType.CAR, driver, color, price, electric);
    }

    @Override
    public String getDescription()
    {
        return "The car is " + color + " and it costs " + price + " euro!" + " [Driver: " + driver.toString() + "]";
    }
}
