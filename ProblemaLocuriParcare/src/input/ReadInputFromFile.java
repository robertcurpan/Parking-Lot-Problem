package input;


import exceptions.IncorrectFileInputsException;
import parking.Driver;
import parking.ParkingLot;
import parking.ParkingSpot;
import structures.FileInputs;
import vehicles.VehicleType;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ReadInputFromFile {
    public FileInputs readInputFromFile() throws FileNotFoundException, IncorrectFileInputsException {
        FileInputs fileInputs = new FileInputs();

        File file = new File("input.txt");
        Scanner fileReader = new Scanner(file);

        // If the file contains the dimensions (first line)
        if (fileReader.hasNextLine()) {
            String line = fileReader.nextLine();
            String[] noOfParkingSpotsForEachVehicleType = line.split(" ");

            int noVehicleTypes = VehicleType.values().length;
            if (noVehicleTypes != noOfParkingSpotsForEachVehicleType.length) {
                throw new IncorrectFileInputsException("Nr de valori de pe prima linie a fisierului nu corespunde cu numarul de tipuri de vehicule!");
            }

            for (String noOfParkingSpots : noOfParkingSpotsForEachVehicleType) {
                fileInputs.addLotDimension(Integer.parseInt(noOfParkingSpots));
            }

            try {
                for (VehicleType vehicleType : VehicleType.values()) {
                    int noOfParkingSpots = Integer.parseInt(noOfParkingSpotsForEachVehicleType[vehicleType.ordinal()]);
                    for (int index = 0; index < noOfParkingSpots; ++index) {
                        line = fileReader.nextLine();
                        if (line.equals("electric") || line.equals("nonelectric"))
                            fileInputs.addLotInput(line);
                        else {
                            throw new IncorrectFileInputsException("Incorrect input in file! (electric/nonelectric)" + " -> [" + line + "]");
                        }
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
                throw new IncorrectFileInputsException("Not enough input lines in file!");
            }
        } else {
            throw new IncorrectFileInputsException("File is empty!");
        }

        return fileInputs;
    }

    public ParkingLot initializeAndGetParkingLot() {
        Map<VehicleType, Integer> noOfExistingSpotsForVehicleType = new HashMap<VehicleType, Integer>();
        Map<VehicleType, Integer> noOfEmptySpots = new HashMap<VehicleType, Integer>();
        Map<VehicleType, ArrayList<ParkingSpot>> parkingSpots = new HashMap<VehicleType, ArrayList<ParkingSpot>>();
        Map<Integer, Driver> assignedParkingSpots = new HashMap<Integer, Driver>();

        // Read file inputs
        FileInputs fileInputs;

        try {
            fileInputs = readInputFromFile();
        } catch (FileNotFoundException | IncorrectFileInputsException e) {
            throw new RuntimeException(e);
        }

        // Add dimensions
        for (VehicleType vehicleType : VehicleType.values()) {
            noOfExistingSpotsForVehicleType.put(vehicleType, fileInputs.getParkingLotDimensions().get(vehicleType.ordinal()));
        }

        // Initially, all spots are empty
        for (VehicleType vehicleType : VehicleType.values()) {
            noOfEmptySpots.put(vehicleType, noOfExistingSpotsForVehicleType.get(vehicleType));
            parkingSpots.put(vehicleType, new ArrayList<ParkingSpot>(noOfExistingSpotsForVehicleType.get(vehicleType)));
        }

        // Add the actual parking spots to the collection
        int index = 0;
        ArrayList<String> inputLines = fileInputs.getParkingLotInputLines();
        for (VehicleType vehicleType : VehicleType.values()) {
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

// TODO id-ul locului de parcare ar trebui dat in fisier (nu generat automat cu variabila "index" in a doua functie)