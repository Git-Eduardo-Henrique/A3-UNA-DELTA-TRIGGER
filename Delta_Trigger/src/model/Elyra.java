package model;

public class Elyra extends Personagem {
    public Elyra() {
        super("Elyra", 95, 50, new Atributos(4, 3, 5, 3, 6, 4), 0);
        definirNivel(2);
        vidaMaxima = 105;
        vida = 105;
        manaMaxima = 55;
        mana = 55;

        Equipamento arco = new Equipamento("Arco Arcano Inicial", "Arma", 0, 1, 0, 1, 0, 1, 0);
        Equipamento armadura = new Equipamento("Armadura de Exploradora", "Armadura", 0, 0, 1, 0, 1, 1, 1);
        inventario.adicionarEquipamento(arco);
        inventario.adicionarEquipamento(armadura);
        equipar(arco);
        equipar(armadura);
    }

    @Override
    public boolean habilidade(Personagem[] grupo, Inimigo[] inimigos) {
        while (true) {
            System.out.println("\n" + Cores.magia("=== HABILIDADES DE ELYRA ==="));
            System.out.println("1 - Flecha Arcana (6 mana)");
            System.out.println("2 - Flecha de Gelo (8 mana)");
            System.out.println("0 - Voltar");
            int op = Entrada.lerInt("Escolha: ");
            if (op == 0) return false;
            if (op != 1 && op != 2) {
                System.out.println(Cores.aviso("Opcao invalida. Escolha 1, 2 ou 0."));
                continue;
            }
            int custo = op == 1 ? 6 : 8;
            if (mana < custo) {
                System.out.println(Cores.aviso("Mana insuficiente. Sao necessarios " + custo + " pontos."));
                continue;
            }
            Inimigo alvo = Batalha.escolherInimigo(inimigos);
            if (alvo == null) continue;
            usarMana(custo);
            int dano;
            if (op == 1) {
                dano = Math.max(1, getForca() * 2 + getInteligencia() * 2 + random.nextInt(5) - alvo.getDefesa() / 2);
                System.out.println(Cores.magia("Flecha Arcana causou " + alvo.receberDano(dano) + " de dano!"));
            } else {
                dano = Math.max(1, getInteligencia() * 3 + random.nextInt(4) - alvo.getDefesa() / 2);
                System.out.println(Cores.magia("Flecha de Gelo causou " + alvo.receberDano(dano) + " de dano!"));
            }
            return true;
        }
    }
}
