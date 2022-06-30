package database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import exceptions.ParkingSpotNotFoundException;
import exceptions.ParkingSpotNotOccupiedException;
import exceptions.SimultaneousOperationInDatabaseCollectionException;
import org.bson.Document;
import org.bson.conversions.Bson;
import parking.Driver;
import parking.ParkingSpot;
import vehicles.*;

import java.util.ArrayList;
import java.util.HashMap;


/*
!!! Functiile din baza de date trebuie sa stie doar de operatii CRUD. Noile valori in cazul unui update (sau valorile in cazul unui insert) sunt preluate din obiectele date ca parametru
 */
public class ParkingSpotsCollection {
    private MongoCollection<Document> parkingSpotsCollection;

    public ParkingSpotsCollection(Database database) {
        parkingSpotsCollection = database.getDatabase().getCollection("parkingSpotInputs");
    }

    public int getDriverIdForOccupiedSpot(ParkingSpot parkingSpot) throws ParkingSpotNotOccupiedException {
        Bson filterId = Filters.eq("parkingSpotId", parkingSpot.getId());
        Document document = parkingSpotsCollection.find(filterId).first();
        return (int) document.get("driverId");
    }

    // Preiau starea obiectului parkingSpot (care reprezinta starea finala) si actualizez in baza de date folosind aceasta stare
    public void updateParkingSpotFreeStatus(ParkingSpot parkingSpot) throws SimultaneousOperationInDatabaseCollectionException, ParkingSpotNotFoundException {
        // Trebuie sa facem locul "free" (eliberam locul de parcare)
        Bson filterId = Filters.eq("parkingSpotId", parkingSpot.getId());
        Document parkingSpotDocument = parkingSpotsCollection.find(filterId).first();
        if(parkingSpotDocument == null) {
            throw new ParkingSpotNotFoundException();
        }

        // Citim versiunea documentului ca sa vedem daca putem facem modificari
        // Daca versiunea citita nu e aceeasi cu versiunea curenta (din momentul in care se face modificarea - update - atunci aruncam o exceptie)
        int version = (int) parkingSpotDocument.get("version");

        Bson filterParkingSpotId = Filters.eq("parkingSpotId", parkingSpot.getId());
        Bson filterVersion = Filters.eq("version", version);

        UpdateResult updateResult = parkingSpotsCollection.updateOne(Filters.and(filterParkingSpotId, filterVersion), Updates.combine(Updates.set("free", parkingSpot.isFree()), Updates.set("driverId", null), Updates.set("version", version + 1)));
        if(updateResult.getMatchedCount() == 0)
            throw new SimultaneousOperationInDatabaseCollectionException();

    }

    public void updateParkingSpotWhenDriverParks(ParkingSpot parkingSpot, Driver driver) throws SimultaneousOperationInDatabaseCollectionException, ParkingSpotNotFoundException {
        Bson filterId = Filters.eq("parkingSpotId", parkingSpot.getId());
        Document parkingSpotDocument = parkingSpotsCollection.find(filterId).first();
        if(parkingSpotDocument == null) {
            throw new ParkingSpotNotFoundException();
        }

        driver.setId(parkingSpot.getId() + 7);
        int version = (int) parkingSpotDocument.get("version");
        Bson filterParkingSpotId = Filters.eq("parkingSpotId", parkingSpot.getId());
        Bson filterVersion = Filters.eq("version", version);

        // Ocup locul de parcare, asignez un driver id soferului care urmeaza sa parcheze aici (vom considera acelasi cu idParkingSpot)
        UpdateResult updateResult = parkingSpotsCollection.updateOne(Filters.and(filterParkingSpotId, filterVersion), Updates.combine(Updates.set("free", parkingSpot.isFree()), Updates.set("driverId", driver.getId()), Updates.set("version", version + 1)));
        if(updateResult.getMatchedCount() == 0)
            throw new SimultaneousOperationInDatabaseCollectionException();

    }

    public int getParkingSpotId(VehicleType vehicleType, boolean electric) throws ParkingSpotNotFoundException {
        Bson vehicleTypeFilter = Filters.eq("parkingSpotVehicleType", vehicleType.getVehicleTypeName());
        Bson electricFilter = Filters.eq("parkingSpotType", electric ? "electric" : "nonelectric");
        Bson freeFilter = Filters.eq("free", true);

        Document parkingSpotDocument = parkingSpotsCollection.find(Filters.and(vehicleTypeFilter, electricFilter, freeFilter)).first();
        if(parkingSpotDocument == null) {
            throw new ParkingSpotNotFoundException();
        }

        // Am gasit un parking spot cu cerintele cautate (daca n-am gasit, se arunca runtime exception)
        int parkingSpotId = (int) parkingSpotDocument.get("parkingSpotId");

        return parkingSpotId;
    }

    public ArrayList<ParkingSpot> getParkingSpots() {
        ArrayList<ParkingSpot> parkingSpots = new ArrayList<>();

        MongoCursor<Document> cursor = this.parkingSpotsCollection.find().iterator();
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
            VehicleType spotVehicleType = VehicleType.getVehicleTypeByName(parkingSpotVehicleType);

            parkingSpots.add(new ParkingSpot(spotId, spotVehicleType, free, electric));
        }

        return parkingSpots;
    }

    public HashMap<Integer, Driver> getDriversAndCorrespondingParkingSpot(ArrayList<Driver> drivers) {
        HashMap<Integer, Driver> driversAndCorrespondingSpot = new HashMap<>();

        // Luam locurile ocupate (unde soferii si-au parcat vehiculele)
        MongoCursor<Document> cursor = parkingSpotsCollection.find(Filters.eq("free", false)).iterator();
        while(cursor.hasNext()) {
            Document currentDocument = cursor.next();
            int parkingSpotId = (int) currentDocument.get("parkingSpotId");
            int driverId = (int) currentDocument.get("driverId");
            for(Driver driver : drivers) {
                if (driver.getId() == driverId) {
                    driversAndCorrespondingSpot.put(parkingSpotId, driver);
                    break;
                }
            }
        }

        return driversAndCorrespondingSpot;
    }

    public int getNumberOfEmptySpotsForVehicleType(VehicleType vehicleType) {
        String vehicleTypeString = vehicleType.getVehicleTypeName();

        Bson vehicleTypeFilter = Filters.eq("parkingSpotVehicleType", vehicleTypeString);
        Bson freeFilter = Filters.eq("free", true);
        int numberOfEmptySpots = (int) parkingSpotsCollection.countDocuments(Filters.and(vehicleTypeFilter, freeFilter));
        return numberOfEmptySpots;
    }

    public ParkingSpot getParkingSpotById(int parkingSpotId) throws ParkingSpotNotFoundException {
        Bson filterId = Filters.eq("parkingSpotId", parkingSpotId);
        Document parkingSpotDocument = parkingSpotsCollection.find(filterId).first();
        if(parkingSpotDocument == null) {
            throw new ParkingSpotNotFoundException();
        }

        String vehicleTypeString = (String) parkingSpotDocument.get("parkingSpotVehicleType");
        String electricString = (String) parkingSpotDocument.get("parkingSpotType");
        boolean free = (boolean) parkingSpotDocument.get("free");
        VehicleType vehicleType = VehicleType.getVehicleTypeByName(vehicleTypeString);
        boolean electric = electricString.equals("electric");

        return new ParkingSpot(parkingSpotId, vehicleType, free, electric);
    }

    public void initializeParkingSpotsCollection() {
        Document document1 = new Document();
        document1.append("parkingSpotId", 7);
        document1.append("parkingSpotType", "electric");
        document1.append("parkingSpotVehicleType", "Motorcycle");
        document1.append("free", true);
        document1.append("driverId", null);
        document1.append("version", 1);

        Document document2 = new Document();
        document2.append("parkingSpotId", 2);
        document2.append("parkingSpotType", "nonelectric");
        document2.append("parkingSpotVehicleType", "Motorcycle");
        document2.append("free", true);
        document2.append("driverId", null);
        document2.append("version", 1);

        Document document3 = new Document();
        document3.append("parkingSpotId", 1);
        document3.append("parkingSpotType", "nonelectric");
        document3.append("parkingSpotVehicleType", "Car");
        document3.append("free", true);
        document3.append("driverId", null);
        document3.append("version", 1);

        Document document4 = new Document();
        document4.append("parkingSpotId", 3);
        document4.append("parkingSpotType", "nonelectric");
        document4.append("parkingSpotVehicleType", "Car");
        document4.append("free", true);
        document4.append("driverId", null);
        document4.append("version", 1);

        Document document5 = new Document();
        document5.append("parkingSpotId", 9);
        document5.append("parkingSpotType", "electric");
        document5.append("parkingSpotVehicleType", "Car");
        document5.append("free", true);
        document5.append("driverId", null);
        document5.append("version", 1);

        Document document6 = new Document();
        document6.append("parkingSpotId", 6);
        document6.append("parkingSpotType", "electric");
        document6.append("parkingSpotVehicleType", "Truck");
        document6.append("free", true);
        document6.append("driverId", null);
        document6.append("version", 1);

        Document document7 = new Document();
        document7.append("parkingSpotId", 5);
        document7.append("parkingSpotType", "electric");
        document7.append("parkingSpotVehicleType", "Truck");
        document7.append("free", true);
        document7.append("driverId", null);
        document7.append("version", 1);

        Document document8 = new Document();
        document8.append("parkingSpotId", 8);
        document8.append("parkingSpotType", "nonelectric");
        document8.append("parkingSpotVehicleType", "Truck");
        document8.append("free", true);
        document8.append("driverId", null);
        document8.append("version", 1);

        Document document9 = new Document();
        document9.append("parkingSpotId", 4);
        document9.append("parkingSpotType", "electric");
        document9.append("parkingSpotVehicleType", "Truck");
        document9.append("free", true);
        document9.append("driverId", null);
        document9.append("version", 1);

        parkingSpotsCollection.insertOne(document1);
        parkingSpotsCollection.insertOne(document2);
        parkingSpotsCollection.insertOne(document3);
        parkingSpotsCollection.insertOne(document4);
        parkingSpotsCollection.insertOne(document5);
        parkingSpotsCollection.insertOne(document6);
        parkingSpotsCollection.insertOne(document7);
        parkingSpotsCollection.insertOne(document8);
        parkingSpotsCollection.insertOne(document9);
    }
    public void resetParkingSpotsCollection() {
        // Delete all documents from "parkingSpotInputs" collection
        parkingSpotsCollection.deleteMany(new Document());
    }


}
