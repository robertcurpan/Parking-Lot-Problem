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

public class VIPRegularTicketGeneratorUnitTest {
    @Test
    public void getSpotFromTheNextParkingSpotCategories() throws ParkingSpotNotFoundException {
        // Given
        Driver driver = mock(Driver.class);
        when(driver.getVipStatus()).thenReturn(true);
        when(driver.getVehicle()).thenReturn(new Motorcycle("blue", 2000, false));

        TicketGenerator ticketGenerator = new VIPRegularTicketGenerator();


        Ticket ticket;

        // When
        ticket = ticketGenerator.getTicket(driver);
        assertEquals(2, ticket.getSpotId());

        ticket = ticketGenerator.getTicket(driver);
        assertEquals(1, ticket.getSpotId());

        ticket = ticketGenerator.getTicket(driver);
        assertEquals(3, ticket.getSpotId());

        ticket = ticketGenerator.getTicket(driver);
        assertEquals(8, ticket.getSpotId());
    }

    @Test
    public void throwExceptionWhenThereIsNoSpotAvailable() throws ParkingSpotNotFoundException {
        // Given
        Driver driver = mock(Driver.class);
        when(driver.getVipStatus()).thenReturn(true);
        when(driver.getVehicle()).thenReturn(new Truck("blue", 2000, false));

        TicketGenerator ticketGenerator = new VIPRegularTicketGenerator();

        Ticket ticket = ticketGenerator.getTicket(driver);
        assertThrowsExactly(ParkingSpotNotFoundException.class, () -> ticketGenerator.getTicket(driver));
    }
}
