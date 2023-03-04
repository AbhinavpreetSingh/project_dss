package org.example.client;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.example.model.LiftRide;

import com.google.gson.Gson;

public class JavaClient {
	private final static Random ran = new Random();
	public static void main(String[] args) {
        String endpoint = "http://localhost:9090/coen6317/skiers/1/seasons/2022/days/7/skiers/1234";
        
//        int liftId = ran.nextInt(40) + 1;
//        int time = ran.nextInt(360) + 1;
//        LiftRide liftRide = new LiftRide(liftId, time);
//        Gson gson = new Gson();
//        String json = gson.toJson(liftRide);
        // Create a thread pool and start multiple instances of the client
        int numClients = 100;
        ExecutorService executor = Executors.newFixedThreadPool(numClients);
        for (int i = 0; i < numClients; i++) {
        	int liftId = ran.nextInt(40) + 1;
            int time = ran.nextInt(360) + 1;
            LiftRide liftRide = new LiftRide(liftId, time);
            Gson gson = new Gson();
            String json = gson.toJson(liftRide);
            
        	long startTime = System.currentTimeMillis();
            executor.submit(new SkiRideClient(endpoint, json));
            
            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            System.out.println("Client " + i + " = " + liftId + " in " + time);
            System.out.println("Elapsed time: " + elapsedTime + " milliseconds");
            
        }

        // Wait for all clients to finish
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
	}

}
