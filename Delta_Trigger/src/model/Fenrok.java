package model;

import java.util.ArrayList;
import java.util.List;

public class Fenrok extends Inimigo {
    public Fenrok() {
        super("Fenrok, Lobo Alfa", 110, 18, 8, 10, 50);
    }

    @Override
    public List<Item> sortearDrops() {
        List<Item> drops = new ArrayList<Item>();
        drops.add(new Item("Pocao de Vida", "Pocao", 15, 30, 0, 2));
        drops.add(new Item("Pocao de Mana", "Pocao", 18, 0, 20, 2));
        return drops;
    }

    @Override
    public Item sortearMaterial() {
        return new Item("Chifre de Alfa", "Material", 20, 0, 0);
    }
}
