package strategy;

import database.VehiclesCollection;
import database.ParkingSpotsCollection;
import exceptions.ParkingSpotNotFoundException;
import exceptions.SimultaneousOperationInDatabaseCollectionException;
import org.junit.jupiter.api.Test;
import parking.*;
import structures.Ticket;
import vehicles.Motorcycle;
import vehicles.Vehicle;
import vehicles.VehicleType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ElectricTicketGeneratorUnitTest {
    @Test
    public void throwExceptionWhenNoSpotIsAvailable() throws ParkingSpotNotFoundException, SimultaneousOperationInDatabaseCollectionException {
        // MOCKUIM ParkingSpotsColleciton deoarece aici testam ELECTRIC TICKET GENERATOR, nu ParkingSpotsCollection

        // Given
        ParkingSpotsCollection parkingSpotsCollection = mock(ParkingSpotsCollection.class);
        Vehicle vehicle = new Motorcycle(new Driver("Andrei", false), "red", 2000, true);

        // Creat obiectul manual
        TicketGenerator ticketGenerator = new ElectricTicketGenerator();
        Ticket ticket;

        // When
        when(parkingSpotsCollection.getIdForAvailableParkingSpot(TicketGeneratorUtil.getSmallestFittingParkingSpotTypeFromVehicleType(vehicle.getVehicleType()), vehicle.getElectric())).thenReturn(9);
        ticket = ticketGenerator.getTicket(parkingSpotsCollection, vehicle);
        assertEquals(9, ticket.getSpotId());

        // getParkingSpotId() arunca RuntimeException (aceasta e prinsa in ElectricTicketGenerator si in catch se arunca ParkingSpotNotFoundException)
        when(parkingSpotsCollection.getIdForAvailableParkingSpot(TicketGeneratorUtil.getSmallestFittingParkingSpotTypeFromVehicleType(vehicle.getVehicleType()), vehicle.getElectric())).thenThrow(new ParkingSpotNotFoundException());
        assertThrowsExactly(ParkingSpotNotFoundException.class, () -> ticketGenerator.getTicket(parkingSpotsCollection, vehicle)); // nu mai exista locuri libere pt un sofer non-vip cu masina electrica
    }

}
