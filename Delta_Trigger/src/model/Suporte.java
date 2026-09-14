package model;

public class Suporte extends Personagem {
    public Suporte() {
        super("Lyra", 90, 60, new Atributos(2, 4, 7, 4, 3, 2), 100);
    }

    public void habilidade(Personagem[] grupo, Inimigo inimigo) {
        System.out.println("1 - Cura Individual (8 mana)");
        System.out.println("2 - Cura em Grupo (14 mana)");
        int op = Entrada.lerInt("Escolha: ");

        if (op == 1 && usarMana(8)) {
            System.out.println("1 - Kael");
            System.out.println("2 - Lyra");
            int alvo = Entrada.lerInt("Alvo: ");
            Personagem p = (alvo == 1 ? grupo[0] : grupo[1]);
            int cura = 18 + getInteligencia() * 2;
            p.curar(cura);
            System.out.println(p.getNome() + " recuperou " + cura + " HP.");
        } else if (op == 2 && usarMana(14)) {
            int cura = 12 + getInteligencia();
            for (Personagem p : grupo) if (p.vivo()) p.curar(cura);
            System.out.println("O grupo recuperou " + cura + " HP.");
        }
    }
}
