# Java Daemon Thread

I thread daemon sono una caratteristica importante della programmazione concorrente in Java, che si differenziano significativamente dai thread normali (non-daemon).

## Caratteristiche dei Daemon Thread

Un daemon thread è un thread di "servizio" che opera in background con le seguenti peculiarità:

- **Ciclo di vita**: viene automaticamente terminato quando tutti i thread non-daemon hanno completato la loro esecuzione
- **Priorità**: tipicamente ha priorità bassa poiché esegue attività di supporto
- **Terminazione**: non impedisce alla JVM di terminare l'esecuzione
- **Finalizzazione**: non è garantito che esegua operazioni di pulizia alla chiusura

## Creazione di un Daemon Thread

Per creare un daemon thread, è necessario impostare la proprietà `daemon` a `true` prima di avviare il thread:

```java
Thread daemonThread = new Thread(() -> {
    while (true) {
        System.out.println("Daemon thread in esecuzione...");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
});

// Imposta il thread come daemon PRIMA di avviarlo
daemonThread.setDaemon(true);
daemonThread.start();
```

## Verifica dello stato daemon

È possibile verificare se un thread è daemon o meno:

```java
boolean isDaemon = thread.isDaemon();
```

## Utilizzi comuni

I daemon thread sono comunemente utilizzati per:

1. **Garbage Collection**: il thread del garbage collector è un daemon thread
2. **Monitoraggio delle risorse**: thread che controllano lo stato del sistema
3. **Servizi di background**: come l'eliminazione di file temporanei, log di sistema
4. **Timeout manager**: gestione di timeout per operazioni asincrone
5. **Heartbeat**: invio periodico di segnali di attività in applicazioni distribuite

## Esempio completo

```java
public class DaemonThreadExample {
    public static void main(String[] args) {
        Thread daemonThread = new Thread(() -> {
            int count = 0;
            while (true) {
                System.out.println("Daemon thread: iterazione " + (++count));
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        
        daemonThread.setDaemon(true);
        daemonThread.start();
        
        // Il thread principale esegue alcune operazioni
        try {
            System.out.println("Thread principale: operazione in corso...");
            Thread.sleep(2500);
            System.out.println("Thread principale: operazione completata");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("Thread principale: terminazione");
        // Quando il main thread termina, anche il daemon thread terminerà
    }
}
```

## Best Practices

1. **Non utilizzare daemon thread per operazioni critiche**: evitare di eseguire operazioni che richiedono una terminazione pulita
2. **Attenzione alle risorse**: i daemon thread potrebbero non chiudere correttamente le risorse (file, connessioni di rete)
3. **Impostare daemon prima di start()**: l'impostazione deve avvenire prima dell'avvio del thread
4. **Usare timeout adeguati**: se un daemon thread attende una risorsa indefinitamente, potrebbe non terminare mai

## Differenze con i Thread normali

| Caratteristica | Thread Daemon | Thread non-Daemon |
|----------------|---------------|------------------|
| Terminazione | Termina quando tutti i thread non-daemon terminano | Deve terminare esplicitamente |
| Finalizzazione | Non garantita | Garantita |
| Utilizzo | Operazioni di supporto, servizi | Operazioni principali |
| Priorità JVM | La JVM può terminare anche se sono attivi | La JVM attende che terminino |

## Note di implementazione

- Un thread eredita lo stato daemon dal thread che lo crea
- Non è possibile modificare lo stato daemon di un thread dopo che è stato avviato
- I thread daemon creano altri thread daemon per default