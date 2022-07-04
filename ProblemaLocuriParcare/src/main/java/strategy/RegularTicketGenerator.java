package strategy;

import database.ParkingSpotsCollection;
import exceptions.ParkingSpotNotFoundException;
import exceptions.SimultaneousOperationInDatabaseCollectionException;
import parking.ParkingSpotType;
import structures.Ticket;
import vehicles.Vehicle;

public class RegularTicketGenerator implements TicketGenerator
{
    @Override
    public Ticket getTicket(ParkingSpotsCollection parkingSpotsCollection, Vehicle vehicle) throws ParkingSpotNotFoundException, SimultaneousOperationInDatabaseCollectionException {
        // Caut un loc de parcare liber, cu charger electric si specific vehiculului soferului
        int idParkingSpot = parkingSpotsCollection.getIdForAvailableParkingSpot(ParkingSpotType.getSmallestFittingParkingSpotTypeFromVehicleType(vehicle.getVehicleType()), false);
        return new Ticket(idParkingSpot, vehicle);
    }

}
