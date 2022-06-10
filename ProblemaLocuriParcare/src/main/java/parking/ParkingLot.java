package parking;

import vehicles.VehicleType;

import java.util.ArrayList;
import java.util.Map;

public class ParkingLot {
    private Map<VehicleType, Integer> noOfExistingSpotsForVehicleType;
    private Map<VehicleType, Integer> emptySpotsNumber;
    private ArrayList<ParkingSpot> parkingSpots;
    private Map<Integer, Driver> assignedParkingSpots;

    public ParkingLot(Map<VehicleType, Integer> noOfExistingSpotsForVehicleType, Map<VehicleType, Integer> emptySpotsNumber, ArrayList<ParkingSpot> parkingSpots, Map<Integer, Driver> assignedParkingSpots) {
        this.noOfExistingSpotsForVehicleType = noOfExistingSpotsForVehicleType;
        this.emptySpotsNumber = emptySpotsNumber;
        this.parkingSpots = parkingSpots;
        this.assignedParkingSpots = assignedParkingSpots;
    }

    public Map<Integer, Driver> getAssignedParkingSpots() { return assignedParkingSpots; }
    public Map<VehicleType, Integer> getNoOfExistingSpotsForVehicleType() { return noOfExistingSpotsForVehicleType; }
    public Map<VehicleType, Integer> getEmptySpotsNumber() { return emptySpotsNumber; }
    public ArrayList<ParkingSpot> getParkingSpots() { return parkingSpots; }
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

    public void occupyParkingSpot(ParkingSpot parkingSpotToOccupy, Driver driver) {
        // We will search for the parking spot with id = spotId and save the vehicle type and index of that spot
        for(ParkingSpot parkingSpot : parkingSpots) {
            if(parkingSpot.getId() == parkingSpotToOccupy.getId()) {
                // S-a ocupat locul de parcare
                parkingSpot.setFree(false);

                // Asignam locul de parcare unui sofer
                assignParkingSpotToDriver(parkingSpotToOccupy.getId(), driver);

                // Scadem nr de locuri libere disponibile
                decrementEmptySpotsNumberForVehicleType(parkingSpotToOccupy.getSpotType());

                return;
            }
        }
    }

    Driver releaseParkingSpot(ParkingSpot parkingSpot) {
        // Obtinem soferul care se afla pe locul ce va fi eliberat
        Driver driver = getAssignedParkingSpots().get(parkingSpot.getId());

        // Scoatem locul din lista de locuri de parcare asignate soferilor
        removeAssignedParkingSpot(parkingSpot.getId());

        // S-a eliberat un loc de parcare
        for(ParkingSpot parkingSpot1 : parkingSpots) {
            if(parkingSpot1.getId() == parkingSpot.getId()) {
                parkingSpot1.setFree(true);
                break;
            }
        }

        // Incrementam nr de locuri libere pt categoria specificata
        incrementEmptySpotsNumberForVehicleType(parkingSpot.getSpotType());

        return driver;
    }

}
