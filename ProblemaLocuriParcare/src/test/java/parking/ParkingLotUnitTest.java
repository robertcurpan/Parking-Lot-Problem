package parking;

import exceptions.ParkingSpotNotFoundException;
import exceptions.ParkingSpotNotOccupiedException;
import org.junit.jupiter.api.Test;
import strategy.TicketGenerator;
import structures.Ticket;
import vehicles.Car;
import vehicles.Motorcycle;
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

        ParkingLotService parkingLotService = new ParkingLotService(ticketGeneratorCreator);
        Driver driver = new Driver("Andrei", new Motorcycle("pink", 2000, true), false);

        when(ticketGeneratorCreator.getTicketGenerator(driver)).thenReturn(ticketGenerator);
        when(ticketGenerator.getTicket(driver)).thenReturn(ticketMock);

        // When
        Ticket ticket = parkingLotService.getParkingTicket(driver);

        // Then
        verify(ticketGeneratorCreator).getTicketGenerator(driver); // verific ca atunci cand se apeleaza getParkingTicket apeleaza metoda getTicketGenerator
        verify(ticketGenerator).getTicket(driver);
    }

    @Test
    public void throwExceptionWhenLeavingAFreeParkingSpot() throws ParkingSpotNotOccupiedException {
        // Given
        TicketGeneratorCreator ticketGeneratorCreator = mock(TicketGeneratorCreator.class);
        TicketGenerator ticketGenerator = mock(TicketGenerator.class);

        ParkingLotService parkingLotService = new ParkingLotService(ticketGeneratorCreator);
        Driver driver = new Driver("Andrei", new Car("pink", 2000, true), true);

        // When, Then
        assertThrowsExactly(ParkingSpotNotOccupiedException.class, () -> parkingLotService.leaveParkingLot(0));
    }
}