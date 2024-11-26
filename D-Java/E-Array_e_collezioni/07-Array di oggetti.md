## 7 Array di Oggetti

In Java, oltre agli array di tipi primitivi, è possibile creare array di **oggetti**. Questa struttura è particolarmente utile per organizzare e gestire insiemi di oggetti complessi, come persone, libri, studenti o prodotti, in una singola variabile strutturata.

### Creazione di un Array di Oggetti

Per creare un array di oggetti, occorre:
1. Definire una classe per il tipo di oggetto (ad esempio `Persona`).
2. Dichiarare e istanziare un array di tipo `Classe[]`.

Esempio: Creiamo un array di oggetti `Persona`.

```java
class Persona {
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

    @Override
    public String toString() {
        return "Nome: " + nome + ", Età: " + eta;
    }
}

public class ArrayDiOggetti {
    public static void main(String[] args) {
        // Creazione di un array di 3 oggetti Persona
        Persona[] persone = new Persona[3];

        // Inizializzazione degli oggetti nell'array
        persone[0] = new Persona("Alice", 25);
        persone[1] = new Persona("Bob", 30);
        persone[2] = new Persona("Charlie", 28);

        // Stampa degli oggetti nell'array
        for (Persona persona : persone) {
            System.out.println(persona);
        }
    }
}
```

**Output**:
```
Nome: Alice, Età: 25
Nome: Bob, Età: 30
Nome: Charlie, Età: 28
```

### Utilità degli Array di Oggetti

L’array di oggetti è ideale quando si devono gestire più istanze dello stesso tipo di oggetto in un programma. Un array di oggetti consente di:
- Memorizzare dati complessi in modo ordinato.
- Applicare operazioni a un insieme di oggetti, come la ricerca e l’ordinamento.
- Passare l’array come argomento per metodi che elaborano insiemi di oggetti.

### Accesso e Manipolazione degli Elementi

Gli elementi di un array di oggetti possono essere **modificati e letti** come in qualsiasi altro array, tramite l'indice. Inoltre, è possibile chiamare i metodi dell'oggetto direttamente tramite l’elemento dell'array.

Esempio di accesso agli attributi:

```java
System.out.println("Nome della prima persona: " + persone[0].getNome());
```

### Esempio Avanzato: Array di Oggetti con Metodi Aggiuntivi

Ecco un esempio di come implementare un metodo per **calcolare l’età media** delle persone in un array di oggetti `Persona`.

```java
public class GestionePersone {

    // Metodo che calcola l'età media delle persone nell'array
    public static double calcolaEtaMedia(Persona[] persone) {
        int sommaEta = 0;
        for (Persona persona : persone) {
            sommaEta += persona.getEta();
        }
        return (double) sommaEta / persone.length;
    }

    public static void main(String[] args) {
        Persona[] persone = {
            new Persona("Alice", 25),
            new Persona("Bob", 30),
            new Persona("Charlie", 28)
        };

        System.out.println("Età media: " + calcolaEtaMedia(persone));
    }
}
```

**Output**:
```
Età media: 27.666666666666668
```

### Conclusione

Gli array di oggetti sono strumenti potenti per organizzare dati complessi. Consentono di trattare gruppi di oggetti con una struttura ordinata e iterabile, facilitando l'elaborazione collettiva di informazioni. Utilizzando array di oggetti, è possibile gestire insiemi omogenei di dati complessi, come insiemi di utenti, prodotti o entità di qualsiasi tipo in applicazioni Java.

[06-for potenziato](06-for%20potenziato.md) - [INDICE](README.md) - [08-Array multidimensionali](08-Array%20multidimensionali.md)