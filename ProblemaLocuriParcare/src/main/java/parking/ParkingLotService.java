package parking;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import database.VehiclesCollection;
import database.ParkingSpotsCollection;
import exceptions.VehicleNotFoundException;
import exceptions.ParkingSpotNotFoundException;
import exceptions.ParkingSpotNotOccupiedException;
import exceptions.SimultaneousOperationInDatabaseCollectionException;
import org.bson.Document;
import strategy.TicketGenerator;
import structures.Ticket;
import vehicles.Vehicle;
import vehicles.VehicleType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class ParkingLotService {

    private TicketGeneratorCreator ticketGeneratorCreator;
    private ParkingSpotsCollection parkingSpotsCollection;
    private VehiclesCollection vehiclesCollection;

    public ParkingLotService(TicketGeneratorCreator ticketGeneratorCreator, ParkingSpotsCollection parkingSpotsCollection, VehiclesCollection vehiclesCollection) {
        this.ticketGeneratorCreator = ticketGeneratorCreator;
        this.parkingSpotsCollection = parkingSpotsCollection;
        this.vehiclesCollection = vehiclesCollection;
    }

    public Ticket getParkingTicket(Vehicle vehicle) throws ParkingSpotNotFoundException, SimultaneousOperationInDatabaseCollectionException {
        TicketGenerator ticketGenerator = ticketGeneratorCreator.getTicketGenerator(vehicle);
        Ticket ticket = ticketGenerator.getTicket(parkingSpotsCollection, vehicle);
        ParkingSpot parkingSpot = parkingSpotsCollection.getParkingSpotById(ticket.getSpotId());
        parkingSpot.setVehicleId(vehicle.getVehicleId()); // nu putem seta id-ul din interfata, asa ca il setam aici (mapare 1:1 dintre parkingSpotId si vehicleId)
        parkingSpotsCollection.updateParkingSpotWhenDriverParks(parkingSpot);
        vehiclesCollection.addVehicle(vehicle);
        return ticket;
    }

    public Vehicle leaveParkingLot(int idParkingSpot) throws ParkingSpotNotOccupiedException, SimultaneousOperationInDatabaseCollectionException, ParkingSpotNotFoundException, VehicleNotFoundException {
        ParkingSpot parkingSpot = parkingSpotsCollection.getParkingSpotById(idParkingSpot);
        if(!parkingSpotsCollection.isParkingSpotFree(parkingSpot)) {
            Vehicle vehicle = vehiclesCollection.getVehicleById(parkingSpot.getVehicleId());
            parkingSpotsCollection.updateParkingSpotWhenDriverLeaves(parkingSpot);  // o functie din colectia bazei de date trebuie sa stie doar operatii CRUD (valorile atributelor ce trebuie actualizate le ia din obiectul dat ca parametru - parkingSpot.isFree())
            vehiclesCollection.removeVehicle(vehicle);
            return vehicle;
        }

        throw new ParkingSpotNotOccupiedException();
    }

    public int getNumberOfEmptySpotsForParkingSpotType(ParkingSpotType parkingSpotType) {
        return parkingSpotsCollection.getNumberOfEmptySpotsForParkingSpotType(parkingSpotType);
    }

    public HashMap<Integer, Vehicle> getVehiclesAndCorrespondingParkingSpots() throws VehicleNotFoundException {
        HashMap<Integer, Vehicle> vehiclesAndCorrespondingParkingSpots = new HashMap<>();

        ArrayList<Vehicle> vehicles = vehiclesCollection.getAllVehicles();
        ArrayList<ParkingSpot> parkingSpots = parkingSpotsCollection.getParkingSpots();

        for(Vehicle vehicle : vehicles) {
            for(ParkingSpot parkingSpot : parkingSpots) {
                if(vehicle.getVehicleId().equals(parkingSpot.getVehicleId())) {
                    vehiclesAndCorrespondingParkingSpots.put(parkingSpot.getId(), vehicle);
                }
            }
        }

        return vehiclesAndCorrespondingParkingSpots;
    }

    public ArrayList<ParkingSpot> getAllParkingSpots() {
        return parkingSpotsCollection.getParkingSpots();
    }

    // TODO - DE VAZUT
    public ParkingSpot getParkingSpotById(int parkingSpotId) throws ParkingSpotNotFoundException {
        return parkingSpotsCollection.getParkingSpotById(parkingSpotId);
    }

}
