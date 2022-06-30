package strategy;

import database.DriversCollection;
import database.ParkingSpotsCollection;
import exceptions.ParkingSpotNotFoundException;
import exceptions.SimultaneousOperationInDatabaseCollectionException;
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
    public void throwExceptionWhenNoSpotIsAvailable() throws ParkingSpotNotFoundException, SimultaneousOperationInDatabaseCollectionException {
        // MOCKUIM ParkingSpotsColleciton deoarece aici testam ELECTRIC TICKET GENERATOR, nu ParkingSpotsCollection

        // Given
        ParkingSpotsCollection parkingSpotsCollection = mock(ParkingSpotsCollection.class);
        DriversCollection driversCollection = mock(DriversCollection.class);
        Driver driver = mock(Driver.class);
        when(driver.getVipStatus()).thenReturn(false);  // specificam ca driver-ul nu e VIP
        when(driver.getVehicle()).thenReturn(new Car("red", 2000, true)); // specificam ca driver-ul are o masina electrica

        // Creat obiectul manual
        TicketGenerator ticketGenerator = new ElectricTicketGenerator();
        Ticket ticket;

        // When
        when(parkingSpotsCollection.getParkingSpotId(driver.getVehicle().getType(), true)).thenReturn(9);
        ticket = ticketGenerator.getTicket(parkingSpotsCollection, driver);
        assertEquals(9, ticket.getSpotId());

        // getParkingSpotId() arunca RuntimeException (aceasta e prinsa in ElectricTicketGenerator si in catch se arunca ParkingSpotNotFoundException)
        when(parkingSpotsCollection.getParkingSpotId(driver.getVehicle().getType(), true)).thenThrow(new ParkingSpotNotFoundException());
        assertThrowsExactly(ParkingSpotNotFoundException.class, () -> ticketGenerator.getTicket(parkingSpotsCollection, driver)); // nu mai exista locuri libere pt un sofer non-vip cu masina electrica
    }

}
