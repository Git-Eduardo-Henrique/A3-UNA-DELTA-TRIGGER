// ============================================================
// ARQUIVO: Suporte.java
// Personagem "Lyra", da classe Suporte. Tem menos ataque, mas
// muita mana e inteligencia. Suas habilidades sao curas:
// "Cura Individual" e "Cura em Grupo".
// ============================================================

package model;

public class Suporte extends Personagem {

    // Construtor: cria a Lyra com seus atributos iniciais.
    // Ordem dos atributos: forca, defesa, inteligencia,
    // resistencia, velocidade e sorte.
    public Suporte() {
        super("Lyra", 90, 60, new Atributos(2, 4, 7, 4, 3, 2));
    }

    // Mostra o menu de habilidades de Lyra e executa a escolhida.
    // Retorna true se uma habilidade foi realmente usada.
    public boolean habilidade(Grupo grupo, Inimigo[] inimigos) {
        while (true) {
            // Mostra as opcoes de cura.
            System.out.println("\n" + Cores.magia("=== HABILIDADES DE LYRA ==="));
            System.out.println("1 - Cura Individual (8 mana)");
            System.out.println("2 - Cura em Grupo (14 mana)");
            System.out.println("0 - Voltar");

            int op = Entrada.lerInt("Escolha: ");

            // Opcao 0: sai do menu sem usar habilidade.
            if (op == 0) return false;

            // So aceita as opcoes 1 e 2.
            if (op != 1 && op != 2) {
                System.out.println(Cores.aviso("Opcao invalida. Escolha 1, 2 ou 0."));
                continue;
            }

            // --- CURA INDIVIDUAL ---
            if (op == 1) {
                Personagem alvo = escolherAlvo(grupo);
                if (alvo == null) continue;

                // Nao cura quem ja esta com a vida cheia.
                if (alvo.getVida() >= alvo.getVidaMaxima()) {
                    System.out.println(Cores.aviso(alvo.getNome() + " ja esta com a vida cheia."));
                    continue;
                }

                // Verifica se ha mana suficiente.
                if (mana < 8) {
                    System.out.println(Cores.aviso("Mana insuficiente. Sao necessarios 8 pontos."));
                    continue;
                }

                // Usa a mana e cura o alvo.
                usarMana(8);
                int antes = alvo.getVida();
                alvo.curar(18 + getInteligencia() * 2);
                System.out.println(Cores.sucesso(alvo.getNome() + " recuperou " + (alvo.getVida() - antes) + " HP."));
                return true;
            }

            // --- CURA EM GRUPO ---
            // Verifica se algum personagem vivo esta ferido.
            boolean ferido = false;
            for (Personagem p : grupo.getPersonagens()) if (p.vivo() && p.getVida() < p.getVidaMaxima()) ferido = true;

            if (!ferido) {
                System.out.println(Cores.aviso("Todos os personagens vivos estao com a vida cheia."));
                continue;
            }

            if (mana < 14) {
                System.out.println(Cores.aviso("Mana insuficiente. Sao necessarios 14 pontos."));
                continue;
            }

            // Usa a mana e cura todos os aliados vivos.
            usarMana(14);
            int cura = 12 + getInteligencia();
            for (Personagem p : grupo.getPersonagens()) if (p.vivo()) p.curar(cura);
            System.out.println(Cores.sucesso("Cura em Grupo restaurou " + cura + " HP dos aliados vivos."));
            return true;
        }
    }

    // Mostra os aliados vivos e deixa o jogador escolher quem curar.
    private Personagem escolherAlvo(Grupo grupo) {
        System.out.println("\nEscolha o alvo da cura:");
        int n = 0;
        for (Personagem p : grupo.getPersonagens()) {
            if (p.vivo()) {
                n++;
                System.out.println(n + " - " + Cores.nomePersonagem(p.getNome()) + " | HP " + p.getVida() + "/" + p.getVidaMaxima());
            }
        }
        System.out.println("0 - Voltar");

        int op = Entrada.lerInt("Alvo: ");
        if (op == 0) return null;

        n = 0;
        for (Personagem p : grupo.getPersonagens()) {
            if (p.vivo()) {
                n++;
                if (n == op) return p;
            }
        }
        System.out.println(Cores.aviso("Alvo invalido."));
        return null;
    }
}
