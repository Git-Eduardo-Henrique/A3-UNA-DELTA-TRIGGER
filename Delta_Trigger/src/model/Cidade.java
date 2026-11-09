// ============================================================
// ARQUIVO: Cidade.java
// Representa um cenario do tipo "Cidade" (local seguro com lojas,
// pousadas e tavernas). Herda os dados basicos de Cenario.
// ============================================================

package model;

public class Cidade extends Cenario {

    // Construtor: repassa os dados para a classe Cenario.
    public Cidade(String id, String nome, String descricao) {
        super(id, nome, descricao);
    }

    // Informa o tipo deste cenario.
    public String getTipo() { return "Cidade"; }
}
