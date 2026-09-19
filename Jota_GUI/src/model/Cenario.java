package model;

public abstract class Cenario {
    private final String nome;
    private final String descricao;

    public Cenario(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public abstract String getTipo();
}
