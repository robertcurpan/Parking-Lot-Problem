package database;

import exceptions.ParkingSpotNotFoundException;
import exceptions.ParkingSpotNotOccupiedException;
import exceptions.SimultaneousOperationInDatabaseCollectionException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parking.Driver;
import parking.ParkingSpot;
import parking.ParkingSpotType;
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
    public void getVehicleIdFromParkingSpot() throws ParkingSpotNotFoundException, SimultaneousOperationInDatabaseCollectionException, ParkingSpotNotOccupiedException {
        Driver driver = new Driver("Robert", false);
        Vehicle vehicle = new Car(6, driver, "red", 2000, false);
        ParkingSpot parkingSpot = new ParkingSpot(1, vehicle.getVehicleId(), ParkingSpotType.getSmallestFittingParkingSpotTypeFromVehicleType(vehicle.getVehicleType()), false, false);

        parkingSpotsCollection.updateParkingSpotWhenDriverParks(parkingSpot);
        assertEquals(6, parkingSpotsCollection.getParkingSpotById(parkingSpot.getId()).getVehicleId());
    }

    @Test
    public void getNoOfEmptySpotsForVehicleType() throws ParkingSpotNotFoundException, SimultaneousOperationInDatabaseCollectionException {
        Driver driver = new Driver("Robert", false);
        Vehicle vehicle1 = new Car(6, driver, "red", 2000, false);
        Vehicle vehicle2 = new Car(8, driver, "red", 2000, false);
        ParkingSpot parkingSpot1 = new ParkingSpot(1, vehicle1.getVehicleId(), ParkingSpotType.getSmallestFittingParkingSpotTypeFromVehicleType(vehicle1.getVehicleType()), false, false);
        ParkingSpot parkingSpot2 = new ParkingSpot(3, vehicle2.getVehicleId(), ParkingSpotType.getSmallestFittingParkingSpotTypeFromVehicleType(vehicle2.getVehicleType()), false, false);

        // Exista 3 locuri disponibile pt masina si ocupam 2, deci va ramane 1 liber
        parkingSpotsCollection.updateParkingSpotWhenDriverParks(parkingSpot1);
        parkingSpotsCollection.updateParkingSpotWhenDriverParks(parkingSpot2);

        assertEquals(1, parkingSpotsCollection.getNumberOfEmptySpotsForParkingSpotType(ParkingSpotType.getSmallestFittingParkingSpotTypeFromVehicleType(VehicleType.CAR)));
    }

    @Test
    public void getParkingSpotId() throws ParkingSpotNotFoundException {
        assertEquals(7, parkingSpotsCollection.getIdForAvailableParkingSpot(ParkingSpotType.getSmallestFittingParkingSpotTypeFromVehicleType(VehicleType.MOTORCYCLE), true));
    }

    @Test
    public void updateParkingSpotWhenDriverParks() throws ParkingSpotNotFoundException, SimultaneousOperationInDatabaseCollectionException {
        Driver driver = new Driver("Robert", false);
        Vehicle vehicle = new Car(6, driver, "red", 2000, false);
        ParkingSpot parkingSpot = new ParkingSpot(1, vehicle.getVehicleId(), ParkingSpotType.getSmallestFittingParkingSpotTypeFromVehicleType(vehicle.getVehicleType()), false, false);

        parkingSpotsCollection.updateParkingSpotWhenDriverParks(parkingSpot);

        assertEquals(false, parkingSpotsCollection.getParkingSpotById(1).getFree());
    }

}
