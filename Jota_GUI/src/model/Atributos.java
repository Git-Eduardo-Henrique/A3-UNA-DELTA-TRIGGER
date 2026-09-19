package model;

public class Atributos {
    private int forca;
    private int defesa;
    private int inteligencia;
    private int resistencia;
    private int velocidade;
    private int sorte;

    public Atributos(int forca, int defesa, int inteligencia, int resistencia, int velocidade, int sorte) {
        this.forca = Math.max(0, forca);
        this.defesa = Math.max(0, defesa);
        this.inteligencia = Math.max(0, inteligencia);
        this.resistencia = Math.max(0, resistencia);
        this.velocidade = Math.max(0, velocidade);
        this.sorte = Math.max(0, sorte);
    }

    public boolean aumentar(int opcao) {
        switch (opcao) {
            case 1: forca++; return true;
            case 2: defesa++; return true;
            case 3: inteligencia++; return true;
            case 4: resistencia++; return true;
            case 5: velocidade++; return true;
            case 6: sorte++; return true;
            default: return false;
        }
    }

    public int getForca() { return forca; }
    public int getDefesa() { return defesa; }
    public int getInteligencia() { return inteligencia; }
    public int getResistencia() { return resistencia; }
    public int getVelocidade() { return velocidade; }
    public int getSorte() { return sorte; }

    public String salvar() {
        return forca + "," + defesa + "," + inteligencia + "," + resistencia + "," + velocidade + "," + sorte;
    }

    public void mostrar() {
        System.out.println("Forca: " + forca + " | Defesa: " + defesa + " | Inteligencia: " + inteligencia);
        System.out.println("Resistencia: " + resistencia + " | Velocidade: " + velocidade + " | Sorte: " + sorte);
    }
}
