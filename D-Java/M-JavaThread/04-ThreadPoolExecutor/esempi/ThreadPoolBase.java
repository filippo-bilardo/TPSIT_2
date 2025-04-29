/**
 * Esempio 01: Utilizzo Base di ThreadPoolExecutor
 * 
 * Questo esempio dimostra l'utilizzo base del framework Executor di Java
 * per gestire pool di thread. Vengono illustrati diversi tipi di pool:
 * - Fixed Thread Pool
 * - Cached Thread Pool
 * - Single Thread Executor
 * - Scheduled Thread Pool
 */
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ThreadPoolBase {
    public static void main(String[] args) {
        System.out.println("Dimostrazione dell'utilizzo di ThreadPoolExecutor");
        System.out.println("==============================================");
        
        // Esempio 1: Fixed Thread Pool
        System.out.println("\n1. Fixed Thread Pool (3 thread):");
        ExecutorService fixedPool = Executors.newFixedThreadPool(3);
        eseguiTaskSuPool(fixedPool, "FixedPool", 10);
        chiudiPool(fixedPool, "Fixed Thread Pool");
        
        // Esempio 2: Cached Thread Pool
        System.out.println("\n2. Cached Thread Pool (dimensione dinamica):");
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        eseguiTaskSuPool(cachedPool, "CachedPool", 10);
        chiudiPool(cachedPool, "Cached Thread Pool");
        
        // Esempio 3: Single Thread Executor
        System.out.println("\n3. Single Thread Executor (1 thread):");
        ExecutorService singlePool = Executors.newSingleThreadExecutor();
        eseguiTaskSuPool(singlePool, "SinglePool", 5);
        chiudiPool(singlePool, "Single Thread Pool");
        
        // Esempio 4: Scheduled Thread Pool
        System.out.println("\n4. Scheduled Thread Pool (attività pianificate):");
        ScheduledExecutorService scheduledPool = Executors.newScheduledThreadPool(2);
        
        // Esegui un task dopo un ritardo
        scheduledPool.schedule(() -> {
            System.out.println("ScheduledPool: Task eseguito dopo 2 secondi");
        }, 2, TimeUnit.SECONDS);
        
        // Esegui un task periodicamente
        scheduledPool.scheduleAtFixedRate(() -> {
            System.out.println("ScheduledPool: Task periodico eseguito ogni secondo");
        }, 0, 1, TimeUnit.SECONDS);
        
        // Lascia che i task pianificati vengano eseguiti per un po'
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        chiudiPool(scheduledPool, "Scheduled Thread Pool");
        
        System.out.println("\nVantaggi dell'utilizzo di ThreadPoolExecutor:");
        System.out.println("1. Riutilizzo dei thread (riduce l'overhead di creazione)");
        System.out.println("2. Gestione controllata delle risorse");
        System.out.println("3. Separazione tra creazione dei task e loro esecuzione");
        System.out.println("4. Supporto per diverse strategie di esecuzione");
    }
    
    /**
     * Esegue un numero specificato di task su un pool di thread.
     */
    private static void eseguiTaskSuPool(ExecutorService pool, String poolName, int numTasks) {
        for (int i = 1; i <= numTasks; i++) {
            final int taskId = i;
            pool.execute(() -> {
                System.out.println(poolName + ": Task " + taskId + " eseguito da " + 
                                   Thread.currentThread().getName());
                try {
                    // Simuliamo un'operazione che richiede tempo
                    Thread.sleep((long) (Math.random() * 500));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
    }
    
    /**
     * Chiude correttamente un pool di thread.
     */
    private static void chiudiPool(ExecutorService pool, String poolName) {
        try {
            // Rifiuta nuovi task
            pool.shutdown();
            
            // Attende la terminazione dei task in esecuzione
            if (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
                // Forza la terminazione se necessario
                pool.shutdownNow();
                
                if (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
                    System.err.println(poolName + ": Il pool non si è terminato");
                }
            }
            System.out.println(poolName + ": Pool chiuso correttamente");
        } catch (InterruptedException e) {
            // Ripristina lo stato di interruzione
            Thread.currentThread().interrupt();
            // Forza la terminazione
            pool.shutdownNow();
            System.err.println(poolName + ": Interruzione durante la chiusura del pool");
        }
    }
}