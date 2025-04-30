# ReentrantLock

## Introduzione ai Lock Rientranti

Un lock rientrante è un lock che può essere acquisito più volte dallo stesso thread senza causare deadlock. Questo comportamento è simile a quello dei blocchi `synchronized` in Java, che sono intrinsecamente rientranti.

`ReentrantLock` è l'implementazione più comune dell'interfaccia `Lock` e fornisce lo stesso comportamento di base dei blocchi `synchronized`, ma con funzionalità aggiuntive che offrono maggiore flessibilità e potenza.

## Caratteristiche di ReentrantLock

`ReentrantLock` offre diverse caratteristiche che lo rendono più potente rispetto ai blocchi `synchronized` tradizionali:

1. **Rientranza**: Un thread può acquisire lo stesso lock più volte senza bloccarsi
2. **Fairness**: Opzione per garantire che i thread acquisiscano il lock nell'ordine in cui lo hanno richiesto
3. **Interrompibilità**: Possibilità di interrompere un thread in attesa di un lock
4. **Tentativi non bloccanti**: Metodi per tentare di acquisire un lock senza bloccarsi
5. **Timeout**: Possibilità di specificare un timeout per l'acquisizione di un lock
6. **Condizioni multiple**: Supporto per più condizioni di attesa associate allo stesso lock
7. **Informazioni di monitoraggio**: Metodi per ottenere informazioni sullo stato del lock

## Creazione di un ReentrantLock

`ReentrantLock` può essere creato in due modalità: fair (equa) o unfair (non equa).

```java
// Creazione di un ReentrantLock non equo (default)
ReentrantLock lockUnfair = new ReentrantLock();

// Creazione di un ReentrantLock equo
ReentrantLock lockFair = new ReentrantLock(true);
```

### Modalità Fair vs Unfair

- **Modalità Fair**: I lock vengono acquisiti nell'ordine in cui sono stati richiesti (FIFO). Questo garantisce che nessun thread subisca "starvation" (attesa indefinita), ma può ridurre le prestazioni complessive.

- **Modalità Unfair**: I lock vengono acquisiti in modo opportunistico, senza garantire un ordine specifico. Questo può portare a migliori prestazioni, ma alcuni thread potrebbero dover attendere più a lungo.

La modalità unfair è quella predefinita perché offre prestazioni migliori nella maggior parte dei casi. La modalità fair dovrebbe essere utilizzata solo quando è necessario garantire che tutti i thread abbiano un'opportunità equa di acquisire il lock.

## Utilizzo Base di ReentrantLock

Ecco un esempio di utilizzo base di `ReentrantLock`:

```java
import java.util.concurrent.locks.ReentrantLock;

public class Counter {
    private final ReentrantLock lock = new ReentrantLock();
    private int count = 0;
    
    public void increment() {
        lock.lock();
        try {
            count++;
        } finally {
            lock.unlock();
        }
    }
    
    public int getCount() {
        lock.lock();
        try {
            return count;
        } finally {
            lock.unlock();
        }
    }
}
```

Questo esempio è equivalente a utilizzare metodi `synchronized`, ma offre la base per sfruttare le funzionalità aggiuntive di `ReentrantLock`.

## Rientranza

La rientranza permette a un thread di acquisire lo stesso lock più volte senza bloccarsi. Questo è utile quando un metodo sincronizzato chiama un altro metodo sincronizzato sullo stesso oggetto.

```java
import java.util.concurrent.locks.ReentrantLock;

public class RecursiveTask {
    private final ReentrantLock lock = new ReentrantLock();
    
    public void outerMethod() {
        lock.lock();
        try {
            System.out.println("Outer method acquired the lock");
            System.out.println("Hold count: " + lock.getHoldCount());
            innerMethod();
        } finally {
            lock.unlock();
        }
    }
    
    public void innerMethod() {
        lock.lock();
        try {
            System.out.println("Inner method acquired the lock");
            System.out.println("Hold count: " + lock.getHoldCount());
        } finally {
            lock.unlock();
        }
    }
    
    public static void main(String[] args) {
        RecursiveTask task = new RecursiveTask();
        task.outerMethod();
    }
}
```

In questo esempio, `outerMethod()` acquisisce il lock e poi chiama `innerMethod()`, che acquisisce nuovamente lo stesso lock. Grazie alla rientranza, il thread non si blocca quando tenta di acquisire il lock in `innerMethod()`.

L'output sarà simile a:

```
Outer method acquired the lock
Hold count: 1
Inner method acquired the lock
Hold count: 2
```

Il metodo `getHoldCount()` restituisce il numero di volte che il lock è stato acquisito dal thread corrente.

## Tentativi di Acquisizione Non Bloccanti

Uno dei vantaggi principali di `ReentrantLock` rispetto a `synchronized` è la possibilità di tentare di acquisire un lock senza bloccarsi.

```java
import java.util.concurrent.locks.ReentrantLock;

public class NonBlockingLockExample {
    private final ReentrantLock lock = new ReentrantLock();
    
    public void criticalSection() {
        boolean acquired = lock.tryLock();
        if (acquired) {
            try {
                // Sezione critica
                System.out.println("Lock acquisito, esecuzione della sezione critica");
            } finally {
                lock.unlock();
            }
        } else {
            // Fare qualcos'altro se il lock non è disponibile
            System.out.println("Lock non disponibile, esecuzione del codice alternativo");
        }
    }
    
    public static void main(String[] args) {
        NonBlockingLockExample example = new NonBlockingLockExample();
        
        // Crea due thread che tentano di acquisire il lock
        Thread t1 = new Thread(() -> {
            example.criticalSection();
        });
        
        Thread t2 = new Thread(() -> {
            example.criticalSection();
        });
        
        t1.start();
        t2.start();
    }
}
```

In questo esempio, `tryLock()` tenta di acquisire il lock e restituisce immediatamente `true` se il lock è stato acquisito o `false` se il lock non è disponibile.

## Acquisizione con Timeout

`ReentrantLock` permette anche di specificare un timeout per l'acquisizione di un lock, evitando che un thread rimanga bloccato indefinitamente.

```java
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class TimeoutLockExample {
    private final ReentrantLock lock = new ReentrantLock();
    
    public void criticalSectionWithTimeout() {
        try {
            boolean acquired = lock.tryLock(2, TimeUnit.SECONDS);
            if (acquired) {
                try {
                    // Sezione critica
                    System.out.println("Lock acquisito, esecuzione della sezione critica");
                    Thread.sleep(1000); // Simulazione di lavoro
                } finally {
                    lock.unlock();
                }
            } else {
                // Timeout scaduto
                System.out.println("Timeout scaduto, impossibile acquisire il lock");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Thread interrotto durante l'attesa del lock");
        }
    }
    
    public static void main(String[] args) {
        TimeoutLockExample example = new TimeoutLockExample();
        
        // Thread 1 acquisisce il lock e lo mantiene per 5 secondi
        Thread t1 = new Thread(() -> {
            example.lock.lock();
            try {
                System.out.println("Thread 1 ha acquisito il lock");
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                example.lock.unlock();
            }
        });
        
        // Thread 2 tenta di acquisire il lock con un timeout di 2 secondi
        Thread t2 = new Thread(() -> {
            try {
                Thread.sleep(1000); // Attendi che Thread 1 acquisisca il lock
                System.out.println("Thread 2 tenta di acquisire il lock con timeout");
                example.criticalSectionWithTimeout();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        t1.start();
        t2.start();
    }
}
```

In questo esempio, `Thread 2` tenta di acquisire il lock con un timeout di 2 secondi, ma poiché `Thread 1` mantiene il lock per 5 secondi, il timeout scadrà e `Thread 2` eseguirà il codice alternativo.

## Interrompibilità

`ReentrantLock` permette di interrompere un thread che è in attesa di acquisire un lock, a differenza dei blocchi `synchronized` dove un thread in attesa non può essere interrotto.

```java
import java.util.concurrent.locks.ReentrantLock;

public class InterruptibleLockExample {
    private final ReentrantLock lock = new ReentrantLock();
    
    public void criticalSectionInterruptibly() {
        try {
            lock.lockInterruptibly();
            try {
                // Sezione critica
                System.out.println("Lock acquisito, esecuzione della sezione critica");
                Thread.sleep(5000); // Simulazione di lavoro lungo
            } finally {
                lock.unlock();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Thread interrotto durante l'attesa del lock");
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        InterruptibleLockExample example = new InterruptibleLockExample();
        
        // Thread 1 acquisisce il lock e lo mantiene per 10 secondi
        Thread t1 = new Thread(() -> {
            example.lock.lock();
            try {
                System.out.println("Thread 1 ha acquisito il lock");
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                example.lock.unlock();
            }
        });
        
        // Thread 2 tenta di acquisire il lock in modo interrompibile
        Thread t2 = new Thread(() -> {
            try {
                Thread.sleep(1000); // Attendi che Thread 1 acquisisca il lock
                System.out.println("Thread 2 tenta di acquisire il lock in modo interrompibile");
                example.criticalSectionInterruptibly();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        t1.start();
        t2.start();
        
        // Attendi un po' e poi interrompi Thread 2
        Thread.sleep(3000);
        t2.interrupt();
    }
}
```

In questo esempio, `Thread 2` tenta di acquisire il lock in modo interrompibile con `lockInterruptibly()`. Poiché `Thread 1` mantiene il lock, `Thread 2` si blocca. Dopo 3 secondi, `Thread 2` viene interrotto e può gestire l'interruzione invece di rimanere bloccato indefinitamente.

## Metodi di Diagnostica

`ReentrantLock` offre diversi metodi per ottenere informazioni sullo stato del lock, utili per il debug e il monitoraggio:

```java
import java.util.concurrent.locks.ReentrantLock;

public class LockDiagnosticsExample {
    private final ReentrantLock lock = new ReentrantLock();
    
    public void showDiagnostics() {
        System.out.println("Lock è detenuto da un thread? " + lock.isLocked());
        System.out.println("Lock è detenuto dal thread corrente? " + lock.isHeldByCurrentThread());
        System.out.println("Numero di thread in attesa: " + lock.getQueueLength());
        System.out.println("Numero di volte che il lock è stato acquisito dal thread corrente: " + lock.getHoldCount());
    }
    
    public void acquireLock() {
        lock.lock();
        try {
            System.out.println("Lock acquisito");
            showDiagnostics();
        } finally {
            lock.unlock();
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        LockDiagnosticsExample example = new LockDiagnosticsExample();
        
        // Mostra diagnostica prima di acquisire il lock
        System.out.println("Prima dell'acquisizione:");
        example.showDiagnostics();
        
        // Thread 1 acquisisce il lock
        Thread t1 = new Thread(() -> {
            example.lock.lock();
            try {
                System.out.println("\nThread 1 ha acquisito il lock");
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                example.lock.unlock();
            }
        });
        
        // Thread 2 e 3 tentano di acquisire il lock mentre è detenuto da Thread 1
        Thread t2 = new Thread(() -> {
            try {
                Thread.sleep(1000);
                System.out.println("\nThread 2 tenta di acquisire il lock");
                example.acquireLock();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        Thread t3 = new Thread(() -> {
            try {
                Thread.sleep(1500);
                System.out.println("\nThread 3 tenta di acquisire il lock");
                example.acquireLock();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        t1.start();
        t2.start();
        t3.start();
        
        // Attendi un po' e poi mostra la diagnostica dal thread principale
        Thread.sleep(2000);
        System.out.println("\nDiagnostica dal thread principale mentre altri thread sono in attesa:");
        example.showDiagnostics();
    }
}
```

Questo esempio mostra l'uso dei seguenti metodi di diagnostica:

- `isLocked()`: Verifica se il lock è attualmente detenuto da qualche thread
- `isHeldByCurrentThread()`: Verifica se il lock è detenuto dal thread corrente
- `getQueueLength()`: Restituisce una stima del numero di thread in attesa di acquisire il lock
- `getHoldCount()`: Restituisce il numero di volte che il lock è stato acquisito dal thread corrente

## Pattern di Utilizzo Sicuro

È fondamentale seguire un pattern di utilizzo sicuro per evitare deadlock e garantire che il lock venga sempre rilasciato, anche in caso di eccezioni.

```java
import java.util.concurrent.locks.ReentrantLock;

public class SafeLockUsagePattern {
    private final ReentrantLock lock = new ReentrantLock();
    
    // Pattern corretto: acquisizione e rilascio in un blocco try-finally
    public void correctUsage() {
        lock.lock();
        try {
            // Sezione critica
            System.out.println("Esecuzione della sezione critica");
            // Anche se si verifica un'eccezione qui, il lock verrà rilasciato
            if (Math.random() < 0.5) {
                throw new RuntimeException("Eccezione simulata");
            }
        } finally {
            lock.unlock();
        }
    }
    
    // Pattern errato: manca il blocco try-finally
    public void incorrectUsage() {
        lock.lock();
        // Sezione critica
        System.out.println("Esecuzione della sezione critica");
        // Se si verifica un'eccezione qui, il lock non verrà rilasciato!
        if (Math.random() < 0.5) {
            throw new RuntimeException("Eccezione simulata");
        }
        lock.unlock();
    }
    
    // Pattern errato: rilascio condizionale del lock
    public void anotherIncorrectUsage(boolean condition) {
        lock.lock();
        try {
            // Sezione critica
            System.out.println("Esecuzione della sezione critica");
            if (condition) {
                return; // Il lock verrà rilasciato grazie al blocco finally
            }
            // Altro codice...
        } finally {
            if (condition) {
                // ERRORE: rilascio condizionale del lock
                // Se condition è false, il lock non verrà rilasciato!
                lock.unlock();
            }
        }
    }
    
    public static void main(String[] args) {
        SafeLockUsagePattern example = new SafeLockUsagePattern();
        
        try {
            System.out.println("Tentativo di utilizzo corretto:");
            example.correctUsage();
        } catch (Exception e) {
            System.out.println("Eccezione catturata: " + e.getMessage());
            System.out.println("Il lock è stato rilasciato? " + !example.lock.isLocked());
        }
        
        try {
            System.out.println("\nTentativo di utilizzo errato:");
            example.incorrectUsage();
        } catch (Exception e) {
            System.out.println("Eccezione catturata: " + e.getMessage());
            System.out.println("Il lock è stato rilasciato? " + !example.lock.isLocked());
            // Forza il rilascio del lock per continuare l'esempio
            if (example.lock.isHeldByCurrentThread()) {
                example.lock.unlock();
            }
        }
    }
}
```

Questo esempio illustra l'importanza di utilizzare un blocco `try-finally` per garantire che il lock venga sempre rilasciato, anche in caso di eccezioni o return anticipati.

## Confronto con synchronized

| Caratteristica | synchronized | ReentrantLock |
|----------------|--------------|---------------|
| Sintassi | Più semplice | Più verbosa |
| Rilascio automatico | Sì | No (richiede unlock esplicito) |
| Tentativo non bloccante | No | Sì (tryLock) |
| Timeout | No | Sì (tryLock con timeout) |
| Interrompibilità | No | Sì (lockInterruptibly) |
| Fairness | No | Sì (con costruttore) |
| Condizioni multiple | No | Sì (newCondition) |
| Informazioni di diagnostica | No | Sì (metodi di ispezione) |
| Prestazioni | Migliorato in Java 6+ | Simile a synchronized in Java 6+ |

## Quando Usare ReentrantLock

`ReentrantLock` dovrebbe essere preferito a `synchronized` nei seguenti casi:

1. Quando è necessario un tentativo non bloccante di acquisire un lock
2. Quando è necessario specificare un timeout per l'acquisizione di un lock
3. Quando è necessario interrompere un thread in attesa di un lock
4. Quando è necessario garantire fairness nell'acquisizione dei lock
5. Quando sono necessarie condizioni multiple associate allo stesso lock
6. Quando sono necessarie informazioni di diagnostica sullo stato del lock

In tutti gli altri casi, è preferibile utilizzare `synchronized` per la sua sintassi più semplice e il rilascio automatico del lock.

## Conclusione

`ReentrantLock` è un'implementazione potente e flessibile dell'interfaccia `Lock` che offre numerose funzionalità aggiuntive rispetto ai blocchi `synchronized` tradizionali. Queste funzionalità aggiuntive lo rendono particolarmente utile in scenari complessi dove è necessario un controllo fine sulla sincronizzazione.

Tuttavia, questa potenza aggiuntiva comporta anche una maggiore responsabilità: è fondamentale seguire le best practice per l'acquisizione e il rilascio dei lock per evitare problemi come deadlock, livelock o lock non rilasciati.

Nel prossimo capitolo, esploreremo `ReadWriteLock`, un'interfaccia che permette di separare i lock per le operazioni di lettura e scrittura, migliorando ulteriormente la concorrenza in scenari dove le letture sono molto più frequenti delle scritture.

## Navigazione

- [Indice del Modulo](./README.md)
- Precedente: [Interfaccia Lock](./01-InterfacciaLock.md)
- Successivo: [ReadWriteLock](./03-ReadWriteLock.md)