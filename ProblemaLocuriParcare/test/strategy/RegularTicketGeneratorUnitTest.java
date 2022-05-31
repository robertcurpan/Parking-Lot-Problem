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
import vehicles.VehicleType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RegularTicketGeneratorUnitTest {
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
        when(driver.getVipStatus()).thenReturn(false);
        when(driver.getVehicle()).thenReturn(new Car("blue", 2000, false));

        TicketGenerator ticketGenerator = ticketGeneratorCreator.getTicketGenerator(driver);


        ParkingSpotIdAndVehicleTypeId parkingSpotIdAndVehicleTypeId;

        // When
        parkingSpotIdAndVehicleTypeId = ticketGenerator.getTicket(parkingLot, driver, VehicleType.Car);
        assertEquals(2, parkingSpotIdAndVehicleTypeId.getParkingSpotId());

        parkingSpotIdAndVehicleTypeId = ticketGenerator.getTicket(parkingLot, driver, VehicleType.Car);
        assertEquals(3, parkingSpotIdAndVehicleTypeId.getParkingSpotId());

        assertThrowsExactly(ParkingSpotNotFoundException.class, () -> ticketGenerator.getTicket(parkingLot, driver, VehicleType.Car));
    }
}
