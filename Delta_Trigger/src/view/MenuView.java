package view;

import model.Cores;
import model.Entrada;

public class MenuView {
    public static void titulo() {
        System.out.println(Cores.CIANO + "========================================" + Cores.RESET);
        System.out.println(Cores.CIANO + "             DELTA TRIGGER" + Cores.RESET);
        System.out.println(Cores.CIANO + "                AETHORIA" + Cores.RESET);
        System.out.println(Cores.CIANO + "========================================" + Cores.RESET);
    }

    public static int menuInicial(boolean temSave) {
        titulo();
        System.out.println("\n1 - Novo Jogo");
        System.out.println("2 - Carregar Jogo" + (temSave ? "" : " (nenhum save encontrado)"));
        System.out.println("3 - Sair");
        return Entrada.lerInt("Escolha: ");
    }

    public static int escolherLider() {
        while (true) {
            System.out.println("\n========================================");
            System.out.println("           ESCOLHA O LIDER");
            System.out.println("========================================");
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
