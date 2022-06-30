package strategy;

import database.DriversCollection;
import database.ParkingSpotsCollection;
import exceptions.ParkingSpotNotFoundException;
import exceptions.SimultaneousOperationInDatabaseCollectionException;
import parking.Driver;
import structures.Ticket;


public class ElectricTicketGenerator implements TicketGenerator
{
    @Override
    public Ticket getTicket(ParkingSpotsCollection parkingSpotsCollection, Driver driver) throws ParkingSpotNotFoundException, SimultaneousOperationInDatabaseCollectionException {
        // Caut un loc de parcare liber, cu charger electric si specific vehiculului soferului
        int idParkingSpot = parkingSpotsCollection.getParkingSpotId(driver.getVehicle().getType(), true);
        return new Ticket(idParkingSpot, driver.getVehicle());
    }

}
