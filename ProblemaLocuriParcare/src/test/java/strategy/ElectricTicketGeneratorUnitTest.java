package strategy;

import exceptions.ParkingSpotNotFoundException;
import org.junit.jupiter.api.Test;
import parking.*;
import structures.Ticket;
import vehicles.Car;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ElectricTicketGeneratorUnitTest {
    @Test
    public void throwExceptionWhenNoSpotIsAvailable() throws ParkingSpotNotFoundException {
        // Given
        Driver driver = mock(Driver.class);
        when(driver.getVipStatus()).thenReturn(false);  // specificam ca driver-ul nu e VIP
        when(driver.getVehicle()).thenReturn(new Car("red", 2000, true)); // specificam ca driver-ul are o masina electrica

        // Creat obiectul manual
        TicketGenerator ticketGenerator = new ElectricTicketGenerator();
        Ticket ticket;

        // When
        ticket = ticketGenerator.getTicket(driver);
        assertEquals(9, ticket.getSpotId());
        assertThrowsExactly(ParkingSpotNotFoundException.class, () -> ticketGenerator.getTicket(driver)); // nu mai exista locuri libere pt un sofer non-vip cu masina electrica
    }

}
