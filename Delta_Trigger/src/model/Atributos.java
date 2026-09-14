package model;

public class Atributos {
    private int forca, defesa, inteligencia, resistencia, velocidade, sorte;

    public Atributos(int forca, int defesa, int inteligencia, int resistencia, int velocidade, int sorte) {
        this.forca = forca; this.defesa = defesa; this.inteligencia = inteligencia;
        this.resistencia = resistencia; this.velocidade = velocidade; this.sorte = sorte;
    }

    public void aumentar(int opcao) {
        switch (opcao) {
            case 1: forca++; break;
            case 2: defesa++; break;
            case 3: inteligencia++; break;
            case 4: resistencia++; break;
            case 5: velocidade++; break;
            case 6: sorte++; break;
            default: System.out.println("Opcao invalida.");
        }
    }

    public int getForca() { return forca; }
    public int getDefesa() { return defesa; }
    public int getInteligencia() { return inteligencia; }
    public int getResistencia() { return resistencia; }
    public int getVelocidade() { return velocidade; }
    public int getSorte() { return sorte; }

    public void mostrar() {
        System.out.println("Forca: " + forca + " | Defesa: " + defesa
                + " | Inteligencia: " + inteligencia);
        System.out.println("Resistencia: " + resistencia + " | Velocidade: "
                + velocidade + " | Sorte: " + sorte);
    }
}
