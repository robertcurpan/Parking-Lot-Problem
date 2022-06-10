package database;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import parking.Driver;
import parking.ParkingLot;
import parking.ParkingSpot;
import structures.ParkingLotDimensions;
import vehicles.VehicleType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReadInputFromDatabase {
    private MongoCollection<Document> collection;

    public ReadInputFromDatabase() {
        collection = Database.getInstance().getParkingLotDB().getCollection("parkingSpotInputs");
    }

    public ParkingLotDimensions getParkingLotDimensions() {
        // We need to make a query for the parking lot sizes (for each category).
        Document nrParkingSpotsDocument = collection.find(Filters.exists("nrParkingSpots")).first();
        ArrayList<Integer> nrParkingSpots = (ArrayList<Integer>) nrParkingSpotsDocument.get("nrParkingSpots");
        return new ParkingLotDimensions(nrParkingSpots.get(0), nrParkingSpots.get(1), nrParkingSpots.get(2));
    }

    public ArrayList<ParkingSpot> getParkingSpots() {
        ArrayList<ParkingSpot> parkingSpots = new ArrayList<>();

        MongoCursor<Document> cursor = collection.find(Filters.exists("parkingSpotId")).iterator();
        while(cursor.hasNext()) {
            Document document = cursor.next();
            int parkingSpotId = (int) document.get("parkingSpotId");
            String parkingSpotType = (String) document.get("parkingSpotType");
            String parkingSpotVehicleType = (String) document.get("parkingSpotVehicleType");

            boolean electric = parkingSpotType.equals("electric");
            VehicleType vehicleType = VehicleType.MOTORCYCLE;
            if(parkingSpotVehicleType.equals("Motorcycle")) {
                vehicleType = VehicleType.MOTORCYCLE;
            } else if (parkingSpotVehicleType.equals("Car")) {
                vehicleType = VehicleType.CAR;
            } else if (parkingSpotVehicleType.equals("Truck")) {
                vehicleType = VehicleType.TRUCK;
            }

            parkingSpots.add(new ParkingSpot(parkingSpotId, vehicleType, true, electric));
        }

        return parkingSpots;
    }

    public ParkingLot getParkingLot() {
        ParkingLotDimensions parkingLotDimensions = getParkingLotDimensions();
        ArrayList<ParkingSpot> parkingSpots = getParkingSpots();
        Map<VehicleType, Integer> noOfExistingSpotsForVehicleType = new HashMap<VehicleType, Integer>();
        Map<VehicleType, Integer> noOfEmptySpots = new HashMap<VehicleType, Integer>();
        Map<Integer, Driver> assignedParkingSpots = new HashMap<Integer, Driver>();

        noOfExistingSpotsForVehicleType.put(VehicleType.MOTORCYCLE, parkingLotDimensions.getMotorcycleParkingSpotsDimension());
        noOfExistingSpotsForVehicleType.put(VehicleType.CAR, parkingLotDimensions.getCarParkingSpotsDimension());
        noOfExistingSpotsForVehicleType.put(VehicleType.TRUCK, parkingLotDimensions.getTruckParkingSpotsDimension());

        noOfEmptySpots.put(VehicleType.MOTORCYCLE, parkingLotDimensions.getMotorcycleParkingSpotsDimension());
        noOfEmptySpots.put(VehicleType.CAR, parkingLotDimensions.getCarParkingSpotsDimension());
        noOfEmptySpots.put(VehicleType.TRUCK, parkingLotDimensions.getTruckParkingSpotsDimension());

        return new ParkingLot(noOfExistingSpotsForVehicleType, noOfEmptySpots, parkingSpots, assignedParkingSpots);
    }

}
