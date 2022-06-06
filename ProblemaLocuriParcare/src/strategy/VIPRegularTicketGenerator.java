package strategy;

import exceptions.ParkingSpotNotFoundException;
import parking.Driver;
import parking.ParkingLot;
import parking.ParkingSpot;
import structures.Ticket;
import vehicles.VehicleType;

public class VIPRegularTicketGenerator implements TicketGenerator {
    @Override
    public Ticket getTicket(ParkingLot parkingLot, Driver driver) throws ParkingSpotNotFoundException {
        int vehicleTypeId = VehicleType.values()[driver.getVehicle().getType()].ordinal();
        while (vehicleTypeId < VehicleType.values().length) {
            try {
                int idSpot = findEmptyNonElectricSpotOnCurrentCategory(parkingLot, driver, vehicleTypeId);

                return new Ticket(idSpot, driver.getVehicle());
            } catch (ParkingSpotNotFoundException exception) {
                ++vehicleTypeId;
            }
        }

        // Daca nu am iesit cu "return" din while, inseamna ca nu am gasit un loc de parcare.
        throw new ParkingSpotNotFoundException();
    }

    public int findEmptyNonElectricSpotOnCurrentCategory(ParkingLot parkingLot, Driver driver, int vehicleTypeId) throws ParkingSpotNotFoundException {
        VehicleType vehicleType = VehicleType.values()[vehicleTypeId];
        if (!parkingLot.getParkingSpots().containsKey(vehicleType)) {
            throw new ParkingSpotNotFoundException();
        }

        for (ParkingSpot parkingSpot : parkingLot.getParkingSpots().get(vehicleType)) {
            if (parkingSpot.isFree() && !parkingSpot.hasElectricCharger()) {
                synchronized (parkingSpot) {
                    if (parkingSpot.isFree()) {
                        parkingLot.occupyParkingSpot(parkingSpot.getId(), driver);

                        // Daca am gasit un loc, opresc cautarea
                        return parkingSpot.getId();
                    }
                }
            }
        }

        throw new ParkingSpotNotFoundException();
    }
}
