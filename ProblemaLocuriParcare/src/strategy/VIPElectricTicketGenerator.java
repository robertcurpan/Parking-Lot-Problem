package strategy;

import exceptions.ParkingSpotNotFoundException;
import parking.Driver;
import parking.ParkingLot;
import structures.ParkingSpotIdAndVehicleTypeId;
import vehicles.VehicleType;

public class VIPElectricTicketGenerator implements TicketGenerator
{
    @Override
    public ParkingSpotIdAndVehicleTypeId getTicket(ParkingLot parkingLot, Driver d, VehicleType vehicleType) throws ParkingSpotNotFoundException
    {
        int idSpot;
        int vehicleTypeId = vehicleType.ordinal();
        while(vehicleTypeId < VehicleType.values().length)
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
        VehicleType vehicleType = VehicleType.values()[vehicleTypeId];

        int n = parkingLot.getParkingSpots().get(vehicleType).size();
        int spot = -1;

        for(int i = 0; i < n; ++i)
        {
            if(parkingLot.getParkingSpots().get(vehicleType).get(i).isFree() && parkingLot.getParkingSpots().get(vehicleType).get(i).hasElectricCharger())
            {
                synchronized (parkingLot.getParkingSpots().get(vehicleType).get(i))
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

        return spot;
    }
}
