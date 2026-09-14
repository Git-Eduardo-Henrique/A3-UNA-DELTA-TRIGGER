package model;

public class Item {
    private String nome, tipo;
    private int preco, curaHp, curaMana;

    public Item(String nome, String tipo, int preco, int curaHp, int curaMana) {
        this.nome = nome; this.tipo = tipo; this.preco = preco;
        this.curaHp = curaHp; this.curaMana = curaMana;
    }

    public String getNome() { return nome; }
    public int getPreco() { return preco; }
    public int getCuraHp() { return curaHp; }
    public int getCuraMana() { return curaMana; }

    public String descricao() {
        return nome + " [" + tipo + "] $" + preco;
    }
}
