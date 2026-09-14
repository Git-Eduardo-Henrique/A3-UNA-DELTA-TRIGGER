import java.util.Scanner;

public class Batalha {

    private Scanner scanner = new Scanner(System.in);

    public void iniciar(Personagem personagem, Inimigo inimigo) {
        System.out.println("\n=================================");
        System.out.println("           BATALHA");
        System.out.println("=================================");

        while (personagem.estaVivo() && inimigo.estaVivo()) {
            System.out.println("\n" + personagem.getNome()
                    + " - Vida: " + personagem.getVida()
                    + " | Mana: " + personagem.getMana());

            System.out.println(inimigo.getNome()
                    + " - Vida: " + inimigo.getVida()
                    + "/" + inimigo.getVidaMaxima());

            System.out.println("\nEscolha uma ação:");
            System.out.println("1 - Atacar");
            System.out.println("2 - Defender");
            System.out.println("3 - Especial");
            System.out.println("4 - Fugir");

            int opcao = lerOpcao();

            switch (opcao) {
                case 1:
                    personagem.atacar(inimigo);
                    break;

                case 2:
                    personagem.defender();
                    break;

                case 3:
                    usarEspecial(personagem, inimigo);
                    break;

                case 4:
                    System.out.println("Você fugiu da batalha.");
                    return;

                default:
                    System.out.println("Opção inválida.");
                    continue;
            }

            if (inimigo.estaVivo()) {
                inimigo.atacar(personagem);
            }
        }

        if (personagem.estaVivo()) {
            System.out.println("\nVocê venceu a batalha!");
        } else {
            System.out.println("\nVocê foi derrotado.");
        }
    }

    private void usarEspecial(Personagem personagem, Inimigo inimigo) {
        if (personagem instanceof Guerreiro) {
            Guerreiro guerreiro = (Guerreiro) personagem;
            guerreiro.golpeForte(inimigo);

        } else if (personagem instanceof Suporte) {
            Suporte suporte = (Suporte) personagem;
            suporte.curar(personagem);

        } else {
            System.out.println("Esse personagem não possui especial.");
        }
    }

    private int lerOpcao() {
        while (!scanner.hasNextInt()) {
            System.out.println("Digite apenas um número.");
            scanner.next();
        }

        return scanner.nextInt();
    }
}
