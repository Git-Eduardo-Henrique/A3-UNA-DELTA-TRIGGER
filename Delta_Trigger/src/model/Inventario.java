package model;

import java.util.ArrayList;
import java.util.List;

public class Inventario {
    private final List<Item> itens = new ArrayList<Item>();
    private final List<Equipamento> equipamentos = new ArrayList<Equipamento>();
    public void adicionarItem(Item novo) {
        if (novo == null) return;
        Item existente = encontrarItem(novo.getNome());
        if (existente != null) existente.adicionarQuantidade(novo.getQuantidade()); else itens.add(novo);
    }
    public boolean removerUmaUnidade(Item item) { if (item == null || !item.removerUma()) return false; if (item.getQuantidade() == 0) itens.remove(item); return true; }
    public boolean removerQuantidade(Item item, int quantidade) { if (item == null || !item.removerQuantidade(quantidade)) return false; if (item.getQuantidade()==0) itens.remove(item); return true; }
    public void adicionarEquipamento(Equipamento e) { if (e != null) equipamentos.add(e); }
    public List<Item> getItens(){return itens;} public List<Equipamento> getEquipamentos(){return equipamentos;}
    public Item encontrarItem(String nome){ for(Item i:itens) if(i.getNome().equalsIgnoreCase(nome)) return i; return null; }
    public Equipamento encontrarEquipamento(String nome){ for(Equipamento e:equipamentos) if(e.getNome().equalsIgnoreCase(nome)) return e; return null; }
    public void mostrar(){
        System.out.println("\n=== INVENTARIO DO GRUPO ===");
        System.out.println("Itens:"); if(itens.isEmpty()) System.out.println("Nenhum item."); else for(Item i:itens) System.out.println("- "+i.descricao());
        System.out.println("Equipamentos:"); if(equipamentos.isEmpty()) System.out.println("Nenhum equipamento."); else for(Equipamento e:equipamentos) System.out.println("- "+e.descricao());
    }
}
