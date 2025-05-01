## Implementare l'Interfaccia Runnable

Il secondo approccio per creare thread in Java consiste nell'implementare l'interfaccia `Runnable` e passare l'oggetto a un'istanza di `Thread`. Questo metodo è generalmente preferito rispetto all'estensione della classe `Thread`.

### Esempio Base

```java
public class MioRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("Il thread è in esecuzione!");
        // Codice da eseguire nel thread
    }
    
    public static void main(String[] args) {
        MioRunnable runnable = new MioRunnable();
        Thread thread = new Thread(runnable);
        thread.start(); // Avvia il thread
    }
}
```

### Punti Chiave
- L'interfaccia `Runnable` ha un solo metodo: `run()`
- L'oggetto `Runnable` viene passato al costruttore di `Thread`
- È necessario chiamare `start()` sull'oggetto `Thread` per avviare un nuovo thread

### Vantaggi
- Separa il "cosa fare" (l'implementazione di `Runnable`) dal "come farlo" (la classe `Thread`)
- La classe può estendere un'altra classe se necessario
- Lo stesso oggetto `Runnable` può essere utilizzato da più thread
- Più adatto al design orientato agli oggetti

### Svantaggi
- Sintassi leggermente più verbosa
- Nessun accesso diretto ai metodi di `Thread` (è necessario utilizzare `Thread.currentThread()`)

### Esempio Pratico: Thread con Parametri

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

### Best Practices
1. **Preferire l'interfaccia Runnable**: separa il compito dall'esecuzione.
2. **Gestire le eccezioni**: gestire correttamente le eccezioni all'interno del metodo `run()`.
3. **Evitare thread eccessivi**: troppi thread possono degradare le prestazioni.
4. **Dare nomi significativi ai thread**: utile per il debugging.

```java
Thread thread = new Thread(runnable, "ThreadDownload");
```

### Confronto con altri approcci
Per un confronto dettagliato tra l'estensione di `Thread` e l'implementazione di `Runnable`, consulta il capitolo [02a-EstenderelaClasseThread.md](./02a-EstenderelaClasseThread.md).
Per l'approccio con lambda expressions, vedi il capitolo successivo: [02c-ThreadLambda.md](./02c-ThreadLambda.md).

### Esempio Avanzato: Thread Anonimo

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
    }
}
```

---

--- 

Per approfondire gli altri approcci, consulta:
- [02a-EstenderelaClasseThread.md](./02a-EstenderelaClasseThread.md)
- [02c-ThreadLambda.md](./02c-ThreadLambda.md)

## Navigazione del Corso
- [📑 Indice](../README.md)
- [⬅️ Concetti Base dei Thread](./01-ConcettiBase.md)
- [➡️ Ciclo di Vita di un Thread](./03-CicloVita.md)

