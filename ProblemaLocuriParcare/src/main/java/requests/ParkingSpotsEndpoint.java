package requests;

import exceptions.*;
import parking.ParkingSpot;
import structures.Ticket;
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
