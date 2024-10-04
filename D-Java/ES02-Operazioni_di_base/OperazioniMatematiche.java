public class OperazioniMatematiche {

    public static void main(String[] args) {
        // Operatori matematici
        int a = 10;
        int b = 5;

        System.out.println("\nOperatori matematici:");
        System.out.println("Addizione 10+5: " + (a + b));
        System.out.println("Sottrazione 10-5: " + (a - b));
        System.out.println("Moltiplicazione 10*5: " + (a * b));
        System.out.println("Divisione 10/5: " + (a / b));
        System.out.println("Resto della divisione 12%5: " + (12 % b));

        // Funzioni della libreria Math
        double x = 3.0;
        double y = 4.0;

        System.out.println("\nFunzioni della libreria Math:");
        System.out.println("Potenza Math.pow(3.0,4.0): " + Math.pow(x, y));
        System.out.println("Radice quadrata Math.sqrt(3.0): " + Math.sqrt(x));
        System.out.println("Valore assoluto Math.abs(-3.0): " + Math.abs(-x));
        System.out.println("Parte superiore Math.ceil(3.2): " + Math.ceil(3.2));
        System.out.println("Parte inferiore Math.floor(3.2): " + Math.floor(3.2));
        System.out.println("Arrotondamento round(3.6): " + Math.round(3.6));
        System.out.println("Arrotondamento round(3.2): " + Math.round(3.2));

        // Funzioni trigonometriche
        double angoloInGradi = 45.0;
        double angoloInRadianti = Math.toRadians(angoloInGradi);

        System.out.println("\nFunzioni trigonometriche:");
        System.out.println("Seno(45): " + Math.sin(angoloInRadianti));
        System.out.println("Coseno(45): " + Math.cos(angoloInRadianti));
        System.out.println("Tangente(45): " + Math.tan(angoloInRadianti));
    }
}
