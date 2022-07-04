package strategy;

import database.VehiclesCollection;
import database.ParkingSpotsCollection;
import exceptions.ParkingSpotNotFoundException;
import exceptions.SimultaneousOperationInDatabaseCollectionException;
import org.junit.jupiter.api.Test;
import parking.Driver;
import parking.ParkingSpotType;
import structures.Ticket;
import vehicles.Motorcycle;
import vehicles.Truck;
import vehicles.Vehicle;
import vehicles.VehicleType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class VIPRegularTicketGeneratorUnitTest {
    @Test
    public void getSpotFromTheNextParkingSpotCategories() throws ParkingSpotNotFoundException, SimultaneousOperationInDatabaseCollectionException {
        // Given
        ParkingSpotsCollection parkingSpotsCollection = mock(ParkingSpotsCollection.class);
        Vehicle vehicle = mock(Vehicle.class);
        Driver driver = mock(Driver.class);
        when(vehicle.getDriver()).thenReturn(driver);
        when(vehicle.getElectric()).thenReturn(false);
        when(vehicle.getDriver().getVipStatus()).thenReturn(true);
        when(vehicle.getVehicleType()).thenReturn(VehicleType.MOTORCYCLE);

        TicketGenerator ticketGenerator = new VIPRegularTicketGenerator();
        Ticket ticket;

        // When
        when(parkingSpotsCollection.getIdForAvailableParkingSpot(ParkingSpotType.getSmallestFittingParkingSpotTypeFromVehicleType(vehicle.getVehicleType()), false)).thenReturn(2);
        ticket = ticketGenerator.getTicket(parkingSpotsCollection, vehicle);
        assertEquals(2, ticket.getSpotId());

        when(parkingSpotsCollection.getIdForAvailableParkingSpot(ParkingSpotType.getSmallestFittingParkingSpotTypeFromVehicleType(vehicle.getVehicleType()), false)).thenReturn(1);
        ticket = ticketGenerator.getTicket(parkingSpotsCollection, vehicle);
        assertEquals(1, ticket.getSpotId());

        when(parkingSpotsCollection.getIdForAvailableParkingSpot(ParkingSpotType.getSmallestFittingParkingSpotTypeFromVehicleType(vehicle.getVehicleType()), false)).thenReturn(3);
        ticket = ticketGenerator.getTicket(parkingSpotsCollection, vehicle);
        assertEquals(3, ticket.getSpotId());

        when(parkingSpotsCollection.getIdForAvailableParkingSpot(ParkingSpotType.getSmallestFittingParkingSpotTypeFromVehicleType(vehicle.getVehicleType()), false)).thenReturn(8);
        ticket = ticketGenerator.getTicket(parkingSpotsCollection, vehicle);
        assertEquals(8, ticket.getSpotId());
    }

    @Test
    public void throwExceptionWhenThereIsNoSpotAvailable() throws ParkingSpotNotFoundException {
        // Given
        ParkingSpotsCollection parkingSpotsCollection = mock(ParkingSpotsCollection.class);
        Vehicle vehicle = mock(Vehicle.class);
        Driver driver = mock(Driver.class);
        when(vehicle.getDriver()).thenReturn(driver);
        when(vehicle.getElectric()).thenReturn(false);
        when(vehicle.getDriver().getVipStatus()).thenReturn(true);
        when(vehicle.getVehicleType()).thenReturn(VehicleType.TRUCK);

        TicketGenerator ticketGenerator = new VIPRegularTicketGenerator();

        when(parkingSpotsCollection.getIdForAvailableParkingSpot(ParkingSpotType.getSmallestFittingParkingSpotTypeFromVehicleType(vehicle.getVehicleType()), false)).thenThrow(new ParkingSpotNotFoundException());
        assertThrowsExactly(ParkingSpotNotFoundException.class, () -> ticketGenerator.getTicket(parkingSpotsCollection, vehicle));
    }
}
