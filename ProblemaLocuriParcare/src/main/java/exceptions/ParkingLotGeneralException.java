package exceptions;

public class ParkingLotGeneralException extends Exception {
    protected String message;

    public ParkingLotGeneralException()
    {
        message = "ParkingLot General Exception";
    }

    public ParkingLotGeneralException(String message)
    {
        this.message = message;
    }

    @Override
    public String toString()
    {
        return message;
    }
}
