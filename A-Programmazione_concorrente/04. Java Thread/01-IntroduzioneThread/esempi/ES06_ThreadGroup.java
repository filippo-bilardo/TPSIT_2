/**
 * Esempio 06: Gruppi di Thread
 * 
 * Questo esempio dimostra come utilizzare i ThreadGroup in Java.
 * I ThreadGroup permettono di raggruppare più thread correlati e gestirli come un'unica entità.
 * Sono utili per applicare operazioni collettive come interrompere tutti i thread di un gruppo
 * o impostare la stessa priorità per tutti i thread del gruppo.
 */
public class ES06_ThreadGroup {
    public static void main(String[] args) {
        System.out.println("Dimostrazione dei gruppi di thread");
        System.out.println("=================================\n");
        
        // Otteniamo il gruppo di thread principale
        ThreadGroup gruppoMain = Thread.currentThread().getThreadGroup();
        System.out.println("Thread principale appartiene al gruppo: " + gruppoMain.getName());
        
        // Creiamo un nuovo gruppo di thread
        ThreadGroup gruppoWorker = new ThreadGroup("GruppoWorker");
        System.out.println("Creato nuovo gruppo di thread: " + gruppoWorker.getName());
        System.out.println("Il gruppo è un sottogruppo di: " + gruppoWorker.getParent().getName());
        
        // Creiamo alcuni thread nel gruppo worker
        Thread[] workers = new Thread[3];
        for (int i = 0; i < workers.length; i++) {
            workers[i] = new Thread(gruppoWorker, () -> {
                String threadName = Thread.currentThread().getName();
                System.out.println(threadName + ": iniziato");
                try {
                    // Simuliamo un'operazione
                    for (int j = 0; j < 5; j++) {
                        System.out.println(threadName + ": lavorando... " + j);
                        Thread.sleep(300);
                    }
                } catch (InterruptedException e) {
                    System.out.println(threadName + ": interrotto");
                    return; // Usciamo dal thread se viene interrotto
                }
                System.out.println(threadName + ": completato");
            }, "Worker-" + i);
        }
        
        // Impostiamo la priorità per tutto il gruppo
        gruppoWorker.setMaxPriority(7);
        System.out.println("Priorità massima impostata per il gruppo: " + gruppoWorker.getMaxPriority());
        
        // Avviamo i thread
        System.out.println("\nAvvio dei thread nel gruppo " + gruppoWorker.getName() + ":");
        for (Thread worker : workers) {
            worker.start();
            System.out.println("- " + worker.getName() + ": avviato (priorità: " + worker.getPriority() + ")");
        }
        
        // Mostriamo informazioni sul gruppo
        System.out.println("\nInformazioni sul gruppo " + gruppoWorker.getName() + ":");
        System.out.println("- Thread attivi nel gruppo: " + gruppoWorker.activeCount());
        System.out.println("- Gruppi attivi nel gruppo: " + gruppoWorker.activeGroupCount());
        
        // Attendiamo un po' e poi interrompiamo tutti i thread nel gruppo
        try {
            Thread.sleep(1000);
            System.out.println("\nInterruzione di tutti i thread nel gruppo " + gruppoWorker.getName());
            gruppoWorker.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Attendiamo che tutti i thread nel gruppo terminino
        try {
            for (Thread worker : workers) {
                worker.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("\nTutti i thread nel gruppo " + gruppoWorker.getName() + " sono terminati");
        System.out.println("Thread attivi nel gruppo: " + gruppoWorker.activeCount());
        
        System.out.println("\nNota: I ThreadGroup sono considerati una funzionalità legacy in Java moderno.");
        System.out.println("Per applicazioni nuove, è preferibile utilizzare gli Executor o altri");
        System.out.println("meccanismi di gestione dei thread forniti dal pacchetto java.util.concurrent.");
    }
}