package strategy;

import exceptions.ParkingSpotNotFoundException;
import input.ReadInputFromFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parking.Driver;
import parking.ParkingLot;
import structures.Ticket;
import vehicles.Car;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RegularTicketGeneratorUnitTest {
    ParkingLot parkingLot;
    ReadInputFromFile readInputFromFile;

    @BeforeEach
    public void initializeParkingLot() {
        readInputFromFile = new ReadInputFromFile();
        parkingLot = readInputFromFile.initializeAndGetParkingLot();
    }

    @Test
    public void throwExceptionWhenNoSpotIsAvailable() throws ParkingSpotNotFoundException {
        // Given
        Driver driver = mock(Driver.class);
        when(driver.getVipStatus()).thenReturn(false);
        when(driver.getVehicle()).thenReturn(new Car("blue", 2000, false));

        TicketGenerator ticketGenerator = new RegularTicketGenerator();


        Ticket ticket;

        // When
        ticket = ticketGenerator.getTicket(parkingLot, driver);
        assertEquals(1, ticket.getSpotId());

        ticket = ticketGenerator.getTicket(parkingLot, driver);
        assertEquals(3, ticket.getSpotId());

        assertThrowsExactly(ParkingSpotNotFoundException.class, () -> ticketGenerator.getTicket(parkingLot, driver));
    }
}
