package Strategy;

import Parking.ParkingSpot;
import Vehicles.VehicleType;

import java.sql.Driver;
import java.util.ArrayList;

// Part of Strategy design patter
public interface TicketGenerator
{
    ArrayList<Integer> getTicket(ArrayList<ParkingSpot>[] parkingSpots, int vehicleTypeId);
}
