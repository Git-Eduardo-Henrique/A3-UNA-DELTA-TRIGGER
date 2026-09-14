package model;
import java.util.Random;

public class Inimigo {
    private String nome;
    private int vida, vidaMaxima, ataque, defesa, velocidade, recompensa;
    private Random random = new Random();

    public Inimigo(String nome, int hp, int ataque, int defesa, int velocidade, int recompensa) {
        this.nome = nome; vida = hp; vidaMaxima = hp; this.ataque = ataque;
        this.defesa = defesa; this.velocidade = velocidade; this.recompensa = recompensa;
    }

    public int atacar() { return Math.max(1, ataque + random.nextInt(5) - 2); }
    public int receberDano(int dano) {
        int finalDano = Math.max(1, dano - defesa / 3);
        vida = Math.max(0, vida - finalDano);
        return finalDano;
    }
    public boolean vivo() { return vida > 0; }
    public String getNome() { return nome; }
    public int getVida() { return vida; }
    public int getVidaMaxima() { return vidaMaxima; }
    public int getDefesa() { return defesa; }
    public int getRecompensa() { return recompensa; }
}
