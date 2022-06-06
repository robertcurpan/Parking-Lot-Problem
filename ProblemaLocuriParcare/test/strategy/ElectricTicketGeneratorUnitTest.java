package strategy;

import exceptions.ParkingSpotNotFoundException;
import input.ReadInputFromFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parking.*;
import structures.Ticket;
import vehicles.Car;
import vehicles.VehicleType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ElectricTicketGeneratorUnitTest {
    @Test
    public void throwExceptionWhenNoSpotIsAvailable() throws ParkingSpotNotFoundException {
        // Given
        ReadInputFromFile readInputFromFile = new ReadInputFromFile();
        ParkingLot parkingLot = readInputFromFile.initializeAndGetParkingLot();

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

    @Test
    public void throwExceptionWhenCarSpotsDoNotExist() throws ParkingSpotNotFoundException {
        // Given
        TicketGeneratorCreator ticketGeneratorCreator = new TicketGeneratorCreator();

        Map<VehicleType, Integer> noOfExistingSpotsForVehicleType = new HashMap<>();
        noOfExistingSpotsForVehicleType.put(VehicleType.Motorcycle, 2);
        Map<VehicleType, Integer> emptySpotsNumber = new HashMap<>();
        emptySpotsNumber.put(VehicleType.Motorcycle, 2);
        Map<VehicleType, ArrayList<ParkingSpot>> parkingSpots = new HashMap<>();
        parkingSpots.put(VehicleType.Motorcycle, new ArrayList<>(Arrays.asList(new ParkingSpot(0, true, true), new ParkingSpot(1, true, true))));
        Map<Integer, Driver> assignedParkingSpots = new HashMap<>();

        ParkingLot parkingLot =  new ParkingLot(noOfExistingSpotsForVehicleType, emptySpotsNumber, parkingSpots, assignedParkingSpots); // colectii hardcodate
        ParkingLotService parkingLotService = new ParkingLotService(ticketGeneratorCreator);
        Driver driver = new Driver("Andrei", new Car("pink", 2000, true), false);

        // When + Then
        assertThrowsExactly(ParkingSpotNotFoundException.class, () -> parkingLotService.getParkingTicket(parkingLot, driver));
    }

}
