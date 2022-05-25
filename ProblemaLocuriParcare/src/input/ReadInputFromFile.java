package input;


import parking.ParkingLot;
import parking.ParkingSpot;
import parking.ParkingSpotType;
import structures.FileInputs;
import vehicles.VehicleType;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
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

            for(int i = 0; i < aux.length; ++i)
                fileInputs.addLotDimension(Integer.parseInt(aux[i]));

            try
            {
                int k = 0;

                while(k < ParkingSpotType.values().length)
                {
                    int currentDimension = Integer.parseInt(aux[k]);
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
        else
        {
            System.out.println("Not enough input lines in file!");
            System.exit(3);
        }

        return fileInputs;
    }

    public void setVariablesInParkingLot(ParkingLot parkingLot)
    {
        // Read file inputs
        FileInputs fileInputs;
        try
        {
            fileInputs = readInputFromFile();
        }
        catch (FileNotFoundException e)
        {
            throw new RuntimeException(e);
        }

        // Add dimensions
        for(int index = 0; index < fileInputs.getParkingLotDimensions().size(); ++index)
        {
            parkingLot.addNoOfExistingSpotsForVehicleType(fileInputs.getParkingLotDimensions().get(index));
        }

        // Initially, all spots are empty
        for(int index = 0; index < VehicleType.values().length; ++index)
        {
            parkingLot.addNoOfEmptySpots(parkingLot.getNoOfExistingSpotsForVehicleType().get(index));
            parkingLot.getParkingSpots()[index] = new ArrayList<ParkingSpot>(parkingLot.getNoOfExistingSpotsForVehicleType().get(index));
        }

        // Add the actual parking spots to the collection
        int index = 0;
        ArrayList<String> inputLines = fileInputs.getParkingLotInputLines();
        for (ParkingSpotType type :ParkingSpotType.values()) {
            while (parkingLot.getParkingSpots()[type.ordinal()].size() < parkingLot.getNoOfExistingSpotsForVehicleType().get(type.ordinal())) {
                String line = inputLines.get(index);
                boolean electric = line.equals("electric");
                parkingLot.getParkingSpots()[type.ordinal()].add(new ParkingSpot(index, true, electric));
                ++index;
            }
        }

    }

}
