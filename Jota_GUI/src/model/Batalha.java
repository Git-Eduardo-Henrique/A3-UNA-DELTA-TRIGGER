package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class Batalha {
    public enum Resultado { VITORIA, DERROTA, FUGA }
    private static final Random RANDOM = new Random();

    private static class Acao {
        Personagem personagem;
        Inimigo inimigo;
        int velocidade;
        boolean jogador;

        Acao(Personagem personagem, int velocidade) {
            this.personagem = personagem;
            this.velocidade = velocidade;
            this.jogador = true;
        }

        Acao(Inimigo inimigo, int velocidade) {
            this.inimigo = inimigo;
            this.velocidade = velocidade;
            this.jogador = false;
        }
    }

    public static Resultado iniciar(Personagem[] grupo, Inimigo[] inimigos, boolean permitirFuga) {
        int rodada = 1;
        System.out.println("\n========================================");
        System.out.println("              BATALHA");
        System.out.println("========================================");

        while (grupoVivo(grupo) && inimigoVivo(inimigos)) {
            System.out.println("\n" + Cores.CINZA + "---------- RODADA " + rodada + " ----------" + Cores.RESET);
            List<Acao> ordem = criarOrdem(grupo, inimigos);

            for (Acao acao : ordem) {
                if (!grupoVivo(grupo) || !inimigoVivo(inimigos)) break;

                if (acao.jogador) {
                    if (!acao.personagem.vivo()) continue;
                    int resultadoTurno = turnoPersonagem(acao.personagem, grupo, inimigos, permitirFuga);
                    if (resultadoTurno == 2) return Resultado.FUGA;
                } else {
                    if (!acao.inimigo.vivo()) continue;
                    turnoInimigo(acao.inimigo, grupo);
                }
            }
            rodada++;
        }

        if (!grupoVivo(grupo)) {
            mostrarGameOver();
            return Resultado.DERROTA;
        }
        if (!inimigoVivo(inimigos)) {
            finalizarVitoria(grupo, inimigos);
            return Resultado.VITORIA;
        }
        return Resultado.DERROTA;
    }

    private static List<Acao> criarOrdem(Personagem[] grupo, Inimigo[] inimigos) {
        List<Acao> ordem = new ArrayList<Acao>();
        // Na demo, os personagens agem na ordem do grupo e depois os inimigos.
        // A Velocidade ja esta registrada nos atributos e sera usada para uma
        // iniciativa mais completa nas proximas areas, sem alterar o tutorial.
        for (Personagem p : grupo) if (p.vivo()) ordem.add(new Acao(p, p.getVelocidade()));
        for (Inimigo e : inimigos) if (e.vivo()) ordem.add(new Acao(e, e.getVelocidade()));
        return ordem;
    }

    private static int turnoPersonagem(Personagem p, Personagem[] grupo, Inimigo[] inimigos, boolean permitirFuga) {
        while (true) {
            mostrarStatus(grupo, inimigos);
            System.out.println("\nVez de " + nomeColorido(p) + ".");
            System.out.println("1 - Atacar");
            System.out.println("2 - Defender");
            System.out.println("3 - Habilidade");
            System.out.println("4 - Item");
            System.out.println("5 - Fugir");

            int op = Entrada.lerInt("Acao: ");
            if (op == 1) {
                Inimigo alvo = escolherInimigo(inimigos);
                if (alvo == null) continue;
                int dano = alvo.receberDano(p.atacar());
                System.out.println(nomeColorido(p) + " atacou " + Cores.inimigo(alvo.getNome())
                        + " e causou " + dano + " de dano.");
                return 1;
            }
            if (op == 2) {
                p.defender();
                return 1;
            }
            if (op == 3) {
                if (p.habilidade(grupo, inimigos)) return 1;
                continue;
            }
            if (op == 4) {
                if (usarItem(p, grupo)) return 1;
                continue;
            }
            if (op == 5) {
                if (!permitirFuga) {
                    System.out.println(Cores.aviso("Nao e possivel fugir desta batalha."));
                    continue;
                }
                System.out.println(Cores.amarelo("Voce fugiu da batalha."));
                return 2;
            }
            System.out.println(Cores.aviso("Opcao invalida. Escolha uma opcao de 1 a 5."));
        }
    }

    private static void turnoInimigo(Inimigo inimigo, Personagem[] grupo) {
        Personagem alvo = alvoAleatorio(grupo);
        if (alvo == null) return;
        int dano = alvo.receberDano(inimigo.atacar());
        System.out.println(Cores.inimigo(inimigo.getNome()) + " atacou " + nomeColorido(alvo)
                + " e causou " + dano + " de dano.");
        if (!alvo.vivo()) System.out.println(Cores.aviso(alvo.getNome() + " foi derrotado!"));
    }

    public static Inimigo escolherInimigo(Inimigo[] inimigos) {
        while (true) {
            System.out.println("\nEscolha o alvo:");
            int numero = 0;
            for (Inimigo inimigo : inimigos) {
                if (inimigo.vivo()) {
                    numero++;
                    System.out.println(numero + " - " + Cores.inimigo(inimigo.getNome())
                            + " | HP " + inimigo.getVida() + "/" + inimigo.getVidaMaxima());
                }
            }
            System.out.println("0 - Voltar");
            int escolha = Entrada.lerInt("Alvo: ");
            if (escolha == 0) return null;
            numero = 0;
            for (Inimigo inimigo : inimigos) {
                if (inimigo.vivo()) {
                    numero++;
                    if (numero == escolha) return inimigo;
                }
            }
            System.out.println(Cores.aviso("Alvo invalido. Escolha um inimigo que esteja vivo."));
        }
    }

    private static boolean usarItem(Personagem usuario, Personagem[] grupo) {
        while (true) {
            List<Item> utilizaveis = new ArrayList<Item>();
            for (Item item : usuario.getInventario().getItens()) if (item.utilizavelEmBatalha()) utilizaveis.add(item);
            if (utilizaveis.isEmpty()) {
                System.out.println(Cores.aviso("Voce nao possui itens utilizaveis em batalha."));
                return false;
            }

            System.out.println("\n=== ITENS DE " + usuario.getNome().toUpperCase() + " ===");
            for (int i = 0; i < utilizaveis.size(); i++) {
                Item item = utilizaveis.get(i);
                System.out.println((i + 1) + " - " + item.descricao());
            }
            System.out.println("0 - Voltar");
            int op = Entrada.lerInt("Item: ");
            if (op == 0) return false;
            if (op < 1 || op > utilizaveis.size()) {
                System.out.println(Cores.aviso("Item invalido."));
                continue;
            }

            Item item = utilizaveis.get(op - 1);
            Personagem alvo = escolherAliadoVivo(grupo);
            if (alvo == null) continue;

            if (item.getCuraHp() > 0) {
                if (alvo.getVida() >= alvo.getVidaMaxima()) {
                    System.out.println(Cores.aviso(alvo.getNome() + " ja esta com a vida cheia."));
                    continue;
                }
                int antes = alvo.getVida();
                alvo.curar(item.getCuraHp());
                usuario.getInventario().removerUmaUnidade(item);
                System.out.println(Cores.sucesso(alvo.getNome() + " recuperou " + (alvo.getVida() - antes) + " HP."));
                return true;
            }

            if (item.getCuraMana() > 0) {
                if (alvo.getMana() >= alvo.getManaMaxima()) {
                    System.out.println(Cores.aviso(alvo.getNome() + " ja esta com a mana cheia."));
                    continue;
                }
                int antes = alvo.getMana();
                alvo.curarMana(item.getCuraMana());
                usuario.getInventario().removerUmaUnidade(item);
                System.out.println(Cores.sucesso(alvo.getNome() + " recuperou " + (alvo.getMana() - antes) + " mana."));
                return true;
            }
        }
    }

    private static Personagem escolherAliadoVivo(Personagem[] grupo) {
        while (true) {
            System.out.println("\nEscolha o alvo:");
            for (int i = 0; i < grupo.length; i++) {
                Personagem p = grupo[i];
                if (p.vivo()) System.out.println((i + 1) + " - " + nomeColorido(p) + " | HP " + p.getVida() + "/" + p.getVidaMaxima());
            }
            System.out.println("0 - Voltar");
            int op = Entrada.lerInt("Alvo: ");
            if (op == 0) return null;
            if (op >= 1 && op <= grupo.length && grupo[op - 1].vivo()) return grupo[op - 1];
            System.out.println(Cores.aviso("Alvo invalido. Escolha um personagem vivo."));
        }
    }

    private static void mostrarStatus(Personagem[] grupo, Inimigo[] inimigos) {
        System.out.println("\n--- GRUPO ---");
        for (Personagem p : grupo) {
            String status = p.vivo() ? "" : " | [DERROTADO]";
            System.out.println(nomeColorido(p) + " | HP " + p.getVida() + "/" + p.getVidaMaxima()
                    + " | Mana " + p.getMana() + "/" + p.getManaMaxima() + status);
        }
        System.out.println("--- INIMIGOS ---");
        for (Inimigo e : inimigos) {
            if (e.vivo()) System.out.println(Cores.inimigo(e.getNome()) + " | HP " + e.getVida() + "/" + e.getVidaMaxima());
            else System.out.println(Cores.CINZA + e.getNome() + " | [DERROTADO]" + Cores.RESET);
        }
    }

    private static Personagem alvoAleatorio(Personagem[] grupo) {
        List<Personagem> vivos = new ArrayList<Personagem>();
        for (Personagem p : grupo) if (p.vivo()) vivos.add(p);
        if (vivos.isEmpty()) return null;
        return vivos.get(RANDOM.nextInt(vivos.size()));
    }

    private static boolean grupoVivo(Personagem[] grupo) {
        for (Personagem p : grupo) if (p.vivo()) return true;
        return false;
    }

    private static boolean inimigoVivo(Inimigo[] inimigos) {
        for (Inimigo e : inimigos) if (e.vivo()) return true;
        return false;
    }

    private static void finalizarVitoria(Personagem[] grupo, Inimigo[] inimigos) {
        Personagem dono = grupo[0];
        int dinheiro = 0;
        System.out.println("\n========================================");
        System.out.println(Cores.sucesso("               VITORIA!"));
        System.out.println("========================================");

        for (Inimigo inimigo : inimigos) {
            dinheiro += inimigo.getRecompensa();
            for (Item drop : inimigo.sortearDrops()) {
                dono.getInventario().adicionarItem(drop);
                System.out.println(Cores.sucesso("DROP: " + drop.getNome() + " x" + drop.getQuantidade()));
            }
            Item material = inimigo.sortearMaterial();
            if (material != null) {
                dono.getInventario().adicionarItem(material);
                System.out.println(Cores.sucesso("DROP: " + material.getNome() + " x1"));
            }
        }
        dono.adicionarDinheiro(dinheiro);
        System.out.println(Cores.amarelo("Dinheiro recebido: +$" + dinheiro));
        System.out.println("Os drops foram para o inventario do lider do grupo.");
        Entrada.enter();
    }

    private static void mostrarGameOver() {
        System.out.println("\n========================================");
        System.out.println(Cores.vermelho("             GAME OVER"));
        System.out.println("========================================");
        System.out.println("Todos os personagens foram derrotados.");
    }

    private static String nomeColorido(Personagem p) {
        if (p.getNome().equals("Kael")) return Cores.kael(p.getNome());
        if (p.getNome().equals("Lyra")) return Cores.lyra(p.getNome());
        return Cores.elyra(p.getNome());
    }
}
