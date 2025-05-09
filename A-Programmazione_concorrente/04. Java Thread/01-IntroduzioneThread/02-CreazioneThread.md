# Creazione di Thread in Java

## Introduzione

In Java, esistono principalmente due modi per creare e avviare un thread:

1. Estendere la classe `Thread`
2. Implementare l'interfaccia `Runnable`

Entrambi gli approcci hanno i loro vantaggi e svantaggi. In questo capitolo, esploreremo entrambi i metodi e vedremo anche come utilizzare le classi anonime e le espressioni lambda (introdotte in Java 8) per creare thread in modo più conciso.

## 1. Estendere la Classe Thread

Il primo approccio consiste nell'estendere la classe `Thread` e sovrascrivere il metodo `run()`.

### Esempio Base

```java
public class MioThread extends Thread {
    @Override
    public void run() {
        System.out.println("Il thread è in esecuzione!");
        // Codice da eseguire nel thread
    }
}
 
public class TestCreazioneThread {
    public static void main(String[] args) {
        MioThread thread = new MioThread();
        thread.start(); // Avvia il thread
    }
}
```

### Punti Chiave

- Il metodo `run()` contiene il codice che verrà eseguito nel nuovo thread
- Il metodo `start()` avvia effettivamente il thread, creando un nuovo thread di esecuzione
- **Non chiamare direttamente** il metodo `run()`, altrimenti il codice verrà eseguito nel thread corrente
- Ogni oggetto `Thread` può essere avviato una sola volta

### Vantaggi

- Approccio diretto e intuitivo
- Accesso diretto ai metodi della classe `Thread` (come `getName()`, `getPriority()`, ecc.)

### Svantaggi

- Java non supporta l'ereditarietà multipla, quindi se la classe estende già un'altra classe, non può estendere anche `Thread`
- Mescola il "cosa fare" (logica di business) con il "come farlo" (meccanismo di threading)

## 2. Implementare l'Interfaccia Runnable

Il secondo approccio, generalmente preferito, consiste nell'implementare l'interfaccia `Runnable` e passare l'oggetto a un'istanza di `Thread`.

### Esempio Base

```java
public class MioRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("Il thread è in esecuzione!");
        // Codice da eseguire nel thread
    }
}

public class TestCreazioneThread {
    public static void main(String[] args) {
        //Thread thread1 = new Thread(new MioRunnable());
        MioRunnable runnable = new MioRunnable();
        Thread thread1 = new Thread(runnable);
        thread1.start(); // Avvia il thread
    }
}
```

### Punti Chiave

- L'interfaccia `Runnable` ha un solo metodo: `run()`
- L'oggetto `Runnable` viene passato al costruttore di `Thread`
- È ancora necessario chiamare `start()` sull'oggetto `Thread` per avviare un nuovo thread

### Vantaggi

- Separa il "cosa fare" (l'implementazione di `Runnable`) dal "come farlo" (la classe `Thread`)
- La classe può estendere un'altra classe se necessario
- Lo stesso oggetto `Runnable` può essere utilizzato da più thread
- Più adatto al pattern di design orientato agli oggetti

### Svantaggi

- Sintassi leggermente più verbosa
- Nessun accesso diretto ai metodi di `Thread` (è necessario utilizzare `Thread.currentThread()`)


## 3. Utilizzare una classe anonima

Le classi anonime in Java permettono di definire e istanziare una classe in un'unica operazione. Questa tecnica è particolarmente utile quando si devono implementare interfacce come Runnable per un uso unico o molto limitato.

### Esempio Base
```java
public class Main {
    public static void main(String[] args) {
        // Creazione di un thread utilizzando una classe anonima che implementa Runnable
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Thread con classe anonima in esecuzione");
                // Qui possiamo inserire tutto il codice che vogliamo eseguire nel thread
            }
        });
        thread.start();
        
        // Possiamo anche estendere direttamente Thread con una classe anonima
        Thread threadExtended = new Thread() {
            @Override
            public void run() {
                System.out.println("Thread esteso con classe anonima in esecuzione");
            }
        };
        threadExtended.start();
    }
}
```

### Vantaggi dell'utilizzo di classi anonime:

- **Concisione**: Non è necessario creare una classe separata per un'implementazione di Runnable che verrà utilizzata una sola volta
- **Scope**: La classe anonima ha accesso alle variabili locali del metodo in cui è definita (se dichiarate final o effettivamente finali)
- **Incapsulamento**: Il codice relativo al thread è definito esattamente dove viene utilizzato, migliorando la leggibilità

### Limitazioni:

- La classe anonima non può avere costruttori espliciti
- Non può implementare/estendere più di una classe o interfaccia
- Non può essere riutilizzata in altre parti del codice

## 4. Utilizzo di Lambda Expressions (Java 8+)

Con Java 8, è possibile utilizzare le [espressioni lambda](<LambdaExpression.md>) per implementare l'interfaccia `Runnable` in modo più conciso, poiché `Runnable` è un'interfaccia funzionale (ha un solo metodo astratto).

### Esempio Base

```java
public class ThreadLambda {
    public static void main(String[] args) {
        // Utilizzo di lambda expression
        Thread thread = new Thread(() -> {
            System.out.println("Il thread è in esecuzione con lambda!");
            // Codice da eseguire nel thread
        });
        
        thread.start();
    }
}
```

### Vantaggi delle Lambda

- Sintassi più concisa e leggibile
- Ideale per implementazioni semplici
- Riduce il boilerplate code

## Confronto tra i Due Approcci

| Caratteristica | Estendere Thread | Implementare Runnable |
|----------------|------------------|------------------------|
| Ereditarietà | Consuma l'unica possibilità di ereditarietà | Permette di estendere altre classi |
| Riutilizzo | Un'istanza di Thread può essere avviata una sola volta | Lo stesso Runnable può essere usato da più thread |
| Condivisione stato | Più difficile condividere stato tra thread | Più facile condividere stato tra thread |
| Accesso ai metodi Thread | Diretto | Tramite Thread.currentThread() |
| Design OOP | Meno flessibile | Più flessibile |

## Quale metodo scegliere?

- Estendi `Thread` quando hai bisogno di modificare il comportamento del thread stesso
- Implementa `Runnable` quando vuoi solo definire un'attività da eseguire in un thread
- Usa classi anonime per implementazioni one-off di Runnable più complesse
- Usa espressioni lambda per implementazioni concise e semplici (Java 8+)

## Best Practices

1. **Preferire l'interfaccia Runnable**: In generale, implementare `Runnable` è considerato un approccio migliore perché separa il compito dall'esecuzione.

2. **Utilizzare pool di thread**: Per applicazioni reali, è consigliabile utilizzare un pool di thread (che vedremo in moduli successivi) anziché creare thread individuali.

3. **Gestire le eccezioni**: Assicurarsi di gestire correttamente le eccezioni all'interno del metodo `run()`, poiché non verranno propagate al thread chiamante.

4. **Evitare thread eccessivi**: Creare troppi thread può degradare le prestazioni a causa dell'overhead di gestione.

5. **Dare nomi significativi ai thread**: Utilizzare il costruttore `Thread(Runnable r, String name)` per assegnare nomi significativi ai thread, utili per il debugging.

```java
Thread thread = new Thread(runnable, "ThreadDownload");
```

## Esempi Pratici

### Thread con Parametri

```java
public class ThreadConParametri implements Runnable {
    private String messaggio;
    private int ripetizioni;
    
    public ThreadConParametri(String messaggio, int ripetizioni) {
        this.messaggio = messaggio;
        this.ripetizioni = ripetizioni;
    }
    
    @Override
    public void run() {
        System.out.println("Avvio Thread " + Thread.currentThread().getName());
        for (int i = 0; i < ripetizioni; i++) {
            System.out.println(messaggio + " (" + (i+1) + "/" + ripetizioni + ")");
            try {
                Thread.sleep(1000); // Pausa di 1 secondo
            } catch (InterruptedException e) {
                System.out.println("Thread interrotto");
                return;
            }
        }
    }
    
    public static void main(String[] args) {
        ThreadConParametri runnable1 = new ThreadConParametri("Primo thread", 5);
        Thread t1 = new Thread(runnable1, "Thread1");
        Thread t2 = new Thread(new ThreadConParametri("Secondo thread", 3), "Thread2");
        
        t1.start();
        t2.start();
    }
}
```

### Thread Anonimo

```java
public class ThreadAnonimo {
    public static void main(String[] args) {
        // Thread anonimo con classe anonima
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Thread anonimo in esecuzione");
            }
        });
        
        thread.start();
        
        // Con Java 8+ si può usare la lambda expression
        new Thread(() -> System.out.println("Thread lambda in esecuzione")).start();
    }
}
```

Nel prossimo capitolo, esploreremo il ciclo di vita dei thread e come controllarlo attraverso i metodi forniti dalla classe `Thread`.

## Navigazione del Corso
- [📑 Indice](../README.md)
- [⬅️ Concetti Base dei Thread](./01-ConcettiBase.md)
- [➡️ Ciclo di Vita di un Thread](./03-CicloVita.md)