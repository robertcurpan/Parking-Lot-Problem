import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;

public class ParkingLot
{
    private ArrayList<Integer> dimensions;
    private ArrayList<Integer> nrEmptySpots;
    private ArrayList<ParkingSpot> parkingSpots;

    private HashMap<Integer, Driver> drivers;

    // Constants
    public static final int SMALL_SPOT_ID = 0;
    public static final int MEDIUM_SPOT_ID = 1;
    public static final int LARGE_SPOT_ID = 2;

    public ParkingLot()
    {


        dimensions = new ArrayList<Integer>(3);
        nrEmptySpots = new ArrayList<Integer>(3);

        readInputFromFile();

        drivers = new HashMap<Integer, Driver>();
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

                int smallDimension = Integer.parseInt(aux[0]);
                int mediumDimension = Integer.parseInt(aux[1]);
                int largeDimension = Integer.parseInt(aux[2]);

                // Set dimensions
                dimensions.add(smallDimension);
                dimensions.add(mediumDimension);
                dimensions.add(largeDimension);

                // Initially, all spots are empty
                for(int i = 0; i < dimensions.size(); ++i)
                    nrEmptySpots.add(dimensions.get(i));

                parkingSpots = new ArrayList<ParkingSpot>(dimensions.get(0) + dimensions.get(1) + dimensions.get(2));

                try
                {
                    for(int i = 0; i < dimensions.get(0) + dimensions.get(1) + dimensions.get(2); ++i)
                    {
                        line = fileReader.nextLine();
                        if(line.equals("electric"))
                            parkingSpots.add(new ParkingSpot(i, true, true));
                        else if(line.equals("nonelectric"))
                            parkingSpots.add(new ParkingSpot(i, true, false));
                        else
                        {
                            System.out.println("Incorrect input in file!");
                            System.out.println(line);
                            return;
                        }

                    }
                }
                catch(Exception e)
                {
                    System.out.println("Not enough lines in file!");
                    e.printStackTrace();
                }

            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("An error occured while opening the file!");
            e.printStackTrace();
        }
    }

    public String getParkingTicket(Driver d)
    {
        String ans = "-";

        // Check if the driver is VIP
        boolean isVIP = d.getVipStatus();
        int vehicleTypeId = d.getVehicle().getType();
        boolean isElectric = d.getVehicle().isElectric();


        int idSpot = findEmptySpot(vehicleTypeId, isElectric);

        // S-a gasit un loc liber la categoria corespunzatoare masinii
        if(idSpot != -1)
        {
            // Asignam locul de parcare unui sofer
            drivers.put(idSpot, d);

            ans = "The driver " + d.toString() + " received the following parking slot: " + idSpot;
        }
        else
        {
            // Nu s-a gasit un loc liber la categoria corespunzatoare masinii
            if(isVIP && vehicleTypeId < LARGE_SPOT_ID)
            {
                // Putem cauta un loc la o categorie superioara
                ++vehicleTypeId;
                while(vehicleTypeId <= LARGE_SPOT_ID && idSpot == -1)
                {
                    idSpot = findEmptySpot(vehicleTypeId, isElectric);

                    ++vehicleTypeId;
                }

                if(idSpot == -1) // Nu s-a gasit un loc de parcare
                    ans = "The driver " + d.toString() + " does not have an empty parking spot!";
                else
                {
                    // Asignam locul de parcare unui sofer
                    drivers.put(idSpot, d);

                    ans = "The driver " + d.toString() + " received the following parking slot: " + idSpot;
                }
            }
            else
            {
                ans = "The driver " + d.toString() + " does not have an empty parking spot!";
            }
        }

        return ans;
    }


    int findEmptySpot(int vehicleTypeId, boolean isElectric)
    {
        int n = dimensions.get(vehicleTypeId);
        int spot = -1;
        int startIndex = 0;
        int endIndex = 0;

        for(int i = 1; i <= vehicleTypeId; ++i)
            startIndex += dimensions.get(i - 1);

        endIndex = startIndex + n;

        for(int i = startIndex; i < endIndex ; ++i)
        {
            if(parkingSpots.get(i).isFree())
            {
                if(isElectric) // Masina este electrica
                {
                    if(parkingSpots.get(i).hasElectricCharger()) // Locul de parcare are charger
                    {
                        // S-a ocupat locul de parcare
                        parkingSpots.get(i).setFree(false);

                        // Scadem nr de locuri libere disponibile
                        nrEmptySpots.set(vehicleTypeId, nrEmptySpots.get(vehicleTypeId) - 1);

                        // Salvam id-ul locului de parcare
                        spot = i;

                        break;
                    }
                }
                else // Masina nu este electrica
                {
                    if(!parkingSpots.get(i).hasElectricCharger()) // Locul de parcare nu are charger
                    {
                        // S-a ocupat locul de parcare
                        parkingSpots.get(i).setFree(false);

                        // Scadem nr de locuri libere disponibile
                        nrEmptySpots.set(vehicleTypeId, nrEmptySpots.get(vehicleTypeId) - 1);

                        // Salvam id-ul locului de parcare
                        spot = i;

                        break;
                    }
                }

            }
        }

        return spot;
    }


    public String leaveParkingLot(Integer idParkingSpot)
    {
        if(!drivers.containsKey(idParkingSpot))
            return "Locul cu id-ul " + idParkingSpot.toString() + " nu este ocupat!";

        // S-a eliberat un loc de parcare
        parkingSpots.get(idParkingSpot).setFree(true);

        // Incrementam nr de locuri libere pt categoria specifica
        int currentlyEmptySpots = nrEmptySpots.get(ParkingSpotType.getParkingSpotType(idParkingSpot, dimensions));
        nrEmptySpots.set(ParkingSpotType.getParkingSpotType(idParkingSpot, dimensions), currentlyEmptySpots + 1);

        // Scoatem locul din lista de locuri de parcare asignate soferilor
        Driver dr = drivers.get(idParkingSpot);
        drivers.remove(idParkingSpot);

        return "The driver: " + dr.toString() + " has left the parking lot (he was on spot: " + idParkingSpot + ")";
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

}
