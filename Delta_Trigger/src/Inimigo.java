public class Inimigo {
    private String nome;
    private int vida;
    private int vidaMaxima;
    private int ataque;
    private int defesa;

    public Inimigo(String nome, int vida, int ataque, int defesa) {
        this.nome = nome;
        this.vida = vida;
        this.vidaMaxima = vida;
        this.ataque = ataque;
        this.defesa = defesa;
    }

    public void atacar(Personagem personagem) {
        int dano = Math.max(1, ataque - personagem.getDefesa());
        personagem.receberDano(dano);

        System.out.println(nome + " atacou " + personagem.getNome() + "!");
    }

    public void receberDano(int dano) {
        vida -= dano;

        if (vida < 0) {
            vida = 0;
        }
    }

    public boolean estaVivo() {
        return vida > 0;
    }

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
