package model;
import java.util.ArrayList;
import java.util.List;

public class Inventario {
    private List<Item> itens = new ArrayList<>();
    private List<Equipamento> equipamentos = new ArrayList<>();

    public void adicionarItem(Item item) { itens.add(item); }
    public void adicionarEquipamento(Equipamento e) { equipamentos.add(e); }
    public boolean removerItem(Item item) { return itens.remove(item); }

    public List<Item> getItens() { return itens; }
    public List<Equipamento> getEquipamentos() { return equipamentos; }

    public void mostrar() {
        System.out.println("\n=== INVENTARIO ===");
        if (itens.isEmpty()) System.out.println("Nenhum item.");
        for (int i = 0; i < itens.size(); i++)
            System.out.println((i + 1) + " - " + itens.get(i).descricao());

        if (!equipamentos.isEmpty()) {
            System.out.println("Equipamentos:");
            for (Equipamento e : equipamentos) System.out.println("- " + e.descricao());
        }
    }
}
