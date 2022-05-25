package parking;

import exceptions.ParkingSpotNotFoundException;
import strategy.*;
import structures.ParkingSpotIdAndVehicleTypeId;
import vehicles.VehicleType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ParkingLot {
    private Map<VehicleType, Integer> noOfExistingSpotsForVehicleType;
    private Map<VehicleType, Integer> emptySpotsNumber;
    private Map<VehicleType, ArrayList<ParkingSpot>> parkingSpots;

    private Map<Integer, Driver> assignedParkingSpots;

    private TicketGenerator ticketGenerator;

    public ParkingLot() {
        noOfExistingSpotsForVehicleType = new HashMap<VehicleType, Integer>();
        emptySpotsNumber = new HashMap<VehicleType, Integer>();
        parkingSpots = new HashMap<VehicleType, ArrayList<ParkingSpot>>();
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

    public String getParkingTicket(Driver driver) {
        String ans = "-";
        int vehicleTypeId = driver.getVehicle().getType();
        int idSpot;

        TicketGenerator ticketGenerator = getTicketGenerator(driver);

        // In urma apelului, vehicleTypeId nu mai este neaparat acelasi. Avand in vedere ca un sofer poate fi VIP, s-ar putea asigna un loc de parcare de la o categorie superioada
        // Trebuie sa folosim valoarea noua (care se actualizeaza in functia getTicket) si aici pt a actualiza nr de locuri libere.
        try {
            ParkingSpotIdAndVehicleTypeId parkingSpotIdAndVehicleTypeId = ticketGenerator.getTicket(this, driver, VehicleType.values()[vehicleTypeId]);
            idSpot = parkingSpotIdAndVehicleTypeId.getParkingSpotId();
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
        int sum = 0;
        VehicleType vehicleType = VehicleType.Motorcycle;
        for(VehicleType vehType : VehicleType.values())
        {
            if(sum + noOfExistingSpotsForVehicleType.get(vehType) <= idParkingSpot)
            {
                sum += noOfExistingSpotsForVehicleType.get(vehType);
            }
            else
            {
                vehicleType = vehType;
                break;
            }
        }

        int index = idParkingSpot - sum;

        // S-a eliberat un loc de parcare
        parkingSpots.get(vehicleType).get(index).setFree(true);

        // Incrementam nr de locuri libere pt categoria specificata
        incrementEmptySpotsNumberForVehicleType(vehicleType);
    }


    public String showOccupiedSpots() {
        StringBuilder ans = new StringBuilder();
        ans.append("\r\n");
        for (HashMap.Entry<Integer, Driver> entry : assignedParkingSpots.entrySet()) {
            ans.append(entry.getValue().toString()).append(" -> parking slot: ").append(entry.getKey().toString()).append("\r\n");
        }

        ans.append("\r\n");
        ans.append("-----> Number of free parking spots left: \r\n");
        ans.append("Motorcycle: ").append(emptySpotsNumber.get(VehicleType.Motorcycle)).append(" free spots.\r\n");
        ans.append("Car: ").append(emptySpotsNumber.get(VehicleType.Car)).append(" free spots.\r\n");
        ans.append("Truck: ").append(emptySpotsNumber.get(VehicleType.Truck)).append(" free spots.\r\n");

        return ans.toString();
    }

    public String showAllParkingSpots() {
        StringBuilder ans = new StringBuilder();
        ans.append("\r\n");

        for(VehicleType vehicleType : VehicleType.values())
        {
            for(ParkingSpot parkingSpot : parkingSpots.get(vehicleType))
            {
                ans.append(parkingSpot.getId()).append(" -> eletric: ").append(parkingSpot.hasElectricCharger()).append("\r\n");
            }
        }

        ans.append("\r\n");

        return ans.toString();
    }

    public void addNoOfExistingSpotsForVehicleType(VehicleType vehicleType, int noOfExistingSpots)
    {
        noOfExistingSpotsForVehicleType.put(vehicleType, noOfExistingSpots);
    }

    public Map<VehicleType, Integer> getNoOfExistingSpotsForVehicleType() { return noOfExistingSpotsForVehicleType; }

    public void addNoOfEmptySpots(VehicleType vehicleType, int noOfEmptySpots) { emptySpotsNumber.put(vehicleType, noOfEmptySpots); }

    public Map<VehicleType, ArrayList<ParkingSpot>> getParkingSpots() { return parkingSpots; }

    public Map<VehicleType, Integer> getEmptySpotsNumber() { return emptySpotsNumber; }

    public void assignParkingSpotToDriver(int parkingSpot, Driver driver)
    {
        assignedParkingSpots.put(parkingSpot, driver);
    }

    public void decrementEmptySpotsNumberForVehicleType(VehicleType vehicleType)
    {
        int currentlyEmptySpots = emptySpotsNumber.get(vehicleType);
        emptySpotsNumber.put(vehicleType, currentlyEmptySpots - 1);
    }

    public void incrementEmptySpotsNumberForVehicleType(VehicleType vehicleType)
    {
        int currentlyEmptySpots = emptySpotsNumber.get(vehicleType);
        emptySpotsNumber.put(vehicleType, currentlyEmptySpots + 1);
    }

}
