package parking;

import vehicles.VehicleType;

import java.util.ArrayList;
import java.util.Map;

public class ParkingLot {
    private Map<VehicleType, Integer> noOfExistingSpotsForVehicleType;
    private Map<VehicleType, Integer> emptySpotsNumber;
    private Map<VehicleType, ArrayList<ParkingSpot>> parkingSpots;
    private Map<Integer, Driver> assignedParkingSpots;

    public ParkingLot(Map<VehicleType, Integer> noOfExistingSpotsForVehicleType, Map<VehicleType, Integer> emptySpotsNumber, Map<VehicleType, ArrayList<ParkingSpot>> parkingSpots, Map<Integer, Driver> assignedParkingSpots) {
        this.noOfExistingSpotsForVehicleType = noOfExistingSpotsForVehicleType;
        this.emptySpotsNumber = emptySpotsNumber;
        this.parkingSpots = parkingSpots;
        this.assignedParkingSpots = assignedParkingSpots;
    }

    public Map<Integer, Driver> getAssignedParkingSpots() { return assignedParkingSpots; }
    public Map<VehicleType, Integer> getNoOfExistingSpotsForVehicleType() { return noOfExistingSpotsForVehicleType; }
    public Map<VehicleType, Integer> getEmptySpotsNumber() { return emptySpotsNumber; }
    public Map<VehicleType, ArrayList<ParkingSpot>> getParkingSpots() { return parkingSpots; }

    public void removeAssignedParkingSpot(int idParkingSpot) {
        assignedParkingSpots.remove(idParkingSpot);
    }

    public void assignParkingSpotToDriver(int parkingSpotId, Driver driver) {
        assignedParkingSpots.put(parkingSpotId, driver);
    }

    public void decrementEmptySpotsNumberForVehicleType(VehicleType vehicleType) {
        int currentlyEmptySpots = emptySpotsNumber.get(vehicleType);
        emptySpotsNumber.put(vehicleType, currentlyEmptySpots - 1);
    }

    public void incrementEmptySpotsNumberForVehicleType(VehicleType vehicleType) {
        int currentlyEmptySpots = emptySpotsNumber.get(vehicleType);
        emptySpotsNumber.put(vehicleType, currentlyEmptySpots + 1);
    }

    public void occupyParkingSpot(int spotId, Driver driver) {
        // We will search for the parking spot with id = spotId and save the vehicle type and index of that spot
        int spotIndex;

        for(VehicleType vehicleType : VehicleType.values()) {
            spotIndex = 0;
            for(ParkingSpot parkingSpot : parkingSpots.get(vehicleType)) {
                if(parkingSpot.getId() == spotId) {
                    // S-a ocupat locul de parcare
                    parkingSpots.get(vehicleType).get(spotIndex).setFree(false);

                    // Asignam locul de parcare unui sofer
                    assignParkingSpotToDriver(spotId, driver);

                    // Scadem nr de locuri libere disponibile
                    decrementEmptySpotsNumberForVehicleType(vehicleType);

                    return;
                }
                ++spotIndex;
            }
        }

    }

    Driver releaseParkingSpot(int idParkingSpot, VehicleType vehicleType, int index) {
        // Obtinem soferul care se afla pe locul ce va fi eliberat
        Driver driver = getAssignedParkingSpots().get(idParkingSpot);

        // Scoatem locul din lista de locuri de parcare asignate soferilor
        removeAssignedParkingSpot(idParkingSpot);

        // S-a eliberat un loc de parcare
        getParkingSpots().get(vehicleType).get(index).setFree(true);

        // Incrementam nr de locuri libere pt categoria specificata
        incrementEmptySpotsNumberForVehicleType(vehicleType);

        return driver;
    }

}
