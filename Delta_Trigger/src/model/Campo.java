// ============================================================
// ARQUIVO: Campo.java
// Representa um cenario do tipo "Campo" (area aberta).
// Herda os dados basicos de Cenario e diz que seu tipo e "Campo".
// ============================================================

package model;

public class Campo extends Cenario {

    // Construtor: repassa os dados para a classe Cenario.
    public Campo(String id, String nome, String descricao) {
        super(id, nome, descricao);
    }

    // Informa o tipo deste cenario.
    public String getTipo() { return "Campo"; }
}
