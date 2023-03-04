package org.example.client;

import java.io.IOException;
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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.example.model.LiftRide;

import com.google.gson.Gson;

public class SkiRideUploader {

    private static int NUM_THREADS = 100;
    private static  int MAX_POSTS_PER_THREAD = 100;
    private static int MAX_REQUESTS = 10000;
    private static  int TOTAL_POSTS = NUM_THREADS * MAX_POSTS_PER_THREAD;
    private final HttpClient client;
    private final String endpoint;
    private final List<LiftRide> liftRides;
    private final Random ran;
    private final Gson gson;
    
    
    private AtomicInteger numSentRequests = new AtomicInteger(0);
    
    
    
    
    private AtomicInteger numRequestsSent;
    
    
    private int numUnSuccessfulRequests;
    private int numSuccessfulRequests;
    private long startTime = 0;
    private long endTime = 0;
    private long elapsedTime = 0;
    private long latency = 0;
    private List<Long> responseTimes;
    
    private long p99ResponseTime;

    public SkiRideUploader(String endpoint) {
        this.client = HttpClient.newHttpClient();
        this.endpoint = endpoint;
        this.liftRides = Collections.synchronizedList(new ArrayList<>());
        this.ran = new Random();
        this.gson = new Gson();
        this.numUnSuccessfulRequests = 0;
        this.numSentRequests = new AtomicInteger(0);
        this.numRequestsSent = new AtomicInteger(0);
        this.responseTimes = Collections.synchronizedList(new ArrayList<>());
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
        startTime = System.currentTimeMillis();
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        CountDownLatch latch = new CountDownLatch(NUM_THREADS);
        
        for (int i = 0; i < NUM_THREADS; i++) {
        	executor.submit(new SkiRidePoster(latch));
        }
        
        // Wait for all threads to finish
        
        latch.await();
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        
        endTime = System.currentTimeMillis();
        elapsedTime = endTime - startTime;
        
        long totalResponseTime = 0;
        for (long responseTime : responseTimes) {
            totalResponseTime += responseTime;
        }
        double meanResponseTime = (double) totalResponseTime / responseTimes.size();

        Collections.sort(responseTimes);
        double medianResponseTime;
        if (responseTimes.size() % 2 == 0) {
            medianResponseTime = ((double) responseTimes.get(responseTimes.size() / 2) +
                                  (double) responseTimes.get(responseTimes.size() / 2 - 1)) / 2;
        } else {
            medianResponseTime = (double) responseTimes.get(responseTimes.size() / 2);
        }
        p99ResponseTime = responseTimes.get((int) (responseTimes.size() * 0.99));


        double throughput = (double) numSentRequests.get() / ((double) elapsedTime / 1000);
        
        
        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.println("");
        
        
        System.out.println("Number of threads: " + NUM_THREADS + " and " + "Post requests per thread: " + MAX_POSTS_PER_THREAD);
        System.out.println("Total elapsed time: " + elapsedTime + " milliseconds");
        System.out.println("Number of unsuccessful requests: " + numUnSuccessfulRequests);
        System.out.println("Number of successful requests: " + numSentRequests.get());  //(MAX_REQUESTS - numUnSuccessfulRequests)
        System.out.println("Total throughput: " + throughput + " requests per second");
        System.out.println("Latency: " + latency + " miliseconds");
        System.out.println("Mean response time: " + meanResponseTime + " milliseconds");
        System.out.println("Median response time: " + medianResponseTime + " milliseconds");
        System.out.println("p99: " + p99ResponseTime + " milliseconds");
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
            int numFailPosts = 0;

            while (numSentRequests.get() < MAX_REQUESTS && numFailPosts < MAX_POSTS_PER_THREAD) {
            	LiftRide liftRide = null;
            	synchronized (liftRides) {
            		if(!liftRides.isEmpty()) {
            			liftRide = liftRides.remove(0);
            		}
            	}
            	

                if (liftRide != null) {
                	
                    String json = gson.toJson(liftRide);
                    System.out.println(json);
                    long sTime = System.currentTimeMillis();
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(endpoint))
                            .header("Content-Type", "application/json")
                            .POST(HttpRequest.BodyPublishers.ofString(json))
                            .build();
	                try {
                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    long eTime = System.currentTimeMillis();
                    latency = eTime - sTime;
                    if (response.statusCode() == 201) {
                    	long responseTime = eTime - sTime;
                    	responseTimes.add(responseTime);

                        numSuccessfulRequests++;
                    } else {
                        numUnSuccessfulRequests++;
                        numFailPosts++;
                    }
                } catch (IOException | InterruptedException e) {
                    numUnSuccessfulRequests++;
                    numFailPosts++;
                }
                numSentRequests.incrementAndGet();
                
            }
                
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
    	
    	String url = "http://168.138.67.254:8080/coen6317/skiers/" + rId + "/seasons/" + season + "/days/" + dId + "/skiers/" + sId;
        SkiRideUploader uploader = new SkiRideUploader(url);
        try {
            uploader.run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


