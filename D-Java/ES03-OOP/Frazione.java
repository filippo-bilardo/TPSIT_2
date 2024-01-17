/**
 * Frazione.java
 * Esempio di definizia di una classe in Java
 * Contiene un metodo main() per la verifica
 * Utilizza il costruttore per passare due parametri
 * 
 * La classe Frazione rappresenta una frazione
 * e permette di eseguire le operazioni di base
 */

public class Frazione {
    // Attributi
    private int numeratore;
    private int denominatore;
    
    // Costruttore
    // utilizzo this per distinguere i parametri dagli attributi
    // se non volessimo usare this, potremmo usare nomi diversi
    // per i parametri, ad esempio num e den
    public Frazione(int num, int den) {
        numeratore = num;
        denominatore = den;
    }
    
    // Metodi
    public int getNumeratore() {
        return numeratore;
    }
    
    public int getDenominatore() {
        return denominatore;
    }
    
    public void setNumeratore(int numeratore) {
        this.numeratore = numeratore;
    }
    
    public void setDenominatore(int denominatore) {
        this.denominatore = denominatore;
    }
    
    public Frazione somma(Frazione f) {
        int num = numeratore * f.denominatore + f.numeratore * denominatore;
        int den = denominatore * f.denominatore;
        return new Frazione(num, den);
    }
    
    public Frazione sottrazione(Frazione f) {
        int num = numeratore * f.denominatore - f.numeratore * denominatore;
        int den = denominatore * f.denominatore;
        return new Frazione(num, den);
    }
    
    public Frazione moltiplicazione(Frazione f) {
        int num = numeratore * f.numeratore;
        int den = denominatore * f.denominatore;
        return new Frazione(num, den);
    }
    
    public Frazione divisione(Frazione f) {
        int num = numeratore * f.denominatore;
        int den = denominatore * f.numeratore;
        return new Frazione(num, den);
    }
    
    public String toString() {
        return numeratore + "/" + denominatore;
    }
    
    public static void main(String[] args) {
        Frazione f1 = new Frazione(1, 2);
        Frazione f2 = new Frazione(1, 3);
        System.out.println("f1 = " + f1);
        System.out.println("f2 = " + f2);
        System.out.println("f1 + f2 = " + f1.somma(f2));
        System.out.println("f1 - f2 = " + f1.sottrazione(f2));
        System.out.println("f1 * f2 = " + f1.moltiplicazione(f2));
        System.out.println("f1 / f2 = " + f1.divisione(f2));
    }
}




 