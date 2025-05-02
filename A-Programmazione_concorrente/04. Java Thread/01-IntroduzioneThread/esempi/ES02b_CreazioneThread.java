/**
 * Esempio 02: Creazione di Thread in Java
 * 
 * Questo esempio mostra i due approcci principali per creare thread in Java:
 * 1. Estendendo la classe Thread
 * 2. Implementando l'interfaccia Runnable
 */
public class ES02b_CreazioneThread {
    public static void main(String[] args) {
        System.out.println("Dimostrazione dei metodi di creazione dei thread");
        System.out.println("=============================================");
        
        // 1. Creazione di thread estendendo la classe Thread
        System.out.println("\n1. Creazione di thread estendendo la classe Thread:");
        ThreadEreditato thread1 = new ThreadEreditato("Thread-Ereditato");
        thread1.start();
        
        // 2. Creazione di thread implementando l'interfaccia Runnable
        System.out.println("\n2. Creazione di thread implementando l'interfaccia Runnable:");
        RunnableImplementato runnableTask = new RunnableImplementato("Runnable-Implementato");
        Thread thread2 = new Thread(runnableTask);
        thread2.start();
        
        // 3. Creazione di thread usando espressioni lambda (Java 8+)
        System.out.println("\n3. Creazione di thread usando espressioni lambda:");
        Thread thread3 = new Thread(() -> {
            String threadName = "Thread-Lambda";
            System.out.println(threadName + ": Thread creato con espressione lambda");
            for (int i = 1; i <= 3; i++) {
                System.out.println(threadName + ": Esecuzione passo " + i);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    System.out.println(threadName + ": Thread interrotto");
                    return;
                }
            }
            System.out.println(threadName + ": Thread completato");
        });
        thread3.start();
        
        // Attendiamo il completamento di tutti i thread
        /* 
        try {
            thread1.join();  // Aspettiamo che il thread1 termini
            thread2.join();
            thread3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
            */
        
        System.out.println("\nTutti i thread hanno completato l'esecuzione");
        System.out.println("\nNota: L'approccio con Runnable è generalmente preferito perché:");
        System.out.println("- Separa il 'cosa fare' (Runnable) dal 'come farlo' (Thread)");
        System.out.println("- Permette alla classe di ereditare da altre classi");
        System.out.println("- Lo stesso Runnable può essere usato da più thread");
    }
}

// Approccio 1: Estendere la classe Thread
class ThreadEreditato extends Thread {
    private String threadName;
    
    public ThreadEreditato(String name) {
        this.threadName = name;
    }
    
    @Override
    public void run() {
        System.out.println(threadName + ": Thread creato estendendo la classe Thread");
        for (int i = 1; i <= 3; i++) {
            System.out.println(threadName + ": Esecuzione passo " + i);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                System.out.println(threadName + ": Thread interrotto");
                return;
            }
        }
        System.out.println(threadName + ": Thread completato");
    }
}

// Approccio 2: Implementare l'interfaccia Runnable
class RunnableImplementato implements Runnable {
    private String threadName;
    
    public RunnableImplementato(String name) {
        this.threadName = name;
    }
    
    @Override
    public void run() {
        System.out.println(threadName + ": Thread creato implementando Runnable");
        for (int i = 1; i <= 3; i++) {
            System.out.println(threadName + ": Esecuzione passo " + i);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                System.out.println(threadName + ": Thread interrotto");
                return;
            }
        }
        System.out.println(threadName + ": Thread completato");
    }
}
/**
    Dimostrazione dei metodi di creazione dei thread
    =============================================

    1. Creazione di thread estendendo la classe Thread:

    2. Creazione di thread implementando l'interfaccia Runnable:

    3. Creazione di thread usando espressioni lambda:
    Thread-Lambda: Thread creato con espressione lambda
    Thread-Ereditato: Thread creato estendendo la classe Thread
    Runnable-Implementato: Thread creato implementando Runnable
    Thread-Lambda: Esecuzione passo 1
    Thread-Ereditato: Esecuzione passo 1
    Runnable-Implementato: Esecuzione passo 1
    Thread-Lambda: Esecuzione passo 2
    Thread-Ereditato: Esecuzione passo 2
    Runnable-Implementato: Esecuzione passo 2
    Thread-Lambda: Esecuzione passo 3
    Thread-Ereditato: Esecuzione passo 3
    Runnable-Implementato: Esecuzione passo 3
    Thread-Lambda: Thread completato
    Thread-Ereditato: Thread completato
    Runnable-Implementato: Thread completato

    Tutti i thread hanno completato l'esecuzione

    Nota: L'approccio con Runnable è generalmente preferito perché:
    - Separa il 'cosa fare' (Runnable) dal 'come farlo' (Thread)
    - Permette alla classe di ereditare da altre classi
    - Lo stesso Runnable può essere usato da più thread
 */