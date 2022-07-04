package parking;

public class Driver {
    private String name;
    private boolean vipStatus;

    public Driver(String name, boolean vipStatus) {
        this.name = name;
        this.vipStatus = vipStatus;
    }

    public String getName() {
        return name;
    }
    public boolean getVipStatus() {
        return vipStatus;
    }

    @Override
    public String toString() {
        String vipStatus = getVipStatus() ? "VIP" : "NonVIP";

        return String.format("(%s, %s)", getName(), vipStatus);
    }

    @Override
    public boolean equals(Object object) {
        if(object instanceof Driver) {
            Driver driver = (Driver) object;
            return this.name.equals(driver.name) && this.vipStatus == driver.vipStatus;
        }
        return false;
    }

}
