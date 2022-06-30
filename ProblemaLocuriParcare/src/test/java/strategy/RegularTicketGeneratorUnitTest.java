package strategy;

import database.DriversCollection;
import database.ParkingSpotsCollection;
import exceptions.ParkingSpotNotFoundException;
import exceptions.SimultaneousOperationInDatabaseCollectionException;
import org.junit.jupiter.api.Test;
import parking.Driver;
import structures.Ticket;
import vehicles.Car;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RegularTicketGeneratorUnitTest {
    @Test
    public void throwExceptionWhenNoSpotIsAvailable() throws ParkingSpotNotFoundException, SimultaneousOperationInDatabaseCollectionException {
        // Given
        ParkingSpotsCollection parkingSpotsCollection = mock(ParkingSpotsCollection.class);
        DriversCollection driversCollection = mock(DriversCollection.class);
        Driver driver = mock(Driver.class);
        when(driver.getVipStatus()).thenReturn(false);
        when(driver.getVehicle()).thenReturn(new Car("blue", 2000, false));

        TicketGenerator ticketGenerator = new RegularTicketGenerator();
        Ticket ticket;

        // When
        when(parkingSpotsCollection.getParkingSpotId(driver.getVehicle().getType(), false)).thenReturn(1);
        ticket = ticketGenerator.getTicket(parkingSpotsCollection, driver);
        assertEquals(1, ticket.getSpotId());

        when(parkingSpotsCollection.getParkingSpotId(driver.getVehicle().getType(), false)).thenReturn(3);
        ticket = ticketGenerator.getTicket(parkingSpotsCollection, driver);
        assertEquals(3, ticket.getSpotId());

        when(parkingSpotsCollection.getParkingSpotId(driver.getVehicle().getType(), false)).thenThrow(new ParkingSpotNotFoundException());
        assertThrowsExactly(ParkingSpotNotFoundException.class, () -> ticketGenerator.getTicket(parkingSpotsCollection, driver));
    }
}
