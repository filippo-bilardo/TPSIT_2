# Collezioni Concorrenti

In questa esercitazione esploreremo le collezioni concorrenti fornite dal package `java.util.concurrent`, progettate specificamente per l'uso in ambienti multi-thread.

Le collezioni standard di Java (come ArrayList, HashMap, ecc.) non sono thread-safe o, se lo sono (come Vector o Hashtable), utilizzano meccanismi di sincronizzazione che possono limitare le prestazioni in scenari di alta concorrenza. Le collezioni concorrenti risolvono questi problemi offrendo implementazioni ottimizzate per l'accesso concorrente.

## Indice degli Argomenti Teorici

1. [Introduzione alle Collezioni Concorrenti](./01-IntroduzioneCollezioniConcorrenti.md)
   - Problemi delle collezioni sincronizzate tradizionali
   - Concorrenza fine-grained vs coarse-grained
   - Lock-free e wait-free algorithms
   - Quando usare le collezioni concorrenti

2. [ConcurrentHashMap](./02-ConcurrentHashMap.md)
   - Struttura interna e funzionamento
   - Confronto con HashMap e Hashtable
   - Operazioni atomiche e aggregate
   - Iterazione sicura

3. [CopyOnWriteArrayList e CopyOnWriteArraySet](./03-CopyOnWriteCollections.md)
   - Semantica copy-on-write
   - Casi d'uso ideali
   - Vantaggi e svantaggi
   - Confronto con ArrayList e Vector

4. [BlockingQueue e Implementazioni](./04-BlockingQueue.md)
   - Interfaccia BlockingQueue
   - ArrayBlockingQueue
   - LinkedBlockingQueue
   - PriorityBlockingQueue
   - DelayQueue e SynchronousQueue
   - Applicazioni nel pattern produttore-consumatore

5. [Altre Collezioni Thread-Safe](./05-AltreCollezioni.md)
   - ConcurrentSkipListMap e ConcurrentSkipListSet
   - LinkedTransferQueue
   - ConcurrentLinkedQueue e ConcurrentLinkedDeque
   - Collezioni non bloccanti

## Esempi Pratici

In questa sezione troverai esempi di codice che illustrano l'uso delle collezioni concorrenti:

- [ConcurrentMapExample.java](./esempi/ConcurrentMapExample.java): Utilizzo di ConcurrentHashMap
- [CopyOnWriteExample.java](./esempi/CopyOnWriteExample.java): Esempi di CopyOnWriteArrayList
- [BlockingQueueExample.java](./esempi/BlockingQueueExample.java): Implementazione di un sistema produttore-consumatore
- [SkipListExample.java](./esempi/SkipListExample.java): Utilizzo di ConcurrentSkipListMap

## Esercizi

1. Implementare un cache concorrente utilizzando ConcurrentHashMap
2. Creare un sistema di log thread-safe con CopyOnWriteArrayList
3. Sviluppare un'applicazione di elaborazione di eventi con BlockingQueue
4. Confrontare le prestazioni di diverse collezioni concorrenti in scenari di alta concorrenza

## Navigazione

- [Indice del Corso](../README.md)
- Precedente: [Thread Pool e Executor](../04-ThreadPoolExecutor/README.md)
- Successivo: [Lock e Condizioni](../06-LockCondizioni/README.md)