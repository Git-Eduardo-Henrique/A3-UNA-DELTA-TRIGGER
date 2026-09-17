package util;

import model.*;
import java.io.*;
import java.util.*;

public final class DadosService {
 private static final Map<String,String[]> MONSTROS=new HashMap<String,String[]>();
 private DadosService(){}
 public static void carregarMonstros(){
  File f=new File("data/monstros.txt");if(!f.exists())return;
  try{BufferedReader br=new BufferedReader(new FileReader(f));String l;while((l=br.readLine())!=null){if(l.trim().isEmpty()||l.startsWith("#"))continue;String[]p=l.split("\\|",-1);if(p.length>=8)MONSTROS.put(p[0],p);}br.close();}catch(Exception e){System.out.println(Cores.aviso("Nao foi possivel carregar monstros.txt."));}
 }
 public static Inimigo criarInimigo(String id){String[]p=MONSTROS.get(id);if(p==null){if("LOBINHO".equals(id))return LoboFactory.criarLobinho();if("LOBO".equals(id))return LoboFactory.criarLobo();return null;}if("FENROK".equals(id))return new Fenrok();return new Inimigo(p[1],Integer.parseInt(p[2]),Integer.parseInt(p[3]),Integer.parseInt(p[4]),Integer.parseInt(p[5]),Integer.parseInt(p[6]),Integer.parseInt(p[7]));}
 public static Equipamento criarEquipamento(String id){if("MANTO_PELAGEM_ALFA".equals(id))return new Equipamento("Manto da Pelagem Alfa","Armadura","Epico",0,0,3,0,1,0,1);if("BOTAS_PELAGEM_ALFA".equals(id))return new Equipamento("Botas da Pelagem Alfa","Botas","Raro",0,0,0,0,0,2,1);return null;}
}
