package exceptions;

public class VehicleTooExpensiveException extends ParkingLotGeneralException {

    public VehicleTooExpensiveException()
    {
        message = "The vehicle's price must be at most 10.000!";
    }

    public VehicleTooExpensiveException(String message)
    {
        this.message = message;
    }

    @Override
    public String toString()
    {
        return message;
    }
}
