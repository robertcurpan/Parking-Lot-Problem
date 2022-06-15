package strategy;

import exceptions.ParkingSpotNotFoundException;
import parking.Driver;
import structures.Ticket;

// Part of Strategy design patter
public interface TicketGenerator
{
    Ticket getTicket(Driver d) throws ParkingSpotNotFoundException;
}
