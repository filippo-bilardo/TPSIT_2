/**
 * Esempio 02: Utilizzo di ReentrantLock e Condition
 * 
 * Questo esempio dimostra l'utilizzo avanzato di ReentrantLock e Condition per:
 * - Implementare un buffer limitato thread-safe
 * - Gestire la sincronizzazione tra produttori e consumatori
 * - Utilizzare timeout nelle operazioni di lock
 * - Gestire l'interruzione dei thread
 */
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockCondition {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Dimostrazione dell'utilizzo di ReentrantLock e Condition");
        System.out.println("======================================================");
        
        // Crea un buffer limitato thread-safe con ReentrantLock e Condition
        BufferLimitato<Integer> buffer = new BufferLimitato<>(10);
        
        // Crea un pool di thread per produttori e consumatori
        ExecutorService executor = Executors.newFixedThreadPool(6);
        
        System.out.println("\nAvvio produttori e consumatori...");
        
        // Avvia 3 produttori
        for (int i = 0; i < 3; i++) {
            final int prodId = i;
            executor.submit(() -> {
                try {
                    for (int j = 1; j <= 20; j++) {
                        int item = prodId * 100 + j;
                        boolean success = buffer.put(item, 100, TimeUnit.MILLISECONDS);
                        if (success) {
                            System.out.println("Produttore " + prodId + ": inserito elemento " + item);
                        } else {
                            System.out.println("Produttore " + prodId + ": timeout inserimento elemento " + item);
                        }
                        Thread.sleep((int)(Math.random() * 100)); // Simula lavoro variabile
                    }
                    System.out.println("Produttore " + prodId + ": completato");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Produttore " + prodId + ": interrotto");
                }
                return null;
            });
        }
        
        // Avvia 3 consumatori
        for (int i = 0; i < 3; i++) {
            final int consId = i;
            executor.submit(() -> {
                try {
                    while (!Thread.currentThread().isInterrupted()) {
                        Integer item = buffer.take(200, TimeUnit.MILLISECONDS);
                        if (item != null) {
                            System.out.println("Consumatore " + consId + ": prelevato elemento " + item);
                        } else {
                            System.out.println("Consumatore " + consId + ": timeout prelievo elemento");
                        }
                        Thread.sleep((int)(Math.random() * 200)); // Consumatori più lenti
                    }
                } catch (InterruptedException e) {
                    // Gestione normale dell'interruzione
                    System.out.println("Consumatore " + consId + ": interrotto");
                }
                return null;
            });
        }
        
        // Lascia che produttori e consumatori lavorino per un po'
        Thread.sleep(5000);
        
        // Dimostra l'uso del metodo tryLock() con timeout
        System.out.println("\nDimostrazione di tryLock con timeout:");
        Thread lockDemoThread = new Thread(() -> {
            ReentrantLock lock = new ReentrantLock();
            try {
                // Acquisisce il lock
                System.out.println("Thread demo: acquisizione lock");
                lock.lock();
                try {
                    System.out.println("Thread demo: lock acquisito, attesa 2 secondi");
                    Thread.sleep(2000);
                } finally {
                    System.out.println("Thread demo: rilascio lock");
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        lockDemoThread.start();
        Thread.sleep(500); // Attende che il thread demo acquisisca il lock
        
        // Tenta di acquisire lo stesso lock con timeout
        ReentrantLock demoLock = new ReentrantLock();
        System.out.println("Main thread: tentativo di acquisire lock con timeout 1 secondo");
        boolean lockAcquired = demoLock.tryLock(1, TimeUnit.SECONDS);
        if (lockAcquired) {
            try {
                System.out.println("Main thread: lock acquisito");
            } finally {
                demoLock.unlock();
            }
        } else {
            System.out.println("Main thread: timeout acquisizione lock");
        }
        
        // Termina l'executor e attende il completamento
        System.out.println("\nTerminazione executor...");
        executor.shutdown();
        executor.awaitTermination(2, TimeUnit.SECONDS);
        executor.shutdownNow(); // Interrompe i thread rimanenti
        
        System.out.println("\nVantaggi di ReentrantLock rispetto a synchronized:");
        System.out.println("1. Possibilità di tentare l'acquisizione con timeout (tryLock)");
        System.out.println("2. Supporto per lock non bloccanti (tryLock senza timeout)");
        System.out.println("3. Possibilità di interrompere thread in attesa del lock");
        System.out.println("4. API per le condition variables più flessibili");
        System.out.println("5. Supporto per lock equi (fair locking)");
    }
}

/**
 * Implementazione di un buffer limitato thread-safe usando ReentrantLock e Condition
 */
class BufferLimitato<E> {
    private final Queue<E> queue;
    private final int capacita;
    private final ReentrantLock lock;
    private final Condition nonPieno;
    private final Condition nonVuoto;
    
    public BufferLimitato(int capacita) {
        this.capacita = capacita;
        this.queue = new LinkedList<>();
        this.lock = new ReentrantLock(true); // Lock equo (fair)
        this.nonPieno = lock.newCondition();
        this.nonVuoto = lock.newCondition();
    }
    
    /**
     * Inserisce un elemento nel buffer, attendendo se necessario fino a quando
     * c'è spazio disponibile o fino al timeout specificato.
     * 
     * @param elemento Elemento da inserire
     * @param timeout Tempo massimo di attesa
     * @param unit Unità di tempo per il timeout
     * @return true se l'elemento è stato inserito, false in caso di timeout
     * @throws InterruptedException se il thread viene interrotto durante l'attesa
     */
    public boolean put(E elemento, long timeout, TimeUnit unit) throws InterruptedException {
        long nanos = unit.toNanos(timeout);
        lock.lockInterruptibly(); // Acquisisce il lock, può essere interrotto
        try {
            // Attende che ci sia spazio nel buffer
            while (queue.size() == capacita) {
                if (nanos <= 0) {
                    return false; // Timeout scaduto
                }
                nanos = nonPieno.awaitNanos(nanos);
            }
            
            // Inserisce l'elemento e segnala ai consumatori
            queue.add(elemento);
            nonVuoto.signal();
            return true;
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Preleva un elemento dal buffer, attendendo se necessario fino a quando
     * un elemento diventa disponibile o fino al timeout specificato.
     * 
     * @param timeout Tempo massimo di attesa
     * @param unit Unità di tempo per il timeout
     * @return L'elemento prelevato, o null in caso di timeout
     * @throws InterruptedException se il thread viene interrotto durante l'attesa
     */
    public E take(long timeout, TimeUnit unit) throws InterruptedException {
        long nanos = unit.toNanos(timeout);
        lock.lockInterruptibly(); // Acquisisce il lock, può essere interrotto
        try {
            // Attende che ci sia almeno un elemento nel buffer
            while (queue.isEmpty()) {
                if (nanos <= 0) {
                    return null; // Timeout scaduto
                }
                nanos = nonVuoto.awaitNanos(nanos);
            }
            
            // Preleva l'elemento e segnala ai produttori
            E elemento = queue.remove();
            nonPieno.signal();
            return elemento;
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Restituisce il numero di elementi attualmente nel buffer.
     * 
     * @return Numero di elementi nel buffer
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