// ============================================================
// ARQUIVO: Inimigo.java (versao antiga)
// Representa um monstro que o jogador enfrenta nas batalhas.
// Guarda nome, vida, ataque e defesa, e sabe atacar e receber dano.
// ============================================================

public class Inimigo {
    // Dados basicos do inimigo.
    private String nome;      // Nome do monstro.
    private int vida;         // Vida atual.
    private int vidaMaxima;   // Vida maxima possivel.
    private int ataque;       // Poder de ataque.
    private int defesa;       // Capacidade de reduzir dano.

    // Construtor: cria o inimigo com os valores recebidos.
    public Inimigo(String nome, int vida, int ataque, int defesa) {
        this.nome = nome;
        this.vida = vida;
        this.vidaMaxima = vida;
        this.ataque = ataque;
        this.defesa = defesa;
    }

    // Ataca um personagem. O dano e o ataque menos a defesa do alvo,
    // mas sempre causa pelo menos 1 de dano.
    public void atacar(Personagem personagem) {
        int dano = Math.max(1, ataque - personagem.getDefesa());
        personagem.receberDano(dano);

        System.out.println(nome + " atacou " + personagem.getNome() + "!");
    }

    // Retira vida do inimigo quando ele leva dano.
    public void receberDano(int dano) {
        vida -= dano;

        // A vida nunca fica negativa.
        if (vida < 0) {
            vida = 0;
        }
    }

    // Diz se o inimigo ainda esta vivo.
    public boolean estaVivo() {
        return vida > 0;
    }

    // Metodos que apenas devolvem (get) os valores dos atributos.

    public String getNome() {
        return nome;
    }

    public int getVida() {
        return vida;
    }

    public int getVidaMaxima() {
        return vidaMaxima;
    }

    public int getAtaque() {
        return ataque;
    }

    public int getDefesa() {
        return defesa;
    }
}
