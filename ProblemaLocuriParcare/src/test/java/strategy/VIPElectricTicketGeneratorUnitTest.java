package strategy;

import database.ParkingSpotsCollection;
import exceptions.ParkingSpotNotFoundException;
import exceptions.SimultaneousOperationInDatabaseCollectionException;
import org.junit.jupiter.api.Test;
import parking.Driver;
import parking.ParkingSpotType;
import structures.Ticket;
import vehicles.Motorcycle;
import vehicles.Vehicle;
import vehicles.VehicleType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class VIPElectricTicketGeneratorUnitTest {
    @Test
    public void getSpotFromTheNextParkingSpotCategories() throws ParkingSpotNotFoundException, SimultaneousOperationInDatabaseCollectionException {
        // Given
        ParkingSpotsCollection parkingSpotsCollection = mock(ParkingSpotsCollection.class);
        Vehicle vehicle = mock(Vehicle.class);
        Driver driver = mock(Driver.class);
        when(vehicle.getDriver()).thenReturn(driver);
        when(vehicle.getElectric()).thenReturn(true);
        when(vehicle.getDriver().getVipStatus()).thenReturn(true);
        when(vehicle.getVehicleType()).thenReturn(VehicleType.MOTORCYCLE);

        TicketGenerator ticketGenerator = new VIPElectricTicketGenerator();
        Ticket ticket;

        // When, Then
        when(parkingSpotsCollection.getIdForAvailableParkingSpot(TicketGeneratorUtil.getSmallestFittingParkingSpotTypeFromVehicleType(vehicle.getVehicleType()), true)).thenReturn(7);
        ticket = ticketGenerator.getTicket(parkingSpotsCollection, vehicle);
        assertEquals(7, ticket.getSpotId());

        when(parkingSpotsCollection.getIdForAvailableParkingSpot(TicketGeneratorUtil.getSmallestFittingParkingSpotTypeFromVehicleType(vehicle.getVehicleType()), true)).thenReturn(9);
        ticket = ticketGenerator.getTicket(parkingSpotsCollection, vehicle);
        assertEquals(9, ticket.getSpotId());
    }

    @Test
    public void throwExceptionWhenThereIsNoSpotAvailable() throws ParkingSpotNotFoundException {
        // Given
        ParkingSpotsCollection parkingSpotsCollection = mock(ParkingSpotsCollection.class);
        Vehicle vehicle = new Motorcycle(new Driver("Andrei", true), "red", 2000, true);

        TicketGenerator ticketGenerator = new VIPElectricTicketGenerator();

        //TODO se poate si cu mai multe when-uri
        when(parkingSpotsCollection.getIdForAvailableParkingSpot(any(ParkingSpotType.class), eq(vehicle.getElectric()))).thenThrow(new ParkingSpotNotFoundException());
        assertThrowsExactly(ParkingSpotNotFoundException.class, () -> ticketGenerator.getTicket(parkingSpotsCollection, vehicle));
    }
}
