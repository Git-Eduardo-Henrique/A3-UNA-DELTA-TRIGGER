// ============================================================
// ARQUIVO: Caverna.java
// Representa um cenario do tipo "Caverna", onde acontecem batalhas.
// Uma caverna e dividida em varias "waves" (ondas de inimigos).
// ============================================================

package model;

import java.util.ArrayList;
import java.util.List;

public class Caverna extends Cenario {
    // Lista de ondas (waves) de inimigos desta caverna.
    private final List<Wave> waves = new ArrayList<Wave>();

    // Construtor: repassa os dados para a classe Cenario.
    public Caverna(String id, String nome, String descricao) {
        super(id, nome, descricao);
    }

    // Informa o tipo deste cenario.
    public String getTipo() { return "Caverna"; }

    // Adiciona uma nova onda de inimigos a caverna.
    public void adicionarWave(Wave w) { waves.add(w); }

    // Devolve a lista de ondas da caverna.
    public List<Wave> getWaves() { return waves; }
}
