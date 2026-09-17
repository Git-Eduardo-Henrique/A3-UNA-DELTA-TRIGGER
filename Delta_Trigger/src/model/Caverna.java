package model;
import java.util.ArrayList;import java.util.List;
public class Caverna extends Cenario { private final List<Wave> waves=new ArrayList<Wave>(); public Caverna(String id,String nome,String descricao){super(id,nome,descricao);} public String getTipo(){return "Caverna";} public void adicionarWave(Wave w){waves.add(w);}public List<Wave> getWaves(){return waves;} }
