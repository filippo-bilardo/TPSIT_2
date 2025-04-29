# Lock e Condizioni

In questa esercitazione esploreremo l'API Lock introdotta in Java 5, che offre meccanismi di sincronizzazione più flessibili e potenti rispetto ai blocchi `synchronized` tradizionali.

L'interfaccia `Lock` e le sue implementazioni forniscono funzionalità avanzate come tentativi di acquisizione non bloccanti, timeout, fairness e la possibilità di separare i lock per la lettura e la scrittura. Le condizioni (oggetti `Condition`) offrono un'alternativa più flessibile ai metodi `wait()`, `notify()` e `notifyAll()`.

## Indice degli Argomenti Teorici

1. [Interfaccia Lock](./01-InterfacciaLock.md)
   - Limitazioni dei blocchi synchronized
   - Metodi dell'interfaccia Lock
   - Acquisizione e rilascio espliciti
   - Tentativi di acquisizione con timeout

2. [ReentrantLock](./02-ReentrantLock.md)
   - Lock rientranti
   - Modalità fair e unfair
   - Metodi di diagnostica
   - Pattern di utilizzo sicuro (try-finally)

3. [ReadWriteLock](./03-ReadWriteLock.md)
   - Concorrenza tra lettori e scrittori
   - ReentrantReadWriteLock
   - Downgrading e upgrading
   - Casi d'uso ottimali

4. [Condition](./04-Condition.md)
   - Interfaccia Condition
   - Confronto con wait/notify
   - Attesa condizionale
   - Segnalazione selettiva
   - Implementazione di buffer limitati

5. [StampedLock](./05-StampedLock.md)
   - Lock ottimistico per la lettura
   - Modalità di accesso
   - Conversione tra modalità
   - Confronto con ReadWriteLock

## Esempi Pratici

In questa sezione troverai esempi di codice che illustrano l'uso dei lock e delle condizioni:

- [ReentrantLockExample.java](./esempi/ReentrantLockExample.java): Utilizzo base di ReentrantLock
- [ReadWriteLockExample.java](./esempi/ReadWriteLockExample.java): Implementazione di una cache con ReadWriteLock
- [ConditionExample.java](./esempi/ConditionExample.java): Buffer limitato con Condition
- [StampedLockExample.java](./esempi/StampedLockExample.java): Accesso ottimistico con StampedLock

## Esercizi

1. Implementare un sistema di prenotazione concorrente utilizzando ReentrantLock
2. Creare una cache thread-safe con ReadWriteLock
3. Sviluppare un semaforo personalizzato utilizzando Lock e Condition
4. Confrontare le prestazioni di synchronized, ReentrantLock e StampedLock

## Navigazione

- [Indice del Corso](../README.md)
- Precedente: [Collezioni Concorrenti](../05-CollezioniConcorrenti/README.md)
- Successivo: [Atomic Variables](../07-AtomicVariables/README.md)