package requests;

import exceptions.*;
import structures.Ticket;
import vehicles.VehicleJson;
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
