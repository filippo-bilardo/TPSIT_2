# Funzioni Anonime in Java

Le funzioni anonime sono un concetto fondamentale nella programmazione funzionale, implementato in Java principalmente attraverso le lambda expressions. Approfondiamo questo argomento importante che è strettamente correlato alle lambda che abbiamo già discusso.

## Concetto di Funzione Anonima

Una funzione anonima è una funzione che viene definita senza un nome e tipicamente utilizzata una sola volta o passata come argomento ad altre funzioni. In Java, ci sono due modi principali per implementare funzioni anonime:

1. **Classi anonime** (pre-Java 8)
2. **Lambda expressions** (da Java 8)

## Classi Anonime

Prima di Java 8, le funzioni anonime venivano implementate attraverso classi anonime, specialmente quando si implementavano interfacce con un solo metodo:

```java
// Implementazione di un comparatore con classe anonima
Comparator<String> comparator = new Comparator<String>() {
    @Override
    public int compare(String s1, String s2) {
        return s1.length() - s2.length();
    }
};

// Implementazione di un listener con classe anonima
button.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Button clicked!");
    }
});
```

## Lambda come Funzioni Anonime

Con Java 8, le lambda expressions hanno fornito una sintassi molto più concisa per le funzioni anonime:

```java
// Comparatore come funzione anonima con lambda
Comparator<String> comparator = (s1, s2) -> s1.length() - s2.length();

// Listener come funzione anonima con lambda
button.addActionListener(e -> System.out.println("Button clicked!"));
```

## Differenze tra Classi Anonime e Lambda

| Caratteristica | Classe Anonima | Lambda Expression |
|----------------|----------------|------------------|
| Sintassi | Verbosa | Concisa |
| `this` | Riferimento all'istanza della classe anonima | Riferimento alla classe contenitore |
| Shadowing di variabili | Può dichiarare variabili che nascondono variabili dell'ambito esterno | Non può dichiarare variabili che nascondono variabili dell'ambito esterno |
| Implementazioni multiple | Può implementare interfacce con più metodi | Solo interfacce funzionali (un solo metodo astratto) |

## Funzioni Anonime in Altri Contesti

### 1. In combinazione con Stream API

```java
// Funzione anonima come filtro
List<String> filtered = list.stream()
    .filter(s -> s.length() > 3)  // Predicato anonimo
    .collect(Collectors.toList());

// Funzione anonima come mapper
List<Integer> lengths = list.stream()
    .map(s -> s.length())  // Funzione anonima di trasformazione
    .collect(Collectors.toList());
```

### 2. In operazioni di ordinamento

```java
// Ordinamento con funzione anonima
list.sort((a, b) -> {
    if (a.length() != b.length()) {
        return a.length() - b.length();
    }
    return a.compareTo(b);
});
```

### 3. Con ExecutorService

```java
ExecutorService executor = Executors.newSingleThreadExecutor();

// Invio di task come funzione anonima
executor.submit(() -> {
    System.out.println("Task eseguito nel thread: " + 
                       Thread.currentThread().getName());
    return "Completato";
});
```

## Cattura del Contesto nelle Funzioni Anonime

Le funzioni anonime in Java "catturano" il contesto circostante. Questo comportamento è soggetto a regole specifiche:

```java
String prefix = "Nome: ";  // Variabile locale

// La funzione anonima cattura la variabile prefix
Consumer<String> printer = name -> System.out.println(prefix + name);

// Utilizzo della funzione anonima
printer.accept("Mario");  // Output: Nome: Mario
```

La cattura delle variabili locali funziona solo se queste sono `final` o "effectively final" (non modificate dopo l'inizializzazione).

## Funzioni Anonime Multi-riga

Quando una funzione anonima richiede più istruzioni, si utilizza un blocco di codice con parentesi graffe:

```java
Function<Integer, Integer> factorial = n -> {
    int result = 1;
    for (int i = 1; i <= n; i++) {
        result *= i;
    }
    return result;
};

System.out.println(factorial.apply(5));  // Output: 120
```

## Pattern Comuni di Utilizzo

### 1. Callback Functions

```java
public void processData(List<String> data, Consumer<String> callback) {
    for (String item : data) {
        // Elaborazione...
        callback.accept(item);  // Chiamata alla funzione anonima
    }
}

// Utilizzo
processData(items, item -> System.out.println("Elaborato: " + item));
```

### 2. Strategy Pattern

```java
// Interface che definisce la strategia
interface ValidationStrategy {
    boolean validate(String text);
}

// Utilizzo di funzioni anonime come strategie
ValidationStrategy numericValidator = text -> text.matches("\\d+");
ValidationStrategy emailValidator = text -> text.contains("@");

// Funzione che usa la strategia
boolean validate(String text, ValidationStrategy strategy) {
    return strategy.validate(text);
}

// Chiamata
boolean isValid = validate("12345", text -> text.matches("\\d+"));
```

### 3. Builder Pattern con funzioni anonime

```java
public class UIComponentBuilder {
    private Runnable onClickAction;
    
    public UIComponentBuilder onClick(Runnable action) {
        this.onClickAction = action;
        return this;
    }
    
    // Altri metodi builder...
}

// Utilizzo
UIComponentBuilder builder = new UIComponentBuilder()
    .onClick(() -> System.out.println("Componente cliccato!"));
```

## Best Practices per le Funzioni Anonime

1. **Mantenere la brevità**: Se una funzione anonima diventa troppo lunga o complessa, è meglio estrarla in un metodo separato.

2. **Evitare side-effects**: Per ideale, le funzioni anonime dovrebbero essere pure (senza effetti collaterali).

3. **Preferire method references quando possibile**: Se la funzione anonima si limita a chiamare un metodo esistente, usare il method reference.

4. **Nominare le variabili lambda significativamente**: Scegliere nomi di parametri che riflettono il loro significato.

```java
// Non ottimale
list.forEach(x -> System.out.println(x));

// Meglio
list.forEach(item -> System.out.println(item));

// Ancora meglio (method reference)
list.forEach(System.out::println);
```

## Esercizi Proposti

1. Scrivere una funzione che accetta una lista di stringhe e una funzione anonima come filtro, restituendo solo gli elementi che soddisfano il criterio del filtro.

2. Implementare un sistema di notifica che permetta di registrare diverse funzioni anonime come handler per tipi diversi di eventi.

3. Creare un comparatore composito utilizzando funzioni anonime che permetta di ordinare oggetti per diversi criteri in sequenza.

## Domande di autovalutazione

1. Quale affermazione riguardante le funzioni anonime in Java è corretta?
   a) Possono implementare qualsiasi interfaccia
   b) Richiedono sempre l'utilizzo di parentesi graffe
   c) Possono accedere solo a variabili final o effectively final del contesto circostante
   d) Non possono restituire valori

2. In quale dei seguenti casi è preferibile utilizzare una classe anonima rispetto a una lambda expression?
   a) Quando si implementa un'interfaccia con un solo metodo
   b) Quando è necessario accedere a variabili locali modificabili
   c) Quando si implementa un'interfaccia con più metodi
   d) Quando è necessario chiamare un metodo esistente

3. Cosa rappresenta `this` all'interno di una lambda expression?
   a) Un nuovo oggetto creato per la lambda
   b) L'istanza dell'interfaccia funzionale
   c) L'istanza della classe contenitore
   d) Un riferimento nullo

4. Quale dei seguenti è un vantaggio delle lambda expressions rispetto alle classi anonime?
   a) Possibilità di implementare interfacce con più metodi
   b) Sintassi più concisa
   c) Possibilità di dichiarare nuove variabili con lo stesso nome di quelle nell'ambito esterno
   d) Creazione di un nuovo contesto per `this`

Risposte: 1-c, 2-c, 3-c, 4-b