/*
Crea una classe base Forma con i seguenti attributi e metodi:

Attributi: nome (una stringa che descrive il tipo di forma, ad esempio "Cerchio", "Rettangolo").
Metodi:

calcolaArea(): restituisce 0 (sarà sovrascritto nelle sottoclassi).
calcolaPerimetro(): restituisce 0 (sarà sovrascritto nelle sottoclassi).
descrizione(): stampa il nome della forma.

*/

class Forma {

    //attributo nome
    private String nome;

    //Costruttore
    public Forma(String nome){
        this.nome = nome;
    }

    //Metodi
    //calcolaArea(): restituisce 0 (sarà sovrascritto nelle sottoclassi).
    public double calcolaArea() {
        return 0;
    }

    //calcolaPerimetro(): restituisce 0 (sarà sovrascritto nelle sottoclassi).
    public double calcolaPerimetro(){
        return 0;
    }
    //descrizione(): stampa il nome della forma.
    public void descrizione() {
        System.out.println("Questa forma è un " + nome);
    }
}

public class FormaTest {
    public static void main(String[] args) {
        Forma cerchio = new Forma("Cerchiooooo");
        Forma rettangolo = new Forma("Rectang");

        System.out.printf("L'area è %d ", cerchio.calcolaArea() );
        cerchio.descrizione();

        System.out.printf("L'area è %d ", rettangolo.calcolaArea() );
        rettangolo.descrizione();
 
        Cerchio cerchiogiusto = new Cerchio("Cerchio", 10);
        System.out.printf("L'area è %d ", cerchiogiusto.calcolaArea() );
        cerchiogiusto.descrizione();

        Triangolo tri = new Triangolo("triiiii",10 , 5);
        System.out.printf("L'area è %d ", tri.calcolaArea() );
        System.out.printf("L'area è %d ", tri.Area() );
        cerchiogiusto.descrizione();

 
    }
}

class Cerchio extends Forma {
    private double raggio;
    private final double PIGRECO = 3.14;


    public Cerchio(String nome, double raggio) {
        super(nome);
        this.raggio = raggio;
    }

    @Override
    public double calcolaArea() {
        return PIGRECO * raggio * raggio;
    }
}

class Triangolo extends Forma {
    //private int area;
    private double base;
    private double altezza;

    public Triangolo(String nome, double b, double h) {
        super(nome);
        base = b;
        altezza = h;
    }

    
    public double Area() {
        return 99;
        //System.out.printf("L'area è %f", base*altezza/2);
    }

}
