public class OperazioniMatematiche {

    public static void main(String[] args) {
        // Operatori matematici
        int a = 10;
        int b = 5;

        System.out.println("Addizione: " + (a + b));
        System.out.println("Sottrazione: " + (a - b));
        System.out.println("Moltiplicazione: " + (a * b));
        System.out.println("Divisione: " + (a / b));
        System.out.println("Resto della divisione: " + (a % b));

        // Funzioni della libreria Math
        double x = 3.0;
        double y = 4.0;

        System.out.println("Potenza: " + Math.pow(x, y));
        System.out.println("Radice quadrata: " + Math.sqrt(x));
        System.out.println("Valore assoluto: " + Math.abs(-x));
        System.out.println("Parte superiore (ceil): " + Math.ceil(x));
        System.out.println("Parte inferiore (floor): " + Math.floor(x));
        System.out.println("Arrotondamento: " + Math.round(x));

        // Funzioni trigonometriche
        double angoloInGradi = 45.0;
        double angoloInRadianti = Math.toRadians(angoloInGradi);

        System.out.println("Seno: " + Math.sin(angoloInRadianti));
        System.out.println("Coseno: " + Math.cos(angoloInRadianti));
        System.out.println("Tangente: " + Math.tan(angoloInRadianti));
    }
}
