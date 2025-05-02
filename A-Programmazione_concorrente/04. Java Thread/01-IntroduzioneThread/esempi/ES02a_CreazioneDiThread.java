/**
 * Esempio che dimostra i due principali metodi per creare thread in Java:
 * 1. Implementando l'interfaccia Runnable
 * 2. Estendendo la classe Thread
 * Inoltre, mostra come utilizzare le lambda expressions per creare thread in modo più conciso.
 */
public class ES02a_CreazioneDiThread {
    public static void main(String[] args) {
        System.out.println("Thread principale: " + Thread.currentThread().getName());
        
        // Creazione di thread con l'interfaccia Runnable
        Thread t1 = new Thread(new MioRunnableTask(), "Thread-Runnable");
        
        // Creazione di thread estendendo la classe Thread
        MioThread t2 = new MioThread("Thread-Esteso");
        
        // Creazione di thread con lambda expression (Java 8+)
        Thread t3 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("Thread Lambda " + Thread.currentThread().getId() + ": iterazione " + i);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    System.out.println("Thread Lambda interrotto");
                }
            }
        }, "Thread-Lambda");
        
        // Avvio dei thread
        t1.start();
        t2.start();
        t3.start();
        
        // Attesa del completamento dei thread
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            System.out.println("Thread principale interrotto");
        }
        
        System.out.println("Tutti i thread hanno completato l'esecuzione");
    }
}

/**
 * Implementazione di Runnable
 */
class MioRunnableTask implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println(Thread.currentThread().getName() + ": iterazione " + i);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " interrotto");
            }
        }
    }
}

/**
 * Estensione della classe Thread
 */
class MioThread extends Thread {
    public MioThread(String name) {
        super(name);
    }
    
    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println(getName() + ": iterazione " + i);
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                System.out.println(getName() + " interrotto");
            }
        }
    }
}

/**
 * Output: java ES01_CreazioneDiThread

    Thread principale: main
    Thread Lambda: iterazione 0
    Thread-Esteso: iterazione 0
    Thread-Runnable: iterazione 0
    Thread-Runnable: iterazione 1
    Thread-Esteso: iterazione 1
    Thread Lambda: iterazione 1
    Thread-Runnable: iterazione 2
    Thread-Esteso: iterazione 2
    Thread-Runnable: iterazione 3
    Thread Lambda: iterazione 2
    Thread-Runnable: iterazione 4
    Thread-Esteso: iterazione 3
    Thread Lambda: iterazione 3
    Thread-Esteso: iterazione 4
    Thread Lambda: iterazione 4
    Tutti i thread hanno completato l'esecuzione

 */
