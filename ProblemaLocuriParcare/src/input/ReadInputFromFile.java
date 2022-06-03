package input;


import parking.Driver;
import parking.ParkingLot;
import parking.ParkingSpot;
import parking.TicketGeneratorCreator;
import structures.FileInputs;
import vehicles.VehicleType;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ReadInputFromFile
{
    public FileInputs readInputFromFile() throws FileNotFoundException
    {
        FileInputs fileInputs = new FileInputs();

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

            for (String s : aux) {
                fileInputs.addLotDimension(Integer.parseInt(s));
            }

            try
            {
                int k = 0;

                for (VehicleType vehicleType : VehicleType.values())
                {
                    int currentDimension = Integer.parseInt(aux[vehicleType.ordinal()]);
                    for(int index = 0; index < currentDimension; ++index)
                    {
                        line = fileReader.nextLine();
                        if(line.equals("electric") || line.equals("nonelectric"))
                            fileInputs.addLotInput(line);
                        else
                        {
                            System.out.println("Incorrect input in file! (electric/nonelectric)");
                            System.out.println(line);
                            System.exit(2);
                        }
                    }

                }

            }
            catch(Exception e)
            {
                System.out.println("Not enough input lines in file!");
                e.printStackTrace();
                System.exit(3);
            }
        }
        else
        {
            System.out.println("Not enough input lines in file!");
            System.exit(3);
        }

        return fileInputs;
    }

    public ParkingLot initializeAndGetParkingLot()
    {
        Map<VehicleType, Integer> noOfExistingSpotsForVehicleType = new HashMap<VehicleType, Integer>();
        Map<VehicleType, Integer> noOfEmptySpots = new HashMap<VehicleType, Integer>();
        Map<VehicleType, ArrayList<ParkingSpot>> parkingSpots = new HashMap<VehicleType, ArrayList<ParkingSpot>>();
        Map<Integer, Driver> assignedParkingSpots = new HashMap<Integer, Driver>();

        // Read file inputs
        FileInputs fileInputs;

        try {
            fileInputs = readInputFromFile();
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        // Add dimensions
        for(VehicleType vehicleType : VehicleType.values()) {
            noOfExistingSpotsForVehicleType.put(vehicleType, fileInputs.getParkingLotDimensions().get(vehicleType.ordinal()));
        }

        // Initially, all spots are empty
        for(VehicleType vehicleType : VehicleType.values()) {
            noOfEmptySpots.put(vehicleType, noOfExistingSpotsForVehicleType.get(vehicleType));
            parkingSpots.put(vehicleType, new ArrayList<ParkingSpot>(noOfExistingSpotsForVehicleType.get(vehicleType)));
        }

        // Add the actual parking spots to the collection
        int index = 0;
        ArrayList<String> inputLines = fileInputs.getParkingLotInputLines();
        for (VehicleType vehicleType :VehicleType.values()) {
            while (parkingSpots.get(vehicleType).size() < noOfExistingSpotsForVehicleType.get(vehicleType)) {
                String line = inputLines.get(index);
                boolean electric = line.equals("electric");
                parkingSpots.get(vehicleType).add(new ParkingSpot(index, true, electric));
                ++index;
            }
        }

        return new ParkingLot(noOfExistingSpotsForVehicleType, noOfEmptySpots, parkingSpots, assignedParkingSpots);
    }

}
