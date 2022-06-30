package database;

import exceptions.DriverNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parking.Driver;
import vehicles.Car;
import vehicles.Vehicle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

public class DriversCollectionIntegrationTest {
    private Database database = new Database("parkingLotTestDB");
    private DriversCollection driversCollection = new DriversCollection(database);

    @BeforeEach
    void resetDriversCollection() {
        driversCollection.resetDriversCollection();
    }

    @Test
    public void addDriver() throws DriverNotFoundException {
        Vehicle vehicle = new Car("red", 2000, true);
        Driver driver = new Driver("Robert", vehicle, true);

        driversCollection.addDriver(driver);
        System.out.println(driver.getId());

        assertEquals(driver, driversCollection.getDriverById(0));
    }

    @Test
    public void removeDriver() throws DriverNotFoundException {
        Vehicle vehicle = new Car("red", 2000, true);
        Driver driver = new Driver("Robert", vehicle, true);

        driversCollection.addDriver(driver);
        assertEquals(driver, driversCollection.getDriverById(0));
        driversCollection.removeDriver(driver);
        assertThrowsExactly(DriverNotFoundException.class, () -> driversCollection.getDriverById(0));
    }
}
