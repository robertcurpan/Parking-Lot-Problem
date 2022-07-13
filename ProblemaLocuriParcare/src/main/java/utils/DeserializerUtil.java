package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import structures.ExceptionJson;
import factory.VehicleCreatorGenerator;
import org.json.JSONArray;
import org.json.JSONObject;
import parking.Driver;
import parking.ParkingSpot;
import structures.Ticket;
import vehicles.Vehicle;
import vehicles.VehicleType;

import java.util.*;

public class DeserializerUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static List<ParkingSpot> getParkingSpots(String parkingSpotsJsonString) throws JsonProcessingException {
        List<ParkingSpot> parkingSpots = mapper.readValue(parkingSpotsJsonString, new TypeReference<List<ParkingSpot>>(){});
        return parkingSpots;
    }

    public static List<Ticket> getTickets(String vehiclesJsonString) {
        List<Ticket> tickets = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(vehiclesJsonString);

        for(int index = 0; index < jsonArray.length(); ++index) {
            JSONObject ticketObject = jsonArray.getJSONObject(index);
            Ticket ticket = getTicket(ticketObject.toString());

            tickets.add(ticket);
        }

        return tickets;
    }

    public static Ticket getTicket(String ticketJsonString) {
        JSONObject ticketJsonObject = new JSONObject(ticketJsonString);
        int idParkingSpot = ticketJsonObject.getInt("spotId");
        JSONObject vehicleJsonObject = ticketJsonObject.getJSONObject("vehicle");

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

        return new Ticket(idParkingSpot, vehicle);
    }

    public static ExceptionJson getExceptionJson(String exceptionString) throws JsonProcessingException {
        return mapper.readValue(exceptionString, ExceptionJson.class);
    }

}
