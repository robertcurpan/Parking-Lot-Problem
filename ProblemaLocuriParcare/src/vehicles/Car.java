package vehicles;

public class Car extends Vehicle
{
    public Car(String color, int price, boolean electric)
    {
        super(VehicleType.Car.ordinal(), color, price, electric);
    }

    @Override
    public String getDescription()
    {
        String res = "The car is " + color + " and it costs " + price + " euro!";
        return res;
    }
}
