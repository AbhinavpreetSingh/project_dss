package org.example.client;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.example.model.LiftRide;

import com.google.gson.Gson;

public class SkierMultiPost {

    private static final int NUM_THREADS = 32;
    private static final int MAX_POSTS_PER_THREAD = 1000;
    private static final int TOTAL_POSTS = NUM_THREADS * MAX_POSTS_PER_THREAD;
    private static final int MAX_REQUESTS = 10000;
    
    private final HttpClient client;
    private final String endpoint;
    private final List<LiftRide> liftRides;
    private final Random ran;
    private final Gson gson;
    private final AtomicInteger numRequestsSent;
    
    
    
    
    private int numUnSuccessfulRequests;

    public SkierMultiPost(String endpoint) {
        this.client = HttpClient.newHttpClient();
        this.endpoint = endpoint;
        this.liftRides = Collections.synchronizedList(new ArrayList<>(MAX_REQUESTS));
        this.ran = new Random();
        this.gson = new Gson();
        this.numRequestsSent = new AtomicInteger(0);
        this.numUnSuccessfulRequests = 0;
    }

    public void run() throws InterruptedException {
        // Generate lift ride events
        for (int i = 0; i < TOTAL_POSTS; i++) {
            int liftId = ran.nextInt(40) + 1;
            int time = ran.nextInt(360) + 1;
            LiftRide liftRide = new LiftRide(liftId, time);
            liftRides.add(liftRide);
        }

        // Create threads to each send POST requests
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        CountDownLatch latch = new CountDownLatch(NUM_THREADS);
        long startTime = System.currentTimeMillis();
        
        
        for (int i = 0; i < NUM_THREADS; i++) {
            executor.submit(new SkiRidePoster(latch));
        }

        // Wait for all threads to finish
        while (numRequestsSent.get() != MAX_REQUESTS) {
        	latch.await();executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        }
        
        

        // Print total elapsed time
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        double throughput = (double) TOTAL_POSTS / ((double) elapsedTime / 1000);
        System.out.println("Total elapsed time: " + elapsedTime + " milliseconds");
        System.out.println("Number of unsuccessful requests: " + numUnSuccessfulRequests);
        System.out.println("Number of successful requests: " + MAX_REQUESTS);
        System.out.println("Number of Total requests: " + (MAX_REQUESTS - numUnSuccessfulRequests));
        System.out.println("Total throughput: " + throughput + " requests per second");
        try {
            client.connectTimeout();
        } catch (Exception e) {
            Logger.getLogger(SkiRideUploader.class.getName()).log(Level.SEVERE, "Failed to close HttpClient", e);
        }
    }

    private class SkiRidePoster implements Runnable {
        private final CountDownLatch latch;

        public SkiRidePoster(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void run() {
//            int numPosted = 0;

            while (numRequestsSent.get() < MAX_REQUESTS) {
            	LiftRide liftRide = null;
            	if(!liftRides.isEmpty())
            		 liftRide = liftRides.remove(0);
            	

                if (liftRide != null) {
                    String json = gson.toJson(liftRide);
                    System.out.println(json);
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(endpoint))
                            .header("Content-Type", "application/json")
                            .POST(HttpRequest.BodyPublishers.ofString(json))
                            .build();

                    int retryCount = 0;
                    try {
	                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
	                    System.out.println(response.statusCode());
	                    while (retryCount < 5 && response.statusCode() >= 400) {
	                        	System.out.println("Before HttpResponse block");
	                            System.out.println("Response status code: " + response.statusCode());

	                            if (response.statusCode() == 201) {
//	                                numPosted++;
	                                
	                                numUnSuccessfulRequests = numRequestsSent.get();
	                                break;
	                            }
	                    }
                    } catch (Exception e) {
                    	retryCount++;
                    }
                } else {
                    break;
                }
                numRequestsSent.incrementAndGet();
            }
            
            latch.countDown();
        }
    }
    
    public static void main(String[] args) {
    	Random ran = new Random();
    	int resortId = ran.nextInt(10) + 1;
    	String rId = Integer.toString(resortId);
    	int skierId = ran.nextInt(100000) + 1;
    	String sId = Integer.toString(skierId);
    	int seasonId = 2022;
    	String season = Integer.toString(seasonId);
    	int dayId = ran.nextInt(7) + 1;
    	String dId = Integer.toString(dayId);
    	
    	String url = "http://localhost:9090/coen6317/skiers/" + rId + "/seasons/" + season + "/days/" + dId + "/skiers/" + sId;
    	System.out.println(url);
    	
        SkiRideUploader uploader = new SkiRideUploader(url);
        try {
            uploader.run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


