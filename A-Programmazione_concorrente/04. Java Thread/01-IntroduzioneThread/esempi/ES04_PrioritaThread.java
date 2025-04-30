/**
 * Esempio 04: Priorità dei Thread
 * 
 * Questo esempio dimostra come impostare e utilizzare le priorità dei thread in Java.
 * Le priorità dei thread influenzano l'ordine di esecuzione quando il sistema operativo
 * deve decidere quale thread eseguire, ma non garantiscono un ordine preciso.
 * 
 * In Java, le priorità vanno da MIN_PRIORITY (1) a MAX_PRIORITY (10), con NORM_PRIORITY (5) come valore predefinito.
 */
public class ES04_PrioritaThread {
    public static void main(String[] args) {
        System.out.println("Dimostrazione delle priorità dei thread");
        System.out.println("======================================\n");
        
        System.out.println("Priorità disponibili in Java:");
        System.out.println("- Thread.MIN_PRIORITY: " + Thread.MIN_PRIORITY);
        System.out.println("- Thread.NORM_PRIORITY: " + Thread.NORM_PRIORITY);
        System.out.println("- Thread.MAX_PRIORITY: " + Thread.MAX_PRIORITY);
        System.out.println();
        
        // Creiamo tre thread con priorità diverse
        Thread threadBassa = new Thread(new ContaTask("Thread a priorità BASSA"), "Bassa");
        Thread threadNormale = new Thread(new ContaTask("Thread a priorità NORMALE"), "Normale");
        Thread threadAlta = new Thread(new ContaTask("Thread a priorità ALTA"), "Alta");
        
        // Impostiamo le priorità
        threadBassa.setPriority(Thread.MIN_PRIORITY); // 1
        threadNormale.setPriority(Thread.NORM_PRIORITY); // 5 (default)
        threadAlta.setPriority(Thread.MAX_PRIORITY); // 10
        
        System.out.println("Avvio dei thread con diverse priorità:");
        System.out.println("- " + threadBassa.getName() + ": priorità = " + threadBassa.getPriority());
        System.out.println("- " + threadNormale.getName() + ": priorità = " + threadNormale.getPriority());
        System.out.println("- " + threadAlta.getName() + ": priorità = " + threadAlta.getPriority());
        System.out.println();
        
        // Avviamo i thread
        threadBassa.start();
        threadNormale.start();
        threadAlta.start();
        
        // Attendiamo il completamento di tutti i thread
        try {
            threadBassa.join();
            threadNormale.join();
            threadAlta.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("\nNota: Le priorità dei thread sono solo suggerimenti per lo scheduler del sistema operativo.");
        System.out.println("Non garantiscono un ordine preciso di esecuzione, ma influenzano le decisioni dello scheduler.");
        System.out.println("L'effetto delle priorità può variare tra diversi sistemi operativi e JVM.");
    }
    
    // Classe interna per il task di conteggio
    static class ContaTask implements Runnable {
        private String nome;
        
        public ContaTask(String nome) {
            this.nome = nome;
        }
        
        @Override
        public void run() {
            System.out.println(nome + ": iniziato");
            
            // Eseguiamo un'operazione di conteggio
            long count = 0;
            for (long i = 0; i < 1_000_000_000; i++) {
                count++;
                
                // Ogni 100 milioni, stampiamo un aggiornamento
                if (i % 100_000_000 == 0) {
                    System.out.println(nome + ": conteggio a " + i);
                }
            }
            
            System.out.println(nome + ": completato");
        }
    }
}