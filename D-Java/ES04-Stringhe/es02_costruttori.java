/**
 * es03_costruttori
 * Esempi di costruttori della classe String
 * 
 * @author Filippo Bilardo
 * @date 07/03/2024
 */
public class es02_costruttori {

    public static void main(String[] args) {
     
        // Costruttore di default (stringa vuota)
        String s1 = new String();
        System.out.println(s1);
     
        // Costruttore con array di caratteri 
        char[] array = {'c', 'i', 'a', 'o'};
        String s2 = new String(array);
        System.out.println(s2);
        // Costruttore con array di caratteri e offset
        String s3 = new String(array, 1, 2);
        System.out.println(s3);

        // Costruttore con array di byte
        byte[] array2 = {97, 98, 99, 100};
        String s4 = new String(array2);
        System.out.println(s4);
        // Costruttore con array di byte e offset
        String s5 = new String(array2, 1, 2);
        System.out.println(s5);
        // Costruttore con array di byte e charset
        String s6 = new String(array2, java.nio.charset.StandardCharsets.UTF_8);
        System.out.println(s6);
        // Costruttore con array di byte, offset e lunghezza
        String s7 = new String(array2, 1, 2, java.nio.charset.StandardCharsets.UTF_8);
        System.out.println(s7);
        
        // Costruttore con StringBuffer 
        // StringBuffer è una classe che rappresenta una stringa modificabile
        // quindi è possibile modificare il valore della stringa
        // ad esempio aggiungendo o rimuovendo caratteri
        StringBuffer sb = new StringBuffer("ciao");
        String s8 = new String(sb);
        System.out.println(s8);
        // aggiungiamo un carattere
        sb.append('!');
        System.out.println(s8);
        // rimuoviamo un carattere
        sb.deleteCharAt(sb.length() - 1);
        System.out.println(s8);

        // Costruttore con StringBuilder
        // StringBuilder è una classe che rappresenta una stringa modificabile
        // quindi è possibile modificare il valore della stringa
        // ad esempio aggiungendo o rimuovendo caratteri
        StringBuilder sb2 = new StringBuilder("ciao");
        String s9 = new String(sb2);
        System.out.println(s9);
        // aggiungiamo un carattere
        sb2.append('!');
        System.out.println(s9);
        // rimuoviamo un carattere
        sb2.deleteCharAt(sb2.length() - 1);
        System.out.println(s9);

        // https://docs.oracle.com/javase/8/docs/api/java/lang/String.html
        
    }

    
}
