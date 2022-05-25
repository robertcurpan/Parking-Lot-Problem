package strategy;

import exceptions.ParkingSpotNotFoundException;
import parking.Driver;
import parking.ParkingLot;
import parking.ParkingSpot;
import structures.ParkingSpotIdAndVehicleTypeId;
import vehicles.VehicleType;

import java.util.List;
import java.util.Map;


public class ElectricTicketGenerator implements TicketGenerator
{
    @Override
    public ParkingSpotIdAndVehicleTypeId getTicket(ParkingLot parkingLot, Driver d, VehicleType vehicleType) throws ParkingSpotNotFoundException
    {
        int n = parkingLot.getParkingSpots().get(vehicleType).size();
        int spot = -1;

        for(int i = 0; i < n; ++i)
        {
            if(parkingLot.getParkingSpots().get(vehicleType).get(i).isFree() && parkingLot.getParkingSpots().get(vehicleType).get(i).hasElectricCharger())
            {
                // Daca intra 2 thread-uri simultan aici, unul din ele va intra in synchronized si ocupa un loc de parcare.
                // Dupa ce iese, intra al doilea si, daca nu mai punem o data if-ul in care verificam daca locul e liber, atunci
                // i se va asigna acelasi loc de parcare (gresit). De aceea mai punem o data if-ul de mai sus.
                synchronized (parkingLot.getParkingSpots().get(vehicleType).get(i)) // am pus lock pe locul de parcare (un singur thread per loc de parcare -> nu incurc "ceilalti soferi")
                {
                    if(parkingLot.getParkingSpots().get(vehicleType).get(i).isFree())
                    {
                        // S-a ocupat locul de parcare
                        parkingLot.getParkingSpots().get(vehicleType).get(i).setFree(false);

                        // Salvez id-ul locului de parcare
                        spot = parkingLot.getParkingSpots().get(vehicleType).get(i).getId();

                        // Asignam locul de parcare unui sofer
                        parkingLot.assignParkingSpotToDriver(spot, d);

                        // Scadem nr de locuri libere disponibile
                        parkingLot.decrementEmptySpotsNumberForVehicleType(vehicleType);

                        // Daca am gasit un loc, opresc cautarea
                        break;
                    }
                }
            }
        }



        if(spot == -1)
            throw new ParkingSpotNotFoundException();

        return new ParkingSpotIdAndVehicleTypeId(spot, vehicleType.ordinal());
    }
}
