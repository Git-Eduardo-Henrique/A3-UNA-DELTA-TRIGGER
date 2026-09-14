package model;

public class LoboFactory {
    public static Inimigo criarLobinho(int onda) {
        return new Inimigo("Lobinho", 24 + onda * 3, 7 + onda, 3 + onda / 2, 8 + onda, 8 + onda);
    }

    public static Inimigo criarLobo(int onda) {
        return new Inimigo("Lobo", 42 + onda * 5, 10 + onda * 2, 5 + onda, 9 + onda, 15 + onda * 2);
    }
}
