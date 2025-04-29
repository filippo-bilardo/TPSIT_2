/**
 * Esempio 01: Problemi di Concorrenza
 * 
 * Questo esempio dimostra i problemi classici di concorrenza che possono verificarsi
 * quando più thread accedono e modificano dati condivisi senza sincronizzazione.
 * In particolare, illustra una race condition su un contatore condiviso.
 */
public class ES01_ProblemiConcorrenza {
    // Contatore condiviso tra i thread
    private static int contatore = 0;
    
    public static void main(String[] args) {
        System.out.println("Dimostrazione dei problemi di concorrenza");
        System.out.println("=======================================");
        
        // Numero di thread e incrementi per thread
        final int NUM_THREADS = 5;
        final int INCREMENTI_PER_THREAD = 1000;
        
        // Array di thread
        Thread[] threads = new Thread[NUM_THREADS];
        
        // Creiamo e avviamo i thread
        for (int i = 0; i < NUM_THREADS; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < INCREMENTI_PER_THREAD; j++) {
                    // Operazione non atomica di incremento
                    contatore++;
                }
            });
            threads[i].start();
        }
        
        // Attendiamo che tutti i thread completino l'esecuzione
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        // Valore atteso: NUM_THREADS * INCREMENTI_PER_THREAD
        int valoreAtteso = NUM_THREADS * INCREMENTI_PER_THREAD;
        
        System.out.println("\nRisultati:");
        System.out.println("Valore atteso del contatore: " + valoreAtteso);
        System.out.println("Valore effettivo del contatore: " + contatore);
        
        if (contatore < valoreAtteso) {
            System.out.println("\nSi è verificata una race condition!");
            System.out.println("Alcuni incrementi sono andati persi a causa dell'accesso concorrente");
            System.out.println("non sincronizzato alla variabile condivisa.");
        }
        
        System.out.println("\nSpiegazione:");
        System.out.println("L'operazione contatore++ non è atomica, ma composta da tre passaggi:");
        System.out.println("1. Leggere il valore attuale del contatore");
        System.out.println("2. Incrementare il valore di 1");
        System.out.println("3. Scrivere il nuovo valore in memoria");
        System.out.println("\nQuando più thread eseguono questa operazione contemporaneamente,");
        System.out.println("possono verificarsi interferenze che portano alla perdita di incrementi.");
        System.out.println("Per risolvere questo problema, è necessario utilizzare meccanismi di");
        System.out.println("sincronizzazione come il keyword 'synchronized' o altre primitive di");
        System.out.println("concorrenza fornite da Java.");
    }
}