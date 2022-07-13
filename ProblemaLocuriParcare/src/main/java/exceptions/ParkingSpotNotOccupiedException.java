package exceptions;

public class ParkingSpotNotOccupiedException extends ParkingLotGeneralException
{
    public ParkingSpotNotOccupiedException()
    {
        message = "The parking spot is not occupied!";
    }

    public ParkingSpotNotOccupiedException(String message)
    {
        this.message = message;
    }

    @Override
    public String toString()
    {
        return message;
    }
}
