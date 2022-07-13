package exceptions;

public class HttpRequestException extends ParkingLotGeneralException {

    public HttpRequestException()
    {
        message = "Error when sending a http request or when receiving the response!";
    }

    public HttpRequestException(String message)
    {
        this.message = message;
    }

    @Override
    public String toString()
    {
        return message;
    }
}
