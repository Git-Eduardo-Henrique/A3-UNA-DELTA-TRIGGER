public class Main {
    public static void main(String[] args) {
        Menu menu = new Menu();

        System.out.println("=================================");
        System.out.println("        DELTA TRIGGER");
        System.out.println("=================================");
        System.out.println("Mundo: Aethoria\n");

        Personagem jogador = menu.escolherPersonagem();

        System.out.println("\n" + jogador.getNome() + " iniciou sua jornada.");
        System.out.println("Uma floresta aparece no caminho...\n");

        System.out.println("=== FLORESTA DOS LOBOS ===");
        System.out.println("Você encontrou um lobo!");

        Inimigo lobo = new Inimigo("Lobo", 60, 12, 5);

        Batalha batalha = new Batalha();
        batalha.iniciar(jogador, lobo);

        if (jogador.estaVivo()) {
            System.out.println("\nVocê chegou à cidade de Eldoria.");
            System.out.println("Aqui termina o tutorial inicial.");
        } else {
            System.out.println("\nFim de jogo.");
        }
    }
}
