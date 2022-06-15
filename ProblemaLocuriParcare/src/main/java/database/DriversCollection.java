package database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import parking.Driver;
import vehicles.*;

import java.util.Objects;

public class DriversCollection {
    private MongoCollection<Document> collection = Database.getInstance().getParkingLotDB().getCollection("drivers");
    // In the collection, we will have a driverId and another document that contains the info of a driver.

    public Driver getDriverById(int driverId) {
        Document document = collection.find(Filters.eq("driverId", driverId)).first();

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

        return new Driver(driverName, vehicle, isDriverVIP);
    }

    public void removeDriverById(int driverId) {
        collection.deleteOne(Filters.eq("driverId", driverId));
    }

    public void addDriver(int driverId, Driver driver) {
        Document vehicleDocument = new Document();
        vehicleDocument.append("type", driver.getVehicle().getVehicleType());
        vehicleDocument.append("color", driver.getVehicle().getColor());
        vehicleDocument.append("price", driver.getVehicle().getPrice());
        vehicleDocument.append("electric", driver.getVehicle().isElectric());

        Document driverDocument = new Document();
        driverDocument.append("driverId", driverId);
        driverDocument.append("name", driver.getName());
        driverDocument.append("vehicle", vehicleDocument);
        driverDocument.append("vipStatus", driver.getVipStatus());

        collection.insertOne(driverDocument);
    }

}
