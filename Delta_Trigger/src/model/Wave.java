// ============================================================
// ARQUIVO: Wave.java
// Representa uma "onda" de inimigos dentro de uma caverna.
// Por exemplo: uma onda pode ter 3 lobinhos e 2 lobos.
// ============================================================

package model;

import java.util.ArrayList;
import java.util.List;

public class Wave {
    // Numero da onda (1, 2, 3...).
    private final int numero;
    // Lista com os identificadores dos inimigos desta onda.
    private final List<String> inimigos = new ArrayList<String>();
    // Mensagem mostrada na tela quando a onda comeca.
    private final String mensagem;

    // Construtor: guarda o numero e a mensagem da onda.
    public Wave(int numero, String mensagem) {
        this.numero = numero;
        this.mensagem = mensagem;
    }

    // Metodos que devolvem (get) as informacoes da onda.
    public int getNumero() { return numero; }
    public String getMensagem() { return mensagem; }

    // Adiciona varios inimigos iguais de uma so vez.
    // Ex.: adicionarInimigo("LOBO", 3) coloca 3 lobos na onda.
    public void adicionarInimigo(String id, int quantidade) {
        for (int i = 0; i < quantidade; i++) inimigos.add(id);
    }

    // Devolve a lista de inimigos da onda.
    public List<String> getInimigos() { return inimigos; }
}
