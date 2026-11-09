// ============================================================
// ARQUIVO: Item.java
// Representa um item do jogo, como pocao de vida ou material.
// Guarda nome, tipo, preco, quanto cura e a quantidade no inventario.
// ============================================================

package model;

public class Item {
    // Dados do item. "final" significa que nao mudam depois de criados.
    private final String nome, tipo;
    private final int preco, curaHp, curaMana;
    private int quantidade; // Pode mudar (comprar, usar, vender).

    // Construtor curto: cria o item ja com quantidade 1.
    public Item(String nome, String tipo, int preco, int curaHp, int curaMana) { this(nome, tipo, preco, curaHp, curaMana, 1); }

    // Construtor completo: cria o item com a quantidade informada.
    public Item(String nome, String tipo, int preco, int curaHp, int curaMana, int quantidade) {
        this.nome = nome; this.tipo = tipo; this.preco = Math.max(0, preco); this.curaHp = Math.max(0, curaHp); this.curaMana = Math.max(0, curaMana); this.quantidade = Math.max(1, quantidade);
    }
    // Metodos que devolvem (get) as informacoes do item.
    public String getNome() { return nome; }
    public String getTipo() { return tipo; }
    public int getPreco() { return preco; }
    public int getCuraHp() { return curaHp; }
    public int getCuraMana() { return curaMana; }
    public int getQuantidade() { return quantidade; }
    // Soma uma quantidade ao item (so se o valor for positivo).
    public void adicionarQuantidade(int v) { if (v > 0) quantidade += v; }

    // Remove uma unidade. Retorna false se nao houver nenhuma.
    public boolean removerUma() { if (quantidade <= 0) return false; quantidade--; return true; }

    // Remove varias unidades. Retorna false se nao houver o suficiente.
    public boolean removerQuantidade(int v) { if (v <= 0 || quantidade < v) return false; quantidade -= v; return true; }

    // Diz se o item serve para usar em batalha (se cura vida ou mana).
    public boolean utilizavelEmBatalha() { return curaHp > 0 || curaMana > 0; }

    // Monta um texto bonito para mostrar o item na tela.
    public String descricao() { return nome + " [" + tipo + "] $" + preco + " (x" + quantidade + ")"; }
}
