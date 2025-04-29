/**
 * Esempio 05: Thread Daemon
 * 
 * Questo esempio dimostra il concetto di thread daemon in Java.
 * I thread daemon sono thread di servizio che vengono terminati automaticamente
 * quando tutti i thread non-daemon sono terminati.
 * 
 * Sono utili per attività di background come garbage collection, pulizia delle risorse,
 * o monitoraggio che non devono bloccare la terminazione del programma.
 */
public class ES05_ThreadDaemon {
    public static void main(String[] args) {
        System.out.println("Dimostrazione dei thread daemon");
        System.out.println("==============================\n");
        
        // Creiamo un thread normale (non-daemon)
        Thread threadNormale = new Thread(() -> {
            System.out.println("Thread normale: iniziato");
            try {
                // Esegue un'operazione per 2 secondi
                for (int i = 1; i <= 5; i++) {
                    System.out.println("Thread normale: lavorando... " + i);
                    Thread.sleep(400);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Thread normale: completato");
        }, "ThreadNormale");
        
        // Creiamo un thread daemon
        Thread threadDaemon = new Thread(() -> {
            System.out.println("Thread daemon: iniziato");
            int count = 1;
            try {
                // Loop infinito - il thread daemon continuerà finché ci sono thread non-daemon attivi
                while (true) {
                    System.out.println("Thread daemon: esecuzione continua... " + count++);
                    Thread.sleep(300);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Questa riga non verrà mai eseguita perché il thread daemon verrà terminato
            // automaticamente quando il thread principale e gli altri thread non-daemon terminano
            System.out.println("Thread daemon: completato (questa riga non verrà mai stampata)");
        }, "ThreadDaemon");
        
        // Impostiamo il thread come daemon
        threadDaemon.setDaemon(true);
        
        System.out.println("Thread principale: avvio dei thread");
        System.out.println("- " + threadNormale.getName() + ": è daemon? " + threadNormale.isDaemon());
        System.out.println("- " + threadDaemon.getName() + ": è daemon? " + threadDaemon.isDaemon());
        
        // Avviamo entrambi i thread
        threadNormale.start();
        threadDaemon.start();
        
        // Attendiamo il completamento del thread normale
        try {
            threadNormale.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("\nThread principale: il thread normale è terminato");
        System.out.println("Thread principale: in uscita dal programma");
        System.out.println("Nota: Il thread daemon verrà terminato automaticamente");
        System.out.println("      quando il thread principale termina.");
        
        // Aggiungiamo una breve pausa per dimostrare che il thread daemon
        // è ancora in esecuzione prima che il programma termini
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}