package vehicles;

public class Truck extends Vehicle
{

    public Truck(String color, int price, boolean electric)
    {
        super(VehicleType.Truck.ordinal(), color, price, electric);
    }

    @Override
    public String getDescription()
    {
        String res = "The truck is " + color + " and it costs " + price + " euro!";
        return res;
    }
}
