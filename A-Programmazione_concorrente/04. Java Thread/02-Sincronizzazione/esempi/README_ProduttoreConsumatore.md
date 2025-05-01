# Pattern Produttore-Consumatore in Java

Questo progetto contiene diverse implementazioni del classico problema di sincronizzazione Produttore-Consumatore in Java, utilizzando vari meccanismi di sincronizzazione.

## Descrizione del Problema

Il problema Produttore-Consumatore è un classico esempio di sincronizzazione tra thread. Consiste in:

- Uno o più **produttori** che generano dati e li inseriscono in un buffer condiviso
- Uno o più **consumatori** che prelevano i dati dal buffer condiviso e li elaborano
- Un **buffer condiviso** con capacità limitata o illimitata

La sfida principale è garantire che:
1. I produttori non inseriscano dati quando il buffer è pieno
2. I consumatori non prelevino dati quando il buffer è vuoto
3. L'accesso al buffer avvenga in modo thread-safe (senza race condition)

## Implementazioni Disponibili

### 1. Versione Base con synchronized, wait() e notify()

**File:** `ES04_ProduttoreConsumatore.java`

Implementazione base che utilizza i meccanismi di sincronizzazione nativi di Java:
- `synchronized` per garantire l'accesso esclusivo al buffer
- `wait()` per mettere in attesa i thread quando non possono procedere
- `notify()` per risvegliare i thread in attesa quando le condizioni cambiano

Questa versione implementa un buffer con capacità singola (un solo elemento).

### 2. Versione Avanzata con buffer a capacità multipla

**File:** `ES04_ProduttoreConsumatore_Avanzato.java`

Estensione della versione base con:
- Buffer a capacità configurabile
- Supporto per più produttori e consumatori
- Statistiche di esecuzione (elementi prodotti/consumati, attese)
- Visualizzazione dello stato del buffer

### 3. Versione con ReentrantLock e Condition

**File:** `ES04_ProduttoreConsumatore_ReentrantLock.java`

Implementazione che utilizza le classi del package `java.util.concurrent.locks`:
- `ReentrantLock` per il controllo dell'accesso esclusivo
- `Condition` per gestire le condizioni di attesa (buffer pieno/vuoto)

Vantaggi rispetto a synchronized:
- Possibilità di interrompere l'attesa
- Supporto per timeout nell'acquisizione del lock
- Possibilità di avere più condizioni di attesa
- Fairness configurabile (ordine FIFO per i thread in attesa)

### 4. Versione con BlockingQueue

**File:** `ES04_ProduttoreConsumatore_BlockingQueue.java`

Implementazione che utilizza `ArrayBlockingQueue`, una coda thread-safe con operazioni bloccanti:
- `put()` blocca se la coda è piena
- `take()` blocca se la coda è vuota

Questa è la soluzione più semplice e concisa, poiché la sincronizzazione è gestita internamente dalla coda.

### 5. Versione con Semaphore

**File:** `ES04_ProduttoreConsumatore_Semaphore.java`

Implementazione che utilizza i semafori per controllare l'accesso al buffer:
- Un semaforo per i posti disponibili nel buffer
- Un semaforo per i dati disponibili nel buffer
- Un semaforo binario (mutex) per garantire l'accesso esclusivo

Questa implementazione è più vicina all'approccio classico dei sistemi operativi per risolvere il problema.

## Come Eseguire gli Esempi

Per eseguire ciascun esempio, compilare il file Java corrispondente e avviare la classe principale:

```bash
javac ES04_ProduttoreConsumatore.java
java ES04_ProduttoreConsumatore
```

Ripetere per gli altri file sostituendo il nome del file.

## Confronto tra le Implementazioni

| Implementazione | Vantaggi | Svantaggi |
|-----------------|----------|------------|
| synchronized/wait/notify | Semplice, nativo in Java | Meno flessibile, possibili falsi risvegli |
| ReentrantLock/Condition | Più flessibile, interrompibile | Più verboso, richiede gestione esplicita del lock |
| BlockingQueue | Molto conciso, facile da usare | Meno controllo sui dettagli di implementazione |
| Semaphore | Controllo preciso sull'accesso | Più complesso da implementare correttamente |

## Conclusioni

La scelta dell'implementazione dipende dalle esigenze specifiche:

- Per casi semplici: `BlockingQueue` è la soluzione più immediata
- Per maggiore controllo: `ReentrantLock` con `Condition`
- Per compatibilità con codice legacy: `synchronized` con `wait`/`notify`
- Per implementazioni più vicine ai concetti teorici: `Semaphore`

Tutte le implementazioni risolvono lo stesso problema, ma con approcci diversi che riflettono l'evoluzione delle API di concorrenza in Java.