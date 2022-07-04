package exceptions;

public class VehicleNotFoundException extends Exception {
    private String message;
    public VehicleNotFoundException()
    {
        message = "Vehicle not found!";
    }

    public VehicleNotFoundException(String message)
    {
        this.message = message;
    }

    @Override
    public String toString()
    {
        return message;
    }
}
