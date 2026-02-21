package org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.api;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.utils.JPAUtil;

import jakarta.persistence.EntityManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Tests de charge simples (Ex 10.8)
 * Nécessite que le serveur soit lancé sur http://localhost:8080/demo
 */
public class SimpleLoadTest {

    @Test
    @Tag("load-test")
    public void testConcurrentRequests() throws InterruptedException {
        int threads = 10;
        int requestsPerThread = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        AtomicInteger successCount = new AtomicInteger(0);

        for (int i = 0; i < threads * requestsPerThread; i++) {
            executor.submit(() -> {
                try {
                    // Simule un accès métier (industrialisation)
                    org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.services.AnnonceService service = new org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.services.AnnonceService();
                    service.getAllAnnonces();
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    System.err.println("Load test failed for one request: " + e.getMessage());
                }
            });
        }

        executor.shutdown();
        boolean finished = executor.awaitTermination(15, TimeUnit.SECONDS);

        System.out
                .println("Simple Load Test completed: " + successCount.get() + " service calls executed concurrently.");
        org.junit.jupiter.api.Assertions.assertTrue(successCount.get() > 0);
        org.junit.jupiter.api.Assertions.assertTrue(finished, "Le test de charge a expiré");
    }
}
