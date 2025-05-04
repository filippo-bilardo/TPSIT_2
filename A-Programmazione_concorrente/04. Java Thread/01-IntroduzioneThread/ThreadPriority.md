# Priorità dei Thread in Java

## Cos'è la priorità di un thread?
Ogni thread in Java possiede un livello di priorità, rappresentato da un valore intero compreso tra `Thread.MIN_PRIORITY` (1) e `Thread.MAX_PRIORITY` (10). Il valore predefinito è `Thread.NORM_PRIORITY` (5).

La priorità suggerisce al thread scheduler quale thread dovrebbe essere eseguito per primo, ma **non garantisce** un ordine preciso, poiché il comportamento dipende dal sistema operativo e dalla JVM.

## Come impostare e leggere la priorità
```java
Thread t = new Thread();
t.setPriority(Thread.MAX_PRIORITY); // Imposta la priorità a 10
int p = t.getPriority(); // Legge la priorità
```

## Esempio pratico
```java
public class PriorityDemo {
    public static void main(String[] args) {
        Thread low = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("Low priority");
            }
        });
        Thread high = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("High priority");
            }
        });
        low.setPriority(Thread.MIN_PRIORITY);
        high.setPriority(Thread.MAX_PRIORITY);
        low.start();
        high.start();
    }
}
```
**Nota:** L'output potrebbe non essere sempre "ordinato" secondo la priorità, specialmente su sistemi moderni.

## Best practice
- Usa le priorità solo quando strettamente necessario.
- Non fare affidamento sull'ordine di esecuzione basato sulla priorità.
- Preferisci la sincronizzazione e la progettazione corretta dei thread.

## Limitazioni
- Le priorità sono solo "suggerimenti" per il sistema operativo.
- Alcuni sistemi ignorano le priorità o le gestiscono in modo diverso.

## Riferimenti
- [Documentazione ufficiale Java](https://docs.oracle.com/javase/tutorial/essential/concurrency/)
- [Thread (Java Platform SE 8)](https://docs.oracle.com/javase/8/docs/api/java/lang/Thread.html)