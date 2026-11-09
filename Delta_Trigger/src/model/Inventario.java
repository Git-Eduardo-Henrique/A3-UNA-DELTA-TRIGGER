// ============================================================
// ARQUIVO: Inventario.java
// Guarda os itens e equipamentos do grupo. Cuida de adicionar,
// remover, encontrar e mostrar tudo o que o grupo carrega.
// ============================================================

package model;

import java.util.ArrayList;
import java.util.List;

public class Inventario {
    // Listas com os itens e os equipamentos do grupo.
    private final List<Item> itens = new ArrayList<Item>();
    private final List<Equipamento> equipamentos = new ArrayList<Equipamento>();

    // Adiciona um item. Se ja existir um item com o mesmo nome,
    // apenas soma a quantidade em vez de criar uma linha nova.
    public void adicionarItem(Item novo) {
        if (novo == null) return;
        Item existente = encontrarItem(novo.getNome());
        if (existente != null) existente.adicionarQuantidade(novo.getQuantidade());
        else itens.add(novo);
    }

    // Remove uma unidade de um item. Se a quantidade chegar a zero,
    // o item sai da lista do inventario.
    public boolean removerUmaUnidade(Item item) {
        if (item == null || !item.removerUma()) return false;
        if (item.getQuantidade() == 0) itens.remove(item);
        return true;
    }

    // Remove varias unidades de um item (usado ao vender).
    public boolean removerQuantidade(Item item, int quantidade) {
        if (item == null || !item.removerQuantidade(quantidade)) return false;
        if (item.getQuantidade() == 0) itens.remove(item);
        return true;
    }

    // Guarda um equipamento (sem duplicar o mesmo objeto).
    public void adicionarEquipamento(Equipamento e) {
        if (e != null && !equipamentos.contains(e)) equipamentos.add(e);
    }

    // Remove um equipamento e diz se ele existia.
    public boolean removerEquipamento(Equipamento e) { return e != null && equipamentos.remove(e); }

    // Diz se o inventario possui determinado equipamento.
    public boolean possuiEquipamento(Equipamento e) { return e != null && equipamentos.contains(e); }

    // Metodos que devolvem (get) as listas do inventario.
    public List<Item> getItens(){return itens;}
    public List<Equipamento> getEquipamentos(){return equipamentos;}

    // Procura um item pelo nome (sem diferenciar maiusculas/minusculas).
    public Item encontrarItem(String nome){for(Item i:itens) if(i.getNome().equalsIgnoreCase(nome)) return i; return null;}

    // Procura um equipamento pelo nome.
    public Equipamento encontrarEquipamento(String nome){for(Equipamento e:equipamentos) if(e.getNome().equalsIgnoreCase(nome)) return e; return null;}

    // Mostra na tela todos os itens e equipamentos do inventario.
    public void mostrar(){
        System.out.println("\n=== INVENTARIO DO GRUPO ===");
        System.out.println("Itens:");
        if(itens.isEmpty()) System.out.println("Nenhum item."); else for(Item i:itens) System.out.println("- "+i.descricao());
        System.out.println("Equipamentos:");
        if(equipamentos.isEmpty()) System.out.println("Nenhum equipamento."); else for(Equipamento e:equipamentos) System.out.println("- "+e.descricao());
    }
}
