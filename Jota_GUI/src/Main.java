import controller.JogoController;
import model.Cores;
import util.SaveService;
import view.MenuView;

public class Main {
    public static void main(String[] args) {
        while (true) {
            boolean temSave = SaveService.existeSave();
            int op = MenuView.menuInicial(temSave);
            if (op == 1) {
                new JogoController().iniciarNovoJogo();
            } else if (op == 2) {
                if (!temSave) {
                    System.out.println(Cores.aviso("Nenhum jogo salvo foi encontrado."));
                } else {
                    SaveService.DadosSave save = SaveService.carregar();
                    if (save != null) new JogoController().iniciarCarregado(save);
                }
            } else if (op == 3) {
                System.out.println(Cores.ciano("Ate a proxima aventura em Aethoria!"));
                break;
            } else {
                System.out.println(Cores.aviso("Opcao invalida. Escolha 1, 2 ou 3."));
            }
        }
    }
}
