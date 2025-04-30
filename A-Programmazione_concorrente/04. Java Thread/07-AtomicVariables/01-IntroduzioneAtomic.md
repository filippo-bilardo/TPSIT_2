# Introduzione alle Variabili Atomiche

Le variabili atomiche sono un componente fondamentale della programmazione concorrente in Java, fornendo operazioni thread-safe su singole variabili senza la necessità di utilizzare lock espliciti. In questo capitolo, esploreremo i concetti di base delle variabili atomiche e il loro ruolo nella programmazione concorrente.

## Problemi di Concorrenza e Race Condition

Nella programmazione multi-thread, uno dei problemi più comuni è la race condition. Questo si verifica quando due o più thread accedono e modificano contemporaneamente una risorsa condivisa, e il risultato finale dipende dall'ordine di esecuzione dei thread.

Consideriamo un semplice esempio di un contatore condiviso:

```java
public class Counter {
    private int count = 0;
    
    public void increment() {
        count++; // Operazione non atomica!
    }
    
    public int getCount() {
        return count;
    }
}
```

L'operazione `count++` sembra semplice, ma in realtà è composta da tre operazioni distinte:

1. Leggere il valore corrente di `count`
2. Incrementare il valore di 1
3. Scrivere il nuovo valore in `count`

Se due thread eseguono questa operazione contemporaneamente, possono verificarsi interferenze che portano a risultati inaspettati:

- Thread 1 legge `count` = 0
- Thread 2 legge `count` = 0
- Thread 1 incrementa a 1
- Thread 2 incrementa a 1
- Thread 1 scrive 1 in `count`
- Thread 2 scrive 1 in `count`

Il risultato finale è 1, non 2 come ci si aspetterebbe.

## Soluzioni Tradizionali: Sincronizzazione

La soluzione tradizionale a questo problema è utilizzare la sincronizzazione:

```java
public class SynchronizedCounter {
    private int count = 0;
    
    public synchronized void increment() {
        count++;
    }
    
    public synchronized int getCount() {
        return count;
    }
}
```

Questo approccio funziona, ma ha alcuni svantaggi:

1. **Overhead di performance**: L'acquisizione e il rilascio di lock hanno un costo in termini di prestazioni.
2. **Rischio di deadlock**: Se più lock sono coinvolti, c'è il rischio di deadlock.
3. **Granularità grossolana**: La sincronizzazione blocca l'intero metodo o oggetto, anche quando potrebbe essere necessario proteggere solo una singola variabile.

## Introduzione alle Variabili Atomiche

Le variabili atomiche, introdotte in Java 5 nel package `java.util.concurrent.atomic`, offrono un'alternativa più efficiente alla sincronizzazione tradizionale per operazioni su singole variabili.

Ecco un esempio di contatore utilizzando `AtomicInteger`:

```java
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicCounter {
    private AtomicInteger count = new AtomicInteger(0);
    
    public void increment() {
        count.incrementAndGet();
    }
    
    public int getCount() {
        return count.get();
    }
}
```

## Vantaggi delle Variabili Atomiche

Le variabili atomiche offrono diversi vantaggi rispetto alla sincronizzazione tradizionale:

1. **Migliori prestazioni**: Le operazioni atomiche sono generalmente più veloci dell'acquisizione e del rilascio di lock.
2. **Nessun rischio di deadlock**: Poiché non utilizzano lock, non c'è rischio di deadlock.
3. **Granularità fine**: Proteggono solo la singola variabile, non l'intero metodo o oggetto.
4. **Operazioni composte**: Forniscono operazioni atomiche composte come compare-and-set (CAS).

## Primitive Hardware per la Concorrenza

Le variabili atomiche in Java si basano su primitive hardware specifiche della piattaforma, come le istruzioni Compare-and-Swap (CAS). Queste istruzioni permettono di eseguire operazioni atomiche a livello hardware, senza la necessità di lock software.

L'istruzione CAS funziona così:

1. Confronta il valore corrente di una variabile con un valore atteso.
2. Se sono uguali, sostituisce il valore corrente con un nuovo valore.
3. Restituisce un indicatore di successo o fallimento.

Questa operazione è atomica, nel senso che avviene come un'unica unità indivisibile a livello hardware.

## Classi Atomic in Java

Java fornisce diverse classi atomic per diversi tipi di dati:

- **Tipi primitivi**: `AtomicInteger`, `AtomicLong`, `AtomicBoolean`
- **Riferimenti**: `AtomicReference`, `AtomicStampedReference`, `AtomicMarkableReference`
- **Array**: `AtomicIntegerArray`, `AtomicLongArray`, `AtomicReferenceArray`
- **Updater**: `AtomicIntegerFieldUpdater`, `AtomicLongFieldUpdater`, `AtomicReferenceFieldUpdater`
- **Accumulatori**: `DoubleAdder`, `LongAdder`, `DoubleAccumulator`, `LongAccumulator`

Nei prossimi capitoli, esploreremo in dettaglio ciascuna di queste classi e i loro casi d'uso.

## Programmazione Lock-Free

Le variabili atomiche sono un componente fondamentale della programmazione lock-free, un approccio alla concorrenza che evita l'uso di lock tradizionali. La programmazione lock-free offre diversi vantaggi:

1. **Resistenza ai deadlock**: Senza lock, non ci sono deadlock.
2. **Resistenza ai livelock**: Meno probabilità di livelock rispetto agli approcci basati su lock.
3. **Tolleranza ai guasti**: Se un thread fallisce durante un'operazione, non blocca altri thread.
4. **Migliori prestazioni in scenari di alta contesa**: Meno overhead in situazioni con molti thread che competono per le stesse risorse.

Tuttavia, la programmazione lock-free è anche più complessa e richiede una comprensione approfondita dei modelli di memoria e delle garanzie di atomicità.

## Conclusioni

Le variabili atomiche sono uno strumento potente per la programmazione concorrente in Java, offrendo un'alternativa efficiente e sicura alla sincronizzazione tradizionale per operazioni su singole variabili. Nei prossimi capitoli, esploreremo in dettaglio le diverse classi atomic e i loro casi d'uso.

Nel prossimo capitolo, esamineremo le classi atomic di base come `AtomicInteger`, `AtomicLong`, `AtomicBoolean` e `AtomicReference`.