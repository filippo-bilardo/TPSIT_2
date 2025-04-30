package esempi;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Random;

/**
 * Esempio di implementazione del pattern Worker Thread utilizzando ThreadPoolExecutor.
 * Questo pattern utilizza un pool di thread per elaborare le richieste da una coda di lavoro,
 * ottimizzando l'utilizzo delle risorse e migliorando le prestazioni in scenari con molte richieste.
 */
public class WorkerThreadExample {

    // Classe che rappresenta un task da elaborare
    static class Task implements Runnable {
        private final int id;
        private final int duration;
        private static final Random random = new Random();

        public Task(int id) {
            this.id = id;
            // Simulazione di task con durata variabile (tra 500ms e 3000ms)
            this.duration = 500 + random.nextInt(2500);
        }

        @Override
        public void run() {
            try {
                System.out.printf("Task %d: Inizio elaborazione (durata prevista: %dms) nel thread %s%n", 
                                id, duration, Thread.currentThread().getName());
                
                // Simulazione di un'operazione che richiede tempo
                Thread.sleep(duration);
                
                System.out.printf("Task %d: Elaborazione completata nel thread %s%n", 
                                id, Thread.currentThread().getName());
            } catch (InterruptedException e) {
                System.out.printf("Task %d: Interrotto nel thread %s%n", 
                                id, Thread.currentThread().getName());
                Thread.currentThread().interrupt();
            }
        }

        @Override
        public String toString() {
            return "Task-" + id;
        }
    }

    // Factory personalizzata per la creazione di thread con nomi significativi
    static class NamedThreadFactory implements ThreadFactory {
        private final String namePrefix;
        private final AtomicInteger threadNumber = new AtomicInteger(1);

        public NamedThreadFactory(String namePrefix) {
            this.namePrefix = namePrefix;
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, namePrefix + "-" + threadNumber.getAndIncrement());
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }

    // Handler personalizzato per la gestione dei task rifiutati
    static class MonitoredCallerRunsPolicy implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            System.out.printf("ATTENZIONE: Task %s rifiutato. Esecuzione nel thread chiamante: %s%n", 
                            r, Thread.currentThread().getName());
            
            // Esegue il task nel thread del chiamante
            if (!executor.isShutdown()) {
                r.run();
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Esempio del pattern Worker Thread ===\n");

        // Creazione di un ThreadPoolExecutor personalizzato
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
            2,                                  // corePoolSize: numero minimo di thread
            5,                                  // maximumPoolSize: numero massimo di thread
            30, TimeUnit.SECONDS,               // keepAliveTime: tempo di inattività prima della terminazione
            new ArrayBlockingQueue<>(10),       // workQueue: coda con capacità limitata
            new NamedThreadFactory("Worker"),   // threadFactory: factory personalizzata
            new MonitoredCallerRunsPolicy()     // handler: politica di gestione dei task rifiutati
        );

        // Stampa delle informazioni iniziali sul pool
        System.out.println("Configurazione del pool:");
        System.out.println("- Core pool size: " + executor.getCorePoolSize());
        System.out.println("- Maximum pool size: " + executor.getMaximumPoolSize());
        System.out.println("- Keep alive time: " + executor.getKeepAliveTime(TimeUnit.SECONDS) + " secondi");
        System.out.println("- Capacità della coda: " + ((ArrayBlockingQueue<?>) executor.getQueue()).remainingCapacity());
        System.out.println();

        // Sottomissione di task all'executor
        System.out.println("Sottomissione di 20 task al pool di worker thread...");
        for (int i = 1; i <= 20; i++) {
            try {
                executor.execute(new Task(i));
                System.out.printf("Task %d: Sottomesso al pool (Pool size: %d, Active: %d, Queue size: %d)%n", 
                                i, executor.getPoolSize(), executor.getActiveCount(), executor.getQueue().size());
                
                // Breve pausa tra le sottomissioni per osservare meglio il comportamento
                if (i % 5 == 0) {
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                System.out.println("Errore nella sottomissione del task " + i + ": " + e.getMessage());
            }
        }

        // Attesa per permettere il completamento dei task
        System.out.println("\nAttesa per il completamento di tutti i task...");
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                System.out.println("Timeout raggiunto. Forzatura dello shutdown.");
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            System.out.println("Shutdown interrotto. Forzatura dello shutdown.");
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        System.out.println("\n=== Fine dell'esempio ===");
    }
}