package database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import parking.Driver;
import parking.ParkingSpot;
import vehicles.*;

import java.util.ArrayList;
import java.util.HashMap;

public class ParkingSpotsCollection {
    private static MongoCollection<Document> collection = Database.getInstance().getParkingLotDB().getCollection("parkingSpotInputs");

    public static Driver getDriverAssignedToParkingSpot(int idParkingSpot) {
        Bson filterId = Filters.eq("parkingSpotId", idParkingSpot);
        Bson filterFree = Filters.eq("free", false);
        Document document = collection.find(Filters.and(filterId, filterFree)).first();
        if(document == null) {
            throw new RuntimeException();
        }

        // Daca nu s-a aruncat o exceptie, inseamna ca s-a gasit locul de parcare cu id-ul dat (si e ocupat -> exista un driver acolo)
        int driverId = (int) document.get("driverId");
        return DriversCollection.getDriverById(driverId);
    }

    public static void makeParkingSpotFree(int idParkingSpot) {
        // Trebuie sa facem locul "free" (eliberam locul de parcare)
        Bson filterId = Filters.eq("parkingSpotId", idParkingSpot);
        Document parkingSpotDocument = collection.find(filterId).first();
        if(parkingSpotDocument == null) {
            throw new RuntimeException();
        }

        collection.updateOne(Filters.eq("parkingSpotId", idParkingSpot), Updates.set("free", true));

        // Eliminam driver-ul aferent din colectia cu driveri.
        int driverId = (int) parkingSpotDocument.get("driverId");
        DriversCollection.removeDriverById(driverId);

        collection.updateOne(Filters.eq("parkingSpotId", idParkingSpot), Updates.set("driverId", null));
    }

    public static void occupyParkingSpot(int idParkingSpot, Driver driver) {
        Bson filterId = Filters.eq("parkingSpotId", idParkingSpot);
        Document parkingSpotDocument = collection.find(filterId).first();
        if(parkingSpotDocument == null) {
            throw new RuntimeException();
        }

        int driverId = idParkingSpot + 7;

        // Ocup locul de parcare
        collection.updateOne(Filters.eq("parkingSpotId", idParkingSpot), Updates.set("free", false));
        // Asignez un driver id soferului care urmeaza sa parcheze aici (vom considera acelasi cu idParkingSpot)
        collection.updateOne(Filters.eq("parkingSpotId", idParkingSpot), Updates.set("driverId", driverId));

        // Adaug soferul in colectia cu soferi
        DriversCollection.addDriver(driverId, driver);
    }

    public static int getParkingSpotId(VehicleType vehicleType, boolean electric) {
        String vehicleTypeString = "";
        if(vehicleType == VehicleType.MOTORCYCLE) {
            vehicleTypeString = "Motorcycle";
        } else if (vehicleType == VehicleType.CAR) {
            vehicleTypeString = "Car";
        } else if (vehicleType == VehicleType.TRUCK) {
            vehicleTypeString = "Truck";
        }

        String electricString = "";
        if(electric) {
            electricString = "electric";
        } else {
            electricString = "nonelectric";
        }

        Bson vehicleTypeFilter = Filters.eq("parkingSpotVehicleType", vehicleTypeString);
        Bson electricFilter = Filters.eq("parkingSpotType", electricString);
        Bson freeFilter = Filters.eq("free", true);

        Document parkingSpotDocument = collection.find(Filters.and(vehicleTypeFilter, electricFilter, freeFilter)).first();
        if(parkingSpotDocument == null) {
            throw new RuntimeException();
        }

        // Am gasit un parking spot cu cerintele cautate (daca n-am gasit, se arunca runtime exception)
        int parkingSpotId = (int) parkingSpotDocument.get("parkingSpotId");



        return parkingSpotId;
    }

    public static ArrayList<ParkingSpot> getParkingSpots() {
        ArrayList<ParkingSpot> parkingSpots = new ArrayList<>();

        MongoCursor<Document> cursor = collection.find().iterator();
        while(cursor.hasNext()) {
            Document currentDocument = cursor.next();
            int spotId = (int) currentDocument.get("parkingSpotId");

            boolean free = (boolean) currentDocument.get("free");

            String parkingSpotType = (String) currentDocument.get("parkingSpotType");
            boolean electric = false;
            if(parkingSpotType.equals("electric")) {
                electric = true;
            }

            String parkingSpotVehicleType = (String) currentDocument.get("parkingSpotVehicleType");
            VehicleType spotVehicleType = null;
            if (parkingSpotVehicleType.equals("Motorcycle")) {
                spotVehicleType = VehicleType.MOTORCYCLE;
            } else if (parkingSpotVehicleType.equals("Car")) {
                spotVehicleType = VehicleType.CAR;
            } else if (parkingSpotVehicleType.equals("Truck")) {
                spotVehicleType = VehicleType.TRUCK;
            }

            parkingSpots.add(new ParkingSpot(spotId, spotVehicleType, free, electric));
        }

        return parkingSpots;
    }

    public static HashMap<Integer, Driver> getDrivers() {
        HashMap<Integer, Driver> drivers = new HashMap<>();

        // Luam locurile ocupate (unde soferii si-au parcat vehiculele)
        MongoCursor<Document> cursor = collection.find(Filters.eq("free", false)).iterator();
        while(cursor.hasNext()) {
            Document currentDocument = cursor.next();
            int parkingSpotId = (int) currentDocument.get("parkingSpotId");
            int driverId = (int) currentDocument.get("driverId");
            Driver driver = DriversCollection.getDriverById(driverId);

            drivers.put(parkingSpotId, driver);
        }

        return drivers;
    }

    public static int getNumberOfEmptySpotsForVehicleType(VehicleType vehicleType) {
        String vehicleTypeString = "";
        if (vehicleType == VehicleType.MOTORCYCLE) {
            vehicleTypeString = "Motorcycle";
        } else if (vehicleType == VehicleType.CAR) {
            vehicleTypeString = "Car";
        } else if (vehicleType == VehicleType.TRUCK) {
            vehicleTypeString = "Truck";
        }

        Bson vehicleTypeFilter = Filters.eq("parkingSpotVehicleType", vehicleTypeString);
        Bson freeFilter = Filters.eq("free", true);
        int numberOfEmptySpots = (int) collection.countDocuments(Filters.and(vehicleTypeFilter, freeFilter));
        return numberOfEmptySpots;
    }


}
