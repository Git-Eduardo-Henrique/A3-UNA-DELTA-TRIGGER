public class Suporte extends Personagem {

    public Suporte(String nome) {
        super(nome, 90, 60, 12, 10, 12);
    }

    public void curar(Personagem aliado) {
        int custo = 8;

        if (getMana() < custo) {
            System.out.println("Mana insuficiente!");
            return;
        }

        gastarMana(custo);
        aliado.recuperarVida(25);

        System.out.println(getNome() + " usou Cura!");
        System.out.println(aliado.getNome() + " recuperou vida.");
    }
}
