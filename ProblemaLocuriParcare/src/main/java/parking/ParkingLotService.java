package parking;

import database.ParkingSpotsCollection;
import exceptions.ParkingSpotNotFoundException;
import exceptions.ParkingSpotNotOccupiedException;
import strategy.TicketGenerator;
import structures.Ticket;

public class ParkingLotService {

    private TicketGeneratorCreator ticketGeneratorCreator;

    public ParkingLotService(TicketGeneratorCreator ticketGeneratorCreator) {
        this.ticketGeneratorCreator = ticketGeneratorCreator;
        // argument ParkingSpotsCollection
    }

    public Ticket getParkingTicket(Driver driver) throws ParkingSpotNotFoundException {
        TicketGenerator ticketGenerator = ticketGeneratorCreator.getTicketGenerator(driver);
        Ticket ticket = ticketGenerator.getTicket(driver);
        return ticket;
    }

    public Driver leaveParkingLot(int idParkingSpot) throws ParkingSpotNotOccupiedException {
        try {
            Driver driver = ParkingSpotsCollection.getDriverAssignedToParkingSpot(idParkingSpot);
            ParkingSpotsCollection.makeParkingSpotFree(idParkingSpot);
            return driver;
        } catch (RuntimeException ex) {
            throw new ParkingSpotNotOccupiedException();
        }

    }

}
