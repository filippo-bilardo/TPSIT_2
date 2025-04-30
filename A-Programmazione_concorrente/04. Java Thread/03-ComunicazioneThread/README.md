# Comunicazione tra Thread

## Descrizione

Questo modulo esplora i meccanismi di comunicazione tra thread in Java. La comunicazione efficace tra thread è fondamentale per coordinare le attività e condividere dati in modo sicuro. In questo modulo, imparerai come utilizzare i metodi di sincronizzazione di Java per implementare pattern di comunicazione comuni come il produttore-consumatore.

## Indice degli Argomenti Teorici

1. [Metodi di Comunicazione](./01-MetodiComunicazione.md)
   - Metodi `wait()`, `notify()` e `notifyAll()`
   - Meccanismo di attesa e notifica
   - Condizioni di attesa
   - Gestione delle interruzioni

2. [Pattern Produttore-Consumatore](./02-ProduttoreConsumatore.md)
   - Implementazione del buffer condiviso
   - Sincronizzazione produttore-consumatore
   - Gestione del buffer pieno/vuoto
   - Varianti del pattern

3. [Scambio di Dati](./03-ScambioDati.md)
   - Tecniche sicure per lo scambio di dati
   - Immutabilità e thread safety
   - Passaggio di messaggi
   - Callback e listener

## Esercitazioni Pratiche

1. **BufferCondiviso**: Implementa un buffer thread-safe per lo scambio di dati.
   - File: [BufferCondiviso.java](./esempi/BufferCondiviso.java)

2. **ProduttoreConsumatore**: Implementa il classico pattern produttore-consumatore.
   - File: [ProduttoreConsumatore.java](./esempi/ProduttoreConsumatore.java)

3. **EventDispatcher**: Crea un sistema di eventi con publisher e subscriber.
   - File: [EventDispatcher.java](./esempi/EventDispatcher.java)

4. **WorkerPool**: Implementa un pool di worker thread che elaborano task da una coda condivisa.
   - File: [WorkerPool.java](./esempi/WorkerPool.java)

## Sfide

1. **Buffer Limitato**: Implementa un buffer con capacità limitata dove i produttori attendono quando il buffer è pieno e i consumatori attendono quando è vuoto.
2. **Semaforo**: Implementa un semaforo utilizzando i meccanismi di wait/notify.
3. **Barriera Ciclica**: Crea una barriera che permette a un gruppo di thread di attendere che tutti raggiungano un punto comune prima di procedere.

## Navigazione del Corso
- [📑 Indice](../README.md)
- [⬅️ Sincronizzazione](../02-Sincronizzazione/README.md)
- [➡️ Thread Pool e Executor](../04-ThreadPoolExecutor/README.md)