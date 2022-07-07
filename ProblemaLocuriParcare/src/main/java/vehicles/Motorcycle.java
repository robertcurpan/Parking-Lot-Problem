package vehicles;

import parking.Driver;

public class Motorcycle extends Vehicle
{
    public Motorcycle() {
        super();
    }
    public Motorcycle(Driver driver, String color, int price, boolean electric)
    {
        super(VehicleType.MOTORCYCLE, driver, color, price, electric);
    }

    @Override
    public String getDescription()
    {
        return "The motorcycle is " + color + " and it costs " + price + " euro!" + " [Driver: " + driver.toString() + "]";
    }
}
