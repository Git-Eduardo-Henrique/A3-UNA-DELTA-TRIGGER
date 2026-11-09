// ============================================================
// ARQUIVO: MenuView.java
// Cuida apenas da parte visual dos menus iniciais: desenha o
// titulo do jogo, o menu de Novo Jogo/Carregar/Sair e a tela
// de escolha do lider do grupo.
// ============================================================

package view;

import model.*;

public final class MenuView {

    // Construtor privado: esta classe so tem metodos estaticos.
    private MenuView() {}

    // Mostra o titulo do jogo na tela.
    public static void titulo() {
        System.out.println(Cores.CIANO
                + "========================================\n"
                + "             DELTA TRIGGER\n"
                + "                AETHORIA\n"
                + "========================================"
                + Cores.RESET);
    }

    // Mostra o menu inicial e devolve a opcao escolhida.
    // O parametro "save" indica se existe um jogo salvo.
    public static int menuInicial(boolean save) {
        titulo();
        System.out.println("\n1 - Novo Jogo");
        System.out.println("2 - Carregar Jogo" + (save ? "" : " (nenhum save encontrado)"));
        System.out.println("3 - Sair");
        return Entrada.lerInt("Escolha: ");
    }

    // Mostra a tela para escolher o lider do grupo.
    // Retorna 1 (Kael), 2 (Lyra) ou 0 (voltar).
    public static int escolherLider() {
        while (true) {
            System.out.println("\n========================================\n"
                    + "           ESCOLHA O LIDER\n"
                    + "========================================");
            System.out.println(Cores.kael("1 - Kael | Guerreiro | linha de frente"));
            System.out.println(Cores.lyra("2 - Lyra | Suporte | cura e apoio"));
            System.out.println("0 - Voltar");

            int op = Entrada.lerInt("Escolha: ");
            if (op == 1 || op == 2) return op;
            if (op == 0) return 0;
            System.out.println(Cores.aviso("Opcao invalida. Escolha 1, 2 ou 0."));
        }
    }
}
