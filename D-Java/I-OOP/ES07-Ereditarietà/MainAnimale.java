class Animale {
    public void mangia() {
        System.out.println("L'animale sta mangiando.");
    }
}

class Cane extends Animale {
    public void abbaia() {
        System.out.println("Il cane sta abbaiando.");
    }
}

public class MainAnimale {
    public static void main(String[] args) {
        Cane mioCane = new Cane();
        Animale mioAnimale = new Cane();
        mioCane.mangia(); // Metodo ereditato dalla superclasse
        mioCane.abbaia(); // Metodo specifico della sottoclasse
        mioAnimale.mangia(); // Metodo ereditato dalla superclasse
        //smioAnimale.abbaia(); // Errore: il metodo abbaia() non è presente nella superclasse
    }
}