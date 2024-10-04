// Confronto prestazioni con calcolo dei tempi
public class stringhe3 {

    public static void main(String[] args) {
        int iterations = 100000;
        
        // Test con String
        long startTime = System.nanoTime();
        String s = "";
        for (int i = 0; i < iterations; i++) {
            s += "a";
        }
        long endTime = System.nanoTime();
        long stringTime = endTime - startTime;
        
        // Test con StringBuilder
        startTime = System.nanoTime();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < iterations; i++) {
            sb.append("a");
        }
        endTime = System.nanoTime();
        long stringBuilderTime = endTime - startTime;
        
        // Test con StringBuffer
        startTime = System.nanoTime();
        StringBuffer sbuf = new StringBuffer();
        for (int i = 0; i < iterations; i++) {
            sbuf.append("a");
        }
        endTime = System.nanoTime();
        long stringBufferTime = endTime - startTime;
        
        // Stampa risultati
        System.out.println("Test eseguiti con " + iterations + " iterazioni:");
        System.out.println("Tempo String: " + stringTime / 1_000_000 + " millisecondi");
        System.out.println("Tempo StringBuilder: " + stringBuilderTime / 1_000_000 + " millisecondi");
        System.out.println("Tempo StringBuffer: " + stringBufferTime / 1_000_000 + " millisecondi");
        
        // Calcolo e stampa dei rapporti di velocità
        double stringVsBuilder = (double) stringTime / stringBuilderTime;
        double stringVsBuffer = (double) stringTime / stringBufferTime;
        double bufferVsBuilder = (double) stringBufferTime / stringBuilderTime;
        
        System.out.printf("String è %.2f volte più lento di StringBuilder%n", stringVsBuilder);
        System.out.printf("String è %.2f volte più lento di StringBuffer%n", stringVsBuffer);
        System.out.printf("StringBuffer è %.2f volte più lento di StringBuilder%n", bufferVsBuilder);
    }
}

/* Output esempio (i tempi possono variare in base all'hardware):
Test eseguiti con 100000 iterazioni:
Tempo String: 2516 millisecondi
Tempo StringBuilder: 1 millisecondi
Tempo StringBuffer: 2 millisecondi
String è 2516.00 volte più lento di StringBuilder
String è 1258.00 volte più lento di StringBuffer
StringBuffer è 2.00 volte più lento di StringBuilder

ubuntu@fblabs:/home/git-projects/TPSIT_2/D-Java/ES04-Stringhe$ java stringhe3
Test eseguiti con 100000 iterazioni:
Tempo String: 717 millisecondi
Tempo StringBuilder: 5 millisecondi
Tempo StringBuffer: 5 millisecondi
String è 119.92 volte più lento di StringBuilder
String è 133.42 volte più lento di StringBuffer
StringBuffer è 0.90 volte più lento di StringBuilder

PS C:\Users\filippo.bilardo\Documents\Lavori> java stringhe3  
Test eseguiti con 100000 iterazioni:
Tempo String: 819 millisecondi
Tempo StringBuilder: 3 millisecondi
Tempo StringBuffer: 3 millisecondi
String è 228,45 volte più lento di StringBuilder
String è 230,04 volte più lento di StringBuffer
StringBuffer è 0,99 volte più lento di StringBuilder
PS C:\Users\filippo.bilardo\Documents\Lavori> 
*/