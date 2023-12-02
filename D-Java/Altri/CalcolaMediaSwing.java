import javax.swing.JOptionPane;

public class CalcolaMediaSwing {

    public static void main(String[] args) {
        // Chiede all'utente di inserire tre numeri reali
        double numero1 = chiediNumero("Inserisci il primo numero:");
        double numero2 = chiediNumero("Inserisci il secondo numero:");
        double numero3 = chiediNumero("Inserisci il terzo numero:");

        // Calcola la media aritmetica
        double media = calcolaMedia(numero1, numero2, numero3);

        // Mostra la media in una finestra di dialogo
        mostraRisultato("La media aritmetica è: " + media);
    }

    // Metodo per chiedere all'utente di inserire un numero
    private static double chiediNumero(String messaggio) {
        String input = JOptionPane.showInputDialog(messaggio);
        return Double.parseDouble(input);
    }

    // Metodo per calcolare la media aritmetica
    private static double calcolaMedia(double num1, double num2, double num3) {
        return (num1 + num2 + num3) / 3;
    }

    // Metodo per mostrare un risultato in una finestra di dialogo
    private static void mostraRisultato(String risultato) {
        JOptionPane.showMessageDialog(null, risultato);
    }
}
