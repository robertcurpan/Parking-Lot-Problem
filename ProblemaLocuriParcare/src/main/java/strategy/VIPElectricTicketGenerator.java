package strategy;

import database.DriversCollection;
import database.ParkingSpotsCollection;
import exceptions.ParkingSpotNotFoundException;
import exceptions.SimultaneousOperationInDatabaseCollectionException;
import parking.Driver;
import structures.Ticket;
import vehicles.VehicleType;

public class VIPElectricTicketGenerator implements TicketGenerator {
    @Override
    public Ticket getTicket(ParkingSpotsCollection parkingSpotsCollection, Driver driver) throws ParkingSpotNotFoundException, SimultaneousOperationInDatabaseCollectionException {
        int vehicleTypeId = driver.getVehicle().getType().ordinal();
        while (vehicleTypeId < VehicleType.values().length) {
            try {
                int idParkingSpot = findEmptyElectricSpotOnCurrentCategory(parkingSpotsCollection, driver, vehicleTypeId);
                return new Ticket(idParkingSpot, driver.getVehicle());
            } catch (ParkingSpotNotFoundException exception) {
                ++vehicleTypeId;
            }

        }

        // Daca nu am iesit cu "return" din while, inseamna ca nu am gasit un loc de parcare.
        throw new ParkingSpotNotFoundException();
    }

    //TODO metoda de mai jos trebuie sa fie in serviciu
    public int findEmptyElectricSpotOnCurrentCategory(ParkingSpotsCollection parkingSpotsCollection, Driver driver, int vehicleTypeId) throws ParkingSpotNotFoundException {
        // Caut un loc de parcare liber, cu charger electric si specific vehiculului soferului
        int idParkingSpot = parkingSpotsCollection.getParkingSpotId(VehicleType.values()[vehicleTypeId], driver.getVehicle().isElectric());
        return idParkingSpot;
    }

}
