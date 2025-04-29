/**
 * Esempio che dimostra come gestire le eccezioni nei thread in Java
 * usando l'UncaughtExceptionHandler
 */
public class ES04_GestioneEccezioni {
    public static void main(String[] args) {
        System.out.println("Dimostrazione di gestione delle eccezioni nei thread");
        
        // Thread con eccezione non gestita senza handler
        Thread threadSenzaHandler = new Thread(() -> {
            System.out.println("Thread senza handler sta per generare un'eccezione...");
            throw new RuntimeException("Eccezione non gestita dal Thread");
        }, "ThreadSenzaHandler");
        
        // Thread con eccezione gestita internamente
        Thread threadConTryCatch = new Thread(() -> {
            try {
                System.out.println("Thread con try-catch sta per generare un'eccezione...");
                int result = 10 / 0; // Genera una ArithmeticException
            } catch (Exception e) {
                System.out.println("Eccezione catturata internamente: " + e.getMessage());
            }
        }, "ThreadConTryCatch");
        
        // Thread con handler di eccezione personalizzato
        Thread threadConHandler = new Thread(() -> {
            System.out.println("Thread con handler personalizzato sta per generare un'eccezione...");
            throw new IllegalStateException("Eccezione generata di proposito");
        }, "ThreadConHandler");
        
        // Impostazione di un handler personalizzato
        threadConHandler.setUncaughtExceptionHandler((t, e) -> {
            System.out.println("Handler personalizzato per il thread " + t.getName() + 
                              " ha catturato un'eccezione di tipo " + e.getClass().getSimpleName() +
                              ": " + e.getMessage());
        });
        
        // Handler globale per tutti i thread
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            System.out.println("Handler GLOBALE: Eccezione non gestita nel thread " + 
                              t.getName() + ": " + e.getMessage());
        });
        
        // Avvio dei thread
        threadConTryCatch.start();
        
        try {
            // Attendiamo il completamento del primo thread per chiarezza nell'output
            threadConTryCatch.join();
            
            threadConHandler.start();
            threadConHandler.join();
            
            threadSenzaHandler.start();
            threadSenzaHandler.join();
        } catch (InterruptedException e) {
            System.out.println("Thread principale interrotto");
        }
        
        System.out.println("\nNOTA: Le eccezioni non gestite in un thread normalmente causano "
                + "la stampa dello stack trace ma non terminano il programma principale");
    }
}
