package strategy;

import database.ParkingSpotsCollection;
import exceptions.ParkingSpotNotFoundException;
import parking.Driver;
import structures.Ticket;
import vehicles.VehicleType;

public class RegularTicketGenerator implements TicketGenerator
{
    @Override
    public Ticket getTicket(Driver driver) throws ParkingSpotNotFoundException {
        // Caut un loc de parcare liber, cu charger electric si specific vehiculului soferului
        try {
            synchronized (ParkingSpotsCollection.class) {
                int idParkingSpot = ParkingSpotsCollection.getParkingSpotId(VehicleType.values()[driver.getVehicle().getType()], false);
                ParkingSpotsCollection.occupyParkingSpot(idParkingSpot, driver);
                return new Ticket(idParkingSpot, driver.getVehicle());
            }
        } catch (RuntimeException ex) {
            throw new ParkingSpotNotFoundException();
        }
    }
}
