package strategy;

import exceptions.ParkingSpotNotFoundException;
import input.ReadInputFromFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parking.Driver;
import parking.ParkingLot;
import structures.Ticket;
import vehicles.Motorcycle;
import vehicles.Truck;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class VIPRegularTicketGeneratorUnitTest {
    ParkingLot parkingLot;
    ReadInputFromFile readInputFromFile;

    @BeforeEach
    public void initializeParkingLot() {
        readInputFromFile = new ReadInputFromFile();
        parkingLot = readInputFromFile.initializeAndGetParkingLot();
    }

    @Test
    public void getSpotFromTheNextParkingSpotCategories() throws ParkingSpotNotFoundException {
        // Given
        Driver driver = mock(Driver.class);
        when(driver.getVipStatus()).thenReturn(true);
        when(driver.getVehicle()).thenReturn(new Motorcycle("blue", 2000, false));

        TicketGenerator ticketGenerator = new VIPRegularTicketGenerator();


        Ticket ticket;

        // When
        ticket = ticketGenerator.getTicket(parkingLot, driver);
        assertEquals(1, ticket.getSpotId());

        ticket = ticketGenerator.getTicket(parkingLot, driver);
        assertEquals(2, ticket.getSpotId());

        ticket = ticketGenerator.getTicket(parkingLot, driver);
        assertEquals(3, ticket.getSpotId());

        ticket = ticketGenerator.getTicket(parkingLot, driver);
        assertEquals(7, ticket.getSpotId());
    }

    @Test
    public void throwExceptionWhenThereIsNoSpotAvailable() throws ParkingSpotNotFoundException {
        // Given
        Driver driver = mock(Driver.class);
        when(driver.getVipStatus()).thenReturn(true);
        when(driver.getVehicle()).thenReturn(new Truck("blue", 2000, false));

        TicketGenerator ticketGenerator = new VIPRegularTicketGenerator();

        Ticket ticket;
        ticket = ticketGenerator.getTicket(parkingLot, driver);
        assertThrowsExactly(ParkingSpotNotFoundException.class, () -> ticketGenerator.getTicket(parkingLot, driver));
    }
}
