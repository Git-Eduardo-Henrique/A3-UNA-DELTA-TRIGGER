// ============================================================
// ARQUIVO: Elyra.java
// Personagem "Elyra", da classe Arqueira Arcana. Entra no grupo
// mais tarde, na taverna de Eldoria. Ja comeca no nivel 2 e com
// um arco e uma armadura equipados. Usa flechas magicas.
// ============================================================

package model;

public class Elyra extends Personagem {

    // Construtor: cria a Elyra, sobe para o nivel 2 e equipa
    // o equipamento inicial dela.
    public Elyra() {
        super("Elyra", 105, 55, new Atributos(4, 3, 5, 3, 6, 4));
        definirNivel(2);

        // Cria o arco e a armadura iniciais (so ela pode usar).
        Equipamento arco = new Equipamento("Arco de Madeira Reforcado", "Arma", "Comum", "Arqueira Arcana", 40, 1, 0, 0, 0, 0, 0);
        Equipamento armadura = new Equipamento("Traje de Cacadora", "Armadura", "Comum", "Arqueira Arcana", 45, 0, 0, 0, 0, 1, 0);

        equiparDireto(arco);
        equiparDireto(armadura);
    }

    // Mostra o menu de habilidades de Elyra e executa a escolhida.
    // Retorna true se uma habilidade foi realmente usada.
    public boolean habilidade(Grupo grupo, Inimigo[] inimigos) {
        while (true) {
            // Mostra as opcoes de flechas magicas.
            System.out.println("\n" + Cores.magia("=== HABILIDADES DE ELYRA ==="));
            System.out.println("1 - Flecha Arcana (6 mana)");
            System.out.println("2 - Flecha de Gelo (8 mana)");
            System.out.println("0 - Voltar");

            int op = Entrada.lerInt("Escolha: ");

            // Opcao 0: sai do menu sem usar habilidade.
            if (op == 0) return false;

            // So aceita as opcoes 1 e 2.
            if (op != 1 && op != 2) {
                System.out.println(Cores.aviso("Opcao invalida. Escolha 1, 2 ou 0."));
                continue;
            }

            // Define o custo de mana de cada flecha.
            int custo = op == 1 ? 6 : 8;

            // Se nao tiver mana suficiente, avisa e volta ao menu.
            if (mana < custo) {
                System.out.println(Cores.aviso("Mana insuficiente. Sao necessarios " + custo + " pontos."));
                continue;
            }

            // Escolhe o inimigo alvo.
            Inimigo alvo = Batalha.escolherInimigo(inimigos);
            if (alvo == null) continue;

            // Cobra a mana e calcula o dano conforme a flecha usada.
            usarMana(custo);
            int dano = op == 1
                    ? getForca() * 2 + getInteligencia() * 2 + random.nextInt(5) - 2
                    : getInteligencia() * 3 + random.nextInt(4) - 2;

            // Calcula se acertou e aplica o dano.
            Batalha.AtaqueResultado r = Batalha.calcularAtaque(this, alvo, dano);
            if (r.acertou) {
                System.out.println(Cores.magia((op == 1 ? "Flecha Arcana" : "Flecha de Gelo") + " causou " + r.dano + " de dano!"));
            } else {
                System.out.println(Cores.aviso("Elyra errou o ataque!"));
            }
            return true;
        }
    }
}
