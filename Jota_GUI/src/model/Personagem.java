package model;

import java.util.Random;

public abstract class Personagem {
    protected final String nome;
    protected int nivel;
    protected int vida;
    protected int vidaMaxima;
    protected int mana;
    protected int manaMaxima;
    protected int pontosAtributo;
    protected int dinheiro;
    protected int experiencia;
    protected final Atributos base;
    protected Equipamento armaEquipada;
    protected Equipamento armaduraEquipada;
    protected Equipamento botasEquipadas;
    protected final Inventario inventario = new Inventario();
    protected boolean defendendo;
    protected final Random random = new Random();

    public Personagem(String nome, int hp, int mp, Atributos atributos, int dinheiro) {
        this.nome = nome;
        this.nivel = 1;
        this.vidaMaxima = hp;
        this.vida = hp;
        this.manaMaxima = mp;
        this.mana = mp;
        this.base = atributos;
        this.dinheiro = Math.max(0, dinheiro);
    }

    public abstract boolean habilidade(Personagem[] grupo, Inimigo[] inimigos);

    private int bonus(int atributo) {
        int total = 0;
        total += bonusEquipamento(armaEquipada, atributo);
        total += bonusEquipamento(armaduraEquipada, atributo);
        total += bonusEquipamento(botasEquipadas, atributo);
        return total;
    }

    private int bonusEquipamento(Equipamento equipamento, int atributo) {
        if (equipamento == null) return 0;
        switch (atributo) {
            case 1: return equipamento.getForca();
            case 2: return equipamento.getDefesa();
            case 3: return equipamento.getInteligencia();
            case 4: return equipamento.getResistencia();
            case 5: return equipamento.getVelocidade();
            case 6: return equipamento.getSorte();
            default: return 0;
        }
    }

    public int getForca() { return base.getForca() + bonus(1); }
    public int getDefesa() { return base.getDefesa() + bonus(2); }
    public int getInteligencia() { return base.getInteligencia() + bonus(3); }
    public int getResistencia() { return base.getResistencia() + bonus(4); }
    public int getVelocidade() { return base.getVelocidade() + bonus(5); }
    public int getSorte() { return base.getSorte() + bonus(6); }

    public int atacar() {
        int dano = getForca() * 2 + random.nextInt(5) - 2;
        if (random.nextInt(100) < Math.min(40, 5 + getSorte() * 2)) dano += Math.max(1, getForca() / 2);
        return Math.max(1, dano);
    }

    public int receberDano(int dano) {
        int reducao = getDefesa() / 2 + getResistencia() / 4;
        if (defendendo) {
            reducao += 5;
            defendendo = false;
        }
        int danoFinal = Math.max(1, dano - reducao);
        vida = Math.max(0, vida - danoFinal);
        return danoFinal;
    }

    public void defender() {
        defendendo = true;
        System.out.println(Cores.aviso(nome + " assumiu uma postura defensiva para o proximo ataque."));
    }

    public boolean usarMana(int custo) {
        if (custo < 0 || mana < custo) return false;
        mana -= custo;
        return true;
    }

    public void curar(int valor) {
        if (valor > 0) vida = Math.min(vidaMaxima, vida + valor);
    }

    public void curarMana(int valor) {
        if (valor > 0) mana = Math.min(manaMaxima, mana + valor);
    }

    public void recuperarTudo() {
        vida = vidaMaxima;
        mana = manaMaxima;
        defendendo = false;
    }

    public void subirNivel() {
        nivel++;
        pontosAtributo += 3;
        vidaMaxima += 10;
        manaMaxima += 5;
        recuperarTudo();
    }

    public boolean distribuirPonto(int opcao) {
        if (pontosAtributo <= 0) return false;
        if (!base.aumentar(opcao)) return false;
        pontosAtributo--;
        return true;
    }

    public void limparEquipamentosEquipados() {
        armaEquipada = null;
        armaduraEquipada = null;
        botasEquipadas = null;
    }

    public void equipar(Equipamento equipamento) {
        if (equipamento == null) return;
        String tipo = equipamento.getTipo();
        if ("Arma".equalsIgnoreCase(tipo)) armaEquipada = equipamento;
        else if ("Armadura".equalsIgnoreCase(tipo)) armaduraEquipada = equipamento;
        else if ("Botas".equalsIgnoreCase(tipo)) botasEquipadas = equipamento;
    }

    public boolean vivo() { return vida > 0; }
    public String getNome() { return nome; }
    public int getVida() { return vida; }
    public int getVidaMaxima() { return vidaMaxima; }
    public int getMana() { return mana; }
    public int getManaMaxima() { return manaMaxima; }
    public int getNivel() { return nivel; }
    public int getDinheiro() { return dinheiro; }
    public int getPontosAtributo() { return pontosAtributo; }
    public int getExperiencia() { return experiencia; }
    public Inventario getInventario() { return inventario; }
    public Equipamento getArmaEquipada() { return armaEquipada; }
    public Equipamento getArmaduraEquipada() { return armaduraEquipada; }
    public Equipamento getBotasEquipadas() { return botasEquipadas; }
    public Atributos getAtributosBase() { return base; }

    public void adicionarDinheiro(int valor) { if (valor > 0) dinheiro += valor; }
    public boolean gastarDinheiro(int valor) {
        if (valor < 0 || dinheiro < valor) return false;
        dinheiro -= valor;
        return true;
    }

    public void definirNivel(int valor) { nivel = Math.max(1, valor); }
    public void definirVidaMaxima(int valor) { vidaMaxima = Math.max(1, valor); }
    public void definirVida(int valor) { vida = Math.max(0, Math.min(vidaMaxima, valor)); }
    public void definirManaMaxima(int valor) { manaMaxima = Math.max(0, valor); }
    public void definirMana(int valor) { mana = Math.max(0, Math.min(manaMaxima, valor)); }
    public void definirDinheiro(int valor) { dinheiro = Math.max(0, valor); }
    public void definirPontosAtributo(int valor) { pontosAtributo = Math.max(0, valor); }
    public void definirExperiencia(int valor) { experiencia = Math.max(0, valor); }

    public void status() {
        String nomeColorido = nome.equals("Kael") ? Cores.kael(nome.toUpperCase())
                : nome.equals("Lyra") ? Cores.lyra(nome.toUpperCase()) : Cores.elyra(nome.toUpperCase());
        System.out.println("\n=== " + nomeColorido + " ===");
        System.out.println("Nivel: " + nivel + " | HP: " + vida + "/" + vidaMaxima
                + " | Mana: " + mana + "/" + manaMaxima + " | $" + dinheiro);
        base.mostrar();
        System.out.println("Arma: " + (armaEquipada == null ? "nenhuma" : armaEquipada.getNome()));
        System.out.println("Armadura: " + (armaduraEquipada == null ? "nenhuma" : armaduraEquipada.getNome()));
        System.out.println("Botas: " + (botasEquipadas == null ? "nenhuma" : botasEquipadas.getNome()));
        if (pontosAtributo > 0) System.out.println(Cores.aviso("Pontos de atributo disponiveis: " + pontosAtributo));
    }
}
