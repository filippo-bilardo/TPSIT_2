package esempi;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Questo esempio dimostra come si verifica un deadlock e come rilevarlo.
 * Due thread tentano di acquisire due lock in ordine diverso, creando le condizioni per un deadlock.
 * L'esempio include anche una versione corretta che evita il deadlock utilizzando un ordine coerente
 * di acquisizione dei lock.
 */
public class DeadlockExample {

    // Due lock che verranno utilizzati per dimostrare il deadlock
    private static final Lock lockA = new ReentrantLock();
    private static final Lock lockB = new ReentrantLock();

    public static void main(String[] args) {
        // Decommentare una delle due righe seguenti per eseguire la versione con deadlock o quella corretta
        demonstrateDeadlock();
        // demonstrateDeadlockPrevention();
    }

    /**
     * Dimostra come si verifica un deadlock.
     * Thread 1: acquisisce lockA, poi tenta di acquisire lockB
     * Thread 2: acquisisce lockB, poi tenta di acquisire lockA
     */
    private static void demonstrateDeadlock() {
        System.out.println("Dimostrazione di deadlock - ATTENZIONE: questo programma si bloccherà!\n");

        Thread thread1 = new Thread(() -> {
            System.out.println("Thread 1: Tentativo di acquisire lockA");
            lockA.lock();
            try {
                System.out.println("Thread 1: lockA acquisito");
                
                // Simulazione di lavoro
                sleep(1000);
                
                System.out.println("Thread 1: Tentativo di acquisire lockB");
                lockB.lock();
                try {
                    System.out.println("Thread 1: lockB acquisito");
                    // Operazione critica con entrambi i lock
                } finally {
                    lockB.unlock();
                    System.out.println("Thread 1: lockB rilasciato");
                }
            } finally {
                lockA.unlock();
                System.out.println("Thread 1: lockA rilasciato");
            }
        }, "Thread-1");

        Thread thread2 = new Thread(() -> {
            System.out.println("Thread 2: Tentativo di acquisire lockB");
            lockB.lock();
            try {
                System.out.println("Thread 2: lockB acquisito");
                
                // Simulazione di lavoro
                sleep(1000);
                
                System.out.println("Thread 2: Tentativo di acquisire lockA");
                lockA.lock();
                try {
                    System.out.println("Thread 2: lockA acquisito");
                    // Operazione critica con entrambi i lock
                } finally {
                    lockA.unlock();
                    System.out.println("Thread 2: lockA rilasciato");
                }
            } finally {
                lockB.unlock();
                System.out.println("Thread 2: lockB rilasciato");
            }
        }, "Thread-2");

        thread1.start();
        thread2.start();

        // Attendi che i thread terminino (non accadrà a causa del deadlock)
        try {
            thread1.join();
            thread2.join();
            System.out.println("Entrambi i thread sono terminati (questo messaggio non verrà mai visualizzato)");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Dimostra come prevenire un deadlock utilizzando un ordine coerente di acquisizione dei lock.
     * Entrambi i thread acquisiscono prima lockA e poi lockB.
     */
    private static void demonstrateDeadlockPrevention() {
        System.out.println("Dimostrazione di prevenzione del deadlock\n");

        Thread thread1 = new Thread(() -> {
            System.out.println("Thread 1: Tentativo di acquisire lockA");
            lockA.lock();
            try {
                System.out.println("Thread 1: lockA acquisito");
                
                // Simulazione di lavoro
                sleep(1000);
                
                System.out.println("Thread 1: Tentativo di acquisire lockB");
                lockB.lock();
                try {
                    System.out.println("Thread 1: lockB acquisito");
                    // Operazione critica con entrambi i lock
                } finally {
                    lockB.unlock();
                    System.out.println("Thread 1: lockB rilasciato");
                }
            } finally {
                lockA.unlock();
                System.out.println("Thread 1: lockA rilasciato");
            }
        }, "Thread-1");

        Thread thread2 = new Thread(() -> {
            System.out.println("Thread 2: Tentativo di acquisire lockA"); // Nota: anche Thread 2 acquisisce prima lockA
            lockA.lock();
            try {
                System.out.println("Thread 2: lockA acquisito");
                
                // Simulazione di lavoro
                sleep(1000);
                
                System.out.println("Thread 2: Tentativo di acquisire lockB");
                lockB.lock();
                try {
                    System.out.println("Thread 2: lockB acquisito");
                    // Operazione critica con entrambi i lock
                } finally {
                    lockB.unlock();
                    System.out.println("Thread 2: lockB rilasciato");
                }
            } finally {
                lockA.unlock();
                System.out.println("Thread 2: lockA rilasciato");
            }
        }, "Thread-2");

        thread1.start();
        thread2.start();

        // Attendi che i thread terminino
        try {
            thread1.join();
            thread2.join();
            System.out.println("Entrambi i thread sono terminati con successo");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Metodo di utilità per mettere in pausa un thread.
     */
    private static void sleep(long milliseconds) {
        try {
            TimeUnit.MILLISECONDS.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Metodo di utilità per generare un thread dump.
     * Può essere utilizzato per analizzare lo stato dei thread durante un deadlock.
     */
    private static void generateThreadDump() {
        System.out.println("\n=== THREAD DUMP ===\n");
        for (Thread t : Thread.getAllStackTraces().keySet()) {
            System.out.println("Thread: " + t.getName() + 
                               " (ID: " + t.getId() + ", " + 
                               "State: " + t.getState() + ")");
        }
        System.out.println("\n=== END THREAD DUMP ===\n");
    }
}