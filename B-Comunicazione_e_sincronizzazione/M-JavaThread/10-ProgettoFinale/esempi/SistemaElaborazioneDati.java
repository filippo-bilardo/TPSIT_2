import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Esempio di implementazione di un Sistema di Elaborazione Dati Concorrente.
 * Questo sistema dimostra l'utilizzo di vari meccanismi di concorrenza
 * e pattern appresi durante il corso.
 */
public class SistemaElaborazioneDati {

    public static void main(String[] args) throws Exception {
        // Creazione del sistema
        Controller controller = new Controller();
        
        // Avvio del sistema
        controller.start();
        
        // Esecuzione per un periodo di tempo
        Thread.sleep(30000); // 30 secondi
        
        // Arresto del sistema
        controller.shutdown();
        
        // Stampa delle statistiche finali
        controller.printFinalStats();
    }
    
    /**
     * Classe che rappresenta un dato da elaborare.
     */
    static class Data {
        private final int id;
        private final String source;
        private final double value;
        private final long timestamp;
        
        public Data(int id, String source, double value) {
            this.id = id;
            this.source = source;
            this.value = value;
            this.timestamp = System.currentTimeMillis();
        }
        
        public int getId() { return id; }
        public String getSource() { return source; }
        public double getValue() { return value; }
        public long getTimestamp() { return timestamp; }
        
        @Override
        public String toString() {
            return String.format("Data[id=%d, source=%s, value=%.2f, time=%d]", 
                               id, source, value, timestamp);
        }
    }
    
    /**
     * Classe che rappresenta il risultato dell'elaborazione.
     */
    static class Result {
        private final int dataId;
        private final double processedValue;
        private final long processingTime;
        
        public Result(int dataId, double processedValue, long processingTime) {
            this.dataId = dataId;
            this.processedValue = processedValue;
            this.processingTime = processingTime;
        }
        
        public int getDataId() { return dataId; }
        public double getProcessedValue() { return processedValue; }
        public long getProcessingTime() { return processingTime; }
    }
    
    /**
     * Componente che genera dati simulati.
     */
    static class DataGenerator implements Runnable {
        private final BlockingQueue<Data> outputQueue;
        private final String sourceName;
        private final Random random = new Random();
        private final AtomicLong counter = new AtomicLong(0);
        private volatile boolean running = true;
        private final int delayMs;
        
        public DataGenerator(String sourceName, BlockingQueue<Data> queue, int delayMs) {
            this.sourceName = sourceName;
            this.outputQueue = queue;
            this.delayMs = delayMs;
        }
        
        @Override
        public void run() {
            try {
                while (running) {
                    // Genera un nuovo dato
                    int id = (int) counter.incrementAndGet();
                    double value = random.nextDouble() * 100;
                    Data data = new Data(id, sourceName, value);
                    
                    // Inserisci il dato nella coda
                    outputQueue.put(data);
                    
                    // Simula un ritardo nella generazione
                    Thread.sleep(delayMs);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("DataGenerator " + sourceName + " interrotto");
            } catch (Exception e) {
                System.err.println("Errore in DataGenerator " + sourceName + ": " + e.getMessage());
            }
        }
        
        public void stop() {
            running = false;
        }
        
        public long getGeneratedCount() {
            return counter.get();
        }
    }
    
    /**
     * Pool di worker che elaborano i dati.
     */
    static class WorkerPool {
        private final ExecutorService executor;
        private final BlockingQueue<Data> inputQueue;
        private final Consumer<Result> resultConsumer;
        private final AtomicLong processedCount = new AtomicLong(0);
        private final AtomicLong errorCount = new AtomicLong(0);
        private volatile boolean running = true;
        private final List<Future<?>> workerFutures = new ArrayList<>();
        
        public WorkerPool(int numWorkers, BlockingQueue<Data> inputQueue, Consumer<Result> resultConsumer) {
            this.executor = Executors.newFixedThreadPool(numWorkers);
            this.inputQueue = inputQueue;
            this.resultConsumer = resultConsumer;
            
            // Avvia i worker
            for (int i = 0; i < numWorkers; i++) {
                workerFutures.add(executor.submit(this::workerTask));
            }
        }
        
        private void workerTask() {
            try {
                while (running || !inputQueue.isEmpty()) {
                    // Preleva un dato dalla coda con timeout
                    Data data = inputQueue.poll(100, TimeUnit.MILLISECONDS);
                    if (data != null) {
                        try {
                            // Elabora il dato
                            long startTime = System.currentTimeMillis();
                            double processedValue = processData(data);
                            long processingTime = System.currentTimeMillis() - startTime;
                            
                            // Crea il risultato
                            Result result = new Result(data.getId(), processedValue, processingTime);
                            
                            // Invia il risultato al consumer
                            resultConsumer.accept(result);
                            
                            // Incrementa il contatore
                            processedCount.incrementAndGet();
                        } catch (Exception e) {
                            errorCount.incrementAndGet();
                            System.err.println("Errore nell'elaborazione del dato " + data.getId() + ": " + e.getMessage());
                        }
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Worker interrotto");
            }
        }
        
        private double processData(Data data) {
            // Simulazione di elaborazione
            try {
                // Simula un carico di lavoro variabile
                int processingTime = 50 + new Random().nextInt(200);
                Thread.sleep(processingTime);
                
                // Applica una trasformazione al valore
                return Math.sqrt(data.getValue()) * 10;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Elaborazione interrotta", e);
            }
        }
        
        public void shutdown() {
            running = false;
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
        
        public long getProcessedCount() {
            return processedCount.get();
        }
        
        public long getErrorCount() {
            return errorCount.get();
        }
    }
    
    /**
     * Componente che aggrega i risultati dell'elaborazione.
     */
    static class ResultAggregator {
        private final Map<String, AggregatedStats> statsBySource = new ConcurrentHashMap<>();
        private final ReadWriteLock lock = new ReentrantReadWriteLock();
        private final AtomicLong totalResults = new AtomicLong(0);
        
        public void addResult(Result result, String source) {
            totalResults.incrementAndGet();
            
            // Aggiorna le statistiche per la fonte
            statsBySource.computeIfAbsent(source, k -> new AggregatedStats())
                         .updateStats(result.getProcessedValue(), result.getProcessingTime());
        }
        
        public Map<String, AggregatedStats> getStatsBySource() {
            lock.readLock().lock();
            try {
                // Crea una copia delle statistiche
                Map<String, AggregatedStats> copy = new HashMap<>();
                for (Map.Entry<String, AggregatedStats> entry : statsBySource.entrySet()) {
                    copy.put(entry.getKey(), entry.getValue().copy());
                }
                return copy;
            } finally {
                lock.readLock().unlock();
            }
        }
        
        public long getTotalResults() {
            return totalResults.get();
        }
        
        /**
         * Classe che mantiene statistiche aggregate.
         */
        static class AggregatedStats {
            private double sum = 0;
            private double min = Double.MAX_VALUE;
            private double max = Double.MIN_VALUE;
            private long count = 0;
            private long totalProcessingTime = 0;
            
            public synchronized void updateStats(double value, long processingTime) {
                sum += value;
                min = Math.min(min, value);
                max = Math.max(max, value);
                count++;
                totalProcessingTime += processingTime;
            }
            
            public synchronized AggregatedStats copy() {
                AggregatedStats copy = new AggregatedStats();
                copy.sum = this.sum;
                copy.min = this.min;
                copy.max = this.max;
                copy.count = this.count;
                copy.totalProcessingTime = this.totalProcessingTime;
                return copy;
            }
            
            public double getAverage() {
                return count > 0 ? sum / count : 0;
            }
            
            public double getMin() {
                return min != Double.MAX_VALUE ? min : 0;
            }
            
            public double getMax() {
                return max != Double.MIN_VALUE ? max : 0;
            }
            
            public long getCount() {
                return count;
            }
            
            public double getAverageProcessingTime() {
                return count > 0 ? (double) totalProcessingTime / count : 0;
            }
        }
    }
    
    /**
     * Componente che visualizza lo stato del sistema.
     */
    static class Dashboard implements Runnable {
        private final BlockingQueue<Data> inputQueue;
        private final ResultAggregator aggregator;
        private final List<DataGenerator> generators;
        private final WorkerPool workerPool;
        private volatile boolean running = true;
        
        public Dashboard(BlockingQueue<Data> inputQueue, ResultAggregator aggregator, 
                        List<DataGenerator> generators, WorkerPool workerPool) {
            this.inputQueue = inputQueue;
            this.aggregator = aggregator;
            this.generators = generators;
            this.workerPool = workerPool;
        }
        
        @Override
        public void run() {
            try {
                while (running) {
                    // Stampa lo stato del sistema
                    printSystemStatus();
                    
                    // Aggiorna ogni 2 secondi
                    Thread.sleep(2000);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Dashboard interrotta");
            }
        }
        
        private void printSystemStatus() {
            System.out.println("\n=== STATO DEL SISTEMA ===\n");
            
            // Stato delle code
            System.out.println("Coda di input: " + inputQueue.size() + " elementi");
            
            // Stato dei generatori
            System.out.println("\nGeneratori di dati:");
            long totalGenerated = 0;
            for (int i = 0; i < generators.size(); i++) {
                DataGenerator generator = generators.get(i);
                long count = generator.getGeneratedCount();
                totalGenerated += count;
                System.out.println("  Fonte-" + i + ": " + count + " elementi generati");
            }
            
            // Stato dei worker
            System.out.println("\nElaborazione:");
            System.out.println("  Elementi elaborati: " + workerPool.getProcessedCount());
            System.out.println("  Errori: " + workerPool.getErrorCount());
            
            // Statistiche aggregate
            System.out.println("\nStatistiche per fonte:");
            Map<String, ResultAggregator.AggregatedStats> statsBySource = aggregator.getStatsBySource();
            for (Map.Entry<String, ResultAggregator.AggregatedStats> entry : statsBySource.entrySet()) {
                ResultAggregator.AggregatedStats stats = entry.getValue();
                System.out.println("  " + entry.getKey() + ":");
                System.out.printf("    Media: %.2f, Min: %.2f, Max: %.2f, Conteggio: %d%n", 
                                 stats.getAverage(), stats.getMin(), stats.getMax(), stats.getCount());
                System.out.printf("    Tempo medio di elaborazione: %.2f ms%n", 
                                 stats.getAverageProcessingTime());
            }
            
            // Riepilogo
            System.out.println("\nRiepilogo:");
            System.out.println("  Totale generati: " + totalGenerated);
            System.out.println("  Totale elaborati: " + workerPool.getProcessedCount());
            System.out.println("  Totale risultati: " + aggregator.getTotalResults());
            
            // Calcolo throughput
            double throughput = workerPool.getProcessedCount() / (Runtime.getRuntime().totalMemory() / 1_000_000.0);
            System.out.printf("  Throughput: %.2f elementi/MB%n", throughput);
        }
        
        public void stop() {
            running = false;
        }
    }
    
    /**
     * Componente che gestisce il ciclo di vita dell'applicazione.
     */
    static class Controller {
        private static final int NUM_SOURCES = 3;
        private static final int NUM_WORKERS = 4;
        private static final int QUEUE_CAPACITY = 100;
        
        private final BlockingQueue<Data> dataQueue = new ArrayBlockingQueue<>(QUEUE_CAPACITY);
        private final ResultAggregator aggregator = new ResultAggregator();
        private final List<DataGenerator> generators = new ArrayList<>();
        private final List<Thread> generatorThreads = new ArrayList<>();
        private WorkerPool workerPool;
        private Dashboard dashboard;
        private Thread dashboardThread;
        
        public void start() {
            System.out.println("Avvio del sistema di elaborazione dati...");
            
            // Crea e avvia i generatori di dati
            for (int i = 0; i < NUM_SOURCES; i++) {
                String sourceName = "Fonte-" + i;
                // Ogni fonte ha un ritardo diverso
                int delay = 100 + i * 50;
                DataGenerator generator = new DataGenerator(sourceName, dataQueue, delay);
                generators.add(generator);
                
                Thread thread = new Thread(generator, "Generator-" + i);
                generatorThreads.add(thread);
                thread.start();
            }
            
            // Crea e avvia il pool di worker
            workerPool = new WorkerPool(NUM_WORKERS, dataQueue, 
                                      result -> aggregator.addResult(result, "Fonte-" + (result.getDataId() % NUM_SOURCES)));
            
            // Crea e avvia la dashboard
            dashboard = new Dashboard(dataQueue, aggregator, generators, workerPool);
            dashboardThread = new Thread(dashboard, "Dashboard");
            dashboardThread.start();
            
            System.out.println("Sistema avviato con " + NUM_SOURCES + " fonti e " + 
                              NUM_WORKERS + " worker");
        }
        
        public void shutdown() {
            System.out.println("\nArresto del sistema...");
            
            // Ferma i generatori
            for (DataGenerator generator : generators) {
                generator.stop();
            }
            
            // Interrompi i thread dei generatori
            for (Thread thread : generatorThreads) {
                thread.interrupt();
            }
            
            // Attendi che i worker completino l'elaborazione
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            // Ferma il pool di worker
            workerPool.shutdown();
            
            // Ferma la dashboard
            dashboard.stop();
            dashboardThread.interrupt();
            
            System.out.println("Sistema arrestato");
        }
        
        public void printFinalStats() {
            System.out.println("\n=== STATISTICHE FINALI ===\n");
            
            // Statistiche dei generatori
            long totalGenerated = 0;
            for (int i = 0; i < generators.size(); i++) {
                DataGenerator generator = generators.get(i);
                long count = generator.getGeneratedCount();
                totalGenerated += count;
                System.out.println("Fonte-" + i + ": " + count + " elementi generati");
            }
            
            // Statistiche di elaborazione
            System.out.println("\nElaborazione:");
            System.out.println("Elementi elaborati: " + workerPool.getProcessedCount());
            System.out.println("Errori: " + workerPool.getErrorCount());
            
            // Statistiche aggregate
            System.out.println("\nStatistiche per fonte:");
            Map<String, ResultAggregator.AggregatedStats> statsBySource = aggregator.getStatsBySource();
            for (Map.Entry<String, ResultAggregator.AggregatedStats> entry : statsBySource.entrySet()) {
                ResultAggregator.AggregatedStats stats = entry.getValue();
                System.out.println(entry.getKey() + ":");
                System.out.printf("  Media: %.2f, Min: %.2f, Max: %.2f, Conteggio: %d%n", 
                                stats.getAverage(), stats.getMin(), stats.getMax(), stats.getCount());
                System.out.printf("  Tempo medio di elaborazione: %.2f ms%n", 
                                stats.getAverageProcessingTime());
            }
            
            // Riepilogo
            System.out.println("\nRiepilogo finale:");
            System.out.println("Totale generati: " + totalGenerated);
            System.out.println("Totale elaborati: " + workerPool.getProcessedCount());
            System.out.println("Totale risultati: " + aggregator.getTotalResults());
            
            // Efficienza
            double efficiency = (double) workerPool.getProcessedCount() / totalGenerated * 100;
            System.out.printf("Efficienza: %.2f%%%n", efficiency);
        }
    }
}