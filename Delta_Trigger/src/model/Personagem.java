// ============================================================
// ARQUIVO: Personagem.java
// Classe base (modelo) de todos os personagens jogaveis.
// Guarda vida, mana, nivel, atributos e equipamentos.
// Cada classe (Guerreiro, Suporte, Elyra) herda desta e cria
// sua propria habilidade especial.
// ============================================================

package model;

import java.util.Random;

public abstract class Personagem {
    // Dados basicos do personagem.
    protected final String nome;           // Nome exibido na tela.
    protected int nivel, vida, vidaMaxima, mana, manaMaxima, pontosAtributo, experiencia;
    protected final Atributos base;        // Atributos sem contar equipamentos.
    // Equipamentos atualmente usados pelo personagem.
    protected Equipamento armaEquipada, armaduraEquipada, botasEquipadas, acessorioEquipado;
    protected boolean defendendo;          // true quando esta defendendo.
    protected final Random random = new Random(); // Para valores aleatorios.

    // Construtor: cria o personagem no nivel 1 com vida e mana cheias.
    public Personagem(String nome, int hp, int mp, Atributos atributos) {
        this.nome = nome;
        nivel = 1;
        vidaMaxima = hp;
        vida = hp;
        manaMaxima = mp;
        mana = mp;
        base = atributos;
    }

    // Metodo abstrato: cada classe implementa sua habilidade especial.
    public abstract boolean habilidade(Grupo grupo, Inimigo[] inimigos);

    // Soma os bonus de todos os equipamentos em um atributo.
    // 1=forca, 2=defesa, 3=inteligencia, 4=resistencia, 5=velocidade, 6=sorte.
    private int bonus(int atributo) {
        int total = 0;
        total += bonusEquipamento(armaEquipada, atributo);
        total += bonusEquipamento(armaduraEquipada, atributo);
        total += bonusEquipamento(botasEquipadas, atributo);
        total += bonusEquipamento(acessorioEquipado, atributo);
        return total;
    }

    // Devolve o bonus de um equipamento especifico para o atributo pedido.
    private int bonusEquipamento(Equipamento e, int a) {
        if (e == null) return 0;
        switch (a) {
            case 1: return e.getForca();
            case 2: return e.getDefesa();
            case 3: return e.getInteligencia();
            case 4: return e.getResistencia();
            case 5: return e.getVelocidade();
            case 6: return e.getSorte();
            default: return 0;
        }
    }

    // Atributos finais = atributos base + bonus dos equipamentos.
    public int getForca(){return base.getForca()+bonus(1);} public int getDefesa(){return base.getDefesa()+bonus(2);} public int getInteligencia(){return base.getInteligencia()+bonus(3);} public int getResistencia(){return base.getResistencia()+bonus(4);} public int getVelocidade(){return base.getVelocidade()+bonus(5);} public int getSorte(){return base.getSorte()+bonus(6);}

    // Calcula o dano de um ataque normal (com pequena variacao aleatoria).
    public int atacar(){return Math.max(1,getForca()*2+random.nextInt(5)-2);}

    // Recebe dano. A defesa e a resistencia reduzem o dano e,
    // se estiver defendendo, a reducao aumenta em +5.
    // Devolve o dano que realmente foi causado.
    public int receberDano(int dano){
        int reducao=getDefesa()/2+getResistencia()/4;
        if(defendendo){reducao+=5; defendendo=false;} // Defender vale so uma vez.
        int finalDano=Math.max(1,dano-reducao);
        vida=Math.max(0,vida-finalDano);
        return finalDano;
    }

    // Ativa a postura defensiva para o proximo ataque recebido.
    public void defender(){defendendo=true;}

    // Recupera mana ao usar a acao "defender/recuperar mana".
    public void recuperarManaDefesa(){
        int ganho=Math.max(3,3+getInteligencia()/3);
        mana=Math.min(manaMaxima,mana+ganho);
        System.out.println(Cores.sucesso(nome+" recuperou "+ganho+" de mana."));
    }

    // Gasta mana. Retorna false se o custo for invalido ou faltar mana.
    public boolean usarMana(int custo){if(custo<0||mana<custo)return false;mana-=custo;return true;}

    // Cura vida ou mana, sempre respeitando o valor maximo.
    public void curar(int v){if(v>0)vida=Math.min(vidaMaxima,vida+v);} public void curarMana(int v){if(v>0)mana=Math.min(manaMaxima,mana+v);}

    // Restaura vida e mana ao maximo (usado ao descansar na pousada).
    public void recuperarTudo(){vida=vidaMaxima;mana=manaMaxima;defendendo=false;}

    // Sobe um nivel: ganha pontos de atributo e mais vida/mana maxima.
    public void subirNivel(){nivel++;pontosAtributo+=3;vidaMaxima+=10;manaMaxima+=5;recuperarTudo();}

    // Gasta um ponto de atributo para aumentar o atributo escolhido.
    public boolean distribuirPonto(int op){if(pontosAtributo<=0||!base.aumentar(op))return false;pontosAtributo--;return true;}

    // Remove todos os equipamentos equipados no personagem.
    public void limparEquipamentosEquipados(){armaEquipada=null;armaduraEquipada=null;botasEquipadas=null;acessorioEquipado=null;}

    // Verifica se o personagem pode usar o equipamento informado.
    public boolean podeEquipar(Equipamento e){
        if(e==null || !e.podeSerUsadoPor(this)) return false;
        String t=e.getTipo();
        return "Arma".equalsIgnoreCase(t)||"Armadura".equalsIgnoreCase(t)||"Botas".equalsIgnoreCase(t)||"Acessorio".equalsIgnoreCase(t)||"Acessório".equalsIgnoreCase(t);
    }
    // Devolve o equipamento equipado do tipo pedido (Arma, Armadura...).
    public Equipamento equipamentoDoTipo(String tipo){
        if("Arma".equalsIgnoreCase(tipo)) return armaEquipada;
        if("Armadura".equalsIgnoreCase(tipo)) return armaduraEquipada;
        if("Botas".equalsIgnoreCase(tipo)) return botasEquipadas;
        if("Acessorio".equalsIgnoreCase(tipo)||"Acessório".equalsIgnoreCase(tipo)) return acessorioEquipado;
        return null;
    }
    // Equipa o item no "slot" correto (arma, armadura, botas ou acessorio).
    public boolean equiparDireto(Equipamento e){
        if(!podeEquipar(e)) return false;
        String t=e.getTipo();
        if("Arma".equalsIgnoreCase(t)) armaEquipada=e;
        else if("Armadura".equalsIgnoreCase(t)) armaduraEquipada=e;
        else if("Botas".equalsIgnoreCase(t)) botasEquipadas=e;
        else if("Acessorio".equalsIgnoreCase(t)||"Acessório".equalsIgnoreCase(t)) acessorioEquipado=e;
        return true;
    }
    // Diz se o personagem esta vivo e devolve (get) seus dados basicos.
    public boolean vivo(){return vida>0;} public String getNome(){return nome;} public int getVida(){return vida;} public int getVidaMaxima(){return vidaMaxima;} public int getMana(){return mana;} public int getManaMaxima(){return manaMaxima;} public int getNivel(){return nivel;} public int getPontosAtributo(){return pontosAtributo;} public int getExperiencia(){return experiencia;} public Inventario getInventario(){return null;}
    public Equipamento getArmaEquipada(){return armaEquipada;} public Equipamento getArmaduraEquipada(){return armaduraEquipada;} public Equipamento getBotasEquipadas(){return botasEquipadas;} public Equipamento getAcessorioEquipado(){return acessorioEquipado;} public Atributos getAtributosBase(){return base;}
    // Metodos usados principalmente ao carregar um save para
    // reaplicar os valores que o personagem tinha antes.
    public void definirNivel(int v){nivel=Math.max(1,v);} public void definirVidaMaxima(int v){vidaMaxima=Math.max(1,v);} public void definirVida(int v){vida=Math.max(0,Math.min(vidaMaxima,v));} public void definirManaMaxima(int v){manaMaxima=Math.max(0,v);} public void definirMana(int v){mana=Math.max(0,Math.min(manaMaxima,v));} public void definirPontosAtributo(int v){pontosAtributo=Math.max(0,v);} public void definirExperiencia(int v){experiencia=Math.max(0,v);}
    // Devolve o nome da classe do personagem (Guerreiro, Suporte, Elyra...).
    public String classe(){return getClass().getSimpleName();}

    // Devolve o nome ja pintado com a cor daquele personagem.
    public String nomeColorido(){return Cores.nomePersonagem(nome);}

    // Mostra na tela a ficha completa do personagem.
    public void status(){System.out.println("\n=== "+nomeColorido()+" ===");System.out.println("Nivel: "+nivel+" | HP: "+vida+"/"+vidaMaxima+" | Mana: "+mana+"/"+manaMaxima);System.out.println("Forca: "+getForca()+" | Defesa: "+getDefesa()+" | Inteligencia: "+getInteligencia());System.out.println("Resistencia: "+getResistencia()+" | Velocidade: "+getVelocidade()+" | Sorte: "+getSorte());System.out.println("Arma: "+nomeEquip(armaEquipada));System.out.println("Armadura: "+nomeEquip(armaduraEquipada));System.out.println("Botas: "+nomeEquip(botasEquipadas));System.out.println("Acessorio: "+nomeEquip(acessorioEquipado));if(pontosAtributo>0)System.out.println(Cores.aviso("Pontos de atributo disponiveis: "+pontosAtributo));}
    // Devolve o nome do equipamento ou "nenhum" se estiver vazio.
    private String nomeEquip(Equipamento e){return e==null?"nenhum":e.getNome();}
}
