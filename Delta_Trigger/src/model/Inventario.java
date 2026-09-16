package model;

import java.util.ArrayList;
import java.util.List;

public class Inventario {
    private final List<Item> itens = new ArrayList<Item>();
    private final List<Equipamento> equipamentos = new ArrayList<Equipamento>();

    public void adicionarItem(Item novo) {
        if (novo == null) return;
        for (Item item : itens) {
            if (item.getNome().equalsIgnoreCase(novo.getNome())) {
                item.adicionarQuantidade(novo.getQuantidade());
                return;
            }
        }
        itens.add(novo);
    }

    public void removerUmaUnidade(Item item) {
        if (item == null) return;
        if (item.removerUma() && item.getQuantidade() <= 0) itens.remove(item);
    }

    public void adicionarEquipamento(Equipamento equipamento) {
        if (equipamento != null) equipamentos.add(equipamento);
    }

    public List<Item> getItens() { return itens; }
    public List<Equipamento> getEquipamentos() { return equipamentos; }

    public Item encontrarItem(String nome) {
        for (Item item : itens) if (item.getNome().equalsIgnoreCase(nome)) return item;
        return null;
    }

    public Equipamento encontrarEquipamento(String nome) {
        for (Equipamento e : equipamentos) if (e.getNome().equalsIgnoreCase(nome)) return e;
        return null;
    }

    public void mostrar(Personagem personagem) {
        System.out.println("\n=== ITENS ===");
        if (itens.isEmpty()) System.out.println("Nenhum item.");
        for (Item item : itens) System.out.println("- " + item.descricao());
        System.out.println("=== EQUIPAMENTOS ===");
        if (equipamentos.isEmpty()) System.out.println("Nenhum equipamento.");
        for (Equipamento e : equipamentos) {
            boolean equipado = personagem != null && (e == personagem.getArmaEquipada() || e == personagem.getArmaduraEquipada() || e == personagem.getBotasEquipadas());
            System.out.println("- " + e.descricao() + (equipado ? " - EQUIPADO" : ""));
        }
    }

    public void mostrar() { mostrar(null); }
}
