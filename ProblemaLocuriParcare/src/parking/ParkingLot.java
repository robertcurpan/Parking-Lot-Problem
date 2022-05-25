package parking;

import exceptions.ParkingSpotNotFoundException;
import strategy.*;
import structures.FileInputs;
import structures.ParkingSpotIdAndVehicleTypeId;
import vehicles.VehicleType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParkingLot {
    private List<Integer> noOfExistingSpotsForVehicleType;
    private List<Integer> emptySpotsNumber;
    private List<ParkingSpot>[] parkingSpots;

    private Map<Integer, Driver> assignedParkingSpots;

    private TicketGenerator ticketGenerator;

    public ParkingLot() {
        int noOfVehicleTypes = VehicleType.values().length;
        noOfExistingSpotsForVehicleType = new ArrayList<Integer>(noOfVehicleTypes);
        emptySpotsNumber = new ArrayList<Integer>(noOfVehicleTypes);
        parkingSpots = new ArrayList[noOfVehicleTypes];
        assignedParkingSpots = new HashMap<Integer, Driver>();
    }

    public TicketGenerator getTicketGenerator(Driver driver) {
        // In functie de statutul soferului si de tipul masinii, stabilim strategia potrivita
        boolean isVip = driver.getVipStatus();
        boolean isElectric = driver.getVehicle().isElectric();

        if (isVip && isElectric) {
            return new VIPElectricTicketGenerator();
        }
        if (isVip && !isElectric) {
            return new VIPRegularTicketGenerator();
        }
        if (!isVip && isElectric) {
            return new ElectricTicketGenerator();
        }
        if (!isVip && !isElectric) {
            return new RegularTicketGenerator();
        }
        throw new IllegalStateException();
    }

    public void setVariablesFromFileInputs(FileInputs fileInputs) {
        // Add dimensions
        noOfExistingSpotsForVehicleType.addAll(fileInputs.getParkingLotDimensions());


        // Initially, all spots are empty
        for (int i = 0; i < noOfExistingSpotsForVehicleType.size(); ++i) {
            emptySpotsNumber.add(noOfExistingSpotsForVehicleType.get(i));
            parkingSpots[i] = new ArrayList<ParkingSpot>(noOfExistingSpotsForVehicleType.get(i));
        }


        int index = 0;
        ArrayList<String> inputLines = fileInputs.getParkingLotInputLines();
        for (ParkingSpotType type :ParkingSpotType.values()) {
            while (parkingSpots[type.ordinal()].size() < noOfExistingSpotsForVehicleType.get(type.ordinal())) {
                String line = inputLines.get(index);
                boolean electric = line.equals("electric");
                parkingSpots[type.ordinal()].add(new ParkingSpot(index, true, electric));
                ++index;
            }
        }

    }

    public String getParkingTicket(Driver driver) {
        String ans = "-";
        int vehicleTypeId = driver.getVehicle().getType();
        int idSpot;

        TicketGenerator ticketGenerator = getTicketGenerator(driver);

        // In urma apelului, vehicleTypeId nu mai este neaparat acelasi. Avand in vedere ca un sofer poate fi VIP, s-ar putea asigna un loc de parcare de la o categorie superioada
        // Trebuie sa folosim valoarea noua (care se actualizeaza in functia getTicket) si aici pt a actualiza nr de locuri libere.
        try {
            ParkingSpotIdAndVehicleTypeId parkingSpotIdAndVehicleTypeId = ticketGenerator.getTicket(this, driver, vehicleTypeId);
            idSpot = parkingSpotIdAndVehicleTypeId.getParkingSpotId();
            vehicleTypeId = parkingSpotIdAndVehicleTypeId.getVehicleTypeId();
        } catch (ParkingSpotNotFoundException ex) {
            return ex.toString();
        }

        // S-a gasit un loc liber la categoria corespunzatoare masinii

        ans = "The driver " + driver.toString() + " received the following parking slot: " + idSpot;

        return ans;
    }


    public String leaveParkingLot(Integer idParkingSpot) {
        if (!assignedParkingSpots.containsKey(idParkingSpot))
            return "The spot with id: " + idParkingSpot.toString() + " is not occupied!";

        // Eliberam locul de parcare
        freeEmptySpot(idParkingSpot);

        // Scoatem locul din lista de locuri de parcare asignate soferilor
        Driver dr = assignedParkingSpots.get(idParkingSpot);
        assignedParkingSpots.remove(idParkingSpot);

        return "The driver: " + dr.toString() + " has left the parking lot (he was on spot: " + idParkingSpot + ")";
    }

    public void freeEmptySpot(Integer idParkingSpot) {
        ArrayList<Integer> typeAndIndex = new ArrayList<Integer>();

        int sum = 0;
        int vehicleTypeId = 0;
        while (sum + noOfExistingSpotsForVehicleType.get(vehicleTypeId) <= idParkingSpot) {
            sum += noOfExistingSpotsForVehicleType.get(vehicleTypeId);
            ++vehicleTypeId;
        }

        int index = idParkingSpot - sum;

        typeAndIndex.add(vehicleTypeId);
        typeAndIndex.add(index);

        // S-a eliberat un loc de parcare
        parkingSpots[vehicleTypeId].get(index).setFree(true);

        // Incrementam nr de locuri libere pt categoria specificata
        int currentlyEmptySpots = emptySpotsNumber.get(vehicleTypeId);
        emptySpotsNumber.set(vehicleTypeId, currentlyEmptySpots + 1);
    }


    public String showOccupiedSpots() {
        StringBuilder ans = new StringBuilder();
        ans.append("\r\n");
        for (HashMap.Entry<Integer, Driver> entry : assignedParkingSpots.entrySet()) {
            ans.append(entry.getValue().toString()).append(" -> parking slot: ").append(entry.getKey().toString()).append("\r\n");
        }

        ans.append("\r\n");
        ans.append("-----> Number of free parking spots left: \r\n");
        ans.append("Motorcycle: ").append(emptySpotsNumber.get(0)).append(" free spots.\r\n");
        ans.append("Car: ").append(emptySpotsNumber.get(1)).append(" free spots.\r\n");
        ans.append("Truck: ").append(emptySpotsNumber.get(2)).append(" free spots.\r\n");

        return ans.toString();
    }

    public String showAllParkingSpots() {
        StringBuilder ans = new StringBuilder();
        ans.append("\r\n");
        for (int k = 0; k < noOfExistingSpotsForVehicleType.size(); ++k) {
            for (int i = 0; i < parkingSpots[k].size(); ++i) {
                ans.append(parkingSpots[k].get(i).getId()).append(" -> eletric: ").append(parkingSpots[k].get(i).hasElectricCharger()).append("\r\n");
            }
        }

        ans.append("\r\n");

        return ans.toString();
    }

    public void addNoOfExistingSpotsForVehicleType(int noOfExistingSpots)
    {
        noOfExistingSpotsForVehicleType.add(noOfExistingSpots);
    }

    public List<Integer> getNoOfExistingSpotsForVehicleType() { return noOfExistingSpotsForVehicleType; }

    public void addNoOfEmptySpots(Integer noOfEmptySpots) { emptySpotsNumber.add(noOfEmptySpots); }

    public List<ParkingSpot>[] getParkingSpots() { return parkingSpots; }

    public List<Integer> getEmptySpotsNumber() { return emptySpotsNumber; }

    public void assignParkingSpotToDriver(int parkingSpot, Driver driver)
    {
        assignedParkingSpots.put(parkingSpot, driver);
    }

    public void decrementEmptySpotsNumberForVehicleType(int vehicleTypeId)
    {
        int currentlyEmptySpots = emptySpotsNumber.get(vehicleTypeId);
        emptySpotsNumber.set(vehicleTypeId, currentlyEmptySpots - 1);
    }

}
