package model;

public class Item {
    private final String nome;
    private final String tipo;
    private final int preco;
    private final int curaHp;
    private final int curaMana;
    private int quantidade;

    public Item(String nome, String tipo, int preco, int curaHp, int curaMana) {
        this(nome, tipo, preco, curaHp, curaMana, 1);
    }

    public Item(String nome, String tipo, int preco, int curaHp, int curaMana, int quantidade) {
        this.nome = nome;
        this.tipo = tipo;
        this.preco = Math.max(0, preco);
        this.curaHp = Math.max(0, curaHp);
        this.curaMana = Math.max(0, curaMana);
        this.quantidade = Math.max(1, quantidade);
    }

    public String getNome() { return nome; }
    public String getTipo() { return tipo; }
    public int getPreco() { return preco; }
    public int getCuraHp() { return curaHp; }
    public int getCuraMana() { return curaMana; }
    public int getQuantidade() { return quantidade; }

    public void adicionarQuantidade(int valor) {
        if (valor > 0) quantidade += valor;
    }

    public boolean removerUma() {
        if (quantidade <= 0) return false;
        quantidade--;
        return true;
    }

    public boolean utilizavelEmBatalha() {
        return curaHp > 0 || curaMana > 0;
    }

    public String descricao() {
        return nome + " [" + tipo + "] $" + preco + " (x" + quantidade + ")";
    }
}
