public class es_string {
    public static void main(String[] args) {
        String testo = null;
        try {
            // Tentativo di accedere a un metodo su un oggetto null
            System.out.println(testo.length());
        } catch (NullPointerException e) {
            System.out.println("Errore: tentativo di accedere a un oggetto null");
        } finally {
            System.out.println("Blocco eseguito sempre.");
        }
    }    
}
