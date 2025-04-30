# Pool di Thread

In questa lezione esploreremo i diversi tipi di pool di thread disponibili nel framework Executor di Java, le loro caratteristiche e i casi d'uso appropriati per ciascuno.

Un pool di thread è una raccolta di thread worker che vengono riutilizzati per eseguire più attività, eliminando l'overhead di creazione e distruzione continua di thread. Java fornisce diverse implementazioni predefinite di pool di thread attraverso la classe `Executors`.

## Fixed Thread Pool

### Descrizione
Un Fixed Thread Pool mantiene un numero fisso di thread attivi, indipendentemente dal numero di task in coda. Se un thread termina per un'eccezione non gestita, viene creato un nuovo thread per sostituirlo.

### Creazione
```java
ExecutorService executor = Executors.newFixedThreadPool(nThreads);
```

### Caratteristiche
- Numero costante di thread
- I task vengono accodati in una coda illimitata quando tutti i thread sono occupati
- Ideale per limitare l'utilizzo delle risorse
- Garantisce che non vengano creati più thread del numero specificato

### Casi d'uso
- Applicazioni server con carico prevedibile
- Elaborazione batch con risorse limitate
- Quando è necessario limitare il numero massimo di thread concorrenti

## Cached Thread Pool

### Descrizione
Un Cached Thread Pool crea nuovi thread secondo necessità e riutilizza quelli esistenti quando sono disponibili. I thread inutilizzati vengono terminati dopo 60 secondi di inattività.

### Creazione
```java
ExecutorService executor = Executors.newCachedThreadPool();
```

### Caratteristiche
- Dimensione del pool flessibile
- Crea nuovi thread se necessario
- Riutilizza thread esistenti quando disponibili
- I thread inattivi vengono terminati dopo 60 secondi
- Non utilizza una coda: i task vengono assegnati direttamente ai thread

### Casi d'uso
- Molti task di breve durata
- Applicazioni con carico variabile
- Quando il numero di thread necessari non è prevedibile

## Scheduled Thread Pool

### Descrizione
Uno Scheduled Thread Pool permette di eseguire task dopo un ritardo specificato o periodicamente a intervalli definiti.

### Creazione
```java
ScheduledExecutorService executor = Executors.newScheduledThreadPool(corePoolSize);
```

### Caratteristiche
- Supporta l'esecuzione ritardata e periodica dei task
- Mantiene un numero specificato di thread core
- Utilizza una coda di priorità per gestire i task in base al tempo di esecuzione

### Metodi principali
```java
// Esegue il task una volta dopo il ritardo specificato
schedule(Runnable command, long delay, TimeUnit unit)

// Esegue il task periodicamente dopo un ritardo iniziale
scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit)

// Esegue il task periodicamente con un ritardo fisso tra la fine di un'esecuzione e l'inizio della successiva
scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit)
```

### Casi d'uso
- Task di manutenzione periodici
- Polling di risorse esterne a intervalli regolari
- Implementazione di timeout

## Single Thread Executor

### Descrizione
Un Single Thread Executor utilizza un singolo thread worker per eseguire i task. Garantisce che i task vengano eseguiti sequenzialmente nell'ordine in cui sono stati sottomessi.

### Creazione
```java
ExecutorService executor = Executors.newSingleThreadExecutor();
```

### Caratteristiche
- Utilizza un solo thread worker
- Garantisce l'ordine di esecuzione dei task (FIFO)
- Se il thread termina per un'eccezione, ne viene creato uno nuovo

### Casi d'uso
- Quando è necessario garantire l'esecuzione sequenziale dei task
- Per task che non sono thread-safe
- Per evitare problemi di concorrenza

## Work Stealing Pool (Fork/Join)

### Descrizione
Un Work Stealing Pool è basato sul framework Fork/Join e implementa un algoritmo di "furto di lavoro" (work stealing) per bilanciare il carico tra i thread del pool.

### Creazione
```java
ExecutorService executor = Executors.newWorkStealingPool();
// Oppure specificando il parallelismo
ExecutorService executor = Executors.newWorkStealingPool(parallelism);
```

### Caratteristiche
- Utilizza il framework Fork/Join
- Implementa l'algoritmo di work stealing
- Il numero di thread è basato sul numero di processori disponibili se non specificato
- Ottimizzato per task ricorsivi (divide et impera)

### Casi d'uso
- Algoritmi paralleli ricorsivi
- Elaborazione di strutture dati gerarchiche
- Quando i task possono essere suddivisi in sottotask più piccoli

## Considerazioni sulla scelta del Pool

La scelta del tipo di pool di thread dipende da diversi fattori:

1. **Natura dei task**: durata, interdipendenza, requisiti di ordinamento
2. **Carico di lavoro**: costante, variabile, prevedibile
3. **Risorse disponibili**: CPU, memoria
4. **Requisiti di latenza**: tempo di risposta necessario

È importante monitorare le prestazioni del pool di thread e regolare i parametri in base alle esigenze specifiche dell'applicazione.

## Configurazione avanzata con ThreadPoolExecutor

Per un controllo più fine sul comportamento del pool di thread, è possibile utilizzare direttamente la classe `ThreadPoolExecutor`:

```java
ThreadPoolExecutor executor = new ThreadPoolExecutor(
    corePoolSize,          // numero minimo di thread
    maximumPoolSize,        // numero massimo di thread
    keepAliveTime,          // tempo di inattività prima che un thread in eccesso venga terminato
    timeUnit,               // unità di tempo per keepAliveTime
    workQueue,              // coda per i task in attesa
    threadFactory,          // factory per creare nuovi thread
    rejectedExecutionHandler // gestore per i task rifiutati
);
```

Questa configurazione avanzata permette di personalizzare completamente il comportamento del pool di thread in base alle esigenze specifiche dell'applicazione.

## Navigazione

- [Indice del Corso](../README.md)
- [Thread Pool e Executor](./README.md)
- Precedente: [Framework Executor](./01-FrameworkExecutor.md)
- Successivo: [Callable e Future](./03-CallableFuture.md)