package structures;

import exceptions.ParkingSpotNotFoundException;
import parking.ParkingSpot;
import parking.ParkingSpotType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParkingLotStatus {

    private List<ParkingSpot> parkingSpots;
    private List<Ticket> tickets;

    public ParkingLotStatus(List<ParkingSpot> parkingSpots, List<Ticket> tickets) {
        this.parkingSpots = parkingSpots;
        this.tickets = tickets;
    }

    public List<ParkingSpot> getParkingSpots() { return parkingSpots; }
    public List<Ticket> getTickets() { return tickets; }

    public ParkingSpot getParkingSpotById(int idParkingSpot) throws ParkingSpotNotFoundException {
        for(ParkingSpot parkingSpot : parkingSpots) {
            if(parkingSpot.getId() == idParkingSpot)
                return parkingSpot;
        }

        throw new ParkingSpotNotFoundException();
    }

    public void removeTicket(Ticket ticket) {
        tickets.remove(ticket);
    }

    public void addTicket(Ticket ticket) {
        tickets.add(ticket);
    }
}
