package strategy;

import exceptions.ParkingSpotNotFoundException;
import input.ReadInputFromFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parking.Driver;
import parking.ParkingLot;
import structures.Ticket;
import vehicles.Car;
import vehicles.VehicleType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ElectricTicketGeneratorUnitTest {

    ParkingLot parkingLot;
    ReadInputFromFile readInputFromFile;

    @BeforeEach
    public void initializeParkingLot() {
        readInputFromFile = new ReadInputFromFile();
        parkingLot = readInputFromFile.initializeAndGetParkingLot();
    }

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
        ticket = ticketGenerator.getTicket(parkingLot, driver);
        assertEquals(4, ticket.getSpotId());
        assertThrowsExactly(ParkingSpotNotFoundException.class, () -> ticketGenerator.getTicket(parkingLot, driver)); // nu mai exista locuri libere pt un sofer non-vip cu masina electrica
    }

}
