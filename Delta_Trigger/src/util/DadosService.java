// ============================================================
// ARQUIVO: DadosService.java
// Le os arquivos de dados (data/monstros.txt e data/items.txt) e
// transforma as linhas em objetos do jogo (inimigos, itens e
// equipamentos). Assim, para mudar valores do jogo nao e preciso
// mexer no codigo, so nos arquivos de texto.
// ============================================================

package util;

import model.*;
import java.io.*;
import java.util.*;

public final class DadosService {
    // Mapas que guardam os dados lidos, usando o ID como chave.
    private static final Map<String,String[]> MONSTROS = new HashMap<String,String[]>();
    private static final Map<String,String[]> EQUIPAMENTOS = new LinkedHashMap<String,String[]>();
    private static final Map<String,String[]> ITENS = new LinkedHashMap<String,String[]>();

    // Construtor privado: a classe so tem metodos estaticos.
    private DadosService() {}

    // Carrega todos os dados necessarios para o jogo.
    public static void carregarDados() { carregarMonstros(); carregarItens(); }

    // Le o arquivo data/monstros.txt e guarda os monstros.
    public static void carregarMonstros() {
        MONSTROS.clear();
        File f=new File("data/monstros.txt"); if(!f.exists()) return;
        try(BufferedReader br=new BufferedReader(new FileReader(f))){
            String l; while((l=br.readLine())!=null){
                if(l.trim().isEmpty()||l.startsWith("#")) continue;
                String[] p=l.split("\\|",-1); if(p.length>=8) MONSTROS.put(p[0],p);
            }
        }catch(Exception e){System.out.println(Cores.aviso("Nao foi possivel carregar monstros.txt."));}
    }

    // Le o arquivo data/items.txt e separa itens e equipamentos.
    private static void carregarItens() {
        ITENS.clear(); EQUIPAMENTOS.clear();
        File f=new File("data/items.txt"); if(!f.exists()) return;
        try(BufferedReader br=new BufferedReader(new FileReader(f))){
            String l; while((l=br.readLine())!=null){
                if(l.trim().isEmpty()||l.startsWith("#")) continue;
                String[] p=l.split("\\|",-1);
                if("ITEM".equalsIgnoreCase(p[0])&&p.length>=6) ITENS.put(p[1].toUpperCase(),p);
                else if("EQUIP".equalsIgnoreCase(p[0])&&p.length>=13) EQUIPAMENTOS.put(p[1].toUpperCase(),p);
            }
        }catch(Exception e){System.out.println(Cores.aviso("Nao foi possivel carregar items.txt."));}
    }

    // Cria um inimigo pelo ID. Se nao achar no arquivo, usa os
    // lobos prontos da LoboFactory como plano B.
    public static Inimigo criarInimigo(String id){
        String[]p=MONSTROS.get(id);
        if(p==null){if("LOBINHO".equals(id))return LoboFactory.criarLobinho();if("LOBO".equals(id))return LoboFactory.criarLobo();return null;}
        return new Inimigo(p[1],Integer.parseInt(p[2]),Integer.parseInt(p[3]),Integer.parseInt(p[4]),Integer.parseInt(p[5]),Integer.parseInt(p[6]),Integer.parseInt(p[7]));
    }

    // Cria um item pelo ID (retorna null se nao existir).
    public static Item criarItem(String id) {
        String[] p=ITENS.get(id.toUpperCase());
        if(p==null) return null;
        return new Item(p[1],p[2],Integer.parseInt(p[3]),Integer.parseInt(p[4]),Integer.parseInt(p[5]));
    }

    // Devolve a lista de todos os itens que a loja vende.
    public static List<Item> itensDaLoja() {
        List<Item> lista=new ArrayList<Item>();
        for(String[]p:ITENS.values()) lista.add(new Item(p[1],p[2],Integer.parseInt(p[3]),Integer.parseInt(p[4]),Integer.parseInt(p[5])));
        return lista;
    }

    // Cria um equipamento pelo ID (retorna null se nao existir).
    public static Equipamento criarEquipamento(String id){
        String[]p=EQUIPAMENTOS.get(id.toUpperCase());
        if(p==null) return null;
        return equipamento(p);
    }

    // Devolve a lista de todos os equipamentos que a loja vende.
    public static List<Equipamento> equipamentosDaLoja() {
        List<Equipamento> lista=new ArrayList<Equipamento>();
        for(String[]p:EQUIPAMENTOS.values()) lista.add(equipamento(p));
        return lista;
    }

    // Procura um equipamento pelo nome (usado ao carregar o save).
    public static Equipamento equipamentoPorNome(String nome) {
        if(nome==null||"-".equals(nome)) return null;
        for(String[]p:EQUIPAMENTOS.values()) if(p[2].equalsIgnoreCase(nome)) return equipamento(p);
        return null;
    }

    // Converte uma linha do arquivo em um objeto Equipamento.
    private static Equipamento equipamento(String[]p){
        return new Equipamento(p[2],p[3],p[4],p[5],Integer.parseInt(p[6]),Integer.parseInt(p[7]),Integer.parseInt(p[8]),Integer.parseInt(p[9]),Integer.parseInt(p[10]),Integer.parseInt(p[11]),Integer.parseInt(p[12]));
    }
}
