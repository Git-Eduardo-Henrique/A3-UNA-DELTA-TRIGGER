package view;

import model.Cores;
import model.Entrada;
import model.Equipamento;
import model.Item;
import model.Personagem;
import util.SaveService;

public class CidadeView {
    public static int abrir(Personagem[] grupo, String lider, boolean elyraEntrou) {
        while (true) {
            System.out.println("\n========================================");
            System.out.println("              ELDORIA");
            System.out.println("========================================");
            System.out.println("Lider: " + lider);
            System.out.println("\n1 - Pousada");
            System.out.println("2 - Loja");
            System.out.println("3 - Taverna");
            System.out.println("4 - Ver equipe");
            System.out.println("5 - Ver inventario");
            int op = Entrada.lerInt("Escolha: ");

            if (op == 1) pousada(grupo, lider, elyraEntrou);
            else if (op == 2) loja(grupo);
            else if (op == 3) {
                if (!elyraEntrou) return 1;
                System.out.println(Cores.aviso("Elyra ja foi encontrada na taverna."));
            } else if (op == 4) equipe(grupo);
            else if (op == 5) inventario(grupo);
            else System.out.println(Cores.aviso("Opcao invalida. Escolha uma opcao do menu."));
        }
    }

    private static void pousada(Personagem[] grupo, String lider, boolean elyraEntrou) {
        while (true) {
            System.out.println("\n=== POUSADA ===");
            System.out.println("1 - Descansar");
            System.out.println("2 - Salvar jogo");
            System.out.println("0 - Voltar");
            int op = Entrada.lerInt("Escolha: ");
            if (op == 0) return;
            if (op == 1) {
                for (Personagem p : grupo) if (p.vivo()) p.recuperarTudo();
                System.out.println(Cores.sucesso("O grupo descansou. HP e Mana foram restaurados."));
            } else if (op == 2) {
                boolean ok = SaveService.salvar(grupo, lider, "ELDORIA", 5, elyraEntrou);
                System.out.println(ok ? Cores.sucesso("Jogo salvo com sucesso! (save.txt criptografado)")
                        : Cores.vermelho("Nao foi possivel salvar o jogo."));
            } else {
                System.out.println(Cores.aviso("Opcao invalida. Escolha 1, 2 ou 0."));
            }
        }
    }

    private static void loja(Personagem[] grupo) {
        while (true) {
            System.out.println("\n=== LOJA DE ELDORIA ===");
            System.out.println("1 - Comprar");
            System.out.println("2 - Vender itens");
            System.out.println("3 - Vender tudo");
            System.out.println("4 - Equipar equipamento");
            System.out.println("0 - Voltar");
            int op = Entrada.lerInt("Escolha: ");
            if (op == 0) return;
            if (op == 1) comprar(grupo);
            else if (op == 2) vender(grupo, false);
            else if (op == 3) vender(grupo, true);
            else if (op == 4) equipar(grupo);
            else System.out.println(Cores.aviso("Opcao invalida. Escolha 1 a 4 ou 0."));
        }
    }

    private static void comprar(Personagem[] grupo) {
        Personagem p = escolherPersonagem(grupo);
        if (p == null) return;
        while (true) {
            System.out.println("\nDinheiro de " + p.getNome() + ": $" + p.getDinheiro());
            System.out.println("1 - Espada de Ferro [Arma] $35 | F+2");
            System.out.println("2 - Armadura de Couro [Armadura] $30 | D+2 R+1");
            System.out.println("3 - Cajado Simples [Arma] $35 | I+2");
            System.out.println("4 - Pocao de Vida [Pocao] $15 | +30 HP");
            System.out.println("5 - Pocao de Mana [Pocao] $18 | +20 Mana");
            System.out.println("0 - Voltar");
            int produto = Entrada.lerInt("Produto: ");
            if (produto == 0) return;
            if (produto == 1) { comprarEquipamento(p, new Equipamento("Espada de Ferro", "Arma", 35, 2, 0, 0, 0, 0, 0)); return; }
            if (produto == 2) { comprarEquipamento(p, new Equipamento("Armadura de Couro", "Armadura", 30, 0, 2, 0, 1, 0, 0)); return; }
            if (produto == 3) { comprarEquipamento(p, new Equipamento("Cajado Simples", "Arma", 35, 0, 0, 2, 0, 0, 0)); return; }
            if (produto == 4) { comprarItem(p, new Item("Pocao de Vida", "Pocao", 15, 30, 0)); return; }
            if (produto == 5) { comprarItem(p, new Item("Pocao de Mana", "Pocao", 18, 0, 20)); return; }
            System.out.println(Cores.aviso("Produto invalido."));
        }
    }

    private static void comprarEquipamento(Personagem p, Equipamento e) {
        if (!p.gastarDinheiro(e.getPreco())) {
            System.out.println(Cores.aviso("Dinheiro insuficiente. Voce possui $" + p.getDinheiro() + "."));
            return;
        }
        p.getInventario().adicionarEquipamento(e);
        System.out.println(Cores.sucesso("Comprado: " + e.getNome() + ". Guardado no inventario."));
    }

    private static void comprarItem(Personagem p, Item item) {
        if (!p.gastarDinheiro(item.getPreco())) {
            System.out.println(Cores.aviso("Dinheiro insuficiente. Voce possui $" + p.getDinheiro() + "."));
            return;
        }
        p.getInventario().adicionarItem(item);
        System.out.println(Cores.sucesso("Comprado: " + item.getNome() + " x1."));
    }

    private static void vender(Personagem[] grupo, boolean tudo) {
        Personagem p = escolherPersonagem(grupo);
        if (p == null) return;
        if (p.getInventario().getItens().isEmpty()) {
            System.out.println(Cores.aviso("Nenhum item para vender."));
            return;
        }
        if (tudo) {
            int total = 0;
            for (Item item : p.getInventario().getItens()) total += Math.max(1, item.getPreco() / 2) * item.getQuantidade();
            p.getInventario().getItens().clear();
            p.adicionarDinheiro(total);
            System.out.println(Cores.sucesso("Todos os itens vendaveis foram vendidos por $" + total + "."));
            return;
        }
        while (true) {
            System.out.println("\nItens de " + p.getNome() + ":");
            for (int i = 0; i < p.getInventario().getItens().size(); i++) {
                System.out.println((i + 1) + " - " + p.getInventario().getItens().get(i).descricao());
            }
            System.out.println("0 - Voltar");
            int op = Entrada.lerInt("Item: ");
            if (op == 0) return;
            if (op < 1 || op > p.getInventario().getItens().size()) {
                System.out.println(Cores.aviso("Item invalido."));
                continue;
            }
            Item item = p.getInventario().getItens().get(op - 1);
            int valor = Math.max(1, item.getPreco() / 2);
            p.adicionarDinheiro(valor);
            p.getInventario().removerUmaUnidade(item);
            System.out.println(Cores.sucesso(item.getNome() + " vendido por $" + valor + "."));
            return;
        }
    }

    private static void equipar(Personagem[] grupo) {
        Personagem p = escolherPersonagem(grupo);
        if (p == null) return;
        if (p.getInventario().getEquipamentos().isEmpty()) {
            System.out.println(Cores.aviso("Nenhum equipamento no inventario."));
            return;
        }
        while (true) {
            System.out.println("\nEquipamentos de " + p.getNome() + ":");
            for (int i = 0; i < p.getInventario().getEquipamentos().size(); i++) {
                Equipamento e = p.getInventario().getEquipamentos().get(i);
                boolean equipado = e == p.getArmaEquipada() || e == p.getArmaduraEquipada() || e == p.getBotasEquipadas();
                System.out.println((i + 1) + " - " + e.descricao() + (equipado ? " - EQUIPADO" : ""));
            }
            System.out.println("0 - Voltar");
            int op = Entrada.lerInt("Equipamento: ");
            if (op == 0) return;
            if (op < 1 || op > p.getInventario().getEquipamentos().size()) {
                System.out.println(Cores.aviso("Equipamento invalido."));
                continue;
            }
            Equipamento e = p.getInventario().getEquipamentos().get(op - 1);
            p.equipar(e);
            System.out.println(Cores.sucesso(e.getNome() + " equipado em " + p.getNome() + "."));
            return;
        }
    }

    private static Personagem escolherPersonagem(Personagem[] grupo) {
        System.out.println("\nEscolha o personagem:");
        for (int i = 0; i < grupo.length; i++) System.out.println((i + 1) + " - " + grupo[i].getNome());
        System.out.println("0 - Voltar");
        int op = Entrada.lerInt("Escolha: ");
        if (op == 0) return null;
        if (op < 1 || op > grupo.length) {
            System.out.println(Cores.aviso("Personagem invalido."));
            return null;
        }
        return grupo[op - 1];
    }

    private static void equipe(Personagem[] grupo) {
        for (Personagem p : grupo) p.status();
        Entrada.enter();
    }

    private static void inventario(Personagem[] grupo) {
        Personagem p = escolherPersonagem(grupo);
        if (p != null) {
            p.getInventario().mostrar(p);
            Entrada.enter();
        }
    }
}
