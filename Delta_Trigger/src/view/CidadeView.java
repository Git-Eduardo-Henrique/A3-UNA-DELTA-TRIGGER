// ============================================================
// ARQUIVO: CidadeView.java
// Cuida da tela da cidade: pousada (descansar/salvar), loja
// (comprar/vender/equipar), taverna (missoes/rumores) e menu
// do grupo (atributos e inventario).
// ============================================================

package view;

import model.*;
import util.*;
import java.util.List;

public final class CidadeView {

    // Construtor privado: a classe so tem metodos estaticos.
    private CidadeView() {}

    // Guarda o que aconteceu na cidade e precisa ser levado de volta
    // para o controlador do jogo.
    public static class Resultado {
        public boolean irCaverna;      // O jogador aceitou ir para a caverna?
        public boolean elyraEntrou;    // A Elyra entrou no grupo?
        public boolean elyraRecusada;  // O jogador recusou a Elyra?
    }

    // Abre o menu principal da cidade e fica nele ate o jogador sair.
    public static Resultado abrir(Grupo grupo, String cidade, boolean elyraEntrou, boolean elyraRecusada, int waveFloresta) {
        Resultado resultado = new Resultado();
        resultado.elyraEntrou = elyraEntrou;
        resultado.elyraRecusada = elyraRecusada;
        while (true) {
            cabecalho(cidade);
            System.out.println("1 - Pousada");
            System.out.println("2 - Loja");
            System.out.println("3 - Taverna");
            System.out.println("4 - Grupo");
            System.out.println("0 - Sair");
            System.out.println(Cores.amarelo("Ouro do grupo: $" + grupo.getOuro()));
            int op = Entrada.lerInt("Escolha: ");
            if (op == 0) return resultado;
            if (op == 1) pousada(grupo, cidade, waveFloresta, resultado);
            else if (op == 2) loja(grupo);
            else if (op == 3) {
                if ("Eldoria".equalsIgnoreCase(cidade)) {
                    boolean[] estado = tavernaEldoria(grupo, resultado.elyraEntrou, resultado.elyraRecusada);
                    resultado.elyraEntrou = estado[0];
                    resultado.elyraRecusada = estado[1];
                    resultado.irCaverna = estado.length > 2 && estado[2];
                } else tavernaGenerica(cidade);
            } else if (op == 4) grupo(grupo);
            else System.out.println(Cores.aviso("Opcao invalida. Escolha 1, 2, 3, 4 ou 0."));
        }
    }

    // Mostra o cabecalho da cidade e explica cada opcao do menu.
    private static void cabecalho(String cidade) {
        System.out.println("\n========================================");
        System.out.println("               " + cidade.toUpperCase());
        System.out.println("========================================");
        System.out.println("Pousada: descansar e salvar o progresso.");
        System.out.println("Loja: comprar, vender e equipar equipamentos.");
        System.out.println("Taverna: missoes e rumores da regiao.");
        System.out.println("Grupo: personagens, atributos e equipamentos.");
    }

    // Pousada: permite descansar (recuperar tudo) e salvar o jogo.
    private static void pousada(Grupo g, String cidade, int wave, Resultado r) {
        while (true) {
            System.out.println("\n=== POUSADA ===");
            System.out.println("1 - Descansar ($10)");
            System.out.println("2 - Salvar jogo");
            System.out.println("0 - Voltar");
            int op = Entrada.lerInt("Escolha: ");
            if (op == 0) return;
            if (op == 1) {
                if (!g.gastarOuro(10)) { System.out.println(Cores.aviso("O grupo precisa de $10 para descansar. Ouro atual: $" + g.getOuro() + ".")); continue; }
                for (Personagem p : g.getPersonagens()) p.recuperarTudo();
                System.out.println(Cores.sucesso("O grupo descansou. HP e Mana foram restaurados."));
            } else if (op == 2) {
                boolean ok = SaveService.salvar(g, cidade.toUpperCase(), wave, r.elyraEntrou, r.elyraRecusada);
                System.out.println(ok ? Cores.sucesso("Jogo salvo com sucesso! (save.txt criptografado)") : Cores.vermelho("Nao foi possivel salvar o jogo."));
            } else System.out.println(Cores.aviso("Opcao invalida. Escolha 1, 2 ou 0."));
        }
    }

    // Loja: menu para comprar, vender ou equipar equipamentos.
    private static void loja(Grupo g) {
        while (true) {
            System.out.println("\n=== LOJA ===");
            System.out.println("Ouro do grupo: $" + g.getOuro());
            System.out.println("1 - Comprar");
            System.out.println("2 - Vender");
            System.out.println("3 - Equipar equipamento");
            System.out.println("0 - Voltar");
            int op = Entrada.lerInt("Escolha: ");
            if (op == 0) return;
            if (op == 1) comprar(g);
            else if (op == 2) vender(g);
            else if (op == 3) equipar(g);
            else System.out.println(Cores.aviso("Opcao invalida. Escolha 1, 2, 3 ou 0."));
        }
    }

    // Compra equipamentos ou itens, descontando o ouro do grupo.
    private static void comprar(Grupo g) {
        List<Item> itens = DadosService.itensDaLoja();
        List<Equipamento> equipamentos = DadosService.equipamentosDaLoja();
        while (true) {
            System.out.println("\n=== COMPRAR ===");
            int n = 1;
            for (Equipamento e : equipamentos) System.out.println(n++ + " - " + e.descricao());
            for (Item i : itens) System.out.println(n++ + " - " + i.descricao().replace("(x1)", ""));
            System.out.println("0 - Voltar");
            int op = Entrada.lerInt("Produto: ");
            if (op == 0) return;
            int total = equipamentos.size() + itens.size();
            if (op < 1 || op > total) { System.out.println(Cores.aviso("Produto invalido.")); continue; }
            int qtd = Entrada.lerInt("Quantidade: ");
            if (qtd == 0) { System.out.println(Cores.aviso("Compra cancelada.")); continue; }
            if (qtd < 0) { System.out.println(Cores.aviso("A quantidade nao pode ser negativa.")); continue; }
            if (op <= equipamentos.size()) {
                Equipamento modelo = equipamentos.get(op - 1);
                int custo = modelo.getPreco() * qtd;
                if (!g.gastarOuro(custo)) { System.out.println(Cores.aviso("Dinheiro insuficiente. Custo: $" + custo + ". Ouro atual: $" + g.getOuro() + ".")); continue; }
                for (int i=0;i<qtd;i++) g.getInventario().adicionarEquipamento(clonar(modelo));
                System.out.println(Cores.sucesso("Comprado: " + modelo.getNome() + " x" + qtd + "."));
            } else {
                Item modelo = itens.get(op - equipamentos.size() - 1);
                int custo = modelo.getPreco() * qtd;
                if (!g.gastarOuro(custo)) { System.out.println(Cores.aviso("Dinheiro insuficiente. Custo: $" + custo + ". Ouro atual: $" + g.getOuro() + ".")); continue; }
                g.getInventario().adicionarItem(new Item(modelo.getNome(), modelo.getTipo(), modelo.getPreco(), modelo.getCuraHp(), modelo.getCuraMana(), qtd));
                System.out.println(Cores.sucesso("Comprado: " + modelo.getNome() + " x" + qtd + "."));
            }
        }
    }

    // Vende itens por metade do preco (minimo 1 de ouro por unidade).
    private static void vender(Grupo g) {
        if (g.getInventario().getItens().isEmpty()) { System.out.println(Cores.aviso("Nenhum item para vender.")); return; }
        while (true) {
            System.out.println("\n=== VENDER ===");
            for (int i=0;i<g.getInventario().getItens().size();i++) System.out.println((i+1) + " - " + g.getInventario().getItens().get(i).descricao());
            System.out.println("0 - Voltar");
            int op=Entrada.lerInt("Item: ");
            if(op==0)return;
            if(op<1||op>g.getInventario().getItens().size()){System.out.println(Cores.aviso("Item invalido."));continue;}
            Item item=g.getInventario().getItens().get(op-1);
            int qtd=Entrada.lerInt("Quantidade (1 a "+item.getQuantidade()+"): ");
            if(qtd==0){System.out.println(Cores.aviso("Venda cancelada."));continue;}
            if(qtd<0||qtd>item.getQuantidade()){System.out.println(Cores.aviso("Quantidade invalida. Voce possui x"+item.getQuantidade()+"."));continue;}
            int valor=Math.max(1,item.getPreco()/2)*qtd;
            g.getInventario().removerQuantidade(item,qtd); g.adicionarOuro(valor);
            System.out.println(Cores.sucesso(item.getNome()+" vendido: x"+qtd+" por $"+valor+".")); return;
        }
    }

    // Escolhe um personagem e equipa nele um equipamento do inventario.
    private static void equipar(Grupo g) {
        if(g.getInventario().getEquipamentos().isEmpty()){System.out.println(Cores.aviso("Nenhum equipamento no inventario do grupo."));return;}
        Personagem p=escolherPersonagem(g); if(p==null)return;
        while(true){
            System.out.println("\nEquipamentos disponiveis para equipar:");
            for(int i=0;i<g.getInventario().getEquipamentos().size();i++) System.out.println((i+1)+" - "+g.getInventario().getEquipamentos().get(i).descricao());
            System.out.println("0 - Voltar");
            int op=Entrada.lerInt("Equipamento: ");
            if(op==0)return;
            if(op<1||op>g.getInventario().getEquipamentos().size()){System.out.println(Cores.aviso("Equipamento invalido."));continue;}
            Equipamento e=g.getInventario().getEquipamentos().get(op-1);
            if(!e.podeSerUsadoPor(p)){System.out.println(Cores.aviso("Esse equipamento nao pode ser usado por "+p.getNome()+"."));continue;}
            Equipamento antigo=p.equipamentoDoTipo(e.getTipo());
            if(EquipamentoService.equipar(g,p,e)){
                System.out.println(Cores.sucesso(e.getNome()+" equipado em "+p.getNome()+"."));
                if(antigo!=null) System.out.println(Cores.normal(antigo.getNome()+" voltou para o inventario."));
                mostrarAtributos(p);
            } else System.out.println(Cores.aviso("Nao foi possivel equipar esse equipamento."));
            return;
        }
    }

    // Menu do grupo: ver personagens, equipar, melhorar atributos e ver inventario.
    private static void grupo(Grupo g) {
        while(true){
            System.out.println("\n=== GRUPO ===");
            System.out.println("Lider: "+Cores.nomePersonagem(g.getLider()));
            for(Personagem p:g.getPersonagens()) mostrarResumo(p);
            System.out.println("\n1 - Equipar equipamentos");
            System.out.println("2 - Melhorar atributos");
            System.out.println("3 - Ver inventario");
            System.out.println("0 - Voltar");
            int op=Entrada.lerInt("Escolha: ");
            if(op==0)return;
            if(op==1)equipar(g);
            else if(op==2)melhorarAtributos(g);
            else if(op==3){g.getInventario().mostrar();Entrada.enter();}
            else System.out.println(Cores.aviso("Opcao invalida."));
        }
    }

    // Mostra um resumo dos dados de um personagem.
    private static void mostrarResumo(Personagem p){
        System.out.println("\n"+Cores.nomePersonagem(p.getNome())+" | Nivel "+p.getNivel()+" | HP "+p.getVida()+"/"+p.getVidaMaxima()+" | Mana "+p.getMana()+"/"+p.getManaMaxima());
        System.out.println("Forca: "+p.getForca()+" | Defesa: "+p.getDefesa()+" | Inteligencia: "+p.getInteligencia());
        System.out.println("Resistencia: "+p.getResistencia()+" | Velocidade: "+p.getVelocidade()+" | Sorte: "+p.getSorte());
        System.out.println("Arma: "+nome(p.getArmaEquipada())+" | Armadura: "+nome(p.getArmaduraEquipada()));
        System.out.println("Botas: "+nome(p.getBotasEquipadas())+" | Acessorio: "+nome(p.getAcessorioEquipado()));
        if(p.getPontosAtributo()>0)System.out.println(Cores.aviso("Pontos de atributo disponiveis: "+p.getPontosAtributo()));
    }

    // Gasta os pontos de atributo ganhos ao subir de nivel.
    private static void melhorarAtributos(Grupo g){
        Personagem p=escolherPersonagem(g); if(p==null)return;
        while(p.getPontosAtributo()>0){
            System.out.println("\n=== MELHORAR ATRIBUTOS: "+Cores.nomePersonagem(p.getNome())+" ===");
            System.out.println("Pontos disponiveis: "+p.getPontosAtributo());
            System.out.println("1 - Forca: "+p.getForca());
            System.out.println("2 - Defesa: "+p.getDefesa());
            System.out.println("3 - Inteligencia: "+p.getInteligencia());
            System.out.println("4 - Resistencia: "+p.getResistencia());
            System.out.println("5 - Velocidade: "+p.getVelocidade());
            System.out.println("6 - Sorte: "+p.getSorte());
            System.out.println("0 - Guardar pontos para depois");
            int op=Entrada.lerInt("Escolha: "); if(op==0)return;
            int antes=valorAtributo(p,op);
            if(p.distribuirPonto(op)) System.out.println(Cores.sucesso(nomeAtributo(op)+": "+antes+" -> "+valorAtributo(p,op)+". Restam "+p.getPontosAtributo()+" pontos."));
            else System.out.println(Cores.aviso("Opcao invalida."));
        }
    }

    // Devolve o valor do atributo conforme o numero da opcao (1 a 6).
    private static int valorAtributo(Personagem p,int op){switch(op){case 1:return p.getForca();case 2:return p.getDefesa();case 3:return p.getInteligencia();case 4:return p.getResistencia();case 5:return p.getVelocidade();case 6:return p.getSorte();default:return -1;}}
    // Devolve o nome do atributo conforme o numero da opcao (1 a 6).
    private static String nomeAtributo(int op){switch(op){case 1:return "Forca";case 2:return "Defesa";case 3:return "Inteligencia";case 4:return "Resistencia";case 5:return "Velocidade";case 6:return "Sorte";default:return "Atributo";}}
    // Mostra os atributos atuais do personagem em uma unica linha.
    private static void mostrarAtributos(Personagem p){System.out.println("Atributos atuais: Forca "+p.getForca()+" | Defesa "+p.getDefesa()+" | Inteligencia "+p.getInteligencia()+" | Resistencia "+p.getResistencia()+" | Velocidade "+p.getVelocidade()+" | Sorte "+p.getSorte());}
    // Devolve o nome do equipamento ou "nenhum" se estiver vazio.
    private static String nome(Equipamento e){return e==null?"nenhum":e.getNome();}

    // Mostra os personagens e deixa o jogador escolher um.
    private static Personagem escolherPersonagem(Grupo g){System.out.println("\nEscolha o personagem:");for(int i=0;i<g.getPersonagens().size();i++)System.out.println((i+1)+" - "+Cores.nomePersonagem(g.getPersonagens().get(i).getNome()));System.out.println("0 - Voltar");int op=Entrada.lerInt("Escolha: ");if(op==0)return null;if(op<1||op>g.getPersonagens().size()){System.out.println(Cores.aviso("Personagem invalido."));return null;}return g.getPersonagens().get(op-1);}

    // Taverna de Eldoria: mostra a missao da caverna e os rumores
    // (rumores podem fazer a Elyra entrar no grupo).
    // Retorna um array com os estados: entrou, recusada e irCaverna.
    private static boolean[] tavernaEldoria(Grupo g, boolean entrou, boolean recusada){
        while(true){
            System.out.println("\n=== TAVERNA DE ELDORIA ===");
            System.out.println("1 - Missoes");
            System.out.println("2 - Rumores");
            System.out.println("0 - Voltar");
            int op=Entrada.lerInt("Escolha: ");
            if(op==0)return new boolean[]{entrou,recusada};
            if(op==1){
                System.out.println("\n=== MISSAO ATUAL ===");
                System.out.println(Cores.amarelo("Mineiros de Eldoria desapareceram."));
                System.out.println("Alguns mineiros enviados para trabalhar nas minas de cristais nao retornaram. Os ultimos relatos indicam uma area mais profunda.");
                System.out.println("A cidade precisa de alguem para descobrir o que aconteceu e, se possivel, trazer os mineiros de volta.");
                System.out.println("Destino: Caverna dos Slimes");
                System.out.println("1 - Ir para a Caverna dos Slimes");
                System.out.println("2 - Voltar");
                int m=Entrada.lerInt("Escolha: ");
                if(m==1){System.out.println(Cores.sucesso("A missao foi aceita. O caminho para a Caverna dos Slimes foi marcado."));return new boolean[]{entrou,recusada,true};}
            } else if(op==2){
                if(!entrou){
                    boolean aceita=eventoElyra(g);
                    if(aceita){entrou=true;recusada=false;}
                    else recusada=true;
                } else {System.out.println("Viajantes comentam sobre as minas e a estranha energia nas criaturas.");Entrada.enter();}
            } else System.out.println(Cores.aviso("Opcao invalida."));
        }
    }

    // Evento de encontro com a Elyra: mostra a conversa e pergunta
    // se o jogador quer que ela entre no grupo.
    private static boolean eventoElyra(Grupo g){
        System.out.println("\n=== RUMORES DE ELDORIA ===");
        System.out.println("Uma figura surge entre as arvores, observando o grupo de longe.");
        System.out.println("Ela se aproxima com cautela, mantendo o arco em maos, mas sem demonstrar intencao de atacar.");
        System.out.println(Cores.elyra("Elyra: \"Meu nome e Elyra. Sou uma das guardias desta regiao e conheco estas florestas ha muitos anos.\""));
        System.out.println(Cores.elyra("\"Eu vi voces enfrentarem Fenrok. Ele sempre protegeu esta parte da floresta, entao ve-lo daquele jeito me preocupou.\""));
        System.out.println(Cores.elyra("\"Nos ultimos tempos, algumas criaturas comecaram a agir de forma diferente. Ha algo errado nesta regiao, e eu quero descobrir o que esta acontecendo.\""));
        System.out.println(Cores.elyra("\"Se voces estao seguindo esse caminho para investigar o que esta acontecendo, talvez nossos objetivos sejam os mesmos.\""));
        System.out.println(Cores.elyra("\"Vou seguir com voces. Conheco estas terras e posso ajudar a encontrar o que esta causando essas mudancas.\""));
        System.out.println("\n1 - Aceitar Elyra no grupo");
        System.out.println("2 - Continuar sem ela");
        System.out.println("0 - Voltar");
        int op=Entrada.lerInt("Escolha: ");
        if(op==0)return false;
        if(op==2){System.out.println(Cores.aviso("Elyra permanece em Eldoria. Ela podera ser encontrada novamente pelos rumores da taverna."));Entrada.enter();return false;}
        if(op!=1){System.out.println(Cores.aviso("Opcao invalida."));return false;}
        Elyra e=new Elyra();g.adicionar(e);
        System.out.println(Cores.sucesso("*** ELYRA ENTROU NO GRUPO! ***"));
        System.out.println("Nivel: 2 | Classe: Arqueira Arcana");
        System.out.println("Elyra entra com seu equipamento inicial ja equipado.");Entrada.enter();return true;
    }

    // Taverna das outras cidades: so mostra um texto de ambientacao.
    private static void tavernaGenerica(String cidade){System.out.println("\nA taverna de "+cidade+" esta cheia de viajantes.");System.out.println("Rumores sobre a regiao, criaturas e antigas historias circulam entre as mesas.");Entrada.enter();}

    // Cria uma copia do equipamento (usado ao comprar varias unidades).
    private static Equipamento clonar(Equipamento e){return new Equipamento(e.getNome(),e.getTipo(),e.getRaridade(),e.getClassePermitida(),e.getPreco(),e.getForca(),e.getDefesa(),e.getInteligencia(),e.getResistencia(),e.getVelocidade(),e.getSorte());}
}
