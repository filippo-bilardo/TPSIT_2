# ReadWriteLock

## Concorrenza tra Lettori e Scrittori

In molte applicazioni, le operazioni di lettura sono molto più frequenti delle operazioni di scrittura. In questi scenari, utilizzare un lock esclusivo per tutte le operazioni (sia lettura che scrittura) può limitare inutilmente la concorrenza.

L'interfaccia `ReadWriteLock` risolve questo problema fornendo due tipi di lock:

1. **Lock di Lettura (Read Lock)**: Può essere acquisito simultaneamente da più thread di lettura
2. **Lock di Scrittura (Write Lock)**: Esclusivo, blocca sia i lettori che gli scrittori

Questo approccio aumenta significativamente la concorrenza in applicazioni con molte letture e poche scritture.

## L'Interfaccia ReadWriteLock

L'interfaccia `ReadWriteLock` è molto semplice:

```java
public interface ReadWriteLock {
    Lock readLock();
    Lock writeLock();
}
```

I metodi restituiscono due lock distinti ma correlati:

- `readLock()`: Restituisce il lock per le operazioni di lettura
- `writeLock()`: Restituisce il lock per le operazioni di scrittura

## ReentrantReadWriteLock

La classe `ReentrantReadWriteLock` è l'implementazione standard dell'interfaccia `ReadWriteLock`. Come suggerisce il nome, supporta la rientranza sia per i lock di lettura che per quelli di scrittura.

```java
public class ReentrantReadWriteLock implements ReadWriteLock {
    public ReentrantReadWriteLock() { ... }
    public ReentrantReadWriteLock(boolean fair) { ... }
    
    public ReentrantReadWriteLock.ReadLock readLock() { ... }
    public ReentrantReadWriteLock.WriteLock writeLock() { ... }
    
    // Altri metodi...
}
```

Caratteristiche principali:

1. **Fairness**: Il costruttore accetta un parametro booleano che determina se il lock deve essere equo (fair)
2. **Rientranza**: Un thread può acquisire più volte lo stesso lock (sia di lettura che di scrittura)
3. **Downgrading**: Un thread che possiede il lock di scrittura può acquisire il lock di lettura senza rilasciare il lock di scrittura

### Regole di Acquisizione

Le regole che governano l'acquisizione dei lock sono:

1. Un thread può acquisire più volte il lock di lettura
2. Un thread può acquisire più volte il lock di scrittura
3. Un thread che possiede il lock di scrittura può acquisire il lock di lettura
4. Un thread che possiede solo il lock di lettura non può acquisire il lock di scrittura

## Downgrading e Upgrading

### Downgrading (da Write a Read)

Il downgrading è il processo di conversione da un lock di scrittura a un lock di lettura. Questo è supportato direttamente:

```java
ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
Lock writeLock = rwl.writeLock();
Lock readLock = rwl.readLock();

writeLock.lock();  // Acquisisce il lock di scrittura
try {
    // Operazioni di modifica dei dati
    
    readLock.lock();  // Acquisisce anche il lock di lettura (downgrading)
} finally {
    writeLock.unlock();  // Rilascia il lock di scrittura ma mantiene quello di lettura
}

try {
    // Operazioni di lettura
} finally {
    readLock.unlock();  // Rilascia il lock di lettura
}
```

Il downgrading è utile quando un thread modifica una struttura dati e poi deve leggerla, permettendo ad altri thread di lettura di accedere contemporaneamente.

### Upgrading (da Read a Write)

L'upgrading (passare da un lock di lettura a uno di scrittura) non è direttamente supportato e può portare a deadlock. Per eseguire un upgrading sicuro, è necessario rilasciare prima il lock di lettura e poi acquisire quello di scrittura:

```java
ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
Lock readLock = rwl.readLock();
Lock writeLock = rwl.writeLock();

readLock.lock();
try {
    // Operazioni di lettura
} finally {
    readLock.unlock();  // Rilascia il lock di lettura
}

writeLock.lock();  // Acquisisce il lock di scrittura
try {
    // Operazioni di scrittura
} finally {
    writeLock.unlock();
}
```

Attenzione: tra il rilascio del lock di lettura e l'acquisizione del lock di scrittura, altri thread potrebbero modificare i dati.

## Casi d'Uso Ottimali

I `ReadWriteLock` sono particolarmente utili in scenari come:

1. **Cache**: Dove le letture sono molto più frequenti degli aggiornamenti
2. **Dizionari e Mappe**: Consultati frequentemente ma aggiornati raramente
3. **Configurazioni**: Lette da molti thread ma modificate raramente
4. **Strutture dati condivise**: Con pattern di accesso asimmetrici (molte letture, poche scritture)

### Esempio: Cache con ReadWriteLock

```java
public class CacheConReadWriteLock<K, V> {
    private final Map<K, V> cache = new HashMap<>();
    private final ReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Lock readLock = rwl.readLock();
    private final Lock writeLock = rwl.writeLock();
    
    public V get(K key) {
        readLock.lock();
        try {
            return cache.get(key);
        } finally {
            readLock.unlock();
        }
    }
    
    public void put(K key, V value) {
        writeLock.lock();
        try {
            cache.put(key, value);
        } finally {
            writeLock.unlock();
        }
    }
    
    public V computeIfAbsent(K key, Function<K, V> mappingFunction) {
        readLock.lock();
        try {
            V value = cache.get(key);
            if (value != null) {
                return value;
            }
        } finally {
            readLock.unlock();
        }
        
        writeLock.lock();
        try {
            // Doppio controllo in caso un altro thread abbia inserito il valore
            // mentre questo thread rilasciava il readLock e acquisiva il writeLock
            V value = cache.get(key);
            if (value == null) {
                value = mappingFunction.apply(key);
                cache.put(key, value);
            }
            return value;
        } finally {
            writeLock.unlock();
        }
    }
}
```

## Considerazioni sulle Prestazioni

1. **Overhead**: I `ReadWriteLock` hanno un overhead leggermente maggiore rispetto ai lock semplici
2. **Contesa**: In caso di alta contesa, i `ReadWriteLock` possono avere prestazioni inferiori
3. **Fairness**: La modalità fair può ridurre significativamente le prestazioni
4. **Rapporto letture/scritture**: Il vantaggio aumenta con l'aumentare del rapporto letture/scritture

## Conclusione

I `ReadWriteLock` offrono un meccanismo potente per aumentare la concorrenza in applicazioni con pattern di accesso asimmetrici. Permettono a più thread di lettura di operare contemporaneamente, aumentando significativamente il throughput in scenari con molte letture e poche scritture.

Tuttavia, è importante valutare attentamente il pattern di accesso dell'applicazione: se le operazioni di scrittura sono frequenti quanto quelle di lettura, o se il costo delle operazioni è molto basso, un lock semplice potrebbe essere più efficiente.

Nel prossimo capitolo, esploreremo l'interfaccia `Condition`, che fornisce un meccanismo avanzato per la comunicazione tra thread.

## Navigazione

- [Indice del Corso](../README.md)
- [Indice del Modulo](./README.md)
- Precedente: [ReentrantLock](./02-ReentrantLock.md)
- Successivo: [Condition](./04-Condition.md)