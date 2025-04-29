/**
 * Esempio 01: Scambio di Dati tra Thread
 * 
 * Questo esempio dimostra come i thread possono scambiarsi dati in modo sicuro
 * utilizzando un oggetto condiviso e la sincronizzazione.
 */
public class ScambioDati {
    public static void main(String[] args) {
        System.out.println("Dimostrazione dello scambio di dati tra thread");
        System.out.println("===========================================");
        
        // Creiamo un oggetto condiviso per lo scambio di messaggi
        MessaggioCondiviso messaggio = new MessaggioCondiviso();
        
        // Thread produttore che invia messaggi
        Thread produttore = new Thread(() -> {
            String[] messaggiDaInviare = {
                "Primo messaggio dal produttore",
                "Secondo messaggio dal produttore",
                "Terzo messaggio dal produttore",
                "Quarto messaggio dal produttore",
                "FINE" // Messaggio di terminazione
            };
            
            for (String msg : messaggiDaInviare) {
                messaggio.imposta(msg);
                
                // Breve pausa tra i messaggi
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        
        // Thread consumatore che riceve messaggi
        Thread consumatore = new Thread(() -> {
            String messaggioRicevuto;
            do {
                messaggioRicevuto = messaggio.ottieni();
                
                // Breve pausa tra le letture
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (!messaggioRicevuto.equals("FINE"));
            
            System.out.println("\nConsumatore: Ho ricevuto il messaggio di terminazione. Esco.");
        });
        
        // Avviamo i thread
        System.out.println("Avvio dello scambio di messaggi...");
        produttore.start();
        consumatore.start();
        
        // Attendiamo che entrambi i thread completino
        try {
            produttore.join();
            consumatore.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("\nScambio di messaggi completato.");
    }
    
    /**
     * Classe che rappresenta un messaggio condiviso tra thread.
     * Utilizza la sincronizzazione per garantire uno scambio sicuro dei dati.
     */
    static class MessaggioCondiviso {
        private String contenuto;
        private boolean disponibile = false;
        
        /**
         * Imposta un nuovo messaggio (chiamato dal produttore).
         * Se c'è già un messaggio non letto, attende che venga consumato.
         */
        public synchronized void imposta(String messaggio) {
            // Attende che il messaggio precedente sia stato letto
            while (disponibile) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            
            // Imposta il nuovo messaggio
            contenuto = messaggio;
            disponibile = true;
            System.out.println("Produttore: Ho inviato il messaggio -> " + messaggio);
            
            // Notifica il consumatore che un nuovo messaggio è disponibile
            notify();
        }
        
        /**
         * Ottiene il messaggio corrente (chiamato dal consumatore).
         * Se non ci sono messaggi disponibili, attende che ne arrivi uno.
         */
        public synchronized String ottieni() {
            // Attende che un messaggio sia disponibile
            while (!disponibile) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            
            // Ottiene il messaggio
            disponibile = false;
            System.out.println("Consumatore: Ho ricevuto il messaggio -> " + contenuto);
            
            // Notifica il produttore che il messaggio è stato letto
            notify();
            
            return contenuto;
        }
    }
}