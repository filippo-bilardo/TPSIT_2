// Esempio di Race Condition
// Il risultato finale dipende dall'ordine di esecuzione dei thread
public class ES06_RaceCondition {
    private static int contatore = 0;

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                contatore++;
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                contatore++;
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("Valore atteso: 20000");
        System.out.println("Valore reale: " + contatore);
        System.out.println("(Esempio di Race Condition: il valore reale spesso NON è 20000)");
    }
}