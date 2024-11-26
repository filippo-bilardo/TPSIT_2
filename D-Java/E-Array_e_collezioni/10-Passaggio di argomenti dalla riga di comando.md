## 10 Passaggio di Argomenti dalla Riga di Comando

In Java, è possibile **passare argomenti a un programma direttamente dalla riga di comando** al momento della sua esecuzione. Questi argomenti vengono forniti al metodo `main` come un array di `String`, permettendo al programma di ricevere informazioni esterne e comportarsi in modo dinamico in base ai valori passati.

### Come Funziona il Passaggio di Argomenti

Il metodo `main` in Java è definito come:
```java
public static void main(String[] args)
```

Dove `args` è un array di `String` che contiene gli argomenti passati alla riga di comando. Ogni elemento di `args` rappresenta un singolo argomento passato al programma. Se nessun argomento viene fornito, `args` sarà un array vuoto.

### Esecuzione con Argomenti dalla Riga di Comando

Per passare argomenti a un programma Java, basta includerli dopo il nome della classe quando si esegue il programma. Ad esempio, se si ha un file `ProgrammaEsempio.java` compilato in `ProgrammaEsempio.class`, è possibile passare argomenti come segue:

```bash
java ProgrammaEsempio argomento1 argomento2 argomento3
```

In questo caso:
- `args[0]` sarà `"argomento1"`.
- `args[1]` sarà `"argomento2"`.
- `args[2]` sarà `"argomento3"`.

### Esempio: Stampa degli Argomenti

Il seguente programma stampa tutti gli argomenti passati dalla riga di comando:

```java
public class StampaArgomenti {
    public static void main(String[] args) {
        System.out.println("Numero di argomenti: " + args.length);

        for (int i = 0; i < args.length; i++) {
            System.out.println("Argomento " + i + ": " + args[i]);
        }
    }
}
```

**Esecuzione del Programma:**
```bash
java StampaArgomenti Ciao Mondo Programmazione
```

**Output:**
```
Numero di argomenti: 3
Argomento 0: Ciao
Argomento 1: Mondo
Argomento 2: Programmazione
```

Il programma utilizza `args.length` per determinare il numero di argomenti passati e li stampa uno per uno.

### Esempio: Somma di Numeri Passati come Argomenti

Un uso pratico del passaggio di argomenti è quello di accettare numeri dalla riga di comando, calcolare la loro somma e visualizzare il risultato.

```java
public class SommaArgomenti {
    public static void main(String[] args) {
        int somma = 0;

        for (String arg : args) {
            try {
                int numero = Integer.parseInt(arg);
                somma += numero;
            } catch (NumberFormatException e) {
                System.out.println("Errore: '" + arg + "' non è un numero valido.");
            }
        }

        System.out.println("La somma dei numeri è: " + somma);
    }
}
```

**Esecuzione del Programma:**
```bash
java SommaArgomenti 10 20 30 abc 40
```

**Output:**
```
Errore: 'abc' non è un numero valido.
La somma dei numeri è: 100
```

In questo esempio:
- Ogni argomento viene convertito in un numero con `Integer.parseInt(arg)`.
- Se l’argomento non è un numero, viene generata un’eccezione `NumberFormatException`, gestita con un messaggio di errore.
- La somma di tutti i numeri validi viene poi stampata.

### Vantaggi del Passaggio di Argomenti dalla Riga di Comando

1. **Flessibilità**: Permette di modificare il comportamento del programma senza dover cambiare il codice o ricompilarlo.
2. **Automazione**: Consente l'integrazione del programma in script di automazione o altre applicazioni che possono passare parametri dinamicamente.
3. **Input Dinamico**: Ideale per programmi che richiedono configurazioni diverse o input specifici forniti dall'utente al momento dell'esecuzione.

### Considerazioni

- **Gestione degli Errori**: Quando si accettano argomenti dalla riga di comando, è importante gestire correttamente i possibili errori, come la conversione di tipi o il numero errato di argomenti.
- **Tipi di Dati**: Gli argomenti della riga di comando sono sempre stringhe, quindi se sono necessari altri tipi di dati, è necessario convertirli manualmente.
- **Ordine degli Argomenti**: Gli argomenti vengono ricevuti nell’ordine in cui vengono passati dalla riga di comando, quindi l'ordine deve essere rispettato per il corretto funzionamento del programma.

### Conclusione

Il passaggio di argomenti dalla riga di comando offre un modo semplice e flessibile per personalizzare l'esecuzione dei programmi Java. È utile per testare diverse configurazioni, accettare input dinamici e rendere i programmi più interattivi e versatili.

[09-Passaggio di array come argomenti](09-Passaggio%20di%20array%20come%20argomenti.md) - [INDICE](README.md) - [11-La classe Arrays](11-La%20classe%20Arrays.md)
