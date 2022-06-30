package database;

import exceptions.ParkingSpotNotFoundException;
import exceptions.ParkingSpotNotOccupiedException;
import exceptions.SimultaneousOperationInDatabaseCollectionException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parking.Driver;
import parking.ParkingSpot;
import vehicles.Car;
import vehicles.Vehicle;
import vehicles.VehicleType;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParkingSpotsCollectionIntegrationTest {
    private Database database = new Database("parkingLotTestDB");
    private ParkingSpotsCollection parkingSpotsCollection = new ParkingSpotsCollection(database);

    @BeforeEach
    void initializeDatabaseCollections() {
        parkingSpotsCollection.initializeParkingSpotsCollection();
    }

    @AfterEach
    void resetDatabaseCollections() {
        parkingSpotsCollection.resetParkingSpotsCollection();
    }

    @Test
    public void getDriverIdToParkingSpot() throws ParkingSpotNotFoundException, SimultaneousOperationInDatabaseCollectionException, ParkingSpotNotOccupiedException {
        Vehicle vehicle = new Car("red", 2000, false);
        Driver driver = new Driver("Robert", vehicle, false);
        ParkingSpot parkingSpot = new ParkingSpot(1, vehicle.getType(), false, false);

        parkingSpotsCollection.updateParkingSpotWhenDriverParks(parkingSpot, driver);
        assertEquals(8, parkingSpotsCollection.getDriverIdForOccupiedSpot(parkingSpot));
    }

    @Test
    public void getNoOfEmptySpotsForVehicleType() throws ParkingSpotNotFoundException, SimultaneousOperationInDatabaseCollectionException {
        Vehicle vehicle = new Car("red", 2000, false);
        Driver driver = new Driver("Robert", vehicle, false);
        ParkingSpot parkingSpot1 = new ParkingSpot(1, vehicle.getType(), false, false);
        ParkingSpot parkingSpot2 = new ParkingSpot(3, vehicle.getType(), false, false);

        // Exista 3 locuri disponibile pt masina si ocupam 2, deci va ramane 1 liber
        parkingSpotsCollection.updateParkingSpotWhenDriverParks(parkingSpot1, driver);
        parkingSpotsCollection.updateParkingSpotWhenDriverParks(parkingSpot2, driver);

        assertEquals(1, parkingSpotsCollection.getNumberOfEmptySpotsForVehicleType(VehicleType.CAR));
    }

    @Test
    public void getParkingSpotId() throws ParkingSpotNotFoundException, SimultaneousOperationInDatabaseCollectionException {
        Vehicle vehicle = new Car("red", 2000, false);
        Driver driver = new Driver("Robert", vehicle, false);

        assertEquals(7, parkingSpotsCollection.getParkingSpotId(VehicleType.MOTORCYCLE, true));
    }

    @Test
    public void updateParkingSpotWhenDriverParks() throws ParkingSpotNotFoundException, SimultaneousOperationInDatabaseCollectionException {
        Vehicle vehicle = new Car("red", 2000, false);
        Driver driver = new Driver("Robert", vehicle, false);
        ParkingSpot parkingSpot = new ParkingSpot(1, vehicle.getType(), false, false);

        parkingSpotsCollection.updateParkingSpotWhenDriverParks(parkingSpot, driver);

        assertEquals(false, parkingSpotsCollection.getParkingSpotById(1).isFree());
    }

}
