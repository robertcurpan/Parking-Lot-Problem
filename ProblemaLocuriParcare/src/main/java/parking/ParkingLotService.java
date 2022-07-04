package parking;

import database.VehiclesCollection;
import database.ParkingSpotsCollection;
import exceptions.VehicleNotFoundException;
import exceptions.ParkingSpotNotFoundException;
import exceptions.ParkingSpotNotOccupiedException;
import exceptions.SimultaneousOperationInDatabaseCollectionException;
import strategy.TicketGenerator;
import structures.Ticket;
import vehicles.Vehicle;
import vehicles.VehicleType;

import java.util.ArrayList;
import java.util.HashMap;

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
        ParkingSpot parkingSpot = getParkingSpotById(ticket.getSpotId());
        parkingSpot.setFree(false);
        parkingSpot.setVehicleId(ticket.getSpotId() + 7); // nu putem seta id-ul din interfata, asa ca il setam aici (mapare 1:1 dintre parkingSpotId si vehicleId)
        vehicle.setVehicleId(parkingSpot.getVehicleId());
        parkingSpotsCollection.updateParkingSpotWhenDriverParks(parkingSpot);
        vehiclesCollection.addVehicle(vehicle);
        return ticket;
    }

    public Vehicle leaveParkingLot(int idParkingSpot) throws ParkingSpotNotOccupiedException, SimultaneousOperationInDatabaseCollectionException, ParkingSpotNotFoundException, VehicleNotFoundException {
        ParkingSpot parkingSpot = parkingSpotsCollection.getParkingSpotById(idParkingSpot);
        if(!parkingSpot.getFree()) {
            Vehicle vehicle = vehiclesCollection.getVehicleById(parkingSpot.getVehicleId());
            parkingSpot.setFree(true);
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
        return parkingSpotsCollection.getVehiclesAndCorrespondingParkingSpots(vehiclesCollection.getAllVehicles());
    }

    public ArrayList<ParkingSpot> getAllParkingSpots() {
        return parkingSpotsCollection.getParkingSpots();
    }

    public ParkingSpot getParkingSpotById(int parkingSpotId) throws ParkingSpotNotFoundException {
        return parkingSpotsCollection.getParkingSpotById(parkingSpotId);
    }

}
