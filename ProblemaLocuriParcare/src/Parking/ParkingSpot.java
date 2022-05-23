package Parking;

public class ParkingSpot
{
    private int id;
    private boolean free;
    private boolean electric;

    public ParkingSpot(int id, boolean free, boolean electric)
    {
        this.id = id;
        this.free = free;
        this.electric = electric;
    }

    public int getId() { return this.id; }
    public boolean isFree() { return this.free; }
    public boolean hasElectricCharger() { return this.electric; }

    public void setId(int id) { this.id = id; }
    public void setFree(boolean free) { this.free = free; }
    public void setElectric(boolean electric) { this.electric = electric; }

    @Override
    public String toString()
    {
        return "Spot with id: " + id + " -> free: " + free + ", electric: " + electric;
    }

}
