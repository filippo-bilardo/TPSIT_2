interface InterfacciaA {
    default void metodo() {
        System.out.println("Metodo di default da InterfacciaA");
    }
}

interface InterfacciaB {
    default void metodo() {
        System.out.println("Metodo di default da InterfacciaB");
    }
}

class ClasseImplementazione implements InterfacciaA, InterfacciaB {
    @Override
    public void metodo() {
        // Risoluzione dell'ambiguità
        InterfacciaA.super.metodo();
        InterfacciaB.super.metodo();
        System.out.println("Metodo sovrascritto nella classe");
    }
}

public class EredMultipla {

    public static void main(String[] args) {
        ClasseImplementazione obj = new ClasseImplementazione();
        obj.metodo();
    }
}

