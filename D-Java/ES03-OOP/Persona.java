public class Persona {
    String nome;
    int eta;
    void saluta() {System.out.println("Ciao " + nome);}

    public static void main(String[] args) {
        Persona p = new Persona();
        p.nome = "Mario";
        p.eta = 30;
        p.saluta();
    }
}
