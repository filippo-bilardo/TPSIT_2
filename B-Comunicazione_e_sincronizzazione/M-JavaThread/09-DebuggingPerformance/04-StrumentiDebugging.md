# Strumenti di Debugging

Il debugging di applicazioni multi-thread è notevolmente più complesso rispetto alle applicazioni a singolo thread. In questa sezione, esploreremo gli strumenti disponibili in Java per analizzare e risolvere problemi di concorrenza.

## jstack e thread dump

Uno degli strumenti più basilari ma potenti per il debugging di applicazioni multi-thread è `jstack`, che permette di ottenere un thread dump dell'applicazione Java in esecuzione.

### Cos'è un thread dump

Un thread dump è un'istantanea dello stato di tutti i thread in un'applicazione Java in un determinato momento. Include informazioni come:

- Identificatore del thread
- Stato del thread (RUNNABLE, BLOCKED, WAITING, etc.)
- Stack trace completo
- Informazioni sui lock acquisiti o attesi

### Come ottenere un thread dump

#### Utilizzando jstack da riga di comando

```bash
jstack <pid>
```

Dove `<pid>` è l'ID del processo Java di cui si vuole ottenere il thread dump.

#### Programmaticamente in Java

```java
public class ThreadDumpExample {
    public static void main(String[] args) {
        // Ottiene tutti i thread attivi
        Map<Thread, StackTraceElement[]> stackTraces = Thread.getAllStackTraces();
        
        for (Map.Entry<Thread, StackTraceElement[]> entry : stackTraces.entrySet()) {
            Thread thread = entry.getKey();
            StackTraceElement[] stackElements = entry.getValue();
            
            System.out.println("Thread: " + thread.getName() + 
                               " (ID: " + thread.getId() + ")" +
                               " State: " + thread.getState());
            
            for (StackTraceElement element : stackElements) {
                System.out.println("\tat " + element);
            }
            System.out.println();
        }
    }
}
```

### Interpretazione di un thread dump

L'analisi di un thread dump richiede pratica, ma ecco alcuni pattern comuni da cercare:

1. **Thread nello stato BLOCKED**: Indicano potenziali deadlock o contesa per le risorse
2. **Thread nello stato WAITING o TIMED_WAITING**: Potrebbero indicare problemi di sincronizzazione
3. **Thread con stack trace identici**: Possono indicare colli di bottiglia

### Identificazione di deadlock

Un thread dump mostra chiaramente i deadlock, indicando quali thread sono bloccati e su quali risorse:

```
Found one Java-level deadlock:
=============================
"Thread-1":
  waiting to lock monitor 0x00007f9e4c00a1c8 (object 0x00000007d6aa2c98, a java.lang.Object),
  which is held by "Thread-0"

"Thread-0":
  waiting to lock monitor 0x00007f9e4c00c588 (object 0x00000007d6aa2ca8, a java.lang.Object),
  which is held by "Thread-1"
```

## VisualVM e JConsole

Java include strumenti grafici che facilitano il monitoraggio e l'analisi delle applicazioni Java.

### VisualVM

VisualVM è uno strumento visuale che integra diversi strumenti di monitoraggio JDK in un'unica interfaccia.

#### Funzionalità principali

1. **Monitoraggio in tempo reale**: CPU, memoria, thread, classi caricate
2. **Profiling**: Analisi dell'utilizzo di CPU e memoria
3. **Thread dump**: Visualizzazione e analisi dei thread dump
4. **Heap dump**: Analisi dell'utilizzo della memoria heap
5. **MBean browser**: Accesso alle informazioni di gestione JMX

#### Utilizzo per problemi di concorrenza

- Monitorare il numero di thread attivi
- Identificare thread bloccati o in attesa
- Analizzare l'utilizzo della CPU per thread
- Rilevare deadlock

### JConsole

JConsole è un'applicazione grafica più semplice ma comunque potente per il monitoraggio delle applicazioni Java.

#### Funzionalità principali

1. **Panoramica**: Informazioni generali sull'applicazione
2. **Memoria**: Monitoraggio dell'utilizzo della memoria
3. **Thread**: Visualizzazione e gestione dei thread
4. **Classi**: Monitoraggio delle classi caricate
5. **MBeans**: Accesso alle informazioni di gestione JMX

## Java Flight Recorder

Java Flight Recorder (JFR) è uno strumento di profiling a basso impatto che raccoglie dati diagnostici e di profiling dall'applicazione in esecuzione.

### Caratteristiche principali

1. **Basso overhead**: Impatto minimo sulle prestazioni dell'applicazione
2. **Registrazione continua**: Può essere eseguito in produzione
3. **Dati dettagliati**: Raccoglie informazioni su thread, allocazioni di memoria, operazioni I/O, etc.

### Come utilizzare JFR

#### Avvio di un'applicazione con JFR abilitato

```bash
java -XX:+FlightRecorder -XX:StartFlightRecording=duration=60s,filename=recording.jfr MyApplication
```

#### Avvio della registrazione su un'applicazione in esecuzione

```bash
jcmd <pid> JFR.start duration=60s filename=recording.jfr
```

### Analisi dei dati JFR

I file di registrazione JFR possono essere analizzati con Java Mission Control (JMC) o altri strumenti compatibili.

JMC fornisce visualizzazioni dettagliate di:

1. **Attività dei thread**: Identificazione di thread bloccati o in attesa
2. **Contesa di lock**: Rilevamento di problemi di sincronizzazione
3. **Allocazioni di memoria**: Analisi dell'utilizzo della memoria
4. **Operazioni I/O**: Identificazione di colli di bottiglia I/O

## Analizzatori statici di codice

Gli analizzatori statici di codice possono identificare potenziali problemi di concorrenza senza eseguire l'applicazione.

### SpotBugs (successore di FindBugs)

SpotBugs include un rilevatore di bug di concorrenza che può identificare:

1. **Inconsistent synchronization**: Accesso non sincronizzato a campi che sono a volte sincronizzati
2. **Lazy initialization of static fields**: Inizializzazione non thread-safe di campi statici
3. **Wait not in loop**: Chiamate a wait() non in un ciclo
4. **Double-checked locking**: Implementazioni errate del pattern double-checked locking

### ThreadSafe

ThreadSafe è un analizzatore specializzato per problemi di concorrenza che può rilevare:

1. **Race condition**: Accessi non sincronizzati a dati condivisi
2. **Deadlock**: Potenziali deadlock basati sull'ordine di acquisizione dei lock
3. **Thread safety violations**: Violazioni delle regole di thread-safety

## Tecniche di debugging avanzate

### Logging thread-safe

Implementare un sistema di logging thread-safe è essenziale per il debugging di applicazioni concorrenti:

```java
import java.util.concurrent.ConcurrentLinkedQueue;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ThreadSafeLogger {
    private static final ConcurrentLinkedQueue<String> logQueue = new ConcurrentLinkedQueue<>();
    private static final ThreadLocal<SimpleDateFormat> dateFormat = 
        ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"));
    
    public static void log(String message) {
        String threadName = Thread.currentThread().getName();
        String timestamp = dateFormat.get().format(new Date());
        logQueue.add(String.format("[%s] [%s] %s", timestamp, threadName, message));
    }
    
    public static void dumpLogs() {
        for (String log : logQueue) {
            System.out.println(log);
        }
    }
}
```

### Debugging con breakpoint condizionali

I moderni IDE supportano breakpoint condizionali che possono essere attivati solo in determinate condizioni, utili per il debugging di problemi specifici di thread.

Esempio in IntelliJ IDEA:
1. Impostare un breakpoint
2. Fare clic con il tasto destro sul breakpoint
3. Selezionare "Condizione" e inserire una condizione come `Thread.currentThread().getName().equals("Thread-1")`

## Navigazione

- [Indice del Corso](../README.md)
- [Indice del Modulo](./README.md)
- Precedente: [Race Condition e Problemi di Visibilità](./03-RaceCondition.md)
- Successivo: [Ottimizzazione delle Prestazioni](./05-OttimizzazionePrestazioni.md)