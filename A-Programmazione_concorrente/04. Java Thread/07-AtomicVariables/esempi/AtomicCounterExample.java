package esempi;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Esempio di utilizzo di AtomicInteger per implementare un contatore thread-safe.
 * Questo esempio confronta un contatore non sincronizzato con un contatore atomico
 * in un ambiente multi-thread.
 */
public class AtomicCounterExample {
    // Contatore non sincronizzato
    private static int unsafeCounter = 0;
    
    // Contatore atomico
    private static AtomicInteger atomicCounter = new AtomicInteger(0);
    
    // Numero di thread e incrementi per thread
    private static final int NUM_THREADS = 10;
    private static final int INCREMENTS_PER_THREAD = 1000;
    
    public static void main(String[] args) throws InterruptedException {
        // Creazione di un pool di thread
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        
        // Esecuzione dei task di incremento
        for (int i = 0; i < NUM_THREADS; i++) {
            executor.submit(() -> {
                for (int j = 0; j < INCREMENTS_PER_THREAD; j++) {
                    // Incremento non sicuro
                    unsafeCounter++;
                    
                    // Incremento atomico
                    atomicCounter.incrementAndGet();
                }
            });
        }
        
        // Attesa del completamento di tutti i thread
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        
        // Calcolo del valore atteso
        int expectedValue = NUM_THREADS * INCREMENTS_PER_THREAD;
        
        // Stampa dei risultati
        System.out.println("Valore atteso: " + expectedValue);
        System.out.println("Contatore non sincronizzato: " + unsafeCounter);
        System.out.println("Contatore atomico: " + atomicCounter.get());
        
        // Verifica dei risultati
        System.out.println("\nIl contatore non sincronizzato è corretto? " + 
                          (unsafeCounter == expectedValue));
        System.out.println("Il contatore atomico è corretto? " + 
                          (atomicCounter.get() == expectedValue));
    }
}