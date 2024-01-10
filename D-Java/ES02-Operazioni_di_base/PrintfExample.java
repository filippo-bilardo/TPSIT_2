public class PrintfExample {

    public static void main(String[] args) {

        String nome = "Alice";
        int età = 25;
        double altezza = 1.75;

        // Stampa una stringa
        System.out.printf("Ciao, %s!\n", "Mondo");
        // Utilizzo di printf per formattare l'output
        System.out.printf("Nome: %s, Età: %d, Altezza: %.2f\n", nome, età, altezza);
        // Concatenazione di valori
        System.out.printf("Nome: "+nome+", Età: "+età+", Altezza: "+altezza+"\n");
        // Altri esempi
        System.out.printf("Il numero %d in formato esadecimale è: %x\n", 255, 255);
        System.out.printf("Il numero %f con due decimali: %.2f\n", 3.14159, 3.14159);
        System.out.printf("Il numero %s è convertito in maiuscolo: %S\n", "hello", "hello");
        // Utilizzo di argomenti posizionali
        System.out.printf("%2$d, %1$d\n", 10, 20);

        System.out.println("\nCiao Mondo!\n\n");
    }
}
