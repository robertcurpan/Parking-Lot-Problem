package database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import exceptions.DriverNotFoundException;
import org.bson.Document;
import parking.Driver;
import vehicles.*;

import java.util.ArrayList;

public class DriversCollection {
    private MongoCollection<Document> driversCollection;
    // In the collection, we will have a driverId and another document that contains the info of a driver.

    public DriversCollection(Database database) {
        driversCollection = database.getDatabase().getCollection("drivers");
    }

    public Driver getDriverById(int driverId) throws DriverNotFoundException {
        Document document = driversCollection.find(Filters.eq("driverId", driverId)).first();
        if (document == null) {
            throw new DriverNotFoundException();
        }

        String driverName = (String) document.get("name");
        boolean isDriverVIP = (boolean) document.get("vipStatus");
        Document vehicleDocument = (Document) document.get("vehicle");
        String vehicleColor = (String) vehicleDocument.get("color");
        int vehiclePrice = (int) vehicleDocument.get("price");
        boolean vehicleElectric = (boolean) vehicleDocument.get("electric");
        String vehicleType = (String) vehicleDocument.get("type");

        Vehicle vehicle = null;
        VehicleType type = VehicleType.valueOf(vehicleType);
        switch(type) {
            case MOTORCYCLE: vehicle = new Motorcycle(vehicleColor, vehiclePrice, vehicleElectric); break;
            case CAR: vehicle = new Car(vehicleColor, vehiclePrice, vehicleElectric); break;
            case TRUCK: vehicle = new Truck(vehicleColor, vehiclePrice, vehicleElectric); break;
        }

        Driver driver = new Driver(driverName, vehicle, isDriverVIP);
        driver.setId(driverId);
        return driver;
    }

    public void removeDriver(Driver driver) {
        driversCollection.deleteOne(Filters.eq("driverId", driver.getId()));
    }

    public void addDriver(Driver driver) {
        Document vehicleDocument = new Document();
        vehicleDocument.append("type", driver.getVehicle().getVehicleType());
        vehicleDocument.append("color", driver.getVehicle().getColor());
        vehicleDocument.append("price", driver.getVehicle().getPrice());
        vehicleDocument.append("electric", driver.getVehicle().isElectric());

        Document driverDocument = new Document();
        driverDocument.append("driverId", driver.getId());
        driverDocument.append("name", driver.getName());
        driverDocument.append("vehicle", vehicleDocument);
        driverDocument.append("vipStatus", driver.getVipStatus());

        driversCollection.insertOne(driverDocument);
    }

    public ArrayList<Driver> getAllDrivers() throws DriverNotFoundException {
        ArrayList<Driver> drivers = new ArrayList<>();

        // Luam toti driverii din colectie (daca sunt in colectie, inseamna ca au parcat)
        MongoCursor<Document> cursor = driversCollection.find().iterator();
        while(cursor.hasNext()) {
            Document currentDocument = cursor.next();
            int driverId = (int) currentDocument.get("driverId");
            Driver driver = getDriverById(driverId);

            drivers.add(driver);
        }

        return drivers;
    }

    public void resetDriversCollection() {
        driversCollection.deleteMany(new Document());
    }

}
