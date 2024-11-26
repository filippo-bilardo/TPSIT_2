## 5 Applicazione di esempio: mescolare e distribuire un mazzo di carte da gioco

Svilupperemo una semplice applicazione che simula un mazzo di carte da gioco. Implementeremo due classi fondamentali:

1. **Classe `Carta`**: rappresenta una singola carta del mazzo, contenente un seme (come "cuori" o "picche") e un valore (come "Asso", "Re" o "7").
2. **Classe `MazzoDiCarte`**: rappresenta un mazzo di 52 carte da gioco, gestisce l'inizializzazione del mazzo, la mischiatura e la distribuzione delle carte.

Questa applicazione ci permetterà di comprendere come gestire oggetti complessi utilizzando array e array dinamici, e come manipolare collezioni di oggetti in modo efficace.

### La Classe `Carta`

La classe `Carta` rappresenta una singola carta da gioco. Ogni carta ha due attributi principali:
- **Seme**: il seme della carta, come "Cuori", "Quadri", "Fiori" o "Picche".
- **Valore**: il valore della carta, come "Asso", "2", "3", …, "Re".

Definiamo la classe `Carta` con questi attributi e un costruttore per inizializzarli. Aggiungiamo anche un metodo `toString()` per rappresentare la carta come una stringa leggibile, utile quando vogliamo visualizzare una carta.

```java
public class Carta {
    private final String seme;
    private final String valore;

    // Costruttore per inizializzare seme e valore della carta
    public Carta(String seme, String valore) {
        this.seme = seme;
        this.valore = valore;
    }

    // Metodo toString per restituire una rappresentazione della carta
    @Override
    public String toString() {
        return valore + " di " + seme;
    }
}
```

Con questa classe, possiamo rappresentare ogni carta del mazzo con una combinazione di `seme` e `valore`.

### La Classe `MazzoDiCarte`

La classe `MazzoDiCarte` rappresenta l'intero mazzo di carte da gioco. Dovremo creare 52 oggetti `Carta`, uno per ciascuna combinazione di semi e valori, memorizzarli in un array, e fornire metodi per mischiare e distribuire le carte.

1. **Creazione del Mazzo**: Il mazzo deve essere composto da 52 carte uniche, con tutte le possibili combinazioni di semi e valori.
2. **Mischiare il Mazzo**: Utilizziamo una funzione di mischiatura che randomizzi l'ordine delle carte.
3. **Distribuire le Carte**: Un metodo che restituisce la carta in cima al mazzo e avanza l’indice della "carta corrente".

Definiamo i semi e i valori come array di `String` e li utilizziamo per creare tutte le combinazioni di carte nel costruttore. Ecco il codice per `MazzoDiCarte`:

```java
import java.util.Random;

public class MazzoDiCarte {
    private Carta[] mazzo;   // Array che contiene tutte le carte
    private int cartaCorrente; // Indice della prossima carta da distribuire
    private static final int NUMERO_CARTE = 52; // Numero di carte in un mazzo

    // Costruttore per inizializzare il mazzo di carte
    public MazzoDiCarte() {
        String[] semi = {"Cuori", "Quadri", "Fiori", "Picche"};
        String[] valori = {"Asso", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Regina", "Re"};
        
        mazzo = new Carta[NUMERO_CARTE];
        cartaCorrente = 0; // All'inizio, la prima carta è in cima

        // Creazione del mazzo
        int index = 0;
        for (String seme : semi) {
            for (String valore : valori) {
                mazzo[index++] = new Carta(seme, valore);
            }
        }
    }

    // Metodo per mischiare le carte nel mazzo
    public void mescola() {
        Random random = new Random();

        for (int i = 0; i < mazzo.length; i++) {
            // Genera un indice casuale per lo scambio
            int indiceScambio = random.nextInt(NUMERO_CARTE);
            Carta temp = mazzo[i];
            mazzo[i] = mazzo[indiceScambio];
            mazzo[indiceScambio] = temp;
        }

        // Reset del contatore delle carte distribuite
        cartaCorrente = 0;
    }

    // Metodo per distribuire una carta dal mazzo
    public Carta distribuisciCarta() {
        if (cartaCorrente < mazzo.length) {
            return mazzo[cartaCorrente++]; // Restituisce la carta e incrementa l'indice
        } else {
            System.out.println("Tutte le carte sono state distribuite.");
            return null; // Ritorna null se tutte le carte sono state distribuite
        }
    }
}
```

### Spiegazione del Codice

- **Creazione del Mazzo**: Nel costruttore `MazzoDiCarte`, creiamo un array di `Carta` chiamato `mazzo` che contiene esattamente 52 oggetti `Carta`, uno per ogni combinazione di seme e valore.
- **Metodo `mescola()`**: Utilizziamo la classe `Random` per generare un indice casuale, con cui scambiamo le carte all’interno dell’array `mazzo`. Questo processo viene ripetuto per ogni carta nel mazzo per garantire una mischiatura efficace.
- **Metodo `distribuisciCarta()`**: Restituisce la "prossima" carta nel mazzo (indicata da `cartaCorrente`) e incrementa l’indice per la distribuzione successiva. Se tutte le carte sono state distribuite, il metodo restituisce `null` e stampa un messaggio di avviso.

#### Utilizzo delle Classi `Carta` e `MazzoDiCarte`

Ora che abbiamo definito le classi `Carta` e `MazzoDiCarte`, possiamo creare un semplice programma per mescolare e distribuire tutte le carte di un mazzo:

```java
public class GiocoCarte {
    public static void main(String[] args) {
        MazzoDiCarte mazzo = new MazzoDiCarte(); // Creiamo un nuovo mazzo di carte

        System.out.println("Mischio il mazzo...");
        mazzo.mescola(); // Mischiamo il mazzo

        System.out.println("\nDistribuzione delle carte:");
        for (int i = 0; i < 52; i++) {
            System.out.println(mazzo.distribuisciCarta());
        }
    }
}
```

**Esempio di Output:**
```
Mischio il mazzo...

Distribuzione delle carte:
Asso di Cuori
3 di Fiori
Re di Quadri
...
10 di Picche
```

### Conclusione
Con queste classi, abbiamo implementato una simulazione completa di un mazzo di carte da gioco. La classe `Carta` rappresenta ogni carta con un seme e un valore, mentre la classe `MazzoDiCarte` gestisce l’intero mazzo, fornendo metodi per mescolare e distribuire le carte. Questo esempio mostra l’utilità degli array e degli oggetti per la gestione e manipolazione di dati complessi, e serve come base per ulteriori estensioni come l’implementazione di giochi di carte più sofisticati.

[04-Eccezioni.md](04-Eccezioni.md) - [INDICE](README.md) - [06-for potenziato](06-for%20potenziato.md)
