public class Driver
{
    private String name;
    private Vehicle vehicle;
    private boolean vipStatus;

    public Driver(String name, Vehicle vehicle, boolean vipStatus)
    {
        this.name = name;
        this.vehicle = vehicle;
        this.vipStatus = vipStatus;
    }

    public String getName() { return name; }
    public Vehicle getVehicle() { return vehicle; }
    public boolean getVipStatus() { return vipStatus; }

    @Override
    public String toString()
    {
        String str = "(" + getName() + ", " + getVehicle().getDescription() + ", ";

        if(getVipStatus() == true)
            str += "VIP, ";
        else
            str += "NonVIP, ";

        if(vehicle.isElectric())
            str += "Electric vehicle)";
        else
            str += "Non-electric vehicle)";

        return str;
    }

}
