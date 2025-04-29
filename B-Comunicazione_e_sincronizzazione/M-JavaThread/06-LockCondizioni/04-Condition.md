# Condition
## Introduzione alle Condizioni

L'interfaccia `Condition` fornisce un meccanismo per i thread per sospendere l'esecuzione ("attendere") fino a quando non viene notificato che una certa condizione potrebbe essere vera. È un'alternativa più flessibile ai metodi `wait()`, `notify()` e `notifyAll()` degli oggetti in Java.

Le condizioni sono sempre associate a un lock e si ottengono chiamando il metodo `newCondition()` su un oggetto `Lock`.

## L'Interfaccia Condition

L'interfaccia `Condition` definisce i metodi per attendere e segnalare condizioni:

```java
public interface Condition {
    void await() throws InterruptedException;
    boolean await(long time, TimeUnit unit) throws InterruptedException;
    long awaitNanos(long nanosTimeout) throws InterruptedException;
    void awaitUninterruptibly();
    boolean awaitUntil(Date deadline) throws InterruptedException;
    
    void signal();
    void signalAll();
}
```

Esaminiamo in dettaglio ciascun metodo:

### Metodi di Attesa

- `await()`: Fa attendere il thread corrente fino a quando non viene segnalato o interrotto. Rilascia temporaneamente il lock associato durante l'attesa.

- `await(long time, TimeUnit unit)`: Come `await()`, ma con un timeout. Ritorna `false` se il timeout scade prima della segnalazione.

- `awaitNanos(long nanosTimeout)`: Versione più precisa di `await()` con timeout. Ritorna il tempo rimanente (in nanosecondi) o un valore ≤ 0 se il timeout è scaduto.

- `awaitUninterruptibly()`: Come `await()`, ma non può essere interrotto. Utile quando l'interruzione potrebbe lasciare lo stato in modo inconsistente.

- `awaitUntil(Date deadline)`: Attende fino a una data/ora specifica. Ritorna `false` se la deadline è passata prima della segnalazione.

### Metodi di Segnalazione

- `signal()`: Sveglia un singolo thread in attesa sulla condizione. Il thread risvegliato competerà per riacquisire il lock.

- `signalAll()`: Sveglia tutti i thread in attesa sulla condizione. I thread risvegliati competeranno per riacquisire il lock.

## Confronto con wait/notify

Le condizioni offrono diversi vantaggi rispetto ai metodi `wait()`, `notify()` e `notifyAll()` tradizionali:

| Caratteristica | Condition | wait/notify |
|----------------|-----------|-------------|
| Associazione | Con un Lock specifico | Con un oggetto qualsiasi |
| Condizioni multiple | Sì, per lo stesso lock | No, una sola per oggetto |
| Segnalazione | Selettiva o broadcast | Solo broadcast con notifyAll() |
| Timeout | Varie opzioni | Solo millisecondi |
| Interrompibilità | Configurabile | Sempre interrompibile |
| Rilascio lock | Esplicito | Implicito |

## Attesa Condizionale

L'attesa condizionale è un pattern comune nella programmazione concorrente. Un thread attende che una certa condizione diventi vera prima di procedere. Con l'interfaccia `Condition`, questo pattern diventa più chiaro e flessibile:

```java
lock.lock();
try {
    while (!condizione) {  // Verifica la condizione in un ciclo
        condition.await();  // Attendi se la condizione non è vera
    }
    // Procedi quando la condizione è vera
} finally {
    lock.unlock();
}
```

È importante notare l'uso del ciclo `while` invece di un semplice `if`. Questo protegge contro i "risvegli spuri" (quando un thread viene risvegliato senza che la condizione sia necessariamente vera) e contro i cambiamenti di stato tra la segnalazione e la riacquisizione del lock.

## Segnalazione Selettiva

Uno dei principali vantaggi delle condizioni è la possibilità di avere più condizioni associate allo stesso lock. Questo permette una segnalazione più selettiva e mirata:

```java
class BoundedBuffer<E> {
    private final Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();
    
    private final E[] items;
    private int putIndex, takeIndex, count;
    
    // ...
    
    public void put(E e) throws InterruptedException {
        lock.lock();
        try {
            while (count == items.length) {
                notFull.await();  // Attendi che ci sia spazio
            }
            items[putIndex] = e;
            putIndex = (putIndex + 1) % items.length;
            count++;
            notEmpty.signal();  // Segnala che il buffer non è più vuoto
        } finally {
            lock.unlock();
        }
    }
    
    public E take() throws InterruptedException {
        lock.lock();
        try {
            while (count == 0) {
                notEmpty.await();  // Attendi che ci sia almeno un elemento
            }
            E e = items[takeIndex];
            takeIndex = (takeIndex + 1) % items.length;
            count--;
            notFull.signal();  // Segnala che il buffer non è più pieno
            return e;
        } finally {
            lock.unlock();
        }
    }
}
```

In questo esempio, abbiamo due condizioni distinte:
- `notFull`: per i produttori che attendono spazio nel buffer
- `notEmpty`: per i consumatori che attendono elementi nel buffer

Questo approccio è più efficiente rispetto a `notifyAll()`, poiché sveglia solo i thread che possono effettivamente procedere.

## Implementazione di Buffer Limitati

L'esempio precedente mostra come implementare un buffer limitato (o bounded buffer), un pattern comune nella programmazione concorrente. Questo pattern è utile quando si ha un produttore che genera dati a una velocità diversa rispetto a un consumatore che li elabora.

Il buffer limitato agisce come un intermediario, permettendo al produttore e al consumatore di lavorare in modo asincrono. Le condizioni `notFull` e `notEmpty` garantiscono che:

1. I produttori attendano quando il buffer è pieno
2. I consumatori attendano quando il buffer è vuoto
3. I produttori sveglino i consumatori quando aggiungono elementi
4. I consumatori sveglino i produttori quando rimuovono elementi

Questo pattern può essere esteso a scenari più complessi con più produttori e consumatori.

## Best Practices

Quando si utilizzano le condizioni, è importante seguire alcune best practices:

1. **Verifica sempre le condizioni in un ciclo while**: Protegge contro i risvegli spuri e i cambiamenti di stato.

2. **Mantieni il lock durante la verifica della condizione**: La condizione deve essere verificata mentre si detiene il lock.

3. **Usa la segnalazione selettiva quando possibile**: Usa `signal()` invece di `signalAll()` quando sai esattamente quale thread svegliare.

4. **Documenta le condizioni di attesa**: Chiarisci quali condizioni un thread sta attendendo e quali azioni possono soddisfarle.

5. **Evita di chiamare metodi esterni mentre detieni il lock**: Può portare a deadlock o ridurre la concorrenza.

## Conclusione

L'interfaccia `Condition` offre un meccanismo potente e flessibile per la comunicazione tra thread. Rispetto ai metodi `wait()` e `notify()` tradizionali, le condizioni permettono una gestione più precisa e selettiva dell'attesa e della segnalazione.

Le condizioni sono particolarmente utili in scenari complessi dove è necessario coordinare l'attività di più thread in base a diverse condizioni. Il pattern del buffer limitato è solo uno dei molti esempi dove le condizioni possono migliorare significativamente la chiarezza e l'efficienza del codice concorrente.

Nel prossimo capitolo, esploreremo `StampedLock`, un tipo di lock introdotto in Java 8 che offre modalità di accesso ottimistiche per migliorare ulteriormente le prestazioni in scenari specifici.