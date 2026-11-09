// ============================================================
// ARQUIVO: Equipamento.java
// Representa uma peca de equipamento: arma, armadura, botas ou
// acessorio. Cada equipamento da bonus nos atributos do personagem.
// ============================================================

package model;

public class Equipamento {
    // Dados do equipamento. "final" = nao mudam depois de criado.
    private final String nome, tipo, raridade, classePermitida;
    private final int preco, forca, defesa, inteligencia, resistencia, velocidade, sorte;

    // Construtor 1: usa raridade "Comum" e libera para todas as classes.
    public Equipamento(String nome, String tipo, int preco, int forca, int defesa, int inteligencia, int resistencia, int velocidade, int sorte) {
        this(nome, tipo, "Comum", "Todos", preco, forca, defesa, inteligencia, resistencia, velocidade, sorte);
    }

    // Construtor 2: define a raridade, mas libera para todas as classes.
    public Equipamento(String nome, String tipo, String raridade, int preco, int forca, int defesa, int inteligencia, int resistencia, int velocidade, int sorte) {
        this(nome, tipo, raridade, "Todos", preco, forca, defesa, inteligencia, resistencia, velocidade, sorte);
    }

    // Construtor 3 (completo): define todos os dados do equipamento.
    public Equipamento(String nome, String tipo, String raridade, String classePermitida, int preco, int forca, int defesa, int inteligencia, int resistencia, int velocidade, int sorte) {
        this.nome = nome;
        this.tipo = tipo;
        this.raridade = raridade == null || raridade.trim().isEmpty() ? "Comum" : raridade;
        this.classePermitida = classePermitida == null || classePermitida.trim().isEmpty() ? "Todos" : classePermitida;
        this.preco = Math.max(0, preco);
        this.forca = forca; this.defesa = defesa; this.inteligencia = inteligencia;
        this.resistencia = resistencia; this.velocidade = velocidade; this.sorte = sorte;
    }

    // Metodos que devolvem (get) as informacoes do equipamento.
    public String getNome(){return nome;}
    public String getTipo(){return tipo;}
    public String getRaridade(){return raridade;}
    public String getClassePermitida(){return classePermitida;}
    public int getPreco(){return preco;}
    public int getForca(){return forca;}
    public int getDefesa(){return defesa;}
    public int getInteligencia(){return inteligencia;}
    public int getResistencia(){return resistencia;}
    public int getVelocidade(){return velocidade;}
    public int getSorte(){return sorte;}

    // Verifica se o personagem pode usar este equipamento.
    // "Todos" libera para qualquer classe; caso contrario, compara os nomes.
    public boolean podeSerUsadoPor(Personagem p) {
        if (p == null || "Todos".equalsIgnoreCase(classePermitida)) return true;
        String classe = p.classe();
        if ("Elyra".equalsIgnoreCase(classe)) classe = "Arqueira Arcana";
        if ("Varyn".equalsIgnoreCase(classe)) classe = "Mago Sombrio";
        String[] permitidas = classePermitida.split(",");
        for (String permitida : permitidas) if (permitida.trim().equalsIgnoreCase(classe) || permitida.trim().equalsIgnoreCase(p.classe())) return true;
        return false;
    }

    // Monta um texto com todos os bonus e o preco do equipamento.
    public String descricao() {
        String stats = "Forca +"+forca+" | Defesa +"+defesa+" | Inteligencia +"+inteligencia+
                " | Resistencia +"+resistencia+" | Velocidade +"+velocidade+" | Sorte +"+sorte;
        return Cores.raridade(raridade, nome) + " [" + tipo + ", " + raridade + "] | " + stats + " | $" + preco;
    }
}
