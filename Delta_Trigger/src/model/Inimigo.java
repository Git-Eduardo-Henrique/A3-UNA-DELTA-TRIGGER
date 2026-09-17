package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Inimigo {
    private final String nome; private int vida; private final int vidaMaxima, ataque, defesa, velocidade, sorte, recompensa; private final Random random=new Random();
    public Inimigo(String nome,int hp,int ataque,int defesa,int velocidade,int sorte,int recompensa){this.nome=nome;vidaMaxima=hp;vida=hp;this.ataque=ataque;this.defesa=defesa;this.velocidade=velocidade;this.sorte=sorte;this.recompensa=recompensa;}
    public int atacar(){return Math.max(1,ataque+random.nextInt(5)-2);} public int receberDano(int dano){int finalDano=Math.max(1,dano-defesa/3);vida=Math.max(0,vida-finalDano);return finalDano;} public boolean vivo(){return vida>0;}
    public String getNome(){return nome;}public int getVida(){return vida;}public int getVidaMaxima(){return vidaMaxima;}public int getDefesa(){return defesa;}public int getVelocidade(){return velocidade;}public int getSorte(){return sorte;}public int getRecompensa(){return recompensa;}
    public List<Item> sortearDrops(){List<Item> d=new ArrayList<Item>();if("Lobinho".equals(nome)){if(random.nextInt(100)<70)d.add(new Item("Pocao de Vida","Pocao",15,30,0));if(random.nextInt(100)<55)d.add(new Item("Pocao de Mana","Pocao",18,0,20));}else if("Lobo".equals(nome)){if(random.nextInt(100)<80)d.add(new Item("Pocao de Vida","Pocao",15,30,0));if(random.nextInt(100)<70)d.add(new Item("Pocao de Mana","Pocao",18,0,20));}return d;}
    public Item sortearMaterial(){if("Lobinho".equals(nome)||"Lobo".equals(nome))return new Item("Chifre de Lobo","Material",8,0,0);return null;}
}
