package model;

public class Guerreiro extends Personagem {
    public Guerreiro() {
        super("Kael", 120, 30, new Atributos(5, 5, 2, 4, 3, 3), 80);
    }

    public void habilidade(Personagem[] grupo, Inimigo inimigo) {
        System.out.println("1 - Ataque Bruto (5 mana)");
        System.out.println("2 - Investida de Armadura (4 mana)");
        int op = Entrada.lerInt("Escolha: ");

        if (op == 1 && usarMana(5)) {
            int dano = Math.max(1, getForca() * 3 - inimigo.getDefesa() / 2 + random.nextInt(5));
            System.out.println("Ataque Bruto causou " + inimigo.receberDano(dano) + " de dano!");
        } else if (op == 2 && usarMana(4)) {
            int dano = Math.max(1, getForca() * 2 + getDefesa() / 2
                    - inimigo.getDefesa() / 2 + random.nextInt(4));
            System.out.println("Investida de Armadura causou " + inimigo.receberDano(dano) + " de dano!");
            defendendo = true;
            System.out.println("Kael ficou protegido para o proximo ataque.");
        }
    }
}
