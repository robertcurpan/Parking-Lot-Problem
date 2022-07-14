package requests;

import com.fasterxml.jackson.core.JsonProcessingException;
import exceptions.*;
import structures.ExceptionJson;
import structures.Ticket;
import utils.DeserializerUtil;
import utils.HttpRequestCreatorUtil;
import vehicles.Vehicle;
import vehicles.VehicleJson;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class TicketsEndpoint {

    private HttpClientWrapper httpClientWrapper;

    public TicketsEndpoint(HttpClientWrapper httpClientWrapper) { this.httpClientWrapper = httpClientWrapper; }

    public Ticket generateParkingTicket(VehicleJson vehicleJson) throws ParkingLotGeneralException {
        return httpClientWrapper.generateParkingTicketFromUri("http://localhost:8080/generateParkingTicket", vehicleJson);
    }

    public List<Ticket> getTickets() throws HttpRequestException {
        return httpClientWrapper.getTicketsFromUri("http://localhost:8080/getTickets");
    }
}
