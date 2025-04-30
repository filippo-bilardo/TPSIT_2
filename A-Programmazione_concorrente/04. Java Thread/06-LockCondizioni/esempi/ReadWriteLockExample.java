package esempi;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Esempio di implementazione di una cache thread-safe utilizzando ReadWriteLock.
 * Questo esempio mostra come utilizzare un ReadWriteLock per ottimizzare l'accesso
 * concorrente a una cache, permettendo letture simultanee ma scritture esclusive.
 */
public class ReadWriteLockExample {
    
    /**
     * Implementazione di una cache thread-safe con ReadWriteLock.
     */
    private static class Cache<K, V> {
        private final Map<K, V> cache = new HashMap<>();
        private final ReadWriteLock lock = new ReentrantReadWriteLock();
        
        /**
         * Ottiene un valore dalla cache. Se il valore non è presente,
         * lo calcola utilizzando la funzione di calcolo fornita.
         * Utilizza un lock di lettura per le operazioni di lettura e
         * un lock di scrittura solo quando è necessario aggiungere un nuovo valore.
         */
        public V get(K key, ComputeFunction<K, V> computeFunction) {
            // Prima prova a leggere con un lock di lettura
            lock.readLock().lock();
            try {
                V value = cache.get(key);
                if (value != null) {
                    System.out.println(Thread.currentThread().getName() + ": Cache hit per " + key);
                    return value;
                }
            } finally {
                lock.readLock().unlock();
            }
            
            // Se il valore non è presente, acquisisce un lock di scrittura
            lock.writeLock().lock();
            try {
                // Ricontrolla dopo aver acquisito il lock di scrittura
                // (potrebbe essere stato aggiunto da un altro thread nel frattempo)
                V value = cache.get(key);
                if (value == null) {
                    System.out.println(Thread.currentThread().getName() + ": Cache miss per " + key + ", calcolo il valore");
                    value = computeFunction.compute(key);
                    cache.put(key, value);
                }
                return value;
            } finally {
                lock.writeLock().unlock();
            }
        }
        
        /**
         * Aggiorna un valore nella cache.
         * Utilizza un lock di scrittura per garantire l'esclusività dell'operazione.
         */
        public void put(K key, V value) {
            lock.writeLock().lock();
            try {
                System.out.println(Thread.currentThread().getName() + ": Aggiornamento cache per " + key);
                cache.put(key, value);
            } finally {
                lock.writeLock().unlock();
            }
        }
        
        /**
         * Rimuove un valore dalla cache.
         * Utilizza un lock di scrittura per garantire l'esclusività dell'operazione.
         */
        public void remove(K key) {
            lock.writeLock().lock();
            try {
                System.out.println(Thread.currentThread().getName() + ": Rimozione dalla cache per " + key);
                cache.remove(key);
            } finally {
                lock.writeLock().unlock();
            }
        }
        
        /**
         * Restituisce la dimensione corrente della cache.
         * Utilizza un lock di lettura poiché è un'operazione di sola lettura.
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
     * Interfaccia funzionale per il calcolo di un valore.
     */
    @FunctionalInterface
    private interface ComputeFunction<K, V> {
        V compute(K key);
    }
    
    public static void main(String[] args) throws InterruptedException {
        // Creazione della cache
        Cache<Integer, String> cache = new Cache<>();
        
        // Funzione di calcolo (simulazione di un'operazione costosa)
        ComputeFunction<Integer, String> computeFunction = key -> {
            try {
                // Simulazione di un'operazione che richiede tempo
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return "Valore per " + key;
        };
        
        // Creazione di un pool di thread
        ExecutorService executor = Executors.newFixedThreadPool(10);
        
        // Simulazione di operazioni di lettura concorrenti
        for (int i = 0; i < 20; i++) {
            final int key = i % 5;  // Utilizzo di 5 chiavi diverse
            executor.submit(() -> {
                String value = cache.get(key, computeFunction);
                System.out.println(Thread.currentThread().getName() + ": Ottenuto " + value);
            });
        }
        
        // Attesa di un po' di tempo
        Thread.sleep(2000);
        
        // Simulazione di operazioni di scrittura concorrenti
        for (int i = 0; i < 5; i++) {
            final int key = i;
            executor.submit(() -> {
                cache.put(key, "Nuovo valore per " + key);
            });
        }
        
        // Simulazione di operazioni miste (lettura/scrittura)
        for (int i = 0; i < 10; i++) {
            final int key = ThreadLocalRandom.current().nextInt(10);
            executor.submit(() -> {
                if (key < 5) {
                    String value = cache.get(key, computeFunction);
                    System.out.println(Thread.currentThread().getName() + ": Ottenuto " + value);
                } else if (key < 8) {
                    cache.put(key, "Valore aggiornato per " + key);
                } else {
                    cache.remove(key);
                }
            });
        }
        
        // Chiusura ordinata del pool di thread
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
        
        // Stampa della dimensione finale della cache
        System.out.println("\nDimensione finale della cache: " + cache.size());
    }
}