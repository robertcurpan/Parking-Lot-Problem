package strategy;

import exceptions.ParkingSpotNotFoundException;
import org.junit.jupiter.api.Test;
import parking.Driver;
import structures.Ticket;
import vehicles.Motorcycle;
import vehicles.Truck;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class VIPElectricTicketGeneratorUnitTest {
    @Test
    public void getSpotFromTheNextParkingSpotCategories() throws ParkingSpotNotFoundException {
        // Given
        Driver driver = mock(Driver.class);
        when(driver.getVipStatus()).thenReturn(true);
        when(driver.getVehicle()).thenReturn(new Motorcycle("blue", 2000, true));

        TicketGenerator ticketGenerator = new VIPElectricTicketGenerator();
        Ticket ticket;

        // When, Then
        ticket = ticketGenerator.getTicket(driver);
        assertEquals(7, ticket.getSpotId());

        ticket = ticketGenerator.getTicket(driver);
        assertEquals(9, ticket.getSpotId());
    }

    @Test
    public void throwExceptionWhenThereIsNoSpotAvailable() throws ParkingSpotNotFoundException {
        // Given
        Driver driver = mock(Driver.class);
        when(driver.getVipStatus()).thenReturn(true);
        when(driver.getVehicle()).thenReturn(new Truck("blue", 2000, true));

        TicketGenerator ticketGenerator = new VIPElectricTicketGenerator();

        Ticket ticket;
        ticket = ticketGenerator.getTicket(driver);
        ticket = ticketGenerator.getTicket(driver);
        ticket = ticketGenerator.getTicket(driver);
        assertThrowsExactly(ParkingSpotNotFoundException.class, () -> ticketGenerator.getTicket(driver));

    }
}
