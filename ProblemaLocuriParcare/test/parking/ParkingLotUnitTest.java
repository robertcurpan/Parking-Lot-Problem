package parking;

import exceptions.ParkingSpotNotFoundException;
import exceptions.ParkingSpotNotOccupiedException;
import org.junit.jupiter.api.Test;
import strategy.TicketGenerator;
import structures.Ticket;
import vehicles.Car;
import vehicles.Motorcycle;
import vehicles.Truck;
import vehicles.VehicleType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.*;

public class ParkingLotUnitTest {

    @Test
    public void getCorrectParkingSpotIdAndVerifyMethodCalls() throws ParkingSpotNotFoundException {
        // Given
        TicketGeneratorCreator ticketGeneratorCreator = mock(TicketGeneratorCreator.class);
        TicketGenerator ticketGenerator = mock(TicketGenerator.class);
        Ticket ticketMock = mock(Ticket.class);

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

        when(ticketGeneratorCreator.getTicketGenerator(driver)).thenReturn(ticketGenerator);
        when(ticketGenerator.getTicket(parkingLot, driver)).thenReturn(ticketMock);

        // When
        Ticket ticket = parkingLotService.getParkingTicket(parkingLot, driver);

        // Then
        verify(ticketGeneratorCreator).getTicketGenerator(driver); // verific ca atunci cand se apeleaza getParkingTicket apeleaza metoda getTicketGenerator
        verify(ticketGenerator).getTicket(parkingLot, driver);
        assertEquals(0, ticket.getSpotId());
    }

    @Test
    public void throwExceptionWhenLeavingAFreeParkingSpot() throws ParkingSpotNotOccupiedException {
        // Given
        TicketGeneratorCreator ticketGeneratorCreator = mock(TicketGeneratorCreator.class);
        TicketGenerator ticketGenerator = mock(TicketGenerator.class);

        Map<VehicleType, Integer> noOfExistingSpotsForVehicleType = new HashMap<>();
        noOfExistingSpotsForVehicleType.put(VehicleType.Motorcycle, 2);
        Map<VehicleType, Integer> emptySpotsNumber = new HashMap<>();
        emptySpotsNumber.put(VehicleType.Motorcycle, 2);
        Map<VehicleType, ArrayList<ParkingSpot>> parkingSpots = new HashMap<>();
        parkingSpots.put(VehicleType.Motorcycle, new ArrayList<>(Arrays.asList(new ParkingSpot(0, true, true), new ParkingSpot(1, true, true))));
        Map<Integer, Driver> assignedParkingSpots = new HashMap<>();

        ParkingLot parkingLot =  new ParkingLot(noOfExistingSpotsForVehicleType, emptySpotsNumber, parkingSpots, assignedParkingSpots); // colectii hardcodate
        ParkingLotService parkingLotService = new ParkingLotService(ticketGeneratorCreator);
        Driver driver = new Driver("Andrei", new Car("pink", 2000, true), true);

        // When, Then
        assertThrowsExactly(ParkingSpotNotOccupiedException.class, () -> parkingLotService.leaveParkingLot(parkingLot, 0));
    }
}