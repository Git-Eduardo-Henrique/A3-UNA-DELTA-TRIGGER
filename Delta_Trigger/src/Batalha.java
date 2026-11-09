// ============================================================
// ARQUIVO: Batalha.java (versao antiga)
// Controla uma batalha simples entre um personagem e um inimigo.
// Em cada rodada o jogador escolhe uma acao (atacar, defender,
// usar especial ou fugir) e depois o inimigo contra-ataca.
// ============================================================

import java.util.Scanner;

public class Batalha {

    // Objeto usado para ler o que o jogador digita no teclado.
    private Scanner scanner = new Scanner(System.in);

    // Inicia e controla a batalha ate alguem vencer ou o jogador fugir.
    public void iniciar(Personagem personagem, Inimigo inimigo) {
        System.out.println("\n=================================");
        System.out.println("           BATALHA");
        System.out.println("=================================");

        // Continua enquanto os dois estiverem vivos.
        while (personagem.estaVivo() && inimigo.estaVivo()) {
            // Mostra a situacao atual da batalha.
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

            // Le a acao escolhida pelo jogador.
            int opcao = lerOpcao();

            // Executa a acao escolhida.
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
                    return;   // Encerra a batalha na hora.

                default:
                    System.out.println("Opção inválida.");
                    continue; // Volta ao inicio da rodada.
            }

            // Se o inimigo sobreviveu, ele contra-ataca.
            if (inimigo.estaVivo()) {
                inimigo.atacar(personagem);
            }
        }

        // Mostra o resultado final da batalha.
        if (personagem.estaVivo()) {
            System.out.println("\nVocê venceu a batalha!");
        } else {
            System.out.println("\nVocê foi derrotado.");
        }
    }

    // Usa a habilidade especial de acordo com a classe do personagem.
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

    // Le um numero inteiro, repetindo ate o jogador digitar algo valido.
    private int lerOpcao() {
        while (!scanner.hasNextInt()) {
            System.out.println("Digite apenas um número.");
            scanner.next();
        }

        return scanner.nextInt();
    }
}
