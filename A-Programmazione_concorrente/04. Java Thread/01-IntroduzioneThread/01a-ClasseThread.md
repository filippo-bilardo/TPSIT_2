# La classe Thread di Java

La classe `Thread` è uno dei componenti fondamentali della programmazione concorrente in Java. Questa classe permette di creare ed eseguire thread indipendenti all'interno della Java Virtual Machine (JVM), consentendo lo sviluppo di applicazioni che possono eseguire più attività contemporaneamente.

## Concetti fondamentali

Prima di esaminare nel dettaglio la classe `Thread`, è importante comprendere alcuni concetti di base:

- Un **thread** rappresenta un flusso di esecuzione indipendente all'interno di un programma
- Il **multithreading** è la capacità di un programma di eseguire più thread contemporaneamente
- La **concorrenza** si riferisce alla capacità di gestire più attività che progrediscono contemporaneamente

## Costruttori principali

- `Thread()`: Crea un nuovo thread senza specificare un'attività da eseguire
- `Thread(String name)`: Crea un nuovo thread con un nome specifico
- `Thread(Runnable target)`: Crea un thread che eseguirà l'implementazione del metodo `run()` dell'oggetto `Runnable` fornito
- `Thread(Runnable target, String name)`: Crea un thread con un nome specifico che eseguirà l'oggetto `Runnable` fornito

## Attributi principali

- `name`: Il nome del thread, utile per identificarlo durante il debug
- `priority`: La priorità del thread (valori da 1 a 10, con 5 come default)
- `daemon`: Flag che indica se il thread è un daemon thread (thread di servizio)
- `id`: Identificatore univoco del thread, assegnato automaticamente dal sistema
- `state`: Lo stato attuale del thread (NEW, RUNNABLE, BLOCKED, WAITING, TIMED_WAITING, TERMINATED).

## Metodi principali

### Metodi di controllo del ciclo di vita
- `start()`: Avvia l'esecuzione del thread, invocando il metodo `run()`
- `run()`: Contiene il codice da eseguire nel thread (da sovrascrivere)
- `sleep(long millis)`: Mette in pausa il thread corrente per il tempo specificato in millisecondi
- `join()`: Attende il completamento del thread
- `join(long millis)`: Attende il completamento del thread per un massimo di millisecondi specificati
- `interrupt()`: Interrompe un thread, impostando il suo flag di interruzione.

### Metodi di interrogazione
- `isAlive()`: Verifica se il thread è attivo
- `getState()`: Restituisce lo stato attuale del thread
- `getName()`: Ottiene il nome del thread
- `getPriority()`: Ottiene la priorità del thread
- `isDaemon()`: Verifica se il thread è un daemon thread
- `getId()`: Ottiene l'identificatore univoco del thread

### Metodi di configurazione
- `setName(String name)`: Imposta il nome del thread
- `setPriority(int priority)`: Imposta la priorità del thread
- `setDaemon(boolean isDaemon)`: Imposta il thread come daemon thread (deve essere chiamato prima di `start()`)

### Metodi statici utili
- `Thread.currentThread()`: Restituisce il riferimento al thread corrente in esecuzione
- `Thread.yield()`: Suggerisce allo scheduler di concedere temporaneamente la CPU ad altri thread
- `Thread.sleep(long millis)`: Mette in pausa il thread corrente per il tempo specificato

## Stati di un thread

Un thread può trovarsi in uno dei seguenti stati durante il suo ciclo di vita:

1. **NEW**: Il thread è stato creato ma non ancora avviato
2. **RUNNABLE**: Il thread è in esecuzione o pronto per essere eseguito
3. **BLOCKED**: Il thread è bloccato in attesa di un monitor lock
4. **WAITING**: Il thread è in attesa indefinita che un altro thread esegua una particolare azione
5. **TIMED_WAITING**: Il thread è in attesa per un periodo di tempo specificato
6. **TERMINATED**: Il thread ha completato la sua esecuzione

## Domande di autovalutazione

1. Quale dei seguenti costruttori della classe Thread non esiste?
   a) Thread()
   b) Thread(String name)
   c) Thread(int priority)
   d) Thread(Runnable target)

2. Cosa succede se si chiama direttamente il metodo run() invece di start()?
   a) Il thread viene eseguito normalmente
   b) Il codice viene eseguito nel thread chiamante, non in un nuovo thread
   c) Viene lanciata un'eccezione
   d) Il thread viene avviato ma con priorità minima

3. Quale metodo permette di attendere il completamento di un thread?
   a) wait()
   b) sleep()
   c) join()
   d) yield()

4. Quale di questi stati non è uno stato valido per un thread in Java?
   a) RUNNABLE
   b) BLOCKED
   c) PAUSED
   d) TERMINATED

5. Come si può ottenere un riferimento al thread corrente?
   a) Thread.getThread()
   b) Thread.currentThread()
   c) this.getThread()
   d) Thread.getRunningThread()

## Risposte corrette

1. c) Thread(int priority) - Non esiste un costruttore che accetta direttamente la priorità come parametro.
2. b) Il codice viene eseguito nel thread chiamante, non in un nuovo thread.
3. c) join()
4. c) PAUSED - Gli stati validi sono: NEW, RUNNABLE, BLOCKED, WAITING, TIMED_WAITING e TERMINATED.
5. b) Thread.currentThread()