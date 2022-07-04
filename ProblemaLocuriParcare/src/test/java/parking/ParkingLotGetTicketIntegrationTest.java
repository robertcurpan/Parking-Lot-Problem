package parking;

import database.Database;
import database.VehiclesCollection;
import database.ParkingSpotsCollection;
import exceptions.ParkingSpotNotFoundException;
import exceptions.SimultaneousOperationInDatabaseCollectionException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import structures.Ticket;
import vehicles.Car;
import vehicles.Vehicle;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ParkingLotGetTicketIntegrationTest
{
    private Database database = new Database("parkingLotTestDB");

    private ParkingSpotsCollection parkingSpotsCollection = new ParkingSpotsCollection(database);

    private VehiclesCollection vehiclesCollection = new VehiclesCollection(database);

    public static final String COLOR = "red";
    public static final int PRICE = 1000;
    public static final boolean NON_ELECTRIC = false;
    public static final String NAME = "Robert";
    public static final boolean VIP = true;

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
    public void getParkingTicket() throws ParkingSpotNotFoundException, SimultaneousOperationInDatabaseCollectionException {
        // Fiecare test are cumva prestabilite (alegem dinainte) ce tip de vehicul, cate locuri de parcare etc. si verificam ca am primit ce am asteptat

        // 1) Given (preconditiile testului - trebuie sa le avem ca sa putem executa testul)
        ParkingLotService parkingLotService = new ParkingLotService(new TicketGeneratorCreator(), parkingSpotsCollection, vehiclesCollection);
        Driver driver = new Driver("Robert", false);
        Vehicle vehicle = new Car(6, driver, "red", 2000, false);

        // 2) When
        Ticket parkingTicket = parkingLotService.getParkingTicket(vehicle);

        // 3) Then (asserturi)
        assertEquals(1, parkingTicket.getSpotId());
    }
}
