/**
 * Esempio 02: Pattern Thread-Per-Message
 * 
 * Questo esempio dimostra l'implementazione del pattern Thread-Per-Message, che:
 * - Crea un nuovo thread per ogni richiesta o messaggio da elaborare
 * - È utile per operazioni asincrone che non richiedono un risultato immediato
 * - Può essere implementato con thread espliciti o con ExecutorService
 */
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ES02_ThreadPerMessagePattern {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Dimostrazione del Pattern Thread-Per-Message");
        System.out.println("===========================================");
        
        // Esempio 1: Implementazione con thread espliciti
        System.out.println("\n1. Implementazione con thread espliciti:");
        ServerConThreadEspliciti server1 = new ServerConThreadEspliciti();
        for (int i = 1; i <= 5; i++) {
            server1.elaboraRichiesta("Richiesta-" + i);
        }
        
        // Breve pausa per vedere l'output
        Thread.sleep(1000);
        
        // Esempio 2: Implementazione con ExecutorService
        System.out.println("\n2. Implementazione con ExecutorService:");
        ServerConExecutor server2 = new ServerConExecutor();
        for (int i = 1; i <= 5; i++) {
            server2.elaboraRichiesta("Richiesta-" + i);
        }
        
        // Attende il completamento e chiude l'executor
        Thread.sleep(2000);
        server2.shutdown();
        
        // Esempio 3: Implementazione con CompletableFuture (Java 8+)
        System.out.println("\n3. Implementazione con CompletableFuture:");
        ServerConCompletableFuture server3 = new ServerConCompletableFuture();
        for (int i = 1; i <= 5; i++) {
            server3.elaboraRichiesta("Richiesta-" + i);
        }
        
        // Attende il completamento e chiude l'executor
        Thread.sleep(2000);
        server3.shutdown();
        
        System.out.println("\nVantaggi del Pattern Thread-Per-Message:");
        System.out.println("1. Semplice da implementare e comprendere");
        System.out.println("2. Disaccoppia l'invocazione dall'esecuzione");
        System.out.println("3. Migliora la reattività dell'applicazione");
        System.out.println("4. Ideale per operazioni asincrone senza risultato");
        
        System.out.println("\nSvantaggi del Pattern Thread-Per-Message:");
        System.out.println("1. Può creare troppi thread se il carico è elevato");
        System.out.println("2. Overhead di creazione thread (mitigato con thread pool)");
        System.out.println("3. Non adatto quando è necessario un risultato immediato");
        System.out.println("4. Richiede attenzione alla gestione delle eccezioni");
    }
}

/**
 * Implementazione del pattern Thread-Per-Message usando thread espliciti
 */
class ServerConThreadEspliciti {
    /**
     * Elabora una richiesta in un nuovo thread dedicato
     */
    public void elaboraRichiesta(String richiesta) {
        System.out.println("Server riceve: " + richiesta);
        
        // Crea un nuovo thread per ogni richiesta
        Thread thread = new Thread(() -> {
            // Simula elaborazione
            try {
                System.out.println("Inizio elaborazione: " + richiesta + 
                        " [Thread: " + Thread.currentThread().getName() + "]");
                Thread.sleep((int)(Math.random() * 1000)); // Tempo casuale
                System.out.println("Fine elaborazione: " + richiesta + 
                        " [Thread: " + Thread.currentThread().getName() + "]");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Elaborazione interrotta: " + richiesta);
            }
        });
        
        // Imposta come daemon per non bloccare la JVM
        thread.setDaemon(true);
        thread.start();
    }
}

/**
 * Implementazione del pattern Thread-Per-Message usando ExecutorService
 */
class ServerConExecutor {
    private final ExecutorService executor;
    
    public ServerConExecutor() {
        // Crea un pool di thread per gestire le richieste
        this.executor = Executors.newCachedThreadPool(r -> {
            Thread t = new Thread(r);
            t.setDaemon(true); // Thread daemon
            return t;
        });
    }
    
    /**
     * Elabora una richiesta usando un thread dal pool
     */
    public void elaboraRichiesta(String richiesta) {
        System.out.println("Server riceve: " + richiesta);
        
        // Sottomette il task all'executor
        executor.execute(() -> {
            // Simula elaborazione
            try {
                System.out.println("Inizio elaborazione: " + richiesta + 
                        " [Thread: " + Thread.currentThread().getName() + "]");
                Thread.sleep((int)(Math.random() * 1000)); // Tempo casuale
                System.out.println("Fine elaborazione: " + richiesta + 
                        " [Thread: " + Thread.currentThread().getName() + "]");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Elaborazione interrotta: " + richiesta);
            }
        });
    }
    
    /**
     * Chiude l'executor
     */
    public void shutdown() throws InterruptedException {
        System.out.println("Chiusura executor...");
        executor.shutdown();
        executor.awaitTermination(2, TimeUnit.SECONDS);
    }
}

/**
 * Implementazione del pattern Thread-Per-Message usando CompletableFuture
 */
class ServerConCompletableFuture {
    private final ExecutorService executor;
    
    public ServerConCompletableFuture() {
        // Crea un pool di thread per gestire le richieste
        this.executor = Executors.newCachedThreadPool(r -> {
            Thread t = new Thread(r);
            t.setDaemon(true); // Thread daemon
            return t;
        });
    }
    
    /**
     * Elabora una richiesta usando CompletableFuture
     */
    public void elaboraRichiesta(String richiesta) {
        System.out.println("Server riceve: " + richiesta);
        
        // Crea un CompletableFuture che esegue l'elaborazione in modo asincrono
        java.util.concurrent.CompletableFuture.runAsync(() -> {
            // Simula elaborazione
            try {
                System.out.println("Inizio elaborazione: " + richiesta + 
                        " [Thread: " + Thread.currentThread().getName() + "]");
                Thread.sleep((int)(Math.random() * 1000)); // Tempo casuale
                System.out.println("Fine elaborazione: " + richiesta + 
                        " [Thread: " + Thread.currentThread().getName() + "]");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Elaborazione interrotta: " + richiesta);
            }
        }, executor).exceptionally(ex -> {
            // Gestione eccezioni
            System.err.println("Errore durante l'elaborazione di " + richiesta + ": " + ex.getMessage());
            return null;
        });
    }
    
    /**
     * Chiude l'executor
     */
    public void shutdown() throws InterruptedException {
        System.out.println("Chiusura executor...");
        executor.shutdown();
        executor.awaitTermination(2, TimeUnit.SECONDS);
    }
}