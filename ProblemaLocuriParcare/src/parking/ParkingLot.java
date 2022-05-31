package parking;

import exceptions.ParkingSpotNotFoundException;
import exceptions.ParkingSpotNotOccupiedException;
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
    private TicketGeneratorCreator ticketGeneratorCreator;

    private TicketGenerator ticketGenerator;

    public ParkingLot(Map<VehicleType, Integer> noOfExistingSpotsForVehicleType, Map<VehicleType, Integer> emptySpotsNumber, Map<VehicleType, ArrayList<ParkingSpot>> parkingSpots, Map<Integer, Driver> assignedParkingSpots, TicketGeneratorCreator ticketGeneratorCreator) {
        this.noOfExistingSpotsForVehicleType = noOfExistingSpotsForVehicleType;
        this.emptySpotsNumber = emptySpotsNumber;
        this.parkingSpots = parkingSpots;
        this.assignedParkingSpots = assignedParkingSpots;
        this.ticketGeneratorCreator = ticketGeneratorCreator;
    }

    public int getParkingTicket(Driver driver) throws ParkingSpotNotFoundException
    {
        String ans = "-";
        int vehicleTypeId = driver.getVehicle().getType();
        int idSpot;

        TicketGenerator ticketGenerator = ticketGeneratorCreator.getTicketGenerator(driver);

        // In urma apelului, vehicleTypeId nu mai este neaparat acelasi. Avand in vedere ca un sofer poate fi VIP, s-ar putea asigna un loc de parcare de la o categorie superioada
        // Trebuie sa folosim valoarea noua (care se actualizeaza in functia getTicket) si aici pt a actualiza nr de locuri libere.

        ParkingSpotIdAndVehicleTypeId parkingSpotIdAndVehicleTypeId = ticketGenerator.getTicket(this, driver, VehicleType.values()[vehicleTypeId]);
        idSpot = parkingSpotIdAndVehicleTypeId.getParkingSpotId();

        return idSpot;
    }


    public Driver leaveParkingLot(int idParkingSpot) throws ParkingSpotNotOccupiedException
    {
        if (!assignedParkingSpots.containsKey(idParkingSpot))
            throw new ParkingSpotNotOccupiedException();

        // Eliberam locul de parcare
        freeEmptySpot(idParkingSpot);

        // Scoatem locul din lista de locuri de parcare asignate soferilor
        Driver driver = assignedParkingSpots.get(idParkingSpot);
        assignedParkingSpots.remove(idParkingSpot);

        return driver;
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

    public Map<Integer, Driver> getAssignedParkingSpots() { return assignedParkingSpots; }

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

    public TicketGeneratorCreator getTicketGeneratorCreator() {
        return ticketGeneratorCreator;
    }

}
