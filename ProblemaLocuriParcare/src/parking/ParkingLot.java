package parking;

import exceptions.ParkingSpotNotFoundException;
import strategy.*;
import structures.FileInputs;
import structures.ParkingSpotIdAndVehicleTypeId;
import vehicles.VehicleType;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ParkingLot
{
    private ArrayList<Integer> dimensions;
    private ArrayList<Integer> nrEmptySpots;
    private ArrayList<ParkingSpot>[] parkingSpots;

    private HashMap<Integer, Driver> drivers;

    private TicketGenerator ticketGenerator;

    public ParkingLot()
    {
        int nrVehicleTypes = VehicleType.values().length;
        dimensions = new ArrayList<Integer>(nrVehicleTypes);
        nrEmptySpots = new ArrayList<Integer>(nrVehicleTypes);
        parkingSpots = new ArrayList[nrVehicleTypes];
        drivers = new HashMap<Integer, Driver>();
    }

    public void setStrategy(Driver d)
    {
        // In functie de statutul soferului si de tipul masinii, stabilim strategia potrivita
        boolean isVip = d.getVipStatus();
        boolean isElectric = d.getVehicle().isElectric();

        if(isVip)
        {
            if(isElectric)
                ticketGenerator = new VIPElectricTicketGenerator();
            else
                ticketGenerator = new VIPRegularTicketGenerator();
        }
        else
        {
            if(isElectric)
                ticketGenerator = new ElectricTicketGenerator();
            else
                ticketGenerator = new RegularTicketGenerator();
        }
    }

    public void setVariablesFromFileInputs(FileInputs fileInputs)
    {
        // Add dimensions
        for(int i = 0; i < fileInputs.getParkingLotDimensions().size(); ++i)
            dimensions.add(fileInputs.getParkingLotDimensions().get(i));

        // Initially, all spots are empty
        for(int i = 0; i < dimensions.size(); ++i)
        {
            nrEmptySpots.add(dimensions.get(i));
            parkingSpots[i] = new ArrayList<ParkingSpot>(dimensions.get(i));
        }


        int k = 0;
        int j = 0;
        int i = 0;
        ArrayList<String> inputLines = fileInputs.getParkingLotInputLines();
        while(k <= ParkingSpotType.Large.ordinal())
        {
            while(i < dimensions.get(k))
            {
                String line = inputLines.get(j);
                if(line.equals("electric"))
                    parkingSpots[k].add(new ParkingSpot(j, true, true));
                else if(line.equals("nonelectric"))
                    parkingSpots[k].add(new ParkingSpot(j, true, false));

                ++i;
                ++j;
            }

            i = 0;
            ++k;
        }

    }

    public String getParkingTicket(Driver d)
    {
        String ans = "-";
        int vehicleTypeId = d.getVehicle().getType();
        int idSpot = -1;

        setStrategy(d);

        // In urma apelului, vehicleTypeId nu mai este neaparat acelasi. Avand in vedere ca un sofer poate fi VIP, s-ar putea asigna un loc de parcare de la o categorie superioada
        // Trebuie sa folosim valoarea noua (care se actualizeaza in functia getTicket) si aici pt a actualiza nr de locuri libere.
        try
        {
            ParkingSpotIdAndVehicleTypeId parkingSpotIdAndVehicleTypeId = ticketGenerator.getTicket(parkingSpots, nrEmptySpots, drivers, d, vehicleTypeId);
            idSpot = parkingSpotIdAndVehicleTypeId.getParkingSpotId();
            vehicleTypeId = parkingSpotIdAndVehicleTypeId.getVehicleTypeId();
        }
        catch(ParkingSpotNotFoundException ex)
        {
            return ex.toString();
        }

        // S-a gasit un loc liber la categoria corespunzatoare masinii

        ans = "The driver " + d.toString() + " received the following parking slot: " + idSpot;

        return ans;
    }


    public String leaveParkingLot(Integer idParkingSpot)
    {
        if(!drivers.containsKey(idParkingSpot))
            return "The spot with id: " + idParkingSpot.toString() + " is not occupied!";

        // Eliberam locul de parcare
        freeEmptySpot(idParkingSpot);

        // Scoatem locul din lista de locuri de parcare asignate soferilor
        Driver dr = drivers.get(idParkingSpot);
        drivers.remove(idParkingSpot);

        return "The driver: " + dr.toString() + " has left the parking lot (he was on spot: " + idParkingSpot + ")";
    }

    public void freeEmptySpot(Integer idParkingSpot)
    {
        ArrayList<Integer> typeAndIndex = new ArrayList<Integer>();

        int sum = 0;
        int vehicleTypeId = 0;
        while(sum + dimensions.get(vehicleTypeId) <= idParkingSpot)
        {
            sum += dimensions.get(vehicleTypeId);
            ++vehicleTypeId;
        }

        int index = idParkingSpot - sum;

        typeAndIndex.add(vehicleTypeId);
        typeAndIndex.add(index);

        // S-a eliberat un loc de parcare
        parkingSpots[vehicleTypeId].get(index).setFree(true);

        // Incrementam nr de locuri libere pt categoria specificata
        int currentlyEmptySpots = nrEmptySpots.get(vehicleTypeId);
        nrEmptySpots.set(vehicleTypeId, currentlyEmptySpots + 1);
    }


    public String showOccupiedSpots()
    {
        StringBuilder ans = new StringBuilder();
        ans.append("\r\n");
        for(HashMap.Entry<Integer, Driver> entry : drivers.entrySet())
        {
            ans.append(entry.getValue().toString()).append(" -> parking slot: ").append(entry.getKey().toString()).append("\r\n");
        }

        ans.append("\r\n");
        ans.append("-----> Number of free parking spots left: \r\n");
        ans.append("Motorcycle: ").append(nrEmptySpots.get(0)).append(" free spots.\r\n");
        ans.append("Car: ").append(nrEmptySpots.get(1)).append(" free spots.\r\n");
        ans.append("Truck: ").append(nrEmptySpots.get(2)).append(" free spots.\r\n");

        return ans.toString();
    }

    public String showAllParkingSpots()
    {
        StringBuilder ans = new StringBuilder();
        ans.append("\r\n");
        for(int k = 0; k < dimensions.size(); ++k)
        {
            for(int i = 0 ; i < parkingSpots[k].size(); ++i)
            {
                ans.append(parkingSpots[k].get(i).getId()).append(" -> eletric: ").append(parkingSpots[k].get(i).hasElectricCharger()).append("\r\n");
            }
        }

        ans.append("\r\n");

        return ans.toString();
    }

}
