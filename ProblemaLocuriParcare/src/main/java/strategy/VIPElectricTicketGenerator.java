package strategy;

import database.ParkingSpotsCollection;
import exceptions.ParkingSpotNotFoundException;
import parking.Driver;
import structures.Ticket;
import vehicles.VehicleType;

public class VIPElectricTicketGenerator implements TicketGenerator {
    @Override
    public Ticket getTicket(Driver driver) throws ParkingSpotNotFoundException {
        int vehicleTypeId = driver.getVehicle().getType();
        while (vehicleTypeId < VehicleType.values().length) {
            try {
                int idParkingSpot = findEmptyElectricSpotOnCurrentCategory(driver, vehicleTypeId);
                return new Ticket(idParkingSpot, driver.getVehicle());
            } catch (ParkingSpotNotFoundException exception) {
                ++vehicleTypeId;
            }

        }

        // Daca nu am iesit cu "return" din while, inseamna ca nu am gasit un loc de parcare.
        throw new ParkingSpotNotFoundException();
    }

    public int findEmptyElectricSpotOnCurrentCategory(Driver driver, int vehicleTypeId) throws ParkingSpotNotFoundException {
        // Caut un loc de parcare liber, cu charger electric si specific vehiculului soferului
        try {
            synchronized (ParkingSpotsCollection.class) {
                int idParkingSpot = ParkingSpotsCollection.getParkingSpotId(VehicleType.values()[vehicleTypeId], driver.getVehicle().isElectric());
                ParkingSpotsCollection.occupyParkingSpot(idParkingSpot, driver);
                return idParkingSpot;
            }
        } catch (RuntimeException ex) {
            throw new ParkingSpotNotFoundException();
        }
    }
}
