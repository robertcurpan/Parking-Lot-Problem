package strategy;

import exceptions.ParkingSpotNotFoundException;
import parking.Driver;
import parking.ParkingSpot;
import parking.ParkingSpotType;
import structures.ParkingSpotIdAndVehicleTypeId;

import java.util.ArrayList;
import java.util.HashMap;

public class VIPElectricTicketGenerator implements TicketGenerator
{
    @Override
    public ParkingSpotIdAndVehicleTypeId getTicket(ArrayList<ParkingSpot>[] parkingSpots, ArrayList<Integer> nrEmptySpots, HashMap<Integer, Driver> drivers, Driver d, int vehicleTypeId) throws ParkingSpotNotFoundException
    {
        int idSpot = -1;
        while(vehicleTypeId <= ParkingSpotType.Large.ordinal())
        {
            idSpot = findEmptyElectricSpotOnCurrentCategory(parkingSpots, nrEmptySpots, drivers, d, vehicleTypeId);

            if(idSpot != -1)
                return new ParkingSpotIdAndVehicleTypeId(idSpot, vehicleTypeId);

            ++vehicleTypeId;
        }

        // Daca nu am iesit cu "return" din while, inseamna ca nu am gasit un loc de parcare.
        throw new ParkingSpotNotFoundException();
    }

    public int findEmptyElectricSpotOnCurrentCategory(ArrayList<ParkingSpot>[] parkingSpots, ArrayList<Integer> nrEmptySpots, HashMap<Integer, Driver> drivers, Driver d, int vehicleTypeId)
    {
        int n = parkingSpots[vehicleTypeId].size();
        int spot = -1;

        for(int i = 0; i < n; ++i)
        {
            synchronized (this)
            {
                if(parkingSpots[vehicleTypeId].get(i).isFree() && parkingSpots[vehicleTypeId].get(i).hasElectricCharger())
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

        return spot;
    }

}
