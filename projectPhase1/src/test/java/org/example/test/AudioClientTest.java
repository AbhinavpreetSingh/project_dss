package org.example.test;

//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.Matchers.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//import java.nio.charset.StandardCharsets;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;
//
//import org.junit.jupiter.api.Test;
//
//import com.google.gson.Gson;
//
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.StringEntity;
//
//import org.apache.commons.io.IOUtils;
//import org.eclipse.jetty.client.HttpClient;
//import org.eclipse.jetty.client.HttpResponse;
//import org.eclipse.jetty.client.api.ContentProvider;
//import org.eclipse.jetty.client.api.ContentResponse;
//import org.eclipse.jetty.client.api.Request;
//import org.eclipse.jetty.client.util.StringContentProvider;
//import org.eclipse.jetty.http.HttpHeader;
//
//
//class AudioClientTest {
//	
//	
//}


//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.Matchers.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.net.URI;
//import java.net.http.HttpRequest;
//import java.nio.charset.StandardCharsets;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.TimeoutException;
//
//import org.junit.jupiter.api.Test;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.gson.Gson;
//
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.StringEntity;
//
//import org.apache.commons.io.IOUtils;
//import org.eclipse.jetty.client.HttpClient;
//import org.eclipse.jetty.client.HttpResponse;
//import org.eclipse.jetty.client.api.ContentResponse;
//import org.eclipse.jetty.client.api.Request;
//import org.eclipse.jetty.client.util.StringContentProvider;
//import org.eclipse.jetty.http.HttpHeader;
//import org.eclipse.jetty.http.HttpMethod;
//import org.eclipse.jetty.http.MimeTypes;
//import org.eclipse.jetty.util.ssl.SslContextFactory;
//import org.eclipse.jetty.util.thread.QueuedThreadPool;
//import org.example.model.Audio;
//
//
//class AudioClientTest {
//	
//	void runConcurrentRequestsFromMultipleClients(int num_clients, int ratio) throws Exception {
////		final String uri = "http://155.248.230.86:8080/audios";
//		final String uri = "http://localhost:9090/coen6317/audios";
//		
//
//		
//		ExecutorService executor = Executors.newFixedThreadPool(num_clients);
//		HttpClient client = new HttpClient();
//		client.start();
//		
//		List<Long> roundTimes = new ArrayList<>();
//		for(int i = 0; i < num_clients; i++) {
////			int clientID = i + 1;
//			executor.execute(()->{
//				// get request
//				for (int j = 0; j < ratio; j++) {
//					try {
//						// calculate round-time
//						long startTime = System.currentTimeMillis();
//						
//						ContentResponse res = client.GET(uri);
//						assertThat(res.getStatus(), equalTo(200));
////						System.out.println(res.getContentAsString());
////						System.out.println(res.getStatus());
//						
//						long roundTime = System.currentTimeMillis() - startTime;
//						roundTimes.add(roundTime);
// 
//					} catch (InterruptedException | ExecutionException | TimeoutException e) {
//						System.out.print(e.getMessage());
//					}
//				}
//			
//				// post request
//		    	Request request = client.newRequest("http://localhost:9090/coen6317/audios")
//		                .method(HttpMethod.POST)
//		                .param("id", "87")
//		                .param("name", "John Doe")
//		                .param("track_title", "track_title_87")
//		                .param("album_title", "album_title_87")
//		                .param("track_number", "67")
//		                .param("year", "2021")
//		                .param("reviews", "7")
//		                .param("sold", "9");
//
//		        try {
//					ContentResponse response = request.send();
//					assertThat(response.getStatus(), equalTo(200));
//				} catch (InterruptedException | TimeoutException | ExecutionException e) {
//					e.printStackTrace();
//				}
//
//			});
//		}
//	
//		executor.shutdown();
//		executor.awaitTermination(10, TimeUnit.MINUTES);
//		long totalRoundTime = roundTimes.stream().mapToLong(Long::longValue).sum();
//		System.out.println("The number of clients: " + num_clients + ", ratio: " + ratio + ":1, round-trip time: " +totalRoundTime + "ms");
//	}
//	
//	@Test
//	void testAudios10Clients2ratio() throws Exception {
//		runConcurrentRequestsFromMultipleClients(10,2);
//	}
//	@Test
//	void testAudios50Clients2ratio() throws Exception {
//		runConcurrentRequestsFromMultipleClients(50,2);
//	}
//	@Test
//	void testAudios100Clients2ratio() throws Exception {
//		runConcurrentRequestsFromMultipleClients(100,2);
//	}
//	@Test
//	void testAudios10Clients5ratio() throws Exception {
//		runConcurrentRequestsFromMultipleClients(10,5);
//	}
//	@Test
//	void testAudios50Clients5ratio() throws Exception {
//		runConcurrentRequestsFromMultipleClients(50,5);
//	}
//	@Test
//	void testAudios100Clients5ratio() throws Exception {
//		runConcurrentRequestsFromMultipleClients(100,5);
//	}
//	@Test
//	void testAudios10Clients10ratio() throws Exception {
//		runConcurrentRequestsFromMultipleClients(10,10);
//	}
//	@Test
//	void testAudios50Clients10ratio() throws Exception {
//		runConcurrentRequestsFromMultipleClients(50,10);
//	}
//	@Test
//	void testAudios100Clients10ratio() throws Exception {
//		runConcurrentRequestsFromMultipleClients(100,10);
//	}
//	@Test
//	void testAudios10Clients20ratio() throws Exception {
//		runConcurrentRequestsFromMultipleClients(10,20);
//	}
//	@Test
//	void testAudios50Clients20ratio() throws Exception {
//		runConcurrentRequestsFromMultipleClients(50,20);
//	}
//	@Test
//	void testAudios100Clients20ratio() throws Exception {
//		runConcurrentRequestsFromMultipleClients(100,20);
//	}
//
//}
//
//
//
//
//
////	@Test
////	void testHelloServletGet() throws Exception {
////		
////		HttpClient client = new HttpClient();
////        client.start();
////
////        ContentResponse res = client.GET("http://localhost:9090/coen6317/HelloServlet");
////        
////        System.out.println(res.getContentAsString());
////        
////        client.stop();
////		
////	}
////	
////	
////	@Test
////	void testBlockingServletGet() throws Exception {
////		
////		HttpClient client = new HttpClient();
////        client.start();
////
////        ContentResponse res = client.GET("http://localhost:9090/coen6317/BlockingServlet");
////        
////        System.out.println(res.getContentAsString());
////        
////        client.stop();
////		
////	}
////	
////	@Test
////	void testAsyncServletGet() throws Exception {
////		
////		String url = "http://localhost:9090/coen6317/longtask";
////		HttpClient client = new HttpClient();
////        client.start();
////
////        ContentResponse response = client.GET(url);
////
////		assertThat(response.getStatus(), equalTo(200));
////		
////		String responseContent = IOUtils.toString(response.getContent());
////		
////		 System.out.println(responseContent);
////		//assertThat(responseContent, equalTo( "This is some heavy resource that will be served in an async way"));
////		
////	}
////
////	
////	@Test
////	void testArtistsGet() throws Exception {
////		String url = "http://localhost:9090/coen6317/artists";
////		HttpClient client = new HttpClient();
////        client.start();
////
////        Request request = client.newRequest(url);
////        request.param("id","id200");
////        ContentResponse response = request.send();
////   
////
////		assertThat(response.getStatus(), equalTo(200));
////		
////		String responseContent = IOUtils.toString(response.getContent());
////		
////		 System.out.println(responseContent);
////		client.stop();
////		
////	}
////	
////	@SuppressWarnings("deprecation")
////	@Test
////	void testArtistsPost() throws Exception {
////		
////		String url = "http://localhost:9090/coen6317/artists";
////		HttpClient client = new HttpClient();
////        client.start();
////        
////        Request request = client.POST(url);
////        
////        request.param("id","id200");
////        request.param("name","artist200");
////        
////        ContentResponse response = request.send();
////		String res = new String(response.getContent());
////		System.out.println(res);
////		client.stop();
//	}