package model;

import java.util.Scanner;

public final class Entrada {
    private static final Scanner SCANNER = new Scanner(System.in);

    private Entrada() {}

    public static int lerInt(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String texto = SCANNER.nextLine().trim();
            try {
                return Integer.parseInt(texto);
            } catch (NumberFormatException e) {
                System.out.println(Cores.aviso("Digite apenas um numero inteiro."));
            }
        }
    }

    public static int lerInteiroPositivo(String mensagem) {
        while (true) {
            int valor = lerInt(mensagem);
            if (valor > 0) return valor;
            System.out.println(Cores.aviso("Digite um numero maior que zero."));
        }
    }

    public static void enter() {
        System.out.println(Cores.CINZA + "\n[ENTER] Continuar" + Cores.RESET);
        SCANNER.nextLine();
    }
}
