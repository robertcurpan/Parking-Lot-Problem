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

public class VIPElectricTicketGeneratorUnitTest {
    @Test
    public void getSpotFromTheNextParkingSpotCategories() throws ParkingSpotNotFoundException, SimultaneousOperationInDatabaseCollectionException {
        // Given
        ParkingSpotsCollection parkingSpotsCollection = mock(ParkingSpotsCollection.class);
        DriversCollection driversCollection = mock(DriversCollection.class);
        Driver driver = mock(Driver.class);
        when(driver.getVipStatus()).thenReturn(true);
        when(driver.getVehicle()).thenReturn(new Motorcycle("blue", 2000, true));

        TicketGenerator ticketGenerator = new VIPElectricTicketGenerator();
        Ticket ticket;

        // When, Then
        when(parkingSpotsCollection.getParkingSpotId(driver.getVehicle().getType(), true)).thenReturn(7);
        ticket = ticketGenerator.getTicket(parkingSpotsCollection, driver);
        assertEquals(7, ticket.getSpotId());

        when(parkingSpotsCollection.getParkingSpotId(driver.getVehicle().getType(), true)).thenReturn(9);
        ticket = ticketGenerator.getTicket(parkingSpotsCollection, driver);
        assertEquals(9, ticket.getSpotId());
    }

    @Test
    public void throwExceptionWhenThereIsNoSpotAvailable() throws ParkingSpotNotFoundException {
        // Given
        ParkingSpotsCollection parkingSpotsCollection = mock(ParkingSpotsCollection.class);
        DriversCollection driversCollection = mock(DriversCollection.class);
        Driver driver = mock(Driver.class);
        when(driver.getVipStatus()).thenReturn(true);
        when(driver.getVehicle()).thenReturn(new Truck("blue", 2000, true));

        TicketGenerator ticketGenerator = new VIPElectricTicketGenerator();

        Ticket ticket;
        when(parkingSpotsCollection.getParkingSpotId(driver.getVehicle().getType(), true)).thenThrow(new ParkingSpotNotFoundException());
        assertThrowsExactly(ParkingSpotNotFoundException.class, () -> ticketGenerator.getTicket(parkingSpotsCollection, driver));
    }
}
