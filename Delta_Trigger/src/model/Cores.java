package model;

public final class Cores {
    public static final String RESET = "\u001B[0m";
    public static final String AZUL = "\u001B[34m";
    public static final String ROSA = "\u001B[95m";
    public static final String VERDE = "\u001B[32m";
    public static final String VERMELHO = "\u001B[31m";
    public static final String AMARELO = "\u001B[33m";
    public static final String ROXO = "\u001B[35m";
    public static final String CINZA = "\u001B[37m";
    public static final String CIANO = "\u001B[36m";

    private Cores() {}

    public static String ciano(String texto) { return CIANO + texto + RESET; }
    public static String normal(String texto) { return CINZA + texto + RESET; }
    public static String kael(String texto) { return AZUL + texto + RESET; }
    public static String lyra(String texto) { return ROSA + texto + RESET; }
    public static String elyra(String texto) { return CIANO + texto + RESET; }
    public static String inimigo(String texto) { return VERMELHO + texto + RESET; }
    public static String sucesso(String texto) { return VERDE + texto + RESET; }
    public static String vermelho(String texto) { return VERMELHO + texto + RESET; }
    public static String amarelo(String texto) { return AMARELO + texto + RESET; }
    public static String aviso(String texto) { return AMARELO + texto + RESET; }
    public static String magia(String texto) { return ROXO + texto + RESET; }
}
