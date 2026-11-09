// ============================================================
// ARQUIVO: Menu.java
// Menu simples (versao antiga) que permite escolher o personagem
// inicial do jogo: Kael (Guerreiro) ou Lyra (Suporte).
// ============================================================

import java.util.Scanner;

public class Menu {

    // Objeto usado para ler o que o jogador digita no teclado.
    private Scanner scanner = new Scanner(System.in);

    // Mostra as opcoes e devolve o personagem escolhido.
    public Personagem escolherPersonagem() {
        System.out.println("Escolha seu personagem:");
        System.out.println("1 - Kael (Guerreiro)");
        System.out.println("2 - Lyra (Suporte)");

        // Le a opcao digitada pelo jogador.
        int opcao = lerOpcao();

        // Enquanto a opcao nao for 1 ou 2, pede novamente.
        while (opcao != 1 && opcao != 2) {
            System.out.println("Opção inválida. Escolha 1 ou 2.");
            opcao = lerOpcao();
        }

        // Se escolheu 1, cria o Guerreiro Kael.
        if (opcao == 1) {
            return new Guerreiro("Kael");
        }

        // Caso contrario, cria a Suporte Lyra.
        return new Suporte("Lyra");
    }

    // Le um numero inteiro, repetindo ate o jogador digitar algo valido.
    private int lerOpcao() {
        // Enquanto o que foi digitado nao for um numero, avisa e pede de novo.
        while (!scanner.hasNextInt()) {
            System.out.println("Digite apenas um número.");
            scanner.next();
        }

        return scanner.nextInt();
    }
}
