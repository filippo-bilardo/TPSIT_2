# Thread-per-Message

Il pattern Thread-per-Message è un pattern concorrente che delega l'esecuzione di un'attività a un thread dedicato. Questo pattern è particolarmente utile quando si desidera eseguire operazioni in modo asincrono senza bloccare il thread chiamante.

## Descrizione e Struttura del Pattern

Il pattern Thread-per-Message si basa su un principio semplice: per ogni richiesta o messaggio che richiede elaborazione, viene creato o allocato un thread dedicato per gestirlo. Questo permette al thread chiamante di continuare la sua esecuzione senza attendere il completamento dell'attività delegata.

### Componenti Principali

1. **Client**: Il componente che richiede l'esecuzione di un'attività.

2. **Host**: Il componente che riceve la richiesta e crea o alloca un thread per gestirla.

3. **Thread Dedicato**: Il thread che esegue effettivamente l'attività richiesta.

### Flusso di Esecuzione

1. Il Client invia una richiesta all'Host.
2. L'Host crea o alloca un thread dedicato per gestire la richiesta.
3. Il thread dedicato esegue l'attività richiesta.
4. Il Client continua la sua esecuzione senza attendere il completamento dell'attività.

## Delega di Attività a Thread Dedicati

La delega di attività a thread dedicati offre diversi vantaggi:

1. **Asincronia**: Il Client può continuare la sua esecuzione senza attendere il completamento dell'attività delegata.

2. **Isolamento**: Ogni attività viene eseguita in un contesto isolato, riducendo il rischio di interferenze tra attività diverse.

3. **Semplicità**: Il pattern è concettualmente semplice e facile da implementare.

### Esempio Base in Java

```java
public class ThreadPerMessageExample {
    public void processRequest(final Request request) {
        new Thread(() -> {
            // Elaborazione della richiesta
            processRequestInternal(request);
        }).start();
        
        // Il thread chiamante continua l'esecuzione senza attendere
    }
    
    private void processRequestInternal(Request request) {
        // Logica di elaborazione della richiesta
    }
}
```

In questo esempio, per ogni chiamata a `processRequest()`, viene creato un nuovo thread che esegue l'elaborazione della richiesta, mentre il thread chiamante continua la sua esecuzione.

## Implementazione con Executor

Creare un nuovo thread per ogni richiesta può essere inefficiente, specialmente in applicazioni con un alto volume di richieste. Un approccio più efficiente è utilizzare un `Executor` per gestire i thread:

```java
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPerMessageWithExecutor {
    private final ExecutorService executor;
    
    public ThreadPerMessageWithExecutor(int nThreads) {
        this.executor = Executors.newFixedThreadPool(nThreads);
    }
    
    public void processRequest(final Request request) {
        executor.execute(() -> {
            // Elaborazione della richiesta
            processRequestInternal(request);
        });
        
        // Il thread chiamante continua l'esecuzione senza attendere
    }
    
    private void processRequestInternal(Request request) {
        // Logica di elaborazione della richiesta
    }
    
    public void shutdown() {
        executor.shutdown();
    }
}
```

Questo approccio offre diversi vantaggi:

1. **Riutilizzo dei Thread**: I thread vengono riutilizzati invece di essere creati e distrutti per ogni richiesta.

2. **Controllo delle Risorse**: È possibile limitare il numero di thread concorrenti.

3. **Gestione della Coda**: Le richieste in eccesso vengono messe in coda invece di creare nuovi thread.

## Casi d'Uso e Limitazioni

### Casi d'Uso Ideali

1. **Operazioni I/O**: Operazioni di input/output che potrebbero bloccare il thread chiamante.

2. **Elaborazione Asincrona**: Attività che non richiedono un risultato immediato.

3. **Gestione di Eventi**: Elaborazione di eventi in sistemi event-driven.

4. **Servizi di Rete**: Gestione di richieste di rete in server multi-client.

### Limitazioni e Considerazioni

1. **Overhead di Thread**: La creazione e la gestione di thread comporta un overhead. In sistemi con molte richieste, è preferibile utilizzare un pool di thread.

2. **Gestione degli Errori**: Gli errori nel thread dedicato non sono automaticamente propagati al thread chiamante. È necessario implementare meccanismi specifici per la gestione degli errori.

3. **Risultati Asincroni**: Se è necessario ottenere un risultato dall'attività delegata, è necessario implementare meccanismi aggiuntivi (come Future o callback).

4. **Coordinamento**: Se le attività delegate devono essere coordinate tra loro, potrebbero essere necessari meccanismi di sincronizzazione aggiuntivi.

## Varianti del Pattern

### Thread-per-Message con Future

Quando è necessario ottenere un risultato dall'attività delegata, è possibile utilizzare `Future`:

```java
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadPerMessageWithFuture {
    private final ExecutorService executor;
    
    public ThreadPerMessageWithFuture(int nThreads) {
        this.executor = Executors.newFixedThreadPool(nThreads);
    }
    
    public Future<Result> processRequest(final Request request) {
        return executor.submit(() -> {
            // Elaborazione della richiesta
            return processRequestInternal(request);
        });
    }
    
    private Result processRequestInternal(Request request) {
        // Logica di elaborazione della richiesta
        return new Result();
    }
    
    public void shutdown() {
        executor.shutdown();
    }
}
```

### Thread-per-Message con Callback

Un'altra variante utilizza callback per notificare il completamento dell'attività:

```java
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPerMessageWithCallback {
    private final ExecutorService executor;
    
    public ThreadPerMessageWithCallback(int nThreads) {
        this.executor = Executors.newFixedThreadPool(nThreads);
    }
    
    public void processRequest(final Request request, final Callback<Result> callback) {
        executor.execute(() -> {
            try {
                // Elaborazione della richiesta
                Result result = processRequestInternal(request);
                callback.onSuccess(result);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }
    
    private Result processRequestInternal(Request request) {
        // Logica di elaborazione della richiesta
        return new Result();
    }
    
    public void shutdown() {
        executor.shutdown();
    }
    
    public interface Callback<T> {
        void onSuccess(T result);
        void onError(Exception e);
    }
}
```

## Conclusione

Il pattern Thread-per-Message è un pattern concorrente semplice ma potente che permette di delegare attività a thread dedicati, abilitando l'esecuzione asincrona. Sebbene la sua implementazione base sia semplice, è importante considerare aspetti come la gestione efficiente dei thread, la propagazione degli errori e la gestione dei risultati asincroni.

Nel prossimo capitolo, esploreremo il pattern Worker Thread, che estende il concetto di delega a un pool di thread worker che elaborano le richieste da una coda di lavoro.

## Navigazione

- [Indice del Corso](../README.md)
- [Indice del Modulo](./README.md)
- Precedente: [Introduzione ai Pattern Concorrenti](./01-IntroduzionePattern.md)
- Successivo: [Worker Thread](./03-WorkerThread.md)