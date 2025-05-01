## Creare Thread Estendendo la Classe Thread

In Java, uno dei modi più semplici per creare un nuovo thread è estendere la classe `Thread`. Questo approccio consiste nel definire una sottoclasse di `Thread` e sovrascrivere il metodo `run()`, che contiene il codice che verrà eseguito dal thread.

### Esempio Base

```java
public class MioThread extends Thread {
    public MioThread(String name) {
        super(name); // Imposta il nome del thread
    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println(getName() + ": iterazione " + i);
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                System.out.println(getName() + " interrotto");
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        MioThread t1 = new MioThread("Thread-1");
        MioThread t2 = new MioThread("Thread-2");
        t1.start();
        t2.start();
    }
}
```

### Spiegazione
- **Estensione**: Si crea una classe che estende `Thread`.
- **Override di `run()`**: Si inserisce la logica da eseguire nel thread dentro il metodo `run()`.
- **Avvio**: Si crea un'istanza della sottoclasse e si chiama `start()` per avviare il thread.

### Vantaggi
- Sintassi semplice e diretta per thread "usa e getta".
- Permette di personalizzare facilmente il comportamento del thread tramite override di altri metodi di `Thread`.

### Svantaggi
- **Ereditarietà singola**: In Java una classe può estendere solo una classe, quindi se estendi `Thread` non puoi estendere altre classi.
- **Meno flessibile**: Separare la logica del thread dalla sua esecuzione è più difficile rispetto all'uso di `Runnable`.

### Best Practice
1. Usa questo approccio solo per thread semplici e quando non hai bisogno di estendere altre classi.
2. Per logiche più complesse o riutilizzabili, preferisci l'implementazione di `Runnable`.
3. Ricorda di chiamare sempre `start()` (non `run()`) per avviare il thread.

### Esempio Pratico: Estendere Thread vs Implementare Runnable

```java
// Estendere Thread
class MioThread extends Thread {
    public void run() {
        System.out.println("Thread esteso in esecuzione");
    }
}

// Implementare Runnable
class MioRunnable implements Runnable {
    public void run() {
        System.out.println("Runnable in esecuzione");
    }
}

public class Main {
    public static void main(String[] args) {
        new MioThread().start();
        new Thread(new MioRunnable()).start();
    }
}
```

### Confronto con altri approcci
- **Estendere Thread**: Più semplice, ma limita l'ereditarietà e la riusabilità.
- **Implementare Runnable**: Più flessibile, separa la logica dal thread, consigliato per la maggior parte dei casi.
- **Lambda**: Massima concisione, ideale per logiche brevi e temporanee.

Per approfondire gli altri approcci, consulta:
- [02b-ImplementareRunnable.md](./02b-ImplementareRunnable.md)
- [02c-ThreadLambda.md](./02c-ThreadLambda.md)

---

## Navigazione del Corso
- [📑 Indice](../README.md)
- [➡️ Implementare Runnable](./02b-ImplementareRunnable.md)