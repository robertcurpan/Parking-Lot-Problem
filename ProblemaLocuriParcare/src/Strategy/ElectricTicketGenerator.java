package Strategy;

import Parking.ParkingSpot;
import Vehicles.VehicleType;

import java.sql.Driver;
import java.util.ArrayList;

//TODO De actualizat Empty spots din locul unde apelez strategiile

public class ElectricTicketGenerator implements TicketGenerator
{
    @Override
    public ArrayList<Integer> getTicket(ArrayList<ParkingSpot>[] parkingSpots, int vehicleTypeId)
    {
        ArrayList<Integer> spotIdAndVehicleTypeId = new ArrayList<Integer>();
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

        spotIdAndVehicleTypeId.add(spot);
        spotIdAndVehicleTypeId.add(vehicleTypeId);
        return spotIdAndVehicleTypeId;
    }
}
