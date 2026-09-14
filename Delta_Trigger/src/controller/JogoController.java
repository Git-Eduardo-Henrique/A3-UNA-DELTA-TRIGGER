package controller;
import model.*;
import view.*;

public class JogoController {
    private Personagem[] grupo;
    private String lider;

    public void iniciar() {
        MenuView.titulo();
        montarGrupo();
        System.out.println("\n" + lider + " sera o lider.");
        System.out.println("Kael e Lyra seguem juntos pela Floresta dos Lobos.");

        floresta();
        CidadeView.abrir(grupo);

        System.out.println("\n========================================");
        System.out.println("          FIM DO TUTORIAL DA DEMO");
        System.out.println("========================================");
        System.out.println("A proxima parte pode adicionar Elyra, Varyn, cavernas e quests.");
    }

    private void montarGrupo() {
        grupo = new Personagem[] { new Guerreiro(), new Suporte() };
        lider = MenuView.escolherLider() == 2 ? "Lyra" : "Kael";

        Equipamento espada = new Equipamento("Espada de Madeira","Arma",0,1,0,0,0,0,0);
        Equipamento manto = new Equipamento("Manto Simples","Armadura",0,0,0,1,0,0,0);
        grupo[0].getInventario().adicionarEquipamento(espada); grupo[0].equipar(espada);
        grupo[1].getInventario().adicionarEquipamento(manto); grupo[1].equipar(manto);
        grupo[0].getInventario().adicionarItem(new Item("Pocao de Vida","Pocao",15,30,0));
        grupo[1].getInventario().adicionarItem(new Item("Pocao de Mana","Pocao",18,0,20));
    }

    private void floresta() {
        System.out.println("\n========== FLORESTA DOS LOBOS ==========");
        System.out.println("A floresta serve como tutorial de batalha.");

        for (int onda = 1; onda <= 5; onda++) {
            System.out.println("\n---------- ONDA " + onda + "/5 ----------");
            Inimigo inimigo;

            if (onda <= 2) inimigo = LoboFactory.criarLobinho(onda);
            else if (onda <= 4) inimigo = LoboFactory.criarLobo(onda);
            else {
                System.out.println("Um lobo enorme aparece...");
                inimigo = new Fenrok();
            }

            if (!Batalha.iniciar(grupo, inimigo, false)) {
                System.out.println("Fim da demo.");
                return;
            }

            if (onda < 5) {
                grupo[0].getInventario().adicionarItem(new Item("Presa de Lobo","Material",8,0,0));
                if (Math.random() < 0.5)
                    grupo[1].getInventario().adicionarItem(new Item("Pocao de Vida","Pocao",15,30,0));
            }
        }

        recompensaFenrok();
        nivelarGrupo();
    }

    private void recompensaFenrok() {
        System.out.println("\nFenrok era o protetor da alcateia.");
        System.out.println("A energia estranha da Delta o corrompeu.");
        System.out.println("Ele nao parecia ser realmente mal.");

        Equipamento manto = new Equipamento("Manto da Pelagem Alfa","Armadura",0,0,1,0,2,0,0);
        Equipamento botas = new Equipamento("Botas da Pelagem Alfa","Botas",0,0,0,0,0,2,1);

        grupo[1].getInventario().adicionarEquipamento(manto);
        grupo[1].equipar(manto);
        grupo[1].getInventario().adicionarEquipamento(botas);

        System.out.println("\nDROP GARANTIDO:");
        System.out.println("Lyra recebeu Manto da Pelagem Alfa: +2 Resistencia, +1 Defesa.");
        System.out.println("Botas da Pelagem Alfa foram guardadas para a futura Elyra: +2 Velocidade, +1 Sorte.");
    }

    private void nivelarGrupo() {
        for (Personagem p : grupo) p.subirNivel();

        for (Personagem p : grupo) {
            while (p.getPontosAtributo() > 0) {
                p.status();
                System.out.println("1 Forca | 2 Defesa | 3 Inteligencia | 4 Resistencia | 5 Velocidade | 6 Sorte");
                p.distribuirPonto(Entrada.lerInt("Distribua um ponto para " + p.getNome() + ": "));
            }
        }
    }
}
