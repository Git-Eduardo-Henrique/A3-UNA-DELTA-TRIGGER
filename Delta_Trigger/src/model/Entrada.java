// ============================================================
// ARQUIVO: Entrada.java
// Centraliza a leitura do teclado. Assim, todo o jogo usa o mesmo
// "leitor" e trata erros de digitacao de forma igual.
// ============================================================

package model;

import java.util.Scanner;

public final class Entrada {
    // Um unico leitor de teclado compartilhado por todo o jogo.
    private static final Scanner SCANNER = new Scanner(System.in);

    // Construtor privado: esta classe nao precisa ser criada como objeto.
    private Entrada() {}

    // Mostra uma mensagem e le um numero inteiro do jogador.
    // Se o jogador digitar algo que nao e numero, pede de novo.
    public static int lerInt(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String texto = SCANNER.nextLine().trim();
            try { return Integer.parseInt(texto); }
            catch (NumberFormatException e) { System.out.println(Cores.aviso("Digite apenas um numero inteiro.")); }
        }
    }
    // Le um numero inteiro e so aceita valores maiores que zero.
    public static int lerInteiroPositivo(String mensagem) {
        while (true) { int v = lerInt(mensagem); if (v > 0) return v; System.out.println(Cores.aviso("Digite um numero maior que zero.")); }
    }

    // Pausa o jogo ate o jogador apertar ENTER.
    public static void enter() { System.out.println(Cores.CINZA + "\n[ENTER] Continuar" + Cores.RESET); SCANNER.nextLine(); }
}
