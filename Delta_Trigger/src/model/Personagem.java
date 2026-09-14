package model;
import java.util.Random;

public abstract class Personagem {
    protected String nome;
    protected int nivel = 1, vida, vidaMaxima, mana, manaMaxima;
    protected int pontosAtributo, dinheiro, experiencia;
    protected Atributos base;
    protected Equipamento equipamento;
    protected Inventario inventario = new Inventario();
    protected boolean defendendo;
    protected Random random = new Random();

    public Personagem(String nome, int hp, int mp, Atributos atributos, int dinheiro) {
        this.nome = nome; vidaMaxima = hp; vida = hp; manaMaxima = mp; mana = mp;
        base = atributos; this.dinheiro = dinheiro;
    }

    public abstract void habilidade(Personagem[] grupo, Inimigo inimigo);

    public int getForca() { return base.getForca() + bonus(1); }
    public int getDefesa() { return base.getDefesa() + bonus(2); }
    public int getInteligencia() { return base.getInteligencia() + bonus(3); }
    public int getResistencia() { return base.getResistencia() + bonus(4); }
    public int getVelocidade() { return base.getVelocidade() + bonus(5); }
    public int getSorte() { return base.getSorte() + bonus(6); }

    private int bonus(int tipo) {
        if (equipamento == null) return 0;
        switch (tipo) {
            case 1: return equipamento.getForca();
            case 2: return equipamento.getDefesa();
            case 3: return equipamento.getInteligencia();
            case 4: return equipamento.getResistencia();
            case 5: return equipamento.getVelocidade();
            case 6: return equipamento.getSorte();
            default: return 0;
        }
    }

    public int atacar() {
        return Math.max(1, getForca() * 2 + random.nextInt(7) - 3);
    }

    public int receberDano(int dano) {
        int reducao = getDefesa() / 3 + getResistencia() / 4;
        if (defendendo) { reducao += 5; defendendo = false; }
        int finalDano = Math.max(1, dano - reducao);
        vida = Math.max(0, vida - finalDano);
        return finalDano;
    }

    public void defender() { defendendo = true; System.out.println(nome + " esta se defendendo."); }
    public boolean usarMana(int custo) {
        if (mana < custo) { System.out.println("Mana insuficiente."); return false; }
        mana -= custo; return true;
    }
    public void curar(int valor) { vida = Math.min(vidaMaxima, vida + valor); }
    public void curarMana(int valor) { mana = Math.min(manaMaxima, mana + valor); }
    public void recuperarTudo() { vida = vidaMaxima; mana = manaMaxima; }

    public void subirNivel() {
        nivel++; pontosAtributo += 3; vidaMaxima += 10; manaMaxima += 5;
        recuperarTudo();
        System.out.println("\n*** " + nome + " chegou ao nivel " + nivel + "! ***");
        System.out.println("Recebeu 3 pontos de atributo.");
    }

    public void distribuirPonto(int opcao) {
        if (pontosAtributo <= 0) return;
        if (opcao < 1 || opcao > 6) { System.out.println("Opcao invalida."); return; }
        base.aumentar(opcao); pontosAtributo--;
    }

    public void equipar(Equipamento e) {
        equipamento = e;
        System.out.println(nome + " equipou " + e.getNome() + ".");
    }

    public boolean vivo() { return vida > 0; }
    public String getNome() { return nome; }
    public int getVida() { return vida; }
    public int getVidaMaxima() { return vidaMaxima; }
    public int getMana() { return mana; }
    public int getManaMaxima() { return manaMaxima; }
    public int getNivel() { return nivel; }
    public int getDinheiro() { return dinheiro; }
    public void adicionarDinheiro(int valor) { dinheiro += valor; }
    public boolean gastarDinheiro(int valor) {
        if (dinheiro < valor) return false;
        dinheiro -= valor; return true;
    }
    public Inventario getInventario() { return inventario; }
    public Equipamento getEquipamento() { return equipamento; }
    public int getPontosAtributo() { return pontosAtributo; }

    public void status() {
        System.out.println("\n=== " + nome.toUpperCase() + " ===");
        System.out.println("Nivel " + nivel + " | HP " + vida + "/" + vidaMaxima
                + " | Mana " + mana + "/" + manaMaxima + " | $" + dinheiro);
        base.mostrar();
        System.out.println("Equipado: " + (equipamento == null ? "nenhum" : equipamento.getNome()));
        System.out.println("Pontos: " + pontosAtributo);
    }
}
