package org.example.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.example.model.LiftRide;
import org.junit.jupiter.api.Test;

class SkierApiTest {

    
	@Test
	public void testLatency() throws IOException, InterruptedException {
		
	    String url = "http://localhost:9090/coen6317/skiers/1/seasons/2022/days/4/skiers/1234";
	    String requestBody = "{\"liftID\":\"9\", \"time\":\"234\"}";
	    HttpClient client = HttpClient.newHttpClient();
	    long[] latencies = new long[500];
	    for (int i = 0; i < 500; i++) {
	        long startTime = System.currentTimeMillis();
	        HttpRequest request = HttpRequest.newBuilder()
	                .uri(URI.create(url))
	                .header("Content-Type", "application/json")
	                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
	                .build();
	        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
	        long endTime = System.currentTimeMillis();
	        long latency = endTime - startTime;
	        latencies[i] = latency;
	        System.out.println("Request " + (i+1) + " took " + latency + " miliseconds");
	        assertEquals(201, response.statusCode());
	    }
	    // Compute and print average latency
	    long totalLatency = 0;
	    for (int i = 0; i < 500; i++) {
	        totalLatency += latencies[i];
	    }
	    double averageLatency = (double) totalLatency / 500;
	    double thoughput = (double) 500 / (averageLatency / 1000);
	    System.out.println("Average latency: " + averageLatency + " miliseconds");
	    System.out.println("Throughput: " + thoughput + " requests per second");
	    
	    
	}

}
