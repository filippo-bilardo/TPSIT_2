package esempi;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;

/**
 * Esempio di utilizzo di LongAdder e LongAccumulator per contatori ad alte prestazioni.
 * Questo esempio confronta le prestazioni di AtomicLong, LongAdder e LongAccumulator
 * in scenari di alta contesa tra thread.
 */
public class AdderAccumulatorExample {
    
    // Numero di thread e incrementi per thread
    private static final int NUM_THREADS = 10;
    private static final int INCREMENTS_PER_THREAD = 1_000_000;
    
    public static void main(String[] args) throws InterruptedException {
        // Contatori
        AtomicLong atomicLong = new AtomicLong(0);
        LongAdder longAdder = new LongAdder();
        LongAccumulator longAccumulator = new LongAccumulator(Long::sum, 0);
        
        // Test con AtomicLong
        System.out.println("Test con AtomicLong:");
        long startTime = System.nanoTime();
        testCounter(() -> atomicLong.incrementAndGet());
        long atomicLongTime = System.nanoTime() - startTime;
        System.out.println("Valore finale: " + atomicLong.get());
        System.out.println("Tempo impiegato: " + atomicLongTime / 1_000_000 + " ms\n");
        
        // Test con LongAdder
        System.out.println("Test con LongAdder:");
        startTime = System.nanoTime();
        testCounter(() -> longAdder.increment());
        long longAdderTime = System.nanoTime() - startTime;
        System.out.println("Valore finale: " + longAdder.sum());
        System.out.println("Tempo impiegato: " + longAdderTime / 1_000_000 + " ms\n");
        
        // Test con LongAccumulator
        System.out.println("Test con LongAccumulator:");
        startTime = System.nanoTime();
        testCounter(() -> longAccumulator.accumulate(1));
        long longAccumulatorTime = System.nanoTime() - startTime;
        System.out.println("Valore finale: " + longAccumulator.get());
        System.out.println("Tempo impiegato: " + longAccumulatorTime / 1_000_000 + " ms\n");
        
        // Confronto delle prestazioni
        System.out.println("Confronto delle prestazioni:");
        System.out.println("LongAdder è " + String.format("%.2f", (double)atomicLongTime / longAdderTime) + 
                " volte più veloce di AtomicLong");
        System.out.println("LongAccumulator è " + String.format("%.2f", (double)atomicLongTime / longAccumulatorTime) + 
                " volte più veloce di AtomicLong");
        
        // Dimostrazione di LongAccumulator con funzioni personalizzate
        System.out.println("\nDimostrazione di LongAccumulator con funzioni personalizzate:");
        
        // Accumulatore per trovare il massimo
        LongAccumulator maxAccumulator = new LongAccumulator(Long::max, Long.MIN_VALUE);
        
        // Accumulatore per trovare il minimo
        LongAccumulator minAccumulator = new LongAccumulator(Long::min, Long.MAX_VALUE);
        
        // Accumulatore per calcolare il prodotto
        LongAccumulator productAccumulator = new LongAccumulator((x, y) -> x * y, 1);
        
        // Creazione di un pool di thread
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        
        // Esecuzione dei task
        for (int i = 0; i < NUM_THREADS; i++) {
            final int value = i + 1;
            executor.submit(() -> {
                maxAccumulator.accumulate(value);
                minAccumulator.accumulate(value);
                productAccumulator.accumulate(value);
            });
        }
        
        // Attesa del completamento dei thread
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.SECONDS);
        
        System.out.println("Valore massimo: " + maxAccumulator.get());
        System.out.println("Valore minimo: " + minAccumulator.get());
        System.out.println("Prodotto: " + productAccumulator.get());
    }
    
    /**
     * Metodo per testare le prestazioni di un contatore.
     * 
     * @param incrementAction L'azione di incremento da eseguire
     */
    private static void testCounter(Runnable incrementAction) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        
        for (int i = 0; i < NUM_THREADS; i++) {
            executor.submit(() -> {
                for (int j = 0; j < INCREMENTS_PER_THREAD; j++) {
                    incrementAction.run();
                }
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
    }
}