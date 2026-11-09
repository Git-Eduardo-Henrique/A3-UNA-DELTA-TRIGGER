// ============================================================
// ARQUIVO: Batalha.java
// Controla as batalhas por turnos entre o grupo do jogador e os
// inimigos. Em cada rodada, cada personagem vivo escolhe uma acao
// (atacar, defender, habilidade, item ou fugir) e depois os
// inimigos contra-atacam. No fim, retorna se houve vitoria,
// derrota ou fuga.
// ============================================================

package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class Batalha {
    // Resultado possivel de uma batalha.
    public enum Resultado { VITORIA, DERROTA, FUGA }

    // Guarda o resultado de um ataque: se acertou, quanto dano e se foi critico.
    public static class AtaqueResultado { public final boolean acertou; public final int dano; public final boolean critico; public AtaqueResultado(boolean a,int d,boolean c){acertou=a;dano=d;critico=c;} }

    // Gerador de numeros aleatorios usado nos ataques e criticos.
    private static final Random RANDOM=new Random();

    // Construtor privado: a classe so tem metodos estaticos.
    private Batalha(){}

    // Inicia uma batalha e so termina quando alguem vence, todos
    // morrem ou o jogador foge (se a fuga for permitida).
    public static Resultado iniciar(Grupo grupo, Inimigo[] inimigos, boolean permitirFuga){
        int rodada=1; System.out.println("\n========================================\n              BATALHA\n========================================");
        while(!grupo.todosDerrotados()&&inimigoVivo(inimigos)){
            System.out.println("\n"+Cores.CINZA+"---------- RODADA "+rodada+" ----------"+Cores.RESET);
            for(Personagem p:grupo.getPersonagens()){
                if(grupo.todosDerrotados()||!inimigoVivo(inimigos))break; if(!p.vivo())continue;
                int r=turnoPersonagem(p,grupo,inimigos,permitirFuga); if(r==2)return Resultado.FUGA;
            }
            if(grupo.todosDerrotados()||!inimigoVivo(inimigos))break;
            for(Inimigo e:inimigos){if(!e.vivo())continue;if(grupo.todosDerrotados())break;turnoInimigo(e,grupo);}
            rodada++;
        }
        if(grupo.todosDerrotados()){System.out.println(Cores.vermelho("Todos os personagens foram derrotados."));return Resultado.DERROTA;}
        finalizarVitoria(grupo,inimigos);return Resultado.VITORIA;
    }
    // Executa o turno de um personagem: mostra o menu de acoes e
    // espera a escolha do jogador. Retorna 1 para continuar ou 2 para fuga.
    private static int turnoPersonagem(Personagem p,Grupo grupo,Inimigo[] inimigos,boolean permitirFuga){
        while(true){mostrarStatus(grupo,inimigos);System.out.println("\nVez de "+p.nomeColorido()+".");System.out.println("1 - Atacar");System.out.println("2 - Defender");System.out.println("3 - Habilidade");System.out.println("4 - Item");System.out.println("5 - Fugir");int op=Entrada.lerInt("Acao: ");
            if(op==1){Inimigo alvo=escolherInimigo(inimigos);if(alvo==null)continue;AtaqueResultado r=calcularAtaque(p,alvo,p.atacar());if(r.acertou){System.out.println(p.nomeColorido()+" atacou "+Cores.inimigo(alvo.getNome())+" e causou "+r.dano+" de dano."+(r.critico?Cores.amarelo(" CRITICO!"):""));if(!alvo.vivo())System.out.println(Cores.sucesso(alvo.getNome()+" [DERROTADO]"));}else System.out.println(Cores.aviso(p.nomeColorido()+" errou o ataque!"));return 1;}
            if(op==2){System.out.println("\n1 - Defender");System.out.println("2 - Recuperar Mana");System.out.println("0 - Voltar");int d=Entrada.lerInt("Escolha: ");if(d==0)continue;if(d==1){p.defender();System.out.println(Cores.aviso(p.nomeColorido()+" assumiu uma postura defensiva."));return 1;}if(d==2){if(p.getMana()>=p.getManaMaxima()){System.out.println(Cores.aviso(p.getNome()+" ja esta com a mana cheia."));continue;}p.recuperarManaDefesa();return 1;}System.out.println(Cores.aviso("Opcao invalida. Escolha 1, 2 ou 0."));continue;}
            if(op==3){if(p.habilidade(grupo,inimigos))return 1;continue;}
            if(op==4){if(usarItem(p,grupo))return 1;continue;}
            if(op==5){if(!permitirFuga){System.out.println(Cores.aviso("Nao e possivel fugir desta batalha."));continue;}System.out.println(Cores.amarelo("Voce fugiu da batalha."));return 2;}
            System.out.println(Cores.aviso("Opcao invalida. Escolha uma opcao de 1 a 5."));
        }
    }
    // Calcula um ataque: chance de acerto (baseada na velocidade),
    // chance de critico (baseada na sorte) e aplica o dano no alvo.
    public static AtaqueResultado calcularAtaque(Personagem atacante,Inimigo alvo,int danoBase){int chance=90+(atacante.getVelocidade()-alvo.getVelocidade())*2;chance=Math.max(65,Math.min(96,chance));boolean acerto=RANDOM.nextInt(100)<chance;if(!acerto)return new AtaqueResultado(false,0,false);boolean crit=RANDOM.nextInt(100)<Math.min(30,5+atacante.getSorte()*2);if(crit)danoBase=(int)Math.round(danoBase*1.5);return new AtaqueResultado(true,alvo.receberDano(Math.max(1,danoBase)),crit);}
    // Executa o turno de um inimigo: escolhe um personagem vivo
    // aleatorio e tenta atacar (podendo errar).
    private static void turnoInimigo(Inimigo e,Grupo grupo){List<Personagem> vivos=new ArrayList<Personagem>();for(Personagem p:grupo.getPersonagens())if(p.vivo())vivos.add(p);if(vivos.isEmpty())return;Personagem alvo=vivos.get(RANDOM.nextInt(vivos.size()));int chance=82+(e.getVelocidade()-alvo.getVelocidade())*2;chance=Math.max(60,Math.min(94,chance));if(RANDOM.nextInt(100)>=chance){System.out.println(Cores.inimigo(e.getNome())+" errou o ataque contra "+alvo.nomeColorido()+"!");return;}int dano=alvo.receberDano(e.atacar());System.out.println(Cores.inimigo(e.getNome())+" atacou "+alvo.nomeColorido()+" e causou "+dano+" de dano.");if(!alvo.vivo())System.out.println(Cores.aviso(alvo.getNome()+" foi derrotado!"));}
    // Mostra os inimigos vivos e deixa o jogador escolher um alvo.
    // Retorna null se o jogador decidir voltar.
    public static Inimigo escolherInimigo(Inimigo[] inimigos){while(true){System.out.println("\nEscolha o alvo:");int n=0;for(Inimigo e:inimigos)if(e.vivo()){n++;System.out.println(n+" - "+Cores.inimigo(e.getNome())+" | HP "+e.getVida()+"/"+e.getVidaMaxima());}System.out.println("0 - Voltar");int op=Entrada.lerInt("Alvo: ");if(op==0)return null;n=0;for(Inimigo e:inimigos)if(e.vivo()){n++;if(n==op)return e;}System.out.println(Cores.aviso("Alvo invalido. Escolha um inimigo que esteja vivo."));}}
    // Mostra os itens utilizaveis em batalha e deixa o jogador usar
    // um deles em um aliado. Retorna true se o item foi consumido.
    private static boolean usarItem(Personagem usuario,Grupo grupo){while(true){List<Item> itens=new ArrayList<Item>();for(Item i:grupo.getInventario().getItens())if(i.utilizavelEmBatalha())itens.add(i);if(itens.isEmpty()){System.out.println(Cores.aviso("Voce nao possui itens utilizaveis em batalha."));return false;}System.out.println("\n=== ITENS DO GRUPO ===");for(int i=0;i<itens.size();i++)System.out.println((i+1)+" - "+itens.get(i).descricao());System.out.println("0 - Voltar");int op=Entrada.lerInt("Item: ");if(op==0)return false;if(op<1||op>itens.size()){System.out.println(Cores.aviso("Item invalido."));continue;}Item item=itens.get(op-1);Personagem alvo=escolherAliadoVivo(grupo);if(alvo==null)continue;if(item.getCuraHp()>0){if(alvo.getVida()>=alvo.getVidaMaxima()){System.out.println(Cores.aviso(alvo.getNome()+" ja esta com a vida cheia. A poção nao foi consumida."));continue;}int antes=alvo.getVida();alvo.curar(item.getCuraHp());grupo.getInventario().removerUmaUnidade(item);System.out.println(Cores.sucesso(alvo.getNome()+" recuperou "+(alvo.getVida()-antes)+" HP."));return true;}if(item.getCuraMana()>0){if(alvo.getMana()>=alvo.getManaMaxima()){System.out.println(Cores.aviso(alvo.getNome()+" ja esta com a mana cheia. A poção nao foi consumida."));continue;}int antes=alvo.getMana();alvo.curarMana(item.getCuraMana());grupo.getInventario().removerUmaUnidade(item);System.out.println(Cores.sucesso(alvo.getNome()+" recuperou "+(alvo.getMana()-antes)+" Mana."));return true;}}}
    // Mostra os aliados vivos e deixa o jogador escolher um deles.
    private static Personagem escolherAliadoVivo(Grupo grupo){System.out.println("\nEscolha o alvo:");int n=0;for(Personagem p:grupo.getPersonagens())if(p.vivo()){n++;System.out.println(n+" - "+p.nomeColorido()+" | HP "+p.getVida()+"/"+p.getVidaMaxima()+" | Mana "+p.getMana()+"/"+p.getManaMaxima());}System.out.println("0 - Voltar");int op=Entrada.lerInt("Alvo: ");if(op==0)return null;n=0;for(Personagem p:grupo.getPersonagens())if(p.vivo()){n++;if(n==op)return p;}System.out.println(Cores.aviso("Alvo invalido."));return null;}
    // Diz se pelo menos um inimigo ainda esta vivo.
    private static boolean inimigoVivo(Inimigo[] inimigos){for(Inimigo e:inimigos)if(e.vivo())return true;return false;}
    // Mostra na tela a vida e a mana de todos os aliados e inimigos.
    private static void mostrarStatus(Grupo grupo,Inimigo[] inimigos){System.out.println("\nEQUIPE");for(Personagem p:grupo.getPersonagens())System.out.println(Cores.nomePersonagem(p.getNome())+" - Nv."+p.getNivel()+" | HP "+p.getVida()+"/"+p.getVidaMaxima()+" | Mana "+p.getMana()+"/"+p.getManaMaxima()+(p.vivo()?"":" "+Cores.vermelho("[DERROTADO]")));System.out.println("\nINIMIGOS");for(Inimigo e:inimigos)System.out.println(Cores.inimigo(e.getNome())+" | HP "+e.getVida()+"/"+e.getVidaMaxima()+(e.vivo()?"":" "+Cores.normal("[DERROTADO]")));}
    // Ao vencer, entrega o ouro, os itens e os materiais dos inimigos.
    private static void finalizarVitoria(Grupo grupo,Inimigo[] inimigos){int ouro=0;System.out.println("\n"+Cores.sucesso("===== VITORIA ====="));for(Inimigo e:inimigos)if(!e.vivo())ouro+=e.getRecompensa();if(ouro>0){grupo.adicionarOuro(ouro);System.out.println(Cores.amarelo("Ouro recebido: $"+ouro));}for(Inimigo e:inimigos){for(Item i:e.sortearDrops()){grupo.getInventario().adicionarItem(i);System.out.println(Cores.sucesso("Drop: "+i.getNome()+" x"+i.getQuantidade()));}Item mat=e.sortearMaterial();if(mat!=null){grupo.getInventario().adicionarItem(mat);System.out.println(Cores.sucesso("Material: "+mat.getNome()+" x1"));}}}
}
