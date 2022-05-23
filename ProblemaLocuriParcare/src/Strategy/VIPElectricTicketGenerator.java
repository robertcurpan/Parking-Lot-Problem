package Strategy;

import Parking.ParkingSpot;
import Parking.ParkingSpotType;
import Vehicles.VehicleType;

import java.sql.Driver;
import java.util.ArrayList;

public class VIPElectricTicketGenerator implements TicketGenerator
{
    @Override
    public ArrayList<Integer> getTicket(ArrayList<ParkingSpot>[] parkingSpots, int vehicleTypeId)
    {
        ArrayList<Integer> spotIdAndVehicleTypeId = new ArrayList<Integer>();
        int idSpot;
        while(vehicleTypeId <= ParkingSpotType.Large.ordinal())
        {
            idSpot = findEmptyElectricSpotOnCurrentCategory(parkingSpots, vehicleTypeId);

            if(idSpot != -1)
            {
                spotIdAndVehicleTypeId.add(idSpot);
                spotIdAndVehicleTypeId.add(vehicleTypeId);
                return spotIdAndVehicleTypeId;
            }

            ++vehicleTypeId;
        }

        // Daca idSpot = -1 inseamna ca nu s-a gasit loc de parcare la categoria curenta. Cautam la categoriile superioare
        spotIdAndVehicleTypeId.add(-1);
        spotIdAndVehicleTypeId.add(-1);
        return spotIdAndVehicleTypeId; // daca am iesit din while fara return, inseamna ca nu am gasit un loc de parcare
    }

    public int findEmptyElectricSpotOnCurrentCategory(ArrayList<ParkingSpot>[] parkingSpots, int vehicleTypeId)
    {
        int n = parkingSpots[vehicleTypeId].size();
        int spot = -1;

        for(int i = 0; i < n; ++i)
        {
            if(parkingSpots[vehicleTypeId].get(i).isFree() && parkingSpots[vehicleTypeId].get(i).hasElectricCharger())
            {
                // S-a ocupat locul de parcare
                parkingSpots[vehicleTypeId].get(i).setFree(false);

                // Salvez id-ul locului de parcare
                spot = parkingSpots[vehicleTypeId].get(i).getId();

                // Daca am gasit un loc, opresc cautarea
                break;
            }
        }

        return spot;
    }

}
