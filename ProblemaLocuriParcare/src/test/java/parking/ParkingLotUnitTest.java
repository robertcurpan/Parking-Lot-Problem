package parking;

import database.DriversCollection;
import database.ParkingSpotsCollection;
import exceptions.ParkingSpotNotFoundException;
import exceptions.ParkingSpotNotOccupiedException;
import exceptions.SimultaneousOperationInDatabaseCollectionException;
import org.junit.jupiter.api.Test;
import strategy.TicketGenerator;
import structures.Ticket;
import vehicles.Motorcycle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.*;

public class ParkingLotUnitTest {

    @Test
    public void getCorrectParkingSpotIdAndVerifyMethodCalls() throws ParkingSpotNotFoundException, SimultaneousOperationInDatabaseCollectionException {
        // Given
        ParkingSpotsCollection parkingSpotsCollection = mock(ParkingSpotsCollection.class);
        DriversCollection driversCollection = mock(DriversCollection.class);
        TicketGeneratorCreator ticketGeneratorCreator = mock(TicketGeneratorCreator.class);
        TicketGenerator ticketGenerator = mock(TicketGenerator.class);
        Ticket ticketMock = mock(Ticket.class);
        ParkingSpot parkingSpotMock = mock(ParkingSpot.class);

        ParkingLotService parkingLotService = new ParkingLotService(ticketGeneratorCreator, parkingSpotsCollection, driversCollection);
        Driver driver = new Driver("Andrei", new Motorcycle("pink", 2000, true), false);

        when(ticketGeneratorCreator.getTicketGenerator(driver)).thenReturn(ticketGenerator);
        when(ticketGenerator.getTicket(parkingSpotsCollection, driver)).thenReturn(ticketMock);
        when(parkingLotService.getParkingSpotById(0)).thenReturn(parkingSpotMock);

        // When
        Ticket ticket = parkingLotService.getParkingTicket(driver);

        // Then
        verify(ticketGeneratorCreator).getTicketGenerator(driver); // verific ca atunci cand se apeleaza getParkingTicket apeleaza metoda getTicketGenerator
        verify(ticketGenerator).getTicket(parkingSpotsCollection, driver);
        verify(parkingSpotsCollection).updateParkingSpotWhenDriverParks(parkingSpotMock, driver);
        verify(driversCollection).addDriver(driver);
    }

    @Test
    public void throwExceptionWhenLeavingAFreeParkingSpot() throws ParkingSpotNotOccupiedException, ParkingSpotNotFoundException {
        // Given
        ParkingSpotsCollection parkingSpotsCollection = mock(ParkingSpotsCollection.class);
        DriversCollection driversCollection = mock(DriversCollection.class);
        TicketGeneratorCreator ticketGeneratorCreator = mock(TicketGeneratorCreator.class);
        ParkingSpot parkingSpot = mock(ParkingSpot.class);
        ParkingLotService parkingLotService = new ParkingLotService(ticketGeneratorCreator, parkingSpotsCollection, driversCollection);

        when(parkingSpotsCollection.getParkingSpotById(0)).thenReturn(parkingSpot);
        when(parkingSpotsCollection.getDriverIdForOccupiedSpot(parkingSpot)).thenThrow(new ParkingSpotNotOccupiedException());

        assertThrowsExactly(ParkingSpotNotOccupiedException.class, () -> parkingLotService.leaveParkingLot(0));
    }
}