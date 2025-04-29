/**
 * Implementazione di un pool di worker thread che elaborano task da una coda condivisa.
 * Questo esempio mostra come gestire un gruppo di thread worker che eseguono
 * task in parallelo, prelevandoli da una coda condivisa.
 */
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class WorkerPool {
    
    /**
     * Metodo main che dimostra l'utilizzo del pool di worker.
     */
    public static void main(String[] args) throws InterruptedException {
        // Crea un pool con 4 worker
        TaskWorkerPool pool = new TaskWorkerPool(4);
        
        // Avvia il pool
        pool.start();
        
        // Invia alcuni task al pool
        for (int i = 1; i <= 10; i++) {
            final int taskId = i;
            pool.submit(() -> {
                System.out.println("Esecuzione del task " + taskId + " sul thread " + Thread.currentThread().getName());
                try {
                    // Simula un'elaborazione che richiede tempo
                    Thread.sleep((long) (Math.random() * 2000));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                return "Risultato del task " + taskId;
            });
            
            // Breve pausa tra la sottomissione dei task
            Thread.sleep(200);
        }
        
        // Attende che tutti i task siano completati
        Thread.sleep(5000);
        
        // Arresta il pool
        pool.shutdown();
        
        System.out.println("Tutti i task sono stati completati. Pool arrestato.");
    }
    
    /**
     * Interfaccia funzionale che rappresenta un task eseguibile.
     */
    @FunctionalInterface
    interface Task<T> {
        T execute() throws Exception;
    }
    
    /**
     * Classe che rappresenta un pool di worker thread.
     */
    static class TaskWorkerPool {
        private final BlockingQueue<Runnable> taskQueue;
        private final Thread[] workers;
        private final AtomicBoolean isRunning = new AtomicBoolean(false);
        private final AtomicInteger completedTasks = new AtomicInteger(0);
        
        /**
         * Costruttore che inizializza il pool con il numero specificato di worker.
         */
        public TaskWorkerPool(int numWorkers) {
            this.taskQueue = new LinkedBlockingQueue<>();
            this.workers = new Thread[numWorkers];
            
            // Inizializza i worker thread
            for (int i = 0; i < numWorkers; i++) {
                workers[i] = new Thread(this::processTask, "Worker-" + i);
            }
        }
        
        /**
         * Avvia tutti i worker thread del pool.
         */
        public void start() {
            if (isRunning.compareAndSet(false, true)) {
                System.out.println("Avvio del pool con " + workers.length + " worker");
                for (Thread worker : workers) {
                    worker.start();
                }
            }
        }
        
        /**
         * Sottomette un task al pool per l'esecuzione.
         */
        public <T> void submit(Task<T> task) {
            if (!isRunning.get()) {
                throw new IllegalStateException("Il pool non è in esecuzione");
            }
            
            taskQueue.offer(() -> {
                try {
                    T result = task.execute();
                    System.out.println("Task completato con risultato: " + result);
                    completedTasks.incrementAndGet();
                } catch (Exception e) {
                    System.err.println("Errore nell'esecuzione del task: " + e.getMessage());
                }
            });
        }
        
        /**
         * Metodo eseguito da ciascun worker thread per elaborare i task dalla coda.
         */
        private void processTask() {
            while (isRunning.get()) {
                try {
                    // Preleva un task dalla coda (bloccante)
                    Runnable task = taskQueue.take();
                    
                    // Esegue il task
                    task.run();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println(Thread.currentThread().getName() + " interrotto");
                    break;
                }
            }
            System.out.println(Thread.currentThread().getName() + " terminato");
        }
        
        /**
         * Arresta il pool e tutti i suoi worker thread.
         */
        public void shutdown() {
            if (isRunning.compareAndSet(true, false)) {
                System.out.println("Arresto del pool...");
                
                // Interrompe tutti i worker
                for (Thread worker : workers) {
                    worker.interrupt();
                }
                
                // Attende che tutti i worker terminino
                for (Thread worker : workers) {
                    try {
                        worker.join(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                
                System.out.println("Pool arrestato. Task completati: " + completedTasks.get());
            }
        }
        
        /**
         * Restituisce il numero di task completati.
         */
        public int getCompletedTaskCount() {
            return completedTasks.get();
        }
    }
}