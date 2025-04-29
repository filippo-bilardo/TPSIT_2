/**
 * Esempio 01: Utilizzo Base di Lock e Condition
 * 
 * Questo esempio dimostra l'utilizzo delle principali classi del package java.util.concurrent.locks:
 * - ReentrantLock
 * - ReadWriteLock
 * - Condition
 */
package esempi;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.Queue;
import java.util.LinkedList;

public class LockConditionBase {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Dimostrazione dell'utilizzo di Lock e Condition");
        System.out.println("=============================================");
        
        // Esempio 1: ReentrantLock
        System.out.println("\n1. ReentrantLock:");
        demoReentrantLock();
        
        // Esempio 2: ReadWriteLock
        System.out.println("\n2. ReadWriteLock:");
        demoReadWriteLock();
        
        // Esempio 3: Condition
        System.out.println("\n3. Condition (Produttore-Consumatore):");
        demoCondition();
        
        System.out.println("\nVantaggi dell'utilizzo di Lock e Condition:");
        System.out.println("1. Maggiore flessibilità rispetto a synchronized");
        System.out.println("2. Possibilità di tentare l'acquisizione del lock senza bloccarsi");
        System.out.println("3. Supporto per lock con timeout");
        System.out.println("4. Distinzione tra lock di lettura e scrittura");
        System.out.println("5. Condizioni multiple per lo stesso lock");
    }
    
    /**
     * Dimostrazione di ReentrantLock
     */
    private static void demoReentrantLock() throws InterruptedException {
        // Contatore condiviso
        final Counter counter = new Counter();
        // Numero di thread
        final int numThreads = 5;
        // Numero di incrementi per thread
        final int incrementsPerThread = 1000;
        
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        
        // Avvia più thread che incrementano il contatore
        for (int i = 0; i < numThreads; i++) {
            executor.execute(() -> {
                for (int j = 0; j < incrementsPerThread; j++) {
                    counter.increment();
                }
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        
        System.out.println("Valore finale del contatore: " + counter.getValue());
        System.out.println("Valore atteso: " + (numThreads * incrementsPerThread));
    }
    
    /**
     * Dimostrazione di ReadWriteLock
     */
    private static void demoReadWriteLock() throws InterruptedException {
        // Risorsa condivisa protetta da ReadWriteLock
        final SharedResource resource = new SharedResource();
        // Numero di thread lettori e scrittori
        final int numReaders = 5;
        final int numWriters = 2;
        
        ExecutorService executor = Executors.newFixedThreadPool(numReaders + numWriters);
        
        // Avvia thread scrittori
        for (int i = 0; i < numWriters; i++) {
            final int writerId = i;
            executor.execute(() -> {
                for (int j = 0; j < 3; j++) {
                    resource.write("Scrittore-" + writerId + ", scrittura #" + j);
                    try {
                        Thread.sleep(100); // Pausa tra le scritture
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
        }
        
        // Avvia thread lettori
        for (int i = 0; i < numReaders; i++) {
            final int readerId = i;
            executor.execute(() -> {
                for (int j = 0; j < 5; j++) {
                    String value = resource.read();
                    System.out.println("Lettore-" + readerId + " ha letto: " + value);
                    try {
                        Thread.sleep(50); // Pausa tra le letture
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
    }
    
    /**
     * Dimostrazione di Condition (pattern Produttore-Consumatore)
     */
    private static void demoCondition() throws InterruptedException {
        // Buffer limitato con condition
        final BoundedBuffer<Integer> buffer = new BoundedBuffer<>(5);
        // Numero di produttori e consumatori
        final int numProducers = 2;
        final int numConsumers = 2;
        
        ExecutorService executor = Executors.newFixedThreadPool(numProducers + numConsumers);
        
        // Avvia thread produttori
        for (int i = 0; i < numProducers; i++) {
            final int producerId = i;
            executor.execute(() -> {
                try {
                    for (int j = 0; j < 10; j++) {
                        int item = producerId * 100 + j;
                        buffer.put(item);
                        System.out.println("Produttore-" + producerId + " ha inserito: " + item);
                        Thread.sleep(100); // Pausa tra le produzioni
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        
        // Avvia thread consumatori
        for (int i = 0; i < numConsumers; i++) {
            final int consumerId = i;
            executor.execute(() -> {
                try {
                    for (int j = 0; j < 10; j++) {
                        int item = buffer.take();
                        System.out.println("Consumatore-" + consumerId + " ha prelevato: " + item);
                        Thread.sleep(200); // Consumatore più lento del produttore
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
    }
    
    /**
     * Classe Counter che utilizza ReentrantLock per la sincronizzazione
     */
    static class Counter {
        private final ReentrantLock lock = new ReentrantLock();
        private int count = 0;
        
        public void increment() {
            lock.lock();
            try {
                count++;
            } finally {
                lock.unlock();
            }
        }
        
        public int getValue() {
            lock.lock();
            try {
                return count;
            } finally {
                lock.unlock();
            }
        }
    }
    
    /**
     * Classe che dimostra l'uso di ReadWriteLock
     */
    static class SharedResource {
        private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
        private String sharedData = "Dati iniziali";
        
        public String read() {
            rwLock.readLock().lock();
            try {
                // Simulazione di una lettura che richiede tempo
                Thread.sleep(10);
                return sharedData;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return "Lettura interrotta";
            } finally {
                rwLock.readLock().unlock();
            }
        }
        
        public void write(String newData) {
            rwLock.writeLock().lock();
            try {
                // Simulazione di una scrittura che richiede tempo
                Thread.sleep(50);
                System.out.println("Scrittura in corso: " + newData);
                this.sharedData = newData;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                rwLock.writeLock().unlock();
            }
        }
    }
    
    /**
     * Implementazione di un buffer limitato usando Lock e Condition
     */
    static class BoundedBuffer<E> {
        private final ReentrantLock lock = new ReentrantLock();
        private final Condition notFull = lock.newCondition();
        private final Condition notEmpty = lock.newCondition();
        
        private final Queue<E> queue;
        private final int capacity;
        
        public BoundedBuffer(int capacity) {
            this.capacity = capacity;
            this.queue = new LinkedList<>();
        }
        
        public void put(E item) throws InterruptedException {
            lock.lock();
            try {
                // Attende che ci sia spazio nel buffer
                while (queue.size() == capacity) {
                    System.out.println("Buffer pieno, produttore in attesa...");
                    notFull.await();
                }
                
                queue.add(item);
                
                // Segnala ai consumatori che il buffer non è più vuoto
                notEmpty.signal();
            } finally {
                lock.unlock();
            }
        }
        
        public E take() throws InterruptedException {
            lock.lock();
            try {
                // Attende che ci sia almeno un elemento nel buffer
                while (queue.isEmpty()) {
                    System.out.println("Buffer vuoto, consumatore in attesa...");
                    notEmpty.await();
                }
                
                E item = queue.remove();
                
                // Segnala ai produttori che il buffer non è più pieno
                notFull.signal();
                
                return item;
            } finally {
                lock.unlock();
            }
        }
    }
}