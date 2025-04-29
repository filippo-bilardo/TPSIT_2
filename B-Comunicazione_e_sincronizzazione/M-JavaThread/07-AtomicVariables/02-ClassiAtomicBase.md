# Classi Atomic di Base

Le classi Atomic di base forniscono operazioni atomiche su singoli valori primitivi e riferimenti a oggetti. Queste classi sono progettate per essere utilizzate come blocchi di costruzione per algoritmi concorrenti senza lock.

## AtomicInteger, AtomicLong, AtomicBoolean

Queste classi forniscono operazioni atomiche su tipi primitivi interi e booleani:

### AtomicInteger

`AtomicInteger` fornisce operazioni atomiche su valori interi. Ecco le operazioni principali:

```java
public class AtomicIntegerExample {
    public static void main(String[] args) {
        AtomicInteger atomicInt = new AtomicInteger(0);
        
        // Operazioni di base
        int value = atomicInt.get();           // Ottiene il valore corrente
        atomicInt.set(10);                     // Imposta un nuovo valore
        int oldValue = atomicInt.getAndSet(20); // Imposta un nuovo valore e restituisce il vecchio
        
        // Operazioni di incremento/decremento
        int incremented = atomicInt.incrementAndGet(); // Incrementa e restituisce il nuovo valore
        int decremented = atomicInt.decrementAndGet(); // Decrementa e restituisce il nuovo valore
        int oldValue2 = atomicInt.getAndIncrement();   // Restituisce il valore corrente e poi incrementa
        int oldValue3 = atomicInt.getAndDecrement();   // Restituisce il valore corrente e poi decrementa
        
        // Operazioni aritmetiche
        int added = atomicInt.addAndGet(5);      // Aggiunge un valore e restituisce il risultato
        int oldValue4 = atomicInt.getAndAdd(10); // Restituisce il valore corrente e poi aggiunge
    }
}
```

### AtomicLong

`AtomicLong` funziona in modo simile ad `AtomicInteger`, ma opera su valori `long`. È particolarmente utile per contatori che potrebbero superare il limite di un intero.

```java
AtomicLong atomicLong = new AtomicLong(0L);
long value = atomicLong.get();
atomicLong.set(100L);
long incremented = atomicLong.incrementAndGet();
```

### AtomicBoolean

`AtomicBoolean` fornisce operazioni atomiche su valori booleani. È utile per flag e controlli di stato in ambienti concorrenti.

```java
AtomicBoolean flag = new AtomicBoolean(false);
boolean value = flag.get();
flag.set(true);
boolean oldValue = flag.getAndSet(false); // Imposta a false e restituisce il valore precedente
```

## AtomicReference

`AtomicReference` estende il concetto di operazioni atomiche ai riferimenti a oggetti. Permette di aggiornare atomicamente riferimenti a oggetti di qualsiasi tipo.

```java
public class AtomicReferenceExample {
    static class User {
        private final String name;
        private final int age;
        
        public User(String name, int age) {
            this.name = name;
            this.age = age;
        }
        
        public String toString() {
            return "User{name='" + name + "', age=" + age + "}";
        }
    }
    
    public static void main(String[] args) {
        // Creazione di un AtomicReference con un valore iniziale
        AtomicReference<User> userRef = new AtomicReference<>(new User("Alice", 25));
        
        // Ottenere il valore corrente
        User user = userRef.get();
        System.out.println("Utente corrente: " + user);
        
        // Impostare un nuovo valore
        User newUser = new User("Bob", 30);
        userRef.set(newUser);
        
        // Aggiornare atomicamente il riferimento
        User expectedUser = newUser;
        User updatedUser = new User("Charlie", 35);
        boolean success = userRef.compareAndSet(expectedUser, updatedUser);
        System.out.println("Aggiornamento riuscito: " + success);
        System.out.println("Utente corrente: " + userRef.get());
    }
}
```

## Operazioni Atomiche di Lettura e Scrittura

Tutte le classi Atomic forniscono operazioni di base per la lettura e la scrittura atomica dei valori:

- `get()`: Restituisce il valore corrente.
- `set(newValue)`: Imposta il valore in modo atomico.
- `getAndSet(newValue)`: Imposta un nuovo valore e restituisce il valore precedente in un'unica operazione atomica.

Queste operazioni garantiscono che nessun altro thread possa osservare uno stato intermedio durante l'aggiornamento del valore.

## Operazioni Atomiche di Aggiornamento Condizionale

Le classi Atomic forniscono anche operazioni per aggiornare i valori in modo condizionale:

### compareAndSet (CAS)

Il metodo `compareAndSet(expectedValue, newValue)` è l'operazione fondamentale per l'aggiornamento condizionale. Funziona così:

1. Confronta il valore corrente con un valore atteso.
2. Se sono uguali, aggiorna il valore con un nuovo valore e restituisce `true`.
3. Se sono diversi, non fa nulla e restituisce `false`.

Questa operazione è atomica e sfrutta l'istruzione hardware Compare-and-Swap (CAS).

```java
AtomicInteger atomicInt = new AtomicInteger(10);
boolean success = atomicInt.compareAndSet(10, 20); // success = true, valore = 20
boolean failure = atomicInt.compareAndSet(10, 30); // failure = false, valore rimane 20
```

### updateAndGet e getAndUpdate

Questi metodi permettono di applicare una funzione al valore corrente in modo atomico:

```java
AtomicInteger atomicInt = new AtomicInteger(10);

// Aggiorna il valore applicando una funzione e restituisce il nuovo valore
int result1 = atomicInt.updateAndGet(x -> x * 2); // result1 = 20

// Restituisce il valore corrente e poi lo aggiorna applicando una funzione
int result2 = atomicInt.getAndUpdate(x -> x + 5); // result2 = 20, nuovo valore = 25
```

### accumulateAndGet e getAndAccumulate

Questi metodi permettono di combinare il valore corrente con un altro valore utilizzando una funzione binaria:

```java
AtomicInteger atomicInt = new AtomicInteger(10);

// Combina il valore corrente con un altro valore e restituisce il risultato
int result1 = atomicInt.accumulateAndGet(5, (x, y) -> x * y); // result1 = 50

// Restituisce il valore corrente e poi lo combina con un altro valore
int result2 = atomicInt.getAndAccumulate(2, (x, y) -> x / y); // result2 = 50, nuovo valore = 25
```

Questi metodi sono particolarmente utili quando si devono eseguire operazioni più complesse in modo atomico, senza dover implementare manualmente un ciclo di retry con CAS.

## Utilizzo Pratico

Le classi Atomic di base sono ideali per:

1. **Contatori concorrenti**: Utilizzare `AtomicInteger` o `AtomicLong` per implementare contatori thread-safe senza lock.

2. **Flag di stato**: Utilizzare `AtomicBoolean` per gestire flag di stato in ambienti multi-thread.

3. **Aggiornamenti atomici di oggetti**: Utilizzare `AtomicReference` per aggiornare atomicamente riferimenti a oggetti complessi.

4. **Implementazione di algoritmi lock-free**: Utilizzare le operazioni CAS per implementare strutture dati e algoritmi concorrenti senza lock.

Nel prossimo capitolo, approfondiremo il funzionamento dell'algoritmo Compare-and-Swap (CAS) e come viene utilizzato nelle classi Atomic di Java.

## Navigazione

- [Indice del Corso](../README.md)
- [Indice del Modulo](./README.md)
- Precedente: [Introduzione alle Operazioni Atomiche](./01-IntroduzioneOperazioniAtomiche.md)
- Successivo: [Compare-and-Swap (CAS)](./03-CompareAndSwap.md)