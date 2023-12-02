/**
 * Un oggetto della classe AgendaTelefonica permette di gestire una semplice 
 * agenda (rubrica) telefonica. Ogni utenza dell’agenda ha un nome, un cognome 
 * e un numero di telefono. L’agenda può gestire un numero limitato di utenze. 
 * Tale numero deve essere scelto all’atto della creazione di un oggetto della 
 * classe AgendaTelefonica.
 * 
 */
public class AgendaTelefonica {
    private Contatto[] rubrica; // Array per memorizzare i contatti
    private int numeroMassimoContatti; // Numero massimo di contatti gestibili
    private int numeroContattiInseriti; // Contatore per il numero attuale di contatti inseriti

    // Costruttore
    public AgendaTelefonica(int numeroMassimoContatti) {
        this.numeroMassimoContatti = numeroMassimoContatti;
        this.rubrica = new Contatto[numeroMassimoContatti];
        this.numeroContattiInseriti = 0;
    }

    // Metodo per aggiungere un contatto all'agenda
    public void aggiungiContatto(String nome, String cognome, String numeroTelefono) {
        if (numeroContattiInseriti < numeroMassimoContatti) {
            rubrica[numeroContattiInseriti] = new Contatto(nome, cognome, numeroTelefono);
            numeroContattiInseriti++;
            System.out.println("Contatto aggiunto con successo!");
        } else {
            System.out.println("Agenda piena. Impossibile aggiungere nuovi contatti.");
        }
    }

    // Metodo per visualizzare tutti i contatti presenti nell'agenda
    public void visualizzaContatti() {
        System.out.println("Contatti nella rubrica:");
        for (int i = 0; i < numeroContattiInseriti; i++) {
            System.out.println(rubrica[i]);
        }
    }
    // Metodo per visualizzare tutti i contatti presenti nell'agenda
    public void ricercaContatto(String ricerca) {
        System.out.println("Contatti nella rubrica:");
        for (int i = 0; i < numeroContattiInseriti; i++) {
            if (rubrica[i].toString().contains(ricerca)) {
                System.out.println(rubrica[i]);
            }
        }
    } 

    // Altro metodi e funzionalità possono essere aggiunti secondo le necessità
}

// Classe Contatto
class Contatto {
    private String nome;
    private String cognome;
    private String numeroTelefono;

    public Contatto(String nome, String cognome, String numeroTelefono) {
        this.nome = nome;
        this.cognome = cognome;
        this.numeroTelefono = numeroTelefono;
    }

    @Override
    public String toString() {
        return nome + " " + cognome + ": " + numeroTelefono;
    }
}
