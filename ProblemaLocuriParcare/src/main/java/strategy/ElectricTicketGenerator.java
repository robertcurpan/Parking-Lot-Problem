package strategy;

import exceptions.ParkingSpotNotFoundException;
import parking.Driver;
import parking.ParkingLot;
import parking.ParkingSpot;
import structures.Ticket;
import vehicles.VehicleType;


public class ElectricTicketGenerator implements TicketGenerator
{
    @Override
    public Ticket getTicket(ParkingLot parkingLot, Driver driver) throws ParkingSpotNotFoundException
    {
        for(ParkingSpot parkingSpot : parkingLot.getParkingSpots())
        {
            if(parkingSpot.isFree() && parkingSpot.hasElectricCharger() && parkingSpot.getSpotType() == VehicleType.values()[driver.getVehicle().getType()])
            {
                // Daca intra 2 thread-uri simultan aici, unul din ele va intra in synchronized si ocupa un loc de parcare.
                // Dupa ce iese, intra al doilea si, daca nu mai punem o data if-ul in care verificam daca locul e liber, atunci
                // i se va asigna acelasi loc de parcare (gresit). De aceea mai punem o data if-ul de mai sus.
                synchronized (parkingSpot) // am pus lock pe locul de parcare (un singur thread per loc de parcare -> nu incurc "ceilalti soferi")
                {
                    if(parkingSpot.isFree())
                    {
                        parkingLot.occupyParkingSpot(parkingSpot, driver);

                        // Daca am gasit un loc, opresc cautarea
                        return new Ticket(parkingSpot.getId(), driver.getVehicle());
                    }
                }
            }
        }

        throw new ParkingSpotNotFoundException();
    }
}
