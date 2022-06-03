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

public class VIPElectricTicketGeneratorUnitTest {
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
        when(driver.getVehicle()).thenReturn(new Motorcycle("blue", 2000, true));

        TicketGenerator ticketGenerator = new VIPElectricTicketGenerator();


        Ticket ticket;

        // When, Then
        ticket = ticketGenerator.getTicket(parkingLot, driver);
        assertEquals(0, ticket.getSpotId());

        ticket = ticketGenerator.getTicket(parkingLot, driver);
        assertEquals(4, ticket.getSpotId());
    }

    @Test
    public void throwExceptionWhenThereIsNoSpotAvailable() throws ParkingSpotNotFoundException {
        // Given
        Driver driver = mock(Driver.class);
        when(driver.getVipStatus()).thenReturn(true);
        when(driver.getVehicle()).thenReturn(new Truck("blue", 2000, true));

        TicketGenerator ticketGenerator = new VIPElectricTicketGenerator();

        Ticket ticket;
        ticket = ticketGenerator.getTicket(parkingLot, driver);
        ticket = ticketGenerator.getTicket(parkingLot, driver);
        ticket = ticketGenerator.getTicket(parkingLot, driver);
        assertThrowsExactly(ParkingSpotNotFoundException.class, () -> ticketGenerator.getTicket(parkingLot, driver));

    }
}
