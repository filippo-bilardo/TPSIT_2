# Lock e Oggetti di Sincronizzazione

## Introduzione

Oltre alla keyword `synchronized`, Java offre meccanismi più avanzati per la gestione della concorrenza attraverso l'uso di lock espliciti e oggetti di sincronizzazione. Questi strumenti forniscono un controllo più granulare sulla sincronizzazione e offrono funzionalità aggiuntive rispetto al semplice `synchronized`.

## Oggetti come Lock

In Java, ogni oggetto può fungere da lock (o monitor). Quando utilizziamo la keyword `synchronized` su un blocco di codice, stiamo implicitamente acquisendo il lock dell'oggetto specificato:

```java
Object lockObject = new Object();

public void metodoSicuro() {
    synchronized(lockObject) {
        // Codice protetto dal lock
    }
}
```

Questo approccio permette di utilizzare oggetti dedicati come lock, migliorando la leggibilità del codice e rendendo esplicito l'intento di sincronizzazione.

## Granularità del Lock

La granularità del lock si riferisce alla quantità di dati o risorse protette da un singolo lock. Possiamo distinguere tra:

### Lock a Grana Grossa (Coarse-grained Locking)

Un lock protegge una grande quantità di dati o un'intera struttura dati:

```java
public class ListaSincronizzata {
    private List<String> lista = new ArrayList<>();
    private final Object lock = new Object();
    
    public void aggiungi(String elemento) {
        synchronized(lock) {
            lista.add(elemento);
        }
    }
    
    public void rimuovi(String elemento) {
        synchronized(lock) {
            lista.remove(elemento);
        }
    }
    
    public boolean contiene(String elemento) {
        synchronized(lock) {
            return lista.contains(elemento);
        }
    }
}
```

Vantaggi:
- Semplice da implementare
- Meno rischio di deadlock

Svantaggi:
- Minore concorrenza (un thread alla volta può accedere alla risorsa)
- Potenziale collo di bottiglia nelle prestazioni

### Lock a Grana Fine (Fine-grained Locking)

Utilizzo di lock multipli per proteggere parti diverse di una struttura dati:

```java
public class MappaAvanzata<K, V> {
    private final Map<K, V> mappa = new HashMap<>();
    private final Object[] locks;
    private final int NUM_LOCKS = 16;
    
    public MappaAvanzata() {
        locks = new Object[NUM_LOCKS];
        for (int i = 0; i < NUM_LOCKS; i++) {
            locks[i] = new Object();
        }
    }
    
    private Object getLockForKey(K key) {
        return locks[Math.abs(key.hashCode() % NUM_LOCKS)];
    }
    
    public V get(K key) {
        synchronized(getLockForKey(key)) {
            return mappa.get(key);
        }
    }
    
    public void put(K key, V value) {
        synchronized(getLockForKey(key)) {
            mappa.put(key, value);
        }
    }
}
```

Vantaggi:
- Maggiore concorrenza (più thread possono accedere a parti diverse della struttura)
- Migliori prestazioni in scenari ad alto carico

Svantaggi:
- Più complesso da implementare
- Maggiore rischio di deadlock
- Overhead di memoria per i lock aggiuntivi

## Lock Rientranti

I lock in Java sono rientranti (reentrant), il che significa che un thread può acquisire lo stesso lock più volte senza bloccarsi. Questo è particolarmente utile quando un metodo sincronizzato ne chiama un altro che è anch'esso sincronizzato sullo stesso oggetto:

```java
public class EsempioLockRientrante {
    public synchronized void metodo1() {
        System.out.println("Esecuzione metodo1");
        metodo2(); // Chiama un altro metodo sincronizzato
    }
    
    public synchronized void metodo2() {
        System.out.println("Esecuzione metodo2");
    }
}
```

In questo esempio, quando un thread chiama `metodo1()`, acquisisce il lock dell'istanza. Quando poi `metodo1()` chiama `metodo2()`, il thread può entrare in `metodo2()` senza bloccarsi, poiché ha già acquisito il lock necessario.

## Lock Espliciti: l'interfaccia Lock

Java fornisce l'interfaccia `Lock` nel package `java.util.concurrent.locks` che offre funzionalità più avanzate rispetto al `synchronized`:

```java
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ContoConLock {
    private double saldo;
    private final Lock lock = new ReentrantLock();
    
    public void deposita(double importo) {
        lock.lock();
        try {
            saldo += importo;
        } finally {
            lock.unlock();
        }
    }
    
    public boolean preleva(double importo) {
        lock.lock();
        try {
            if (saldo >= importo) {
                saldo -= importo;
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }
}
```

Vantaggi dei lock espliciti:
- Possibilità di tentare l'acquisizione del lock senza bloccarsi (`tryLock()`)
- Supporto per timeout nell'acquisizione del lock
- Possibilità di interrompere un thread in attesa di un lock
- Implementazioni specializzate (ReadWriteLock, StampedLock, ecc.)

## Deadlock: Cause e Prevenzione

Un **deadlock** si verifica quando due o più thread si bloccano a vicenda, ciascuno in attesa di una risorsa detenuta dall'altro.

### Esempio di Deadlock

```java
public class EsempioDeadlock {
    private final Object risorsa1 = new Object();
    private final Object risorsa2 = new Object();
    
    public void metodo1() {
        synchronized(risorsa1) {
            System.out.println("Thread 1: ha acquisito risorsa1");
            
            try { Thread.sleep(100); } catch (InterruptedException e) {}
            
            synchronized(risorsa2) {
                System.out.println("Thread 1: ha acquisito risorsa2");
            }
        }
    }
    
    public void metodo2() {
        synchronized(risorsa2) {
            System.out.println("Thread 2: ha acquisito risorsa2");
            
            try { Thread.sleep(100); } catch (InterruptedException e) {}
            
            synchronized(risorsa1) {
                System.out.println("Thread 2: ha acquisito risorsa1");
            }
        }
    }
}
```

In questo esempio, se un thread chiama `metodo1()` mentre un altro chiama `metodo2()`, si può verificare un deadlock perché i thread acquisiscono le risorse in ordine diverso.

### Prevenzione del Deadlock

1. **Ordine di acquisizione dei lock**: Assicurarsi che tutti i thread acquisiscano i lock sempre nello stesso ordine.

```java
public void metodoSicuro1() {
    synchronized(risorsa1) {
        synchronized(risorsa2) {
            // Codice che utilizza entrambe le risorse
        }
    }
}

public void metodoSicuro2() {
    synchronized(risorsa1) { // Stesso ordine di acquisizione
        synchronized(risorsa2) {
            // Codice che utilizza entrambe le risorse
        }
    }
}
```

2. **Timeout nell'acquisizione dei lock**: Utilizzare `tryLock()` con timeout per evitare attese indefinite.

```java
public void metodoConTimeout() {
    boolean risorsa1Acquisita = false;
    boolean risorsa2Acquisita = false;
    
    try {
        risorsa1Acquisita = lock1.tryLock(1, TimeUnit.SECONDS);
        if (risorsa1Acquisita) {
            risorsa2Acquisita = lock2.tryLock(1, TimeUnit.SECONDS);
            if (risorsa2Acquisita) {
                // Codice che utilizza entrambe le risorse
            }
        }
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    } finally {
        if (risorsa2Acquisita) {
            lock2.unlock();
        }
        if (risorsa1Acquisita) {
            lock1.unlock();
        }
    }
}
```

3. **Rilevamento del deadlock**: Utilizzare strumenti come JConsole o VisualVM per rilevare deadlock in fase di sviluppo.

4. **Evitare lock annidati**: Quando possibile, evitare di acquisire più lock contemporaneamente.

## Conclusione

La gestione corretta dei lock e degli oggetti di sincronizzazione è fondamentale per creare applicazioni multi-thread robuste. Scegliere la granularità appropriata dei lock e adottare pratiche che prevengono i deadlock sono competenze essenziali per ogni sviluppatore Java che lavora con la programmazione concorrente.

Nel prossimo modulo, esploreremo i meccanismi di comunicazione tra thread, che permettono ai thread di coordinarsi e scambiarsi informazioni in modo efficiente.