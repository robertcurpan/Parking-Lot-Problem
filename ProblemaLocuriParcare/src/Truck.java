public class Truck extends Vehicle
{

    public Truck(String color, int price, boolean electric)
    {
        super(2, color, price, electric);
    }

    @Override
    public String getDescription()
    {
        String res = "The truck is " + color + " and it costs " + price + " euro!";
        return res;
    }
}
