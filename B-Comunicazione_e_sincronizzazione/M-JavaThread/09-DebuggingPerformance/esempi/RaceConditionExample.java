package esempi;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Questo esempio dimostra come si verificano le race condition e come risolverle.
 * Vengono confrontate tre implementazioni di un contatore:
 * 1. Non sincronizzato (soggetto a race condition)
 * 2. Sincronizzato con synchronized
 * 3. Utilizzo di AtomicInteger
 */
public class RaceConditionExample {

    public static void main(String[] args) throws InterruptedException {
        // Numero di thread e incrementi per thread
        int numThreads = 10;
        int incrementsPerThread = 100000;
        
        System.out.println("Test di race condition con " + numThreads + " thread, " + 
                           incrementsPerThread + " incrementi per thread");
        
        // Test con contatore non sincronizzato
        testCounter(new UnsafeCounter(), numThreads, incrementsPerThread);
        
        // Test con contatore sincronizzato
        testCounter(new SynchronizedCounter(), numThreads, incrementsPerThread);
        
        // Test con contatore atomico
        testCounter(new AtomicCounter(), numThreads, incrementsPerThread);
    }
    
    /**
     * Esegue un test sul contatore specificato utilizzando più thread.
     */
    private static void testCounter(Counter counter, int numThreads, int incrementsPerThread) 
            throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        CountDownLatch latch = new CountDownLatch(numThreads);
        
        long startTime = System.nanoTime();
        
        // Avvia i thread che incrementano il contatore
        for (int i = 0; i < numThreads; i++) {
            executor.submit(() -> {
                for (int j = 0; j < incrementsPerThread; j++) {
                    counter.increment();
                }
                latch.countDown();
            });
        }
        
        // Attendi che tutti i thread completino
        latch.await();
        long endTime = System.nanoTime();
        
        // Calcola il valore atteso e verifica se ci sono state race condition
        int expectedValue = numThreads * incrementsPerThread;
        int actualValue = counter.getValue();
        boolean hasRaceCondition = actualValue != expectedValue;
        
        System.out.println("\n" + counter.getClass().getSimpleName() + ":");
        System.out.println("Valore atteso: " + expectedValue);
        System.out.println("Valore effettivo: " + actualValue);
        System.out.println("Race condition rilevata: " + hasRaceCondition);
        System.out.println("Tempo di esecuzione: " + ((endTime - startTime) / 1_000_000) + " ms\n");
        
        executor.shutdown();
    }
    
    /**
     * Interfaccia per i contatori.
     */
    interface Counter {
        void increment();
        int getValue();
    }
    
    /**
     * Implementazione non thread-safe di un contatore.
     * Soggetta a race condition quando utilizzata da più thread.
     */
    static class UnsafeCounter implements Counter {
        private int count = 0;
        
        @Override
        public void increment() {
            count++; // Operazione non atomica: leggi, incrementa, scrivi
        }
        
        @Override
        public int getValue() {
            return count;
        }
    }
    
    /**
     * Implementazione thread-safe di un contatore utilizzando synchronized.
     */
    static class SynchronizedCounter implements Counter {
        private int count = 0;
        
        @Override
        public synchronized void increment() {
            count++;
        }
        
        @Override
        public synchronized int getValue() {
            return count;
        }
    }
    
    /**
     * Implementazione thread-safe di un contatore utilizzando AtomicInteger.
     */
    static class AtomicCounter implements Counter {
        private AtomicInteger count = new AtomicInteger(0);
        
        @Override
        public void increment() {
            count.incrementAndGet();
        }
        
        @Override
        public int getValue() {
            return count.get();
        }
    }
    
    /**
     * Metodo di utilità per visualizzare informazioni sulla race condition.
     */
    private static void explainRaceCondition() {
        System.out.println("\nCosa è una Race Condition?\n");
        System.out.println("Una race condition si verifica quando il comportamento di un programma");
        System.out.println("dipende dall'ordine relativo o dalla tempistica di eventi specifici,");
        System.out.println("e questi eventi non vengono adeguatamente sincronizzati.");
        
        System.out.println("\nNel caso dell'incremento di un contatore (count++), l'operazione");
        System.out.println("è composta da tre passaggi distinti:\n");
        System.out.println("1. Leggere il valore attuale di count");
        System.out.println("2. Incrementare il valore letto");
        System.out.println("3. Scrivere il nuovo valore in count\n");
        
        System.out.println("Se due thread eseguono questi passaggi contemporaneamente, possono");
        System.out.println("verificarsi interleaving problematici. Ad esempio:\n");
        System.out.println("- Thread A legge count = 10");
        System.out.println("- Thread B legge count = 10");
        System.out.println("- Thread A incrementa a 11");
        System.out.println("- Thread B incrementa a 11");
        System.out.println("- Thread A scrive count = 11");
        System.out.println("- Thread B scrive count = 11\n");
        
        System.out.println("Risultato: count = 11 invece di 12!\n");
        
        System.out.println("Soluzioni:\n");
        System.out.println("1. Sincronizzazione: utilizzare 'synchronized' per garantire l'accesso esclusivo");
        System.out.println("2. Variabili atomiche: utilizzare AtomicInteger per operazioni atomiche");
        System.out.println("3. Lock espliciti: utilizzare ReentrantLock o altri lock espliciti");
    }
}