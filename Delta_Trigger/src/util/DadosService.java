package util;

import model.*;
import java.io.*;
import java.util.*;

public final class DadosService {
    private static final Map<String,String[]> MONSTROS = new HashMap<String,String[]>();
    private static final Map<String,String[]> EQUIPAMENTOS = new LinkedHashMap<String,String[]>();
    private static final Map<String,String[]> ITENS = new LinkedHashMap<String,String[]>();
    private DadosService() {}

    public static void carregarDados() { carregarMonstros(); carregarItens(); }

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

    public static Inimigo criarInimigo(String id){
        String[]p=MONSTROS.get(id);
        if(p==null){if("LOBINHO".equals(id))return LoboFactory.criarLobinho();if("LOBO".equals(id))return LoboFactory.criarLobo();return null;}
        return new Inimigo(p[1],Integer.parseInt(p[2]),Integer.parseInt(p[3]),Integer.parseInt(p[4]),Integer.parseInt(p[5]),Integer.parseInt(p[6]),Integer.parseInt(p[7]));
    }

    public static Item criarItem(String id) {
        String[] p=ITENS.get(id.toUpperCase());
        if(p==null) return null;
        return new Item(p[1],p[2],Integer.parseInt(p[3]),Integer.parseInt(p[4]),Integer.parseInt(p[5]));
    }

    public static List<Item> itensDaLoja() {
        List<Item> lista=new ArrayList<Item>();
        for(String[]p:ITENS.values()) lista.add(new Item(p[1],p[2],Integer.parseInt(p[3]),Integer.parseInt(p[4]),Integer.parseInt(p[5])));
        return lista;
    }

    public static Equipamento criarEquipamento(String id){
        String[]p=EQUIPAMENTOS.get(id.toUpperCase());
        if(p==null) return null;
        return equipamento(p);
    }

    public static List<Equipamento> equipamentosDaLoja() {
        List<Equipamento> lista=new ArrayList<Equipamento>();
        for(String[]p:EQUIPAMENTOS.values()) lista.add(equipamento(p));
        return lista;
    }

    public static Equipamento equipamentoPorNome(String nome) {
        if(nome==null||"-".equals(nome)) return null;
        for(String[]p:EQUIPAMENTOS.values()) if(p[2].equalsIgnoreCase(nome)) return equipamento(p);
        return null;
    }

    private static Equipamento equipamento(String[]p){
        return new Equipamento(p[2],p[3],p[4],p[5],Integer.parseInt(p[6]),Integer.parseInt(p[7]),Integer.parseInt(p[8]),Integer.parseInt(p[9]),Integer.parseInt(p[10]),Integer.parseInt(p[11]),Integer.parseInt(p[12]));
    }
}
