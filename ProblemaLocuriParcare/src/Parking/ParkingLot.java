package Parking;

import Parking.Driver;
import Strategy.*;
import Vehicles.VehicleType;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ParkingLot
{
    private ArrayList<Integer> dimensions;
    private ArrayList<Integer> nrEmptySpots;
    private ArrayList<ParkingSpot>[] parkingSpots;

    private HashMap<Integer, Driver> drivers;

    private TicketGenerator strategy;

    public ParkingLot()
    {
        int nrVehicleTypes = VehicleType.values().length;
        dimensions = new ArrayList<Integer>(nrVehicleTypes);
        nrEmptySpots = new ArrayList<Integer>(nrVehicleTypes);
        parkingSpots = new ArrayList[nrVehicleTypes];

        try
        {
            readInputFromFile();
        }
        catch(Exception ex)
        {
            System.out.println(ex.toString());
        }

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
                strategy = new VIPElectricTicketGenerator();
            else
                strategy = new VIPRegularTicketGenerator();
        }
        else
        {
            if(isElectric)
                strategy = new ElectricTicketGenerator();
            else
                strategy = new RegularTicketGenerator();
        }
    }

    public void readInputFromFile()
    {
        try
        {
            File file = new File("C:\\Users\\robertcur\\Desktop\\IntelliJ - Projects\\Parking-Lot-Problem\\ProblemaLocuriParcare\\input.txt");
            Scanner fileReader = new Scanner(file);

            // If the file contains the dimensions (first line)
            if(fileReader.hasNextLine())
            {
                String line = fileReader.nextLine();
                String[] aux = line.split(" ");

                int nrVehicleTypes = VehicleType.values().length;
                if(nrVehicleTypes != aux.length)
                {
                    System.out.println("Nr de valori de pe prima linie a fisierului nu corespunde cu numarul de tipuri de vehicule!");
                    System.exit(1);
                }

                // Add dimensions
                for(int i = 0; i < aux.length; ++i)
                {
                    dimensions.add(Integer.parseInt(aux[i]));
                }

                // Initially, all spots are empty
                for(int i = 0; i < dimensions.size(); ++i)
                {
                    nrEmptySpots.add(dimensions.get(i));
                    parkingSpots[i] = new ArrayList<ParkingSpot>(dimensions.get(i));
                }

                try
                {
                    int k = 0;
                    int j = 0;

                    while(k <= ParkingSpotType.Large.ordinal())
                    {
                        for(int i = 0; i < dimensions.get(k); ++i)
                        {
                            line = fileReader.nextLine();
                            if(line.equals("electric"))
                                parkingSpots[k].add(new ParkingSpot(j, true, true));
                            else if(line.equals("nonelectric"))
                                parkingSpots[k].add(new ParkingSpot(j, true, false));
                            else
                            {
                                System.out.println("Incorrect input in file! (electric/nonelectric)");
                                System.out.println(line);
                                System.exit(2);
                            }

                            ++j;
                        }

                        ++k;
                    }

                }
                catch(Exception e)
                {
                    System.out.println("Not enough input lines in file!");
                    e.printStackTrace();
                    System.exit(3);
                }

            }
        }
        catch (Exception e)
        {
            System.out.println("An error occured while opening the file!");
            e.printStackTrace();
            System.exit(4);
        }
    }

    public String getParkingTicket(Driver d)
    {
        String ans = "-";
        int vehicleTypeId = d.getVehicle().getType();

        setStrategy(d);

        // In urma apelului, vehicleTypeId nu mai este neaparat acelasi. Avand in vedere ca un sofer poate fi VIP, s-ar putea asigna un loc de parcare de la o categorie superioada
        // Trebuie sa folosim valoarea noua (care se actualizeaza in functia getTicket) si aici pt a actualiza nr de locuri libere.
        ArrayList<Integer> result = strategy.getTicket(parkingSpots, vehicleTypeId);
        int idSpot = result.get(0);
        vehicleTypeId = result.get(1);

        // S-a gasit un loc liber la categoria corespunzatoare masinii
        if(idSpot != -1)
        {
            // Asignam locul de parcare unui sofer
            drivers.put(idSpot, d);

            // Scadem nr de locuri libere disponibile
            int currentlyEmptySpots = nrEmptySpots.get(vehicleTypeId);
            nrEmptySpots.set(vehicleTypeId, currentlyEmptySpots - 1);

            ans = "The driver " + d.toString() + " received the following parking slot: " + idSpot;
        }
        else
        {
            ans = "The driver " + d.toString() + " does not have an empty parking spot!";
        }

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
