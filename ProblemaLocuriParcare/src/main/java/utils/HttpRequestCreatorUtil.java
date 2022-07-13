package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpRequest;

public class HttpRequestCreatorUtil {

    public static HttpRequest createGetRequest(String uri) {
        return HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(uri))
                .build();
    }

    public static HttpRequest createPostRequestWithBody(String uri, Object objectBody) {
        String requestBody = null;
        try {
            requestBody = new ObjectMapper().writeValueAsString(objectBody);
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
