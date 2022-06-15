package exceptions;

public class SimultaneousOperationException extends Exception {
    private String message;
    public SimultaneousOperationException()
    {
        message = "Another request was sent at the same time!";
    }

    public SimultaneousOperationException(String message)
    {
        this.message = message;
    }

    @Override
    public String toString()
    {
        return message;
    }
}

