public class PersonaBis {
    String nome;
    int eta;
    void saluta() {System.out.println("Ciao " + nome);}

    //Costruttore di default
    PersonaBis() {
        nome = "Pinco";
        eta = 30;
    }

    //Aggiungiamo un costruttore
    PersonaBis(String nome, int eta) {
        this.nome = nome;
        this.eta = eta;
    }

    public static void main(String[] args) {

        //Il seguente codice darà errore in quanto il 
        //costruttore non è più quello di default
        PersonaBis pb = new PersonaBis();
        Persona p = new Persona();
        p.nome = "Mario";
        p.eta = 30;
        pb.saluta();
    }
}
