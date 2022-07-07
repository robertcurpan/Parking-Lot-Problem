package vehicles;

import parking.Driver;

import java.util.UUID;

public abstract class Vehicle
{
    protected UUID vehicleId;
    protected VehicleType type;
    protected Driver driver;
    protected String color;
    protected int price;
    protected boolean electric;

    public Vehicle() {

    }

    public Vehicle(VehicleType type, Driver driver, String color, int price, boolean electric)
    {
        this.vehicleId = UUID.randomUUID();
        this.type = type;
        this.driver = driver;
        this.color = color;
        this.price = price;
        this.electric = electric;
    }

    public UUID getVehicleId() { return vehicleId; }
    public VehicleType getVehicleType() { return type; }
    public String getColor() { return color; }
    public int getPrice() { return price; }
    public Driver getDriver() { return driver; }
    public boolean getElectric() { return electric; }
    public void setVehicleId(UUID vehicleId) { this.vehicleId = vehicleId; }

    public abstract String getDescription();
    @Override
    public boolean equals(Object object) {
        if(object instanceof Vehicle) {
            Vehicle vehicle = (Vehicle) object;
            return this.type.equals(vehicle.getVehicleType()) && this.driver.equals(vehicle.getDriver()) && this.color.equals(vehicle.color) && this.price == vehicle.price && this.electric == vehicle.getElectric();
        }
        return false;
    }
}
