// ============================================================
// ARQUIVO: SaveService.java
// Cuida de salvar e carregar o progresso do jogador no arquivo
// save.txt. O conteudo e criptografado (AES) para nao poder ser
// lido/editado facilmente. Tambem guarda a classe DadosSave, que
// carrega tudo o que foi salvo.
// ============================================================

package util;

import model.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.security.*;
import java.util.*;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public final class SaveService {
    // Nome do arquivo de save, senha usada na criptografia e versao do save.
    private static final String ARQUIVO="save.txt", CHAVE="DeltaTrigger2026", VERSAO="DELTA_FINAL";

    // Construtor privado: a classe so tem metodos estaticos.
    private SaveService(){}

    // Diz se o arquivo de save existe.
    public static boolean existeSave(){return new File(ARQUIVO).exists();}

    // Salva todo o estado do jogo no arquivo (criptografado).
    // Retorna true se conseguiu salvar.
    public static boolean salvar(Grupo grupo,String local,int waveFloresta,boolean elyraEntrou,boolean elyraRecusada){
        try{
            // Vamos montando o texto do save linha por linha.
            StringBuilder d=new StringBuilder();
            d.append(VERSAO).append("\n"); // Primeira linha: versao do save.
            d.append("STATE|").append(local).append("|").append(waveFloresta).append("|").append(elyraEntrou).append("|").append(elyraRecusada).append("\n");
            // Linha do grupo: lider e ouro.
            d.append("GROUP|").append(grupo.getLider()).append("|").append(grupo.getOuro()).append("\n");
            // Para cada personagem: dados basicos e equipamentos equipados.
            for(Personagem p:grupo.getPersonagens()){
                d.append("P|").append(p.classe()).append("|").append(p.getNivel()).append("|").append(p.getVidaMaxima()).append("|").append(p.getVida()).append("|").append(p.getManaMaxima()).append("|").append(p.getMana()).append("|").append(p.getPontosAtributo()).append("|").append(p.getExperiencia()).append("|").append(p.getAtributosBase().salvar()).append("\n");
                salvarEquipamento(d,"W",p.getNome(),p.getArmaEquipada());
                salvarEquipamento(d,"A",p.getNome(),p.getArmaduraEquipada());
                salvarEquipamento(d,"B",p.getNome(),p.getBotasEquipadas());
                salvarEquipamento(d,"C",p.getNome(),p.getAcessorioEquipado());
            }
            // Itens do inventario (linhas que comecam com "I").
            for(Item it:grupo.getInventario().getItens())d.append("I|").append(it.getNome()).append("|").append(it.getTipo()).append("|").append(it.getPreco()).append("|").append(it.getCuraHp()).append("|").append(it.getCuraMana()).append("|").append(it.getQuantidade()).append("\n");
            // Equipamentos guardados no inventario (linhas "E").
            for(Equipamento e:grupo.getInventario().getEquipamentos())salvarEquipamento(d,"E","-",e);
            // Criptografa o texto e grava no arquivo save.txt.
            Files.write(Paths.get(ARQUIVO),criptografar(d.toString()).getBytes(StandardCharsets.UTF_8));
            return true;
        }catch(Exception e){return false;}
    }

    // Escreve uma linha com os dados de um equipamento.
    // O "tipo" indica onde ele estava (W=arma, A=armadura, B=botas,
    // C=acessorio, E=inventario).
    private static void salvarEquipamento(StringBuilder d,String tipo,String personagem,Equipamento e){
        if(e==null)return;
        d.append(tipo).append("|").append(personagem).append("|").append(e.getNome()).append("|").append(e.getTipo()).append("|").append(e.getRaridade()).append("|").append(e.getClassePermitida()).append("|").append(e.getPreco()).append("|").append(e.getForca()).append("|").append(e.getDefesa()).append("|").append(e.getInteligencia()).append("|").append(e.getResistencia()).append("|").append(e.getVelocidade()).append("|").append(e.getSorte()).append("\n");
    }

    // Le e descriptografa o save, recriando o grupo e todo o estado.
    // Retorna null se o save estiver corrompido.
    public static DadosSave carregar(){
        try{
            String enc=new String(Files.readAllBytes(Paths.get(ARQUIVO)),StandardCharsets.UTF_8);
            String t=descriptografar(enc);String[]ls=t.split("\\n");
            // Confere se o save pertence a esta versao do jogo.
            if(ls.length==0||!VERSAO.equals(ls[0]))throw new Exception("versao");
            String lider="Kael",local="ELDORIA";int wave=5,ouro=0;boolean elyra=false,recusada=false,grupoLido=false;
            List<Personagem> ps=new ArrayList<Personagem>(); List<String[]> itens=new ArrayList<String[]>();List<String[]> equips=new ArrayList<String[]>();
            Map<String,String[]> equipados=new LinkedHashMap<String,String[]>();
            // Le cada linha e monta os dados conforme o tipo dela.
            for(String l:ls){
                String[] p=l.split("\\|",-1); if(p.length==0)continue;
                if("STATE".equals(p[0])&&p.length>=5){local=p[1];wave=Integer.parseInt(p[2]);elyra=Boolean.parseBoolean(p[3]);recusada=Boolean.parseBoolean(p[4]);}
                else if("GROUP".equals(p[0])&&p.length>=3){lider=p[1];ouro=Integer.parseInt(p[2]);grupoLido=true;}
                else if("P".equals(p[0])&&p.length>=10){Personagem q=criar(p[1]);q.definirNivel(Integer.parseInt(p[2]));q.definirVidaMaxima(Integer.parseInt(p[3]));q.definirVida(Integer.parseInt(p[4]));q.definirManaMaxima(Integer.parseInt(p[5]));q.definirMana(Integer.parseInt(p[6]));q.definirPontosAtributo(Integer.parseInt(p[7]));q.definirExperiencia(Integer.parseInt(p[8]));restaurarAtributos(q,p[9]);ps.add(q);}
                else if("I".equals(p[0]))itens.add(p);
                else if("E".equals(p[0]))equips.add(p);
                else if(("W".equals(p[0])||"A".equals(p[0])||"B".equals(p[0])||"C".equals(p[0]))&&p.length>=13)equipados.put(p[0]+"|"+p[1],p);
            }
            // Precisa ter lido os dados do grupo e pelo menos 2 personagens.
            if(!grupoLido||ps.size()<2)throw new Exception("dados");
            // Remonta o grupo, o inventario e os equipamentos.
            Grupo g=new Grupo(ouro,lider);for(Personagem q:ps)g.adicionar(q);
            for(String[]p:itens)g.getInventario().adicionarItem(new Item(p[1],p[2],Integer.parseInt(p[3]),Integer.parseInt(p[4]),Integer.parseInt(p[5]),Integer.parseInt(p[6])));
            for(String[]p:equips)g.getInventario().adicionarEquipamento(criarEquipamentoSalvo(p,2));
            for(Personagem q:g.getPersonagens()){
                equiparSalvo(g,q,equipados.get("W|"+q.getNome()));equiparSalvo(g,q,equipados.get("A|"+q.getNome()));equiparSalvo(g,q,equipados.get("B|"+q.getNome()));equiparSalvo(g,q,equipados.get("C|"+q.getNome()));
            }
            g.definirLider(lider);return new DadosSave(g,local,wave,elyra,recusada);
        }catch(Exception e){System.out.println(Cores.aviso("O save esta corrompido ou foi criado por uma versao diferente."));return null;}
    }

    // Equipa no personagem um equipamento que estava salvo.
    private static void equiparSalvo(Grupo g,Personagem p,String[] v){if(v==null)return;Equipamento e=criarEquipamentoSalvo(v,2);if(e!=null)p.equiparDireto(e);}
    // Recria um equipamento a partir dos dados salvos.
    private static Equipamento criarEquipamentoSalvo(String[]p,int inicio){return new Equipamento(p[inicio],p[inicio+1],p[inicio+2],p[inicio+3],Integer.parseInt(p[inicio+4]),Integer.parseInt(p[inicio+5]),Integer.parseInt(p[inicio+6]),Integer.parseInt(p[inicio+7]),Integer.parseInt(p[inicio+8]),Integer.parseInt(p[inicio+9]),Integer.parseInt(p[inicio+10]));}
    // Cria o personagem correto conforme o nome da classe salvo.
    private static Personagem criar(String c){if("Guerreiro".equals(c))return new Guerreiro();if("Suporte".equals(c))return new Suporte();if("Elyra".equals(c))return new Elyra();throw new IllegalArgumentException();}
    // Reaplica os atributos base que o personagem tinha no save.
    private static void restaurarAtributos(Personagem p,String v){String[]a=v.split(",");if(a.length!=6)throw new IllegalArgumentException();int[]at={p.getAtributosBase().getForca(),p.getAtributosBase().getDefesa(),p.getAtributosBase().getInteligencia(),p.getAtributosBase().getResistencia(),p.getAtributosBase().getVelocidade(),p.getAtributosBase().getSorte()};for(int i=0;i<6;i++){int alvo=Integer.parseInt(a[i]);while(at[i]<alvo){p.getAtributosBase().aumentar(i+1);at[i]++;}}}
    // Transforma o texto em dados embaralhados (criptografia AES).
    private static String criptografar(String s)throws Exception{Cipher c=Cipher.getInstance("AES/ECB/PKCS5Padding");c.init(Cipher.ENCRYPT_MODE,chave());return Base64.getEncoder().encodeToString(c.doFinal(s.getBytes(StandardCharsets.UTF_8)));}
    // Faz o caminho inverso: volta os dados embaralhados para texto.
    private static String descriptografar(String s)throws Exception{Cipher c=Cipher.getInstance("AES/ECB/PKCS5Padding");c.init(Cipher.DECRYPT_MODE,chave());return new String(c.doFinal(Base64.getDecoder().decode(s)),StandardCharsets.UTF_8);}
    // Gera a chave de criptografia a partir da senha fixa (SHA-256).
    private static SecretKeySpec chave()throws Exception{byte[]h=MessageDigest.getInstance("SHA-256").digest(CHAVE.getBytes(StandardCharsets.UTF_8));return new SecretKeySpec(Arrays.copyOf(h,16),"AES");}
    // "Caixa" que carrega tudo o que foi lido do save.
    public static class DadosSave{public final Grupo grupo;public final String local;public final int waveFloresta;public final boolean elyraEntrou,elyraRecusada;public DadosSave(Grupo g,String l,int w,boolean e,boolean r){grupo=g;local=l;waveFloresta=w;elyraEntrou=e;elyraRecusada=r;}}
}
