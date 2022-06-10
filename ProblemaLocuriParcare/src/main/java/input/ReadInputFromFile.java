package input;


import exceptions.IncorrectFileInputsException;
import parking.Driver;
import parking.ParkingLot;
import parking.ParkingSpot;
import structures.FileInputs;
import vehicles.VehicleType;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

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
                        String spotType = line.split(" ")[1];
                        if (spotType.equals("electric") || spotType.equals("nonelectric"))
                            fileInputs.addLotInput(line); // adaugam si id-ul si tipul
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
        ArrayList<ParkingSpot> parkingSpots = new ArrayList<ParkingSpot>();
        Map<Integer, Driver> assignedParkingSpots = new HashMap<Integer, Driver>();

        // Read file inputs
        FileInputs fileInputs;

        try {
            fileInputs = readInputFromFile();
        } catch (FileNotFoundException | IncorrectFileInputsException e) {
            throw new RuntimeException(e);
        }

        int noOfParkingSpots = 0;
        // Add dimensions
        for (VehicleType vehicleType : VehicleType.values()) {
            noOfExistingSpotsForVehicleType.put(vehicleType, fileInputs.getParkingLotDimensions().get(vehicleType.ordinal()));
            noOfParkingSpots += fileInputs.getParkingLotDimensions().get(vehicleType.ordinal());
        }

        // Initially, all spots are empty
        for (VehicleType vehicleType : VehicleType.values()) {
            noOfEmptySpots.put(vehicleType, noOfExistingSpotsForVehicleType.get(vehicleType));
        }

        parkingSpots = new ArrayList<ParkingSpot>(noOfParkingSpots);

        // Add the actual parking spots to the collection
        ArrayList<String> inputLines = fileInputs.getParkingLotInputLines();
        for(String line : inputLines) {
            VehicleType vehicleType = VehicleType.MOTORCYCLE;
            int spotId = Integer.parseInt(line.split(" ")[0]);
            String spotType = line.split(" ")[1];
            String spotSize = line.split(" ")[2];

            if(Objects.equals(spotSize, "motorcycle")) {
                vehicleType = VehicleType.MOTORCYCLE;
            } else if(Objects.equals(spotSize, "car")) {
                vehicleType = VehicleType.CAR;
            } else if(Objects.equals(spotSize, "truck")) {
                vehicleType = VehicleType.TRUCK;
            } else {
                throw new RuntimeException("Parking Spot vehicle type incorrect!");
            }

            boolean electric = spotType.equals("electric");
            parkingSpots.add(new ParkingSpot(spotId, vehicleType, true, electric));
        }

        return new ParkingLot(noOfExistingSpotsForVehicleType, noOfEmptySpots, parkingSpots, assignedParkingSpots);
    }

}