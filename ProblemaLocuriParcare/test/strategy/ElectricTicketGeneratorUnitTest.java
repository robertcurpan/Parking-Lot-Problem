package strategy;

import exceptions.ParkingSpotNotFoundException;
import input.ReadInputFromFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parking.Driver;
import parking.ParkingLot;
import parking.TicketGeneratorCreator;
import structures.ParkingSpotIdAndVehicleTypeId;
import vehicles.Car;
import vehicles.Motorcycle;
import vehicles.VehicleType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ElectricTicketGeneratorUnitTest {

    ParkingLot parkingLot;
    ReadInputFromFile readInputFromFile;
    TicketGeneratorCreator ticketGeneratorCreator;

    @BeforeEach
    public void initializeParkingLot() {
        readInputFromFile = new ReadInputFromFile();
        parkingLot = readInputFromFile.initializeAndGetParkingLot();
        ticketGeneratorCreator = new TicketGeneratorCreator();
    }

    @Test
    public void throwExceptionWhenNoSpotIsAvailable() throws ParkingSpotNotFoundException {
        // Given
        Driver driver = mock(Driver.class);
        when(driver.getVipStatus()).thenReturn(false);  // specificam ca driver-ul nu e VIP
        when(driver.getVehicle()).thenReturn(new Car("red", 2000, true)); // specificam ca driver-ul are o masina electrica

        TicketGenerator ticketGenerator = ticketGeneratorCreator.getTicketGenerator(driver);


        ParkingSpotIdAndVehicleTypeId parkingSpotIdAndVehicleTypeId;

        // When
        parkingSpotIdAndVehicleTypeId = ticketGenerator.getTicket(parkingLot, driver, VehicleType.Car);
        assertEquals(4, parkingSpotIdAndVehicleTypeId.getParkingSpotId());
        assertThrowsExactly(ParkingSpotNotFoundException.class, () -> ticketGenerator.getTicket(parkingLot, driver, VehicleType.Car)); // nu mai exista locuri libere pt un sofer non-vip cu masina electrica
    }

}
