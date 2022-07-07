package database;

import exceptions.VehicleNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parking.Driver;
import vehicles.Car;
import vehicles.Vehicle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

public class VehiclesCollectionIntegrationTest {
    private Database database = new Database("parkingLotTestDB");
    private VehiclesCollection vehiclesCollection = new VehiclesCollection(database);

    @BeforeEach
    void resetDriversCollection() {
        vehiclesCollection.resetVehiclesCollection();
    }

    @Test
    public void addDriver() throws VehicleNotFoundException {
        Driver driver = new Driver("Robert", false);
        Vehicle vehicle = new Car(driver, "red", 2000, false);

        vehiclesCollection.addVehicle(vehicle);

        assertEquals(vehicle, vehiclesCollection.getVehicleById(vehicle.getVehicleId()));
    }

    @Test
    public void removeDriver() throws VehicleNotFoundException {
        Driver driver = new Driver("Robert", false);
        Vehicle vehicle = new Car(driver, "red", 2000, false);

        vehiclesCollection.addVehicle(vehicle);
        assertEquals(vehicle, vehiclesCollection.getVehicleById(vehicle.getVehicleId()));
        vehiclesCollection.removeVehicle(vehicle);
        assertThrowsExactly(VehicleNotFoundException.class, () -> vehiclesCollection.getVehicleById(vehicle.getVehicleId()));
    }
}
