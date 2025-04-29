/**
 * Esempio 01: Tecniche di Debugging e Ottimizzazione delle Prestazioni
 * 
 * Questo esempio dimostra alcune tecniche comuni per il debugging e l'ottimizzazione
 * delle prestazioni nei programmi concorrenti, inclusi:
 * - Rilevamento di deadlock
 * - Identificazione di race condition
 * - Monitoraggio delle prestazioni
 * - Tecniche di logging thread-safe
 */
package esempi;

import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.ConsoleHandler;
import java.util.logging.SimpleFormatter;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public class DebuggingPerformanceBase {
    // Logger thread-safe per il debugging
    private static final Logger LOGGER = Logger.getLogger(DebuggingPerformanceBase.class.getName());
    
    // Configurazione del logger
    static {
        LOGGER.setLevel(Level.ALL);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new SimpleFormatter());
        handler.setLevel(Level.ALL);
        LOGGER.addHandler(handler);
        LOGGER.setUseParentHandlers(false);
    }
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Dimostrazione di Tecniche di Debugging e Ottimizzazione delle Prestazioni");
        System.out.println("=======================================================================");
        
        // Configurazione del ThreadMXBean per il monitoraggio
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        
        // Esempio 1: Rilevamento di deadlock
        System.out.println("\n1. Rilevamento di Deadlock:");
        demoDeadlockDetection(threadMXBean);
        
        // Esempio 2: Identificazione di race condition
        System.out.println("\n2. Identificazione di Race Condition:");
        demoRaceCondition();
        
        // Esempio 3: Monitoraggio delle prestazioni
        System.out.println("\n3. Monitoraggio delle Prestazioni:");
        demoPerformanceMonitoring(threadMXBean);
        
        // Esempio 4: Logging thread-safe
        System.out.println("\n4. Logging Thread-Safe:");
        demoThreadSafeLogging();
        
        System.out.println("\nBest Practices per il Debugging e l'Ottimizzazione:");
        System.out.println("1. Utilizzare strumenti di monitoraggio come JVisualVM o JMC");
        System.out.println("2. Implementare logging thread-safe con informazioni sul thread");
        System.out.println("3. Utilizzare ThreadMXBean per rilevare deadlock e monitorare le prestazioni");
        System.out.println("4. Preferire strutture dati concorrenti e operazioni atomiche");
        System.out.println("5. Evitare blocchi di sincronizzazione troppo ampi");
    }
    
    /**
     * Dimostrazione di rilevamento di deadlock
     */
    private static void demoDeadlockDetection(ThreadMXBean threadMXBean) throws InterruptedException {
        // Crea due lock per simulare un potenziale deadlock
        final Lock lock1 = new ReentrantLock();
        final Lock lock2 = new ReentrantLock();
        
        // Thread che acquisisce lock1 e poi tenta di acquisire lock2
        Thread thread1 = new Thread(() -> {
            LOGGER.info("Thread1: Tentativo di acquisire lock1");
            lock1.lock();
            try {
                LOGGER.info("Thread1: Lock1 acquisito, attesa di 500ms");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                
                LOGGER.info("Thread1: Tentativo di acquisire lock2");
                // In un caso reale, questo potrebbe causare deadlock
                // ma per evitare di bloccare l'esempio, usiamo tryLock con timeout
                boolean acquired = lock2.tryLock(1, TimeUnit.SECONDS);
                if (acquired) {
                    try {
                        LOGGER.info("Thread1: Lock2 acquisito");
                    } finally {
                        lock2.unlock();
                        LOGGER.info("Thread1: Lock2 rilasciato");
                    }
                } else {
                    LOGGER.warning("Thread1: Impossibile acquisire lock2 - potenziale deadlock evitato");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock1.unlock();
                LOGGER.info("Thread1: Lock1 rilasciato");
            }
        }, "DeadlockDemo-Thread1");
        
        // Thread che acquisisce lock2 e poi tenta di acquisire lock1
        Thread thread2 = new Thread(() -> {
            LOGGER.info("Thread2: Tentativo di acquisire lock2");
            lock2.lock();
            try {
                LOGGER.info("Thread2: Lock2 acquisito, attesa di 500ms");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                
                LOGGER.info("Thread2: Tentativo di acquisire lock1");
                // In un caso reale, questo potrebbe causare deadlock
                // ma per evitare di bloccare l'esempio, usiamo tryLock con timeout
                boolean acquired = lock1.tryLock(1, TimeUnit.SECONDS);
                if (acquired) {
                    try {
                        LOGGER.info("Thread2: Lock1 acquisito");
                    } finally {
                        lock1.unlock();
                        LOGGER.info("Thread2: Lock1 rilasciato");
                    }
                } else {
                    LOGGER.warning("Thread2: Impossibile acquisire lock1 - potenziale deadlock evitato");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock2.unlock();
                LOGGER.info("Thread2: Lock2 rilasciato");
            }
        }, "DeadlockDemo-Thread2");
        
        // Avvia i thread
        thread1.start();
        thread2.start();
        
        // Attende che i thread completino
        thread1.join();
        thread2.join();
        
        // Verifica se ci sono deadlock
        long[] deadlockedThreads = threadMXBean.findDeadlockedThreads();
        if (deadlockedThreads != null && deadlockedThreads.length > 0) {
            System.out.println("ATTENZIONE: Rilevati thread in deadlock!");
        } else {
            System.out.println("Nessun deadlock rilevato (in questo esempio abbiamo evitato il deadlock con tryLock)");
        }
        
        System.out.println("Nota: In un'applicazione reale, è possibile implementare un monitor di deadlock");
        System.out.println("che esegue periodicamente threadMXBean.findDeadlockedThreads() per rilevare deadlock.");
    }
    
    /**
     * Dimostrazione di identificazione di race condition
     */
    private static void demoRaceCondition() throws InterruptedException {
        // Contatore non thread-safe per dimostrare race condition
        final Counter unsafeCounter = new Counter();
        // Contatore thread-safe per confronto
        final ThreadSafeCounter safeCounter = new ThreadSafeCounter();
        
        // Numero di thread e incrementi
        final int numThreads = 5;
        final int incrementsPerThread = 1000;
        
        // Esegue test con contatore non thread-safe
        System.out.println("Test con contatore NON thread-safe:");
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        
        for (int i = 0; i < numThreads; i++) {
            executor.execute(() -> {
                for (int j = 0; j < incrementsPerThread; j++) {
                    unsafeCounter.increment();
                }
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        
        // Esegue test con contatore thread-safe
        System.out.println("Test con contatore thread-safe:");
        executor = Executors.newFixedThreadPool(numThreads);
        
        for (int i = 0; i < numThreads; i++) {
            executor.execute(() -> {
                for (int j = 0; j < incrementsPerThread; j++) {
                    safeCounter.increment();
                }
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        
        // Verifica i risultati
        int expectedValue = numThreads * incrementsPerThread;
        System.out.println("Valore contatore NON thread-safe: " + unsafeCounter.getValue());
        System.out.println("Valore contatore thread-safe: " + safeCounter.getValue());
        System.out.println("Valore atteso: " + expectedValue);
        
        // Verifica se c'è stata una race condition
        if (unsafeCounter.getValue() != expectedValue) {
            System.out.println("Race condition rilevata nel contatore NON thread-safe!");
            System.out.println("Differenza: " + (expectedValue - unsafeCounter.getValue()));
        }
    }
    
    /**
     * Dimostrazione di monitoraggio delle prestazioni
     */
    private static void demoPerformanceMonitoring(ThreadMXBean threadMXBean) throws InterruptedException {
        // Abilita il conteggio del tempo CPU se supportato
        if (threadMXBean.isThreadCpuTimeSupported()) {
            threadMXBean.setThreadCpuTimeEnabled(true);
        }
        
        // Crea un'attività CPU-intensive
        Runnable cpuIntensiveTask = () -> {
            long sum = 0;
            for (int i = 0; i < 100_000_000; i++) {
                sum += i;
            }
            System.out.println("Task completato, somma: " + sum);
        };
        
        // Crea un'attività I/O-intensive (simulata)
        Runnable ioIntensiveTask = () -> {
            try {
                for (int i = 0; i < 10; i++) {
                    System.out.println("Simulazione operazione I/O...");
                    Thread.sleep(100); // Simula attesa I/O
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };
        
        // Esegue le attività e monitora le prestazioni
        Thread cpuThread = new Thread(cpuIntensiveTask, "CPUIntensiveThread");
        Thread ioThread = new Thread(ioIntensiveTask, "IOIntensiveThread");
        
        long startTime = System.nanoTime();
        
        cpuThread.start();
        ioThread.start();
        
        cpuThread.join();
        ioThread.join();
        
        long endTime = System.nanoTime();
        
        // Stampa statistiche di esecuzione
        if (threadMXBean.isThreadCpuTimeSupported()) {
            long cpuThreadId = cpuThread.getId();
            long ioThreadId = ioThread.getId();
            
            long cpuThreadCpuTime = threadMXBean.getThreadCpuTime(cpuThreadId);
            long ioThreadCpuTime = threadMXBean.getThreadCpuTime(ioThreadId);
            
            System.out.println("Tempo CPU per thread CPU-intensive: " + 
                             (cpuThreadCpuTime / 1_000_000) + " ms");
            System.out.println("Tempo CPU per thread I/O-intensive: " + 
                             (ioThreadCpuTime / 1_000_000) + " ms");
        }
        
        System.out.println("Tempo totale di esecuzione: " + 
                         ((endTime - startTime) / 1_000_000) + " ms");
    }
    
    /**
     * Dimostrazione di logging thread-safe
     */
    private static void demoThreadSafeLogging() throws InterruptedException {
        // Crea un pool di thread
        ExecutorService executor = Executors.newFixedThreadPool(3);
        
        // Esegue alcune attività con logging
        for (int i = 0; i < 5; i++) {
            final int taskId = i;
            executor.execute(() -> {
                // Log a diversi livelli
                LOGGER.info("Task " + taskId + " avviato su thread " + Thread.currentThread().getName());
                
                try {
                    // Simula un'operazione
                    Thread.sleep((long) (Math.random() * 500));
                    
                    // Simula un warning occasionale
                    if (taskId % 2 == 0) {
                        LOGGER.warning("Task " + taskId + ": condizione di warning rilevata");
                    }
                    
                    // Simula un errore occasionale
                    if (taskId == 3) {
                        try {
                            throw new RuntimeException("Errore simulato nel task " + taskId);
                        } catch (Exception e) {
                            LOGGER.log(Level.SEVERE, "Errore durante l'esecuzione del task " + taskId, e);
                        }
                    }
                    
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    LOGGER.warning("Task " + taskId + " interrotto");
                }
                
                LOGGER.info("Task " + taskId + " completato");
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        
        System.out.println("Logging thread-safe completato. Nota come i messaggi di log includono");
        System.out.println("informazioni sul thread e timestamp, e non sono mescolati tra loro.");
    }
    
    /**
     * Classe Counter non thread-safe per dimostrare race condition
     */
    static class Counter {
        private int value = 0;
        
        public void increment() {
            value++; // Operazione non atomica (leggi-modifica-scrivi)
        }
        
        public int getValue() {
            return value;
        }
    }
    
    /**
     * Classe Counter thread-safe per confronto
     */
    static class ThreadSafeCounter {
        private int value = 0;
        
        public synchronized void increment() {
            value++;
        }
        
        public synchronized int getValue() {
            return value;
        }
    }
}