package esempi;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Questo esempio dimostra come generare e interpretare thread dump per il debugging
 * di applicazioni multi-thread. Vengono create diverse situazioni comuni (thread in attesa,
 * deadlock, thread in esecuzione) e viene mostrato come analizzare lo stato dei thread.
 */
public class ThreadDumpAnalysis {

    private static final Lock lock1 = new ReentrantLock();
    private static final Lock lock2 = new ReentrantLock();
    private static final CountDownLatch startSignal = new CountDownLatch(1);
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Dimostrazione di analisi dei thread dump\n");
        
        // Crea un pool di thread per eseguire diverse attività
        ExecutorService executor = Executors.newFixedThreadPool(4);
        
        // Thread che esegue un'operazione CPU-intensive
        executor.submit(() -> {
            Thread.currentThread().setName("CPU-Intensive-Thread");
            try {
                startSignal.await(); // Attendi il segnale di inizio
                System.out.println("Thread CPU-intensive avviato");
                
                // Simulazione di operazione CPU-intensive
                long sum = 0;
                for (long i = 0; i < 10_000_000_000L; i++) {
                    sum += i;
                    if (i % 1_000_000_000 == 0) {
                        System.out.println("Progresso calcolo: " + (i / 1_000_000_000) + "/10");
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        // Thread che attende su un oggetto (WAITING state)
        executor.submit(() -> {
            Thread.currentThread().setName("Waiting-Thread");
            try {
                startSignal.await(); // Attendi il segnale di inizio
                System.out.println("Thread in attesa avviato");
                
                // Metti il thread in stato WAITING
                synchronized (ThreadDumpAnalysis.class) {
                    ThreadDumpAnalysis.class.wait();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        // Due thread che creano un deadlock
        executor.submit(() -> {
            Thread.currentThread().setName("Deadlock-Thread-1");
            try {
                startSignal.await(); // Attendi il segnale di inizio
                System.out.println("Thread deadlock 1 avviato");
                
                // Crea un deadlock
                lock1.lock();
                try {
                    System.out.println("Deadlock-Thread-1 ha acquisito lock1");
                    TimeUnit.MILLISECONDS.sleep(500); // Pausa per garantire il deadlock
                    
                    System.out.println("Deadlock-Thread-1 tenta di acquisire lock2");
                    lock2.lock();
                    try {
                        System.out.println("Deadlock-Thread-1 ha acquisito entrambi i lock");
                    } finally {
                        lock2.unlock();
                    }
                } finally {
                    lock1.unlock();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        executor.submit(() -> {
            Thread.currentThread().setName("Deadlock-Thread-2");
            try {
                startSignal.await(); // Attendi il segnale di inizio
                System.out.println("Thread deadlock 2 avviato");
                
                // Crea un deadlock (acquisizione lock in ordine inverso)
                lock2.lock();
                try {
                    System.out.println("Deadlock-Thread-2 ha acquisito lock2");
                    TimeUnit.MILLISECONDS.sleep(500); // Pausa per garantire il deadlock
                    
                    System.out.println("Deadlock-Thread-2 tenta di acquisire lock1");
                    lock1.lock();
                    try {
                        System.out.println("Deadlock-Thread-2 ha acquisito entrambi i lock");
                    } finally {
                        lock1.unlock();
                    }
                } finally {
                    lock2.unlock();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        // Avvia tutti i thread
        System.out.println("Avvio di tutti i thread...");
        startSignal.countDown();
        
        // Attendi un po' per permettere ai thread di raggiungere i loro stati
        TimeUnit.SECONDS.sleep(2);
        
        // Genera e analizza il thread dump
        System.out.println("\n=== GENERAZIONE THREAD DUMP ===\n");
        generateThreadDump();
        
        // Verifica se ci sono deadlock
        System.out.println("\n=== RILEVAMENTO DEADLOCK ===\n");
        detectDeadlock();
        
        // Termina l'esecuzione
        System.out.println("\nTerminazione dell'esecuzione...");
        executor.shutdownNow();
        executor.awaitTermination(2, TimeUnit.SECONDS);
    }
    
    /**
     * Genera un thread dump completo di tutti i thread attivi.
     */
    private static void generateThreadDump() {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(true, true);
        
        for (ThreadInfo info : threadInfos) {
            printThreadInfo(info);
        }
    }
    
    /**
     * Rileva eventuali deadlock tra thread.
     */
    private static void detectDeadlock() {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        long[] deadlockedThreadIds = threadMXBean.findDeadlockedThreads();
        
        if (deadlockedThreadIds == null || deadlockedThreadIds.length == 0) {
            System.out.println("Nessun deadlock rilevato.");
            return;
        }
        
        System.out.println("Rilevati " + deadlockedThreadIds.length + " thread in deadlock!");
        ThreadInfo[] deadlockedThreads = threadMXBean.getThreadInfo(deadlockedThreadIds, true, true);
        
        System.out.println("\nDettagli dei thread in deadlock:\n");
        for (ThreadInfo threadInfo : deadlockedThreads) {
            printThreadInfo(threadInfo);
        }
    }
    
    /**
     * Stampa informazioni dettagliate su un thread.
     */
    private static void printThreadInfo(ThreadInfo threadInfo) {
        if (threadInfo == null) {
            return;
        }
        
        StringBuilder sb = new StringBuilder("Thread ID: " + threadInfo.getThreadId() + 
                                          " - Nome: " + threadInfo.getThreadName() + 
                                          " - Stato: " + threadInfo.getThreadState());
        
        if (threadInfo.getLockName() != null) {
            sb.append("\n    In attesa del lock: " + threadInfo.getLockName());
        }
        
        if (threadInfo.getLockOwnerName() != null) {
            sb.append("\n    Lock detenuto da: " + threadInfo.getLockOwnerName() + 
                      " (ID: " + threadInfo.getLockOwnerId() + ")");
        }
        
        // Stampa lo stack trace
        sb.append("\n    Stack trace:");
        for (StackTraceElement element : threadInfo.getStackTrace()) {
            sb.append("\n        at " + element);
        }
        
        System.out.println(sb.toString());
        System.out.println();
    }
    
    /**
     * Fornisce una guida all'interpretazione dei thread dump.
     */
    private static void explainThreadDump() {
        System.out.println("\n=== GUIDA ALL'INTERPRETAZIONE DEI THREAD DUMP ===\n");
        
        System.out.println("Stati dei thread e loro significato:\n");
        System.out.println("1. RUNNABLE: Il thread è in esecuzione o pronto per l'esecuzione.");
        System.out.println("2. BLOCKED: Il thread è bloccato in attesa di un monitor lock.");
        System.out.println("3. WAITING: Il thread è in attesa indefinita che un altro thread esegua un'azione.");
        System.out.println("4. TIMED_WAITING: Il thread è in attesa per un tempo specificato.");
        System.out.println("5. NEW: Il thread è stato creato ma non è ancora stato avviato.");
        System.out.println("6. TERMINATED: Il thread ha completato la sua esecuzione.\n");
        
        System.out.println("Identificazione di problemi comuni:\n");
        System.out.println("1. Deadlock: Cercare thread in stato BLOCKED che attendono lock posseduti da altri thread.");
        System.out.println("2. Thread bloccati: Molti thread in stato BLOCKED possono indicare contesa per le risorse.");
        System.out.println("3. Thread in attesa: Thread in stato WAITING o TIMED_WAITING possono indicare problemi di sincronizzazione.");
        System.out.println("4. CPU elevata: Molti thread in stato RUNNABLE possono indicare un carico CPU elevato.\n");
        
        System.out.println("Strumenti per generare thread dump:\n");
        System.out.println("1. jstack: Utility da riga di comando inclusa nel JDK.");
        System.out.println("   Esempio: jstack <pid> > threaddump.txt");
        System.out.println("2. JVisualVM: Strumento grafico per il monitoraggio e il profiling.");
        System.out.println("3. Java Mission Control: Strumento avanzato per il monitoraggio e l'analisi.");
        System.out.println("4. Programmaticamente: Utilizzando ThreadMXBean come in questo esempio.\n");
    }
}