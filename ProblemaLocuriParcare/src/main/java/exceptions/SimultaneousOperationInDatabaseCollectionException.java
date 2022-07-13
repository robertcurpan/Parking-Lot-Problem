package exceptions;

public class SimultaneousOperationInDatabaseCollectionException extends ParkingLotGeneralException {
    public SimultaneousOperationInDatabaseCollectionException()
    {
        message = "Another request was sent at the same time!";
    }

    public SimultaneousOperationInDatabaseCollectionException(String message)
    {
        this.message = message;
    }

    @Override
    public String toString()
    {
        return message;
    }
}

