package structures;

public class ParkingLotDimensions {

    private int motorcycleParkingSpotsDimension;
    private int carParkingSpotsDimension;
    private int truckParkingSpotsDimension;

    public ParkingLotDimensions(int motorcycleParkingSpotsDimension, int carParkingSpotsDimension, int truckParkingSpotsDimension) {
        this.motorcycleParkingSpotsDimension = motorcycleParkingSpotsDimension;
        this.carParkingSpotsDimension = carParkingSpotsDimension;
        this.truckParkingSpotsDimension = truckParkingSpotsDimension;
    }

    public int getMotorcycleParkingSpotsDimension() {
        return motorcycleParkingSpotsDimension;
    }

    public int getCarParkingSpotsDimension() {
        return carParkingSpotsDimension;
    }

    public int getTruckParkingSpotsDimension() {
        return truckParkingSpotsDimension;
    }

}
