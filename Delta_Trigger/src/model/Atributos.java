// ============================================================
// ARQUIVO: Atributos.java
// Guarda os seis atributos de um personagem:
// forca, defesa, inteligencia, resistencia, velocidade e sorte.
// Sao esses numeros que definem o quao forte o personagem e.
// ============================================================

package model;

public class Atributos {
    // Os seis atributos do personagem.
    private int forca, defesa, inteligencia, resistencia, velocidade, sorte;

    // Construtor: guarda os valores recebidos.
    // Math.max(0, ...) garante que nenhum atributo fique negativo.
    public Atributos(int forca, int defesa, int inteligencia, int resistencia, int velocidade, int sorte) {
        this.forca = Math.max(0, forca);
        this.defesa = Math.max(0, defesa);
        this.inteligencia = Math.max(0, inteligencia);
        this.resistencia = Math.max(0, resistencia);
        this.velocidade = Math.max(0, velocidade);
        this.sorte = Math.max(0, sorte);
    }

    // Aumenta um atributo em +1 de acordo com a opcao escolhida.
    // Retorna true se a opcao era valida e false se nao era.
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

    // Metodos que devolvem (get) o valor de cada atributo.
    public int getForca() { return forca; }
    public int getDefesa() { return defesa; }
    public int getInteligencia() { return inteligencia; }
    public int getResistencia() { return resistencia; }
    public int getVelocidade() { return velocidade; }
    public int getSorte() { return sorte; }

    // Transforma os atributos em um texto separado por virgulas.
    // Esse texto e usado para gravar e carregar o jogo (save).
    public String salvar() {
        return forca + "," + defesa + "," + inteligencia + "," + resistencia + "," + velocidade + "," + sorte;
    }
}
