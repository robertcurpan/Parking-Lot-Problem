package parking;

import database.Database;
import database.VehiclesCollection;
import database.ParkingSpotsCollection;
import exceptions.VehicleNotFoundException;
import exceptions.ParkingSpotNotFoundException;
import exceptions.ParkingSpotNotOccupiedException;
import exceptions.SimultaneousOperationInDatabaseCollectionException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import structures.Ticket;
import vehicles.Car;
import vehicles.Vehicle;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParkingLotLeaveParkingIntegrationTest {
    private Database database = new Database("parkingLotTestDB");

    private ParkingSpotsCollection parkingSpotsCollection = new ParkingSpotsCollection(database);

    private VehiclesCollection vehiclesCollection = new VehiclesCollection(database);

    @BeforeEach
    void initializeDatabaseCollections() {
        parkingSpotsCollection.initializeParkingSpotsCollection();
    }

    @AfterEach
    void resetDatabaseCollections() {
        parkingSpotsCollection.resetParkingSpotsCollection();
        vehiclesCollection.resetVehiclesCollection();
    }

    @Test
    public void leaveParkingLot() throws ParkingSpotNotFoundException, ParkingSpotNotOccupiedException, SimultaneousOperationInDatabaseCollectionException, VehicleNotFoundException {
        // 1) Given
        ParkingLotService parkingLotService = new ParkingLotService(new TicketGeneratorCreator(), parkingSpotsCollection, vehiclesCollection);
        Driver driver = new Driver("Robert", false);
        Vehicle vehicle = new Car(driver, "red", 2000, false);

        // 2) When
        Ticket ticket = parkingLotService.getParkingTicket(vehicle);
        Vehicle vehicleLeft = parkingLotService.leaveParkingLot(ticket.getSpotId());

        // 3) Then
        assertEquals(vehicle, vehicleLeft);
    }

}
