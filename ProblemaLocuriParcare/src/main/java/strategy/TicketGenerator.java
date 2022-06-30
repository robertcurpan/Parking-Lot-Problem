package strategy;

import database.DriversCollection;
import database.ParkingSpotsCollection;
import exceptions.ParkingSpotNotFoundException;
import exceptions.SimultaneousOperationInDatabaseCollectionException;
import parking.Driver;
import structures.Ticket;

// Part of Strategy design patter
public interface TicketGenerator
{
    Ticket getTicket(ParkingSpotsCollection parkingSpotsCollection, Driver driver) throws ParkingSpotNotFoundException, SimultaneousOperationInDatabaseCollectionException;
}
