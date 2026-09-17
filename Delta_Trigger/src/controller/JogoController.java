package controller;

import model.*;
import util.*;
import view.*;

public class JogoController {
    private Grupo grupo;
    private String localAtual="INICIO";
    private int waveFloresta=0;
    private boolean elyraEntrou=false;
    private boolean elyraRecusada=false;
    private final Caverna florestaDosLobos=CenarioService.florestaDosLobos();
    private final Caverna cavernaDosSlimes=CenarioService.cavernaDosSlimes();

    public void iniciarNovoJogo(){
        DadosService.carregarDados();
        if(!introducao())return;
        int escolha=MenuView.escolherLider(); if(escolha==0)return;
        grupo=new Grupo(180,escolha==1?"Kael":"Lyra");
        Personagem kael=new Guerreiro(), lyra=new Suporte();
        grupo.adicionar(kael); grupo.adicionar(lyra);
        Equipamento espada=new Equipamento("Espada Inicial","Arma","Comum","Guerreiro",0,1,0,0,0,0,0);
        Equipamento armaduraK=new Equipamento("Armadura Inicial","Armadura","Comum","Guerreiro",0,0,2,0,0,0,0);
        Equipamento cajado=new Equipamento("Cajado Inicial","Arma","Comum","Suporte",0,0,0,1,0,0,0);
        Equipamento vestimenta=new Equipamento("Vestimenta Inicial","Armadura","Comum","Suporte",0,0,1,1,1,0,0);
        kael.equiparDireto(espada); kael.equiparDireto(armaduraK); lyra.equiparDireto(cajado); lyra.equiparDireto(vestimenta);
        grupo.getInventario().adicionarItem(new Item("Pocao de Vida","Pocao",15,30,0,2));
        grupo.getInventario().adicionarItem(new Item("Pocao de Mana","Pocao",18,0,20,2));
        localAtual="FLORESTA";
        cenaFloresta(); iniciarFloresta();
    }

    public void iniciarCarregado(SaveService.DadosSave save){
        grupo=save.grupo; localAtual=save.local; waveFloresta=save.waveFloresta; elyraEntrou=save.elyraEntrou; elyraRecusada=save.elyraRecusada;
        grupo.definirLider(save.grupo.getLider());
        if("ELDORIA".equals(localAtual))abrirEldoria();
        else if("FLORESTA".equals(localAtual))iniciarFloresta();
        else System.out.println(Cores.aviso("Este ponto do save ainda nao possui retorno nesta demo."));
    }

    private boolean introducao(){
        MenuView.titulo();
        String[] ch={"INTRO_1","INTRO_2","INTRO_3","INTRO_4","INTRO_5","INTRO_6","INTRO_7"};
        for(String c:ch){System.out.println("\n"+TextoService.obter(c,"Aethoria enfrenta uma nova ameaca."));Entrada.enter();}
        return true;
    }

    private void cenaFloresta(){
        System.out.println("\n========================================\n          FLORESTA DOS LOBOS\n========================================");
        System.out.println("Lider escolhido: "+Cores.nomePersonagem(grupo.getLider()));
        System.out.println(TextoService.obter("FLORESTA_CENA","Kael e Lyra entram na floresta."));
        Entrada.enter();
    }

    private void iniciarFloresta(){
        for(int wave=waveFloresta+1;wave<=5;wave++){
            waveFloresta=wave; mostrarWave(wave);
            Inimigo[] inimigos=criarWave(florestaDosLobos,wave);
            Batalha.Resultado r=Batalha.iniciar(grupo,inimigos,false);
            if(r==Batalha.Resultado.DERROTA){gameOver();return;}
            if(r!=Batalha.Resultado.VITORIA)return;
            if(wave<5){System.out.println(Cores.normal("A floresta fica em silencio por alguns instantes."));Entrada.enter();}
        }
        finalizarFenrok();
    }

    private Inimigo[] criarWave(Caverna caverna,int numero){
        for(Wave wave:caverna.getWaves()) if(wave.getNumero()==numero){
            Inimigo[] lista=new Inimigo[wave.getInimigos().size()];
            for(int i=0;i<lista.length;i++){lista[i]=DadosService.criarInimigo(wave.getInimigos().get(i)); if(lista[i]==null)throw new IllegalStateException("Inimigo nao encontrado: "+wave.getInimigos().get(i));}
            return lista;
        }
        return new Inimigo[0];
    }

    private void mostrarWave(int w){
        System.out.println("\n========================================");
        System.out.println(w<5?"          WAVE "+w+"/5":Cores.inimigo("              CHEFE"));
        System.out.println("========================================");
        for(Wave wave:florestaDosLobos.getWaves())if(wave.getNumero()==w){System.out.println(w==5?Cores.inimigo(wave.getMensagem()):wave.getMensagem());break;}
        Entrada.enter();
    }

    private void finalizarFenrok(){
        System.out.println("\n========================================\n"+Cores.sucesso("       FENROK FOI DERROTADO!")+"\n========================================");
        System.out.println(TextoService.obter("FENROK_POS","Fenrok foi derrotado."));
        System.out.println("\n"+Cores.amarelo("RECOMPENSAS DE FENROK"));
        Equipamento m=DadosService.criarEquipamento("MANTO_PELAGEM_ALFA");
        Equipamento b=DadosService.criarEquipamento("BOTAS_PELAGEM_ALFA");
        if(m==null)m=new Equipamento("Manto da Pelagem Alfa","Armadura","Epico","Suporte",0,0,3,0,1,0,1);
        if(b==null)b=new Equipamento("Botas da Pelagem Alfa","Botas","Raro","Todos",0,0,0,0,0,2,1);
        grupo.getInventario().adicionarEquipamento(m);grupo.getInventario().adicionarEquipamento(b);
        System.out.println(Cores.sucesso("+ "+m.getNome()));System.out.println(Cores.sucesso("+ "+b.getNome()));
        for(Personagem p:grupo.getPersonagens()){int hpAntes=p.getVidaMaxima(),manaAntes=p.getManaMaxima();p.subirNivel();System.out.println(Cores.sucesso(p.getNome()+" subiu para o nivel "+p.getNivel()+"! HP maximo "+hpAntes+" -> "+p.getVidaMaxima()+" | Mana maxima "+manaAntes+" -> "+p.getManaMaxima()));}
        distribuirPontos();
        localAtual="ELDORIA";System.out.println(Cores.sucesso("\nO grupo chegou a Eldoria."));Entrada.enter();abrirEldoria();
    }

    private void distribuirPontos(){
        for(Personagem p:grupo.getPersonagens())while(p.getPontosAtributo()>0){
            System.out.println("\n=== PONTOS DE ATRIBUTO: "+Cores.nomePersonagem(p.getNome())+" ===");
            System.out.println("Pontos disponiveis: "+p.getPontosAtributo());
            System.out.println("1 - Forca: "+p.getForca()+"\n2 - Defesa: "+p.getDefesa()+"\n3 - Inteligencia: "+p.getInteligencia()+"\n4 - Resistencia: "+p.getResistencia()+"\n5 - Velocidade: "+p.getVelocidade()+"\n6 - Sorte: "+p.getSorte()+"\n0 - Guardar pontos para depois");
            int op=Entrada.lerInt("Escolha: "); if(op==0)break;
            int antes=valorAtributo(p,op);
            if(p.distribuirPonto(op))System.out.println(Cores.sucesso(nomeAtributo(op)+": "+antes+" -> "+valorAtributo(p,op)+". Restam "+p.getPontosAtributo()+" pontos."));
            else System.out.println(Cores.aviso("Opcao invalida."));
        }
    }
    private int valorAtributo(Personagem p,int op){switch(op){case 1:return p.getForca();case 2:return p.getDefesa();case 3:return p.getInteligencia();case 4:return p.getResistencia();case 5:return p.getVelocidade();case 6:return p.getSorte();default:return -1;}}
    private String nomeAtributo(int op){switch(op){case 1:return "Forca";case 2:return "Defesa";case 3:return "Inteligencia";case 4:return "Resistencia";case 5:return "Velocidade";case 6:return "Sorte";default:return "Atributo";}}

    private void abrirEldoria(){
        localAtual="ELDORIA";
        System.out.println("\n========================================\n               ELDORIA\n========================================");
        System.out.println(TextoService.obter("ELDORIA_CHEGADA","As muralhas de Eldoria surgem diante do grupo."));
        Entrada.enter();
        CidadeView.Resultado r=CidadeView.abrir(grupo,"Eldoria",elyraEntrou,elyraRecusada,waveFloresta);
        elyraEntrou=r.elyraEntrou;elyraRecusada=r.elyraRecusada;
        if(r.irCaverna)entradaCaverna1();
    }

    private void entradaCaverna1(){
        localAtual="CAVERNA1";
        System.out.println("\n========================================\n          CAVERNA DOS SLIMES\n========================================");
        System.out.println("O grupo deixa Eldoria e segue em direcao a caverna.");
        System.out.println("A entrada esta escura. Um ruido estranho vem de dentro.");Entrada.enter();
        System.out.println(Cores.magia(TextoService.obter("SLIME_VARYN_INICIO","Uma figura de manto escuro observa os cristais.")));Entrada.enter();
        for(int wave=1;wave<=5;wave++){
            for(Wave w:cavernaDosSlimes.getWaves())if(w.getNumero()==wave){System.out.println("\n"+w.getMensagem());break;}
            Inimigo[] inimigos=criarWave(cavernaDosSlimes,wave);
            Batalha.Resultado r=Batalha.iniciar(grupo,inimigos,true);
            if(r==Batalha.Resultado.DERROTA){gameOver();return;}
            if(r!=Batalha.Resultado.VITORIA)return;
            if(wave<5)Entrada.enter();
        }
        System.out.println("\n"+Cores.magia(TextoService.obter("SLIME_VARYN_FIM","O mago fala sobre a energia dos cristais.")));Entrada.enter();
        System.out.println(Cores.sucesso("Tutorial concluido. A proxima cidade sera Valdrin.") );
        Entrada.enter();
    }

    private void gameOver(){
        while(true){
            System.out.println("\n========================================\n"+Cores.vermelho("             GAME OVER")+"\n========================================");
            System.out.println("Todos os personagens foram derrotados.");
            System.out.println("\n1 - Carregar jogo salvo\n2 - Voltar ao menu inicial\n3 - Sair");
            int op=Entrada.lerInt("Escolha: ");
            if(op==1){if(!SaveService.existeSave()){System.out.println(Cores.aviso("Nenhum jogo salvo foi encontrado."));continue;}SaveService.DadosSave s=SaveService.carregar();if(s!=null){grupo=s.grupo;localAtual=s.local;waveFloresta=s.waveFloresta;elyraEntrou=s.elyraEntrou;elyraRecusada=s.elyraRecusada;if("ELDORIA".equals(localAtual))abrirEldoria();else if("FLORESTA".equals(localAtual))iniciarFloresta();}return;}
            if(op==2)return;if(op==3){System.out.println(Cores.ciano("Ate a proxima aventura em Aethoria!"));System.exit(0);}System.out.println(Cores.aviso("Opcao invalida. Escolha 1, 2 ou 3."));
        }
    }
}
