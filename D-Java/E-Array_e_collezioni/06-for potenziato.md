## 6 Il For Potenziato

In Java, il **for potenziato** (o **enhanced for loop**) è una versione migliorata del ciclo `for` tradizionale, introdotta a partire da Java 5. Questo ciclo è progettato per iterare facilmente su collezioni di dati, come array e oggetti delle classi del framework `Collection`. Il ciclo for potenziato è più semplice e leggibile rispetto al for tradizionale, poiché elimina la necessità di gestire manualmente gli indici.

### Sintassi del For Potenziato
La sintassi di base del ciclo for potenziato è la seguente:
```java
for (Tipo elemento : arrayOCollezione) {
    // Operazioni da eseguire su elemento
}
```

- **Tipo**: è il tipo di dati dell'elemento all'interno dell'array o della collezione (ad esempio, `int`, `String`, `double`).
- **elemento**: è una variabile che rappresenta l’elemento corrente nell’iterazione. Ad ogni iterazione, `elemento` assumerà il valore di un elemento della collezione.
- **arrayOCollezione**: è l’array o la collezione che si desidera attraversare.

### Esempio: Iterare su un Array di Interi

Il seguente esempio utilizza un for potenziato per sommare tutti gli elementi di un array di interi.

```java
public class SommaArray {
    public static void main(String[] args) {
        int[] numeri = {10, 20, 30, 40, 50};
        int somma = 0;

        for (int numero : numeri) {
            somma += numero; // Somma ciascun elemento
        }

        System.out.println("La somma degli elementi è: " + somma);
    }
}
```

**Output**:
```
La somma degli elementi è: 150
```

In questo esempio, il ciclo for potenziato attraversa ogni elemento dell’array `numeri` senza bisogno di un indice, e somma ogni elemento alla variabile `somma`.

### Esempio: Iterare su un Array di Stringhe

Ecco un altro esempio che mostra come utilizzare il for potenziato per iterare su un array di stringhe.

```java
public class StampaNomi {
    public static void main(String[] args) {
        String[] nomi = {"Alice", "Bob", "Carol", "David"};

        for (String nome : nomi) {
            System.out.println("Ciao, " + nome + "!");
        }
    }
}
```

**Output**:
```
Ciao, Alice!
Ciao, Bob!
Ciao, Carol!
Ciao, David!
```

Ogni elemento dell’array `nomi` viene assegnato alla variabile `nome` per ogni iterazione, rendendo il codice più leggibile rispetto al ciclo `for` tradizionale.

### Vantaggi del For Potenziato
1. **Semplicità**: Elimina la necessità di gestire un indice o contatore manualmente, rendendo il codice più leggibile e facile da comprendere.
2. **Riduzione degli Errori**: Poiché non si manipola un indice, si riduce il rischio di errori di confine (come il superamento dei limiti dell'array).
3. **Codice Compatto**: Il for potenziato riduce la verbosità del ciclo `for` tradizionale, migliorando la leggibilità.

### Limiti del For Potenziato
1. **Accesso Limitato agli Indici**: Nel for potenziato non si può accedere direttamente agli indici dell’array o della collezione. Questo può rappresentare un problema se si ha bisogno dell’indice per modificare gli elementi o per altre operazioni specifiche.
2. **Modifiche Limitate**: Non è possibile aggiornare o modificare direttamente gli elementi dell’array. Il for potenziato è principalmente progettato per leggere i dati, non per modificarli.
Con il for-each, puoi modificare gli stati degli oggetti iterati (se sono di tipo riferimento), ma non puoi cambiare i riferimenti agli elementi della collezione o dell'array originale. Per modifiche ai riferimenti o alla struttura sottostante, è necessario usare un ciclo for tradizionale o un iteratore appropriato per le collezioni.
3. **Non Applicabile a Tutte le Collezioni**: Funziona solo con array e collezioni che implementano l'interfaccia `Iterable`. Non è utilizzabile, ad esempio, con mappe (`Map`), a meno di iterare separatamente sulle chiavi o sui valori.

### Esempio: Modifica degli Elementi
Nel caso si debba aggiornare ciascun elemento di un array, il for potenziato non è la scelta migliore, poiché non consente di modificare direttamente l’array. Qui è preferibile un ciclo `for` tradizionale.

```java
public class IncrementaArray {
    public static void main(String[] args) {
        int[] numeri = {1, 2, 3, 4, 5};

        // Utilizzo di un for tradizionale per incrementare ciascun elemento
        for (int i = 0; i < numeri.length; i++) {
            numeri[i] += 1; // Incrementa ogni elemento di 1
        }

        // Stampa degli elementi aggiornati
        for (int numero : numeri) {
            System.out.print(numero + " ");
        }
    }
}
```

**Output**:
```
2 3 4 5 6
```

### Esempio con Collezione (ArrayList):

```java
import java.util.ArrayList;

public class EsempioForPotenziatoCollezione {
    public static void main(String[] args) {
        ArrayList<String> nomi = new ArrayList<>();
        nomi.add("Mario");
        nomi.add("Luigi");
        nomi.add("Anna");

        // Uso del for potenziato per scorrere l'ArrayList
        for (String nome : nomi) {
            System.out.println(nome);  // Stampa ogni elemento della collezione
        }
    }
}
```

**Output:**
```
Mario
Luigi
Anna
```

### Esempio: Uso con Tipi Complessi (Oggetti):

Il for potenziato può essere usato anche per iterare su collezioni o array di oggetti complessi.

```java
public class Persona {
    private String nome;
    private int eta;

    public Persona(String nome, int eta) {
        this.nome = nome;
        this.eta = eta;
    }

    public String getNome() {
        return nome;
    }

    public int getEta() {
        return eta;
    }
}

import java.util.ArrayList;

public class EsempioForPotenziatoOggetti {
    public static void main(String[] args) {
        ArrayList<Persona> persone = new ArrayList<>();
        persone.add(new Persona("Mario", 30));
        persone.add(new Persona("Luigi", 25));
        persone.add(new Persona("Anna", 28));

        // Uso del for potenziato per iterare su una collezione di oggetti
        for (Persona persona : persone) {
            System.out.println(persona.getNome() + " ha " + persona.getEta() + " anni");
        }
    }
}
```

**Output:**
```
Mario ha 30 anni
Luigi ha 25 anni
Anna ha 28 anni
```

### Conclusione
Il for potenziato è un costrutto utile per iterare in modo semplice e sicuro su array e collezioni, particolarmente indicato quando è necessario soltanto leggere i dati o eseguire operazioni di elaborazione senza modificare gli elementi. Tuttavia, quando è necessario accedere agli indici o aggiornare i valori all’interno dell’array, il ciclo `for` tradizionale rimane una scelta più adatta.

## Il ciclo for-each per iterare le matrici

In Java, un ciclo **for-each** (o **for potenziato**) può essere utilizzato per iterare su ogni elemento di una **matrice** (un array bidimensionale). Con il for-each, possiamo attraversare ogni riga e ogni colonna senza bisogno di gestire manualmente gli indici.

**Ecco un esempio:**

```java
public class MatriceForEach {
    public static void main(String[] args) {
        int[][] matrice = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };

        // Iterazione for-each su una matrice
        for (int[] riga : matrice) {
            for (int elemento : riga) {
                System.out.print(elemento + " ");
            }
            System.out.println(); // Nuova linea per separare le righe
        }
    }
}
```

**Output**:
```
1 2 3 
4 5 6 
7 8 9 
```

**Spiegazione del Codice**

- **Ciclo esterno**: `for (int[] riga : matrice)` attraversa ogni **riga** della matrice. Ogni riga è un array di interi (`int[]`).
- **Ciclo interno**: `for (int elemento : riga)` attraversa ogni **elemento** all'interno della riga corrente.

Questo approccio è particolarmente leggibile e funziona bene quando l'obiettivo è accedere semplicemente a ogni elemento di una matrice senza modificare il suo contenuto.

[05-Esempio mazzo di carte.md](05-Esempio%20mazzo%20di%20carte.md) - [INDICE](README.md) - [07-Array di oggetti](07-Array%20di%20oggetti.md)