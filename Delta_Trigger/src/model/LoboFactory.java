// ============================================================
// ARQUIVO: LoboFactory.java
// "Fabrica" de lobos: cria inimigos do tipo Lobinho e Lobo.
// Serve como plano B caso os dados nao sejam encontrados no arquivo
// data/monstros.txt.
// ============================================================

package model;

public final class LoboFactory {

    // Construtor privado: esta classe nao precisa ser criada como objeto.
    private LoboFactory() {}

    // Cria um lobinho (inimigo mais fraco).
    // Ordem: nome, vida, ataque, defesa, velocidade, sorte, recompensa.
    public static Inimigo criarLobinho() {
        return new Inimigo("Lobinho", 20, 7, 3, 8, 3, 8);
    }

    // Cria um lobo (inimigo mais forte que o lobinho).
    public static Inimigo criarLobo() {
        return new Inimigo("Lobo", 34, 10, 5, 9, 4, 15);
    }
}
