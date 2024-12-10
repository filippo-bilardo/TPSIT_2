class Animale {
    protected String nome;
    private int eta;

    public Animale(String nome) {
        this.nome = nome;
    }
    public Animale(String nome, int eta) {
        this.nome = nome;
        this.eta = eta;
    }

    public void dorme() {
        System.out.println(nome + " sta dormendo.");
    }

    public int getEta() {
        return eta;
    }
}

class Gatto extends Animale {
    public Gatto(String nome) {
        super(nome); // Utilizza il costruttore della superclasse
    }
    public Gatto(String nome, int eta) {
        super(nome, eta); // Utilizza il costruttore della superclasse
    }

    public void miagola() {
        System.out.println(nome + " sta miagolando.");
    }

    @Override
    public String toString() {
        return "Gatto [nome=" + nome + ", eta=" + getEta() + "]";
    } 
}

public class MainAnimale2 {
    public static void main(String[] args) {
        Gatto gatto = new Gatto("Micio", 5);
        gatto.dorme();   // Metodo della superclasse
        gatto.miagola(); // Metodo specifico della sottoclasse
        System.out.println(gatto); // Metodo toString() sovrascritto
    }
}