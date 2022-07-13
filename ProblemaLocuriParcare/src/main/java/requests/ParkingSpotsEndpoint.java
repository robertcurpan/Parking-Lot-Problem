package requests;

import com.fasterxml.jackson.core.JsonProcessingException;
import exceptions.*;
import parking.ParkingSpot;
import structures.ExceptionJson;
import structures.Ticket;
import utils.DeserializerUtil;
import utils.HttpRequestCreatorUtil;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class ParkingSpotsEndpoint {

    private HttpClientWrapper httpClientWrapper;

    public ParkingSpotsEndpoint(HttpClientWrapper httpClientWrapper) { this.httpClientWrapper = httpClientWrapper; }

    public Ticket updateParkingSpotWhenVehicleLeaves(ParkingSpot parkingSpot) throws ParkingLotGeneralException {
        return httpClientWrapper.updateParkingSpotWhenVehicleLeavesFromUri("http://localhost:8080/leaveParkingLot", parkingSpot);
    }

    public List<ParkingSpot> getParkingSpots() throws HttpRequestException {
        return httpClientWrapper.getParkingSpotsFromUri("http://localhost:8080/getParkingSpots");
    }

}
