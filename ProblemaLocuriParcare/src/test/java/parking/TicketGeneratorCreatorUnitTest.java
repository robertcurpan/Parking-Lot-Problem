package parking;

import org.junit.jupiter.api.Test;
import strategy.*;
import vehicles.Car;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TicketGeneratorCreatorUnitTest {

    @Test
    public void getNonVIPElectricTicketGenerator() {
        Driver driver = new Driver("Andrei", new Car("red", 2000, true), false);
        TicketGeneratorCreator ticketGeneratorCreator = new TicketGeneratorCreator();

        TicketGenerator ticketGenerator = ticketGeneratorCreator.getTicketGenerator(driver);

        assertEquals(ElectricTicketGenerator.class, ticketGenerator.getClass());
    }


    @Test
    public void getNonVIPRegularTicketGenerator() {
        Driver driver = new Driver("Andrei", new Car("red", 2000, false), false);
        TicketGeneratorCreator ticketGeneratorCreator = new TicketGeneratorCreator();

        TicketGenerator ticketGenerator = ticketGeneratorCreator.getTicketGenerator(driver);

        assertEquals(RegularTicketGenerator.class, ticketGenerator.getClass());
    }


    @Test
    public void getVIPElectricTicketGenerator() {
        Driver driver = new Driver("Andrei", new Car("red", 2000, true), true);
        TicketGeneratorCreator ticketGeneratorCreator = new TicketGeneratorCreator();

        TicketGenerator ticketGenerator = ticketGeneratorCreator.getTicketGenerator(driver);

        assertEquals(VIPElectricTicketGenerator.class, ticketGenerator.getClass());
    }


    @Test
    public void getVIPRegularTicketGenerator() {
        Driver driver = new Driver("Andrei", new Car("red", 2000, false), true);
        TicketGeneratorCreator ticketGeneratorCreator = new TicketGeneratorCreator();

        TicketGenerator ticketGenerator = ticketGeneratorCreator.getTicketGenerator(driver);

        assertEquals(VIPRegularTicketGenerator.class, ticketGenerator.getClass());
    }
}
