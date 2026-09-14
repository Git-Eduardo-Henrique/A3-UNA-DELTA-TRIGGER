package model;

public class Equipamento {
    private String nome, tipo;
    private int preco, forca, defesa, inteligencia, resistencia, velocidade, sorte;

    public Equipamento(String nome, String tipo, int preco, int forca, int defesa,
                       int inteligencia, int resistencia, int velocidade, int sorte) {
        this.nome = nome; this.tipo = tipo; this.preco = preco;
        this.forca = forca; this.defesa = defesa; this.inteligencia = inteligencia;
        this.resistencia = resistencia; this.velocidade = velocidade; this.sorte = sorte;
    }

    public String getNome() { return nome; }
    public int getPreco() { return preco; }
    public int getForca() { return forca; }
    public int getDefesa() { return defesa; }
    public int getInteligencia() { return inteligencia; }
    public int getResistencia() { return resistencia; }
    public int getVelocidade() { return velocidade; }
    public int getSorte() { return sorte; }

    public String descricao() {
        return nome + " [" + tipo + "] F+" + forca + " D+" + defesa
                + " I+" + inteligencia + " R+" + resistencia
                + " V+" + velocidade + " S+" + sorte + " $" + preco;
    }
}
