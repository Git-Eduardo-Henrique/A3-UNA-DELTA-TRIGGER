// ============================================================
// ARQUIVO: Main.java
// Este e o ponto de entrada do jogo "Delta Trigger".
// E a primeira classe que o Java executa. Ela mostra o menu
// inicial e decide se o jogador vai comecar um jogo novo,
// carregar um jogo salvo ou sair do programa.
// ============================================================

// Importa as classes que serao usadas neste arquivo.
import controller.JogoController;
import model.Cores;
import model.Entrada;
import util.DadosService;
import util.SaveService;
import view.MenuView;

public class Main {

    // Metodo principal: a execucao do programa comeca por aqui.
    public static void main(String[] args) {

        // Carrega os dados dos monstros do arquivo data/monstros.txt.
        DadosService.carregarMonstros();

        // Loop infinito: o menu fica aparecendo ate o jogador escolher sair.
        while (true) {

            // Verifica se existe um jogo salvo no computador.
            boolean save = SaveService.existeSave();

            // Mostra o menu inicial e recebe a opcao digitada pelo jogador.
            int op = MenuView.menuInicial(save);

            if (op == 1) {
                // Opcao 1: iniciar um jogo novo.
                new JogoController().iniciarNovoJogo();

            } else if (op == 2) {
                // Opcao 2: carregar um jogo que ja foi salvo.
                if (!save) {
                    // Se nao existe save, avisa o jogador.
                    System.out.println(Cores.aviso("Nenhum jogo salvo foi encontrado."));
                } else {
                    // Le os dados do save e retoma o jogo.
                    SaveService.DadosSave s = SaveService.carregar();
                    if (s != null) {
                        new JogoController().iniciarCarregado(s);
                    }
                }

            } else if (op == 3) {
                // Opcao 3: encerrar o programa.
                System.out.println(Cores.ciano("Ate a proxima aventura em Aethoria!"));
                break;

            } else {
                // Qualquer outro numero e tratado como opcao invalida.
                System.out.println(Cores.aviso("Opcao invalida. Escolha 1, 2 ou 3."));
            }
        }
    }
}
