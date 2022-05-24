package strategy;

import exceptions.ParkingSpotNotFoundException;
import parking.Driver;
import parking.ParkingSpot;
import structures.ParkingSpotIdAndVehicleTypeId;

import java.util.ArrayList;
import java.util.HashMap;

public class RegularTicketGenerator implements TicketGenerator
{
    @Override
    public ParkingSpotIdAndVehicleTypeId getTicket(ArrayList<ParkingSpot>[] parkingSpots, ArrayList<Integer> nrEmptySpots, HashMap<Integer, Driver> drivers, Driver d, int vehicleTypeId) throws ParkingSpotNotFoundException
    {
        int n = parkingSpots[vehicleTypeId].size();
        int spot = -1;

        for(int i = 0; i < n; ++i)
        {
            synchronized (this)
            {
                if(parkingSpots[vehicleTypeId].get(i).isFree() && !parkingSpots[vehicleTypeId].get(i).hasElectricCharger())
                {
                    // S-a ocupat locul de parcare
                    parkingSpots[vehicleTypeId].get(i).setFree(false);

                    // Salvez id-ul locului de parcare
                    spot = parkingSpots[vehicleTypeId].get(i).getId();

                    // Asignam locul de parcare unui sofer
                    drivers.put(spot, d);

                    // Scadem nr de locuri libere disponibile
                    int currentlyEmptySpots = nrEmptySpots.get(vehicleTypeId);
                    nrEmptySpots.set(vehicleTypeId, currentlyEmptySpots - 1);

                    // Daca am gasit un loc, opresc cautarea
                    break;
                }
            }

        }

        if(spot == -1)
            throw new ParkingSpotNotFoundException();

        return new ParkingSpotIdAndVehicleTypeId(spot, vehicleTypeId);
    }
}
