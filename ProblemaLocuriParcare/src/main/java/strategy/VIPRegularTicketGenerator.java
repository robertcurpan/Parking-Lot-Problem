package strategy;

import database.ParkingSpotsCollection;
import exceptions.ParkingSpotNotFoundException;
import exceptions.SimultaneousOperationInDatabaseCollectionException;
import parking.ParkingSpotType;
import structures.Ticket;
import vehicles.Vehicle;
import vehicles.VehicleType;

public class VIPRegularTicketGenerator implements TicketGenerator {
    @Override
    public Ticket getTicket(ParkingSpotsCollection parkingSpotsCollection, Vehicle vehicle) throws ParkingSpotNotFoundException, SimultaneousOperationInDatabaseCollectionException {
        int parkingSpotTypeId = TicketGeneratorUtil.getSmallestFittingParkingSpotTypeFromVehicleType(vehicle.getVehicleType()).ordinal();
        while (parkingSpotTypeId < ParkingSpotType.values().length) {
            try {
                int idParkingSpot = findEmptyNonElectricSpotOnCurrentCategory(parkingSpotsCollection, vehicle, parkingSpotTypeId);
                return new Ticket(idParkingSpot, vehicle);
            } catch (ParkingSpotNotFoundException exception) {
                ++parkingSpotTypeId;
            }
        }

        // Daca nu am iesit cu "return" din while, inseamna ca nu am gasit un loc de parcare.
        throw new ParkingSpotNotFoundException();
    }

    public int findEmptyNonElectricSpotOnCurrentCategory(ParkingSpotsCollection parkingSpotsCollection, Vehicle vehicle, int parkingSpotTypeId) throws ParkingSpotNotFoundException {
        int idParkingSpot = parkingSpotsCollection.getIdForAvailableParkingSpot(ParkingSpotType.values()[parkingSpotTypeId], vehicle.getElectric());
        return idParkingSpot;
    }

}
