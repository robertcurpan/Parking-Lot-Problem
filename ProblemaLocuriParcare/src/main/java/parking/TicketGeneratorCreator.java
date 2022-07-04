package parking;

import strategy.*;
import vehicles.Vehicle;

public class TicketGeneratorCreator {

    public TicketGenerator getTicketGenerator(Vehicle vehicle) {
        // In functie de statutul soferului si de tipul masinii, stabilim strategia potrivita
        boolean isVip = vehicle.getDriver().getVipStatus();
        boolean isElectric = vehicle.getElectric();

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
