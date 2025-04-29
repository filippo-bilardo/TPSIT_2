/**
 * Esempio che dimostra il problema delle race condition nei thread
 * e le possibili soluzioni
 */
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ES08_RaceCondition {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Dimostrazione di race condition e relative soluzioni");
        
        // Test con contatore non sicuro (race condition)
        testCounter(new UnsafeCounter(), "Contatore non sicuro");
        
        // Test con contatore sincronizzato
        testCounter(new SynchronizedCounter(), "Contatore sincronizzato");
        
        // Test con contatore che usa lock esplicito
        testCounter(new LockCounter(), "Contatore con lock");
        
        // Test con contatore atomico
        testCounter(new AtomicCounter(), "Contatore atomico");
    }
    
    private static void testCounter(Counter counter, String description) throws InterruptedException {
        System.out.println("\n=== Test: " + description + " ===");
        
        // Reset del contatore
        counter.reset();
        
        // Creiamo 10 thread che incrementano il contatore 10000 volte ciascuno
        Thread[] threads = new Thread[10];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    counter.increment();
                }
            });
            threads[i].start();
        }
        
        // Attendiamo che tutti i thread completino
        for (Thread t : threads) {
            t.join();
        }
        
        // Verifichiamo il risultato
        int expected = 10 * 10000; // 10 thread, 10000 incrementi ciascuno
        int actual = counter.getValue();
        System.out.println("Valore atteso: " + expected);
        System.out.println("Valore effettivo: " + actual);
        if (expected == actual) {
            System.out.println("SUCCESSO: Il contatore è stato incrementato correttamente!");
        } else {
            System.out.println("ERRORE: Race condition rilevata! Mancano " + 
                             (expected - actual) + " incrementi.");
        }
    }
    
    // Interfaccia comune per tutti i contatori
    interface Counter {
        void increment();
        int getValue();
        void reset();
    }
    
    // Contatore non sicuro che soffre di race condition
    static class UnsafeCounter implements Counter {
        private int count = 0;
        
        @Override
        public void increment() {
            count++;  // Operazione non atomica!
        }
        
        @Override
        public int getValue() {
            return count;
        }
        
        @Override
        public void reset() {
            count = 0;
        }
    }
    
    // Contatore sicuro con synchronized
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
        
        @Override
        public synchronized void reset() {
            count = 0;
        }
    }
    
    // Contatore sicuro con lock esplicito
    static class LockCounter implements Counter {
        private int count = 0;
        private final Lock lock = new ReentrantLock();
        
        @Override
        public void increment() {
            lock.lock();
            try {
                count++;
            } finally {
                lock.unlock();
            }
        }
        
        @Override
        public int getValue() {
            lock.lock();
            try {
                return count;
            } finally {
                lock.unlock();
            }
        }
        
        @Override
        public void reset() {
            lock.lock();
            try {
                count = 0;
            } finally {
                lock.unlock();
            }
        }
    }
    
    // Contatore sicuro con AtomicInteger
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
        
        @Override
        public void reset() {
            count.set(0);
        }
    }
}
