
class MioThread extends Thread {

    @Override
    public void run() {
        System.out.println("Il thread è in esecuzione!");
        // Codice da eseguire nel thread
    }
    
}

class MioThread2 extends Thread {

    @Override
    public void run() {
        System.out.println("Il secondo threadè in esecuzione!");
        // Ciclo infinito
        while (true) {
            System.out.println("Il secondo thread è in esecuzione!");
            try {
                Thread.sleep(1000); // Pausa di 1 secondo
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
}


public class esempio01 {
    public static void main(String[] args) {
        MioThread thread = new MioThread();
        thread.start(); // Avvia il thread
        MioThread2 thread2 = new MioThread2();
        thread2.start(); // Avvia il secondo thread
        MioThread2 thread3 = new MioThread2();
        thread3.start(); // Avvia il secondo thread
    }
    
}
