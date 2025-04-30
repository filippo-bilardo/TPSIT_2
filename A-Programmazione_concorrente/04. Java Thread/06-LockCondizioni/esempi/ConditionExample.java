package esempi;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Esempio di implementazione di un buffer limitato utilizzando Lock e Condition.
 * Questo esempio mostra come utilizzare le condizioni per coordinare l'attività
 * di produttori e consumatori che operano su un buffer condiviso di capacità limitata.
 */
public class ConditionExample {
    
    /**
     * Implementazione di un buffer limitato thread-safe con Lock e Condition.
     */
    private static class BoundedBuffer<E> {
        private final Queue<E> queue = new LinkedList<>();
        private final int capacity;
        
        private final Lock lock = new ReentrantLock();
        private final Condition notFull = lock.newCondition();
        private final Condition notEmpty = lock.newCondition();
        
        public BoundedBuffer(int capacity) {
            this.capacity = capacity;
        }
        
        /**
         * Aggiunge un elemento al buffer.
         * Se il buffer è pieno, il thread si blocca finché non si libera spazio.
         */
        public void put(E element) throws InterruptedException {
            lock.lock();
            try {
                // Attendi finché il buffer non è pieno
                while (queue.size() == capacity) {
                    System.out.println(Thread.currentThread().getName() + ": Buffer pieno, in attesa...");
                    notFull.await();
                }
                
                // Aggiungi l'elemento e segnala che il buffer non è più vuoto
                queue.add(element);
                System.out.println(Thread.currentThread().getName() + ": Prodotto " + element + ", dimensione buffer: " + queue.size());
                notEmpty.signal();
            } finally {
                lock.unlock();
            }
        }
        
        /**
         * Rimuove e restituisce un elemento dal buffer.
         * Se il buffer è vuoto, il thread si blocca finché non viene aggiunto un elemento.
         */
        public E take() throws InterruptedException {
            lock.lock();
            try {
                // Attendi finché il buffer non è vuoto
                while (queue.isEmpty()) {
                    System.out.println(Thread.currentThread().getName() + ": Buffer vuoto, in attesa...");
                    notEmpty.await();
                }
                
                // Rimuovi l'elemento e segnala che il buffer non è più pieno
                E element = queue.remove();
                System.out.println(Thread.currentThread().getName() + ": Consumato " + element + ", dimensione buffer: " + queue.size());
                notFull.signal();
                return element;
            } finally {
                lock.unlock();
            }
        }
        
        /**
         * Tenta di aggiungere un elemento al buffer con un timeout.
         * Restituisce true se l'elemento è stato aggiunto, false se il timeout è scaduto.
         */
        public boolean offer(E element, long timeout, TimeUnit unit) throws InterruptedException {
            lock.lock();
            try {
                // Converti il timeout in nanosecondi
                long nanosTimeout = unit.toNanos(timeout);
                
                // Attendi finché il buffer non è pieno o il timeout scade
                while (queue.size() == capacity) {
                    if (nanosTimeout <= 0) {
                        return false;  // Timeout scaduto
                    }
                    System.out.println(Thread.currentThread().getName() + ": Buffer pieno, in attesa con timeout...");
                    nanosTimeout = notFull.awaitNanos(nanosTimeout);
                }
                
                // Aggiungi l'elemento e segnala che il buffer non è più vuoto
                queue.add(element);
                System.out.println(Thread.currentThread().getName() + ": Prodotto " + element + " con timeout, dimensione buffer: " + queue.size());
                notEmpty.signal();
                return true;
            } finally {
                lock.unlock();
            }
        }
        
        /**
         * Tenta di rimuovere e restituire un elemento dal buffer con un timeout.
         * Restituisce null se il timeout è scaduto.
         */
        public E poll(long timeout, TimeUnit unit) throws InterruptedException {
            lock.lock();
            try {
                // Converti il timeout in nanosecondi
                long nanosTimeout = unit.toNanos(timeout);
                
                // Attendi finché il buffer non è vuoto o il timeout scade
                while (queue.isEmpty()) {
                    if (nanosTimeout <= 0) {
                        return null;  // Timeout scaduto
                    }
                    System.out.println(Thread.currentThread().getName() + ": Buffer vuoto, in attesa con timeout...");
                    nanosTimeout = notEmpty.awaitNanos(nanosTimeout);
                }
                
                // Rimuovi l'elemento e segnala che il buffer non è più pieno
                E element = queue.remove();
                System.out.println(Thread.currentThread().getName() + ": Consumato " + element + " con timeout, dimensione buffer: " + queue.size());
                notFull.signal();
                return element;
            } finally {
                lock.unlock();
            }
        }
        
        /**
         * Restituisce la dimensione corrente del buffer.
         */
        public int size() {
            lock.lock();
            try {
                return queue.size();
            } finally {
                lock.unlock();
            }
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        // Creazione di un buffer limitato con capacità 5
        BoundedBuffer<Integer> buffer = new BoundedBuffer<>(5);
        
        // Creazione di un pool di thread
        ExecutorService executor = Executors.newFixedThreadPool(10);
        
        // Produttori
        for (int i = 0; i < 3; i++) {
            final int producerId = i;
            executor.submit(() -> {
                try {
                    for (int j = 0; j < 10; j++) {
                        int item = producerId * 100 + j;
                        buffer.put(item);
                        Thread.sleep(200);  // Simulazione di produzione che richiede tempo
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        
        // Consumatori
        for (int i = 0; i < 2; i++) {
            executor.submit(() -> {
                try {
                    while (!Thread.currentThread().isInterrupted()) {
                        buffer.take();
                        Thread.sleep(500);  // Simulazione di consumo che richiede tempo
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        
        // Produttore con timeout
        executor.submit(() -> {
            try {
                for (int j = 0; j < 5; j++) {
                    int item = 900 + j;
                    boolean success = buffer.offer(item, 1, TimeUnit.SECONDS);
                    if (!success) {
                        System.out.println(Thread.currentThread().getName() + ": Timeout scaduto durante la produzione di " + item);
                    }
                    Thread.sleep(300);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        // Consumatore con timeout
        executor.submit(() -> {
            try {
                for (int j = 0; j < 5; j++) {
                    Integer item = buffer.poll(1, TimeUnit.SECONDS);
                    if (item == null) {
                        System.out.println(Thread.currentThread().getName() + ": Timeout scaduto durante il consumo");
                    }
                    Thread.sleep(700);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        // Attesa di un po' di tempo per l'esecuzione
        Thread.sleep(10000);
        
        // Chiusura ordinata del pool di thread
        executor.shutdownNow();
        executor.awaitTermination(2, TimeUnit.SECONDS);
        
        // Stampa della dimensione finale del buffer
        System.out.println("\nDimensione finale del buffer: " + buffer.size());
    }
}