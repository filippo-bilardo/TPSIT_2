import java.lang.Thread.State;

/**
 * Esempio 06: Stati di un Thread
 * 
 * Questo esempio dimostra i vari stati attraverso cui un thread può passare
 * durante il suo ciclo di vita in Java: NEW, RUNNABLE, BLOCKED, WAITING,
 * TIMED_WAITING, TERMINATED.
 */
public class ES03_ThreadStates {

    // Oggetto usato per la sincronizzazione (dimostrazione stato BLOCKED)
    private static final Object blockLock = new Object();
    // Oggetto usato per la sincronizzazione (dimostrazione stato WAITING)
    private static final Object waitLock = new Object();

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Dimostrazione degli stati di un thread");
        System.out.println("====================================\n");

        // --- Stato NEW ---
        // Un thread è nello stato NEW quando è stato creato ma non ancora avviato.
        Thread thread = new Thread(() -> {
            // Questo codice verrà eseguito quando il thread passa allo stato RUNNABLE
            System.out.println("Thread in esecuzione...");
            try {
                // Dimostrazione stato TIMED_WAITING
                System.out.println("Thread (" + Thread.currentThread().getName() + ") - Stato prima di sleep: " + Thread.currentThread().getState());
                Thread.sleep(1500); // Il thread attende per un tempo specificato
                System.out.println("Thread (" + Thread.currentThread().getName() + ") - Stato dopo sleep: " + Thread.currentThread().getState());

                // Gli stati BLOCKED e WAITING verranno dimostrati nel thread principale
                // usando thread separati per un controllo migliore della sincronizzazione.

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Thread interrotto.");
            }
            System.out.println("Thread (" + Thread.currentThread().getName() + ") - Sta per terminare.");
        }, "DemoThread");

        System.out.println("1. Stato NEW:");
        System.out.println("   - Stato del thread '" + thread.getName() + "' dopo la creazione: " + thread.getState());
        System.out.println("   (Il thread è stato creato ma non ancora avviato con start())\n");

        // --- Stato RUNNABLE ---
        // Un thread passa allo stato RUNNABLE quando viene chiamato il metodo start().
        // È pronto per essere eseguito dallo scheduler della JVM.
        System.out.println("2. Stato RUNNABLE:");
        System.out.println("   Avvio del thread...");
        thread.start(); // Avvia il thread
        // Nota: Lo stato potrebbe essere ancora NEW subito dopo start(), 
        //       ma diventa rapidamente RUNNABLE.
        //       Per vederlo RUNNABLE con certezza, potremmo dover attendere un istante.
        Thread.sleep(50); // Breve pausa per dare tempo al thread di avviarsi
        System.out.println("   - Stato del thread '" + thread.getName() + "' dopo start(): " + thread.getState());
        System.out.println("   (Il thread è in esecuzione o pronto per essere eseguito)\n");

        // --- Stato TIMED_WAITING ---
        // Il thread entra in questo stato quando chiama metodi come sleep(long),
        // wait(long), join(long). Attende per un periodo specificato.
        System.out.println("3. Stato TIMED_WAITING:");
        // Attendiamo un po' affinché il thread entri nello sleep()
        Thread.sleep(500);
        System.out.println("   - Stato del thread '" + thread.getName() + "' durante sleep(): " + thread.getState());
        System.out.println("   (Il thread sta aspettando per un tempo definito)\n");

        // Attendiamo che il thread esca da sleep()
        Thread.sleep(1200); // Attesa maggiore del tempo di sleep del thread

        // --- Stato BLOCKED ---
        // Un thread entra nello stato BLOCKED quando tenta di acquisire un lock
        // (tramite blocco synchronized) che è già detenuto da un altro thread.
        System.out.println("4. Stato BLOCKED:");
        Thread threadBloccante = new Thread(() -> {
            System.out.println("   Thread '" + Thread.currentThread().getName() + "' tenta di acquisire blockLock...");
            synchronized (blockLock) {
                System.out.println("   Thread '" + Thread.currentThread().getName() + "' ha acquisito blockLock.");
                // Simula lavoro tenendo il lock
                try { Thread.sleep(50); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }
            System.out.println("   Thread '" + Thread.currentThread().getName() + "' ha rilasciato blockLock.");
        }, "BloccanteThread");

        synchronized (blockLock) { // Il thread principale acquisisce il lock
            System.out.println("   Thread principale ('"+ Thread.currentThread().getName() +"') detiene blockLock.");
            threadBloccante.start();
            Thread.sleep(100); // Diamo tempo al thread bloccante di avviarsi e tentare di acquisire il lock
            System.out.println("   - Stato del thread '" + threadBloccante.getName() + "' mentre attende il lock: " + threadBloccante.getState());
            System.out.println("   (Il thread è BLOCKED perché il thread principale detiene blockLock)\n");
            System.out.println("   Thread principale ('"+ Thread.currentThread().getName() +"') sta per rilasciare blockLock.");
        } // Fine blocco synchronized, blockLock viene rilasciato

        // Attendiamo che threadBloccante possa acquisire il lock e terminare
        threadBloccante.join();
        System.out.println("   Thread '" + threadBloccante.getName() + "' ha terminato. ");
        System.out.println("   - Stato del thread '" + threadBloccante.getName() + "' dopo aver rilasciato blockLock: " + threadBloccante.getState());
        System.out.println("   (Il thread è TERMINATED perché ha completato la sua esecuzione)\n    ");

        // --- Stato WAITING ---
        // Un thread entra nello stato WAITING quando chiama metodi come wait(),
        // join() senza timeout, o LockSupport.park(). Attende indefinitamente
        // che un altro thread esegua un'azione specifica (es. notify(), notifyAll()).
        System.out.println("5. Stato WAITING:");

        Thread threadInAttesa = new Thread(() -> {
            synchronized(waitLock) {
                try {
                    System.out.println("   Thread '" + Thread.currentThread().getName() + "' entra in wait() su waitLock.");
                    waitLock.wait(); // Entra in stato WAITING
                    System.out.println("   Thread '" + Thread.currentThread().getName() + "' risvegliato da wait().");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("   Thread '" + Thread.currentThread().getName() + "' interrotto durante wait().");
                }
            }
            System.out.println("   Thread '" + Thread.currentThread().getName() + "' ha terminato.");
        }, "WaitingThread");

        Thread notificatore = new Thread(() -> {
            try {
                // Attende un po' per dare tempo a WaitingThread di entrare in wait()
                Thread.sleep(500);
                System.out.println("   Thread Notificatore ('"+ Thread.currentThread().getName() +"'): tenta di acquisire waitLock per notificare.");
                synchronized (waitLock) {
                    System.out.println("   Thread Notificatore ('"+ Thread.currentThread().getName() +"'): ha acquisito waitLock, sta per notificare.");
                    waitLock.notify();
                    System.out.println("   Thread Notificatore ('"+ Thread.currentThread().getName() +"'): ha notificato, rilascia waitLock.");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
             System.out.println("   Thread Notificatore ('"+ Thread.currentThread().getName() +"') ha terminato.");
        }, "NotificatoreThread");

        threadInAttesa.start();
        notificatore.start();

        // Diamo tempo a WaitingThread di avviarsi ed entrare in wait()
        Thread.sleep(100);
        System.out.println("   - Stato del thread '" + threadInAttesa.getName() + "' dopo aver chiamato wait(): " + threadInAttesa.getState());
        System.out.println("   (Il thread è WAITING finché non viene chiamato notify()/notifyAll() su waitLock)\n");

        // Attendiamo che entrambi i thread (attesa e notifica) terminino
        threadInAttesa.join();
        notificatore.join();
        System.out.println("   Thread '" + threadInAttesa.getName() + "' e '" + notificatore.getName() + "' hanno terminato.");
        
        // --- Stato TERMINATED ---
        // Un thread entra nello stato TERMINATED quando ha completato la sua esecuzione
        // o è stato terminato in modo anomalo.
        System.out.println("\n6. Stato TERMINATED:");
        System.out.println("   - Stato del thread '" + thread.getName() + "' dopo il completamento: " + thread.getState());
        System.out.println("   (Il thread ha completato la sua esecuzione)\n");
        
        System.out.println("Dimostrazione degli stati di un thread completata.");
    }
}
/**
 * Output:

    Dimostrazione degli stati di un thread
    ====================================

    1. Stato NEW:
    - Stato del thread 'DemoThread' dopo la creazione: NEW
    (Il thread è stato creato ma non ancora avviato con start())

    2. Stato RUNNABLE:
    Avvio del thread...
    Thread in esecuzione...
    Thread (DemoThread) - Stato prima di sleep: RUNNABLE
    - Stato del thread 'DemoThread' dopo start(): TIMED_WAITING
    (Il thread è in esecuzione o pronto per essere eseguito)

    3. Stato TIMED_WAITING:
    - Stato del thread 'DemoThread' durante sleep(): TIMED_WAITING
    (Il thread sta aspettando per un tempo definito)

    Thread (DemoThread) - Stato dopo sleep: RUNNABLE
    Thread (DemoThread) - Sta per terminare.
    4. Stato BLOCKED:
    Thread principale ('main') detiene blockLock.
    Thread 'BloccanteThread' tenta di acquisire blockLock...
    - Stato del thread 'BloccanteThread' mentre attende il lock: BLOCKED
    (Il thread è BLOCKED perché il thread principale detiene blockLock)

    Thread principale ('main') sta per rilasciare blockLock.
    Thread 'BloccanteThread' ha acquisito blockLock.
    Thread 'BloccanteThread' ha rilasciato blockLock.
    Thread 'BloccanteThread' ha terminato. 
    - Stato del thread 'BloccanteThread' dopo aver rilasciato blockLock: TERMINATED
    (Il thread è TERMINATED perché ha completato la sua esecuzione)
        
    5. Stato WAITING:
    Thread 'WaitingThread' entra in wait() su waitLock.
    - Stato del thread 'WaitingThread' dopo aver chiamato wait(): WAITING
    (Il thread è WAITING finché non viene chiamato notify()/notifyAll() su waitLock)

    Thread Notificatore ('NotificatoreThread'): tenta di acquisire waitLock per notificare.
    Thread Notificatore ('NotificatoreThread'): ha acquisito waitLock, sta per notificare.
    Thread Notificatore ('NotificatoreThread'): ha notificato, rilascia waitLock.
    Thread 'WaitingThread' risvegliato da wait().
    Thread 'WaitingThread' ha terminato.
    Thread Notificatore ('NotificatoreThread') ha terminato.
    Thread 'WaitingThread' e 'NotificatoreThread' hanno terminato.

    6. Stato TERMINATED:
    - Stato del thread 'DemoThread' dopo il completamento: TERMINATED
    (Il thread ha completato la sua esecuzione)

    Dimostrazione degli stati di un thread completata.

 * 
 */