// Esempio di Livelock
// I thread rispondono continuamente alle azioni degli altri senza fare progressi
public class ES09_Livelock {
    static class Cucchiaio {
        private volatile boolean disponibile = true;
        public boolean isDisponibile() { return disponibile; }
        public void setDisponibile(boolean disponibile) { this.disponibile = disponibile; }
    }
    static class Filosofo implements Runnable {
        private String nome;
        private Cucchiaio cucchiaio;
        private Filosofo vicino;
        public Filosofo(String nome, Cucchiaio cucchiaio) {
            this.nome = nome;
            this.cucchiaio = cucchiaio;
        }
        public void setVicino(Filosofo vicino) {
            this.vicino = vicino;
        }
        @Override
        public void run() {
            while (true) {
                if (cucchiaio.isDisponibile()) {
                    if (!vicino.cucchiaio.isDisponibile()) {
                        System.out.println(nome + ": cedo il cucchiaio al vicino");
                        continue;
                    }
                    cucchiaio.setDisponibile(false);
                    System.out.println(nome + ": mangia!");
                    try { Thread.sleep(100); } catch (InterruptedException e) {}
                    cucchiaio.setDisponibile(true);
                    break;
                }
            }
        }
    }
    public static void main(String[] args) {
        Cucchiaio cucchiaio = new Cucchiaio();
        Filosofo f1 = new Filosofo("Filosofo 1", cucchiaio);
        Filosofo f2 = new Filosofo("Filosofo 2", cucchiaio);
        f1.setVicino(f2);
        f2.setVicino(f1);
        new Thread(f1).start();
        new Thread(f2).start();
    }
}