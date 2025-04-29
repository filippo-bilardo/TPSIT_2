# Corso sui Thread in Java

## Introduzione

Benvenuti al corso completo sui Thread in Java! Questo corso è progettato per guidarvi attraverso il mondo della programmazione concorrente in Java, partendo dai concetti base fino ad arrivare a tecniche avanzate.

La programmazione multi-thread è fondamentale nello sviluppo di applicazioni moderne, permettendo di eseguire più operazioni contemporaneamente e sfruttare al meglio le risorse hardware disponibili. Attraverso una serie di esercitazioni pratiche e spiegazioni teoriche, imparerete a:

- Creare e gestire thread in Java
- Sincronizzare l'accesso a risorse condivise
- Utilizzare le classi di concorrenza del package `java.util.concurrent`
- Implementare pattern di programmazione concorrente
- Evitare problemi comuni come deadlock e race condition

Ogni esercitazione è progettata per introdurre gradualmente nuovi concetti, permettendovi di consolidare le conoscenze acquisite prima di passare all'argomento successivo.

## Indice delle Esercitazioni

1. [Introduzione ai Thread](./01-IntroduzioneThread/README.md)
   - Concetti base dei thread
   - Creazione di thread in Java
   - Ciclo di vita di un thread

2. [Sincronizzazione](./02-Sincronizzazione/README.md)
   - Problemi di concorrenza
   - Keyword `synchronized`
   - Metodi e blocchi sincronizzati
   - Monitor e lock

3. [Comunicazione tra Thread](./03-ComunicazioneThread/README.md)
   - Metodi `wait()`, `notify()` e `notifyAll()`
   - Produttore-Consumatore
   - Scambio di dati tra thread

4. [Thread Pool e Executor](./04-ThreadPoolExecutor/README.md)
   - Framework Executor
   - Pool di thread
   - Callable e Future
   - CompletableFuture

5. [Collezioni Concorrenti](./05-CollezioniConcorrenti/README.md)
   - ConcurrentHashMap
   - CopyOnWriteArrayList
   - BlockingQueue
   - Altre collezioni thread-safe

6. [Lock e Condizioni](./06-LockCondizioni/README.md)
   - Interface Lock
   - ReentrantLock
   - ReadWriteLock
   - Condition

7. [Atomic Variables](./07-AtomicVariables/README.md)
   - Operazioni atomiche
   - Classi Atomic
   - Compare-and-Swap (CAS)

8. [Pattern Concorrenti](./08-PatternConcorrenti/README.md)
   - Thread-per-message
   - Worker Thread
   - Read-Write Lock
   - Altri pattern

9. [Debugging e Performance](./09-DebuggingPerformance/README.md)
   - Identificazione di problemi di concorrenza
   - Deadlock e starvation
   - Ottimizzazione delle prestazioni
   - Strumenti di profiling

10. [Progetto Finale](./10-ProgettoFinale/README.md)
    - Applicazione completa multi-thread
    - Best practices
    - Revisione dei concetti appresi

## Requisiti

Per seguire questo corso è necessario:

- Conoscenza base di Java (sintassi, OOP, collezioni)
- JDK 8 o superiore installato
- Un IDE Java (Eclipse, IntelliJ IDEA, NetBeans, ecc.)

## Come Utilizzare Questo Corso

Ogni cartella contiene:

1. Un file README.md con la descrizione dell'esercitazione e l'indice degli argomenti teorici
2. File sorgente Java con esempi commentati
3. Guide teoriche dettagliate per ogni argomento
4. Esercizi pratici con soluzioni

Si consiglia di seguire le esercitazioni nell'ordine proposto, poiché ogni modulo si basa sulle conoscenze acquisite nei moduli precedenti.

Buono studio!