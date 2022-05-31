package strategy;

import exceptions.ParkingSpotNotFoundException;
import input.ReadInputFromFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parking.Driver;
import parking.ParkingLot;
import parking.TicketGeneratorCreator;
import structures.ParkingSpotIdAndVehicleTypeId;
import vehicles.Motorcycle;
import vehicles.Truck;
import vehicles.VehicleType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class VIPRegularTicketGeneratorUnitTest {
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
    public void getSpotFromTheNextParkingSpotCategories() throws ParkingSpotNotFoundException {
        // Given
        Driver driver = mock(Driver.class);
        when(driver.getVipStatus()).thenReturn(true);
        when(driver.getVehicle()).thenReturn(new Motorcycle("blue", 2000, false));

        TicketGenerator ticketGenerator = ticketGeneratorCreator.getTicketGenerator(driver);


        ParkingSpotIdAndVehicleTypeId parkingSpotIdAndVehicleTypeId;

        // When
        parkingSpotIdAndVehicleTypeId = ticketGenerator.getTicket(parkingLot, driver, VehicleType.Motorcycle);
        assertEquals(1, parkingSpotIdAndVehicleTypeId.getParkingSpotId());

        parkingSpotIdAndVehicleTypeId = ticketGenerator.getTicket(parkingLot, driver, VehicleType.Motorcycle);
        assertEquals(2, parkingSpotIdAndVehicleTypeId.getParkingSpotId());

        parkingSpotIdAndVehicleTypeId = ticketGenerator.getTicket(parkingLot, driver, VehicleType.Motorcycle);
        assertEquals(3, parkingSpotIdAndVehicleTypeId.getParkingSpotId());

        parkingSpotIdAndVehicleTypeId = ticketGenerator.getTicket(parkingLot, driver, VehicleType.Motorcycle);
        assertEquals(7, parkingSpotIdAndVehicleTypeId.getParkingSpotId());
    }

    @Test
    public void throwExceptionWhenThereIsNoSpotAvailable() throws ParkingSpotNotFoundException {
        // Given
        Driver driver = mock(Driver.class);
        when(driver.getVipStatus()).thenReturn(true);
        when(driver.getVehicle()).thenReturn(new Truck("blue", 2000, false));

        TicketGenerator ticketGenerator = ticketGeneratorCreator.getTicketGenerator(driver);

        ParkingSpotIdAndVehicleTypeId parkingSpotIdAndVehicleTypeId;
        parkingSpotIdAndVehicleTypeId = ticketGenerator.getTicket(parkingLot, driver, VehicleType.Truck);
        assertThrowsExactly(ParkingSpotNotFoundException.class, () -> ticketGenerator.getTicket(parkingLot, driver, VehicleType.Truck));
    }
}
