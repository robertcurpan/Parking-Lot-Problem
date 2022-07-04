package parking;

import org.junit.jupiter.api.Test;
import strategy.*;
import vehicles.Car;
import vehicles.Vehicle;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TicketGeneratorCreatorUnitTest {

    @Test
    public void getNonVIPElectricTicketGenerator() {
        Driver driver = new Driver("Robert", false);
        Vehicle vehicle = new Car(6, driver, "red", 2000, true);
        TicketGeneratorCreator ticketGeneratorCreator = new TicketGeneratorCreator();

        TicketGenerator ticketGenerator = ticketGeneratorCreator.getTicketGenerator(vehicle);

        assertEquals(ElectricTicketGenerator.class, ticketGenerator.getClass());
    }


    @Test
    public void getNonVIPRegularTicketGenerator() {
        Driver driver = new Driver("Robert", false);
        Vehicle vehicle = new Car(6, driver, "red", 2000, false);
        TicketGeneratorCreator ticketGeneratorCreator = new TicketGeneratorCreator();

        TicketGenerator ticketGenerator = ticketGeneratorCreator.getTicketGenerator(vehicle);

        assertEquals(RegularTicketGenerator.class, ticketGenerator.getClass());
    }


    @Test
    public void getVIPElectricTicketGenerator() {
        Driver driver = new Driver("Robert", true);
        Vehicle vehicle = new Car(6, driver, "red", 2000, true);
        TicketGeneratorCreator ticketGeneratorCreator = new TicketGeneratorCreator();

        TicketGenerator ticketGenerator = ticketGeneratorCreator.getTicketGenerator(vehicle);

        assertEquals(VIPElectricTicketGenerator.class, ticketGenerator.getClass());
    }


    @Test
    public void getVIPRegularTicketGenerator() {
        Driver driver = new Driver("Robert", true);
        Vehicle vehicle = new Car(6, driver, "red", 2000, false);
        TicketGeneratorCreator ticketGeneratorCreator = new TicketGeneratorCreator();

        TicketGenerator ticketGenerator = ticketGeneratorCreator.getTicketGenerator(vehicle);

        assertEquals(VIPRegularTicketGenerator.class, ticketGenerator.getClass());
    }
}
