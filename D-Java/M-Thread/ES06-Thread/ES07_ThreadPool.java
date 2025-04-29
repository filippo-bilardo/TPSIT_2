/**
 * Esempio che dimostra l'utilizzo dei thread pool e dell'Executor Framework in Java
 */
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ES07_ThreadPool {
    public static void main(String[] args) {
        System.out.println("Dimostrazione dei vari tipi di thread pool in Java");
        
        // 1. Fixed Thread Pool - numero fisso di thread
        dimostraFixedThreadPool();
        
        // 2. Cached Thread Pool - cresce dinamicamente in base alla necessità
        dimostraCachedThreadPool();
        
        // 3. Scheduled Thread Pool - per esecuzioni pianificate
        dimostraScheduledThreadPool();
        
        // 4. Single Thread Executor - un solo thread che esegue i task in sequenza
        dimostraSingleThreadExecutor();
        
        System.out.println("Tutte le dimostrazioni completate");
    }
    
    // Creazione di una factory personalizzata per dare nomi ai thread
    static class NamingThreadFactory implements ThreadFactory {
        private final String namePrefix;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        
        public NamingThreadFactory(String namePrefix) {
            this.namePrefix = namePrefix;
        }
        
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, namePrefix + "-" + threadNumber.getAndIncrement());
            return thread;
        }
    }
    
    private static void dimostraFixedThreadPool() {
        System.out.println("\n=== Fixed Thread Pool ===");
        
        // Creiamo un pool con 3 thread
        ExecutorService fixedPool = Executors.newFixedThreadPool(
            3,
            new NamingThreadFactory("Fixed")
        );
        
        // Sottomettiamo 5 task (alcuni dovranno attendere)
        for (int i = 1; i <= 5; i++) {
            final int taskId = i;
            fixedPool.execute(() -> {
                String threadName = Thread.currentThread().getName();
                System.out.println("Task " + taskId + " in esecuzione sul " + threadName);
                try {
                    Thread.sleep(1000); // Simuliamo un lavoro
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("Task " + taskId + " completato sul " + threadName);
            });
        }
        
        // Shutdown del pool e attesa del completamento
        shutdownAndAwaitTermination(fixedPool, "Fixed Thread Pool");
    }
    
    private static void dimostraCachedThreadPool() {
        System.out.println("\n=== Cached Thread Pool ===");
        
        // Creiamo un cached thread pool
        ExecutorService cachedPool = Executors.newCachedThreadPool(
            new NamingThreadFactory("Cached")
        );
        
        // Sottomettiamo 10 task (dovrebbero essere eseguiti quasi contemporaneamente)
        for (int i = 1; i <= 10; i++) {
            final int taskId = i;
            cachedPool.execute(() -> {
                String threadName = Thread.currentThread().getName();
                System.out.println("Task " + taskId + " in esecuzione sul " + threadName);
                try {
                    Thread.sleep(500); // Lavoro breve
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("Task " + taskId + " completato sul " + threadName);
            });
        }
        
        shutdownAndAwaitTermination(cachedPool, "Cached Thread Pool");
    }
    
    private static void dimostraScheduledThreadPool() {
        System.out.println("\n=== Scheduled Thread Pool ===");
        
        // Creiamo un scheduled thread pool con 2 thread
        ScheduledExecutorService scheduledPool = Executors.newScheduledThreadPool(
            2,
            new NamingThreadFactory("Scheduled")
        );
        
        // Eseguiamo un task una sola volta dopo 1 secondo
        scheduledPool.schedule(() -> {
            System.out.println("Task singolo eseguito dopo ritardo sul " + 
                             Thread.currentThread().getName());
        }, 1, TimeUnit.SECONDS);
        
        // Eseguiamo un task periodicamente ogni 0.5 secondi, iniziando dopo 1 secondo
        ScheduledFuture<?> periodicTask = scheduledPool.scheduleAtFixedRate(() -> {
            System.out.println("Task periodico eseguito alle " + 
                             System.currentTimeMillis() % 10000 + " sul " +
                             Thread.currentThread().getName());
        }, 1, 1, TimeUnit.SECONDS);
        
        // Lasciamo che il task periodico venga eseguito alcune volte
        try {
            Thread.sleep(5000); // Permetti 5 esecuzioni circa
            
            // Annulliamo il task periodico
            periodicTask.cancel(false);
            System.out.println("Task periodico annullato");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        shutdownAndAwaitTermination(scheduledPool, "Scheduled Thread Pool");
    }
    
    private static void dimostraSingleThreadExecutor() {
        System.out.println("\n=== Single Thread Executor ===");
        
        // Creiamo un executor con un singolo thread
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor(
            new NamingThreadFactory("Single")
        );
        
        // Sottomettiamo 3 task (saranno eseguiti in sequenza)
        for (int i = 1; i <= 3; i++) {
            final int taskId = i;
            singleThreadExecutor.execute(() -> {
                String threadName = Thread.currentThread().getName();
                System.out.println("Task " + taskId + " in esecuzione sul " + threadName);
                try {
                    Thread.sleep(700); // Lavoro
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("Task " + taskId + " completato sul " + threadName);
            });
        }
        
        shutdownAndAwaitTermination(singleThreadExecutor, "Single Thread Executor");
    }
    
    // Utility per shutdown ordinato degli executor
    private static void shutdownAndAwaitTermination(ExecutorService pool, String poolName) {
        pool.shutdown(); // Rifiuta nuovi task ma completa quelli in esecuzione
        try {
            // Attendi un po' per la terminazione
            if (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
                // Forza la terminazione se necessario
                pool.shutdownNow();
                System.out.println(poolName + " forzato a terminare");
                
                // Attendi nuovamente per la terminazione
                if (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
                    System.err.println(poolName + " non si è terminato");
                }
            } else {
                System.out.println(poolName + " terminato con successo");
            }
        } catch (InterruptedException ie) {
            // Se interrotto, ripristina lo stato e forza lo shutdown
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
