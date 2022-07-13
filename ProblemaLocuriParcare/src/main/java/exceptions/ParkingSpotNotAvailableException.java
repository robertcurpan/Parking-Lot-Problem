package exceptions;

public class ParkingSpotNotAvailableException extends ParkingLotGeneralException {

    public ParkingSpotNotAvailableException()
    {
        message = "Parking spot for current vehicle is  not available!";
    }

    public ParkingSpotNotAvailableException(String message)
    {
        this.message = message;
    }

    @Override
    public String toString()
    {
        return message;
    }
}