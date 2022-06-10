package strategy;

import exceptions.ParkingSpotNotFoundException;
import parking.Driver;
import parking.ParkingLot;
import structures.Ticket;

// Part of Strategy design patter
public interface TicketGenerator
{
    Ticket getTicket(ParkingLot parkingLot, Driver d) throws ParkingSpotNotFoundException;
}
