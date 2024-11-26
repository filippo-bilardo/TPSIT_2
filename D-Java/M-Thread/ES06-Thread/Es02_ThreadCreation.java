public class Es02_ThreadCreation implements Runnable {

    public Es02_ThreadCreation() {
        Thread ct = Thread.currentThread();// CLASSE CORRENTE
        ct.setName("Thread principale");
        Thread t = new Thread(this, "Thread figlio");// THREAD
        // FIGLIO
        System.out.println("Thread attuale: " + ct);
        System.out.println("Thread creato: " + t);
        t.start();// AVVIA THREAD FIGLIO
        try {
            Thread.sleep(3000);// FERMA THREAD PADRE PER 3 SECONDI
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
        new Es02_ThreadCreation();
    }
}
