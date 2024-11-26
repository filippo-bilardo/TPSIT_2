public class Wrapper {

    public static void main(String[] args) {
        // Utilizzo della classe wrapper Integer
        Integer numeroIntero = 42; //oppure //new Integer(42);
        System.out.println("Numero intero: " + numeroIntero);

        // Conversione da wrapper a primitivo
        int primitivoIntero = numeroIntero.intValue();
        System.out.println("Primitivo intero: " + primitivoIntero);
        int primitivoIntero2 = numeroIntero;
        System.out.println("Primitivo intero: " + primitivoIntero2);

        // Conversione da stringa a wrapper
        String stringaIntero = "123";
        Integer daStringa = Integer.parseInt(stringaIntero);
        System.out.println("Da stringa a wrapper: " + daStringa);

        // Utilizzo della classe wrapper Double
        Double numeroDouble = 3.14;
        System.out.println("Numero double: " + numeroDouble);

        // Conversione da wrapper a primitivo
        double primitivoDouble = numeroDouble.doubleValue();
        System.out.println("Primitivo double: " + primitivoDouble);

        // Conversione da stringa a wrapper
        String stringaDouble = "2.718";
        Double daStringaDouble = Double.parseDouble(stringaDouble);
        System.out.println("Da stringa a wrapper double: " + daStringaDouble);

        // Alcuni metodi utili delle classi wrapper
        System.out.println("Massimo tra 5 e 10: " + Integer.max(5, 10));    // 10
        System.out.println("Minimo tra 5 e 10: " + Integer.min(5, 10));     // 5
        System.out.println("Integer.MAX_VALUE: "+ Integer.MAX_VALUE);       // 2147483647
        System.out.println("Integer.bitCount(17): "+ Integer.bitCount(17)); // 10001 -> 2
        System.out.println("Valore dell'Integer: " + numeroIntero.intValue());      // 42
        System.out.println("Valore assoluto: " + Math.abs(numeroIntero));           // 42
        System.out.println("Stringa rappresentante: " + numeroIntero.toString());   // "42"
        System.out.println("Hai inserito il numero 42? " + numeroIntero.equals(42));// true
        System.out.println("Restituisce l'hash code: " + numeroIntero.hashCode());  // 42
        System.out.println("Restituisce il valore come double: " + numeroIntero.doubleValue()); // 42.0
        System.out.println("Restituisce il valore come float: " + numeroIntero.floatValue());   // 42.0
        System.out.println("Restituisce il valore come long: " + numeroIntero.longValue());     // 42
        System.out.println("Valore assoluto di -7: " + Math.abs(-7));       // -7 -> 7
        System.out.println("Parte intera di 3.99: " + Math.floor(3.99));    // 3.99 -> 3
        System.out.println("Parte intera di 3.01: " + Math.floor(3.01));    // 3.01 -> 3
        System.out.println("Arrotondamento di 3.99: " + Math.round(3.99));  // 3.99 -> 4
        System.out.println("Arrotondamento di 3.01: " + Math.round(3.01));  // 3.01 -> 3
        System.out.println("Arrotondamento di 3.5: " + Math.round(3.5));    // 3.5 -> 4
        System.out.println("Arrotondamento di 3.51: " + Math.round(3.51));  // 3.51 -> 4
        System.out.println("Arrotondamento di 3.49: " + Math.round(3.49));  // 3.49 -> 3
    }
}
