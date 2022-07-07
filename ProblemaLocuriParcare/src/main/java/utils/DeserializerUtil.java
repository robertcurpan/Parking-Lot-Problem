package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import factory.VehicleCreatorGenerator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import parking.Driver;
import parking.ParkingSpot;
import structures.Ticket;
import vehicles.Car;
import vehicles.Vehicle;
import vehicles.VehicleType;

import java.util.*;

public class DeserializerUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static List<ParkingSpot> getParkingSpots(String parkingSpotsJsonString) throws JsonProcessingException {
        List<ParkingSpot> parkingSpots = mapper.readValue(parkingSpotsJsonString, new TypeReference<List<ParkingSpot>>(){});
        return parkingSpots;
    }

    public static List<Ticket> getTickets(String vehiclesJsonString) throws JsonProcessingException {
        List<Ticket> tickets = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(vehiclesJsonString);

        for(int index = 0; index < jsonArray.length(); ++index) {
            JSONObject ticketObject = jsonArray.getJSONObject(index);
            int spotId = ticketObject.getInt("spotId");
            JSONObject vehicleObject = ticketObject.getJSONObject("vehicle");
            Vehicle vehicle = getVehicleFromJsonObject(vehicleObject.toString());

            tickets.add(new Ticket(spotId, vehicle));
        }

        return tickets;
    }

    public static Vehicle getVehicleFromJsonObject(String vehicleJsonString) throws JSONException {
        JSONObject vehicleJsonObject = new JSONObject(vehicleJsonString);

        String driverName = vehicleJsonObject.getJSONObject("driver").getString("name");
        boolean driverVipStatus = vehicleJsonObject.getJSONObject("driver").getBoolean("vipStatus");
        Driver driver = new Driver(driverName, driverVipStatus);
        boolean electric = vehicleJsonObject.getBoolean("electric");
        int price = vehicleJsonObject.getInt("price");
        String color = vehicleJsonObject.getString("color");
        VehicleType vehicleType = VehicleType.valueOf(vehicleJsonObject.getString("vehicleType"));
        UUID vehicleId = UUID.fromString(vehicleJsonObject.getString("vehicleId"));

        Vehicle vehicle = VehicleCreatorGenerator.getVehicleCreator(vehicleType).getVehicle(driver, color, price, electric);
        vehicle.setVehicleId(vehicleId);

        return vehicle;
    }

    public static Ticket getTicket(String ticketJsonString) {
        JSONObject ticketJsonObject = new JSONObject(ticketJsonString);
        System.out.println(ticketJsonObject);

        int parkingSpotId = ticketJsonObject.getInt("spotId");
        JSONObject vehicleJsonObject = ticketJsonObject.getJSONObject("vehicle");
        Vehicle vehicle = getVehicleFromJsonObject(vehicleJsonObject.toString());

        return new Ticket(parkingSpotId, vehicle);
    }
}
