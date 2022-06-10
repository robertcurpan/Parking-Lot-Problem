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
        int vehicleTypeId = driver.getVehicle().getType();
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

        for (ParkingSpot parkingSpot : parkingLot.getParkingSpots()) {
            if (parkingSpot.isFree() && !parkingSpot.hasElectricCharger() && parkingSpot.getSpotType() == VehicleType.values()[vehicleTypeId]) {
                synchronized (parkingSpot) {
                    if (parkingSpot.isFree()) {
                        parkingLot.occupyParkingSpot(parkingSpot, driver);

                        // Daca am gasit un loc, opresc cautarea
                        return parkingSpot.getId();
                    }
                }
            }
        }

        throw new ParkingSpotNotFoundException();
    }
}
