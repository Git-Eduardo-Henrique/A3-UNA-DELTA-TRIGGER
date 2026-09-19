package model;

public class Guerreiro extends Personagem {
    public Guerreiro() {
        super("Kael", 120, 30, new Atributos(5, 5, 2, 4, 3, 3), 80);
        Equipamento espada = new Equipamento("Espada Inicial", "Arma", 0, 1, 0, 0, 0, 0, 0);
        Equipamento armadura = new Equipamento("Armadura Inicial", "Armadura", 0, 0, 2, 0, 1, 0, 0);
        inventario.adicionarEquipamento(espada);
        inventario.adicionarEquipamento(armadura);
        equipar(espada);
        equipar(armadura);
        inventario.adicionarItem(new Item("Pocao de Vida", "Pocao", 15, 30, 0, 2));
        inventario.adicionarItem(new Item("Pocao de Mana", "Pocao", 18, 0, 20, 1));
    }

    @Override
    public boolean habilidade(Personagem[] grupo, Inimigo[] inimigos) {
        while (true) {
            System.out.println("\n" + Cores.magia("=== HABILIDADES DE KAEL ==="));
            System.out.println("1 - Ataque Bruto (5 mana)");
            System.out.println("2 - Investida de Armadura (4 mana)");
            System.out.println("0 - Voltar");
            int op = Entrada.lerInt("Escolha: ");
            if (op == 0) return false;
            if (op != 1 && op != 2) {
                System.out.println(Cores.aviso("Opcao invalida. Escolha 1, 2 ou 0."));
                continue;
            }

            int custo = op == 1 ? 5 : 4;
            if (mana < custo) {
                System.out.println(Cores.aviso("Mana insuficiente. Sao necessarios " + custo + " pontos."));
                continue;
            }

            Inimigo alvo = Batalha.escolherInimigo(inimigos);
            if (alvo == null) continue;
            usarMana(custo);
            int dano;
            if (op == 1) {
                dano = Math.max(1, getForca() * 3 + random.nextInt(5) - alvo.getDefesa() / 2);
                dano = alvo.receberDano(dano);
                System.out.println(Cores.magia("Ataque Bruto causou " + dano + " de dano!"));
            } else {
                dano = Math.max(1, getForca() * 2 + getDefesa() / 2 + random.nextInt(4) - alvo.getDefesa() / 2);
                dano = alvo.receberDano(dano);
                defendendo = true;
                System.out.println(Cores.magia("Investida de Armadura causou " + dano + " de dano!"));
                System.out.println(Cores.aviso("Kael ficou protegido para o proximo ataque."));
            }
            return true;
        }
    }
}
