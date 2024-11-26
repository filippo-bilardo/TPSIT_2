public class EsempiCharSequence {
    public static void main(String[] args) {
        // 1. Diverse implementazioni di CharSequence
        CharSequence cs1 = "Sono una String";              // String
        CharSequence cs2 = new StringBuilder("Builder");   // StringBuilder
        CharSequence cs3 = new StringBuffer("Buffer");     // StringBuffer
        
        // 2. Metodi comuni di CharSequence
        System.out.println("=== Metodi di base ===");
        System.out.println("Lunghezza: " + cs1.length());           // 14
        System.out.println("Carattere a indice 0: " + cs1.charAt(0)); // 'S'
        System.out.println("Sottosequenza: " + cs1.subSequence(0, 4)); // "Sono"
        System.out.println("ToString: " + cs1.toString());          // "Sono una String"
        
        // 3. Metodo che accetta CharSequence come parametro
        stampaInfoSequenza(cs1);
        stampaInfoSequenza(cs2);
        stampaInfoSequenza(cs3);
        
        // 4. Confronto tra implementazioni
        System.out.println("\n=== Confronto implementazioni ===");
        confrontaImplementazioni();
        
        // 5. Esempio pratico: conteggio vocali
        System.out.println("\n=== Conteggio vocali ===");
        System.out.println("Vocali in cs1: " + contaVocali(cs1));
        System.out.println("Vocali in cs2: " + contaVocali(cs2));
        System.out.println("Vocali in cs3: " + contaVocali(cs3));
    }
    
    // Metodo che accetta qualsiasi implementazione di CharSequence
    public static void stampaInfoSequenza(CharSequence cs) {
        System.out.println("\n=== Info Sequenza ===");
        System.out.println("Contenuto: " + cs);
        System.out.println("Tipo: " + cs.getClass().getSimpleName());
        System.out.println("Lunghezza: " + cs.length());
    }
    
    // Confronto tra diverse implementazioni
    public static void confrontaImplementazioni() {
        String str = "test";
        StringBuilder sb = new StringBuilder("test");
        StringBuffer sbuf = new StringBuffer("test");
        
        // Verifica equals e contentEquals
        System.out.println("str.equals(sb): " + str.equals(sb));                  // false
        System.out.println("str.contentEquals(sb): " + str.contentEquals(sb));    // true
        System.out.println("str.contentEquals(sbuf): " + str.contentEquals(sbuf));// true
    }
    
    // Esempio di utilizzo pratico di CharSequence
    public static int contaVocali(CharSequence cs) {
        int count = 0;
        String vocali = "aeiouAEIOU";
        for (int i = 0; i < cs.length(); i++) {
            if (vocali.indexOf(cs.charAt(i)) != -1) {
                count++;
            }
        }
        return count;
    }
}

// Esempio di classe personalizzata che implementa CharSequence
class SequenzaInvertita implements CharSequence {
    private String contenuto;
    
    public SequenzaInvertita(String contenuto) {
        this.contenuto = contenuto;
    }
    
    @Override
    public int length() {
        return contenuto.length();
    }
    
    @Override
    public char charAt(int index) {
        return contenuto.charAt(contenuto.length() - 1 - index);
    }
    
    @Override
    public CharSequence subSequence(int start, int end) {
        return new SequenzaInvertita(new StringBuilder(contenuto)
            .reverse()
            .substring(start, end));
    }
    
    @Override
    public String toString() {
        return new StringBuilder(contenuto).reverse().toString();
    }
    
    // Test della classe personalizzata
    public static void main(String[] args) {
        SequenzaInvertita seq = new SequenzaInvertita("Ciao Mondo");
        System.out.println("Sequenza invertita: " + seq);         // "odnoM oaiC"
        System.out.println("Carattere a indice 0: " + seq.charAt(0)); // 'o'
        System.out.println("Sottosequenza (0,4): " + seq.subSequence(0, 4)); // "odno"
    }
}