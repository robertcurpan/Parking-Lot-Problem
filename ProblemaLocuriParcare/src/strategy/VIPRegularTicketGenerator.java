package strategy;

import exceptions.ParkingSpotNotFoundException;
import parking.Driver;
import parking.ParkingLot;
import parking.ParkingSpot;
import structures.Ticket;
import vehicles.VehicleType;

public class VIPRegularTicketGenerator implements TicketGenerator
{
    @Override
    public Ticket getTicket(ParkingLot parkingLot, Driver driver) throws ParkingSpotNotFoundException
    {
        int idSpot;
        int vehicleTypeId = VehicleType.values()[driver.getVehicle().getType()].ordinal();
        while(vehicleTypeId < VehicleType.values().length) {
            idSpot = findEmptyNonElectricSpotOnCurrentCategory(parkingLot, driver, vehicleTypeId);

            if(idSpot != -1)
                return new Ticket(idSpot, driver.getVehicle());

            ++vehicleTypeId;
        }

        // Daca nu am iesit cu "return" din while, inseamna ca nu am gasit un loc de parcare.
        throw new ParkingSpotNotFoundException();
    }

    public int findEmptyNonElectricSpotOnCurrentCategory(ParkingLot parkingLot, Driver driver, int vehicleTypeId)
    {
        VehicleType vehicleType = VehicleType.values()[vehicleTypeId];

        for(ParkingSpot parkingSpot : parkingLot.getParkingSpots().get(vehicleType)) {
            if(parkingSpot.isFree() && !parkingSpot.hasElectricCharger()) {
                synchronized (parkingSpot) {
                    if(parkingSpot.isFree()) {
                        parkingLot.occupyParkingSpot(parkingSpot.getId(), driver);

                        // Daca am gasit un loc, opresc cautarea
                        return parkingSpot.getId();
                    }
                }
            }
        }

        return -1;
    }
}
