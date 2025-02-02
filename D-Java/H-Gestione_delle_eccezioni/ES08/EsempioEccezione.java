public class EsempioEccezione {
    public static void main(String[] args) {
        int numero = 10;
        int divisore = 0;

        // Questo genera un'eccezione di tipo ArithmeticException
        int risultato = numero / divisore;

        System.out.println("Risultato: " + risultato);
    }
}