package model;

public class Cidade extends Cenario {
    public Cidade(String nome, String descricao) { super(nome, descricao); }
    @Override public String getTipo() { return "Cidade"; }
}
