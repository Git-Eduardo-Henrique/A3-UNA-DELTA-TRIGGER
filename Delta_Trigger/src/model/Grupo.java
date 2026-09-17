package model;

import java.util.ArrayList;
import java.util.List;

public class Grupo {
    private final List<Personagem> personagens = new ArrayList<Personagem>();
    private final Inventario inventario = new Inventario();
    private int ouro;
    private String lider;

    public Grupo(int ouroInicial, String lider) { this.ouro = Math.max(0, ouroInicial); this.lider = lider; }
    public void adicionar(Personagem p) { if (p != null && !personagens.contains(p)) personagens.add(p); }
    public List<Personagem> getPersonagens(){return personagens;}
    public Inventario getInventario(){return inventario;}
    public int getOuro(){return ouro;}
    public void adicionarOuro(int v){if(v>0) ouro+=v;}
    public boolean gastarOuro(int v){if(v<0||ouro<v)return false; ouro-=v; return true;}
    public String getLider(){return lider;}
    public void definirLider(String nome){ for(Personagem p:personagens) if(p.getNome().equalsIgnoreCase(nome)){ lider=p.getNome(); return; } }
    public Personagem getLiderPersonagem(){ for(Personagem p:personagens) if(p.getNome().equalsIgnoreCase(lider)) return p; return personagens.isEmpty()?null:personagens.get(0); }
    public boolean todosDerrotados(){ if(personagens.isEmpty()) return true; for(Personagem p:personagens) if(p.vivo()) return false; return true; }
    public Personagem encontrar(String nome){ for(Personagem p:personagens) if(p.getNome().equalsIgnoreCase(nome)) return p; return null; }
}
