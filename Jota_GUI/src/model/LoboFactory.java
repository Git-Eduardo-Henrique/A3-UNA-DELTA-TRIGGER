package model;

public final class LoboFactory {
    private LoboFactory() {}

    public static Inimigo criarLobinho() {
        return new Inimigo("Lobinho", 20, 7, 3, 8, 8);
    }

    public static Inimigo criarLobo() {
        return new Inimigo("Lobo", 34, 10, 5, 9, 15);
    }
}
