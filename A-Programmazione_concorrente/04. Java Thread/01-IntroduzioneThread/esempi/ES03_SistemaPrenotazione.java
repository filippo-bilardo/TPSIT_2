// Esempio pratico: Stati dei Thread in un sistema di prenotazione biglietti
// Questo esempio mostra la transizione degli stati dei thread (NEW, RUNNABLE, TIMED_WAITING, WAITING, TERMINATED)
// in un contesto simile a quello richiesto, con commenti esplicativi per ogni fase.

class PrenotazioneBiglietto implements Runnable {
    @Override
    public void run() {
        try {
            // Stato TIMED_WAITING: il thread dorme per 200ms
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Stato di bookingThread mentre mainThread è in attesa: " + ES03_SistemaPrenotazione.mainThread.getState());
        try {
            // Stato TIMED_WAITING: il thread dorme per altri 100ms
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class ES03_SistemaPrenotazione implements Runnable {
    public static Thread mainThread;
    public static ES03_SistemaPrenotazione sistema;

    @Override
    public void run() {
        PrenotazioneBiglietto prenotazione = new PrenotazioneBiglietto();
        Thread bookingThread = new Thread(prenotazione, "bookingThread");

        System.out.println("Stato di bookingThread dopo la creazione: " + bookingThread.getState());
        //Output: Stato di bookingThread dopo la creazione: NEW

        bookingThread.start();
        System.out.println("Stato di bookingThread dopo l'avvio: " + bookingThread.getState());
        //Output: Stato di bookingThread dopo l'avvio: RUNNABLE

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Stato di bookingThread dopo sleep: " + bookingThread.getState());
        //Output: Stato di bookingThread dopo sleep: TIMED_WAITING

        try {
            // Il mainThread entra in stato WAITING
            bookingThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Stato di bookingThread dopo ha terminato: " + bookingThread.getState());
        //Output: Stato di bookingThread dopo ha terminato: TERMINATED
    }

    public static void main(String[] args) {
        sistema = new ES03_SistemaPrenotazione();
        mainThread = new Thread(sistema, "mainThreadSistema");

        System.out.println("Stato di mainThread dopo la creazione: " + mainThread.getState());
        //Output: Stato di mainThread dopo la creazione: NEW
        mainThread.start();
        System.out.println("Stato di mainThread dopo l'avvio: " + mainThread.getState());
        //Output: Stato di mainThread dopo l'avvio: RUNNABLE
    }
}
/**
 * Output:
 
    Stato di mainThread dopo la creazione: NEW
    Stato di mainThread dopo l'avvio: RUNNABLE
    Stato di bookingThread dopo la creazione: NEW
    Stato di bookingThread dopo l'avvio: RUNNABLE
    Stato di bookingThread dopo sleep: TIMED_WAITING
    Stato di bookingThread mentre mainThread è in attesa: WAITING
    Stato di bookingThread dopo ha terminato: TERMINATED
 */