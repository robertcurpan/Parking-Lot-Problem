public class Driver
{
    private String name;
    private VehicleType vehicleType;
    private boolean vipStatus;

    public Driver(String name, VehicleType vehicleType, boolean vipStatus)
    {
        this.name = name;
        this.vehicleType = vehicleType;
        this.vipStatus = vipStatus;
    }

    public String getName() { return name; }
    public VehicleType getVehicleType() { return vehicleType; }
    public boolean getVipStatus() { return vipStatus; }
    public void setName(String name) { this.name = name; }
    public void setVehicleType(VehicleType vehicleType) { this.vehicleType = vehicleType; }
    public void setVipStatus(boolean vipStatus) { this.vipStatus = vipStatus; }

    @Override
    public String toString()
    {
        String str = "(" + getName() + ", " + getVehicleType() + ", ";
        if(getVipStatus() == true)
            str += "VIP)";
        else
            str += "NonVIP)";

        return str;
    }

}
