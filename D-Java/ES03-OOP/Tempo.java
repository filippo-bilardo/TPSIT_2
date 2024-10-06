/**
 * ESERCIZIO 3 -- OOP
 * 
 * Realizzare una classe Tempo che rappresenti un orario (ore, minuti e secondi).
 * La classe deve avere:
 * - un costruttore con ore, minuti e secondi
 * - un costruttore con ore e minuti (secondi = 0)
 * - un costruttore di default (00:00:00)
 * - un metodo per incrementare i secondi
 * - un metodo per calcolare la differenza tra due oggetti Tempo
 * - un metodo toString() per la visualizzazione
 * - un metodo equals() per verificare l'uguaglianza
 * 
 * Realizzare una classe TempoTest con un metodo main() per verificare il funzionamento della classe Tempo.
 * 
 * Esempio di output:
 * Prima dell'incremento: 23:59:59
 * Dopo l'incremento: 00:00:00
 * Differenza tra t1 e t2: 11:30:00
 * t1 è uguale a t3? true
 * t1 è uguale a t4? true
 * 
 * Suggerimento: per calcolare la differenza tra due orari, convertire entrambi in secondi e fare la differenza.
 * Per convertire un orario in secondi: ore * 3600 + minuti * 60 + secondi
 * 
 * @version 05/10/2024
 * 
 * Aggiungere un metodo decrementaSecondi() che decrementa i secondi e 
 * gestisce l'underflow, scalando minuti e ore.
 * Aggiungere un metodo confronta(Tempo t) che confronta due oggetti Tempo e 
 * restituisce 1 se il primo è maggiore, -1 se è minore e 0 se sono uguali.
 */
public class Tempo {
    private int ore;
    private int minuti;
    private int secondi;

    // Costruttore con ore, minuti e secondi
    public Tempo(int ore, int minuti, int secondi) {
        if (ore < 0 || ore >= 24) {
            throw new IllegalArgumentException("Le ore devono essere tra 0 e 23.");
        }
        if (minuti < 0 || minuti >= 60) {
            throw new IllegalArgumentException("I minuti devono essere tra 0 e 59.");
        }
        if (secondi < 0 || secondi >= 60) {
            throw new IllegalArgumentException("I secondi devono essere tra 0 e 59.");
        }
        this.ore = ore;
        this.minuti = minuti;
        this.secondi = secondi;
    }

    // Costruttore con ore e minuti (secondi = 0)
    public Tempo(int ore, int minuti) {
        this(ore, minuti, 0);
    }

    // Costruttore di default (00:00:00)
    public Tempo() {
        this(0, 0, 0);
    }

    // Metodo per incrementare i secondi
    public void incrementaSecondi() {
        this.secondi++;
        if (this.secondi == 60) {
            this.secondi = 0;
            this.minuti++;
            if (this.minuti == 60) {
                this.minuti = 0;
                this.ore++;
                if (this.ore == 24) {
                    this.ore = 0;  // Reset ore a mezzanotte
                }
            }
        }
    }

    // Metodo per calcolare la differenza tra due oggetti Tempo
    public Tempo calcolaDifferenza(Tempo t) {
        int diffSecondi = Math.abs(this.ore * 3600 + this.minuti * 60 + this.secondi
                                    - t.ore * 3600 - t.minuti * 60 - t.secondi);
        int ore = diffSecondi / 3600;
        int minuti = (diffSecondi % 3600) / 60;
        int secondi = diffSecondi % 60;
        return new Tempo(ore, minuti, secondi);
    }

    // Sovrascrittura di toString()
    @Override
    public String toString() {
        return String.format("%02d:%02d:%02d", ore, minuti, secondi);
    }

    // Sovrascrittura di equals()
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Tempo tempo = (Tempo) obj;
        return ore == tempo.ore && minuti == tempo.minuti && secondi == tempo.secondi;
    }
}
