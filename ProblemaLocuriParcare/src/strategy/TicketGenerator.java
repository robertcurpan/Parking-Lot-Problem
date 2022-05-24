package strategy;

import exceptions.ParkingSpotNotFoundException;
import parking.Driver;
import parking.ParkingSpot;
import structures.ParkingSpotIdAndVehicleTypeId;

import java.util.ArrayList;
import java.util.HashMap;

// Part of Strategy design patter
public interface TicketGenerator
{
    ParkingSpotIdAndVehicleTypeId getTicket(ArrayList<ParkingSpot>[] parkingSpots, ArrayList<Integer> nrEmptySpots, HashMap<Integer, Driver> drivers, Driver d, int vehicleTypeId) throws ParkingSpotNotFoundException;
}
