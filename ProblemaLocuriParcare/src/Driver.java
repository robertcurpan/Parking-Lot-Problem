public class Driver
{
    private String _name;
    private VehicleType _vehicleType;
    private boolean _vipStatus;

    public Driver(String name, VehicleType vehicleType, boolean vipStatus)
    {
        _name = name;
        _vehicleType = vehicleType;
        _vipStatus = vipStatus;
    }

    public String getName() { return _name; }
    public VehicleType getVehicleType() { return _vehicleType; }
    public boolean getVipStatus() { return _vipStatus; }
    public void setName(String name) { _name = name; }
    public void setVehicleType(VehicleType vehicleType) { _vehicleType = vehicleType; }
    public void setVipStatus(boolean vipStatus) { _vipStatus = vipStatus; }

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
