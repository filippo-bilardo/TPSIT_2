# Ottimizzazione delle Prestazioni

## Misurazione delle Prestazioni

Prima di ottimizzare un'applicazione multi-thread, è fondamentale misurare le prestazioni attuali per identificare i colli di bottiglia. Senza misurazioni accurate, rischiamo di ottimizzare parti del codice che non influiscono significativamente sulle prestazioni complessive.

Ecco alcuni strumenti e tecniche per la misurazione delle prestazioni:

### Profiler Java

I profiler sono strumenti che monitorano l'esecuzione di un'applicazione e raccolgono informazioni dettagliate sulle prestazioni:

- **Java VisualVM**: Strumento incluso nel JDK che fornisce informazioni su CPU, memoria, thread e classi
- **JProfiler**: Profiler commerciale con funzionalità avanzate
- **YourKit**: Altro profiler commerciale popolare

### Benchmark con JMH

JMH (Java Microbenchmark Harness) è uno strumento sviluppato da Oracle per creare, eseguire e analizzare benchmark Java in modo rigoroso:

```java
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Thread)
@Fork(1)
@Warmup(iterations = 5)
@Measurement(iterations = 5)
public class ConcurrencyBenchmark {

    @Benchmark
    public void testMethod() {
        // Codice da misurare
    }
    
    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
                .include(ConcurrencyBenchmark.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }
}
```

### Metriche Personalizzate

In molti casi, è utile implementare metriche personalizzate per misurare aspetti specifici dell'applicazione:

```java
public class PerformanceMonitor {
    private static final Map<String, Long> startTimes = new ConcurrentHashMap<>();
    private static final Map<String, List<Long>> durations = new ConcurrentHashMap<>();
    
    public static void start(String operationName) {
        startTimes.put(operationName, System.nanoTime());
    }
    
    public static void stop(String operationName) {
        Long startTime = startTimes.remove(operationName);
        if (startTime != null) {
            long duration = System.nanoTime() - startTime;
            durations.computeIfAbsent(operationName, k -> new ArrayList<>()).add(duration);
        }
    }
    
    public static void printStatistics() {
        for (Map.Entry<String, List<Long>> entry : durations.entrySet()) {
            List<Long> times = entry.getValue();
            double average = times.stream().mapToLong(Long::longValue).average().orElse(0);
            long min = times.stream().mapToLong(Long::longValue).min().orElse(0);
            long max = times.stream().mapToLong(Long::longValue).max().orElse(0);
            
            System.out.printf("%s: avg=%.2f ns, min=%d ns, max=%d ns, count=%d%n", 
                    entry.getKey(), average, min, max, times.size());
        }
    }
}
```

## Bilanciamento del Carico

Il bilanciamento del carico è essenziale per sfruttare efficacemente tutti i core disponibili. Un'applicazione multi-thread ben bilanciata distribuisce il lavoro in modo uniforme tra i thread.

### Work Stealing

Una tecnica efficace è il "work stealing", implementata nel ForkJoinPool di Java:

```java
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ParallelSum extends RecursiveTask<Long> {
    private final long[] array;
    private final int start;
    private final int end;
    private static final int THRESHOLD = 10000;

    public ParallelSum(long[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        int length = end - start;
        if (length <= THRESHOLD) {
            return computeSequentially();
        }
        
        int middle = start + length / 2;
        ParallelSum leftTask = new ParallelSum(array, start, middle);
        leftTask.fork();
        
        ParallelSum rightTask = new ParallelSum(array, middle, end);
        Long rightResult = rightTask.compute();
        Long leftResult = leftTask.join();
        
        return leftResult + rightResult;
    }

    private long computeSequentially() {
        long sum = 0;
        for (int i = start; i < end; i++) {
            sum += array[i];
        }
        return sum;
    }
}
```

### Dimensionamento Dinamico dei Pool

Il dimensionamento dinamico dei pool di thread può migliorare le prestazioni adattandosi al carico di lavoro:

```java
ThreadPoolExecutor executor = new ThreadPoolExecutor(
    corePoolSize,    // Dimensione minima del pool
    maximumPoolSize, // Dimensione massima del pool
    keepAliveTime,   // Tempo di inattività prima che un thread in eccesso venga terminato
    TimeUnit.SECONDS,
    new LinkedBlockingQueue<>(),
    new ThreadPoolExecutor.CallerRunsPolicy() // Politica di rifiuto
);

// Monitoraggio e regolazione dinamica
executor.setCorePoolSize(newSize);
executor.setMaximumPoolSize(newMaxSize);
```

## Granularità della Sincronizzazione

La granularità della sincronizzazione ha un impatto significativo sulle prestazioni. Una sincronizzazione troppo fine può introdurre overhead, mentre una sincronizzazione troppo grossolana può limitare la concorrenza.

### Sincronizzazione Fine vs Grossolana

**Sincronizzazione grossolana**:

```java
public class CoarseGrainedList<T> {
    private final List<T> list = new ArrayList<>();
    
    public synchronized void add(T item) {
        list.add(item);
    }
    
    public synchronized boolean remove(T item) {
        return list.remove(item);
    }
    
    public synchronized boolean contains(T item) {
        return list.contains(item);
    }
}
```

**Sincronizzazione fine**:

```java
public class FineGrainedList<T> {
    private final List<T> list = new ArrayList<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    
    public void add(T item) {
        lock.writeLock().lock();
        try {
            list.add(item);
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    public boolean remove(T item) {
        lock.writeLock().lock();
        try {
            return list.remove(item);
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    public boolean contains(T item) {
        lock.readLock().lock();
        try {
            return list.contains(item);
        } finally {
            lock.readLock().unlock();
        }
    }
}
```

### Lock Stripping

Il "lock stripping" divide una struttura dati in segmenti, ciascuno con il proprio lock:

```java
public class StripedMap<K, V> {
    private static final int NUM_LOCKS = 16;
    private final Node<K, V>[] buckets;
    private final Object[] locks;
    
    public StripedMap(int numBuckets) {
        buckets = new Node[numBuckets];
        locks = new Object[NUM_LOCKS];
        for (int i = 0; i < NUM_LOCKS; i++) {
            locks[i] = new Object();
        }
    }
    
    private final int hash(Object key) {
        return Math.abs(key.hashCode() % buckets.length);
    }
    
    private Object getLockForBucket(int bucketIndex) {
        return locks[bucketIndex % NUM_LOCKS];
    }
    
    public V get(K key) {
        int bucketIndex = hash(key);
        synchronized (getLockForBucket(bucketIndex)) {
            for (Node<K, V> n = buckets[bucketIndex]; n != null; n = n.next) {
                if (n.key.equals(key)) {
                    return n.value;
                }
            }
            return null;
        }
    }
    
    public void put(K key, V value) {
        int bucketIndex = hash(key);
        synchronized (getLockForBucket(bucketIndex)) {
            // Implementazione dell'inserimento
        }
    }
    
    // Classe interna per i nodi della mappa
    private static class Node<K, V> {
        final K key;
        V value;
        Node<K, V> next;
        
        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
```

## Tecniche di Scalabilità

La scalabilità è la capacità di un'applicazione di gestire un carico crescente aggiungendo risorse. Ecco alcune tecniche per migliorare la scalabilità:

### Riduzione della Contesa

La contesa per le risorse condivise è uno dei principali ostacoli alla scalabilità. Tecniche per ridurre la contesa includono:

- **Strutture dati lock-free**: Utilizzare implementazioni non bloccanti come `ConcurrentHashMap` e `ConcurrentLinkedQueue`
- **Variabili thread-local**: Utilizzare `ThreadLocal` per mantenere stato specifico per thread
- **Partitioning**: Dividere i dati in partizioni indipendenti

```java
// Esempio di ThreadLocal
private static final ThreadLocal<SimpleDateFormat> dateFormat = 
    ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));

public String formatDate(Date date) {
    return dateFormat.get().format(date);
}
```

### Amdahl's Law

La legge di Amdahl descrive il limite teorico di speedup ottenibile parallelizzando un programma:

```
Speedup = 1 / (S + (1-S)/N)
```

dove S è la frazione di esecuzione che deve rimanere seriale e N è il numero di processori.

Questa legge ci ricorda che è fondamentale ridurre al minimo la parte seriale del codice per ottenere una buona scalabilità.

### Scalabilità Verticale vs Orizzontale

- **Scalabilità verticale**: Aggiungere più risorse a un singolo nodo (più CPU, più memoria)
- **Scalabilità orizzontale**: Aggiungere più nodi a un sistema

Le applicazioni Java moderne spesso combinano entrambi gli approcci, utilizzando la concorrenza per la scalabilità verticale e architetture distribuite per la scalabilità orizzontale.

## Conclusioni

L'ottimizzazione delle prestazioni nelle applicazioni multi-thread richiede un approccio metodico:

1. Misurare le prestazioni attuali per identificare i colli di bottiglia
2. Bilanciare il carico tra i thread disponibili
3. Scegliere la granularità di sincronizzazione appropriata
4. Applicare tecniche di scalabilità per gestire carichi crescenti

Ricorda che l'ottimizzazione prematura è la radice di molti problemi. Misura sempre prima di ottimizzare e verifica che le modifiche abbiano effettivamente migliorato le prestazioni.

## Navigazione

- [Indice del Modulo](./README.md)
- Precedente: [Strumenti di Debugging](./04-StrumentiDebugging.md)
- Successivo: [Progetto Finale](../10-ProgettoFinale/README.md)