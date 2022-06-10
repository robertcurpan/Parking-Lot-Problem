package parking;

import strategy.*;

public class TicketGeneratorCreator {

    public TicketGenerator getTicketGenerator(Driver driver) {
        // In functie de statutul soferului si de tipul masinii, stabilim strategia potrivita
        boolean isVip = driver.getVipStatus();
        boolean isElectric = driver.getVehicle().isElectric();

        if (isVip && isElectric) {
            return new VIPElectricTicketGenerator();
        }
        if (isVip && !isElectric) {
            return new VIPRegularTicketGenerator();
        }
        if (!isVip && isElectric) {
            return new ElectricTicketGenerator();
        }
        if (!isVip && !isElectric) {
            return new RegularTicketGenerator();
        }
        throw new IllegalStateException();
    }
}
