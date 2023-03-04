package org.example.client;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SkiRideClient implements Runnable {

    private final HttpClient client;
    private final String data;
    private final String endpoint;
    
    public SkiRideClient(String endpoint, String data) {
        this.client = HttpClient.newHttpClient();
        this.endpoint = endpoint;
        this.data = data;
    }

    @Override
    public void run() {
        try {
            // Configure the request to upload a day of lift rides
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(endpoint))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(data))
                    .build();

            // Send the request to the server
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Handle the server's response
            System.out.println(response.body());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}

