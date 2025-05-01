## Thread con Lambda Expressions (Java 8+)

Con Java 8 è possibile creare thread in modo ancora più conciso utilizzando le lambda expressions, grazie al fatto che l'interfaccia `Runnable` è funzionale (ha un solo metodo astratto).

### Esempio Base con Lambda

```java
public class ThreadLambda {
    public static void main(String[] args) {
        // Creazione di un thread con lambda expression
        Thread thread = new Thread(() -> {
            System.out.println("Il thread è in esecuzione con lambda!");
            for (int i = 0; i < 3; i++) {
                System.out.println("Iterazione " + i);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    System.out.println("Thread interrotto");
                }
            }
        });
        thread.start();
    }
}
```

### Vantaggi delle Lambda
- Sintassi più concisa e leggibile
- Ideale per implementazioni semplici e veloci
- Riduce il boilerplate code
- Perfetto per thread "usa e getta" o logiche brevi

### Esempio Pratico: Più Thread con Lambda

```java
public class MultiLambda {
    public static void main(String[] args) {
        for (int i = 1; i <= 3; i++) {
            int threadNum = i;
            new Thread(() -> {
                System.out.println("Thread #" + threadNum + " avviato");
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    System.out.println("Thread #" + threadNum + " interrotto");
                }
                System.out.println("Thread #" + threadNum + " terminato");
            }, "Lambda-" + threadNum).start();
        }
    }
}
```

### Confronto con gli altri approcci
- **Estendere Thread**: più verboso, meno flessibile, non permette ereditarietà multipla
- **Implementare Runnable**: più flessibile, separa logica da esecuzione, ma più verboso rispetto alle lambda
- **Lambda**: massimo della concisione, ideale per logiche semplici

Per approfondire gli altri approcci, consulta:
- [02a-EstenderelaClasseThread.md](./02a-EstenderelaClasseThread.md)
- [02b-ImplementareRunnable.md](./02b-ImplementareRunnable.md)

### Best Practices
1. Utilizza le lambda per thread semplici e temporanei
2. Per logiche complesse o riutilizzabili, preferisci classi dedicate
3. Ricorda che le lambda possono accedere solo a variabili final o effettivamente final del contesto esterno

---

## Navigazione del Corso
- [📑 Indice](../README.md)
- [⬅️ Implementare Runnable](./02b-ImplementareRunnable.md)
- [➡️ Ciclo di Vita di un Thread](./03-CicloVita.md)