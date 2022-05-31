package parking;

import exceptions.ParkingSpotNotFoundException;
import exceptions.ParkingSpotNotOccupiedException;
import input.ReadInputFromFile;
import org.junit.jupiter.api.Test;
import vehicles.Car;
import vehicles.Vehicle;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParkingLotLeaveParkingIntegrationTest {

    @Test
    public void leaveParkingLot() throws ParkingSpotNotFoundException, ParkingSpotNotOccupiedException {
        // 1) Given
        ReadInputFromFile readInputFromFile = new ReadInputFromFile();
        ParkingLot parkingLot = readInputFromFile.initializeAndGetParkingLot();
        Vehicle vehicle = new Car("blue", 2400, false);
        Driver driver = new Driver("Robert", vehicle, false);

        // 2) When
        int idAssignedParkingSpot = parkingLot.getParkingTicket(driver);
        Driver driverLeft = parkingLot.leaveParkingLot(idAssignedParkingSpot);

        // 3) Then
        assertEquals(driver, driverLeft);
    }

}
