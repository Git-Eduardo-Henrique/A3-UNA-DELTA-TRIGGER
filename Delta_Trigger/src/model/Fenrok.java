// ============================================================
// ARQUIVO: Fenrok.java
// Representa o chefe "Fenrok, Lobo Alfa" (ultima onda da floresta).
// Herda de Inimigo e garante recompensas melhores ao ser derrotado.
// ============================================================

package model;

import java.util.ArrayList;
import java.util.List;

public class Fenrok extends Inimigo {

    // Construtor: cria o chefe com atributos mais fortes.
    // Ordem: nome, vida, ataque, defesa, velocidade, sorte, recompensa.
    public Fenrok() {
        super("Fenrok, Lobo Alfa", 110, 18, 8, 10, 6, 50);
    }

    // Define os itens que o chefe sempre deixa cair (drops).
    public List<Item> sortearDrops() {
        List<Item> d = new ArrayList<Item>();
        d.add(new Item("Pocao de Vida", "Pocao", 15, 30, 0, 2)); // 2 pocoes de vida.
        d.add(new Item("Pocao de Mana", "Pocao", 18, 0, 20, 2)); // 2 pocoes de mana.
        return d;
    }

    // Define o material especial que o chefe deixa cair.
    public Item sortearMaterial() {
        return new Item("Chifre de Alfa", "Material", 20, 0, 0);
    }
}
