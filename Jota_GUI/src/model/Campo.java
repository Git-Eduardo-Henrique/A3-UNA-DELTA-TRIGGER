package model;

public class Campo extends Cenario {
    public Campo(String nome, String descricao) { super(nome, descricao); }
    @Override public String getTipo() { return "Campo"; }
}
