# Deadlock e Starvation

In questa sezione, esploreremo due problemi critici nella programmazione concorrente: deadlock e starvation. Comprenderemo le loro cause, come rilevarli e le strategie per prevenirli e risolverli.

## Cause e condizioni per il deadlock

Un deadlock si verifica quando due o più thread si bloccano a vicenda, ciascuno in attesa che l'altro rilasci una risorsa. Per avere un deadlock, devono verificarsi simultaneamente quattro condizioni (note come condizioni di Coffman):

1. **Mutua esclusione**: Almeno una risorsa deve essere detenuta in modalità non condivisibile.
2. **Hold and wait**: Un thread deve mantenere almeno una risorsa mentre è in attesa di risorse aggiuntive.
3. **No preemption**: Le risorse non possono essere forzatamente rilasciate.
4. **Attesa circolare**: Deve esistere un insieme di thread in attesa circolare l'uno dell'altro.

Ecco un esempio classico di deadlock in Java:

```java
public class DeadlockExample {
    private static final Object RISORSA_1 = new Object();
    private static final Object RISORSA_2 = new Object();
    
    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            synchronized (RISORSA_1) {
                System.out.println("Thread 1: Ha ottenuto RISORSA_1");
                try {
                    Thread.sleep(100); // Ritardo per aumentare la probabilità di deadlock
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Thread 1: In attesa di RISORSA_2");
                synchronized (RISORSA_2) {
                    System.out.println("Thread 1: Ha ottenuto RISORSA_2");
                }
            }
        });
        
        Thread thread2 = new Thread(() -> {
            synchronized (RISORSA_2) { // Ordine inverso rispetto al thread1
                System.out.println("Thread 2: Ha ottenuto RISORSA_2");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Thread 2: In attesa di RISORSA_1");
                synchronized (RISORSA_1) {
                    System.out.println("Thread 2: Ha ottenuto RISORSA_1");
                }
            }
        });
        
        thread1.start();
        thread2.start();
    }
}
```

## Rilevamento e prevenzione dei deadlock

### Rilevamento dei deadlock

#### 1. Utilizzo di jstack

```bash
jps # Per trovare il PID del processo Java
jstack <pid> # Per generare un thread dump
```

Nel thread dump, cercate sezioni che indicano "deadlock found".

#### 2. Utilizzo di ThreadMXBean

```java
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.lang.management.ThreadInfo;

public class DeadlockDetector {
    public static void checkForDeadlocks() {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        long[] deadlockedThreads = threadMXBean.findDeadlockedThreads();
        
        if (deadlockedThreads != null) {
            ThreadInfo[] threadInfos = threadMXBean.getThreadInfo(deadlockedThreads, true, true);
            System.out.println("Rilevato deadlock tra " + threadInfos.length + " thread!");
            
            for (ThreadInfo threadInfo : threadInfos) {
                System.out.println(threadInfo.getThreadName() + " è in attesa di " + 
                                  threadInfo.getLockName() + " posseduto da " + 
                                  threadInfo.getLockOwnerName());
            }
        } else {
            System.out.println("Nessun deadlock rilevato.");
        }
    }
}
```

### Prevenzione dei deadlock

#### 1. Ordine di acquisizione dei lock

Assicurarsi che tutti i thread acquisiscano i lock nello stesso ordine:

```java
public void metodoSicuro() {
    synchronized (RISORSA_1) {
        synchronized (RISORSA_2) {
            // Operazioni sicure
        }
    }
}
```

#### 2. Timeout nell'acquisizione dei lock

Utilizzare `tryLock()` con timeout per evitare attese indefinite:

```java
Lock lock1 = new ReentrantLock();
Lock lock2 = new ReentrantLock();

public void metodoConTimeout() {
    try {
        if (lock1.tryLock(1, TimeUnit.SECONDS)) {
            try {
                if (lock2.tryLock(1, TimeUnit.SECONDS)) {
                    try {
                        // Operazioni con entrambi i lock
                    } finally {
                        lock2.unlock();
                    }
                }
            } finally {
                lock1.unlock();
            }
        }
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }
}
```

#### 3. Utilizzo di lock gerarchici

Implementare una gerarchia di lock per imporre un ordine naturale di acquisizione.

#### 4. Evitare lock nidificati

Quando possibile, evitare di acquisire un lock mentre si detiene già un altro lock.

## Starvation e livelock

### Starvation

La starvation si verifica quando un thread non può accedere alle risorse necessarie per procedere, spesso perché altri thread monopolizzano tali risorse.

#### Cause della starvation

1. **Priorità dei thread**: Thread con priorità più alta ottengono sempre l'accesso alle risorse.
2. **Lock non equi**: Alcuni lock possono favorire certi thread rispetto ad altri.
3. **Operazioni I/O bloccanti**: Thread bloccati in operazioni I/O non possono competere per le risorse.

#### Esempio di starvation

```java
public class StarvationExample {
    private static final Object RISORSA = new Object();
    
    public static void main(String[] args) {
        // Thread ad alta priorità che monopolizza la risorsa
        Thread threadAltaPriorita = new Thread(() -> {
            while (true) {
                synchronized (RISORSA) {
                    try {
                        System.out.println("Thread ad alta priorità ha la risorsa");
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                // Rilascia la risorsa solo per un tempo brevissimo
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        
        // Thread a bassa priorità che raramente ottiene la risorsa
        Thread threadBassaPriorita = new Thread(() -> {
            while (true) {
                synchronized (RISORSA) {
                    System.out.println("Thread a bassa priorità finalmente ha la risorsa!");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        
        threadAltaPriorita.setPriority(Thread.MAX_PRIORITY);
        threadBassaPriorita.setPriority(Thread.MIN_PRIORITY);
        
        threadBassaPriorita.start();
        threadAltaPriorita.start();
    }
}
```

### Livelock

Un livelock è simile a un deadlock, ma i thread coinvolti cambiano continuamente il loro stato in risposta all'altro thread, senza fare progressi effettivi.

#### Esempio di livelock

```java
public class LivelockExample {
    static class Persona {
        private String nome;
        private boolean isAffamato;
        
        public Persona(String nome) {
            this.nome = nome;
            this.isAffamato = true;
        }
        
        public String getNome() {
            return nome;
        }
        
        public boolean isAffamato() {
            return isAffamato;
        }
        
        public void mangia(Posata posata, Persona altraPersona) {
            while (isAffamato) {
                // Se l'altra persona è affamata, cede la posata
                if (altraPersona.isAffamato()) {
                    System.out.println(nome + ": Cedo la posata a " + altraPersona.getNome());
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
                
                // Altrimenti mangia
                posata.usaPosata();
                System.out.println(nome + ": Ho mangiato!");
                isAffamato = false;
                System.out.println(nome + ": Ho ceduto la posata a " + altraPersona.getNome());
            }
        }
    }
    
    static class Posata {
        private String nome;
        
        public Posata(String nome) {
            this.nome = nome;
        }
        
        public void usaPosata() {
            System.out.println(nome + " è stata usata.");
        }
    }
    
    public static void main(String[] args) {
        final Persona alice = new Persona("Alice");
        final Persona bob = new Persona("Bob");
        final Posata posata = new Posata("Posata");
        
        new Thread(() -> alice.mangia(posata, bob)).start();
        new Thread(() -> bob.mangia(posata, alice)).start();
    }
}
```

## Strategie di risoluzione

### Per la starvation

1. **Lock equi**: Utilizzare `ReentrantLock(true)` per creare lock equi.

```java
private final Lock lock = new ReentrantLock(true); // Lock equo
```

2. **Gestione delle priorità**: Evitare differenze estreme nelle priorità dei thread.

3. **Timeout**: Implementare timeout per evitare attese indefinite.

### Per il livelock

1. **Introdurre casualità**: Aggiungere elementi casuali per rompere i pattern di interazione.

```java
if (altraPersona.isAffamato() && Math.random() > 0.3) { // 30% di probabilità di non cedere
    System.out.println(nome + ": Cedo la posata a " + altraPersona.getNome());
    continue;
}
```

2. **Backoff esponenziale**: Aumentare progressivamente i tempi di attesa tra i tentativi.

3. **Mediatore**: Introdurre un mediatore che coordini l'accesso alle risorse condivise.

## Navigazione

- [Indice del Corso](../README.md)
- [Indice del Modulo](./README.md)
- Precedente: [Identificazione di Problemi di Concorrenza](./01-IdentificazioneProblemi.md)
- Successivo: [Race Condition e Problemi di Visibilità](./03-RaceCondition.md)