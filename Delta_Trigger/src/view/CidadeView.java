package view;
import model.*;
import util.SaveService;
import java.util.List;

public class CidadeView {
    public static void abrir(Personagem[] grupo) {
        boolean sair = false;
        while (!sair) {
            System.out.println("\n========== ELDORIA ==========");
            System.out.println("1 - Loja");
            System.out.println("2 - Vender item");
            System.out.println("3 - Equipar");
            System.out.println("4 - Estalagem");
            System.out.println("5 - Status");
            System.out.println("6 - Inventario");
            System.out.println("7 - Distribuir pontos");
            System.out.println("8 - Salvar");
            System.out.println("9 - Continuar");

            int op = Entrada.lerInt("Escolha: ");
            switch (op) {
                case 1: loja(grupo); break;
                case 2: vender(grupo); break;
                case 3: equipar(grupo); break;
                case 4:
                    for (Personagem p : grupo) if (p.vivo()) p.recuperarTudo();
                    System.out.println("O grupo descansou na estalagem.");
                    break;
                case 5:
                    for (Personagem p : grupo) p.status();
                    break;
                case 6:
                    for (Personagem p : grupo) p.getInventario().mostrar();
                    break;
                case 7: distribuir(grupo); break;
                case 8: SaveService.salvar(grupo); break;
                case 9: sair = true; break;
                default: System.out.println("Opcao invalida.");
            }
        }
    }

    private static void loja(Personagem[] grupo) {
        Equipamento espada = new Equipamento("Espada de Ferro", "Arma", 35, 2,0,0,0,0,0);
        Equipamento armadura = new Equipamento("Armadura de Couro", "Armadura", 30,0,2,0,1,0,0);
        Equipamento cajado = new Equipamento("Cajado Simples", "Arma", 35,0,0,2,0,0,0);
        Item hp = new Item("Pocao de Vida", "Pocao", 15,30,0);
        Item mp = new Item("Pocao de Mana", "Pocao", 18,0,20);

        System.out.println("\n1 - Espada ($35)  2 - Armadura ($30)  3 - Cajado ($35)");
        System.out.println("4 - Pocao de Vida ($15)  5 - Pocao de Mana ($18)");
        int produto = Entrada.lerInt("Produto: ");
        int personagem = Entrada.lerInt("1 - Kael | 2 - Lyra: ");
        if (personagem < 1 || personagem > 2) return;
        Personagem p = grupo[personagem - 1];

        if (produto == 1) comprarEquipamento(p, espada);
        else if (produto == 2) comprarEquipamento(p, armadura);
        else if (produto == 3) comprarEquipamento(p, cajado);
        else if (produto == 4) comprarItem(p, hp);
        else if (produto == 5) comprarItem(p, mp);
    }

    private static void comprarEquipamento(Personagem p, Equipamento e) {
        if (!p.gastarDinheiro(e.getPreco())) { System.out.println("Dinheiro insuficiente."); return; }
        p.getInventario().adicionarEquipamento(e);
        p.equipar(e);
    }

    private static void comprarItem(Personagem p, Item i) {
        if (!p.gastarDinheiro(i.getPreco())) { System.out.println("Dinheiro insuficiente."); return; }
        p.getInventario().adicionarItem(i);
        System.out.println("Comprado: " + i.getNome());
    }

    private static void vender(Personagem[] grupo) {
        int n = Entrada.lerInt("1 - Kael | 2 - Lyra: ");
        if (n < 1 || n > 2) return;
        Personagem p = grupo[n - 1];
        List<Item> itens = p.getInventario().getItens();
        if (itens.isEmpty()) { System.out.println("Nenhum item para vender."); return; }
        for (int i = 0; i < itens.size(); i++) System.out.println((i+1) + " - " + itens.get(i).descricao());
        int op = Entrada.lerInt("Item: ");
        if (op < 1 || op > itens.size()) return;
        Item item = itens.get(op - 1);
        p.adicionarDinheiro(Math.max(1, item.getPreco()/2));
        itens.remove(item);
        System.out.println("Item vendido.");
    }

    private static void equipar(Personagem[] grupo) {
        int n = Entrada.lerInt("1 - Kael | 2 - Lyra: ");
        if (n < 1 || n > 2) return;
        Personagem p = grupo[n - 1];
        List<Equipamento> lista = p.getInventario().getEquipamentos();
        if (lista.isEmpty()) { System.out.println("Nenhum equipamento."); return; }
        for (int i=0;i<lista.size();i++) System.out.println((i+1)+" - "+lista.get(i).descricao());
        int op = Entrada.lerInt("Equipamento: ");
        if (op>=1 && op<=lista.size()) p.equipar(lista.get(op-1));
    }

    private static void distribuir(Personagem[] grupo) {
        for (Personagem p : grupo) {
            while (p.getPontosAtributo() > 0) {
                p.status();
                System.out.println("1 Forca | 2 Defesa | 3 Inteligencia | 4 Resistencia | 5 Velocidade | 6 Sorte");
                p.distribuirPonto(Entrada.lerInt("Atributo para " + p.getNome() + ": "));
            }
        }
    }
}
