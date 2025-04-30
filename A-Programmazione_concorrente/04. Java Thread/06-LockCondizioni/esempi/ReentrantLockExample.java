package esempi;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Esempio di utilizzo base di ReentrantLock per proteggere una risorsa condivisa.
 * Questo esempio mostra come utilizzare un ReentrantLock per sincronizzare l'accesso
 * a un contatore condiviso tra più thread.
 */
public class ReentrantLockExample {
    private static class Counter {
        private final ReentrantLock lock = new ReentrantLock();
        private int count = 0;
        
        /**
         * Incrementa il contatore in modo thread-safe utilizzando ReentrantLock.
         * Nota l'uso del pattern try-finally per garantire il rilascio del lock.
         */
        public void increment() {
            lock.lock();  // Acquisizione del lock
            try {
                count++;
                // Simulazione di un'operazione che richiede tempo
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            } finally {
                lock.unlock();  // Rilascio garantito del lock
            }
        }
        
        /**
         * Ottiene il valore corrente del contatore in modo thread-safe.
         */
        public int getCount() {
            lock.lock();
            try {
                return count;
            } finally {
                lock.unlock();
            }
        }
        
        /**
         * Dimostra l'uso del metodo tryLock() per tentare di acquisire il lock
         * senza bloccarsi indefinitamente.
         */
        public boolean incrementIfAvailable() {
            if (lock.tryLock()) {  // Tentativo non bloccante
                try {
                    count++;
                    return true;
                } finally {
                    lock.unlock();
                }
            }
            return false;  // Non è stato possibile acquisire il lock
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        final Counter counter = new Counter();
        final int numThreads = 5;
        final int incrementsPerThread = 100;
        
        // Creazione e avvio dei thread
        Thread[] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < incrementsPerThread; j++) {
                    counter.increment();
                }
            });
            threads[i].start();
        }
        
        // Attesa del completamento di tutti i thread
        for (Thread thread : threads) {
            thread.join();
        }
        
        // Verifica del risultato
        System.out.println("Valore atteso: " + (numThreads * incrementsPerThread));
        System.out.println("Valore effettivo: " + counter.getCount());
        
        // Dimostrazione di tryLock
        System.out.println("\nDimostrazione di tryLock:");
        final Counter counterForTryLock = new Counter();
        Thread lockHolder = new Thread(() -> {
            counterForTryLock.lock.lock();
            try {
                System.out.println("Thread 1: Ho acquisito il lock e lo manterrò per 2 secondi");
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                counterForTryLock.lock.unlock();
                System.out.println("Thread 1: Ho rilasciato il lock");
            }
        });
        
        Thread tryLocker = new Thread(() -> {
            System.out.println("Thread 2: Tento di acquisire il lock con tryLock()");
            if (counterForTryLock.incrementIfAvailable()) {
                System.out.println("Thread 2: Sono riuscito ad acquisire il lock");
            } else {
                System.out.println("Thread 2: Non sono riuscito ad acquisire il lock, continuo con altre operazioni");
            }
            
            try {
                Thread.sleep(3000);  // Attendi che il primo thread rilasci il lock
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            System.out.println("Thread 2: Nuovo tentativo dopo il rilascio");
            if (counterForTryLock.incrementIfAvailable()) {
                System.out.println("Thread 2: Questa volta sono riuscito ad acquisire il lock");
            } else {
                System.out.println("Thread 2: Ancora non riesco ad acquisire il lock");
            }
        });
        
        lockHolder.start();
        Thread.sleep(500);  // Assicurati che il primo thread abbia acquisito il lock
        tryLocker.start();
        
        lockHolder.join();
        tryLocker.join();
    }
}