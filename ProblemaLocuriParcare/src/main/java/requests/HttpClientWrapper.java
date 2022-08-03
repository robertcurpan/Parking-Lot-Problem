package requests;

import com.fasterxml.jackson.core.JsonProcessingException;
import exceptions.*;
import parking.ParkingSpot;
import structures.ExceptionJson;
import structures.Ticket;
import utils.DeserializerUtil;
import utils.HttpRequestCreatorUtil;
import vehicles.VehicleJson;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

public class HttpClientWrapper {
    private HttpClient httpClient;

    public HttpClientWrapper() {
        httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    public Ticket generateParkingTicketFromUri(String uri, VehicleJson vehicleJson) throws ParkingLotGeneralException {
        HttpRequest request = HttpRequestCreatorUtil.createPostRequestWithBody(uri, vehicleJson);
        HttpResponse<String> response = null;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new HttpRequestException("Http Request exception when trying to generate a new parking ticket! (Generate parking ticket)");
        }

        if(response.statusCode() == 200) {
            Ticket ticket = DeserializerUtil.getTicket(response.body());
            return ticket;
        } else {
            ExceptionJson exceptionJson = null;
            try {
                exceptionJson = DeserializerUtil.getExceptionJson(response.body());
            } catch (JsonProcessingException e) {
                throw new ParkingLotGeneralException("Error while processing JSON received from the REST microservice!");
            }

            switch(exceptionJson.getMessage()) {
                case "notFound": throw new ParkingSpotNotFoundException("The parking spot with the given id does not exist!");
                case "notAvailable": throw new ParkingSpotNotAvailableException("There is no free parking spot available for the current vehicle!");
                case "tooExpensive": throw new VehicleTooExpensiveException();
                case "optimisticLocking": throw new SimultaneousOperationInDatabaseCollectionException("Attempt to perform multiple operations at the same time. Try again!");
                default: throw new ParkingLotGeneralException("Unknown error!");
            }
        }
    }

    public Ticket updateParkingSpotWhenVehicleLeavesFromUri(String uri, ParkingSpot parkingSpot) throws ParkingLotGeneralException {
        HttpRequest request = HttpRequestCreatorUtil.createPostRequestWithBody(uri, parkingSpot);
        HttpResponse<String> response = null;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new HttpRequestException("Http Request Exception when trying to free a parking spot!");
        }

        if(response.statusCode() == 200) {
            Ticket ticket = DeserializerUtil.getTicket(response.body());
            return ticket;
        } else {
            ExceptionJson exceptionJson = null;
            try {
                exceptionJson = DeserializerUtil.getExceptionJson(response.body());
            } catch (JsonProcessingException e) {
                throw new ParkingLotGeneralException("Error while processing JSON received from the REST microservice!");
            }

            switch(exceptionJson.getMessage()) {
                case "notFound": throw new ParkingSpotNotFoundException("The parking spot with the given id does not exist!");
                case "notOccupied": throw new ParkingSpotNotOccupiedException("The parking spot with the given id is not occupied!");
                case "optimisticLocking": throw new SimultaneousOperationInDatabaseCollectionException("Attempt to perform multiple operations at the same time. Try again!");
                default: throw new ParkingLotGeneralException("Unknown error!");
            }
        }
    }

    public List<ParkingSpot> getParkingSpotsFromUri(String uri) throws HttpRequestException {
        HttpRequest request = HttpRequestCreatorUtil.createGetRequest(uri);
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            List<ParkingSpot> parkingSpots = DeserializerUtil.getParkingSpots(response.body());
            return parkingSpots;
        } catch (IOException | InterruptedException e) {
            throw new HttpRequestException("Http Request exception when trying to get all the parking spots! (getParkingSpots)");
        }
    }

    public List<Ticket> getTicketsFromUri(String uri) throws HttpRequestException {
        HttpRequest request = HttpRequestCreatorUtil.createGetRequest(uri);
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            List<Ticket> tickets = DeserializerUtil.getTickets(response.body());
            return tickets;
        } catch (IOException | InterruptedException e) {
            throw new HttpRequestException("Http Request exception when trying to get all the parking tickets! (Get tickets)");
        }
    }

}
