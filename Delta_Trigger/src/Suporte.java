// ============================================================
// ARQUIVO: Suporte.java (versao antiga)
// Personagem do tipo Suporte. Tem menos ataque, mas muita mana
// e a habilidade especial "Cura", que recupera vida de um aliado.
// ============================================================

public class Suporte extends Personagem {

    // Cria o suporte com seus atributos iniciais.
    // Ordem dos valores: nome, vida, mana, ataque, defesa, velocidade.
    public Suporte(String nome) {
        super(nome, 90, 60, 12, 10, 12);
    }

    // Habilidade especial: cura um aliado gastando mana.
    public void curar(Personagem aliado) {
        int custo = 8; // Mana necessaria para usar a cura.

        // Se nao tiver mana suficiente, avisa e cancela a acao.
        if (getMana() < custo) {
            System.out.println("Mana insuficiente!");
            return;
        }

        gastarMana(custo);
        aliado.recuperarVida(25); // Recupera 25 pontos de vida.

        System.out.println(getNome() + " usou Cura!");
        System.out.println(aliado.getNome() + " recuperou vida.");
    }
}
