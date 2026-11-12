package com.shitanshu.shopping.concurrency;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertTrue;

class LoadAndStressTest {

    @Test
    void simulateHighLoadAndStressTraffic() throws InterruptedException {
        int totalRequests = 200;
        int concurrentUsers = 20;
        ExecutorService executorService = Executors.newFixedThreadPool(concurrentUsers);
        
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(2))
                .build();

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < totalRequests; i++) {
            executorService.submit(() -> {
                try {
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create("http://localhost:8080/api/products"))
                            .GET()
                            .build();

                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    if (response.statusCode() == 200 || response.statusCode() == 401 || response.statusCode() == 403) {
                        successCount.incrementAndGet();
                    } else {
                        failureCount.incrementAndGet();
                    }
                } catch (Exception e) {
                    failureCount.incrementAndGet();
                }
            });
        }

        executorService.shutdown();
        boolean finished = executorService.awaitTermination(15, TimeUnit.SECONDS);
        long endTime = System.currentTimeMillis();

        assertTrue(finished, "Load test timed out");
        System.out.println("=== LOAD & STRESS TEST REPORT ===");
        System.out.println("Total Requests Sent: " + totalRequests);
        System.out.println("Successful/Handled Responses: " + successCount.get());
        System.out.println("Failed/Dropped Responses: " + failureCount.get());
        System.out.println("Total Time Taken: " + (endTime - startTime) + " ms");
    }
}
