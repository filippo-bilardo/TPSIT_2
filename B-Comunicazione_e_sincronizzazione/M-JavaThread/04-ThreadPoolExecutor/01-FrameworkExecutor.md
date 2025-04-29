# Framework Executor

## Introduzione

Il framework Executor, introdotto in Java 5 come parte del package `java.util.concurrent`, fornisce un'astrazione di alto livello per l'esecuzione asincrona di attività. Questo framework separa la logica di *cosa* deve essere eseguito dalla logica di *come* deve essere eseguito, permettendo di gestire i thread in modo più efficiente e strutturato.

Prima dell'introduzione del framework Executor, la gestione dei thread in Java richiedeva la creazione e la gestione manuale dei thread, portando spesso a codice complesso e difficile da mantenere. Il framework Executor risolve questi problemi fornendo un'API standardizzata per la sottomissione e l'esecuzione di attività.

## Interfaccia Executor

Al centro del framework c'è l'interfaccia `Executor`, che definisce un singolo metodo:

```java
public interface Executor {
    void execute(Runnable command);
}
```

Questo metodo accetta un oggetto `Runnable` e lo esegue in un momento futuro. La semplicità di questa interfaccia nasconde la potenza del framework: l'implementazione può decidere *come* eseguire il comando, che potrebbe significare:

- Creazione di un nuovo thread per ogni attività
- Riutilizzo di thread esistenti da un pool
- Esecuzione nel thread chiamante
- Esecuzione pianificata in un momento futuro
- Rifiuto dell'esecuzione in base a politiche definite

## ExecutorService

L'interfaccia `ExecutorService` estende `Executor` aggiungendo metodi per gestire il ciclo di vita dell'executor e per ottenere risultati da attività asincrone:

```java
public interface ExecutorService extends Executor {
    void shutdown();
    List<Runnable> shutdownNow();
    boolean isShutdown();
    boolean isTerminated();
    boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException;
    
    <T> Future<T> submit(Callable<T> task);
    <T> Future<T> submit(Runnable task, T result);
    Future<?> submit(Runnable task);
    // Altri metodi...
}
```

I metodi principali includono:

- **shutdown()**: Avvia una chiusura ordinata, in cui gli executor non accettano nuove attività ma completano quelle già in esecuzione
- **shutdownNow()**: Tenta di interrompere tutte le attività in esecuzione e restituisce un elenco di attività in attesa
- **submit()**: Invia un'attività per l'esecuzione e restituisce un oggetto `Future` che rappresenta il risultato dell'attività

## Tipi di Executor Predefiniti

La classe `Executors` fornisce metodi factory per creare diversi tipi di executor predefiniti:

### 1. Fixed Thread Pool

```java
ExecutorService executor = Executors.newFixedThreadPool(nThreads);
```

Crea un pool con un numero fisso di thread. Se vengono inviate più attività di quanti thread sono disponibili, le attività in eccesso vengono messe in coda fino a quando un thread diventa disponibile.

### 2. Cached Thread Pool

```java
ExecutorService executor = Executors.newCachedThreadPool();
```

Crea un pool che crea nuovi thread secondo necessità, ma riutilizza i thread esistenti quando sono disponibili. I thread inutilizzati vengono terminati dopo 60 secondi di inattività.

### 3. Single Thread Executor

```java
ExecutorService executor = Executors.newSingleThreadExecutor();
```

Crea un executor che utilizza un singolo thread per eseguire le attività in modo sequenziale.

### 4. Scheduled Thread Pool

```java
ScheduledExecutorService executor = Executors.newScheduledThreadPool(corePoolSize);
```

Crea un pool che può pianificare l'esecuzione di attività dopo un ritardo o periodicamente.

## Shutdown di un Executor

È fondamentale chiudere correttamente un executor quando non è più necessario, altrimenti l'applicazione potrebbe non terminare correttamente. Il pattern tipico è:

```java
try {
    // Sottometti attività all'executor
    executor.submit(task1);
    executor.submit(task2);
    
    // Altre operazioni...
} finally {
    executor.shutdown();
    try {
        // Attendi la terminazione di tutte le attività
        if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
            executor.shutdownNow(); // Forza la terminazione se necessario
        }
    } catch (InterruptedException e) {
        executor.shutdownNow();
        Thread.currentThread().interrupt();
    }
}
```

## Vantaggi del Framework Executor

1. **Separazione delle responsabilità**: Separa la logica di business dalla gestione dei thread
2. **Riutilizzo dei thread**: Riduce l'overhead di creazione e distruzione dei thread
3. **Gestione del pool**: Controlla il numero di thread attivi contemporaneamente
4. **Politiche di rifiuto**: Definisce comportamenti quando il sistema è sovraccarico
5. **Pianificazione**: Supporta l'esecuzione ritardata o periodica di attività

## Esempio Completo

Ecco un esempio che dimostra l'uso di un fixed thread pool per elaborare una lista di attività:

```java
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExecutorExample {
    public static void main(String[] args) {
        // Crea un pool con 3 thread
        ExecutorService executor = Executors.newFixedThreadPool(3);
        
        try {
            // Sottometti 10 attività al pool
            for (int i = 0; i < 10; i++) {
                final int taskId = i;
                executor.submit(() -> {
                    System.out.println("Esecuzione attività " + taskId + 
                                     " sul thread " + Thread.currentThread().getName());
                    try {
                        // Simula un'elaborazione
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    System.out.println("Attività " + taskId + " completata");
                    return null;
                });
            }
        } finally {
            // Avvia la procedura di shutdown
            executor.shutdown();
            
            try {
                // Attendi fino a 5 secondi per la terminazione
                if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                    System.out.println("Alcune attività non sono state completate, forzo la terminazione");
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
        
        System.out.println("Tutte le attività sono state completate");
    }
}
```

## Conclusione

Il framework Executor rappresenta un significativo miglioramento rispetto alla gestione manuale dei thread, offrendo un'API potente e flessibile per l'esecuzione di attività asincrone. Utilizzando questo framework, è possibile scrivere codice concorrente più pulito, manutenibile e scalabile.

Nel prossimo capitolo, esploreremo in dettaglio i diversi tipi di pool di thread e le loro caratteristiche specifiche.

## Navigazione

- [Indice del Modulo](./README.md)
- Successivo: [Pool di Thread](./02-PoolDiThread.md)