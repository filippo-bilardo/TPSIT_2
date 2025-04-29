/**
 * Esempio 08: Gestione delle Eccezioni non Catturate nei Thread
 * 
 * Questo esempio dimostra come gestire le eccezioni non catturate nei thread utilizzando
 * l'interfaccia UncaughtExceptionHandler. Quando un'eccezione non viene catturata all'interno
 * di un thread, normalmente il thread termina senza influenzare gli altri thread o il thread principale.
 * 
 * L'UncaughtExceptionHandler permette di definire un comportamento personalizzato quando
 * si verifica un'eccezione non catturata in un thread.
 */
public class ES08_UncaughtExceptionHandler {
    public static void main(String[] args) {
        System.out.println("Dimostrazione della gestione delle eccezioni non catturate nei thread");
        System.out.println("==================================================================\n");
        
        // Thread senza handler personalizzato
        Thread threadSenzaHandler = new Thread(() -> {
            System.out.println("Thread senza handler: iniziato");
            System.out.println("Thread senza handler: sto per generare un'eccezione");
            // Generiamo un'eccezione non catturata
            throw new RuntimeException("Eccezione di esempio nel thread senza handler");
        }, "ThreadSenzaHandler");
        
        // Thread con handler personalizzato
        Thread threadConHandler = new Thread(() -> {
            System.out.println("Thread con handler: iniziato");
            System.out.println("Thread con handler: sto per generare un'eccezione");
            // Generiamo un'eccezione non catturata
            throw new RuntimeException("Eccezione di esempio nel thread con handler");
        }, "ThreadConHandler");
        
        // Impostiamo un handler personalizzato per il secondo thread
        threadConHandler.setUncaughtExceptionHandler((thread, exception) -> {
            System.out.println("\nHandler personalizzato per " + thread.getName() + " ha catturato un'eccezione:");
            System.out.println("- Tipo di eccezione: " + exception.getClass().getName());
            System.out.println("- Messaggio: " + exception.getMessage());
            System.out.println("- Stack trace:");
            for (StackTraceElement element : exception.getStackTrace()) {
                System.out.println("  " + element);
            }
            System.out.println("L'handler può eseguire operazioni di pulizia o logging qui");
        });
        
        // Impostiamo un handler predefinito per tutti i thread che non hanno un handler specifico
        Thread.setDefaultUncaughtExceptionHandler((thread, exception) -> {
            System.out.println("\nHandler predefinito ha catturato un'eccezione in " + thread.getName() + ":");
            System.out.println("- Messaggio: " + exception.getMessage());
        });
        
        System.out.println("Avvio dei thread:");
        threadSenzaHandler.start();
        
        // Attendiamo un po' prima di avviare il secondo thread
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        threadConHandler.start();
        
        // Attendiamo che entrambi i thread terminino
        try {
            threadSenzaHandler.join();
            threadConHandler.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("\nNota: Entrambi i thread sono terminati dopo aver generato eccezioni");
        System.out.println("Il thread principale continua la sua esecuzione normalmente");
        
        System.out.println("\nBest practices per la gestione delle eccezioni nei thread:");
        System.out.println("1. Utilizzare try-catch all'interno del codice del thread per gestire le eccezioni localmente");
        System.out.println("2. Impostare un UncaughtExceptionHandler per gestire le eccezioni non catturate");
        System.out.println("3. Utilizzare l'handler per registrare informazioni di debug, notificare altri componenti");
        System.out.println("   o eseguire operazioni di pulizia prima che il thread termini");
    }
}