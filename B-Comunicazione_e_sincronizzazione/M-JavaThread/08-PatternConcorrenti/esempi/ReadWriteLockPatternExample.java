package esempi;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Esempio di implementazione del pattern Read-Write Lock.
 * Questo pattern permette a più thread di leggere contemporaneamente una risorsa,
 * ma richiede accesso esclusivo per le operazioni di scrittura.
 */
public class ReadWriteLockPatternExample {

    /**
     * Implementazione di una cache concorrente utilizzando ReadWriteLock.
     * Ottimizzata per scenari con molte letture e poche scritture.
     */
    static class ConcurrentCache<K, V> {
        private final Map<K, V> cache = new HashMap<>();
        private final ReadWriteLock lock = new ReentrantReadWriteLock();
        private int reads = 0;
        private int writes = 0;

        /**
         * Recupera un valore dalla cache.
         * Utilizza il lock di lettura, permettendo accessi concorrenti in lettura.
         */
        public V get(K key) {
            lock.readLock().lock();
            try {
                reads++;
                // Simulazione di un'operazione di lettura che richiede tempo
                Thread.sleep(10);
                return cache.get(key);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            } finally {
                lock.readLock().unlock();
            }
        }

        /**
         * Inserisce un valore nella cache.
         * Utilizza il lock di scrittura, garantendo accesso esclusivo.
         */
        public void put(K key, V value) {
            lock.writeLock().lock();
            try {
                writes++;
                // Simulazione di un'operazione di scrittura che richiede tempo
                Thread.sleep(50);
                cache.put(key, value);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.writeLock().unlock();
            }
        }

        /**
         * Calcola e memorizza un valore se non è già presente nella cache.
         * Utilizza prima il lock di lettura per verificare se il valore esiste,
         * poi il lock di scrittura solo se necessario (pattern di ottimizzazione comune).
         */
        public V computeIfAbsent(K key, java.util.function.Function<K, V> mappingFunction) {
            // Prima prova con il lock di lettura (operazione veloce)
            lock.readLock().lock();
            try {
                reads++;
                V value = cache.get(key);
                if (value != null) {
                    return value;
                }
            } finally {
                lock.readLock().unlock();
            }
            
            // Se il valore non esiste, acquisisce il lock di scrittura
            lock.writeLock().lock();
            try {
                writes++;
                // Double-check per evitare race condition
                V value = cache.get(key);
                if (value == null) {
                    value = mappingFunction.apply(key);
                    // Simulazione di un'operazione di calcolo che richiede tempo
                    Thread.sleep(100);
                    cache.put(key, value);
                }
                return value;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            } finally {
                lock.writeLock().unlock();
            }
        }

        /**
         * Restituisce il numero di operazioni di lettura effettuate.
         */
        public int getReadCount() {
            return reads;
        }

        /**
         * Restituisce il numero di operazioni di scrittura effettuate.
         */
        public int getWriteCount() {
            return writes;
        }

        /**
         * Restituisce la dimensione attuale della cache.
         */
        public int size() {
            lock.readLock().lock();
            try {
                return cache.size();
            } finally {
                lock.readLock().unlock();
            }
        }
    }

    /**
     * Thread che esegue operazioni di lettura sulla cache.
     */
    static class Reader implements Runnable {
        private final ConcurrentCache<Integer, String> cache;
        private final int iterations;
        private final String name;

        public Reader(ConcurrentCache<Integer, String> cache, int iterations, String name) {
            this.cache = cache;
            this.iterations = iterations;
            this.name = name;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < iterations; i++) {
                    int key = ThreadLocalRandom.current().nextInt(10);
                    String value = cache.get(key);
                    System.out.printf("%s: Letto chiave %d, valore: %s%n", name, key, value);
                    
                    // Breve pausa tra le operazioni
                    Thread.sleep(ThreadLocalRandom.current().nextInt(50));
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Thread che esegue operazioni di scrittura sulla cache.
     */
    static class Writer implements Runnable {
        private final ConcurrentCache<Integer, String> cache;
        private final int iterations;
        private final String name;

        public Writer(ConcurrentCache<Integer, String> cache, int iterations, String name) {
            this.cache = cache;
            this.iterations = iterations;
            this.name = name;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < iterations; i++) {
                    int key = ThreadLocalRandom.current().nextInt(10);
                    String value = "Value-" + System.currentTimeMillis();
                    cache.put(key, value);
                    System.out.printf("%s: Scritto chiave %d, valore: %s%n", name, key, value);
                    
                    // Pausa più lunga tra le operazioni di scrittura
                    Thread.sleep(ThreadLocalRandom.current().nextInt(200, 500));
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Thread che esegue operazioni di calcolo e memorizzazione sulla cache.
     */
    static class Computer implements Runnable {
        private final ConcurrentCache<Integer, String> cache;
        private final int iterations;
        private final String name;

        public Computer(ConcurrentCache<Integer, String> cache, int iterations, String name) {
            this.cache = cache;
            this.iterations = iterations;
            this.name = name;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < iterations; i++) {
                    int key = ThreadLocalRandom.current().nextInt(10);
                    String value = cache.computeIfAbsent(key, k -> "Computed-" + k + "-" + System.currentTimeMillis());
                    System.out.printf("%s: Calcolato per chiave %d, valore: %s%n", name, key, value);
                    
                    // Pausa media tra le operazioni
                    Thread.sleep(ThreadLocalRandom.current().nextInt(100, 300));
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Esempio del pattern Read-Write Lock ===\n");

        // Creazione della cache concorrente
        ConcurrentCache<Integer, String> cache = new ConcurrentCache<>();
        
        // Inizializzazione della cache con alcuni valori
        for (int i = 0; i < 5; i++) {
            cache.put(i, "Initial-" + i);
        }
        System.out.println("Cache inizializzata con " + cache.size() + " elementi\n");

        // Creazione di un pool di thread
        ExecutorService executor = Executors.newFixedThreadPool(10);
        
        // Avvio di thread di lettura (molti lettori)
        for (int i = 1; i <= 5; i++) {
            executor.submit(new Reader(cache, 20, "Reader-" + i));
        }
        
        // Avvio di thread di scrittura (pochi scrittori)
        for (int i = 1; i <= 2; i++) {
            executor.submit(new Writer(cache, 5, "Writer-" + i));
        }
        
        // Avvio di thread di calcolo
        for (int i = 1; i <= 3; i++) {
            executor.submit(new Computer(cache, 8, "Computer-" + i));
        }

        // Attesa per il completamento dei task
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        // Stampa delle statistiche finali
        System.out.println("\nStatistiche finali:");
        System.out.println("- Dimensione finale della cache: " + cache.size());
        System.out.println("- Numero totale di operazioni di lettura: " + cache.getReadCount());
        System.out.println("- Numero totale di operazioni di scrittura: " + cache.getWriteCount());
        System.out.println("- Rapporto letture/scritture: " + 
                          String.format("%.2f", (double) cache.getReadCount() / cache.getWriteCount()));

        System.out.println("\n=== Fine dell'esempio ===");
    }
}