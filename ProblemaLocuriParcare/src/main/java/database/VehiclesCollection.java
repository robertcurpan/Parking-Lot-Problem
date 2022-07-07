package database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import exceptions.VehicleNotFoundException;
import factory.VehicleCreatorGenerator;
import org.bson.Document;
import parking.Driver;
import vehicles.*;

import java.util.ArrayList;
import java.util.UUID;

public class VehiclesCollection {
    private MongoCollection<Document> vehiclesCollection;
    // In the collection, we will have a vehicleId, a driver and the info of the vehicle

    public VehiclesCollection(Database database) {
        vehiclesCollection = database.getDatabase().getCollection("vehicles");
    }

    public Vehicle getVehicleById(UUID vehicleId) throws VehicleNotFoundException {
        Document vehicleDocument = vehiclesCollection.find(Filters.eq("vehicleId", vehicleId)).first();
        if (vehicleDocument == null) {
            throw new VehicleNotFoundException();
        }

        Document driverDocument = (Document) vehicleDocument.get("driver");
        String driverName = (String) driverDocument.get("name");
        boolean driverVIPStatus = (boolean) driverDocument.get("vipStatus");

        String vehicleTypeString = (String) vehicleDocument.get("vehicleType");
        VehicleType vehicleType = VehicleType.getVehicleTypeByName(vehicleTypeString);
        String color = (String) vehicleDocument.get("color");
        int price = (int) vehicleDocument.get("price");
        boolean electric = (boolean) vehicleDocument.get("electric");

        Driver driver = new Driver(driverName, driverVIPStatus);
        Vehicle vehicle = VehicleCreatorGenerator.getVehicleCreator(vehicleType).getVehicle(driver, color, price, electric);
        vehicle.setVehicleId(vehicleId);

        return vehicle;
    }

    public void removeVehicle(Vehicle vehicle) {
        vehiclesCollection.deleteOne(Filters.eq("vehicleId", vehicle.getVehicleId()));
    }

    public void addVehicle(Vehicle vehicle) {
        Document driverDocument = new Document();
        driverDocument.append("name", vehicle.getDriver().getName());
        driverDocument.append("vipStatus", vehicle.getDriver().getVipStatus());

        Document vehicleDocument = new Document();
        vehicleDocument.append("vehicleId", vehicle.getVehicleId());
        vehicleDocument.append("vehicleType", vehicle.getVehicleType().getVehicleTypeName());
        vehicleDocument.append("color", vehicle.getColor());
        vehicleDocument.append("price", vehicle.getPrice());
        vehicleDocument.append("electric", vehicle.getElectric());
        vehicleDocument.append("driver", driverDocument);

        vehiclesCollection.insertOne(vehicleDocument);
    }

    public ArrayList<Vehicle> getAllVehicles() throws VehicleNotFoundException {
        ArrayList<Vehicle> vehicles = new ArrayList<>();

        MongoCursor<Document> cursor = vehiclesCollection.find().iterator();
        while(cursor.hasNext()) {
            Document vehicleDocument = cursor.next();
            UUID vehicleId = (UUID) vehicleDocument.get("vehicleId");
            Vehicle vehicle = getVehicleById(vehicleId);

            vehicles.add(vehicle);
        }

        return vehicles;
    }

    public void resetVehiclesCollection() {
        vehiclesCollection.deleteMany(new Document());
    }

}
