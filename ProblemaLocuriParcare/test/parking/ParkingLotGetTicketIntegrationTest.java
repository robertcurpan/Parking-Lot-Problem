package parking;

import exceptions.ParkingSpotNotFoundException;
import input.ReadInputFromFile;
import org.junit.jupiter.api.Test;
import vehicles.Car;
import vehicles.Vehicle;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParkingLotGetTicketIntegrationTest
{
    @Test
    public void getParkingTicket() throws ParkingSpotNotFoundException
    {
        // Fiecare test are cumva prestabilite (alegem dinainte) ce tip de vehicul, cate locuri de parcare etc. si verificam ca am primit ce am asteptat

        // 1) Given (preconditiile testului - trebuie sa le avem ca sa putem executa testul)
        ReadInputFromFile readInputFromFile = new ReadInputFromFile();
        ParkingLot parkingLot = readInputFromFile.initializeAndGetParkingLot();
        Vehicle vehicle = new Car("red", 1000, false);
        Driver driver = new Driver("Robert", vehicle, true);

        // 2) When
        int parkingTicket = parkingLot.getParkingTicket(driver);

        // 3) Then (asserturi)
        assertEquals(2, parkingTicket);
    }
}
