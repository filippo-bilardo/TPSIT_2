# Classi Atomic Avanzate

Oltre alle classi Atomic di base, Java fornisce diverse classi Atomic avanzate per gestire scenari più complessi e specifici. In questo capitolo, esploreremo queste classi e i loro casi d'uso.

## Array Atomici

Java fornisce classi per gestire array di valori primitivi in modo atomico:

### AtomicIntegerArray

La classe `AtomicIntegerArray` permette di eseguire operazioni atomiche su un array di interi.

```java
import java.util.concurrent.atomic.AtomicIntegerArray;

public class AtomicIntegerArrayExample {
    public static void main(String[] args) {
        // Creazione di un array atomico di 5 elementi
        AtomicIntegerArray array = new AtomicIntegerArray(5);
        
        // Impostazione di valori
        array.set(0, 10);
        array.set(1, 20);
        
        // Operazioni atomiche
        int oldValue = array.getAndIncrement(0); // Incrementa e restituisce il valore precedente
        int newValue = array.incrementAndGet(1); // Incrementa e restituisce il nuovo valore
        
        System.out.println("Valore precedente all'indice 0: " + oldValue);
        System.out.println("Nuovo valore all'indice 0: " + array.get(0));
        System.out.println("Nuovo valore all'indice 1: " + newValue);
        
        // Operazione CAS
        boolean success = array.compareAndSet(2, 0, 30);
        System.out.println("CAS riuscito? " + success);
        System.out.println("Valore all'indice 2: " + array.get(2));
    }
}
```

### AtomicLongArray e AtomicReferenceArray

In modo simile, Java fornisce `AtomicLongArray` per array di long e `AtomicReferenceArray` per array di oggetti:

```java
import java.util.concurrent.atomic.AtomicReferenceArray;

public class AtomicReferenceArrayExample {
    public static void main(String[] args) {
        // Creazione di un array atomico di stringhe
        AtomicReferenceArray<String> array = new AtomicReferenceArray<>(3);
        
        // Impostazione di valori
        array.set(0, "Hello");
        array.set(1, "World");
        
        // Operazioni atomiche
        String oldValue = array.getAndSet(0, "Ciao");
        
        System.out.println("Valore precedente all'indice 0: " + oldValue);
        System.out.println("Nuovo valore all'indice 0: " + array.get(0));
        
        // Operazione CAS
        boolean success = array.compareAndSet(1, "World", "Mondo");
        System.out.println("CAS riuscito? " + success);
        System.out.println("Valore all'indice 1: " + array.get(1));
    }
}
```

## Riferimenti Atomici Avanzati

Come abbiamo visto nel capitolo sul problema dell'ABA, Java fornisce classi speciali per gestire riferimenti atomici con informazioni aggiuntive:

### AtomicStampedReference

La classe `AtomicStampedReference` associa un intero (stamp) a un riferimento, permettendo di rilevare il problema dell'ABA:

```java
import java.util.concurrent.atomic.AtomicStampedReference;

public class AtomicStampedReferenceExample {
    public static void main(String[] args) {
        String initialRef = "initial";
        int initialStamp = 0;
        
        AtomicStampedReference<String> reference = 
            new AtomicStampedReference<>(initialRef, initialStamp);
        
        // Ottiene il valore e lo stamp correnti
        int[] stampHolder = new int[1];
        String value = reference.get(stampHolder);
        int stamp = stampHolder[0];
        
        System.out.println("Valore iniziale: " + value + ", Stamp: " + stamp);
        
        // Aggiorna il riferimento e lo stamp
        boolean success = reference.compareAndSet(
            initialRef, "new", initialStamp, initialStamp + 1);
        
        System.out.println("CAS riuscito? " + success);
        System.out.println("Nuovo valore: " + reference.getReference() + 
                          ", Nuovo stamp: " + reference.getStamp());
        
        // Tentativo che fallirà a causa dello stamp diverso
        success = reference.compareAndSet(
            "new", "newer", initialStamp, initialStamp + 2);
        
        System.out.println("Secondo CAS riuscito? " + success);
    }
}
```

### AtomicMarkableReference

La classe `AtomicMarkableReference` associa un booleano (mark) a un riferimento, utile per implementare algoritmi di rimozione logica:

```java
import java.util.concurrent.atomic.AtomicMarkableReference;

public class AtomicMarkableReferenceExample {
    public static void main(String[] args) {
        String initialRef = "initial";
        boolean initialMark = false;
        
        AtomicMarkableReference<String> reference = 
            new AtomicMarkableReference<>(initialRef, initialMark);
        
        // Ottiene il valore e il mark correnti
        boolean[] markHolder = new boolean[1];
        String value = reference.get(markHolder);
        boolean mark = markHolder[0];
        
        System.out.println("Valore iniziale: " + value + ", Mark: " + mark);
        
        // Marca il riferimento come "rimosso logicamente"
        boolean success = reference.compareAndSet(
            initialRef, initialRef, initialMark, true);
        
        System.out.println("CAS riuscito? " + success);
        System.out.println("Nuovo valore: " + reference.getReference() + 
                          ", Nuovo mark: " + reference.isMarked());
    }
}
```

## Accumulatori ad Alte Prestazioni

Per scenari ad alta concorrenza, Java fornisce classi specializzate che riducono la contesa tra thread:

### LongAdder

La classe `LongAdder` è progettata per scenari in cui molti thread incrementano un contatore. Invece di aggiornare un singolo valore atomico, distribuisce gli aggiornamenti su più variabili interne, riducendo la contesa:

```java
import java.util.concurrent.atomic.LongAdder;

public class LongAdderExample {
    public static void main(String[] args) {
        LongAdder adder = new LongAdder();
        
        // Incrementa il valore
        adder.increment();
        adder.add(5);
        
        // Ottiene la somma
        long sum = adder.sum();
        System.out.println("Somma: " + sum);
        
        // Reset
        adder.reset();
        System.out.println("Dopo reset: " + adder.sum());
    }
}
```

### DoubleAdder

In modo simile, `DoubleAdder` fornisce le stesse funzionalità per valori double.

### LongAccumulator

La classe `LongAccumulator` è una generalizzazione di `LongAdder` che permette di specificare un'operazione binaria (come somma, massimo, minimo, ecc.) e un valore iniziale:

```java
import java.util.concurrent.atomic.LongAccumulator;
import java.util.function.LongBinaryOperator;

public class LongAccumulatorExample {
    public static void main(String[] args) {
        // Crea un accumulatore che calcola il massimo
        LongBinaryOperator max = (x, y) -> Math.max(x, y);
        LongAccumulator accumulator = new LongAccumulator(max, 0);
        
        // Accumula valori
        accumulator.accumulate(5);
        accumulator.accumulate(10);
        accumulator.accumulate(3);
        
        // Ottiene il risultato
        long result = accumulator.get();
        System.out.println("Massimo: " + result);
        
        // Crea un accumulatore per la somma
        LongAccumulator sumAccumulator = new LongAccumulator((x, y) -> x + y, 0);
        
        // Accumula valori
        sumAccumulator.accumulate(5);
        sumAccumulator.accumulate(10);
        
        System.out.println("Somma: " + sumAccumulator.get());
    }
}
```

### DoubleAccumulator

In modo simile, `DoubleAccumulator` fornisce le stesse funzionalità per valori double.

## Confronto di Prestazioni

Le classi `LongAdder` e `LongAccumulator` offrono prestazioni significativamente migliori rispetto a `AtomicLong` in scenari con alta contesa. Ecco un semplice benchmark:

```java
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

public class AtomicPerformanceComparison {
    private static final int NUM_THREADS = 4;
    private static final int ITERATIONS = 1_000_000;
    
    public static void main(String[] args) throws InterruptedException {
        // Test con AtomicLong
        AtomicLong atomicLong = new AtomicLong();
        long atomicLongTime = measurePerformance(() -> {
            for (int i = 0; i < ITERATIONS; i++) {
                atomicLong.incrementAndGet();
            }
        });
        
        // Test con LongAdder
        LongAdder longAdder = new LongAdder();
        long longAdderTime = measurePerformance(() -> {
            for (int i = 0; i < ITERATIONS; i++) {
                longAdder.increment();
            }
        });
        
        System.out.println("AtomicLong tempo: " + atomicLongTime + " ms");
        System.out.println("LongAdder tempo: " + longAdderTime + " ms");
        System.out.println("AtomicLong valore finale: " + atomicLong.get());
        System.out.println("LongAdder valore finale: " + longAdder.sum());
    }
    
    private static long measurePerformance(Runnable task) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < NUM_THREADS; i++) {
            executor.submit(task);
        }
        
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        
        return System.currentTimeMillis() - startTime;
    }
}
```

## Quando Usare le Classi Atomic Avanzate

- **Array Atomici**: Quando è necessario eseguire operazioni atomiche su elementi di un array senza sincronizzare l'intero array.
  
- **AtomicStampedReference**: Quando si implementano algoritmi lock-free che potrebbero essere soggetti al problema dell'ABA.
  
- **AtomicMarkableReference**: Quando si implementano strutture dati con rimozione logica, come liste concatenate lock-free.
  
- **LongAdder/DoubleAdder**: Per contatori ad alte prestazioni in scenari con molti thread che incrementano lo stesso valore.
  
- **LongAccumulator/DoubleAccumulator**: Per operazioni di riduzione personalizzate in ambienti altamente concorrenti.

## Conclusioni

Le classi Atomic avanzate di Java offrono soluzioni specializzate per diversi scenari di concorrenza, permettendo di sviluppare applicazioni ad alte prestazioni senza sacrificare la correttezza. La scelta della classe appropriata dipende dalle specifiche esigenze dell'applicazione e dal pattern di accesso concorrente previsto.

## Navigazione

- [Indice del Corso](../README.md)
- Precedente: [Compare-and-Swap (CAS)](./03-CompareAndSwap.md)
- Successivo: [Atomic Fieldupdater](./05-AtomicFieldUpdater.md)