package parking;

import exceptions.ParkingSpotNotFoundException;
import input.ReadInputFromFile;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import strategy.TicketGenerator;
import structures.ParkingSpotIdAndVehicleTypeId;
import vehicles.Car;
import vehicles.VehicleType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class ParkingLotUnitTest {

    @Test
    public void getParkingTicket() throws ParkingSpotNotFoundException {
        // Given
        TicketGeneratorCreator ticketGeneratorCreator = mock(TicketGeneratorCreator.class);
        TicketGenerator ticketGenerator = mock(TicketGenerator.class);
        ParkingSpotIdAndVehicleTypeId parkingSpotIdAndVehicleTypeId = mock(ParkingSpotIdAndVehicleTypeId.class);

        Map<VehicleType, Integer> noOfExistingSpotsForVehicleType = new HashMap<>();
        noOfExistingSpotsForVehicleType.put(VehicleType.Motorcycle, 2);
        Map<VehicleType, Integer> emptySpotsNumber = new HashMap<>();
        emptySpotsNumber.put(VehicleType.Motorcycle, 2);
        Map<VehicleType, ArrayList<ParkingSpot>> parkingSpots = new HashMap<>();
        parkingSpots.put(VehicleType.Motorcycle, new ArrayList<>(Arrays.asList(new ParkingSpot(0, true, true), new ParkingSpot(1, true, true))));
        Map<Integer, Driver> assignedParkingSpots = new HashMap<>();
        ParkingLot parkingLot =  new ParkingLot(noOfExistingSpotsForVehicleType, emptySpotsNumber, parkingSpots, assignedParkingSpots, ticketGeneratorCreator); // colectii hardcodate
        Driver driver = new Driver("Andrei", new Car("pink", 2000, true), true);

        when(ticketGeneratorCreator.getTicketGenerator(driver)).thenReturn(ticketGenerator);
        when(ticketGenerator.getTicket(parkingLot, driver, VehicleType.values()[driver.getVehicle().getType()])).thenReturn(parkingSpotIdAndVehicleTypeId);
        when(parkingSpotIdAndVehicleTypeId.getParkingSpotId()).thenReturn(0);

        // When
        parkingLot.getParkingTicket(driver);

        // Then
        verify(ticketGeneratorCreator).getTicketGenerator(driver); // verific ca atunci cand se apeleaza getParkingTicket apeleaza metoda getTicketGenerator
        verify(ticketGenerator).getTicket(parkingLot, driver, VehicleType.values()[driver.getVehicle().getType()]);
        verify(parkingSpotIdAndVehicleTypeId).getParkingSpotId();
    }
}