// ============================================================
// ARQUIVO: Inimigo.java
// Representa um monstro do jogo. Tem vida, ataque, defesa,
// velocidade, sorte e uma recompensa em ouro. Tambem sorteia
// os itens e materiais que deixa cair quando e derrotado.
// ============================================================

package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Inimigo {
    // Dados do inimigo. "final" = nao mudam depois de criado.
    private final String nome;
    private int vida; // So a vida atual muda durante a batalha.
    private final int vidaMaxima, ataque, defesa, velocidade, sorte, recompensa;
    // Objeto usado para sortear numeros aleatorios.
    private final Random random = new Random();

    // Construtor: cria o inimigo com os valores recebidos.
    public Inimigo(String nome, int hp, int ataque, int defesa, int velocidade, int sorte, int recompensa) {
        this.nome = nome;
        vidaMaxima = hp;
        vida = hp;
        this.ataque = ataque;
        this.defesa = defesa;
        this.velocidade = velocidade;
        this.sorte = sorte;
        this.recompensa = recompensa;
    }

    // Calcula o dano do ataque com uma pequena variacao aleatoria.
    public int atacar() { return Math.max(1, ataque + random.nextInt(5) - 2); }

    // Recebe dano: a defesa reduz parte do dano (1/3 da defesa).
    // Devolve o dano que realmente foi causado.
    public int receberDano(int dano) {
        int finalDano = Math.max(1, dano - defesa / 3);
        vida = Math.max(0, vida - finalDano);
        return finalDano;
    }

    // Diz se o inimigo ainda esta vivo.
    public boolean vivo() { return vida > 0; }

    // Metodos que devolvem (get) as informacoes do inimigo.
    public String getNome() { return nome; }
    public int getVida() { return vida; }
    public int getVidaMaxima() { return vidaMaxima; }
    public int getDefesa() { return defesa; }
    public int getVelocidade() { return velocidade; }
    public int getSorte() { return sorte; }
    public int getRecompensa() { return recompensa; }

    // Sorteia os itens que o inimigo deixa cair (drops).
    // Cada item tem uma chance em porcentagem de aparecer.
    public List<Item> sortearDrops() {
        List<Item> d = new ArrayList<Item>();
        if ("Lobinho".equals(nome)) {
            if (random.nextInt(100) < 70) d.add(new Item("Pocao de Vida", "Pocao", 15, 30, 0));
            if (random.nextInt(100) < 55) d.add(new Item("Pocao de Mana", "Pocao", 18, 0, 20));
        } else if ("Lobo".equals(nome)) {
            if (random.nextInt(100) < 80) d.add(new Item("Pocao de Vida", "Pocao", 15, 30, 0));
            if (random.nextInt(100) < 70) d.add(new Item("Pocao de Mana", "Pocao", 18, 0, 20));
        }
        return d;
    }

    // Sorteia o material que o inimigo deixa cair.
    public Item sortearMaterial() {
        if ("Lobinho".equals(nome) || "Lobo".equals(nome)) return new Item("Chifre de Lobo", "Material", 8, 0, 0);
        return null;
    }
}
