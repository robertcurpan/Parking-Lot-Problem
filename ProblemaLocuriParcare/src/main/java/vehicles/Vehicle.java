package vehicles;

public abstract class Vehicle
{
    protected int type;
    protected String color;
    protected int price;
    protected boolean electric;

    public Vehicle(int type, String color, int price, boolean electric)
    {
        this.type = type;
        this.color = color;
        this.price = price;
        this.electric = electric;
    }

    public int getType() { return type; }
    public String getVehicleType() { return VehicleType.values()[type].toString(); }
    public int getPrice() { return price; }
    public String getColor() { return color; }
    public boolean isElectric() { return electric; }
    public abstract String getDescription();
}
