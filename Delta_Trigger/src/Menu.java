import java.util.Scanner;

public class Menu {

    private Scanner scanner = new Scanner(System.in);

    public Personagem escolherPersonagem() {
        System.out.println("Escolha seu personagem:");
        System.out.println("1 - Kael (Guerreiro)");
        System.out.println("2 - Lyra (Suporte)");

        int opcao = lerOpcao();

        while (opcao != 1 && opcao != 2) {
            System.out.println("Opção inválida. Escolha 1 ou 2.");
            opcao = lerOpcao();
        }

        if (opcao == 1) {
            return new Guerreiro("Kael");
        }

        return new Suporte("Lyra");
    }

    private int lerOpcao() {
        while (!scanner.hasNextInt()) {
            System.out.println("Digite apenas um número.");
            scanner.next();
        }

        return scanner.nextInt();
    }
}
