package strategy;

import database.ParkingSpotsCollection;
import exceptions.ParkingSpotNotFoundException;
import exceptions.SimultaneousOperationInDatabaseCollectionException;
import parking.Driver;
import structures.Ticket;
import vehicles.Vehicle;

// Part of Strategy design patter
public interface TicketGenerator
{
    Ticket getTicket(ParkingSpotsCollection parkingSpotsCollection, Vehicle vehicle) throws ParkingSpotNotFoundException, SimultaneousOperationInDatabaseCollectionException;
}
