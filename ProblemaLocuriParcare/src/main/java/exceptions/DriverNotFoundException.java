package exceptions;

public class DriverNotFoundException extends Exception {
    private String message;
    public DriverNotFoundException()
    {
        message = "Driver not found!";
    }

    public DriverNotFoundException(String message)
    {
        this.message = message;
    }

    @Override
    public String toString()
    {
        return message;
    }
}
