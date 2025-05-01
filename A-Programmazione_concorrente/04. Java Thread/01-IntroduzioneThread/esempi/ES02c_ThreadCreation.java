/**
 * Esempio di creazione di un thread in Java
 * 
 * Questo programma mostra come creare un thread in Java utilizzando due
 * approcci: estensione della classe Thread e implementazione dell'interfaccia
 * Runnable.
 */
public class ES02c_ThreadCreation implements Runnable {

    public ES02c_ThreadCreation() {

        Thread ct = Thread.currentThread(); // Thread padre (main thread)
        ct.setName("Thread principale");

        Thread t = new Thread(this, "Thread figlio"); // Thread figlio
        t.start(); // Avvia il thread figlio

        System.out.println("Thread attuale: " + ct);
        System.out.println("Thread creato: " + t);

        try {
            Thread.sleep(3000); // Ferma il thread principale per 3 secondi
        } catch (InterruptedException e) {
            System.out.println("principale interrotto");
        }
        System.out.println("uscita Thread principale");
    }

    public void run() {
        try {
            for (int i = 5; i > 0; i--) {
                System.out.println("" + i);
                Thread.sleep(1000); 
            }
        } catch (InterruptedException e) {
            System.out.println("Thread figlio interrotto");
        }
        System.out.println("uscita Thread figlio");
    }

    public static void main(String args[]) {
        new ES02c_ThreadCreation();
    }
}
