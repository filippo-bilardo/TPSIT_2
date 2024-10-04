public class PrintExample {

    public static void main(String[] args) {

        String nome = "Alice";
        int età = 25;
        double altezza = 1.75;

        // Uso di print e println
        System.out.print("Ciao");
        System.out.print(" Mondo!");
        System.out.println();
        System.out.println("Questa è una nuova linea");
        

        // Stampa una stringa
        System.out.printf("\nCiao, %s!\n", "Mondo");
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

        //Stampa di una tabella
        System.out.println("\nTabella:");
        System.out.printf("%-10s %-10s %-10s%n", "Nome", "Età", "Salario");
        System.out.printf("%-10s %-10d %-10.2f%n", "Mario", 30, 1500.50);
        System.out.printf("%-10s %-10d %-10.2f%n", "Luigi", 25, 1200.75);
        System.out.printf("%-10s %-10d %-10.2f%n", "Anna", 28, 1800.00);
        System.out.printf("%-10s %-10d %-10.2f%n", "Sara", 35, 2100.10);

        System.out.println("\nCiao Mondo!\n\n");
    }
}
