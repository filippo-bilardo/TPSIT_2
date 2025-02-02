public class EccezioneAritmetica {

    public static void main(String[] args) {

        int a = 10;
        int b = 0;

        try {
            System.out.printf("La divisione tra a e b è = %d", a/b);
        } 
        catch (Exception ex) {
            System.out.printf("Errore generico");
        }
    }
}
