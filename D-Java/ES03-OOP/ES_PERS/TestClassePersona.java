class Persona {
    // Campi o attributi della classe
    String nome;
    int eta;

    // Costruttore della classe
    public Persona(String nome, int eta) {
        this.nome = nome;
        this.eta = eta;
    }

    // Metodo della classe
    public void saluta() {
        System.out.println("Ciao, sono " + nome + " e ho " + eta + " anni.");
    }
}


public class TestClassePersona {

    public static void main(String[] args) {
        // Creazione di un oggetto Persona
        Persona beatrice = new Persona("Beatrice", 25);
        // Chiamata al metodo saluta
        beatrice.saluta();

        // Creazione di un altro oggetto Persona
        Persona bob = new Persona("Bob", 30);
        // Chiamata al metodo saluta per il secondo oggetto
        bob.saluta();
    }
}
