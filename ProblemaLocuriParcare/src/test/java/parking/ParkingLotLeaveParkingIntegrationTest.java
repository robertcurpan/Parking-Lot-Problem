package parking;

import database.Database;
import database.DriversCollection;
import database.ParkingSpotsCollection;
import exceptions.DriverNotFoundException;
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

    private DriversCollection driversCollection = new DriversCollection(database);

    @BeforeEach
    void initializeDatabaseCollections() {
        parkingSpotsCollection.initializeParkingSpotsCollection();
    }

    @AfterEach
    void resetDatabaseCollections() {
        parkingSpotsCollection.resetParkingSpotsCollection();
        driversCollection.resetDriversCollection();
    }

    @Test
    public void leaveParkingLot() throws ParkingSpotNotFoundException, ParkingSpotNotOccupiedException, SimultaneousOperationInDatabaseCollectionException, DriverNotFoundException {
        // 1) Given
        ParkingLotService parkingLotService = new ParkingLotService(new TicketGeneratorCreator(), parkingSpotsCollection, driversCollection);
        Vehicle vehicle = new Car("blue", 2400, false);
        Driver driver = new Driver("Robert", vehicle, false);

        // 2) When
        Ticket ticket = parkingLotService.getParkingTicket(driver);
        Driver driverLeft = parkingLotService.leaveParkingLot(ticket.getSpotId());

        // 3) Then
        assertEquals(driver, driverLeft);
    }

}
