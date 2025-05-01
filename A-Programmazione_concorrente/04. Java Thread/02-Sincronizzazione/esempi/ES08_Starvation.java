// Esempio di Starvation
// Un thread non ottiene mai accesso alle risorse necessarie
public class ES08_Starvation {
    private static final Object lock = new Object();

    public static void main(String[] args) {
        Thread highPriority = new Thread(() -> {
            while (true) {
                synchronized (lock) {
                    System.out.println("Thread alta priorità in esecuzione");
                }
                try { Thread.sleep(10); } catch (InterruptedException e) {}
            }
        });
        Thread lowPriority = new Thread(() -> {
            while (true) {
                synchronized (lock) {
                    System.out.println("Thread bassa priorità in esecuzione");
                }
                try { Thread.sleep(10); } catch (InterruptedException e) {}
            }
        });
        highPriority.setPriority(Thread.MAX_PRIORITY);
        lowPriority.setPriority(Thread.MIN_PRIORITY);
        highPriority.start();
        lowPriority.start();
    }
}