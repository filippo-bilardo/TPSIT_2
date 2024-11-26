class MioRunnable implements Runnable {
    public void run() {
        // Codice da eseguire nel thread
        for(int i = 0; i < 5; i++) {
            System.out.println("Thread in esecuzione: " + Thread.currentThread().getName());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Thread interrotto");
            }
        }
    }
}

public class ThreadJoin {
    public static void main(String[] args) {
        Thread thread1 = new Thread(new MioRunnable());
        Thread thread2 = new Thread(new MioRunnable());
        thread1.start(); // Avvio del thread
        try {
            thread1.join();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } // Attendo la terminazione del thread
        thread2.start(); // Avvio del thread    
    }
}