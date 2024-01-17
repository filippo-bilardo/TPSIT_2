class MiaClasseBase {
    private String name;
    public MiaClasseBase(String name) {this.name = name;}
}
class MiaClasse {
    private String name;
    public MiaClasse(String name) {this.name = name;}
    @Override public String toString() {
        return "MiaClasse, name=" + name;
    }
}

public class MiaClasseTest {
    
    public static void main(String[] args) {
        // Creazione di un'istanza della classe
        MiaClasse oggetto = new MiaClasse("Alice");
        // Chiamata al metodo toString() e Stampa
        System.out.println(oggetto.toString());

        // Creazione di un'istanza della classe
        MiaClasseBase oggetto1 = new MiaClasseBase("Mario");
        // Chiamata al metodo toString() e Stampa
        System.out.println(oggetto1.toString());
        
    }
}

