package parking;

import database.DriversCollection;
import database.ParkingSpotsCollection;
import exceptions.DriverNotFoundException;
import exceptions.ParkingSpotNotFoundException;
import exceptions.ParkingSpotNotOccupiedException;
import exceptions.SimultaneousOperationInDatabaseCollectionException;
import strategy.TicketGenerator;
import structures.Ticket;
import vehicles.VehicleType;

import java.util.ArrayList;
import java.util.HashMap;

public class ParkingLotService {

    private TicketGeneratorCreator ticketGeneratorCreator;
    private ParkingSpotsCollection parkingSpotsCollection;
    private DriversCollection driversCollection;

    public ParkingLotService(TicketGeneratorCreator ticketGeneratorCreator, ParkingSpotsCollection parkingSpotsCollection, DriversCollection driversCollection) {
        this.ticketGeneratorCreator = ticketGeneratorCreator;
        this.parkingSpotsCollection = parkingSpotsCollection;
        this.driversCollection = driversCollection;
    }

    public Ticket getParkingTicket(Driver driver) throws ParkingSpotNotFoundException, SimultaneousOperationInDatabaseCollectionException {
        TicketGenerator ticketGenerator = ticketGeneratorCreator.getTicketGenerator(driver);
        Ticket ticket = ticketGenerator.getTicket(parkingSpotsCollection, driver);
        ParkingSpot parkingSpot = getParkingSpotById(ticket.getSpotId());
        parkingSpot.setFree(false);
        parkingSpotsCollection.updateParkingSpotWhenDriverParks(parkingSpot, driver);
        driversCollection.addDriver(driver);
        return ticket;
    }

    public Driver leaveParkingLot(int idParkingSpot) throws ParkingSpotNotOccupiedException, SimultaneousOperationInDatabaseCollectionException, ParkingSpotNotFoundException, DriverNotFoundException {
        ParkingSpot parkingSpot = parkingSpotsCollection.getParkingSpotById(idParkingSpot);
        if(!parkingSpot.isFree()) {
            int driverId = parkingSpotsCollection.getDriverIdForOccupiedSpot(parkingSpot);
            Driver driver = driversCollection.getDriverById(driverId);
            parkingSpot.setFree(true);
            parkingSpotsCollection.updateParkingSpotFreeStatus(parkingSpot);  // o functie din colectia bazei de date trebuie sa stie doar operatii CRUD (valorile atributelor ce trebuie actualizate le ia din obiectul dat ca parametru - parkingSpot.isFree())
            driversCollection.removeDriver(driver);
            return driver;
        }

        throw new ParkingSpotNotOccupiedException();
    }

    public int getNumberOfEmptySpotsForVehicleType(VehicleType vehicleType) {
        return parkingSpotsCollection.getNumberOfEmptySpotsForVehicleType(vehicleType);
    }

    public HashMap<Integer, Driver> getDriversAndCorrespondingParkingSpots() throws DriverNotFoundException {
        return parkingSpotsCollection.getDriversAndCorrespondingParkingSpot(driversCollection.getAllDrivers());
    }

    public ArrayList<ParkingSpot> getAllParkingSpots() {
        return parkingSpotsCollection.getParkingSpots();
    }

    public ParkingSpot getParkingSpotById(int parkingSpotId) throws ParkingSpotNotFoundException {
        return parkingSpotsCollection.getParkingSpotById(parkingSpotId);
    }

}
