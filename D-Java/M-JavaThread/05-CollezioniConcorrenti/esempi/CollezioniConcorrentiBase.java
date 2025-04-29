/**
 * Esempio 01: Utilizzo Base delle Collezioni Concorrenti
 * 
 * Questo esempio dimostra l'utilizzo delle principali collezioni concorrenti di Java:
 * - ConcurrentHashMap
 * - CopyOnWriteArrayList
 * - ConcurrentLinkedQueue
 * - BlockingQueue (LinkedBlockingQueue)
 */
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CollezioniConcorrentiBase {
    // Numero di thread per i test
    private static final int NUM_THREADS = 5;
    // Numero di operazioni per thread
    private static final int OPERATIONS_PER_THREAD = 1000;
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Dimostrazione dell'utilizzo delle Collezioni Concorrenti");
        System.out.println("====================================================");
        
        // Esempio 1: ConcurrentHashMap vs HashMap
        System.out.println("\n1. ConcurrentHashMap vs HashMap:");
        testConcurrentHashMap();
        
        // Esempio 2: CopyOnWriteArrayList vs ArrayList
        System.out.println("\n2. CopyOnWriteArrayList vs ArrayList:");
        testCopyOnWriteArrayList();
        
        // Esempio 3: ConcurrentLinkedQueue
        System.out.println("\n3. ConcurrentLinkedQueue:");
        testConcurrentLinkedQueue();
        
        // Esempio 4: BlockingQueue (LinkedBlockingQueue)
        System.out.println("\n4. BlockingQueue (LinkedBlockingQueue):");
        testBlockingQueue();
        
        System.out.println("\nVantaggi dell'utilizzo delle Collezioni Concorrenti:");
        System.out.println("1. Thread-safety senza bisogno di sincronizzazione esplicita");
        System.out.println("2. Migliori prestazioni in scenari concorrenti");
        System.out.println("3. Nessun rischio di ConcurrentModificationException");
        System.out.println("4. Implementazioni ottimizzate per casi d'uso specifici");
    }
    
    /**
     * Test di confronto tra ConcurrentHashMap e HashMap
     */
    private static void testConcurrentHashMap() throws InterruptedException {
        // HashMap standard (non thread-safe)
        final Map<Integer, String> hashMap = new HashMap<>();
        // ConcurrentHashMap (thread-safe)
        final Map<Integer, String> concurrentHashMap = new ConcurrentHashMap<>();
        
        System.out.println("Test con ConcurrentHashMap (thread-safe):");
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        
        // Contatore atomico per tracciare le operazioni completate
        AtomicInteger successCounter = new AtomicInteger(0);
        
        // Avvia più thread che scrivono contemporaneamente sulla mappa
        for (int i = 0; i < NUM_THREADS; i++) {
            final int threadId = i;
            executor.execute(() -> {
                try {
                    for (int j = 0; j < OPERATIONS_PER_THREAD; j++) {
                        int key = threadId * OPERATIONS_PER_THREAD + j;
                        concurrentHashMap.put(key, "Valore-" + key);
                        successCounter.incrementAndGet();
                    }
                } catch (Exception e) {
                    System.err.println("Errore in ConcurrentHashMap: " + e.getMessage());
                }
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
        
        System.out.println("- Operazioni completate con successo: " + successCounter.get());
        System.out.println("- Dimensione finale della ConcurrentHashMap: " + concurrentHashMap.size());
        
        // Nota: Non eseguiamo lo stesso test con HashMap standard perché
        // causerebbe probabilmente una ConcurrentModificationException
        System.out.println("\nNota: HashMap standard non è thread-safe e causerebbe eccezioni");
        System.out.println("se utilizzata da più thread senza sincronizzazione esterna.");
    }
    
    /**
     * Test di confronto tra CopyOnWriteArrayList e ArrayList
     */
    private static void testCopyOnWriteArrayList() throws InterruptedException {
        // ArrayList standard (non thread-safe)
        final List<Integer> arrayList = new ArrayList<>();
        // CopyOnWriteArrayList (thread-safe)
        final List<Integer> copyOnWriteList = new CopyOnWriteArrayList<>();
        
        System.out.println("Test con CopyOnWriteArrayList (thread-safe):");
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        
        // Contatore atomico per tracciare le operazioni completate
        AtomicInteger successCounter = new AtomicInteger(0);
        
        // Avvia più thread che scrivono contemporaneamente sulla lista
        for (int i = 0; i < NUM_THREADS; i++) {
            executor.execute(() -> {
                try {
                    for (int j = 0; j < 200; j++) { // Meno operazioni perché CopyOnWrite è più lento
                        copyOnWriteList.add(j);
                        successCounter.incrementAndGet();
                    }
                } catch (Exception e) {
                    System.err.println("Errore in CopyOnWriteArrayList: " + e.getMessage());
                }
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
        
        System.out.println("- Operazioni completate con successo: " + successCounter.get());
        System.out.println("- Dimensione finale della CopyOnWriteArrayList: " + copyOnWriteList.size());
        System.out.println("\nNota: CopyOnWriteArrayList è ottimale quando le letture sono molto più frequenti delle scritture.");
    }
    
    /**
     * Test di ConcurrentLinkedQueue
     */
    private static void testConcurrentLinkedQueue() throws InterruptedException {
        // Coda concorrente non bloccante
        final Queue<Integer> concurrentQueue = new ConcurrentLinkedQueue<>();
        
        System.out.println("Test con ConcurrentLinkedQueue (non bloccante):");
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        
        // Avvia thread produttori
        for (int i = 0; i < NUM_THREADS / 2; i++) {
            final int threadId = i;
            executor.execute(() -> {
                for (int j = 0; j < 500; j++) {
                    concurrentQueue.offer(threadId * 1000 + j);
                }
                System.out.println("Produttore " + threadId + " ha completato");
            });
        }
        
        // Avvia thread consumatori
        for (int i = 0; i < NUM_THREADS / 2; i++) {
            final int threadId = i;
            executor.execute(() -> {
                int elementsConsumed = 0;
                while (elementsConsumed < 500 || !concurrentQueue.isEmpty()) {
                    Integer value = concurrentQueue.poll();
                    if (value != null) {
                        elementsConsumed++;
                    }
                }
                System.out.println("Consumatore " + threadId + " ha completato");
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
        
        System.out.println("- Elementi rimanenti nella coda: " + concurrentQueue.size());
    }
    
    /**
     * Test di BlockingQueue (LinkedBlockingQueue)
     */
    private static void testBlockingQueue() throws InterruptedException {
        // Coda bloccante con capacità limitata
        final BlockingQueue<Integer> blockingQueue = new LinkedBlockingQueue<>(100);
        
        System.out.println("Test con LinkedBlockingQueue (bloccante):");
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        
        // Contatori per statistiche
        AtomicInteger produced = new AtomicInteger(0);
        AtomicInteger consumed = new AtomicInteger(0);
        
        // Avvia thread produttori
        for (int i = 0; i < NUM_THREADS / 2; i++) {
            executor.execute(() -> {
                try {
                    for (int j = 0; j < 300; j++) {
                        blockingQueue.put(j); // Si blocca se la coda è piena
                        produced.incrementAndGet();
                        Thread.sleep(5); // Rallenta il produttore
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        
        // Avvia thread consumatori
        for (int i = 0; i < NUM_THREADS / 2; i++) {
            executor.execute(() -> {
                try {
                    while (!Thread.currentThread().isInterrupted()) {
                        blockingQueue.take(); // Si blocca se la coda è vuota
                        consumed.incrementAndGet();
                        Thread.sleep(10); // Consumatore più lento del produttore
                    }
                } catch (InterruptedException e) {
                    // Termina normalmente quando l'executor viene chiuso
                }
            });
        }
        
        // Lascia che produttori e consumatori lavorino per un po'
        Thread.sleep(3000);
        
        executor.shutdownNow(); // Interrompe tutti i thread
        executor.awaitTermination(2, TimeUnit.SECONDS);
        
        System.out.println("- Elementi prodotti: " + produced.get());
        System.out.println("- Elementi consumati: " + consumed.get());
        System.out.println("- Elementi rimanenti nella coda: " + blockingQueue.size());
    }
}