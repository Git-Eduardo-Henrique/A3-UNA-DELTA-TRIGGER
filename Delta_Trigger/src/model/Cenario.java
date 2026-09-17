package model;
public abstract class Cenario { private final String id,nome,descricao; public Cenario(String id,String nome,String descricao){this.id=id;this.nome=nome;this.descricao=descricao;} public String getId(){return id;}public String getNome(){return nome;}public String getDescricao(){return descricao;}public abstract String getTipo(); }
