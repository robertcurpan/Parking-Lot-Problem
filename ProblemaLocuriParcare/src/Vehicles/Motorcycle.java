package Vehicles;

public class Motorcycle extends Vehicle
{

    public Motorcycle(String color, int price, boolean electric)
    {
        super(VehicleType.Motorcycle.ordinal(), color, price, electric);
    }

    @Override
    public String getDescription()
    {
        String res = "The motorcycle is " + color + " and it costs " + price + " euro!";
        return res;
    }
}
