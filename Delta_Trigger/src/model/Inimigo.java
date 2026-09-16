package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Inimigo {
    private final String nome;
    private int vida;
    private final int vidaMaxima;
    private final int ataque;
    private final int defesa;
    private final int velocidade;
    private final int recompensa;
    private final Random random = new Random();

    public Inimigo(String nome, int hp, int ataque, int defesa, int velocidade, int recompensa) {
        this.nome = nome;
        this.vidaMaxima = hp;
        this.vida = hp;
        this.ataque = ataque;
        this.defesa = defesa;
        this.velocidade = velocidade;
        this.recompensa = recompensa;
    }

    public int atacar() { return Math.max(1, ataque + random.nextInt(5) - 2); }

    public int receberDano(int dano) {
        int danoFinal = Math.max(1, dano - defesa / 3);
        vida = Math.max(0, vida - danoFinal);
        return danoFinal;
    }

    public boolean vivo() { return vida > 0; }
    public String getNome() { return nome; }
    public int getVida() { return vida; }
    public int getVidaMaxima() { return vidaMaxima; }
    public int getDefesa() { return defesa; }
    public int getVelocidade() { return velocidade; }
    public int getRecompensa() { return recompensa; }

    public List<Item> sortearDrops() {
        List<Item> drops = new ArrayList<Item>();
        if (nome.equals("Lobinho")) {
            if (random.nextInt(100) < 85) drops.add(random.nextBoolean()
                    ? new Item("Pocao de Vida", "Pocao", 15, 30, 0)
                    : new Item("Pocao de Mana", "Pocao", 18, 0, 20));
        } else if (nome.equals("Lobo")) {
            if (random.nextInt(100) < 95) drops.add(new Item("Pocao de Vida", "Pocao", 15, 30, 0));
            if (random.nextInt(100) < 65) drops.add(new Item("Pocao de Mana", "Pocao", 18, 0, 20));
        }
        return drops;
    }

    public Item sortearMaterial() {
        if (nome.equals("Lobinho") || nome.equals("Lobo")) {
            return new Item("Chifre de Lobo", "Material", 8, 0, 0);
        }
        return null;
    }
}
