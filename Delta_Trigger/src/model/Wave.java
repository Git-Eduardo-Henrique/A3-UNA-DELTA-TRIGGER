package model;
import java.util.ArrayList;import java.util.List;
public class Wave { private final int numero;private final List<String> inimigos=new ArrayList<String>();private final String mensagem; public Wave(int numero,String mensagem){this.numero=numero;this.mensagem=mensagem;}public int getNumero(){return numero;}public String getMensagem(){return mensagem;}public void adicionarInimigo(String id,int quantidade){for(int i=0;i<quantidade;i++)inimigos.add(id);}public List<String> getInimigos(){return inimigos;} }
