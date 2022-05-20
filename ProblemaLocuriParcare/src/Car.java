public class Car extends Vehicle
{
    public Car(String color, int price, boolean electric)
    {
        super(1, color, price, electric);
    }

    @Override
    public String getDescription()
    {
        String res = "The car is " + color + " and it costs " + price + " euro!";
        return res;
    }
}
