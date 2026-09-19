package model;

public class Caverna extends Cenario {
    public Caverna(String nome, String descricao) { super(nome, descricao); }
    @Override public String getTipo() { return "Caverna"; }
}
