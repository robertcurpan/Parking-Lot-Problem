package strategy;

import database.ParkingSpotsCollection;
import exceptions.ParkingSpotNotFoundException;
import exceptions.SimultaneousOperationException;
import parking.Driver;
import structures.Ticket;
import vehicles.VehicleType;

public class RegularTicketGenerator implements TicketGenerator
{
    @Override
    public Ticket getTicket(ParkingSpotsCollection parkingSpotsCollection, Driver driver) throws ParkingSpotNotFoundException, SimultaneousOperationException {
        // Caut un loc de parcare liber, cu charger electric si specific vehiculului soferului
        try {
            int idParkingSpot = parkingSpotsCollection.getParkingSpotId(driver.getVehicle().getType(), false);
            parkingSpotsCollection.occupyParkingSpot(idParkingSpot, driver);
            return new Ticket(idParkingSpot, driver.getVehicle());
        } catch (RuntimeException ex) {
            throw new ParkingSpotNotFoundException();
        }
    }
}
