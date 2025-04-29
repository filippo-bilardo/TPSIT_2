package esempi;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Esempio di implementazione del pattern Thread-per-Message.
 * Questo pattern delega l'esecuzione di un'attività a un thread dedicato,
 * permettendo al thread chiamante di continuare la sua esecuzione senza attendere.
 */
public class ThreadPerMessageExample {

    // Interfaccia per rappresentare un'attività da eseguire
    public interface Task {
        void execute();
    }

    // Implementazione base che crea un nuovo thread per ogni messaggio
    public static class BasicHost {
        public void executeTask(final Task task) {
            System.out.println("BasicHost: Delegando task a un nuovo thread");
            new Thread(() -> {
                System.out.println("BasicHost: Esecuzione del task in " + Thread.currentThread().getName());
                task.execute();
            }).start();
            System.out.println("BasicHost: Continuazione immediata nel thread " + Thread.currentThread().getName());
        }
    }

    // Implementazione avanzata che utilizza un ExecutorService
    public static class ExecutorHost {
        private final ExecutorService executor;

        public ExecutorHost(int nThreads) {
            this.executor = Executors.newFixedThreadPool(nThreads);
        }

        public void executeTask(final Task task) {
            System.out.println("ExecutorHost: Delegando task all'executor");
            executor.execute(() -> {
                System.out.println("ExecutorHost: Esecuzione del task in " + Thread.currentThread().getName());
                task.execute();
            });
            System.out.println("ExecutorHost: Continuazione immediata nel thread " + Thread.currentThread().getName());
        }

        public void shutdown() {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    // Esempio di task che simula un'operazione che richiede tempo
    public static class SampleTask implements Task {
        private final String name;
        private final long duration;

        public SampleTask(String name, long duration) {
            this.name = name;
            this.duration = duration;
        }

        @Override
        public void execute() {
            try {
                System.out.println("Task '" + name + "': Inizio elaborazione");
                // Simulazione di un'operazione che richiede tempo
                Thread.sleep(duration);
                System.out.println("Task '" + name + "': Elaborazione completata");
            } catch (InterruptedException e) {
                System.out.println("Task '" + name + "': Interrotto");
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Esempio del pattern Thread-per-Message ===\n");

        // Esempio con BasicHost (un thread per ogni task)
        System.out.println("--- Utilizzo di BasicHost ---");
        BasicHost basicHost = new BasicHost();
        basicHost.executeTask(new SampleTask("Task-A", 2000));
        basicHost.executeTask(new SampleTask("Task-B", 1000));

        // Breve pausa per osservare l'output
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Esempio con ExecutorHost (pool di thread)
        System.out.println("\n--- Utilizzo di ExecutorHost ---");
        ExecutorHost executorHost = new ExecutorHost(2);
        executorHost.executeTask(new SampleTask("Task-C", 2000));
        executorHost.executeTask(new SampleTask("Task-D", 1000));
        executorHost.executeTask(new SampleTask("Task-E", 1500));

        // Attesa per permettere il completamento dei task
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Chiusura dell'executor
        executorHost.shutdown();
        System.out.println("\n=== Fine dell'esempio ===");
    }
}