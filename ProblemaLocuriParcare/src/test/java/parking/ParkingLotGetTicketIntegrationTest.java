package parking;

import exceptions.ParkingSpotNotFoundException;
import input.ReadInputFromFile;
import org.junit.jupiter.api.Test;
import structures.Ticket;
import vehicles.Car;
import vehicles.Vehicle;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ParkingLotGetTicketIntegrationTest
{
    public static final String COLOR = "red";
    public static final int PRICE = 1000;
    public static final boolean NON_ELECTRIC = false;
    public static final String NAME = "Robert";
    public static final boolean VIP = true;

    @Test
    public void getParkingTicket() throws ParkingSpotNotFoundException
    {
        // Fiecare test are cumva prestabilite (alegem dinainte) ce tip de vehicul, cate locuri de parcare etc. si verificam ca am primit ce am asteptat

        // 1) Given (preconditiile testului - trebuie sa le avem ca sa putem executa testul)
        ReadInputFromFile readInputFromFile = new ReadInputFromFile();
        ParkingLot parkingLot = readInputFromFile.initializeAndGetParkingLot();
        ParkingLotService parkingLotService = new ParkingLotService(new TicketGeneratorCreator());
        Vehicle vehicle = new Car(COLOR, PRICE, NON_ELECTRIC);
        Driver driver = new Driver(NAME, vehicle, VIP);

        // 2) When
        Ticket parkingTicket = parkingLotService.getParkingTicket(parkingLot, driver);

        // 3) Then (asserturi)
        assertEquals(1, parkingTicket.getSpotId());
    }
}
