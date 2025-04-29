# Pattern Concorrenti

In questa esercitazione esploreremo i pattern di progettazione specifici per la programmazione concorrente, che forniscono soluzioni riutilizzabili per problemi comuni negli ambienti multi-thread.

I pattern concorrenti aiutano a strutturare il codice in modo da gestire efficacemente la concorrenza, migliorare le prestazioni e ridurre i rischi di race condition, deadlock e altri problemi tipici della programmazione multi-thread.

## Indice degli Argomenti Teorici

1. [Introduzione ai Pattern Concorrenti](./01-IntroduzionePattern.md)
   - Importanza dei pattern nella programmazione concorrente
   - Classificazione dei pattern concorrenti
   - Criteri di scelta del pattern appropriato
   - Trade-off tra prestazioni, semplicità e sicurezza

2. [Thread-per-Message](./02-ThreadPerMessage.md)
   - Descrizione e struttura del pattern
   - Delega di attività a thread dedicati
   - Implementazione con Executor
   - Casi d'uso e limitazioni

3. [Worker Thread](./03-WorkerThread.md)
   - Pool di worker thread
   - Code di lavoro
   - Bilanciamento del carico
   - Implementazione con ThreadPoolExecutor

4. [Read-Write Lock](./04-ReadWriteLock.md)
   - Concorrenza tra operazioni di lettura e scrittura
   - Implementazione del pattern
   - Utilizzo delle classi Java esistenti
   - Ottimizzazione per diversi scenari di accesso

5. [Altri Pattern Concorrenti](./05-AltriPattern.md)
   - Future
   - Promise
   - Barrier
   - Semaphore
   - Double-Checked Locking
   - Thread-Local Storage
   - Active Object

## Esempi Pratici

In questa sezione troverai esempi di codice che illustrano l'implementazione dei pattern concorrenti:

- [ThreadPerMessageExample.java](./esempi/ThreadPerMessageExample.java): Implementazione del pattern Thread-per-Message
- [WorkerThreadExample.java](./esempi/WorkerThreadExample.java): Sistema di elaborazione con worker thread
- [ReadWriteLockPatternExample.java](./esempi/ReadWriteLockPatternExample.java): Implementazione personalizzata del pattern Read-Write Lock
- [BarrierExample.java](./esempi/BarrierExample.java): Sincronizzazione di thread con CyclicBarrier

## Esercizi

1. Implementare un sistema di elaborazione di eventi utilizzando il pattern Thread-per-Message
2. Creare un pool di worker thread personalizzato
3. Sviluppare una cache concorrente utilizzando il pattern Read-Write Lock
4. Implementare il pattern Active Object per un servizio di messaggistica asincrona

## Navigazione

- [Indice del Corso](../README.md)
- Precedente: [Atomic Variables](../07-AtomicVariables/README.md)
- Successivo: [Debugging e Performance](../09-DebuggingPerformance/README.md)