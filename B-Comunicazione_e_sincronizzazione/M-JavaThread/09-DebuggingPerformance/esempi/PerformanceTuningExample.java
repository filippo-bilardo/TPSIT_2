package esempi;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;

/**
 * Questo esempio dimostra varie tecniche di ottimizzazione delle prestazioni
 * per applicazioni multi-thread, confrontando diverse implementazioni e
 * misurando le loro prestazioni.
 */
public class PerformanceTuningExample {

    // Numero di thread da utilizzare nei test
    private static final int NUM_THREADS = Runtime.getRuntime().availableProcessors();
    // Numero di operazioni per thread
    private static final int OPERATIONS_PER_THREAD = 1_000_000;
    // Rapporto letture/scritture (9:1 è comune in molte applicazioni)
    private static final int READ_WRITE_RATIO = 9;
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Test di ottimizzazione delle prestazioni con " + NUM_THREADS + " thread");
        System.out.println("Operazioni per thread: " + NumberFormat.getInstance().format(OPERATIONS_PER_THREAD));
        System.out.println("Rapporto letture/scritture: " + READ_WRITE_RATIO + ":1\n");
        
        // Test 1: Confronto tra diverse implementazioni di contatori
        System.out.println("=== TEST 1: CONFRONTO TRA CONTATORI ===\n");
        testCounters();
        
        // Test 2: Confronto tra diverse implementazioni di collezioni
        System.out.println("\n=== TEST 2: CONFRONTO TRA COLLEZIONI ===\n");
        testCollections();
        
        // Test 3: Confronto tra diverse implementazioni di lock
        System.out.println("\n=== TEST 3: CONFRONTO TRA LOCK ===\n");
        testLocks();
        
        // Test 4: Confronto tra diverse dimensioni di pool di thread
        System.out.println("\n=== TEST 4: DIMENSIONAMENTO POOL DI THREAD ===\n");
        testThreadPoolSizing();
    }
    
    /**
     * Confronta le prestazioni di diverse implementazioni di contatori.
     */
    private static void testCounters() throws InterruptedException {
        // Test con contatore sincronizzato
        long syncTime = timeCounter(new SynchronizedCounter());
        
        // Test con contatore che utilizza lock esplicito
        long lockTime = timeCounter(new LockCounter());
        
        // Test con contatore atomico
        long atomicTime = timeCounter(new AtomicCounter());
        
        // Test con LongAdder (ottimizzato per alta concorrenza)
        long adderTime = timeCounter(new LongAdderCounter());
        
        System.out.println("Tempi di esecuzione per " + 
                          NumberFormat.getInstance().format(NUM_THREADS * OPERATIONS_PER_THREAD) + 
                          " incrementi:");
        System.out.println("Synchronized: " + syncTime + " ms");
        System.out.println("ReentrantLock: " + lockTime + " ms");
        System.out.println("AtomicInteger: " + atomicTime + " ms");
        System.out.println("LongAdder: " + adderTime + " ms");
    }
    
    /**
     * Misura il tempo di esecuzione per un contatore con più thread.
     */
    private static long timeCounter(Counter counter) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        CountDownLatch latch = new CountDownLatch(NUM_THREADS);
        
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < NUM_THREADS; i++) {
            executor.submit(() -> {
                for (int j = 0; j < OPERATIONS_PER_THREAD; j++) {
                    counter.increment();
                }
                latch.countDown();
            });
        }
        
        latch.await();
        long endTime = System.currentTimeMillis();
        
        executor.shutdown();
        return endTime - startTime;
    }
    
    /**
     * Confronta le prestazioni di diverse implementazioni di collezioni.
     */
    private static void testCollections() throws InterruptedException {
        // Dimensione iniziale delle collezioni
        int initialSize = 10000;
        
        // Prepara i dati iniziali
        List<Integer> initialData = new ArrayList<>(initialSize);
        Random random = new Random(42); // Seed fisso per riproducibilità
        for (int i = 0; i < initialSize; i++) {
            initialData.add(random.nextInt(1000000));
        }
        
        // Test con ArrayList sincronizzata
        long syncListTime = timeCollection(
            Collections.synchronizedList(new ArrayList<>(initialData)));
        
        // Test con CopyOnWriteArrayList
        long cowListTime = timeCollection(
            new CopyOnWriteArrayList<>(initialData));
        
        // Test con HashMap sincronizzata
        Map<Integer, Integer> syncMap = Collections.synchronizedMap(new ConcurrentHashMap<>());
        for (Integer value : initialData) {
            syncMap.put(value, value);
        }
        long syncMapTime = timeMap(syncMap);
        
        // Test con ConcurrentHashMap
        Map<Integer, Integer> concurrentMap = new ConcurrentHashMap<>();
        for (Integer value : initialData) {
            concurrentMap.put(value, value);
        }
        long concurrentMapTime = timeMap(concurrentMap);
        
        System.out.println("Tempi di esecuzione per operazioni su collezioni:");
        System.out.println("ArrayList sincronizzata: " + syncListTime + " ms");
        System.out.println("CopyOnWriteArrayList: " + cowListTime + " ms");
        System.out.println("HashMap sincronizzata: " + syncMapTime + " ms");
        System.out.println("ConcurrentHashMap: " + concurrentMapTime + " ms");
    }
    
    /**
     * Misura il tempo di esecuzione per operazioni su una lista con più thread.
     */
    private static long timeCollection(List<Integer> list) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        CountDownLatch latch = new CountDownLatch(NUM_THREADS);
        Random random = new Random();
        
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < NUM_THREADS; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < OPERATIONS_PER_THREAD / 100; j++) { // Ridotto per evitare tempi troppo lunghi
                        int operation = random.nextInt(10);
                        int value = random.nextInt(1000000);
                        
                        if (operation < READ_WRITE_RATIO) { // Lettura
                            int index = random.nextInt(list.size());
                            list.get(index);
                        } else { // Scrittura
                            if (list.size() < 20000) { // Limita la dimensione massima
                                list.add(value);
                            } else {
                                int index = random.nextInt(list.size());
                                list.set(index, value);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }
        
        latch.await();
        long endTime = System.currentTimeMillis();
        
        executor.shutdown();
        return endTime - startTime;
    }
    
    /**
     * Misura il tempo di esecuzione per operazioni su una mappa con più thread.
     */
    private static long timeMap(Map<Integer, Integer> map) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        CountDownLatch latch = new CountDownLatch(NUM_THREADS);
        Random random = new Random();
        
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < NUM_THREADS; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < OPERATIONS_PER_THREAD / 100; j++) { // Ridotto per evitare tempi troppo lunghi
                        int operation = random.nextInt(10);
                        int key = random.nextInt(1000000);
                        
                        if (operation < READ_WRITE_RATIO) { // Lettura
                            map.get(key);
                        } else { // Scrittura
                            map.put(key, key);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }
        
        latch.await();
        long endTime = System.currentTimeMillis();
        
        executor.shutdown();
        return endTime - startTime;
    }
    
    /**
     * Confronta le prestazioni di diverse implementazioni di lock.
     */
    private static void testLocks() throws InterruptedException {
        // Test con synchronized
        long syncTime = timeLock(new SynchronizedLockBenchmark());
        
        // Test con ReentrantLock
        long reentrantTime = timeLock(new ReentrantLockBenchmark());
        
        // Test con ReadWriteLock
        long rwLockTime = timeLock(new ReadWriteLockBenchmark());
        
        // Test con StampedLock
        long stampedLockTime = timeLock(new StampedLockBenchmark());
        
        System.out.println("Tempi di esecuzione per operazioni con diversi lock:");
        System.out.println("Synchronized: " + syncTime + " ms");
        System.out.println("ReentrantLock: " + reentrantTime + " ms");
        System.out.println("ReadWriteLock: " + rwLockTime + " ms");
        System.out.println("StampedLock: " + stampedLockTime + " ms");
    }
    
    /**
     * Misura il tempo di esecuzione per operazioni con un lock con più thread.
     */
    private static long timeLock(LockBenchmark lockBenchmark) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        CountDownLatch latch = new CountDownLatch(NUM_THREADS);
        Random random = new Random();
        
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < NUM_THREADS; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < OPERATIONS_PER_THREAD / 10; j++) { // Ridotto per evitare tempi troppo lunghi
                        int operation = random.nextInt(10);
                        
                        if (operation < READ_WRITE_RATIO) { // Lettura
                            lockBenchmark.read();
                        } else { // Scrittura
                            lockBenchmark.write(random.nextInt(1000));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }
        
        latch.await();
        long endTime = System.currentTimeMillis();
        
        executor.shutdown();
        return endTime - startTime;
    }
    
    /**
     * Confronta le prestazioni di diverse dimensioni di pool di thread.
     */
    private static void testThreadPoolSizing() throws InterruptedException {
        int[] poolSizes = {
            1,
            NUM_THREADS / 2,
            NUM_THREADS,
            NUM_THREADS * 2,
            NUM_THREADS * 4
        };
        
        for (int poolSize : poolSizes) {
            long time = timeThreadPool(poolSize);
            System.out.println("Pool di thread di dimensione " + poolSize + ": " + time + " ms");
        }
    }
    
    /**
     * Misura il tempo di esecuzione per un pool di thread di dimensione specificata.
     */
    private static long timeThreadPool(int poolSize) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(poolSize);
        int totalTasks = NUM_THREADS * 10; // Numero fisso di task indipendentemente dalla dimensione del pool
        CountDownLatch latch = new CountDownLatch(totalTasks);
        
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < totalTasks; i++) {
            executor.submit(() -> {
                try {
                    // Simulazione di un task con CPU e I/O
                    // 30% CPU-bound, 70% I/O-bound
                    if (ThreadLocalRandom.current().nextInt(10) < 3) {
                        // CPU-bound task
                        long sum = 0;
                        for (int j = 0; j < 1000000; j++) {
                            sum += j;
                        }
                    } else {
                        // I/O-bound task (simulato con sleep)
                        Thread.sleep(10);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    latch.countDown();
                }
            });
        }
        
        latch.await();
        long endTime = System.currentTimeMillis();
        
        executor.shutdown();
        return endTime - startTime;
    }
    
    // Interfacce e implementazioni per i test
    
    interface Counter {
        void increment();
        long getValue();
    }
    
    static class SynchronizedCounter implements Counter {
        private long count = 0;
        
        @Override
        public synchronized void increment() {
            count++;
        }
        
        @Override
        public synchronized long getValue() {
            return count;
        }
    }
    
    static class LockCounter implements Counter {
        private final Lock lock = new ReentrantLock();
        private long count = 0;
        
        @Override
        public void increment() {
            lock.lock();
            try {
                count++;
            } finally {
                lock.unlock();
            }
        }
        
        @Override
        public long getValue() {
            lock.lock();
            try {
                return count;
            } finally {
                lock.unlock();
            }
        }
    }
    
    static class AtomicCounter implements Counter {
        private final AtomicInteger count = new AtomicInteger(0);
        
        @Override
        public void increment() {
            count.incrementAndGet();
        }
        
        @Override
        public long getValue() {
            return count.get();
        }
    }
    
    static class LongAdderCounter implements Counter {
        private final LongAdder count = new LongAdder();
        
        @Override
        public void increment() {
            count.increment();
        }
        
        @Override
        public long getValue() {
            return count.sum();
        }
    }
    
    interface LockBenchmark {
        int read();
        void write(int value);
    }
    
    static class SynchronizedLockBenchmark implements LockBenchmark {
        private int value = 0;
        
        @Override
        public synchronized int read() {
            return value;
        }
        
        @Override
        public synchronized void write(int value) {
            this.value = value;
        }
    }
    
    static class ReentrantLockBenchmark implements LockBenchmark {
        private final Lock lock = new ReentrantLock();
        private int value = 0;
        
        @Override
        public int read() {
            lock.lock();
            try {
                return value;
            } finally {
                lock.unlock();
            }
        }
        
        @Override
        public void write(int value) {
            lock.lock();
            try {
                this.value = value;
            } finally {
                lock.unlock();
            }
        }
    }
    
    static class ReadWriteLockBenchmark implements LockBenchmark {
        private final ReadWriteLock lock = new ReentrantReadWriteLock();
        private int value = 0;
        
        @Override
        public int read() {
            lock.readLock().lock();
            try {
                return value;
            } finally {
                lock.readLock().unlock();
            }
        }
        
        @Override
        public void write(int value) {
            lock.writeLock().lock();
            try {
                this.value = value;
            } finally {
                lock.writeLock().unlock();
            }
        }
    }
    
    static class StampedLockBenchmark implements LockBenchmark {
        private final StampedLock lock = new StampedLock();
        private int value = 0;
        
        @Override
        public int read() {
            long stamp = lock.tryOptimisticRead();
            int currentValue = value;
            if (!lock.validate(stamp)) {
                stamp = lock.readLock();
                try {
                    currentValue = value;
                } finally {
                    lock.unlockRead(stamp);
                }
            }
            return currentValue;
        }
        
        @Override
        public void write(int value) {
            long stamp = lock.writeLock();
            try {
                this.value = value;
            } finally {
                lock.unlockWrite(stamp);
            }
        }
    }
}