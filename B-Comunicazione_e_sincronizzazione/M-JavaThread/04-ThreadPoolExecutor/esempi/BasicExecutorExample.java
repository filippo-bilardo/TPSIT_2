package esempi;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Esempio base di utilizzo di un Executor per eseguire task concorrenti.
 * Questo esempio mostra come creare un ExecutorService, sottomettere task
 * e gestire correttamente lo shutdown dell'executor.
 */
public class BasicExecutorExample {

    public static void main(String[] args) {
        System.out.println("Avvio dell'esempio di base di Executor");
        
        // Creazione di un ExecutorService con un pool di thread fisso
        ExecutorService executor = Executors.newFixedThreadPool(3);
        
        try {
            // Sottomissione di 5 task all'executor
            for (int i = 1; i <= 5; i++) {
                final int taskId = i;
                executor.execute(() -> {
                    // Simulazione di un'operazione che richiede tempo
                    try {
                        System.out.println("Task " + taskId + ": Inizio dell'esecuzione - Thread: " + 
                                Thread.currentThread().getName());
                        
                        // Simulazione di un'operazione che richiede tempo
                        Thread.sleep((long) (Math.random() * 2000));
                        
                        System.out.println("Task " + taskId + ": Completato - Thread: " + 
                                Thread.currentThread().getName());
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.out.println("Task " + taskId + ": Interrotto");
                    }
                });
                
                System.out.println("Task " + taskId + ": Sottomesso all'executor");
            }
            
            System.out.println("Tutti i task sono stati sottomessi");
            
        } finally {
            // Avvio della procedura di shutdown dell'executor
            // Non accetta nuovi task ma completa quelli già sottomessi
            executor.shutdown();
            System.out.println("Executor shutdown iniziato");
            
            try {
                // Attesa del completamento di tutti i task o del timeout
                if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                    // Forza lo shutdown se i task non terminano entro il timeout
                    System.out.println("Alcuni task non hanno terminato, forzo lo shutdown");
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                // Ripristina lo stato di interruzione e forza lo shutdown
                Thread.currentThread().interrupt();
                executor.shutdownNow();
                System.out.println("Shutdown forzato a causa di interruzione");
            }
            
            System.out.println("Executor terminato");
        }
        
        System.out.println("Esempio completato");
    }
}