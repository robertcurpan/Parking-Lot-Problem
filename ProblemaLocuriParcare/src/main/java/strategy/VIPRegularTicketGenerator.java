package strategy;

import database.ParkingSpotsCollection;
import exceptions.ParkingSpotNotFoundException;
import exceptions.SimultaneousOperationException;
import parking.Driver;
import structures.Ticket;
import vehicles.VehicleType;

public class VIPRegularTicketGenerator implements TicketGenerator {
    @Override
    public Ticket getTicket(ParkingSpotsCollection parkingSpotsCollection, Driver driver) throws ParkingSpotNotFoundException, SimultaneousOperationException {
        int vehicleTypeId = driver.getVehicle().getType().ordinal();
        while (vehicleTypeId < VehicleType.values().length) {
            try {
                int idParkingSpot = findEmptyNonElectricSpotOnCurrentCategory(parkingSpotsCollection, driver, vehicleTypeId);
                return new Ticket(idParkingSpot, driver.getVehicle());
            } catch (ParkingSpotNotFoundException exception) {
                ++vehicleTypeId;
            }
        }

        // Daca nu am iesit cu "return" din while, inseamna ca nu am gasit un loc de parcare.
        throw new ParkingSpotNotFoundException();
    }

    public int findEmptyNonElectricSpotOnCurrentCategory(ParkingSpotsCollection parkingSpotsCollection, Driver driver, int vehicleTypeId) throws ParkingSpotNotFoundException, SimultaneousOperationException {
        // Caut un loc de parcare liber, cu charger electric si specific vehiculului soferului
        try {
            int idParkingSpot = parkingSpotsCollection.getParkingSpotId(VehicleType.values()[vehicleTypeId], driver.getVehicle().isElectric());
            parkingSpotsCollection.occupyParkingSpot(idParkingSpot, driver);
            return idParkingSpot;
        } catch (RuntimeException ex) {
            throw new ParkingSpotNotFoundException();
        }

    }

}
