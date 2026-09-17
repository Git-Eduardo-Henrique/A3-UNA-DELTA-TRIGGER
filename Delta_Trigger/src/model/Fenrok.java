package model;
import java.util.ArrayList; import java.util.List;
public class Fenrok extends Inimigo {
 public Fenrok(){super("Fenrok, Lobo Alfa",110,18,8,10,6,50);}
 public List<Item> sortearDrops(){List<Item>d=new ArrayList<Item>();d.add(new Item("Pocao de Vida","Pocao",15,30,0,2));d.add(new Item("Pocao de Mana","Pocao",18,0,20,2));return d;}
 public Item sortearMaterial(){return new Item("Chifre de Alfa","Material",20,0,0);}
}
