package strategy;

import database.ParkingSpotsCollection;
import exceptions.ParkingSpotNotFoundException;
import exceptions.SimultaneousOperationException;
import parking.Driver;
import structures.Ticket;

// Part of Strategy design patter
public interface TicketGenerator
{
    Ticket getTicket(ParkingSpotsCollection parkingSpotsCollection, Driver d) throws ParkingSpotNotFoundException, SimultaneousOperationException;
}
