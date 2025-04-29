/**
 * Esempio che dimostra l'uso delle priorità dei thread in Java
 * e come queste possono influenzare lo scheduling dei thread
 */
public class ES03_PrioritaThread {
    public static void main(String[] args) throws InterruptedException {
        // Contatori per tracciare le iterazioni completate da ciascun thread
        Counter counterLow = new Counter();
        Counter counterNormal = new Counter();
        Counter counterHigh = new Counter();
        
        // Creiamo thread con diverse priorità
        Thread lowPriorityThread = new Thread(() -> {
            long startTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - startTime < 5000) {
                counterLow.increment();
            }
        }, "LowPriorityThread");
        
        Thread normalPriorityThread = new Thread(() -> {
            long startTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - startTime < 5000) {
                counterNormal.increment();
            }
        }, "NormalPriorityThread");
        
        Thread highPriorityThread = new Thread(() -> {
            long startTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - startTime < 5000) {
                counterHigh.increment();
            }
        }, "HighPriorityThread");
        
        // Impostazione delle priorità
        lowPriorityThread.setPriority(Thread.MIN_PRIORITY); // 1
        normalPriorityThread.setPriority(Thread.NORM_PRIORITY); // 5 (default)
        highPriorityThread.setPriority(Thread.MAX_PRIORITY); // 10
        
        // Visualizziamo le priorità impostate
        System.out.println("Priorità di " + lowPriorityThread.getName() + ": " 
                          + lowPriorityThread.getPriority());
        System.out.println("Priorità di " + normalPriorityThread.getName() + ": " 
                          + normalPriorityThread.getPriority());
        System.out.println("Priorità di " + highPriorityThread.getName() + ": " 
                          + highPriorityThread.getPriority());
        
        // Avvio dei thread
        System.out.println("Avvio dei thread...");
        lowPriorityThread.start();
        normalPriorityThread.start();
        highPriorityThread.start();
        
        // Attesa del completamento dei thread (5 secondi)
        lowPriorityThread.join();
        normalPriorityThread.join();
        highPriorityThread.join();
        
        // Visualizziamo i risultati
        System.out.println("\nRisultati dopo 5 secondi di esecuzione:");
        System.out.println(lowPriorityThread.getName() + " ha eseguito " 
                          + counterLow.getCount() + " iterazioni");
        System.out.println(normalPriorityThread.getName() + " ha eseguito " 
                          + counterNormal.getCount() + " iterazioni");
        System.out.println(highPriorityThread.getName() + " ha eseguito " 
                          + counterHigh.getCount() + " iterazioni");
        
        System.out.println("\nNOTA: I risultati possono variare significativamente in base "
                          + "al sistema operativo, alla JVM e al carico del sistema.");
    }
    
    static class Counter {
        private long count = 0;
        
        public void increment() {
            count++;
        }
        
        public long getCount() {
            return count;
        }
    }
}
