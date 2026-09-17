package model;

public class Equipamento {
    private final String nome, tipo, raridade, classePermitida;
    private final int preco, forca, defesa, inteligencia, resistencia, velocidade, sorte;

    public Equipamento(String nome, String tipo, int preco, int forca, int defesa, int inteligencia, int resistencia, int velocidade, int sorte) {
        this(nome, tipo, "Comum", "Todos", preco, forca, defesa, inteligencia, resistencia, velocidade, sorte);
    }

    public Equipamento(String nome, String tipo, String raridade, int preco, int forca, int defesa, int inteligencia, int resistencia, int velocidade, int sorte) {
        this(nome, tipo, raridade, "Todos", preco, forca, defesa, inteligencia, resistencia, velocidade, sorte);
    }

    public Equipamento(String nome, String tipo, String raridade, String classePermitida, int preco, int forca, int defesa, int inteligencia, int resistencia, int velocidade, int sorte) {
        this.nome = nome;
        this.tipo = tipo;
        this.raridade = raridade == null || raridade.trim().isEmpty() ? "Comum" : raridade;
        this.classePermitida = classePermitida == null || classePermitida.trim().isEmpty() ? "Todos" : classePermitida;
        this.preco = Math.max(0, preco);
        this.forca = forca; this.defesa = defesa; this.inteligencia = inteligencia;
        this.resistencia = resistencia; this.velocidade = velocidade; this.sorte = sorte;
    }

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

    public boolean podeSerUsadoPor(Personagem p) {
        if (p == null || "Todos".equalsIgnoreCase(classePermitida)) return true;
        String classe = p.classe();
        if ("Elyra".equalsIgnoreCase(classe)) classe = "Arqueira Arcana";
        if ("Varyn".equalsIgnoreCase(classe)) classe = "Mago Sombrio";
        String[] permitidas = classePermitida.split(",");
        for (String permitida : permitidas) if (permitida.trim().equalsIgnoreCase(classe) || permitida.trim().equalsIgnoreCase(p.classe())) return true;
        return false;
    }

    public String descricao() {
        String stats = "Forca +"+forca+" | Defesa +"+defesa+" | Inteligencia +"+inteligencia+
                " | Resistencia +"+resistencia+" | Velocidade +"+velocidade+" | Sorte +"+sorte;
        return Cores.raridade(raridade, nome) + " [" + tipo + ", " + raridade + "] | " + stats + " | $" + preco;
    }
}
