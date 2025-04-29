# Lambda Expressions in Java

Le Lambda Expressions rappresentano uno dei più importanti miglioramenti introdotti con Java 8, permettendo di scrivere codice più conciso e leggibile attraverso l'implementazione del paradigma di programmazione funzionale.

## Che cos'è una Lambda Expression

Una Lambda Expression è essenzialmente una funzione anonima che può essere passata come argomento o restituita come risultato da un'altra funzione. La sintassi di base è:

```
(parametri) -> espressione
```

oppure

```
(parametri) -> { istruzioni; }
```

## Sintassi e Componenti

Una Lambda Expression è composta da:

1. **Lista di parametri**: racchiusa tra parentesi tonde e separata da virgole
2. **Operatore freccia** `->`: separa i parametri dal corpo della lambda
3. **Corpo della lambda**: può essere una singola espressione o un blocco di istruzioni racchiuso tra parentesi graffe

### Esempi di sintassi

```java
// Lambda senza parametri
() -> System.out.println("Hello World")

// Lambda con un parametro (le parentesi sono opzionali con un solo parametro)
x -> x * x
(int x) -> x * x  // con tipo esplicito

// Lambda con più parametri
(x, y) -> x + y
(int x, int y) -> x + y  // con tipi espliciti

// Lambda con blocco di codice
(x, y) -> {
    int sum = x + y;
    return sum;
}
```

## Interfacce Funzionali

Le Lambda Expressions in Java lavorano in congiunzione con le **interfacce funzionali**. Un'interfaccia funzionale è un'interfaccia che contiene esattamente un metodo astratto.

Java fornisce diverse interfacce funzionali predefinite nel package `java.util.function`:

| Interfaccia | Metodo astratto | Descrizione |
|-------------|----------------|-------------|
| `Function<T,R>` | `R apply(T t)` | Trasforma un input di tipo T in un output di tipo R |
| `Predicate<T>` | `boolean test(T t)` | Valuta una condizione su un input di tipo T |
| `Consumer<T>` | `void accept(T t)` | Consuma un input di tipo T senza restituire nulla |
| `Supplier<T>` | `T get()` | Fornisce un valore di tipo T senza ricevere input |
| `BiFunction<T,U,R>` | `R apply(T t, U u)` | Accetta due input e restituisce un output |
| `BinaryOperator<T>` | `T apply(T t1, T t2)` | Operazione su due operandi dello stesso tipo |

### Esempio con interfacce funzionali predefinite

```java
// Consumer
Consumer<String> printer = s -> System.out.println(s);
printer.accept("Hello Consumer");

// Predicate
Predicate<Integer> isPositive = n -> n > 0;
System.out.println(isPositive.test(5));  // true

// Function
Function<String, Integer> strLength = s -> s.length();
System.out.println(strLength.apply("Java"));  // 4

// Supplier
Supplier<LocalDate> today = () -> LocalDate.now();
System.out.println(today.get());

// BiFunction
BiFunction<Integer, Integer, Integer> add = (a, b) -> a + b;
System.out.println(add.apply(2, 3));  // 5
```

## Utilizzo delle Lambda in vari contesti

### 1. Gestione delle collezioni con Stream API

```java
List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David");

// Filtraggio con lambda
List<String> filteredNames = names.stream()
    .filter(name -> name.startsWith("A"))
    .collect(Collectors.toList());

// Trasformazione con lambda
List<Integer> nameLengths = names.stream()
    .map(name -> name.length())
    .collect(Collectors.toList());

// Ordinamento con lambda
names.sort((a, b) -> a.compareTo(b));
// Equivalente a: names.sort(Comparator.naturalOrder());
```

### 2. Gestione degli eventi in JavaFX

```java
button.setOnAction(event -> {
    System.out.println("Button clicked!");
    // Altre azioni...
});
```

### 3. Implementazione di Thread

```java
// Implementazione tradizionale
Thread thread1 = new Thread(new Runnable() {
    @Override
    public void run() {
        System.out.println("Thread in esecuzione");
    }
});

// Con lambda expression
Thread thread2 = new Thread(() -> System.out.println("Thread in esecuzione"));
```

### 4. Implementazione di Comparator

```java
// Comparator tradizionale
Comparator<Person> byAge = new Comparator<Person>() {
    @Override
    public int compare(Person p1, Person p2) {
        return p1.getAge() - p2.getAge();
    }
};

// Con lambda expression
Comparator<Person> byAgeWithLambda = (p1, p2) -> p1.getAge() - p2.getAge();
```

## Variabili e Lambda Expressions

Le Lambda Expressions possono accedere a:
- Variabili locali (che devono essere `final` o "effectively final")
- Parametri del metodo che le contiene
- Variabili di istanza e statiche della classe che le contiene

```java
String prefix = "Name: ";  // effectively final
Consumer<String> printWithPrefix = name -> System.out.println(prefix + name);
```

### Limitazioni nell'accesso alle variabili

Le Lambda possono accedere alle variabili locali solo se queste sono `final` o "effectively final" (non modificate dopo l'inizializzazione). Questa limitazione esiste per evitare problemi di concorrenza.

```java
// Esempio corretto
int factor = 2;  // effectively final
Function<Integer, Integer> multiplier = n -> n * factor;

// Esempio che NON compila
int counter = 0;
Runnable r = () -> {
    System.out.println(counter);  // OK, accesso in lettura
    counter++;  // Errore! Non si può modificare la variabile locale
};
```

## Method References

I method reference sono una forma abbreviata di lambda expression che fanno riferimento a metodi esistenti. La sintassi utilizza l'operatore `::`.

### Tipi di Method Reference

1. **Riferimento a metodo statico**: `ClassName::staticMethod`
2. **Riferimento a metodo di istanza di un oggetto specifico**: `instance::method`
3. **Riferimento a metodo di istanza di un oggetto arbitrario**: `ClassName::instanceMethod`
4. **Riferimento a costruttore**: `ClassName::new`

```java
// Esempio 1: Riferimento a metodo statico
Function<String, Integer> parser = Integer::parseInt;
int value = parser.apply("42");  // 42

// Esempio 2: Riferimento a metodo di istanza di un oggetto specifico
String greeting = "Hello";
Supplier<Integer> lengthSupplier = greeting::length;
int length = lengthSupplier.get();  // 5

// Esempio 3: Riferimento a metodo di istanza di un oggetto arbitrario
Function<String, Integer> stringLength = String::length;
int len = stringLength.apply("Java");  // 4

// Esempio 4: Riferimento a costruttore
Supplier<List<String>> listSupplier = ArrayList::new;
List<String> list = listSupplier.get();  // Nuova ArrayList<String>
```

## Best Practices

1. **Preferire method references quando possibile**: Sono più concise e leggibili
   ```java
   // Invece di
   list.forEach(s -> System.out.println(s));
   // Usare
   list.forEach(System.out::println);
   ```

2. **Mantenere le lambda semplici**: Se una lambda diventa complessa, è meglio estrarla in un metodo separato

3. **Specificare i tipi solo quando necessario**: Lasciare che il compilatore inferisca i tipi quando possibile

4. **Essere consapevoli della cattura di variabili**: Prestare attenzione all'accesso alle variabili locali

5. **Usare le interfacce funzionali predefinite**: Preferire le interfacce del package `java.util.function` quando possibile invece di crearne di nuove

## Domande di autovalutazione

1. Quale dei seguenti NON è una caratteristica delle lambda expressions in Java?
   a) Sono implementazioni di interfacce funzionali
   b) Possono accedere a variabili locali final o effectively final
   c) Possono modificare le variabili locali del contesto circostante
   d) Possono essere passate come argomenti ad altri metodi

2. Quale interfaccia funzionale rappresenta un'operazione che accetta un input e non produce risultati?
   a) `Function<T,R>`
   b) `Predicate<T>`
   c) `Consumer<T>`
   d) `Supplier<T>`

3. Quale delle seguenti affermazioni è corretta riguardo ai method references?
   a) Possono essere utilizzati solo con metodi statici
   b) Sono una forma abbreviata di lambda expression
   c) Richiedono sempre parametri espliciti
   d) Non possono fare riferimento ai costruttori

4. Perché le lambda expressions possono accedere solo a variabili locali che sono final o effectively final?
   a) Per migliorare le prestazioni
   b) Per limitare la complessità del codice
   c) Per garantire la thread-safety
   d) Per ridurre l'uso della memoria

## Risposte corrette
1. c) Possono modificare le variabili locali del contesto circostante
2. c) `Consumer<T>`
3. b) Sono una forma abbreviata di lambda expression
4. c) Per garantire la thread-safety