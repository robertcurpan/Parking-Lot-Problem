package exceptions;

public class IncorrectFileInputsException extends Exception {
    private String message;
    public IncorrectFileInputsException()
    {
        message = "Incorrect inputs in file";
    }

    public IncorrectFileInputsException(String message)
    {
        this.message = message;
    }

    @Override
    public String toString()
    {
        return message;
    }
}
