package esempi;

import java.util.concurrent.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * Esempio che illustra i diversi tipi di thread pool disponibili nel framework Executor.
 * Questo esempio mostra le caratteristiche e i casi d'uso appropriati per ciascun tipo di pool.
 */
public class ThreadPoolTypesExample {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Dimostrazione dei diversi tipi di thread pool\n");
        
        // Esecuzione di esempi per ciascun tipo di thread pool
        demoFixedThreadPool();
        demoCachedThreadPool();
        demoSingleThreadExecutor();
        demoScheduledThreadPool();
        demoWorkStealingPool();
        
        System.out.println("\nTutti gli esempi sono stati completati");
    }
    
    /**
     * Dimostra l'uso di un FixedThreadPool, che mantiene un numero fisso di thread.
     * Ideale per limitare l'utilizzo delle risorse e quando si conosce il carico di lavoro.
     */
    private static void demoFixedThreadPool() throws InterruptedException {
        System.out.println("\n--- Fixed Thread Pool ---");
        System.out.println("Caratteristiche: Numero fisso di thread, coda illimitata");
        System.out.println("Caso d'uso: Limitare l'utilizzo delle risorse, carichi di lavoro noti");
        
        // Creazione di un pool con 3 thread
        ExecutorService fixedPool = Executors.newFixedThreadPool(3);
        
        try {
            // Sottomissione di 6 task (il doppio dei thread disponibili)
            for (int i = 1; i <= 6; i++) {
                final int taskId = i;
                fixedPool.submit(() -> {
                    System.out.println("FixedPool - Task " + taskId + ": Inizio su " + 
                            Thread.currentThread().getName());
                    try {
                        // Simulazione di un'operazione che richiede tempo
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    System.out.println("FixedPool - Task " + taskId + ": Completato");
                    return null;
                });
            }
        } finally {
            // Shutdown ordinato
            fixedPool.shutdown();
            fixedPool.awaitTermination(5, TimeUnit.SECONDS);
        }
    }
    
    /**
     * Dimostra l'uso di un CachedThreadPool, che crea nuovi thread secondo necessità
     * e riutilizza quelli esistenti quando disponibili.
     * Ideale per molti task di breve durata.
     */
    private static void demoCachedThreadPool() throws InterruptedException {
        System.out.println("\n--- Cached Thread Pool ---");
        System.out.println("Caratteristiche: Crea nuovi thread secondo necessità, riutilizza quelli esistenti");
        System.out.println("Caso d'uso: Molti task di breve durata, carico variabile");
        
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        
        try {
            // Sottomissione di task con breve durata
            for (int i = 1; i <= 10; i++) {
                final int taskId = i;
                cachedPool.submit(() -> {
                    System.out.println("CachedPool - Task " + taskId + ": Esecuzione su " + 
                            Thread.currentThread().getName());
                    try {
                        // Operazione molto breve
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    return null;
                });
                
                // Piccola pausa tra le sottomissioni per mostrare il riutilizzo dei thread
                Thread.sleep(100);
            }
        } finally {
            cachedPool.shutdown();
            cachedPool.awaitTermination(5, TimeUnit.SECONDS);
        }
    }
    
    /**
     * Dimostra l'uso di un SingleThreadExecutor, che utilizza un singolo thread worker.
     * Garantisce che i task vengano eseguiti sequenzialmente nell'ordine di sottomissione.
     */
    private static void demoSingleThreadExecutor() throws InterruptedException {
        System.out.println("\n--- Single Thread Executor ---");
        System.out.println("Caratteristiche: Un solo thread, esecuzione sequenziale garantita");
        System.out.println("Caso d'uso: Task che devono essere eseguiti in ordine, accesso a risorse non thread-safe");
        
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        
        try {
            // Sottomissione di task che devono essere eseguiti in ordine
            for (int i = 1; i <= 3; i++) {
                final int taskId = i;
                singleThreadExecutor.submit(() -> {
                    System.out.println("SingleThreadExecutor - Task " + taskId + ": Inizio");
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    System.out.println("SingleThreadExecutor - Task " + taskId + ": Completato");
                    return null;
                });
            }
        } finally {
            singleThreadExecutor.shutdown();
            singleThreadExecutor.awaitTermination(5, TimeUnit.SECONDS);
        }
    }
    
    /**
     * Dimostra l'uso di uno ScheduledThreadPool, che può eseguire task dopo un ritardo
     * o periodicamente.
     */
    private static void demoScheduledThreadPool() throws InterruptedException {
        System.out.println("\n--- Scheduled Thread Pool ---");
        System.out.println("Caratteristiche: Esecuzione di task con ritardo o periodicamente");
        System.out.println("Caso d'uso: Task pianificati, polling, timeout");
        
        ScheduledExecutorService scheduledPool = Executors.newScheduledThreadPool(2);
        
        try {
            // Task da eseguire dopo un ritardo
            scheduledPool.schedule(() -> {
                System.out.println("ScheduledPool - Task con ritardo: Esecuzione alle " + new Date());
                return null;
            }, 2, TimeUnit.SECONDS);
            
            // Task da eseguire periodicamente
            ScheduledFuture<?> periodicTask = scheduledPool.scheduleAtFixedRate(() -> {
                System.out.println("ScheduledPool - Task periodico: Esecuzione alle " + new Date());
            }, 0, 1, TimeUnit.SECONDS);
            
            // Lascia eseguire il task periodico per un po'
            Thread.sleep(3500);
            
            // Cancella il task periodico
            periodicTask.cancel(false);
            System.out.println("ScheduledPool - Task periodico cancellato");
            
        } finally {
            scheduledPool.shutdown();
            scheduledPool.awaitTermination(5, TimeUnit.SECONDS);
        }
    }
    
    /**
     * Dimostra l'uso di un WorkStealingPool (basato su Fork/Join), che utilizza
     * un algoritmo work-stealing per bilanciare il carico tra i thread.
     * Disponibile da Java 8.
     */
    private static void demoWorkStealingPool() throws InterruptedException {
        System.out.println("\n--- Work Stealing Pool (Fork/Join) ---");
        System.out.println("Caratteristiche: Utilizza l'algoritmo work-stealing per bilanciare il carico");
        System.out.println("Caso d'uso: Task ricorsivi, divide-and-conquer, parallelismo di dati");
        
        // Crea un pool con parallelismo pari al numero di processori disponibili
        ExecutorService workStealingPool = Executors.newWorkStealingPool();
        
        try {
            // Crea una lista di task computazionalmente intensivi
            List<Callable<String>> tasks = new ArrayList<>();
            for (int i = 1; i <= Runtime.getRuntime().availableProcessors(); i++) {
                final int taskId = i;
                tasks.add(() -> {
                    System.out.println("WorkStealingPool - Task " + taskId + ": Inizio su " + 
                            Thread.currentThread().getName());
                    
                    // Simulazione di un calcolo intensivo
                    long sum = 0;
                    for (long j = 0; j < 100_000_000L; j++) {
                        sum += j;
                    }
                    
                    System.out.println("WorkStealingPool - Task " + taskId + ": Completato");
                    return "Risultato del task " + taskId + ": " + sum;
                });
            }
            
            // Esecuzione di tutti i task
            List<Future<String>> results = workStealingPool.invokeAll(tasks);
            
            // Stampa dei risultati
            for (Future<String> result : results) {
                try {
                    System.out.println(result.get());
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            
        } finally {
            workStealingPool.shutdown();
            workStealingPool.awaitTermination(30, TimeUnit.SECONDS);
        }
    }
}