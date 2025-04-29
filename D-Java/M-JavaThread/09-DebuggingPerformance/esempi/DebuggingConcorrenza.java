/**
 * Esempio 01: Debugging e Analisi delle Prestazioni in Applicazioni Concorrenti
 * 
 * Questo esempio dimostra tecniche per:
 * - Identificare e risolvere race condition
 * - Rilevare e prevenire deadlock
 * - Analizzare le prestazioni di codice concorrente
 * - Utilizzare strumenti di debugging per thread
 */
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DebuggingConcorrenza {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Tecniche di Debugging e Analisi delle Prestazioni in Applicazioni Concorrenti");
        System.out.println("==========================================================================");
        
        // Esempio 1: Rilevamento di race condition
        System.out.println("\n1. Rilevamento di Race Condition:");
        testRaceCondition();
        
        // Esempio 2: Rilevamento di deadlock
        System.out.println("\n2. Rilevamento di Deadlock:");
        testDeadlockDetection();
        
        // Esempio 3: Analisi delle prestazioni
        System.out.println("\n3. Analisi delle Prestazioni:");
        testPerformanceAnalysis();
        
        // Esempio 4: Strumenti di debugging per thread
        System.out.println("\n4. Strumenti di Debugging per Thread:");
        testThreadDebuggingTools();
        
        System.out.println("\nBest Practices per il Debugging di Applicazioni Concorrenti:");
        System.out.println("1. Utilizzare logging dettagliato con identificatori di thread");
        System.out.println("2. Implementare timeout per identificare blocchi");
        System.out.println("3. Utilizzare strumenti di analisi come VisualVM, JConsole o Java Mission Control");
        System.out.println("4. Preferire strutture dati concorrenti e variabili atomiche");
        System.out.println("5. Implementare test di stress per rivelare problemi di concorrenza");
    }
    
    /**
     * Test per identificare e risolvere race condition
     */
    private static void testRaceCondition() throws InterruptedException {
        System.out.println("Dimostrazione di race condition e soluzione:");
        
        // Contatore non thread-safe
        class UnsafeCounter {
            private int count = 0;
            
            public void increment() {
                count++; // Operazione non atomica (read-modify-write)
            }
            
            public int getCount() {
                return count;
            }
        }
        
        // Contatore thread-safe con AtomicInteger
        class SafeCounter {
            private final AtomicInteger count = new AtomicInteger(0);
            
            public void increment() {
                count.incrementAndGet(); // Operazione atomica
            }
            
            public int getCount() {
                return count.get();
            }
        }
        
        // Test con contatore non sicuro
        final UnsafeCounter unsafeCounter = new UnsafeCounter();
        ExecutorService executor = Executors.newFixedThreadPool(10);
        
        for (int i = 0; i < 10; i++) {
            executor.execute(() -> {
                for (int j = 0; j < 1000; j++) {
                    unsafeCounter.increment();
                }
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        
        System.out.println("- Contatore non thread-safe: " + unsafeCounter.getCount() + 
                " (atteso: 10000)");
        
        // Test con contatore sicuro
        final SafeCounter safeCounter = new SafeCounter();
        executor = Executors.newFixedThreadPool(10);
        
        for (int i = 0; i < 10; i++) {
            executor.execute(() -> {
                for (int j = 0; j < 1000; j++) {
                    safeCounter.increment();
                }
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        
        System.out.println("- Contatore thread-safe: " + safeCounter.getCount() + 
                " (atteso: 10000)");
        
        System.out.println("\nTecniche per identificare race condition:");
        System.out.println("- Utilizzare strumenti come Java PathFinder o Thread Sanitizer");
        System.out.println("- Implementare test di stress con molti thread");
        System.out.println("- Aggiungere logging dettagliato per tracciare modifiche ai dati condivisi");
        System.out.println("- Utilizzare asserzioni per verificare invarianti");
    }
    
    /**
     * Test per rilevare e prevenire deadlock
     */
    private static void testDeadlockDetection() throws InterruptedException {
        System.out.println("Dimostrazione di rilevamento deadlock:");
        
        // Crea due lock per simulare un potenziale deadlock
        final Lock lock1 = new ReentrantLock();
        final Lock lock2 = new ReentrantLock();
        
        // Thread che acquisisce i lock in ordine: lock1 -> lock2
        Thread thread1 = new Thread(() -> {
            try {
                System.out.println("Thread 1: Tentativo di acquisire lock1");
                lock1.lock();
                System.out.println("Thread 1: Lock1 acquisito");
                
                // Simula lavoro
                Thread.sleep(100);
                
                System.out.println("Thread 1: Tentativo di acquisire lock2");
                lock2.lock();
                System.out.println("Thread 1: Lock2 acquisito");
                
                // Rilascia i lock
                lock2.unlock();
                lock1.unlock();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                // Assicura il rilascio dei lock in caso di eccezioni
                if (lock2.tryLock()) lock2.unlock();
                if (lock1.tryLock()) lock1.unlock();
            }
        }, "DeadlockThread-1");
        
        // Thread che acquisisce i lock in ordine inverso: lock2 -> lock1
        // Questo può causare deadlock
        Thread thread2 = new Thread(() -> {
            try {
                System.out.println("Thread 2: Tentativo di acquisire lock2");
                lock2.lock();
                System.out.println("Thread 2: Lock2 acquisito");
                
                // Simula lavoro
                Thread.sleep(100);
                
                System.out.println("Thread 2: Tentativo di acquisire lock1");
                lock1.lock();
                System.out.println("Thread 2: Lock1 acquisito");
                
                // Rilascia i lock
                lock1.unlock();
                lock2.unlock();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                // Assicura il rilascio dei lock in caso di eccezioni
                if (lock1.tryLock()) lock1.unlock();
                if (lock2.tryLock()) lock2.unlock();
            }
        }, "DeadlockThread-2");
        
        // Avvia i thread
        thread1.start();
        thread2.start();
        
        // Attende un po' per permettere ai thread di entrare in deadlock
        Thread.sleep(500);
        
        // Rileva deadlock usando ThreadMXBean
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        long[] deadlockedThreadIds = threadMXBean.findDeadlockedThreads();
        
        if (deadlockedThreadIds != null) {
            System.out.println("\nDEADLOCK RILEVATO! Thread coinvolti:");
            ThreadInfo[] threadInfos = threadMXBean.getThreadInfo(deadlockedThreadIds, true, true);
            for (ThreadInfo info : threadInfos) {
                System.out.println("- " + info.getThreadName() + " (ID: " + info.getThreadId() + ")");
                System.out.println("  Stato: " + info.getThreadState());
                System.out.println("  In attesa di: " + info.getLockName() + 
                        " posseduto da: " + info.getLockOwnerName() + 
                        " (ID: " + info.getLockOwnerId() + ")");
            }
            
            // Interrompe i thread in deadlock
            System.out.println("\nInterruzione dei thread in deadlock...");
            thread1.interrupt();
            thread2.interrupt();
        } else {
            System.out.println("Nessun deadlock rilevato.");
        }
        
        // Attende il completamento dei thread
        thread1.join(1000);
        thread2.join(1000);
        
        System.out.println("\nTecniche per prevenire deadlock:");
        System.out.println("- Acquisire i lock sempre nello stesso ordine");
        System.out.println("- Utilizzare tryLock() con timeout invece di lock()");
        System.out.println("- Evitare di tenere lock durante operazioni bloccanti");
        System.out.println("- Preferire strutture dati concorrenti quando possibile");
    }
    
    /**
     * Test per analizzare le prestazioni di codice concorrente
     */
    private static void testPerformanceAnalysis() throws InterruptedException {
        System.out.println("Analisi delle prestazioni di diverse implementazioni concorrenti:");
        
        // Test con diversi numeri di thread
        for (int numThreads : new int[] {1, 2, 4, 8, 16}) {
            System.out.println("\n- Test con " + numThreads + " thread:");
            
            // Implementazione con synchronized
            long startTime = System.currentTimeMillis();
            testSynchronizedPerformance(numThreads);
            long synchronizedTime = System.currentTimeMillis() - startTime;
            
            // Implementazione con ReentrantLock
            startTime = System.currentTimeMillis();
            testReentrantLockPerformance(numThreads);
            long reentrantLockTime = System.currentTimeMillis() - startTime;
            
            // Implementazione con AtomicInteger
            startTime = System.currentTimeMillis();
            testAtomicPerformance(numThreads);
            long atomicTime = System.currentTimeMillis() - startTime;
            
            // Stampa risultati
            System.out.println("  Synchronized: " + synchronizedTime + " ms");
            System.out.println("  ReentrantLock: " + reentrantLockTime + " ms");
            System.out.println("  AtomicInteger: " + atomicTime + " ms");
        }
        
        System.out.println("\nStrumenti per l'analisi delle prestazioni:");
        System.out.println("- JVisualVM per monitoraggio CPU, memoria e thread");
        System.out.println("- Java Flight Recorder per profiling dettagliato");
        System.out.println("- JMH (Java Microbenchmark Harness) per benchmark precisi");
        System.out.println("- Yourkit o JProfiler per analisi avanzate");
    }
    
    /**
     * Test di prestazioni con synchronized
     */
    private static void testSynchronizedPerformance(int numThreads) throws InterruptedException {
        final int iterations = 1000000 / numThreads;
        final Counter counter = new Counter();
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        
        for (int i = 0; i < numThreads; i++) {
            executor.execute(() -> {
                for (int j = 0; j < iterations; j++) {
                    counter.incrementSynchronized();
                }
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
    }
    
    /**
     * Test di prestazioni con ReentrantLock
     */
    private static void testReentrantLockPerformance(int numThreads) throws InterruptedException {
        final int iterations = 1000000 / numThreads;
        final Counter counter = new Counter();
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        
        for (int i = 0; i < numThreads; i++) {
            executor.execute(() -> {
                for (int j = 0; j < iterations; j++) {
                    counter.incrementWithLock();
                }
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
    }
    
    /**
     * Test di prestazioni con AtomicInteger
     */
    private static void testAtomicPerformance(int numThreads) throws InterruptedException {
        final int iterations = 1000000 / numThreads;
        final Counter counter = new Counter();
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        
        for (int i = 0; i < numThreads; i++) {
            executor.execute(() -> {
                for (int j = 0; j < iterations; j++) {
                    counter.incrementAtomic();
                }
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
    }
    
    /**
     * Test degli strumenti di debugging per thread
     */
    private static void testThreadDebuggingTools() {
        System.out.println("Dimostrazione di strumenti di debugging per thread:");
        
        // Ottiene informazioni sui thread attivi
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        
        // Stampa informazioni sui thread
        System.out.println("\n- Informazioni sui thread attivi:");
        ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(true, true);
        for (int i = 0; i < Math.min(3, threadInfos.length); i++) { // Limita a 3 thread per brevità
            ThreadInfo info = threadInfos[i];
            System.out.println("  Thread: " + info.getThreadName() + 
                    " (ID: " + info.getThreadId() + ")");
            System.out.println("  Stato: " + info.getThreadState());
            System.out.println("  Blocchi posseduti: " + info.getLockedMonitors().length);
            System.out.println("  Stack trace: " + 
                    (info.getStackTrace().length > 0 ? 
                            info.getStackTrace()[0].toString() : "vuoto"));
            System.out.println();
        }
        
        // Stampa statistiche sui thread
        System.out.println("- Statistiche sui thread:");
        System.out.println("  Thread totali: " + threadMXBean.getThreadCount());
        System.out.println("  Thread daemon: " + threadMXBean.getDaemonThreadCount());
        System.out.println("  Thread di picco: " + threadMXBean.getPeakThreadCount());
        System.out.println("  Thread avviati: " + threadMXBean.getTotalStartedThreadCount());
        
        // Controlla se il monitoraggio CPU è supportato
        if (threadMXBean.isThreadCpuTimeSupported()) {
            System.out.println("\n- Monitoraggio CPU per thread:");
            for (int i = 0; i < Math.min(3, threadInfos.length); i++) {
                ThreadInfo info = threadInfos[i];
                long threadId = info.getThreadId();
                if (threadMXBean.isThreadCpuTimeEnabled()) {
                    long cpuTime = threadMXBean.getThreadCpuTime(threadId) / 1000000; // ns -> ms
                    long userTime = threadMXBean.getThreadUserTime(threadId) / 1000000; // ns -> ms
                    System.out.println("  Thread: " + info.getThreadName());
                    System.out.println("  CPU time: " + cpuTime + " ms");
                    System.out.println("  User time: " + userTime + " ms");
                }
            }
        }
    }
    
    /**
     * Classe Counter con diverse implementazioni di incremento
     */
    private static class Counter {
        private int synchronizedCount = 0;
        private int lockCount = 0;
        private final AtomicInteger atomicCount = new AtomicInteger(0);
        private final Lock lock = new ReentrantLock();
        
        // Incremento con synchronized
        public synchronized void incrementSynchronized() {
            synchronizedCount++;
        }
        
        // Incremento con ReentrantLock
        public void incrementWithLock() {
            lock.lock();
            try {
                lockCount++;
            } finally {
                lock.unlock();
            }
        }
        
        // Incremento con AtomicInteger
        public void incrementAtomic() {
            atomicCount.incrementAndGet();
        }
    }
}