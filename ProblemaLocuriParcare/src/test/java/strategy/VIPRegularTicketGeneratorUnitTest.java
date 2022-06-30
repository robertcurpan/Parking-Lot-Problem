package strategy;

import database.DriversCollection;
import database.ParkingSpotsCollection;
import exceptions.ParkingSpotNotFoundException;
import exceptions.SimultaneousOperationInDatabaseCollectionException;
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
    public void getSpotFromTheNextParkingSpotCategories() throws ParkingSpotNotFoundException, SimultaneousOperationInDatabaseCollectionException {
        // Given
        ParkingSpotsCollection parkingSpotsCollection = mock(ParkingSpotsCollection.class);
        DriversCollection driversCollection = mock(DriversCollection.class);
        Driver driver = mock(Driver.class);
        when(driver.getVipStatus()).thenReturn(true);
        when(driver.getVehicle()).thenReturn(new Motorcycle("blue", 2000, false));

        TicketGenerator ticketGenerator = new VIPRegularTicketGenerator();
        Ticket ticket;

        // When
        when(parkingSpotsCollection.getParkingSpotId(driver.getVehicle().getType(), false)).thenReturn(2);
        ticket = ticketGenerator.getTicket(parkingSpotsCollection, driver);
        assertEquals(2, ticket.getSpotId());

        when(parkingSpotsCollection.getParkingSpotId(driver.getVehicle().getType(), false)).thenReturn(1);
        ticket = ticketGenerator.getTicket(parkingSpotsCollection, driver);
        assertEquals(1, ticket.getSpotId());

        when(parkingSpotsCollection.getParkingSpotId(driver.getVehicle().getType(), false)).thenReturn(3);
        ticket = ticketGenerator.getTicket(parkingSpotsCollection, driver);
        assertEquals(3, ticket.getSpotId());

        when(parkingSpotsCollection.getParkingSpotId(driver.getVehicle().getType(), false)).thenReturn(8);
        ticket = ticketGenerator.getTicket(parkingSpotsCollection, driver);
        assertEquals(8, ticket.getSpotId());
    }

    @Test
    public void throwExceptionWhenThereIsNoSpotAvailable() throws ParkingSpotNotFoundException {
        // Given
        ParkingSpotsCollection parkingSpotsCollection = mock(ParkingSpotsCollection.class);
        DriversCollection driversCollection = mock(DriversCollection.class);
        Driver driver = mock(Driver.class);
        when(driver.getVipStatus()).thenReturn(true);
        when(driver.getVehicle()).thenReturn(new Truck("blue", 2000, false));

        TicketGenerator ticketGenerator = new VIPRegularTicketGenerator();

        when(parkingSpotsCollection.getParkingSpotId(driver.getVehicle().getType(), false)).thenThrow(new ParkingSpotNotFoundException());
        assertThrowsExactly(ParkingSpotNotFoundException.class, () -> ticketGenerator.getTicket(parkingSpotsCollection, driver));
    }
}
