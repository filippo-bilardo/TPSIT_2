/**
 * Esempio 05: Dimostrazione di Deadlock
 * 
 * Questo esempio dimostra come si può verificare un deadlock quando due o più thread
 * tentano di acquisire risorse in ordine diverso. Inoltre, mostra come prevenire
 * il deadlock utilizzando un approccio di ordinamento delle risorse.
 */
public class ES05_DeadlockDemo {
    public static void main(String[] args) {
        System.out.println("Dimostrazione di Deadlock e sua prevenzione");
        System.out.println("==========================================\n");
        
        // Creiamo due risorse
        Risorsa risorsaA = new Risorsa("Risorsa A");
        Risorsa risorsaB = new Risorsa("Risorsa B");
        
        System.out.println("Parte 1: Dimostrazione di Deadlock");
        System.out.println("--------------------------------\n");
        
        // Creiamo due thread che acquisiscono le risorse in ordine opposto
        Thread thread1 = new Thread(new ThreadConDeadlock(risorsaA, risorsaB, "Thread-1"));
        Thread thread2 = new Thread(new ThreadConDeadlock(risorsaB, risorsaA, "Thread-2"));
        
        // Avviamo i thread
        thread1.start();
        thread2.start();
        
        // Attendiamo un po' per osservare il deadlock
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Interrompiamo i thread (in una situazione reale, potrebbe non funzionare)
        thread1.interrupt();
        thread2.interrupt();
        
        // Attendiamo che i thread terminino
        try {
            thread1.join(1000);
            thread2.join(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("\nI thread sono stati interrotti, ma potrebbero essere ancora bloccati in deadlock.\n");
        
        System.out.println("Parte 2: Prevenzione del Deadlock");
        System.out.println("--------------------------------\n");
        
        // Creiamo due thread che utilizzano un approccio sicuro
        Thread thread3 = new Thread(new ThreadSenzaDeadlock(risorsaA, risorsaB, "Thread-3"));
        Thread thread4 = new Thread(new ThreadSenzaDeadlock(risorsaB, risorsaA, "Thread-4"));
        
        // Avviamo i thread
        thread3.start();
        thread4.start();
        
        // Attendiamo che i thread completino l'esecuzione
        try {
            thread3.join();
            thread4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("\nTutti i thread hanno completato l'esecuzione senza deadlock!");
    }
}

/**
 * Classe che rappresenta una risorsa condivisa
 */
class Risorsa {
    private final String nome;
    
    public Risorsa(String nome) {
        this.nome = nome;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void usa() {
        System.out.println(Thread.currentThread().getName() + ": utilizzando " + nome);
    }
}

/**
 * Classe che dimostra come si può verificare un deadlock
 */
class ThreadConDeadlock implements Runnable {
    private final Risorsa prima;
    private final Risorsa seconda;
    private final String threadName;
    
    public ThreadConDeadlock(Risorsa prima, Risorsa seconda, String threadName) {
        this.prima = prima;
        this.seconda = seconda;
        this.threadName = threadName;
    }
    
    @Override
    public void run() {
        try {
            // Imposta il nome del thread
            Thread.currentThread().setName(threadName);
            
            System.out.println(threadName + ": tentativo di acquisire lock su " + prima.getNome());
            synchronized (prima) {
                System.out.println(threadName + ": acquisito lock su " + prima.getNome());
                
                // Simula un po' di lavoro
                Thread.sleep(100);
                
                System.out.println(threadName + ": tentativo di acquisire lock su " + seconda.getNome());
                synchronized (seconda) {
                    System.out.println(threadName + ": acquisito lock su " + seconda.getNome());
                    
                    // Usa entrambe le risorse
                    prima.usa();
                    seconda.usa();
                    
                    System.out.println(threadName + ": rilasciati i lock");
                }
            }
        } catch (InterruptedException e) {
            System.out.println(threadName + ": interrotto");
            Thread.currentThread().interrupt();
        }
    }
}

/**
 * Classe che dimostra come prevenire il deadlock utilizzando l'ordinamento delle risorse
 */
class ThreadSenzaDeadlock implements Runnable {
    private Risorsa risorsaA;
    private Risorsa risorsaB;
    private final String threadName;
    
    public ThreadSenzaDeadlock(Risorsa risorsaA, Risorsa risorsaB, String threadName) {
        this.risorsaA = risorsaA;
        this.risorsaB = risorsaB;
        this.threadName = threadName;
    }
    
    @Override
    public void run() {
        try {
            // Imposta il nome del thread
            Thread.currentThread().setName(threadName);
            
            // Determina l'ordine di acquisizione in base al nome delle risorse
            Risorsa prima, seconda;
            if (risorsaA.getNome().compareTo(risorsaB.getNome()) < 0) {
                prima = risorsaA;
                seconda = risorsaB;
            } else {
                prima = risorsaB;
                seconda = risorsaA;
            }
            
            System.out.println(threadName + ": tentativo di acquisire lock su " + prima.getNome());
            synchronized (prima) {
                System.out.println(threadName + ": acquisito lock su " + prima.getNome());
                
                // Simula un po' di lavoro
                Thread.sleep(100);
                
                System.out.println(threadName + ": tentativo di acquisire lock su " + seconda.getNome());
                synchronized (seconda) {
                    System.out.println(threadName + ": acquisito lock su " + seconda.getNome());
                    
                    // Usa entrambe le risorse
                    prima.usa();
                    seconda.usa();
                    
                    System.out.println(threadName + ": rilasciati i lock");
                }
            }
        } catch (InterruptedException e) {
            System.out.println(threadName + ": interrotto");
            Thread.currentThread().interrupt();
        }
    }
}