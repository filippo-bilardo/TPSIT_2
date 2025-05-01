/**
 * Esempio 03: Ciclo di Vita dei Thread in Java
 * 
 * Questo esempio illustra i diversi stati del ciclo di vita di un thread:
 * - NEW: thread creato ma non ancora avviato
 * - RUNNABLE: thread in esecuzione o pronto per essere eseguito
 * - BLOCKED: thread bloccato in attesa di un monitor lock
 * - WAITING: thread in attesa indefinita che un altro thread esegua una particolare azione
 * - TIMED_WAITING: thread in attesa per un tempo specificato
 * - TERMINATED: thread che ha completato l'esecuzione
 */
public class ES03_CicloVitaThread { 
    public static void main(String[] args) {
        System.out.println("Dimostrazione del ciclo di vita dei thread");
        System.out.println("=========================================");
        
        // Creiamo un oggetto condiviso per la sincronizzazione
        final Object lock = new Object();
        
        // Creiamo un thread di esempio
        Thread thread = new Thread(() -> {
            try {
                // Mostriamo lo stato RUNNABLE
                System.out.println("Thread è in stato: " + Thread.currentThread().getState());
                
                // Simuliamo un'operazione
                System.out.println("Thread sta eseguendo un'operazione...");
                Thread.sleep(1000);
                
                // Entriamo nello stato WAITING
                System.out.println("\nThread sta per entrare in stato WAITING...");
                synchronized (lock) {
                    lock.wait(2000); // Attesa con timeout (TIMED_WAITING)
                }
                
                // Dopo il risveglio
                System.out.println("Thread è stato risvegliato!");
                
                // Simuliamo un'altra operazione
                System.out.println("Thread sta completando...");
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("Thread è stato interrotto");
            }
        });
        
        // Stato NEW
        System.out.println("\n1. Stato NEW - Thread creato ma non avviato");
        System.out.println("   Thread è in stato: " + thread.getState());
        
        // Avviamo il thread - passa a RUNNABLE
        System.out.println("\n2. Avvio del thread - passa a RUNNABLE");
        thread.start();
        
        // Diamo al thread il tempo di iniziare
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Mostriamo lo stato TIMED_WAITING
        try {
            Thread.sleep(1000);
            System.out.println("\n3. Thread in attesa temporizzata (TIMED_WAITING)");
            System.out.println("   Thread è in stato: " + thread.getState());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Risvegliamo il thread
        synchronized (lock) {
            System.out.println("\n4. Risveglio del thread");
            lock.notifyAll();
        }
        
        // Attendiamo che il thread termini
        try {
            thread.join();
            
            // Stato TERMINATED
            System.out.println("\n5. Stato TERMINATED - Thread ha completato l'esecuzione");
            System.out.println("   Thread è in stato: " + thread.getState());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("\nNota: Lo stato BLOCKED non è stato mostrato in questo esempio.");
        System.out.println("Un thread entra in stato BLOCKED quando tenta di acquisire un");
        System.out.println("monitor lock già detenuto da un altro thread.");
    }
}
/**
 * Esempio di Output:

    Dimostrazione del ciclo di vita dei thread
    =========================================

    1. Stato NEW - Thread creato ma non avviato
    Thread è in stato: NEW

    2. Avvio del thread - passa a RUNNABLE
    Thread è in stato: RUNNABLE
    Thread sta eseguendo un'operazione...

    Thread sta per entrare in stato WAITING...

    3. Thread in attesa temporizzata (TIMED_WAITING)
    Thread è in stato: TIMED_WAITING

    4. Risveglio del thread
    Thread è stato risvegliato!
    Thread sta completando...

    5. Stato TERMINATED - Thread ha completato l'esecuzione
    Thread è in stato: TERMINATED

    Nota: Lo stato BLOCKED non è stato mostrato in questo esempio.
    Un thread entra in stato BLOCKED quando tenta di acquisire un
    monitor lock già detenuto da un altro thread. 
 */