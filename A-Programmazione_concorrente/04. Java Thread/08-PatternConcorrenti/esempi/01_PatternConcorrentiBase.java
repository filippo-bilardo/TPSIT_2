/**
 * Esempio 01: Implementazione Base dei Pattern Concorrenti
 * 
 * Questo esempio dimostra l'implementazione di alcuni pattern concorrenti comuni:
 * - Producer-Consumer (Produttore-Consumatore)
 * - Thread-Per-Message
 * - Worker Thread
 */
package esempi;

import java.util.concurrent.*;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

public class PatternConcorrentiBase {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Dimostrazione dei Pattern Concorrenti");
        System.out.println("===================================");
        
        // Esempio 1: Producer-Consumer Pattern
        System.out.println("\n1. Pattern Produttore-Consumatore:");
        demoProducerConsumer();
        
        // Esempio 2: Thread-Per-Message Pattern
        System.out.println("\n2. Pattern Thread-Per-Message:");
        demoThreadPerMessage();
        
        // Esempio 3: Worker Thread Pattern
        System.out.println("\n3. Pattern Worker Thread:");
        demoWorkerThread();
        
        System.out.println("\nVantaggi dell'utilizzo dei Pattern Concorrenti:");
        System.out.println("1. Soluzioni standardizzate a problemi comuni di concorrenza");
        System.out.println("2. Codice più leggibile e manutenibile");
        System.out.println("3. Migliore gestione delle risorse");
        System.out.println("4. Separazione delle responsabilità");
    }
    
    /**
     * Dimostrazione del pattern Produttore-Consumatore
     * Questo pattern è utile quando i dati devono essere elaborati in modo asincrono
     * e quando la velocità di produzione e consumo può variare.
     */
    private static void demoProducerConsumer() throws InterruptedException {
        // Buffer condiviso con capacità limitata
        BlockingQueue<Integer> buffer = new ArrayBlockingQueue<>(10);
        
        // Crea e avvia il thread produttore
        Thread producer = new Thread(() -> {
            try {
                Random random = new Random();
                for (int i = 0; i < 20; i++) {
                    int item = random.nextInt(100);
                    System.out.println("Produttore: prodotto item " + item);
                    buffer.put(item); // Si blocca se il buffer è pieno
                    Thread.sleep(100); // Simula tempo di produzione variabile
                }
                System.out.println("Produttore: ha terminato la produzione");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        // Crea e avvia il thread consumatore
        Thread consumer = new Thread(() -> {
            try {
                while (true) {
                    Integer item = buffer.poll(5, TimeUnit.SECONDS);
                    if (item == null) {
                        // Timeout - probabilmente il produttore ha terminato
                        System.out.println("Consumatore: nessun item disponibile, terminazione");
                        break;
                    }
                    System.out.println("Consumatore: elaborato item " + item);
                    Thread.sleep(200); // Consumatore più lento del produttore
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        // Avvia i thread
        producer.start();
        consumer.start();
        
        // Attende il completamento
        producer.join();
        consumer.join();
        
        System.out.println("Pattern Produttore-Consumatore completato");
    }
    
    /**
     * Dimostrazione del pattern Thread-Per-Message
     * Questo pattern è utile per gestire richieste asincrone dove ogni richiesta
     * viene elaborata in un thread separato.
     */
    private static void demoThreadPerMessage() throws InterruptedException {
        // Servizio che implementa il pattern Thread-Per-Message
        MessageService messageService = new MessageService();
        
        // Invia alcuni messaggi
        for (int i = 1; i <= 5; i++) {
            final String message = "Messaggio #" + i;
            System.out.println("Client: invio " + message);
            messageService.sendAsync(message);
            Thread.sleep(100); // Breve pausa tra i messaggi
        }
        
        // Attende che tutti i messaggi siano elaborati
        Thread.sleep(2000);
        
        // Chiude il servizio
        messageService.shutdown();
        System.out.println("Pattern Thread-Per-Message completato");
    }
    
    /**
     * Dimostrazione del pattern Worker Thread
     * Questo pattern utilizza un pool di thread worker per elaborare le richieste
     * da una coda di lavoro condivisa.
     */
    private static void demoWorkerThread() throws InterruptedException {
        // Crea un pool di worker con una coda di lavoro
        WorkerPool workerPool = new WorkerPool(3); // 3 worker threads
        
        // Sottomette alcune attività al pool
        for (int i = 1; i <= 10; i++) {
            final int taskId = i;
            workerPool.submitTask(() -> {
                System.out.println("Worker: inizio elaborazione task " + taskId + 
                                 " su thread " + Thread.currentThread().getName());
                try {
                    // Simula un'elaborazione che richiede tempo
                    Thread.sleep((long) (Math.random() * 500 + 500));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("Worker: completato task " + taskId);
            });
            
            // Breve pausa tra le sottomissioni
            Thread.sleep(100);
        }
        
        // Attende il completamento di tutte le attività
        Thread.sleep(3000);
        
        // Chiude il pool
        workerPool.shutdown();
        System.out.println("Pattern Worker Thread completato");
    }
    
    /**
     * Implementazione del servizio per il pattern Thread-Per-Message
     */
    static class MessageService {
        private final ExecutorService executor;
        
        public MessageService() {
            // Utilizza un cached thread pool per creare nuovi thread secondo necessità
            this.executor = Executors.newCachedThreadPool(r -> {
                Thread t = new Thread(r);
                t.setDaemon(true); // Thread daemon per terminare quando l'applicazione termina
                return t;
            });
        }
        
        /**
         * Invia un messaggio in modo asincrono, creando un nuovo thread per l'elaborazione
         */
        public void sendAsync(String message) {
            executor.execute(() -> {
                try {
                    // Simula l'elaborazione del messaggio
                    System.out.println("MessageService: elaborazione di '" + message + 
                                     "' su thread " + Thread.currentThread().getName());
                    Thread.sleep((long) (Math.random() * 1000));
                    System.out.println("MessageService: completata elaborazione di '" + message + "'");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        
        /**
         * Chiude il servizio
         */
        public void shutdown() {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(2, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
    
    /**
     * Implementazione del pool di worker per il pattern Worker Thread
     */
    static class WorkerPool {
        private final BlockingQueue<Runnable> taskQueue;
        private final List<Thread> workers;
        private volatile boolean isRunning;
        
        public WorkerPool(int numWorkers) {
            this.taskQueue = new LinkedBlockingQueue<>();
            this.workers = new ArrayList<>(numWorkers);
            this.isRunning = true;
            
            // Crea e avvia i thread worker
            for (int i = 0; i < numWorkers; i++) {
                Thread worker = new Thread(() -> {
                    try {
                        while (isRunning || !taskQueue.isEmpty()) {
                            Runnable task = taskQueue.poll(500, TimeUnit.MILLISECONDS);
                            if (task != null) {
                                task.run();
                            }
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
                worker.setName("Worker-" + (i + 1));
                worker.start();
                workers.add(worker);
            }
        }
        
        /**
         * Sottomette un'attività al pool di worker
         */
        public void submitTask(Runnable task) {
            if (isRunning) {
                taskQueue.offer(task);
            } else {
                throw new RejectedExecutionException("WorkerPool è stato chiuso");
            }
        }
        
        /**
         * Chiude il pool di worker
         */
        public void shutdown() {
            isRunning = false;
            
            // Attende che tutti i worker terminino
            for (Thread worker : workers) {
                try {
                    worker.join(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            
            // Verifica se ci sono attività non completate
            if (!taskQueue.isEmpty()) {
                System.out.println("WorkerPool: " + taskQueue.size() + " attività non completate");
            }
        }
    }
}