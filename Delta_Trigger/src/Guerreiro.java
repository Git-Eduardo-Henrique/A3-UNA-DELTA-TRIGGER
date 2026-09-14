public class Guerreiro extends Personagem {

    public Guerreiro(String nome) {
        super(nome, 120, 30, 25, 15, 10);
    }

    public void golpeForte(Inimigo inimigo) {
        int custo = 5;

        if (getMana() < custo) {
            System.out.println("Mana insuficiente!");
            return;
        }

        gastarMana(custo);

        int dano = Math.max(1, (getAtaque() * 2) - inimigo.getDefesa());
        inimigo.receberDano(dano);

        System.out.println(getNome() + " usou Golpe Forte!");
        System.out.println("Dano causado: " + dano);
    }
}
