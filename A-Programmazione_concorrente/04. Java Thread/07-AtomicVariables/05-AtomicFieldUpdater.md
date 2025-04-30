# Atomic Fieldupdater

Le classi `AtomicFieldUpdater` forniscono un modo per eseguire operazioni atomiche su campi volatili di oggetti esistenti senza dover modificare la loro struttura per utilizzare le classi Atomic. In questo capitolo, esploreremo queste classi e i loro casi d'uso.

## Introduzione agli Atomic FieldUpdater

Gli Atomic FieldUpdater sono particolarmente utili quando:

1. Si lavora con classi esistenti che non possono essere modificate per utilizzare direttamente le classi Atomic.
2. Si vuole risparmiare memoria evitando di creare oggetti Atomic per ogni istanza di una classe.
3. Si desidera eseguire operazioni atomiche solo su alcuni campi specifici di una classe.

Java fornisce tre tipi di FieldUpdater:

- `AtomicIntegerFieldUpdater`: Per campi di tipo `int`
- `AtomicLongFieldUpdater`: Per campi di tipo `long`
- `AtomicReferenceFieldUpdater`: Per campi di tipo riferimento

## AtomicIntegerFieldUpdater

La classe `AtomicIntegerFieldUpdater` permette di eseguire operazioni atomiche su campi `int` volatili di oggetti esistenti.

```java
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public class AtomicIntegerFieldUpdaterExample {
    
    // La classe su cui vogliamo operare
    static class Counter {
        // Il campo DEVE essere volatile
        volatile int count;
        
        public Counter() {
            count = 0;
        }
    }
    
    public static void main(String[] args) {
        // Creazione dell'updater per il campo 'count' della classe Counter
        AtomicIntegerFieldUpdater<Counter> updater = 
            AtomicIntegerFieldUpdater.newUpdater(Counter.class, "count");
        
        Counter counter = new Counter();
        
        // Operazioni atomiche sul campo
        updater.set(counter, 5);
        System.out.println("Valore iniziale: " + counter.count);
        
        int oldValue = updater.getAndIncrement(counter);
        System.out.println("Valore precedente: " + oldValue);
        System.out.println("Valore corrente: " + counter.count);
        
        boolean success = updater.compareAndSet(counter, 6, 10);
        System.out.println("CAS riuscito? " + success);
        System.out.println("Valore finale: " + counter.count);
    }
}
```

## AtomicLongFieldUpdater

La classe `AtomicLongFieldUpdater` funziona in modo simile, ma per campi `long` volatili:

```java
import java.util.concurrent.atomic.AtomicLongFieldUpdater;

public class AtomicLongFieldUpdaterExample {
    
    static class Statistics {
        // Il campo DEVE essere volatile
        volatile long totalOperations;
        
        public Statistics() {
            totalOperations = 0;
        }
    }
    
    public static void main(String[] args) {
        // Creazione dell'updater per il campo 'totalOperations' della classe Statistics
        AtomicLongFieldUpdater<Statistics> updater = 
            AtomicLongFieldUpdater.newUpdater(Statistics.class, "totalOperations");
        
        Statistics stats = new Statistics();
        
        // Operazioni atomiche sul campo
        updater.set(stats, 1000);
        System.out.println("Valore iniziale: " + stats.totalOperations);
        
        updater.addAndGet(stats, 500);
        System.out.println("Dopo addAndGet: " + stats.totalOperations);
        
        long oldValue = updater.getAndSet(stats, 2000);
        System.out.println("Valore precedente: " + oldValue);
        System.out.println("Valore corrente: " + stats.totalOperations);
    }
}
```

## AtomicReferenceFieldUpdater

La classe `AtomicReferenceFieldUpdater` permette di eseguire operazioni atomiche su campi di tipo riferimento volatili:

```java
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public class AtomicReferenceFieldUpdaterExample {
    
    static class Node {
        // Il campo DEVE essere volatile
        volatile String data;
        
        public Node(String data) {
            this.data = data;
        }
    }
    
    public static void main(String[] args) {
        // Creazione dell'updater per il campo 'data' della classe Node
        AtomicReferenceFieldUpdater<Node, String> updater = 
            AtomicReferenceFieldUpdater.newUpdater(Node.class, String.class, "data");
        
        Node node = new Node("initial");
        
        // Operazioni atomiche sul campo
        System.out.println("Valore iniziale: " + node.data);
        
        String oldValue = updater.getAndSet(node, "updated");
        System.out.println("Valore precedente: " + oldValue);
        System.out.println("Valore corrente: " + node.data);
        
        boolean success = updater.compareAndSet(node, "updated", "final");
        System.out.println("CAS riuscito? " + success);
        System.out.println("Valore finale: " + node.data);
    }
}
```

## Requisiti e Limitazioni

L'utilizzo degli Atomic FieldUpdater è soggetto a diverse limitazioni:

1. **Il campo deve essere volatile**: Il campo su cui si vuole operare deve essere dichiarato come `volatile`.

2. **Visibilità del campo**: Il campo deve essere accessibile dalla classe che crea l'updater. Questo significa che il campo deve essere pubblico o, se è privato o protetto, l'updater deve essere creato in una classe che ha accesso a quel campo.

3. **Nessuna ereditarietà**: L'updater non può accedere ai campi ereditati da una superclasse.

4. **Tipo corretto**: Il tipo del campo deve corrispondere esattamente al tipo dell'updater (int per AtomicIntegerFieldUpdater, long per AtomicLongFieldUpdater, e il tipo di riferimento specificato per AtomicReferenceFieldUpdater).

5. **Nome del campo come stringa**: Il nome del campo viene specificato come una stringa, quindi non c'è verifica in fase di compilazione. Se il nome è errato, verrà generata un'eccezione a runtime.

## Caso d'Uso: Cache Concorrente

Un caso d'uso comune per gli Atomic FieldUpdater è l'implementazione di una cache concorrente con invalidazione lazy:

```java
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public class ConcurrentCache<K, V> {
    
    static class CacheEntry<V> {
        final long expirationTime;
        volatile V value;
        volatile boolean valid;
        
        public CacheEntry(V value, long ttlMillis) {
            this.value = value;
            this.expirationTime = System.currentTimeMillis() + ttlMillis;
            this.valid = true;
        }
        
        public boolean isExpired() {
            return System.currentTimeMillis() > expirationTime;
        }
    }
    
    private static final AtomicReferenceFieldUpdater<CacheEntry, Object> VALUE_UPDATER =
        AtomicReferenceFieldUpdater.newUpdater(
            CacheEntry.class, Object.class, "value");
    
    private final ConcurrentHashMap<K, CacheEntry<V>> cache = new ConcurrentHashMap<>();
    private final long defaultTtlMillis;
    
    public ConcurrentCache(long defaultTtlMillis) {
        this.defaultTtlMillis = defaultTtlMillis;
    }
    
    public void put(K key, V value) {
        put(key, value, defaultTtlMillis);
    }
    
    public void put(K key, V value, long ttlMillis) {
        cache.put(key, new CacheEntry<>(value, ttlMillis));
    }
    
    @SuppressWarnings("unchecked")
    public V get(K key) {
        CacheEntry<V> entry = cache.get(key);
        
        if (entry == null) {
            return null;
        }
        
        if (entry.isExpired()) {
            // Invalidazione lazy
            cache.remove(key, entry);
            return null;
        }
        
        return entry.value;
    }
    
    @SuppressWarnings("unchecked")
    public boolean replace(K key, V oldValue, V newValue) {
        CacheEntry<V> entry = cache.get(key);
        
        if (entry == null || entry.isExpired()) {
            return false;
        }
        
        // Utilizzo dell'updater per aggiornare atomicamente il valore
        return VALUE_UPDATER.compareAndSet(entry, oldValue, newValue);
    }
    
    public int size() {
        return cache.size();
    }
    
    public void clear() {
        cache.clear();
    }
}
```

## Vantaggi e Svantaggi

### Vantaggi

1. **Efficienza di memoria**: Non è necessario creare oggetti Atomic per ogni istanza della classe.

2. **Flessibilità**: Permette di aggiungere operazioni atomiche a classi esistenti senza modificarle.

3. **Prestazioni**: In alcuni scenari, può offrire prestazioni migliori rispetto all'utilizzo diretto delle classi Atomic.

### Svantaggi

1. **Complessità**: L'utilizzo degli Atomic FieldUpdater è più complesso rispetto alle classi Atomic standard.

2. **Limitazioni**: I requisiti e le limitazioni possono rendere difficile l'utilizzo in alcuni contesti.

3. **Sicurezza dei tipi**: L'utilizzo di stringhe per i nomi dei campi non garantisce la sicurezza dei tipi in fase di compilazione.

4. **Manutenibilità**: Il codice che utilizza gli Atomic FieldUpdater può essere più difficile da mantenere e comprendere.

## Conclusioni

Gli Atomic FieldUpdater sono strumenti potenti per eseguire operazioni atomiche su campi di oggetti esistenti senza dover modificare la loro struttura. Sono particolarmente utili in scenari di ottimizzazione delle prestazioni e della memoria, ma richiedono una comprensione approfondita delle loro limitazioni e requisiti.

In generale, è consigliabile utilizzare le classi Atomic standard (AtomicInteger, AtomicLong, AtomicReference, ecc.) quando possibile, e ricorrere agli Atomic FieldUpdater solo quando necessario per motivi di prestazioni o compatibilità con codice esistente.

## Navigazione

- [Indice del Corso](../README.md)
- Precedente: [Classi Atomic Avanzate](./04-ClassiAtomicAvanzate.md)
- Successivo: [Pattern Concorrenti](../08-PatternConcorrenti/README.md)