public abstract class Personagem {
    private String nome;
    private int vida;
    private int vidaMaxima;
    private int mana;
    private int manaMaxima;
    private int ataque;
    private int defesa;
    private int velocidade;

    public Personagem(String nome, int vida, int mana, int ataque,
                      int defesa, int velocidade) {
        this.nome = nome;
        this.vida = vida;
        this.vidaMaxima = vida;
        this.mana = mana;
        this.manaMaxima = mana;
        this.ataque = ataque;
        this.defesa = defesa;
        this.velocidade = velocidade;
    }

    public void atacar(Inimigo inimigo) {
        int dano = Math.max(1, ataque - inimigo.getDefesa());
        inimigo.receberDano(dano);
        System.out.println(nome + " causou " + dano + " de dano!");
    }

    public void defender() {
        System.out.println(nome + " assumiu uma posição defensiva.");
    }

    public void receberDano(int dano) {
        vida -= dano;

        if (vida < 0) {
            vida = 0;
        }

        System.out.println(nome + " recebeu " + dano + " de dano.");
    }

    public boolean estaVivo() {
        return vida > 0;
    }

    public void mostrarStatus() {
        System.out.println("\n--- STATUS ---");
        System.out.println("Nome: " + nome);
        System.out.println("Vida: " + vida + "/" + vidaMaxima);
        System.out.println("Mana: " + mana + "/" + manaMaxima);
        System.out.println("Ataque: " + ataque);
        System.out.println("Defesa: " + defesa);
        System.out.println("Velocidade: " + velocidade);
    }

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

    public void gastarMana(int quantidade) {
        mana -= quantidade;

        if (mana < 0) {
            mana = 0;
        }
    }

    public void recuperarVida(int quantidade) {
        vida += quantidade;

        if (vida > vidaMaxima) {
            vida = vidaMaxima;
        }
    }

    public void recuperarMana(int quantidade) {
        mana += quantidade;

        if (mana > manaMaxima) {
            mana = manaMaxima;
        }
    }
}
