package exceptions;

public class SimultaneousOperationInDatabaseCollectionException extends Exception {
    private String message;
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

