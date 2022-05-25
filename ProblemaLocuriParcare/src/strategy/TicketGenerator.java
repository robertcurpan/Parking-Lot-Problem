package strategy;

import exceptions.ParkingSpotNotFoundException;
import parking.Driver;
import parking.ParkingLot;
import parking.ParkingSpot;
import structures.ParkingSpotIdAndVehicleTypeId;
import vehicles.VehicleType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Part of Strategy design patter
public interface TicketGenerator
{
    ParkingSpotIdAndVehicleTypeId getTicket(ParkingLot parkingLot, Driver d, VehicleType vehicleType) throws ParkingSpotNotFoundException;
}
