package exceptions;

public class ParkingSpotNotFoundException extends Exception
{
    private String message;
    public ParkingSpotNotFoundException()
    {
        message = "Could not find a parking spot!";
    }

    public ParkingSpotNotFoundException(String message)
    {
        this.message = message;
    }

    @Override
    public String toString()
    {
        return message;
    }
}
