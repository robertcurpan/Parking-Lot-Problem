package parking;

import exceptions.*;
import requests.HttpClientWrapper;
import requests.ParkingSpotsEndpoint;
import requests.TicketsEndpoint;
import structures.ParkingLotStatus;
import structures.Ticket;
import vehicles.VehicleJson;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParkingLotService {
    private HttpClientWrapper httpClientWrapper;
    private ParkingSpotsEndpoint parkingSpotsEndpoint;
    private TicketsEndpoint ticketsEndpoint;

    public ParkingLotService() {
        httpClientWrapper = new HttpClientWrapper();
        parkingSpotsEndpoint = new ParkingSpotsEndpoint(httpClientWrapper);
        ticketsEndpoint = new TicketsEndpoint(httpClientWrapper);
    }

    public Ticket generateParkingTicket(VehicleJson vehicleJson) throws ParkingLotGeneralException {
        doValidations(vehicleJson);
        return ticketsEndpoint.generateParkingTicket(vehicleJson);
    }

    public Ticket leaveParkingLot(ParkingSpot parkingSpot) throws ParkingLotGeneralException {
        return parkingSpotsEndpoint.updateParkingSpotWhenVehicleLeaves(parkingSpot);
    }

    public ParkingLotStatus getParkingLotStatus() throws HttpRequestException {
        return new ParkingLotStatus(parkingSpotsEndpoint.getParkingSpots(), ticketsEndpoint.getTickets());
    }

    public List<Ticket> getTickets() throws HttpRequestException {
        return ticketsEndpoint.getTickets();
    }

    public List<ParkingSpot> getParkingSpots() throws HttpRequestException {
        return parkingSpotsEndpoint.getParkingSpots();
    }

    public Map<ParkingSpotType, Integer> getNoOfEmptyParkingSpots(ParkingLotStatus parkingLotStatus) {
        Map<ParkingSpotType, Integer> noOfEmptySpotsForAllSpotTypes = new HashMap<>();
        for(ParkingSpotType parkingSpotType : ParkingSpotType.values()) {
            noOfEmptySpotsForAllSpotTypes.put(parkingSpotType, 0);
        }

        for(ParkingSpot parkingSpot : parkingLotStatus.getParkingSpots()) {
            if(parkingSpot.getVehicleId() == null) {
                Integer currentNoOfEmptySpotsForParkingSpotType = noOfEmptySpotsForAllSpotTypes.get(parkingSpot.getSpotType());
                noOfEmptySpotsForAllSpotTypes.put(parkingSpot.getSpotType(), currentNoOfEmptySpotsForParkingSpotType + 1);
            }
        }

        return noOfEmptySpotsForAllSpotTypes;
    }

    public ParkingLotStatus updateParkingLotStatusAfterDriverLeaves(ParkingLotStatus parkingLotStatus, Ticket ticket) {
        // Trebuie sa eliberam locul de parcare (scoatem vehicleId si updatam versiunea) si sa scoatem ticket-ul din lista de ticket-uri
        for(ParkingSpot parkingSpot : parkingLotStatus.getParkingSpots()) {
            if(parkingSpot.getId() == ticket.getSpotId()) {
                parkingSpot.setVehicleId(null);
                parkingSpot.setVersion(parkingSpot.getVersion() + 1);
                break;
            }
        }

        for(Ticket ticketIter : parkingLotStatus.getTickets()) {
            if(ticketIter.getSpotId() == ticket.getSpotId()) {
                parkingLotStatus.removeTicket(ticketIter);
                break;
            }
        }

        return parkingLotStatus;
    }

    public ParkingLotStatus updateParkingLotStatusAfterDriverParks(ParkingLotStatus parkingLotStatus, Ticket ticket) {
        // Trebuie sa adaugam un ticket in colectia de tickets si sa updatam locul de parcare (setam vehicle id, updatam versiunea)
        for(ParkingSpot parkingSpot : parkingLotStatus.getParkingSpots()) {
            if(parkingSpot.getId() == ticket.getSpotId()) {
                parkingSpot.setVehicleId(ticket.getVehicle().getVehicleId());
                parkingSpot.setVersion(parkingSpot.getVersion() + 1);
                break;
            }
        }

        parkingLotStatus.addTicket(ticket);

        return parkingLotStatus;
    }

    public void doValidations(VehicleJson vehicleJson) throws VehicleTooExpensiveException {
        if(vehicleJson.getPrice() > 10000) {
            throw new VehicleTooExpensiveException();
        }
    }



}
