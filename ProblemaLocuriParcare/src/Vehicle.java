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
    public boolean isElectric() { return electric; }
    public abstract String getDescription();
}
