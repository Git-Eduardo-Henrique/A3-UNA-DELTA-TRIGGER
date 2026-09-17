package model;

import java.util.Random;

public abstract class Personagem {
    protected final String nome;
    protected int nivel, vida, vidaMaxima, mana, manaMaxima, pontosAtributo, experiencia;
    protected final Atributos base;
    protected Equipamento armaEquipada, armaduraEquipada, botasEquipadas, acessorioEquipado;
    protected boolean defendendo;
    protected final Random random = new Random();
    public Personagem(String nome,int hp,int mp,Atributos atributos){this.nome=nome;nivel=1;vidaMaxima=hp;vida=hp;manaMaxima=mp;mana=mp;base=atributos;}
    public abstract boolean habilidade(Grupo grupo, Inimigo[] inimigos);
    private int bonus(int atributo){int total=0; total+=bonusEquipamento(armaEquipada,atributo); total+=bonusEquipamento(armaduraEquipada,atributo); total+=bonusEquipamento(botasEquipadas,atributo); total+=bonusEquipamento(acessorioEquipado,atributo); return total;}
    private int bonusEquipamento(Equipamento e,int a){if(e==null)return 0;switch(a){case 1:return e.getForca();case 2:return e.getDefesa();case 3:return e.getInteligencia();case 4:return e.getResistencia();case 5:return e.getVelocidade();case 6:return e.getSorte();default:return 0;}}
    public int getForca(){return base.getForca()+bonus(1);} public int getDefesa(){return base.getDefesa()+bonus(2);} public int getInteligencia(){return base.getInteligencia()+bonus(3);} public int getResistencia(){return base.getResistencia()+bonus(4);} public int getVelocidade(){return base.getVelocidade()+bonus(5);} public int getSorte(){return base.getSorte()+bonus(6);}
    public int atacar(){return Math.max(1,getForca()*2+random.nextInt(5)-2);}
    public int receberDano(int dano){int reducao=getDefesa()/2+getResistencia()/4; if(defendendo){reducao+=5; defendendo=false;} int finalDano=Math.max(1,dano-reducao); vida=Math.max(0,vida-finalDano); return finalDano;}
    public void defender(){defendendo=true;}
    public void recuperarManaDefesa(){int ganho=Math.max(3,3+getInteligencia()/3); mana=Math.min(manaMaxima,mana+ganho); System.out.println(Cores.sucesso(nome+" recuperou "+ganho+" de mana."));}
    public boolean usarMana(int custo){if(custo<0||mana<custo)return false;mana-=custo;return true;}
    public void curar(int v){if(v>0)vida=Math.min(vidaMaxima,vida+v);} public void curarMana(int v){if(v>0)mana=Math.min(manaMaxima,mana+v);}
    public void recuperarTudo(){vida=vidaMaxima;mana=manaMaxima;defendendo=false;}
    public void subirNivel(){nivel++;pontosAtributo+=3;vidaMaxima+=10;manaMaxima+=5;recuperarTudo();}
    public boolean distribuirPonto(int op){if(pontosAtributo<=0||!base.aumentar(op))return false;pontosAtributo--;return true;}
    public void limparEquipamentosEquipados(){armaEquipada=null;armaduraEquipada=null;botasEquipadas=null;acessorioEquipado=null;}
    public boolean podeEquipar(Equipamento e){
        if(e==null || !e.podeSerUsadoPor(this)) return false;
        String t=e.getTipo();
        return "Arma".equalsIgnoreCase(t)||"Armadura".equalsIgnoreCase(t)||"Botas".equalsIgnoreCase(t)||"Acessorio".equalsIgnoreCase(t)||"Acessório".equalsIgnoreCase(t);
    }
    public Equipamento equipamentoDoTipo(String tipo){
        if("Arma".equalsIgnoreCase(tipo)) return armaEquipada;
        if("Armadura".equalsIgnoreCase(tipo)) return armaduraEquipada;
        if("Botas".equalsIgnoreCase(tipo)) return botasEquipadas;
        if("Acessorio".equalsIgnoreCase(tipo)||"Acessório".equalsIgnoreCase(tipo)) return acessorioEquipado;
        return null;
    }
    public boolean equiparDireto(Equipamento e){
        if(!podeEquipar(e)) return false;
        String t=e.getTipo();
        if("Arma".equalsIgnoreCase(t)) armaEquipada=e;
        else if("Armadura".equalsIgnoreCase(t)) armaduraEquipada=e;
        else if("Botas".equalsIgnoreCase(t)) botasEquipadas=e;
        else if("Acessorio".equalsIgnoreCase(t)||"Acessório".equalsIgnoreCase(t)) acessorioEquipado=e;
        return true;
    }
    public boolean vivo(){return vida>0;} public String getNome(){return nome;} public int getVida(){return vida;} public int getVidaMaxima(){return vidaMaxima;} public int getMana(){return mana;} public int getManaMaxima(){return manaMaxima;} public int getNivel(){return nivel;} public int getPontosAtributo(){return pontosAtributo;} public int getExperiencia(){return experiencia;} public Inventario getInventario(){return null;}
    public Equipamento getArmaEquipada(){return armaEquipada;} public Equipamento getArmaduraEquipada(){return armaduraEquipada;} public Equipamento getBotasEquipadas(){return botasEquipadas;} public Equipamento getAcessorioEquipado(){return acessorioEquipado;} public Atributos getAtributosBase(){return base;}
    public void definirNivel(int v){nivel=Math.max(1,v);} public void definirVidaMaxima(int v){vidaMaxima=Math.max(1,v);} public void definirVida(int v){vida=Math.max(0,Math.min(vidaMaxima,v));} public void definirManaMaxima(int v){manaMaxima=Math.max(0,v);} public void definirMana(int v){mana=Math.max(0,Math.min(manaMaxima,v));} public void definirPontosAtributo(int v){pontosAtributo=Math.max(0,v);} public void definirExperiencia(int v){experiencia=Math.max(0,v);}
    public String classe(){return getClass().getSimpleName();}
    public String nomeColorido(){return Cores.nomePersonagem(nome);}
    public void status(){System.out.println("\n=== "+nomeColorido()+" ===");System.out.println("Nivel: "+nivel+" | HP: "+vida+"/"+vidaMaxima+" | Mana: "+mana+"/"+manaMaxima);System.out.println("Forca: "+getForca()+" | Defesa: "+getDefesa()+" | Inteligencia: "+getInteligencia());System.out.println("Resistencia: "+getResistencia()+" | Velocidade: "+getVelocidade()+" | Sorte: "+getSorte());System.out.println("Arma: "+nomeEquip(armaEquipada));System.out.println("Armadura: "+nomeEquip(armaduraEquipada));System.out.println("Botas: "+nomeEquip(botasEquipadas));System.out.println("Acessorio: "+nomeEquip(acessorioEquipado));if(pontosAtributo>0)System.out.println(Cores.aviso("Pontos de atributo disponiveis: "+pontosAtributo));}
    private String nomeEquip(Equipamento e){return e==null?"nenhum":e.getNome();}
}
