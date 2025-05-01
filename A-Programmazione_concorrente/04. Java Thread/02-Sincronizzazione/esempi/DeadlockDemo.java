/**
 * DeadlockDemo.java
 * 
 * Questo esempio dimostra come si può verificare un deadlock quando due thread
 * cercano di acquisire gli stessi lock ma in ordine diverso.
 * Inoltre, mostra come prevenire il deadlock utilizzando un ordine coerente
 * di acquisizione dei lock.
 */
public class DeadlockDemo {
    // Risorse che verranno bloccate
    private static final Object RISORSA_A = new Object();
    private static final Object RISORSA_B = new Object();
    
    // Flag per scegliere tra la versione con deadlock e quella sicura
    private static boolean preveniDeadlock = false;
    
    public static void main(String[] args) {
        System.out.println("Dimostrazione di Deadlock");
        System.out.println("=======================\n");
        
        if (args.length > 0 && args[0].equalsIgnoreCase("sicuro")) {
            preveniDeadlock = true;
            System.out.println("Modalità: Prevenzione deadlock attiva\n");
        } else {
            System.out.println("Modalità: Deadlock possibile\n");
            System.out.println("Per eseguire la versione sicura: java DeadlockDemo sicuro\n");
        }
        
        // Creiamo e avviamo i due thread che possono causare deadlock
        Thread thread1 = new Thread(new TaskA(), "Thread-A");
        Thread thread2 = new Thread(new TaskB(), "Thread-B");
        
        thread1.start();
        thread2.start();
        
        // Attendiamo che i thread completino (o entrino in deadlock)
        try {
            thread1.join();
            thread2.join();
            System.out.println("\nEntrambi i thread hanno completato l'esecuzione con successo!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Task che acquisisce prima la risorsa A e poi la risorsa B
     */
    static class TaskA implements Runnable {
        @Override
        public void run() {
            if (preveniDeadlock) {
                eseguiSicuro();
            } else {
                eseguiConRischioDeadlock();
            }
        }
        
        private void eseguiConRischioDeadlock() {
            String threadName = Thread.currentThread().getName();
            
            System.out.println(threadName + ": Tentativo di acquisire la risorsa A");
            synchronized (RISORSA_A) {
                System.out.println(threadName + ": Risorsa A acquisita");
                
                // Simuliamo un'operazione che richiede tempo
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
                System.out.println(threadName + ": Tentativo di acquisire la risorsa B");
                synchronized (RISORSA_B) {
                    System.out.println(threadName + ": Risorsa B acquisita");
                    
                    // Utilizziamo entrambe le risorse
                    System.out.println(threadName + ": Utilizzo di entrambe le risorse");
                }
                System.out.println(threadName + ": Rilasciata la risorsa B");
            }
            System.out.println(threadName + ": Rilasciata la risorsa A");
        }
        
        private void eseguiSicuro() {
            String threadName = Thread.currentThread().getName();
            
            // Acquisizione dei lock sempre nello stesso ordine (A poi B)
            System.out.println(threadName + ": Tentativo di acquisire la risorsa A");
            synchronized (RISORSA_A) {
                System.out.println(threadName + ": Risorsa A acquisita");
                
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
                System.out.println(threadName + ": Tentativo di acquisire la risorsa B");
                synchronized (RISORSA_B) {
                    System.out.println(threadName + ": Risorsa B acquisita");
                    
                    // Utilizziamo entrambe le risorse
                    System.out.println(threadName + ": Utilizzo di entrambe le risorse");
                }
                System.out.println(threadName + ": Rilasciata la risorsa B");
            }
            System.out.println(threadName + ": Rilasciata la risorsa A");
        }
    }
    
    /**
     * Task che acquisisce prima la risorsa B e poi la risorsa A (ordine inverso rispetto a TaskA)
     * Questo può causare deadlock se entrambi i thread acquisiscono la prima risorsa
     * e poi tentano di acquisire la seconda.
     */
    static class TaskB implements Runnable {
        @Override
        public void run() {
            if (preveniDeadlock) {
                eseguiSicuro();
            } else {
                eseguiConRischioDeadlock();
            }
        }
        
        private void eseguiConRischioDeadlock() {
            String threadName = Thread.currentThread().getName();
            
            System.out.println(threadName + ": Tentativo di acquisire la risorsa B");
            synchronized (RISORSA_B) {
                System.out.println(threadName + ": Risorsa B acquisita");
                
                // Simuliamo un'operazione che richiede tempo
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
                System.out.println(threadName + ": Tentativo di acquisire la risorsa A");
                synchronized (RISORSA_A) {
                    System.out.println(threadName + ": Risorsa A acquisita");
                    
                    // Utilizziamo entrambe le risorse
                    System.out.println(threadName + ": Utilizzo di entrambe le risorse");
                }
                System.out.println(threadName + ": Rilasciata la risorsa A");
            }
            System.out.println(threadName + ": Rilasciata la risorsa B");
        }
        
        private void eseguiSicuro() {
            String threadName = Thread.currentThread().getName();
            
            // Acquisizione dei lock sempre nello stesso ordine (A poi B), come in TaskA
            System.out.println(threadName + ": Tentativo di acquisire la risorsa A");
            synchronized (RISORSA_A) {
                System.out.println(threadName + ": Risorsa A acquisita");
                
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
                System.out.println(threadName + ": Tentativo di acquisire la risorsa B");
                synchronized (RISORSA_B) {
                    System.out.println(threadName + ": Risorsa B acquisita");
                    
                    // Utilizziamo entrambe le risorse
                    System.out.println(threadName + ": Utilizzo di entrambe le risorse");
                }
                System.out.println(threadName + ": Rilasciata la risorsa B");
            }
            System.out.println(threadName + ": Rilasciata la risorsa A");
        }
    }
}