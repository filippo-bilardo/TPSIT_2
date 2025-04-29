# Worker Thread

Il pattern Worker Thread è un pattern concorrente che utilizza un pool di thread (worker) per elaborare le richieste da una coda di lavoro. Questo pattern è particolarmente utile per gestire un numero elevato di richieste in modo efficiente, limitando il numero di thread attivi.

## Pool di Worker Thread

Un pool di worker thread è un gruppo di thread che vengono creati in anticipo e rimangono in attesa di lavoro da eseguire. Quando arriva una richiesta, uno dei thread disponibili nel pool la preleva dalla coda e la elabora. Una volta completata l'elaborazione, il thread torna disponibile per gestire altre richieste.

### Vantaggi del Pool di Thread

1. **Riutilizzo dei Thread**: I thread vengono creati una sola volta e riutilizzati per più richieste, riducendo l'overhead di creazione e distruzione dei thread.

2. **Controllo delle Risorse**: È possibile limitare il numero massimo di thread concorrenti, prevenendo il sovraccarico del sistema.

3. **Miglioramento delle Prestazioni**: Il riutilizzo dei thread e la gestione efficiente delle richieste possono migliorare significativamente le prestazioni in scenari con molte richieste.

4. **Gestione della Coda**: Le richieste in eccesso vengono messe in coda invece di creare nuovi thread, permettendo di gestire picchi di carico senza esaurire le risorse.

## Code di Lavoro

Le code di lavoro sono strutture dati che memorizzano le richieste in attesa di essere elaborate dai worker thread. Queste code fungono da buffer tra i client che generano le richieste e i worker thread che le elaborano.

### Tipi di Code

1. **Code Unbounded**: Non hanno un limite di capacità ma possono portare a problemi di memoria se il numero di richieste cresce troppo.

2. **Code Bounded**: Hanno una capacità massima e possono implementare diverse strategie quando la coda è piena (bloccare, rifiutare, sostituire elementi, ecc.).

3. **Code con Priorità**: Permettono di assegnare priorità alle richieste, in modo che quelle più importanti vengano elaborate prima.

### Implementazione in Java

Java fornisce diverse implementazioni di code thread-safe nel package `java.util.concurrent`:

- `LinkedBlockingQueue`: Una coda unbounded basata su linked nodes.
- `ArrayBlockingQueue`: Una coda bounded basata su array.
- `PriorityBlockingQueue`: Una coda con priorità unbounded.
- `DelayQueue`: Una coda che rilascia gli elementi solo dopo un certo ritardo.
- `SynchronousQueue`: Una coda che non memorizza elementi ma sincronizza direttamente producer e consumer.

## Bilanciamento del Carico

Il bilanciamento del carico è un aspetto cruciale del pattern Worker Thread, che mira a distribuire equamente il lavoro tra i worker thread disponibili.

### Strategie di Bilanciamento

1. **Round Robin**: Le richieste vengono distribuite ciclicamente tra i worker thread.

2. **Least Connections**: Le richieste vengono assegnate al worker thread con il minor numero di richieste attive.

3. **Random**: Le richieste vengono assegnate casualmente ai worker thread.

4. **Work Stealing**: I worker thread inattivi possono "rubare" lavoro dalle code di altri worker thread occupati.

### Dimensionamento del Pool

La scelta del numero ottimale di thread nel pool dipende da diversi fattori:

1. **Numero di Core**: In generale, per task CPU-bound, il numero ottimale di thread è vicino al numero di core disponibili.

2. **Natura dei Task**: Per task I/O-bound, può essere vantaggioso avere più thread del numero di core, poiché molti thread potrebbero essere in attesa di operazioni I/O.

3. **Memoria Disponibile**: Ogni thread consuma risorse di sistema, quindi è importante considerare la memoria disponibile.

4. **Latenza vs. Throughput**: Un numero maggiore di thread può migliorare il throughput ma potrebbe aumentare la latenza a causa del context switching.

## Implementazione con ThreadPoolExecutor

Java fornisce una potente implementazione del pattern Worker Thread attraverso la classe `ThreadPoolExecutor` del package `java.util.concurrent`.

```java
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class WorkerThreadExample {
    public static void main(String[] args) {
        // Creazione di un ThreadPoolExecutor con:
        // - corePoolSize: 2 (numero minimo di thread da mantenere nel pool)
        // - maximumPoolSize: 4 (numero massimo di thread nel pool)
        // - keepAliveTime: 10 (tempo di inattività prima che un thread in eccesso venga terminato)
        // - unit: SECONDS (unità di tempo per keepAliveTime)
        // - workQueue: coda con capacità 10
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
            2,                          // corePoolSize
            4,                          // maximumPoolSize
            10, TimeUnit.SECONDS,       // keepAliveTime e unità
            new ArrayBlockingQueue<>(10) // workQueue
        );
        
        // Sottomissione di task all'executor
        for (int i = 0; i < 20; i++) {
            final int taskId = i;
            executor.execute(() -> {
                System.out.println("Esecuzione del task " + taskId + 
                                   " nel thread " + Thread.currentThread().getName());
                try {
                    // Simulazione di un'operazione che richiede tempo
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        
        // Shutdown dell'executor
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
```

### Parametri di ThreadPoolExecutor

1. **corePoolSize**: Il numero minimo di thread da mantenere nel pool, anche se sono inattivi.

2. **maximumPoolSize**: Il numero massimo di thread che possono essere nel pool.

3. **keepAliveTime**: Il tempo massimo che i thread in eccesso rispetto a corePoolSize possono rimanere inattivi prima di essere terminati.

4. **workQueue**: La coda utilizzata per memorizzare i task prima che vengano eseguiti.

5. **threadFactory**: La factory utilizzata per creare nuovi thread (opzionale).

6. **handler**: La strategia da utilizzare quando la coda è piena e il pool ha raggiunto maximumPoolSize (opzionale).

### Politiche di Rifiuto

Quando la coda di lavoro è piena e il pool ha raggiunto il numero massimo di thread, `ThreadPoolExecutor` può utilizzare diverse politiche di rifiuto:

1. **AbortPolicy**: Lancia un'eccezione `RejectedExecutionException` (default).

2. **CallerRunsPolicy**: Esegue il task nel thread del chiamante, rallentando il producer.

3. **DiscardPolicy**: Scarta silenziosamente il task.

4. **DiscardOldestPolicy**: Scarta il task più vecchio nella coda e riprova a sottomettere il nuovo task.

## Casi d'Uso e Considerazioni

### Casi d'Uso Ideali

1. **Server Web**: Gestione di richieste HTTP concorrenti.

2. **Sistemi di Elaborazione Batch**: Elaborazione di grandi quantità di dati in parallelo.

3. **Applicazioni con Molte Operazioni I/O**: Gestione efficiente di operazioni di I/O concorrenti.

4. **Sistemi di Messaggistica**: Elaborazione di messaggi da code.

### Considerazioni Importanti

1. **Dimensionamento del Pool**: Un pool troppo piccolo può limitare il throughput, mentre un pool troppo grande può causare overhead di context switching.

2. **Gestione delle Eccezioni**: È importante gestire correttamente le eccezioni nei worker thread per evitare che terminino inaspettatamente.

3. **Monitoraggio**: È utile monitorare le prestazioni del pool (numero di thread attivi, dimensione della coda, ecc.) per ottimizzare i parametri.

4. **Shutdown Graceful**: È importante implementare una procedura di shutdown che permetta ai task in esecuzione di completarsi.

## Conclusione

Il pattern Worker Thread è un pattern potente e flessibile per gestire l'esecuzione concorrente di task. Grazie alla sua capacità di limitare il numero di thread attivi e di gestire efficientemente le richieste in coda, questo pattern è ideale per applicazioni che devono gestire un numero elevato di richieste concorrenti.

Java fornisce un'implementazione robusta di questo pattern attraverso la classe `ThreadPoolExecutor`, che offre un controllo fine sul comportamento del pool di thread e sulla gestione delle richieste.

Nel prossimo capitolo, esploreremo il pattern Read-Write Lock, che permette di ottimizzare l'accesso concorrente a risorse condivise distinguendo tra operazioni di lettura e scrittura.

## Navigazione

- [Indice del Corso](../README.md)
- [Indice del Modulo](./README.md)
- Precedente: [Thread-per-Message](./02-ThreadPerMessage.md)
- Successivo: [Read-Write Lock](./04-ReadWriteLock.md)