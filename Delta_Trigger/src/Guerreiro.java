// ============================================================
// ARQUIVO: Guerreiro.java (versao antiga)
// Personagem do tipo Guerreiro. Tem bastante vida e ataque,
// e possui a habilidade especial "Golpe Forte".
// ============================================================

public class Guerreiro extends Personagem {

    // Cria o guerreiro com seus atributos iniciais.
    // Ordem dos valores: nome, vida, mana, ataque, defesa, velocidade.
    public Guerreiro(String nome) {
        super(nome, 120, 30, 25, 15, 10);
    }

    // Habilidade especial: causa o dobro do ataque, gastando mana.
    public void golpeForte(Inimigo inimigo) {
        int custo = 5; // Mana necessaria para usar a habilidade.

        // Se nao tiver mana suficiente, avisa e cancela a acao.
        if (getMana() < custo) {
            System.out.println("Mana insuficiente!");
            return;
        }

        gastarMana(custo);

        // Dano = (ataque x 2) menos a defesa do inimigo (minimo 1).
        int dano = Math.max(1, (getAtaque() * 2) - inimigo.getDefesa());
        inimigo.receberDano(dano);

        System.out.println(getNome() + " usou Golpe Forte!");
        System.out.println("Dano causado: " + dano);
    }
}
