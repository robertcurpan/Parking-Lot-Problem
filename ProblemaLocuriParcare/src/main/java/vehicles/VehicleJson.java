package vehicles;

import parking.Driver;

public class VehicleJson {
    private String type;
    private Driver driver;
    private String color;
    private int price;
    private boolean electric;

    public VehicleJson(String type, Driver driver, String color, int price, boolean electric) {
        this.type = type;
        this.driver = driver;
        this.color = color;
        this.price = price;
        this.electric = electric;
    }

    public String getType() { return type; }
    public Driver getDriver() { return driver; }
    public String getColor() { return color; }
    public int getPrice() { return price; }
    public boolean getElectric() { return electric; }

    public void setType(String type) { this.type = type; }
    public void setDriver(Driver driver) { this.driver = driver; }
    public void setColor(String color) { this.color = color; }
    public void setPrice(int price) { this.price = price; }
    public void setElectric(boolean electric) { this.electric = electric; }

    @Override
    public String toString() {
        return type + " / " + driver.toString() + " / " + color + " / " + price + " / " + electric + "\n";
    }
}
