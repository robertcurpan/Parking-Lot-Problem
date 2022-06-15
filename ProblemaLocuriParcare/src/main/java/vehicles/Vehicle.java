package vehicles;

public abstract class Vehicle
{
    protected VehicleType type;
    protected String color;
    protected int price;
    protected boolean electric;

    public Vehicle(VehicleType type, String color, int price, boolean electric)
    {
        this.type = type;
        this.color = color;
        this.price = price;
        this.electric = electric;
    }

    public VehicleType getType() { return type; }
    public String getVehicleType() { return type.toString(); }
    public int getPrice() { return price; }
    public String getColor() { return color; }
    public boolean isElectric() { return electric; }
    public abstract String getDescription();
}
