// ============================================================
// ARQUIVO: Grupo.java
// Representa o grupo de aventureiros do jogador.
// Guarda os personagens, o inventario compartilhado, o ouro
// e quem e o lider do grupo.
// ============================================================

package model;

import java.util.ArrayList;
import java.util.List;

public class Grupo {
    // Lista de personagens que fazem parte do grupo.
    private final List<Personagem> personagens = new ArrayList<Personagem>();
    // Inventario que todos os personagens compartilham.
    private final Inventario inventario = new Inventario();
    private int ouro;       // Dinheiro do grupo.
    private String lider;   // Nome do lider.

    // Construtor: cria o grupo com um ouro inicial e um lider.
    public Grupo(int ouroInicial, String lider) { this.ouro = Math.max(0, ouroInicial); this.lider = lider; }

    // Adiciona um personagem ao grupo (sem repetir o mesmo).
    public void adicionar(Personagem p) { if (p != null && !personagens.contains(p)) personagens.add(p); }
    // Metodos que devolvem as listas e informacoes do grupo.
    public List<Personagem> getPersonagens(){return personagens;}
    public Inventario getInventario(){return inventario;}
    public int getOuro(){return ouro;}

    // Soma ouro ao grupo (apenas valores positivos).
    public void adicionarOuro(int v){if(v>0) ouro+=v;}

    // Tenta gastar ouro. Retorna false se o valor for invalido ou
    // se o grupo nao tiver ouro suficiente.
    public boolean gastarOuro(int v){if(v<0||ouro<v)return false; ouro-=v; return true;}

    public String getLider(){return lider;}

    // Define o lider apenas se existir um personagem com esse nome.
    public void definirLider(String nome){ for(Personagem p:personagens) if(p.getNome().equalsIgnoreCase(nome)){ lider=p.getNome(); return; } }

    // Devolve o objeto Personagem que representa o lider.
    public Personagem getLiderPersonagem(){ for(Personagem p:personagens) if(p.getNome().equalsIgnoreCase(lider)) return p; return personagens.isEmpty()?null:personagens.get(0); }

    // Diz se todos os personagens foram derrotados (sem vida).
    public boolean todosDerrotados(){ if(personagens.isEmpty()) return true; for(Personagem p:personagens) if(p.vivo()) return false; return true; }

    // Procura um personagem pelo nome. Retorna null se nao encontrar.
    public Personagem encontrar(String nome){ for(Personagem p:personagens) if(p.getNome().equalsIgnoreCase(nome)) return p; return null; }
}
