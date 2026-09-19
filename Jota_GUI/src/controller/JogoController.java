package controller;

import model.Batalha;
import model.Cores;
import model.Entrada;
import model.Elyra;
import model.Guerreiro;
import model.Inimigo;
import model.LoboFactory;
import model.Personagem;
import model.Suporte;
import util.SaveService;
import util.TextoService;
import view.CidadeView;
import view.MenuView;

public class JogoController {
    private Personagem[] grupo;
    private String lider;
    private String localAtual = "INICIO";
    private int waveFloresta = 0;
    private boolean elyraEntrou = false;

    public void iniciarNovoJogo() {
        if (!mostrarIntroducao()) return;
        int escolha = MenuView.escolherLider();
        if (escolha == 0) return;
        criarGrupo(escolha);
        cenaInicioFloresta();
        iniciarFloresta();
    }

    public void iniciarCarregado(SaveService.DadosSave save) {
        grupo = save.grupo;
        lider = save.lider;
        localAtual = save.local;
        waveFloresta = save.waveFloresta;
        elyraEntrou = save.elyraEntrou;

        if (localAtual.equals("ELDORIA")) {
            abrirEldoria();
        } else if (localAtual.equals("CAVERNA1")) {
            entradaCaverna1();
        } else {
            System.out.println(Cores.aviso("O save foi carregado em um ponto nao suportado nesta demo."));
        }
    }

    private boolean mostrarIntroducao() {
        MenuView.titulo();
        System.out.println(Cores.normal("\n" + TextoService.obter("INTRO_1",
                "Ha muitos seculos, o reino de Aethoria enfrentou uma grande ameaca.")));
        Entrada.enter();
        System.out.println(Cores.inimigo(TextoService.obter("INTRO_2",
                "Malakar, o Rei Demônio, liderou criaturas contra o reino.")));
        Entrada.enter();
        System.out.println(Cores.normal(TextoService.obter("INTRO_3",
                "Tres grandes forças foram reunidas: Forca, Magia e Espirito.")));
        Entrada.enter();
        System.out.println(Cores.magia(TextoService.obter("INTRO_4",
                "Essas forças criaram o Delta, um selo capaz de prender Malakar na Torre de Noxar.")));
        Entrada.enter();
        System.out.println(Cores.normal(TextoService.obter("INTRO_5",
                "Seculos de paz se passaram. Entao, criaturas voltaram a surgir e uma energia estranha comecou a se espalhar.")));
        Entrada.enter();
        System.out.println(Cores.magia(TextoService.obter("INTRO_6",
                "O poder do Delta comecou a despertar. Esse despertar ficou conhecido como Delta Trigger.")));
        Entrada.enter();
        System.out.println(Cores.normal(TextoService.obter("INTRO_7",
                "Em uma pequena regiao de Aethoria, os irmaos Kael e Lyra decidem investigar os acontecimentos.")));
        Entrada.enter();
        return true;
    }

    private void criarGrupo(int escolha) {
        grupo = new Personagem[] { new Guerreiro(), new Suporte() };
        lider = escolha == 1 ? "Kael" : "Lyra";
        localAtual = "FLORESTA";
    }

    private void cenaInicioFloresta() {
        System.out.println("\n========================================");
        System.out.println("         FLORESTA DOS LOBOS");
        System.out.println("========================================");
        System.out.println("Kael e Lyra entram na floresta para investigar os acontecimentos.");
        System.out.println("O caminho parece tranquilo, mas algo se move entre as arvores...");
        Entrada.enter();
    }

    private void iniciarFloresta() {
        for (int wave = waveFloresta + 1; wave <= 5; wave++) {
            waveFloresta = wave;
            Inimigo[] inimigos = criarWave(wave);
            mostrarWave(wave);
            Batalha.Resultado resultado = Batalha.iniciar(grupo, inimigos, false);
            if (resultado == Batalha.Resultado.DERROTA) {
                telaGameOver();
                return;
            }
            if (resultado != Batalha.Resultado.VITORIA) return;
            if (wave < 5) {
                System.out.println("\n" + Cores.normal("A floresta fica em silencio por alguns instantes."));
                Entrada.enter();
            }
        }
        finalizarFenrok();
    }

    private Inimigo[] criarWave(int wave) {
        if (wave == 1 || wave == 2) {
            return new Inimigo[] { LoboFactory.criarLobinho(), LoboFactory.criarLobinho(), LoboFactory.criarLobinho() };
        }
        if (wave == 3) {
            return new Inimigo[] { LoboFactory.criarLobo(), LoboFactory.criarLobo() };
        }
        if (wave == 4) {
            return new Inimigo[] { LoboFactory.criarLobo(), LoboFactory.criarLobo(), LoboFactory.criarLobo() };
        }
        return new Inimigo[] { new model.Fenrok() };
    }

    private void mostrarWave(int wave) {
        System.out.println("\n========================================");
        if (wave < 5) System.out.println("          WAVE " + wave);
        else System.out.println(Cores.inimigo("              CHEFE"));
        System.out.println("========================================");
        if (wave == 1 || wave == 2) System.out.println("Tres Lobinhos surgiram!");
        else if (wave == 3) System.out.println("Dois Lobos surgiram!");
        else if (wave == 4) System.out.println("Tres Lobos surgiram!");
        else System.out.println(Cores.inimigo("Fenrok, o Lobo Alfa, apareceu!"));
        Entrada.enter();
    }

    private void finalizarFenrok() {
        System.out.println("\n========================================");
        System.out.println(Cores.sucesso("       FENROK FOI DERROTADO!"));
        System.out.println("========================================");
        System.out.println("Fenrok era o protetor da alcateia.");
        System.out.println("A energia estranha do Delta alterou seu comportamento.");
        System.out.println("A batalha deixou uma pergunta: o que esta corrompendo a floresta?");
        Entrada.enter();

        for (Personagem p : grupo) p.subirNivel();
        distribuirPontos();

        System.out.println(Cores.sucesso("Apos a batalha, o grupo segue para Eldoria."));
        localAtual = "ELDORIA";
        Entrada.enter();
        abrirEldoria();
    }

    private void distribuirPontos() {
        for (Personagem p : grupo) {
            while (p.getPontosAtributo() > 0) {
                System.out.println("\n=== PONTOS DE ATRIBUTO: " + p.getNome() + " ===");
                System.out.println("Pontos disponiveis: " + p.getPontosAtributo());
                System.out.println("1 - Forca");
                System.out.println("2 - Defesa");
                System.out.println("3 - Inteligencia");
                System.out.println("4 - Resistencia");
                System.out.println("5 - Velocidade");
                System.out.println("6 - Sorte");
                System.out.println("0 - Guardar pontos para depois");
                int op = Entrada.lerInt("Escolha: ");
                if (op == 0) break;
                if (!p.distribuirPonto(op)) {
                    System.out.println(Cores.aviso("Opcao invalida."));
                } else {
                    System.out.println(Cores.sucesso("Ponto distribuido. Restam " + p.getPontosAtributo() + "."));
                }
            }
        }
    }

    private void abrirEldoria() {
        localAtual = "ELDORIA";
        int resultado = CidadeView.abrir(grupo, lider, elyraEntrou);
        if (resultado == 1 && !elyraEntrou) {
            encontrarElyra();
            elyraEntrou = true;
            entradaCaverna1();
        }
    }

    private void encontrarElyra() {
        System.out.println("\n========================================");
        System.out.println("             TAVERNA");
        System.out.println("========================================");
        System.out.println("Kael e Lyra percebem uma elfa de cabelos vermelhos observando a movimentacao.");
        Entrada.enter();
        System.out.println("Ela se apresenta como Elyra, uma Arqueira Arcana.");
        System.out.println("Elyra tambem investiga a energia estranha que surgiu na regiao.");
        Entrada.enter();
        System.out.println("Depois de conversarem, Elyra decide acompanhar os dois na jornada.");
        System.out.println(Cores.elyra("\n*** ELYRA ENTROU NO GRUPO! ***"));
        System.out.println("Nivel: 2 | Classe: Arqueira Arcana");

        Elyra novaElyra = new Elyra();
        grupo = new Personagem[] { grupo[0], grupo[1], novaElyra };
        System.out.println("Elyra entra no nivel 2 e ja possui seus equipamentos iniciais equipados.");
        Entrada.enter();
    }

    private void entradaCaverna1() {
        localAtual = "CAVERNA1";
        System.out.println("\n========================================");
        System.out.println("          CAVERNA DOS SLIMES");
        System.out.println("========================================");
        System.out.println("Kael, Lyra e Elyra deixam Eldoria e seguem em direcao a caverna.");
        System.out.println("A entrada esta escura. Um ruido estranho vem de dentro.");
        System.out.println(Cores.amarelo("A Caverna 1 sera a proxima area desenvolvida sobre esta base."));
        Entrada.enter();
    }
    private void telaGameOver() {
        while (true) {
            System.out.println("\n========================================");
            System.out.println(Cores.vermelho("             GAME OVER"));
            System.out.println("========================================");
            System.out.println("Todos os personagens foram derrotados.");
            System.out.println("\n1 - Carregar jogo salvo");
            System.out.println("2 - Voltar ao menu inicial");
            System.out.println("3 - Sair");
            int op = Entrada.lerInt("Escolha: ");
            if (op == 1) {
                if (!SaveService.existeSave()) {
                    System.out.println(Cores.aviso("Nenhum jogo salvo foi encontrado."));
                    continue;
                }
                SaveService.DadosSave save = SaveService.carregar();
                if (save != null) {
                    iniciarCarregado(save);
                    return;
                }
            } else if (op == 2) {
                return;
            } else if (op == 3) {
                System.exit(0);
            } else {
                System.out.println(Cores.aviso("Opcao invalida. Escolha 1, 2 ou 3."));
            }
        }
    }

}
