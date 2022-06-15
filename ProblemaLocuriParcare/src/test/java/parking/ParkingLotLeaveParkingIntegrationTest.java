package parking;

import exceptions.ParkingSpotNotFoundException;
import exceptions.ParkingSpotNotOccupiedException;
import org.junit.jupiter.api.Test;
import structures.Ticket;
import vehicles.Car;
import vehicles.Vehicle;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParkingLotLeaveParkingIntegrationTest {

    @Test
    public void leaveParkingLot() throws ParkingSpotNotFoundException, ParkingSpotNotOccupiedException {
        // 1) Given
        ParkingLotService parkingLotService = new ParkingLotService(new TicketGeneratorCreator());
        Vehicle vehicle = new Car("blue", 2400, false);
        Driver driver = new Driver("Robert", vehicle, false);

        // 2) When
        Ticket ticket = parkingLotService.getParkingTicket(driver);
        Driver driverLeft = parkingLotService.leaveParkingLot(ticket.getSpotId());

        // 3) Then
        assertEquals(driver, driverLeft);
    }

}
