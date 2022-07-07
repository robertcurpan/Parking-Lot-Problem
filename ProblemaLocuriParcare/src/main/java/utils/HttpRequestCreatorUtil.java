package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import vehicles.VehicleJson;

import java.net.URI;
import java.net.http.HttpRequest;

public class HttpRequestCreatorUtil {

    public static HttpRequest createGetRequest(String uri) {
        return HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(uri))
                .build();
    }

    public static HttpRequest createPostRequest(String uri) {
        return HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
    }

    public static HttpRequest createPostRequestWithVehicleBody(String uri, VehicleJson vehicleJson) {
        String requestBody = null;
        try {
            requestBody = new ObjectMapper().writeValueAsString(vehicleJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
    }

}
