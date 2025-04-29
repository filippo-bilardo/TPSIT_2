# Thread Pool e Executor

In questa esercitazione esploreremo il framework Executor introdotto in Java 5, che fornisce un'astrazione di alto livello per la gestione dei thread e l'esecuzione di attività asincrone.

I thread pool sono una soluzione efficiente per gestire un gran numero di attività concorrenti senza dover creare un nuovo thread per ogni operazione. Questo approccio offre numerosi vantaggi:

- **Riutilizzo dei thread**: i thread vengono riutilizzati per eseguire più attività, riducendo l'overhead di creazione e distruzione
- **Gestione controllata delle risorse**: limita il numero di thread attivi contemporaneamente
- **Miglioramento delle prestazioni**: riduce la latenza di esecuzione delle attività
- **Separazione delle responsabilità**: separa la logica di business dalla gestione dei thread

## Indice degli Argomenti Teorici

1. [Framework Executor](./01-FrameworkExecutor.md)
   - Interfaccia Executor
   - ExecutorService
   - Tipi di Executor predefiniti
   - Shutdown di un Executor

2. [Pool di Thread](./02-PoolDiThread.md)
   - Fixed Thread Pool
   - Cached Thread Pool
   - Scheduled Thread Pool
   - Single Thread Executor
   - Work Stealing Pool (Fork/Join)

3. [Callable e Future](./03-CallableFuture.md)
   - Interfaccia Callable
   - Interfaccia Future
   - Ottenere risultati da attività asincrone
   - Gestione delle eccezioni

4. [CompletableFuture](./04-CompletableFuture.md)
   - Programmazione asincrona avanzata
   - Composizione di operazioni asincrone
   - Gestione degli errori
   - Combinazione di risultati multipli

## Esempi Pratici

In questa sezione troverai esempi di codice che illustrano l'uso del framework Executor e dei thread pool:

- [BasicExecutorExample.java](./esempi/BasicExecutorExample.java): Esempio base di utilizzo di un Executor
- [ThreadPoolTypesExample.java](./esempi/ThreadPoolTypesExample.java): Esempi dei diversi tipi di thread pool
- [CallableFutureExample.java](./esempi/CallableFutureExample.java): Utilizzo di Callable e Future
- [CompletableFutureExample.java](./esempi/CompletableFutureExample.java): Esempi di programmazione asincrona con CompletableFuture

## Esercizi

1. Implementare un servizio di download concorrente utilizzando un thread pool
2. Creare un'applicazione che esegue attività pianificate con ScheduledExecutorService
3. Sviluppare un sistema di elaborazione asincrona con CompletableFuture

## Navigazione

- [Indice del Corso](../README.md)
- Precedente: [Comunicazione tra Thread](../03-ComunicazioneThread/README.md)
- Successivo: [Collezioni Concorrenti](../05-CollezioniConcorrenti/README.md)