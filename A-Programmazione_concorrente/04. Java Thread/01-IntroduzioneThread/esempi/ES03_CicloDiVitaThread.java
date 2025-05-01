/**
 * Esempio che dimostra il ciclo di vita di un thread in Java
 * e i vari stati attraverso cui passa: NEW, RUNNABLE, BLOCKED, WAITING, TIMED_WAITING, TERMINATED
 */
public class ES03_CicloDiVitaThread {
    public static void main(String[] args) throws InterruptedException {
        // Creiamo un oggetto condiviso per la sincronizzazione
        Object lock = new Object();
        
        // Thread che mostrerà i diversi stati
        Thread lifeCycleThread = new Thread(() -> {
            System.out.println("Thread avviato");
            
            // Simula un lavoro
            for (int i = 0; i < 3; i++) {
                System.out.println("Lavoro in corso: " + i);
                try {
                    Thread.sleep(500); // TIMED_WAITING
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            
            synchronized(lock) {
                try {
                    System.out.println("Thread in attesa");
                    lock.wait(1000); // WAITING
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            
            System.out.println("Thread terminato");
        }, "LifeCycleThread");
        
        // Thread che sarà in stato BLOCKED
        Thread blockingThread = new Thread(() -> {
            synchronized(lock) {
                System.out.println("Thread bloccante ha acquisito il lock");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "BlockingThread");
        
        // Stato NEW prima di start()
        System.out.println("Stato di lifeCycleThread: " + lifeCycleThread.getState());
        
        // Avviamo il thread bloccante per primo
        blockingThread.start();
        Thread.sleep(100); // Diamo tempo al blockingThread di acquisire il lock
        
        // Avviamo il thread del ciclo di vita
        lifeCycleThread.start();
        
        // Stato RUNNABLE dopo start()
        System.out.println("Stato dopo start(): " + lifeCycleThread.getState());
        
        // Attendiamo un po' per dare tempo al thread di entrare in TIMED_WAITING
        Thread.sleep(600);
        System.out.println("Stato durante sleep(): " + lifeCycleThread.getState());
        
        // Attendiamo che il thread tenti di acquisire il lock (dovrebbe andare in BLOCKED)
        Thread.sleep(1500);
        System.out.println("Stato mentre tenta di acquisire lock: " + lifeCycleThread.getState());
        
        // Attendiamo che tutti i thread terminino
        blockingThread.join();
        lifeCycleThread.join();
        
        // Stato TERMINATED dopo join()
        System.out.println("Stato finale: " + lifeCycleThread.getState());
    }
}
/**
    Output:

    Stato di lifeCycleThread: NEW
    Thread bloccante ha acquisito il lock
    Stato dopo start(): RUNNABLE
    Thread avviato
    Lavoro in corso: 0
    Lavoro in corso: 1
    Stato durante sleep(): TIMED_WAITING
    Lavoro in corso: 2
    Thread in attesa
    Stato mentre tenta di acquisire lock: TIMED_WAITING
    Thread terminato
    Stato finale: TERMINATED

 */