package database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import exceptions.ParkingSpotNotFoundException;
import exceptions.SimultaneousOperationInDatabaseCollectionException;
import org.bson.Document;
import org.bson.conversions.Bson;
import parking.ParkingSpot;
import parking.ParkingSpotType;
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

    // Preiau starea obiectului parkingSpot (care reprezinta starea finala) si actualizez in baza de date folosind aceasta stare
    public void updateParkingSpotWhenDriverLeaves(ParkingSpot parkingSpot) throws SimultaneousOperationInDatabaseCollectionException, ParkingSpotNotFoundException {
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

        UpdateResult updateResult = parkingSpotsCollection.updateOne(Filters.and(filterParkingSpotId, filterVersion), Updates.combine(Updates.set("free", parkingSpot.getFree()), Updates.set("vehicleId", 0), Updates.set("version", version + 1)));
        if(updateResult.getMatchedCount() == 0)
            throw new SimultaneousOperationInDatabaseCollectionException();

    }

    public void updateParkingSpotWhenDriverParks(ParkingSpot parkingSpot) throws SimultaneousOperationInDatabaseCollectionException, ParkingSpotNotFoundException {
        Bson filterId = Filters.eq("parkingSpotId", parkingSpot.getId());
        Document parkingSpotDocument = parkingSpotsCollection.find(filterId).first();
        if(parkingSpotDocument == null) {
            throw new ParkingSpotNotFoundException();
        }

        int version = (int) parkingSpotDocument.get("version");
        Bson filterParkingSpotId = Filters.eq("parkingSpotId", parkingSpot.getId());
        Bson filterVersion = Filters.eq("version", version);

        UpdateResult updateResult = parkingSpotsCollection.updateOne(Filters.and(filterParkingSpotId, filterVersion), Updates.combine(Updates.set("free", parkingSpot.getFree()), Updates.set("vehicleId", parkingSpot.getVehicleId()), Updates.set("version", version + 1)));
        if(updateResult.getMatchedCount() == 0)
            throw new SimultaneousOperationInDatabaseCollectionException();

    }

    public int getIdForAvailableParkingSpot(ParkingSpotType parkingSpotType, boolean electric) throws ParkingSpotNotFoundException {
        Bson vehicleTypeFilter = Filters.eq("parkingSpotType", parkingSpotType.getParkingSpotTypeName());
        Bson electricFilter = Filters.eq("electricType", electric ? "electric" : "nonelectric");
        Bson freeFilter = Filters.eq("free", true);

        Document parkingSpotDocument = parkingSpotsCollection.find(Filters.and(vehicleTypeFilter, electricFilter, freeFilter)).first();
        if(parkingSpotDocument == null) {
            throw new ParkingSpotNotFoundException();
        }

        // Am gasit un parking spot cu cerintele cautate
        return (int) parkingSpotDocument.get("parkingSpotId");
    }

    public ArrayList<ParkingSpot> getParkingSpots() {
        ArrayList<ParkingSpot> parkingSpots = new ArrayList<>();

        MongoCursor<Document> cursor = this.parkingSpotsCollection.find().iterator();
        while(cursor.hasNext()) {
            Document parkingSpotDocument = cursor.next();
            int spotId = (int) parkingSpotDocument.get("parkingSpotId");
            int vehicleId = (int) parkingSpotDocument.get("vehicleId");
            boolean free = (boolean) parkingSpotDocument.get("free");
            String electricType = (String) parkingSpotDocument.get("electricType");
            boolean electric = electricType.equals("electric");
            String parkingSpotTypeString = (String) parkingSpotDocument.get("parkingSpotType");
            ParkingSpotType parkingSpotType = ParkingSpotType.getParkingSpotTypeByName(parkingSpotTypeString);

            parkingSpots.add(new ParkingSpot(spotId, vehicleId, parkingSpotType, free, electric));
        }

        return parkingSpots;
    }

    public HashMap<Integer, Vehicle> getVehiclesAndCorrespondingParkingSpots(ArrayList<Vehicle> vehicles) {
        HashMap<Integer, Vehicle> vehiclesAndCorrespondingParkingSpots = new HashMap<>();

        // Luam locurile ocupate (unde soferii si-au parcat vehiculele)
        MongoCursor<Document> cursor = parkingSpotsCollection.find(Filters.eq("free", false)).iterator();
        while(cursor.hasNext()) {
            Document  parkingSpotDocument= cursor.next();
            int parkingSpotId = (int) parkingSpotDocument.get("parkingSpotId");
            int vehicleId = (int) parkingSpotDocument.get("vehicleId");
            for(Vehicle vehicle : vehicles) {
                if (vehicle.getVehicleId() == vehicleId) {
                    vehiclesAndCorrespondingParkingSpots.put(parkingSpotId, vehicle);
                    break;
                }
            }
        }

        return vehiclesAndCorrespondingParkingSpots;
    }

    public int getNumberOfEmptySpotsForParkingSpotType(ParkingSpotType parkingSpotType) {
        String parkingSpotTypeString = parkingSpotType.getParkingSpotTypeName();

        Bson parkingSpotTypeFilter = Filters.eq("parkingSpotType", parkingSpotTypeString);
        Bson freeFilter = Filters.eq("free", true);
        int numberOfEmptySpots = (int) parkingSpotsCollection.countDocuments(Filters.and(parkingSpotTypeFilter, freeFilter));
        return numberOfEmptySpots;
    }

    public ParkingSpot getParkingSpotById(int parkingSpotId) throws ParkingSpotNotFoundException {
        Bson filterId = Filters.eq("parkingSpotId", parkingSpotId);
        Document parkingSpotDocument = parkingSpotsCollection.find(filterId).first();
        if(parkingSpotDocument == null) {
            throw new ParkingSpotNotFoundException();
        }

        int vehicleId = (int) parkingSpotDocument.get("vehicleId");
        String parkingSpotTypeString = (String) parkingSpotDocument.get("parkingSpotType");
        String electricString = (String) parkingSpotDocument.get("electricType");
        boolean free = (boolean) parkingSpotDocument.get("free");
        ParkingSpotType parkingSpotType = ParkingSpotType.getParkingSpotTypeByName(parkingSpotTypeString);
        boolean electric = electricString.equals("electric");

        return new ParkingSpot(parkingSpotId, vehicleId, parkingSpotType, free, electric);
    }

    public void initializeParkingSpotsCollection() {
        Document document1 = new Document();
        document1.append("parkingSpotId", 7);
        document1.append("electricType", "electric");
        document1.append("parkingSpotType", "Small");
        document1.append("free", true);
        document1.append("vehicleId", 0);
        document1.append("version", 1);

        Document document2 = new Document();
        document2.append("parkingSpotId", 2);
        document2.append("electricType", "nonelectric");
        document2.append("parkingSpotType", "Small");
        document2.append("free", true);
        document2.append("vehicleId", 0);
        document2.append("version", 1);

        Document document3 = new Document();
        document3.append("parkingSpotId", 1);
        document3.append("electricType", "nonelectric");
        document3.append("parkingSpotType", "Medium");
        document3.append("free", true);
        document3.append("vehicleId", 0);
        document3.append("version", 1);

        Document document4 = new Document();
        document4.append("parkingSpotId", 3);
        document4.append("electricType", "nonelectric");
        document4.append("parkingSpotType", "Medium");
        document4.append("free", true);
        document4.append("vehicleId", 0);
        document4.append("version", 1);

        Document document5 = new Document();
        document5.append("parkingSpotId", 9);
        document5.append("electricType", "electric");
        document5.append("parkingSpotType", "Medium");
        document5.append("free", true);
        document5.append("vehicleId", 0);
        document5.append("version", 1);

        Document document6 = new Document();
        document6.append("parkingSpotId", 6);
        document6.append("electricType", "electric");
        document6.append("parkingSpotType", "Large");
        document6.append("free", true);
        document6.append("vehicleId", 0);
        document6.append("version", 1);

        Document document7 = new Document();
        document7.append("parkingSpotId", 5);
        document7.append("electricType", "electric");
        document7.append("parkingSpotType", "Large");
        document7.append("free", true);
        document7.append("vehicleId", 0);
        document7.append("version", 1);

        Document document8 = new Document();
        document8.append("parkingSpotId", 8);
        document8.append("electricType", "nonelectric");
        document8.append("parkingSpotType", "Large");
        document8.append("free", true);
        document8.append("vehicleId", 0);
        document8.append("version", 1);

        Document document9 = new Document();
        document9.append("parkingSpotId", 4);
        document9.append("electricType", "electric");
        document9.append("parkingSpotType", "Large");
        document9.append("free", true);
        document9.append("vehicleId", 0);
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
