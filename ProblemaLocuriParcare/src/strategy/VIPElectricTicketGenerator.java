package strategy;

import exceptions.ParkingSpotNotFoundException;
import parking.Driver;
import parking.ParkingLot;
import parking.ParkingSpot;
import parking.ParkingSpotType;
import structures.ParkingSpotIdAndVehicleTypeId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VIPElectricTicketGenerator implements TicketGenerator
{
    @Override
    public ParkingSpotIdAndVehicleTypeId getTicket(ParkingLot parkingLot, Driver d, int vehicleTypeId) throws ParkingSpotNotFoundException
    {
        int idSpot = -1;
        while(vehicleTypeId <= ParkingSpotType.Large.ordinal())
        {
            idSpot = findEmptyElectricSpotOnCurrentCategory(parkingLot, d, vehicleTypeId);

            if(idSpot != -1)
                return new ParkingSpotIdAndVehicleTypeId(idSpot, vehicleTypeId);

            ++vehicleTypeId;
        }

        // Daca nu am iesit cu "return" din while, inseamna ca nu am gasit un loc de parcare.
        throw new ParkingSpotNotFoundException();
    }

    public int findEmptyElectricSpotOnCurrentCategory(ParkingLot parkingLot, Driver d, int vehicleTypeId)
    {
        int n = parkingLot.getParkingSpots()[vehicleTypeId].size();
        int spot = -1;

        for(int i = 0; i < n; ++i)
        {
            if(parkingLot.getParkingSpots()[vehicleTypeId].get(i).isFree() && parkingLot.getParkingSpots()[vehicleTypeId].get(i).hasElectricCharger())
            {
                synchronized (parkingLot.getParkingSpots()[vehicleTypeId].get(i))
                {
                    if(parkingLot.getParkingSpots()[vehicleTypeId].get(i).isFree())
                    {
                        // S-a ocupat locul de parcare
                        parkingLot.getParkingSpots()[vehicleTypeId].get(i).setFree(false);

                        // Salvez id-ul locului de parcare
                        spot = parkingLot.getParkingSpots()[vehicleTypeId].get(i).getId();

                        // Asignam locul de parcare unui sofer
                        parkingLot.assignParkingSpotToDriver(spot, d);

                        // Scadem nr de locuri libere disponibile
                        parkingLot.decrementEmptySpotsNumberForVehicleType(vehicleTypeId);

                        // Daca am gasit un loc, opresc cautarea
                        break;
                    }
                }
            }
        }

        return spot;
    }
}
