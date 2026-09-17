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
    public static final String DOURADO = "\u001B[33m";
    private Cores() {}
    public static String normal(String s) { return CINZA + s + RESET; }
    public static String kael(String s) { return AZUL + s + RESET; }
    public static String lyra(String s) { return ROSA + s + RESET; }
    public static String elyra(String s) { return CIANO + s + RESET; }
    public static String inimigo(String s) { return VERMELHO + s + RESET; }
    public static String sucesso(String s) { return VERDE + s + RESET; }
    public static String aviso(String s) { return AMARELO + s + RESET; }
    public static String amarelo(String s) { return AMARELO + s + RESET; }
    public static String magia(String s) { return ROXO + s + RESET; }
    public static String ciano(String s) { return CIANO + s + RESET; }
    public static String vermelho(String s) { return VERMELHO + s + RESET; }
    public static String nomePersonagem(String nome) {
        if ("Kael".equalsIgnoreCase(nome)) return kael(nome);
        if ("Lyra".equalsIgnoreCase(nome)) return lyra(nome);
        if ("Elyra".equalsIgnoreCase(nome)) return elyra(nome);
        return normal(nome);
    }
    public static String raridade(String raridade, String texto) {
        if (raridade == null) return texto;
        if ("Comum".equalsIgnoreCase(raridade)) return VERDE + texto + RESET;
        if ("Incomum".equalsIgnoreCase(raridade)) return CIANO + texto + RESET;
        if ("Raro".equalsIgnoreCase(raridade)) return ROXO + texto + RESET;
        if ("Epico".equalsIgnoreCase(raridade) || "Épico".equalsIgnoreCase(raridade)) return AMARELO + texto + RESET;
        if ("Lendario".equalsIgnoreCase(raridade) || "Lendário".equalsIgnoreCase(raridade)) return VERMELHO + texto + RESET;
        return texto;
    }
}
