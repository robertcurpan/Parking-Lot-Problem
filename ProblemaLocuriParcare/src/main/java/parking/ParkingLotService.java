package parking;

import database.ParkingSpotsCollection;
import exceptions.ParkingSpotNotFoundException;
import exceptions.ParkingSpotNotOccupiedException;
import exceptions.SimultaneousOperationException;
import strategy.TicketGenerator;
import structures.Ticket;

public class ParkingLotService {

    private TicketGeneratorCreator ticketGeneratorCreator;
    private ParkingSpotsCollection parkingSpotsCollection;

    public ParkingLotService(TicketGeneratorCreator ticketGeneratorCreator, ParkingSpotsCollection parkingSpotsCollection) {
        this.ticketGeneratorCreator = ticketGeneratorCreator;
        this.parkingSpotsCollection = parkingSpotsCollection;
    }

    public Ticket getParkingTicket(Driver driver) throws ParkingSpotNotFoundException, SimultaneousOperationException {
        TicketGenerator ticketGenerator = ticketGeneratorCreator.getTicketGenerator(driver);
        Ticket ticket = ticketGenerator.getTicket(parkingSpotsCollection, driver);
        return ticket;
    }

    public Driver leaveParkingLot(int idParkingSpot) throws ParkingSpotNotOccupiedException, SimultaneousOperationException {
        try {
            Driver driver = parkingSpotsCollection.getDriverAssignedToParkingSpot(idParkingSpot);
            parkingSpotsCollection.makeParkingSpotFree(idParkingSpot);
            return driver;
        } catch (RuntimeException ex) {
            throw new ParkingSpotNotOccupiedException();
        }

    }

}
