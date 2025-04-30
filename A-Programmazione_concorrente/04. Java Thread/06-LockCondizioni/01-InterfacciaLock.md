# Interfaccia Lock

## Limitazioni dei Blocchi Synchronized

I blocchi e i metodi `synchronized` in Java forniscono un meccanismo di base per la sincronizzazione, ma presentano diverse limitazioni:

1. **Mancanza di Flessibilità**: Un thread non può tentare di acquisire un lock e fare altro se il lock non è disponibile
2. **Nessun Timeout**: Non è possibile specificare un timeout per l'acquisizione di un lock
3. **Acquisizione Bloccante**: L'acquisizione di un lock è sempre un'operazione bloccante
4. **Rilascio Obbligatorio**: Un lock deve essere rilasciato nello stesso blocco di codice in cui è stato acquisito
5. **Nessuna Informazione**: Non è possibile ottenere informazioni sullo stato del lock

Per superare queste limitazioni, Java 5 ha introdotto l'interfaccia `Lock` e le sue implementazioni nel package `java.util.concurrent.locks`.

## L'Interfaccia Lock

L'interfaccia `Lock` definisce i metodi per acquisire e rilasciare lock in modo più flessibile rispetto ai blocchi `synchronized`:

```java
public interface Lock {
    void lock();
    void lockInterruptibly() throws InterruptedException;
    boolean tryLock();
    boolean tryLock(long time, TimeUnit unit) throws InterruptedException;
    void unlock();
    Condition newCondition();
}
```

Esaminiamo in dettaglio ciascun metodo:

### lock()

Acquisisce il lock. Se il lock non è disponibile, il thread corrente viene bloccato fino a quando il lock non diventa disponibile.

```java
Lock lock = new ReentrantLock();
lock.lock();
try {
    // Sezione critica
} finally {
    lock.unlock(); // Importante: rilasciare sempre il lock in un blocco finally
}
```

### lockInterruptibly()

Acquisisce il lock a meno che il thread non sia interrotto. Se il lock non è disponibile, il thread corrente viene bloccato fino a quando non si verifica una delle seguenti condizioni:
- Il lock diventa disponibile e il thread lo acquisisce
- Un altro thread interrompe il thread corrente

```java
Lock lock = new ReentrantLock();
try {
    lock.lockInterruptibly();
    try {
        // Sezione critica
    } finally {
        lock.unlock();
    }
} catch (InterruptedException e) {
    // Gestione dell'interruzione
    Thread.currentThread().interrupt();
}
```

Questo metodo è utile quando si vuole permettere a un thread di rispondere a un'interruzione mentre è in attesa di un lock.

### tryLock()

Tenta di acquisire il lock senza bloccare. Restituisce immediatamente con un valore booleano che indica se l'acquisizione ha avuto successo.

```java
Lock lock = new ReentrantLock();
if (lock.tryLock()) {
    try {
        // Sezione critica
    } finally {
        lock.unlock();
    }
} else {
    // Fare qualcos'altro se il lock non è disponibile
}
```

Questo metodo è utile per implementare algoritmi non bloccanti o per evitare deadlock in situazioni in cui un thread ha bisogno di più lock.

### tryLock(long time, TimeUnit unit)

Tenta di acquisire il lock entro il timeout specificato. Restituisce un valore booleano che indica se l'acquisizione ha avuto successo.

```java
Lock lock = new ReentrantLock();
try {
    if (lock.tryLock(1, TimeUnit.SECONDS)) {
        try {
            // Sezione critica
        } finally {
            lock.unlock();
        }
    } else {
        // Timeout scaduto
    }
} catch (InterruptedException e) {
    // Gestione dell'interruzione
    Thread.currentThread().interrupt();
}
```

Questo metodo è utile quando si vuole limitare il tempo di attesa per un lock.

### unlock()

Rilascia il lock. A differenza dei blocchi `synchronized`, il rilascio del lock deve essere gestito esplicitamente.

```java
lock.unlock();
```

È fondamentale rilasciare sempre il lock in un blocco `finally` per garantire che venga rilasciato anche in caso di eccezioni.

### newCondition()

Restituisce un nuovo oggetto `Condition` associato a questo lock. Le condizioni forniscono un meccanismo per i thread per sospendersi temporaneamente (wait) fino a quando non vengono notificati che una condizione potrebbe essere vera.

```java
Lock lock = new ReentrantLock();
Condition condition = lock.newCondition();

// Thread 1
lock.lock();
try {
    while (!conditionMet) {
        condition.await(); // Rilascia il lock e attende
    }
    // Procedi quando la condizione è soddisfatta
} finally {
    lock.unlock();
}

// Thread 2
lock.lock();
try {
    // Modifica lo stato che influisce sulla condizione
    conditionMet = true;
    condition.signal(); // Notifica un thread in attesa
} finally {
    lock.unlock();
}
```

## Acquisizione e Rilascio Espliciti

A differenza dei blocchi `synchronized`, i lock dell'interfaccia `Lock` richiedono un'acquisizione e un rilascio espliciti. Questo offre maggiore flessibilità ma richiede anche maggiore attenzione per evitare problemi come deadlock o lock non rilasciati.

Il pattern standard per l'utilizzo di un lock è:

```java
Lock lock = new ReentrantLock();
lock.lock();
try {
    // Sezione critica
} finally {
    lock.unlock();
}
```

È fondamentale seguire questo pattern per garantire che il lock venga sempre rilasciato, anche in caso di eccezioni nella sezione critica.

## Tentativi di Acquisizione con Timeout

Una delle caratteristiche più potenti dell'interfaccia `Lock` è la possibilità di specificare un timeout per l'acquisizione di un lock. Questo può essere utile per evitare deadlock o per implementare algoritmi che non devono bloccarsi indefinitamente.

```java
public boolean transferMoney(Account fromAccount, Account toAccount, double amount, long timeout, TimeUnit unit) throws InterruptedException {
    long startTime = System.nanoTime();
    long remainingNanos = unit.toNanos(timeout);
    
    while (true) {
        // Tenta di acquisire il lock sul primo account
        if (fromAccount.getLock().tryLock()) {
            try {
                // Tenta di acquisire il lock sul secondo account
                if (toAccount.getLock().tryLock()) {
                    try {
                        // Entrambi i lock acquisiti, esegui il trasferimento
                        if (fromAccount.getBalance() < amount) {
                            throw new InsufficientFundsException();
                        } else {
                            fromAccount.debit(amount);
                            toAccount.credit(amount);
                            return true; // Trasferimento riuscito
                        }
                    } finally {
                        toAccount.getLock().unlock();
                    }
                }
            } finally {
                fromAccount.getLock().unlock();
            }
        }
        
        // Calcola il tempo rimanente
        remainingNanos -= (System.nanoTime() - startTime);
        if (remainingNanos <= 0) {
            return false; // Timeout scaduto
        }
        
        // Breve pausa prima di riprovare
        Thread.sleep(Math.min(TimeUnit.NANOSECONDS.toMillis(remainingNanos), 50));
    }
}
```

Questo esempio mostra come utilizzare i timeout per implementare un algoritmo di trasferimento di denaro che evita deadlock.

## Confronto con synchronized

| Caratteristica | synchronized | Lock |
|----------------|--------------|------|
| Sintassi | Più semplice | Più verbosa |
| Rilascio automatico | Sì | No (richiede unlock esplicito) |
| Tentativo non bloccante | No | Sì (tryLock) |
| Timeout | No | Sì (tryLock con timeout) |
| Interrompibilità | No | Sì (lockInterruptibly) |
| Fairness | No | Sì (con ReentrantLock) |
| Condizioni multiple | No | Sì (newCondition) |
| Informazioni di diagnostica | No | Sì (metodi di ispezione) |

## Conclusione

L'interfaccia `Lock` e le sue implementazioni offrono un meccanismo di sincronizzazione più potente e flessibile rispetto ai blocchi `synchronized` tradizionali. Questa flessibilità aggiuntiva è particolarmente utile in scenari complessi dove è necessario un controllo fine sulla sincronizzazione.

Tuttavia, questa potenza aggiuntiva comporta anche una maggiore responsabilità: è fondamentale seguire le best practice per l'acquisizione e il rilascio dei lock per evitare problemi come deadlock, livelock o lock non rilasciati.

Nel prossimo capitolo, esploreremo in dettaglio `ReentrantLock`, l'implementazione più comune dell'interfaccia `Lock`.

## Navigazione

- [Indice del Modulo](./README.md)
- Successivo: [ReentrantLock](./02-ReentrantLock.md)