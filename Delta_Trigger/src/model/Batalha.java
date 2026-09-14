package model;

public class Batalha {
    public static boolean iniciar(Personagem[] grupo, Inimigo inimigo, boolean permitirFuga) {
        System.out.println("\n===== BATALHA: " + inimigo.getNome() + " =====");

        while (inimigo.vivo() && grupoVivo(grupo)) {
            for (Personagem p : grupo) {
                if (!p.vivo() || !inimigo.vivo()) continue;

                System.out.println("\n" + p.getNome() + " | HP " + p.getVida() + "/" + p.getVidaMaxima()
                        + " | Mana " + p.getMana() + "/" + p.getManaMaxima());
                System.out.println(inimigo.getNome() + " | HP " + inimigo.getVida() + "/" + inimigo.getVidaMaxima());
                System.out.println("1 - Ataque | 2 - Defesa | 3 - Habilidade | 4 - Item | 5 - Fugir");

                int op = Entrada.lerInt("Acao: ");

                if (op == 1) {
                    int dano = p.atacar();
                    System.out.println("Causou " + inimigo.receberDano(dano) + " de dano.");
                } else if (op == 2) {
                    p.defender();
                } else if (op == 3) {
                    p.habilidade(grupo, inimigo);
                } else if (op == 4) {
                    usarItem(p);
                } else if (op == 5) {
                    if (!permitirFuga) System.out.println("Nao ha saida da floresta!");
                    else { System.out.println("Voce fugiu."); return false; }
                } else {
                    System.out.println("Opcao invalida.");
                    continue;
                }

                if (inimigo.vivo()) {
                    Personagem alvo = primeiroVivo(grupo);
                    int dano = alvo.receberDano(inimigo.atacar());
                    System.out.println(inimigo.getNome() + " atacou " + alvo.getNome()
                            + " e causou " + dano + " de dano.");
                }
            }
        }

        if (!inimigo.vivo()) {
            System.out.println("\nVITORIA!");
            for (Personagem p : grupo) if (p.vivo()) p.adicionarDinheiro(inimigo.getRecompensa());
            System.out.println("O grupo recebeu $" + inimigo.getRecompensa() + ".");
            return true;
        }

        System.out.println("O grupo foi derrotado.");
        return false;
    }

    private static boolean grupoVivo(Personagem[] grupo) {
        for (Personagem p : grupo) if (p.vivo()) return true;
        return false;
    }

    private static Personagem primeiroVivo(Personagem[] grupo) {
        for (Personagem p : grupo) if (p.vivo()) return p;
        return grupo[0];
    }

    private static void usarItem(Personagem p) {
        if (p.getInventario().getItens().isEmpty()) {
            System.out.println("Sem itens.");
            return;
        }

        for (int i = 0; i < p.getInventario().getItens().size(); i++)
            System.out.println((i + 1) + " - " + p.getInventario().getItens().get(i).descricao());

        int op = Entrada.lerInt("Item: ");
        if (op < 1 || op > p.getInventario().getItens().size()) return;

        Item item = p.getInventario().getItens().get(op - 1);
        p.curar(item.getCuraHp());
        p.curarMana(item.getCuraMana());
        p.getInventario().removerItem(item);
        System.out.println(item.getNome() + " usado.");
    }
}
