package model;

public class Equipamento {
    private final String nome, tipo, raridade;
    private final int preco, forca, defesa, inteligencia, resistencia, velocidade, sorte;
    public Equipamento(String nome, String tipo, int preco, int forca, int defesa, int inteligencia, int resistencia, int velocidade, int sorte) {
        this(nome, tipo, "Comum", preco, forca, defesa, inteligencia, resistencia, velocidade, sorte);
    }
    public Equipamento(String nome, String tipo, String raridade, int preco, int forca, int defesa, int inteligencia, int resistencia, int velocidade, int sorte) {
        this.nome = nome; this.tipo = tipo; this.raridade = raridade == null ? "Comum" : raridade; this.preco = Math.max(0, preco);
        this.forca=forca; this.defesa=defesa; this.inteligencia=inteligencia; this.resistencia=resistencia; this.velocidade=velocidade; this.sorte=sorte;
    }
    public String getNome(){return nome;} public String getTipo(){return tipo;} public String getRaridade(){return raridade;} public int getPreco(){return preco;}
    public int getForca(){return forca;} public int getDefesa(){return defesa;} public int getInteligencia(){return inteligencia;} public int getResistencia(){return resistencia;} public int getVelocidade(){return velocidade;} public int getSorte(){return sorte;}
    public String descricao() {
        String stats = "Forca +"+forca+" | Defesa +"+defesa+" | Inteligencia +"+inteligencia+" | Resistencia +"+resistencia+" | Velocidade +"+velocidade+" | Sorte +"+sorte;
        return Cores.raridade(raridade, nome) + " [" + tipo + ", " + raridade + "] | " + stats + " | $" + preco;
    }
}
