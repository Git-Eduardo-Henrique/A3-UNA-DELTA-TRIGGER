// ============================================================
// ARQUIVO: Personagem.java (versao antiga)
// Classe base (modelo) de qualquer personagem do jogo.
// Guarda os dados principais, como vida, mana, ataque e defesa,
// e possui as acoes basicas: atacar, defender, receber dano etc.
// ============================================================

public abstract class Personagem {
    // Dados basicos de cada personagem.
    private String nome;        // Nome exibido na tela.
    private int vida;           // Vida atual (HP).
    private int vidaMaxima;     // Vida maxima possivel.
    private int mana;           // Mana atual (usada em habilidades).
    private int manaMaxima;     // Mana maxima possivel.
    private int ataque;         // Poder de ataque.
    private int defesa;         // Capacidade de reduzir dano.
    private int velocidade;     // Velocidade (usada em combate).

    // Construtor: chamado quando um personagem e criado.
    public Personagem(String nome, int vida, int mana, int ataque,
                      int defesa, int velocidade) {
        this.nome = nome;
        this.vida = vida;
        this.vidaMaxima = vida;   // Comeca com a vida cheia.
        this.mana = mana;
        this.manaMaxima = mana;   // Comeca com a mana cheia.
        this.ataque = ataque;
        this.defesa = defesa;
        this.velocidade = velocidade;
    }

    // Ataca um inimigo. O dano e o ataque menos a defesa do alvo.
    public void atacar(Inimigo inimigo) {
        int dano = Math.max(1, ataque - inimigo.getDefesa());
        inimigo.receberDano(dano);
        System.out.println(nome + " causou " + dano + " de dano!");
    }

    // Coloca o personagem em posicao de defesa (apenas aviso na tela).
    public void defender() {
        System.out.println(nome + " assumiu uma posição defensiva.");
    }

    // Retira vida do personagem quando ele leva dano.
    public void receberDano(int dano) {
        vida -= dano;

        // A vida nunca fica negativa; no minimo fica zero.
        if (vida < 0) {
            vida = 0;
        }

        System.out.println(nome + " recebeu " + dano + " de dano.");
    }

    // Diz se o personagem ainda tem vida (esta vivo).
    public boolean estaVivo() {
        return vida > 0;
    }

    // Mostra na tela todos os dados do personagem.
    public void mostrarStatus() {
        System.out.println("\n--- STATUS ---");
        System.out.println("Nome: " + nome);
        System.out.println("Vida: " + vida + "/" + vidaMaxima);
        System.out.println("Mana: " + mana + "/" + manaMaxima);
        System.out.println("Ataque: " + ataque);
        System.out.println("Defesa: " + defesa);
        System.out.println("Velocidade: " + velocidade);
    }

    // Metodos que apenas devolvem (get) os valores dos atributos.
    public String getNome() {
        return nome;
    }

    public int getVida() {
        return vida;
    }

    public int getMana() {
        return mana;
    }

    public int getAtaque() {
        return ataque;
    }

    public int getDefesa() {
        return defesa;
    }

    public int getVelocidade() {
        return velocidade;
    }

    // Gasta mana ao usar uma habilidade. Nunca fica abaixo de zero.
    public void gastarMana(int quantidade) {
        mana -= quantidade;

        if (mana < 0) {
            mana = 0;
        }
    }

    // Devolve vida (cura). Nunca passa da vida maxima.
    public void recuperarVida(int quantidade) {
        vida += quantidade;

        if (vida > vidaMaxima) {
            vida = vidaMaxima;
        }
    }

    // Devolve mana. Nunca passa da mana maxima.
    public void recuperarMana(int quantidade) {
        mana += quantidade;

        if (mana > manaMaxima) {
            mana = manaMaxima;
        }
    }
}
