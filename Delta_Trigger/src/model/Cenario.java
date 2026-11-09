// ============================================================
// ARQUIVO: Cenario.java
// Classe base para os lugares do jogo (campo, cidade, caverna).
// Guarda um identificador, um nome e uma descricao.
// Cada tipo de cenario (Campo, Cidade, Caverna) herda desta classe.
// ============================================================

package model;

public abstract class Cenario {
    // Dados comuns a todo cenario. "final" = nao mudam depois de criados.
    private final String id, nome, descricao;

    // Construtor: guarda os dados recebidos.
    public Cenario(String id, String nome, String descricao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
    }

    // Metodos que devolvem (get) as informacoes do cenario.
    public String getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }

    // Metodo abstrato: cada tipo de cenario deve dizer seu proprio tipo.
    public abstract String getTipo();
}
