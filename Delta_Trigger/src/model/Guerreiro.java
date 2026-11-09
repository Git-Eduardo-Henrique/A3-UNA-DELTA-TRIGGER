// ============================================================
// ARQUIVO: Guerreiro.java
// Personagem "Kael", da classe Guerreiro. Tem bastante vida e forca.
// Suas habilidades especiais sao "Ataque Bruto" e
// "Investida de Armadura".
// ============================================================

package model;

public class Guerreiro extends Personagem {

    // Construtor: cria o Kael com seus atributos iniciais.
    // Ordem dos atributos: forca, defesa, inteligencia,
    // resistencia, velocidade e sorte.
    public Guerreiro() {
        super("Kael", 120, 30, new Atributos(5, 5, 2, 4, 3, 3));
    }

    // Mostra o menu de habilidades de Kael e executa a escolhida.
    // Retorna true se uma habilidade foi realmente usada.
    public boolean habilidade(Grupo grupo, Inimigo[] inimigos) {
        while (true) {
            // Mostra as opcoes de habilidade.
            System.out.println("\n" + Cores.magia("=== HABILIDADES DE KAEL ==="));
            System.out.println("1 - Ataque Bruto (5 mana)");
            System.out.println("2 - Investida de Armadura (4 mana)");
            System.out.println("0 - Voltar");

            int op = Entrada.lerInt("Escolha: ");

            // Opcao 0: sai do menu sem usar habilidade.
            if (op == 0) return false;

            // So aceita as opcoes 1 e 2.
            if (op != 1 && op != 2) {
                System.out.println(Cores.aviso("Opcao invalida. Escolha 1, 2 ou 0."));
                continue;
            }

            // Define o custo de mana de cada habilidade.
            int custo = op == 1 ? 5 : 4;

            // Se nao tiver mana suficiente, avisa e volta ao menu.
            if (mana < custo) {
                System.out.println(Cores.aviso("Mana insuficiente. Sao necessarios " + custo + " pontos."));
                continue;
            }

            // Escolhe qual inimigo sera atacado.
            Inimigo alvo = Batalha.escolherInimigo(inimigos);
            if (alvo == null) continue;

            // Cobra a mana e calcula o dano conforme a habilidade.
            usarMana(custo);
            int dano = op == 1
                    ? getForca() * 3 + random.nextInt(5) - 2
                    : getForca() * 2 + getDefesa() / 2 + random.nextInt(4) - 2;

            // Calcula se o ataque acertou e aplica o dano.
            Batalha.AtaqueResultado r = Batalha.calcularAtaque(this, alvo, dano);
            if (r.acertou) {
                System.out.println(Cores.magia((op == 1 ? "Ataque Bruto" : "Investida de Armadura") + " causou " + r.dano + " de dano!"));
                // A Investida tambem deixa Kael defendido.
                if (op == 2) defendendo = true;
            } else {
                System.out.println(Cores.aviso(Cores.kael("Kael") + " errou o ataque!"));
            }
            return true;
        }
    }
}
