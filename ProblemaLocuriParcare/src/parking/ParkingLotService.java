package parking;

import exceptions.ParkingSpotNotFoundException;
import exceptions.ParkingSpotNotOccupiedException;
import strategy.TicketGenerator;
import structures.Ticket;
import vehicles.VehicleType;

import java.util.ArrayList;
import java.util.Map;

public class ParkingLotService {

    private TicketGeneratorCreator ticketGeneratorCreator;

    public ParkingLotService(TicketGeneratorCreator ticketGeneratorCreator) {
        this.ticketGeneratorCreator = ticketGeneratorCreator;
    }

    public Ticket getParkingTicket(ParkingLot parkingLot, Driver driver) throws ParkingSpotNotFoundException
    {
        TicketGenerator ticketGenerator = ticketGeneratorCreator.getTicketGenerator(driver);

        // In urma apelului, vehicleTypeId nu mai este neaparat acelasi. Avand in vedere ca un sofer poate fi VIP, s-ar putea asigna un loc de parcare de la o categorie superioada
        // Trebuie sa folosim valoarea noua (care se actualizeaza in functia getTicket) si aici pt a actualiza nr de locuri libere.

        Ticket ticket = ticketGenerator.getTicket(parkingLot, driver);

        return ticket;
    }


    public Driver leaveParkingLot(ParkingLot parkingLot, int idParkingSpot) throws ParkingSpotNotOccupiedException
    {
        if (!parkingLot.getAssignedParkingSpots().containsKey(idParkingSpot))
            throw new ParkingSpotNotOccupiedException();

        // Eliberam locul de parcare
        Driver driver = freeEmptySpot(parkingLot, idParkingSpot);

        return driver;
    }

    public Driver freeEmptySpot(ParkingLot parkingLot, int idParkingSpot) {
        int sum = 0;
        VehicleType vehicleType = VehicleType.Motorcycle;
        for(VehicleType vehType : VehicleType.values())
        {
            if(sum + parkingLot.getNoOfExistingSpotsForVehicleType().get(vehType) <= idParkingSpot)
            {
                sum += parkingLot.getNoOfExistingSpotsForVehicleType().get(vehType);
            }
            else
            {
                vehicleType = vehType;
                break;
            }
        }

        int index = idParkingSpot - sum;

        return parkingLot.releaseParkingSpot(idParkingSpot, vehicleType, index);
    }

}
