public class EsempioEccezioneGestita {
    public static void main(String[] args) {
        int numero = 10;
        int divisore = 0;
        int risultato = 0;

        try {
            // Questo genera un'eccezione di tipo ArithmeticException
            risultato = numero / divisore;
        } catch (ArithmeticException e) {
            System.out.println("Errore: " + e.getMessage());
        }


        System.out.println("Risultato: " + risultato);
    }
}