# Read-Write Lock

Il pattern Read-Write Lock è un pattern concorrente che ottimizza l'accesso a risorse condivise distinguendo tra operazioni di lettura e operazioni di scrittura. Questo pattern permette a più thread di leggere contemporaneamente una risorsa, ma richiede accesso esclusivo per le operazioni di scrittura.

## Concorrenza tra Operazioni di Lettura e Scrittura

Nella programmazione concorrente, è comune avere situazioni in cui una risorsa condivisa viene letta frequentemente ma modificata raramente. In questi scenari, utilizzare un lock tradizionale (mutex) per tutte le operazioni, sia di lettura che di scrittura, può limitare inutilmente la concorrenza.

Il pattern Read-Write Lock si basa su due osservazioni fondamentali:

1. **Letture Concorrenti**: Le operazioni di sola lettura non modificano lo stato della risorsa e quindi possono essere eseguite contemporaneamente da più thread senza rischi di race condition.

2. **Scritture Esclusive**: Le operazioni di scrittura modificano lo stato della risorsa e quindi richiedono accesso esclusivo per evitare race condition.

### Regole di Accesso

Il pattern Read-Write Lock implementa le seguenti regole di accesso:

1. Più thread possono acquisire contemporaneamente il lock in modalità lettura.
2. Solo un thread alla volta può acquisire il lock in modalità scrittura.
3. Quando un thread detiene il lock in modalità scrittura, nessun altro thread può acquisire il lock (né in modalità lettura né in modalità scrittura).

## Implementazione del Pattern

Un'implementazione base del pattern Read-Write Lock potrebbe utilizzare due contatori e un mutex:

- Un contatore per il numero di lettori attivi.
- Un flag per indicare se c'è uno scrittore attivo.
- Un mutex per proteggere l'accesso ai contatori stessi.

### Esempio di Implementazione Base

```java
public class SimpleReadWriteLock {
    private int readers = 0;
    private boolean writer = false;
    private final Object mutex = new Object();
    
    public void lockRead() throws InterruptedException {
        synchronized (mutex) {
            while (writer) {
                mutex.wait();
            }
            readers++;
        }
    }
    
    public void unlockRead() {
        synchronized (mutex) {
            readers--;
            if (readers == 0) {
                mutex.notifyAll();
            }
        }
    }
    
    public void lockWrite() throws InterruptedException {
        synchronized (mutex) {
            while (writer || readers > 0) {
                mutex.wait();
            }
            writer = true;
        }
    }
    
    public void unlockWrite() {
        synchronized (mutex) {
            writer = false;
            mutex.notifyAll();
        }
    }
}
```

Questa implementazione base ha alcune limitazioni, come la possibilità di starvation degli scrittori se ci sono continuamente nuovi lettori.

## Utilizzo delle Classi Java Esistenti

Java fornisce un'implementazione del pattern Read-Write Lock attraverso l'interfaccia `ReadWriteLock` e la sua implementazione concreta `ReentrantReadWriteLock` nel package `java.util.concurrent.locks`.

```java
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockExample {
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final java.util.concurrent.locks.Lock readLock = lock.readLock();
    private final java.util.concurrent.locks.Lock writeLock = lock.writeLock();
    
    private Object sharedResource;
    
    public Object read() {
        readLock.lock();
        try {
            return sharedResource;
        } finally {
            readLock.unlock();
        }
    }
    
    public void write(Object newValue) {
        writeLock.lock();
        try {
            sharedResource = newValue;
        } finally {
            writeLock.unlock();
        }
    }
}
```

### Caratteristiche di ReentrantReadWriteLock

`ReentrantReadWriteLock` offre diverse caratteristiche avanzate:

1. **Reentrant**: Un thread può acquisire più volte lo stesso lock (sia in modalità lettura che in modalità scrittura).

2. **Downgrading**: Un thread che detiene il lock in modalità scrittura può acquisire anche il lock in modalità lettura e poi rilasciare il lock in modalità scrittura, "declassando" così il lock da scrittura a lettura.

3. **Fairness**: È possibile creare un'istanza con politica di fairness, che garantisce che le richieste di lock vengano soddisfatte nell'ordine in cui sono state fatte.

4. **Condition**: È possibile ottenere oggetti `Condition` dal lock di scrittura per implementare attese condizionali.

## Ottimizzazione per Diversi Scenari di Accesso

L'efficacia del pattern Read-Write Lock dipende dal rapporto tra operazioni di lettura e operazioni di scrittura. È importante ottimizzare l'implementazione in base allo scenario specifico.

### Scenari Favorevoli

1. **Letture Frequenti, Scritture Rare**: Questo è lo scenario ideale per il pattern Read-Write Lock, che permette a molti thread di leggere contemporaneamente.

2. **Operazioni di Lettura Lunghe**: Se le operazioni di lettura richiedono molto tempo, il pattern Read-Write Lock può migliorare significativamente le prestazioni permettendo a più thread di leggere contemporaneamente.

### Scenari Sfavorevoli

1. **Scritture Frequenti**: Se le operazioni di scrittura sono frequenti, il pattern Read-Write Lock potrebbe non offrire vantaggi significativi rispetto a un mutex tradizionale.

2. **Operazioni Brevi**: Se sia le operazioni di lettura che quelle di scrittura sono molto brevi, l'overhead del pattern Read-Write Lock potrebbe superare i benefici.

### Strategie di Ottimizzazione

1. **Preferenza ai Lettori**: In scenari con molte letture e poche scritture, può essere vantaggioso dare priorità ai lettori, permettendo a nuovi lettori di acquisire il lock anche se ci sono scrittori in attesa.

2. **Preferenza agli Scrittori**: In scenari in cui è importante che le operazioni di scrittura non subiscano starvation, può essere vantaggioso dare priorità agli scrittori, bloccando nuovi lettori se ci sono scrittori in attesa.

3. **Timeout**: Implementare timeout per l'acquisizione dei lock può prevenire blocchi indefiniti in caso di contesa elevata.

## Casi d'Uso Pratici

### Cache Concorrenti

Una cache che viene letta frequentemente ma aggiornata raramente è un caso d'uso ideale per il pattern Read-Write Lock:

```java
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ConcurrentCache<K, V> {
    private final Map<K, V> cache = new HashMap<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    
    public V get(K key) {
        lock.readLock().lock();
        try {
            return cache.get(key);
        } finally {
            lock.readLock().unlock();
        }
    }
    
    public void put(K key, V value) {
        lock.writeLock().lock();
        try {
            cache.put(key, value);
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    public V computeIfAbsent(K key, java.util.function.Function<K, V> mappingFunction) {
        lock.readLock().lock();
        try {
            V value = cache.get(key);
            if (value != null) {
                return value;
            }
        } finally {
            lock.readLock().unlock();
        }
        
        lock.writeLock().lock();
        try {
            // Double-check per evitare race condition
            V value = cache.get(key);
            if (value == null) {
                value = mappingFunction.apply(key);
                cache.put(key, value);
            }
            return value;
        } finally {
            lock.writeLock().unlock();
        }
    }
}
```

### Strutture Dati Concorrenti

Il pattern Read-Write Lock è utile per implementare strutture dati concorrenti che vengono lette frequentemente ma modificate raramente:

```java
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ConcurrentList<E> {
    private final List<E> list = new ArrayList<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    
    public void add(E element) {
        lock.writeLock().lock();
        try {
            list.add(element);
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    public E get(int index) {
        lock.readLock().lock();
        try {
            return list.get(index);
        } finally {
            lock.readLock().unlock();
        }
    }
    
    public List<E> getAll() {
        lock.readLock().lock();
        try {
            return Collections.unmodifiableList(new ArrayList<>(list));
        } finally {
            lock.readLock().unlock();
        }
    }
    
    public int size() {
        lock.readLock().lock();
        try {
            return list.size();
        } finally {
            lock.readLock().unlock();
        }
    }
}
```

## Conclusione

Il pattern Read-Write Lock è un pattern potente per ottimizzare l'accesso concorrente a risorse condivise, permettendo a più thread di leggere contemporaneamente ma garantendo accesso esclusivo per le operazioni di scrittura.

Java fornisce un'implementazione robusta di questo pattern attraverso la classe `ReentrantReadWriteLock`, che offre caratteristiche avanzate come reentrant locks, downgrading e fairness.

Questo pattern è particolarmente efficace in scenari con molte operazioni di lettura e poche operazioni di scrittura, come cache, configurazioni condivise e strutture dati che vengono aggiornate raramente.

Nel prossimo capitolo, esploreremo altri pattern concorrenti utili, come Future, Promise, Barrier e altri.

## Navigazione

- [Indice del Corso](../README.md)
- [Indice del Modulo](./README.md)
- Precedente: [Worker Thread](./03-WorkerThread.md)
- Successivo: [Altri Pattern Concorrenti](./05-AltriPattern.md)