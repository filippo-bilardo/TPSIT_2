# StampedLock
## Introduzione a StampedLock

Introdotto in Java 8, `StampedLock` è un tipo di lock che offre tre modalità di accesso: lettura, scrittura e lettura ottimistica. A differenza di `ReentrantLock` e `ReadWriteLock`, non è rientrante e non è basato direttamente sull'interfaccia `Lock`.

`StampedLock` è progettato per offrire prestazioni migliori in scenari con alta contesa, specialmente quando le operazioni di lettura sono molto più frequenti delle operazioni di scrittura.

## Modalità di Accesso

`StampedLock` supporta tre modalità di accesso:

1. **Modalità Scrittura (Write)**: Esclusiva, simile a un lock di scrittura tradizionale. Nessun altro thread può acquisire un lock di lettura o scrittura.

2. **Modalità Lettura (Read)**: Condivisa, simile a un lock di lettura tradizionale. Più thread possono acquisire lock di lettura contemporaneamente, ma nessun thread può acquisire un lock di scrittura.

3. **Modalità Lettura Ottimistica (Optimistic Read)**: Non è un vero lock, ma un meccanismo che permette di leggere dati senza bloccare, verificando successivamente se una scrittura è avvenuta durante la lettura.

Ogni acquisizione di lock restituisce un "stamp" (un valore long) che rappresenta lo stato del lock e deve essere utilizzato per rilasciare o convertire il lock.

## Metodi Principali

```java
public class StampedLock implements Serializable {
    // Acquisizione lock
    public long writeLock();
    public long readLock();
    public long tryWriteLock();
    public long tryReadLock();
    public long tryOptimisticRead();
    
    // Rilascio lock
    public void unlockWrite(long stamp);
    public void unlockRead(long stamp);
    public void unlock(long stamp);
    
    // Validazione e conversione
    public boolean validate(long stamp);
    public long tryConvertToWriteLock(long stamp);
    public long tryConvertToReadLock(long stamp);
    public long tryConvertToOptimisticRead(long stamp);
}
```

### Acquisizione e Rilascio dei Lock

#### Lock di Scrittura

```java
long stamp = lock.writeLock();  // Acquisizione bloccante
try {
    // Operazioni di scrittura
} finally {
    lock.unlockWrite(stamp);  // o semplicemente lock.unlock(stamp)
}
```

#### Lock di Lettura

```java
long stamp = lock.readLock();  // Acquisizione bloccante
try {
    // Operazioni di lettura
} finally {
    lock.unlockRead(stamp);  // o semplicemente lock.unlock(stamp)
}
```

#### Lettura Ottimistica

```java
long stamp = lock.tryOptimisticRead();  // Non bloccante
// Lettura dei dati
if (!lock.validate(stamp)) {  // Verifica se i dati sono stati modificati
    // Fallback: acquisizione di un lock di lettura tradizionale
    stamp = lock.readLock();
    try {
        // Rilettura dei dati
    } finally {
        lock.unlockRead(stamp);
    }
}
```

## Conversione tra Modalità

Una caratteristica potente di `StampedLock` è la possibilità di convertire un lock da una modalità all'altra:

```java
// Tentativo di conversione da lettura ottimistica a lock di lettura
long stamp = lock.tryOptimisticRead();
// Lettura dei dati
if (!lock.validate(stamp)) {
    stamp = lock.tryConvertToReadLock(stamp);
    if (stamp == 0L) {  // Conversione fallita
        stamp = lock.readLock();  // Acquisizione bloccante
    }
    // Rilettura dei dati
    lock.unlock(stamp);
}

// Tentativo di conversione da lock di lettura a lock di scrittura
long stamp = lock.readLock();
try {
    // Lettura dei dati
    if (necessitaModifica) {
        long writeStamp = lock.tryConvertToWriteLock(stamp);
        if (writeStamp != 0L) {  // Conversione riuscita
            stamp = writeStamp;
            // Operazioni di scrittura
        } else {  // Conversione fallita
            lock.unlockRead(stamp);
            stamp = lock.writeLock();  // Acquisizione bloccante
            // Operazioni di scrittura
        }
    }
} finally {
    lock.unlock(stamp);
}
```

## Lettura Ottimistica

La modalità di lettura ottimistica è una delle caratteristiche più innovative di `StampedLock`. Permette di leggere dati senza acquisire un lock, verificando successivamente se i dati sono stati modificati durante la lettura.

Questo approccio è particolarmente efficace in scenari dove:

1. Le operazioni di lettura sono molto più frequenti delle operazioni di scrittura
2. Le letture sono rapide e non richiedono operazioni complesse
3. Il costo di una rilettura occasionale è accettabile

Ecco un esempio completo di lettura ottimistica:

```java
public class Point {
    private final StampedLock lock = new StampedLock();
    private double x, y;
    
    public void move(double deltaX, double deltaY) {
        long stamp = lock.writeLock();
        try {
            x += deltaX;
            y += deltaY;
        } finally {
            lock.unlock(stamp);
        }
    }
    
    public double distanceFromOrigin() {
        long stamp = lock.tryOptimisticRead();  // Lettura ottimistica
        double currentX = x;
        double currentY = y;
        
        if (!lock.validate(stamp)) {  // Verifica se i dati sono stati modificati
            stamp = lock.readLock();  // Fallback: acquisizione di un lock di lettura
            try {
                currentX = x;
                currentY = y;
            } finally {
                lock.unlock(stamp);
            }
        }
        
        return Math.sqrt(currentX * currentX + currentY * currentY);
    }
}
```

## Confronto con ReadWriteLock

| Caratteristica | StampedLock | ReadWriteLock |
|----------------|-------------|---------------|
| Rientranza | No | Sì (ReentrantReadWriteLock) |
| Lettura ottimistica | Sì | No |
| Conversione tra modalità | Sì | No |
| Fairness | No | Configurabile |
| Interrompibilità | No | Sì |
| Condizioni | No | Sì |
| Prestazioni | Migliori in alta contesa | Buone in scenari generici |

## Limitazioni di StampedLock

Nonostante le sue prestazioni superiori, `StampedLock` presenta alcune limitazioni importanti:

1. **Non è rientrante**: Lo stesso thread non può acquisire lo stesso lock più volte.

2. **Non supporta l'interrompibilità**: Non ci sono metodi interrompibili come `lockInterruptibly()`.

3. **Non supporta le condizioni**: Non è possibile creare oggetti `Condition`.

4. **Gestione manuale degli stamp**: È responsabilità del programmatore gestire correttamente gli stamp restituiti dai metodi di acquisizione.

5. **Rischio di deadlock**: In caso di conversione fallita tra modalità, è necessario rilasciare esplicitamente il lock e riacquisirlo nella nuova modalità.

## Casi d'Uso Ottimali

`StampedLock` è particolarmente adatto per:

1. **Strutture dati con letture frequenti**: Mappe, cache, repository dove le letture sono molto più frequenti delle scritture.

2. **Applicazioni ad alte prestazioni**: Dove la riduzione del contention è critica per le prestazioni.

3. **Scenari con letture rapide**: Dove le operazioni di lettura sono veloci e il costo di una rilettura occasionale è accettabile.

## Esempio Pratico: Cache Concorrente

```java
public class ConcurrentCache<K, V> {
    private final StampedLock lock = new StampedLock();
    private final Map<K, V> map = new HashMap<>();
    
    public V get(K key) {
        long stamp = lock.tryOptimisticRead();
        V value = map.get(key);
        
        if (!lock.validate(stamp)) {
            stamp = lock.readLock();
            try {
                value = map.get(key);
            } finally {
                lock.unlockRead(stamp);
            }
        }
        
        return value;
    }
    
    public V put(K key, V value) {
        long stamp = lock.writeLock();
        try {
            return map.put(key, value);
        } finally {
            lock.unlockWrite(stamp);
        }
    }
    
    public V computeIfAbsent(K key, Function<K, V> mappingFunction) {
        // Prima prova una lettura ottimistica
        long stamp = lock.tryOptimisticRead();
        V value = map.get(key);
        
        if (value != null && lock.validate(stamp)) {
            return value;  // Valore trovato, lettura ottimistica valida
        }
        
        // Prova con un lock di lettura
        stamp = lock.readLock();
        try {
            value = map.get(key);
            if (value != null) {
                return value;  // Valore trovato con lock di lettura
            }
        } finally {
            lock.unlock(stamp);
        }
        
        // Necessario calcolare il valore, acquisisce un lock di scrittura
        stamp = lock.writeLock();
        try {
            // Ricontrolla dopo aver acquisito il lock di scrittura
            value = map.get(key);
            if (value == null) {
                value = mappingFunction.apply(key);
                map.put(key, value);
            }
            return value;
        } finally {
            lock.unlock(stamp);
        }
    }
}
```

## Conclusione

`StampedLock` rappresenta un'evoluzione significativa nei meccanismi di sincronizzazione in Java, offrendo prestazioni superiori in scenari con alta contesa, specialmente quando le operazioni di lettura sono predominanti.

La modalità di lettura ottimistica, in particolare, permette di ridurre drasticamente il contention in molti scenari reali, migliorando la scalabilità delle applicazioni concorrenti.

Tuttavia, la maggiore complessità e le limitazioni di `StampedLock` richiedono una comprensione approfondita del suo funzionamento e un'attenta valutazione dei casi d'uso appropriati. In molti scenari più semplici, `ReentrantLock` o `ReentrantReadWriteLock` potrebbero essere scelte più appropriate per la loro maggiore flessibilità e facilità d'uso.