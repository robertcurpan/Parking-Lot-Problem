package exceptions;

public class ParkingSpotNotOccupiedException extends Exception
{
    private String message;
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
