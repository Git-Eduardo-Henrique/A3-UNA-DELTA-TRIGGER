package model;

public class Suporte extends Personagem {
    public Suporte() {
        super("Lyra", 90, 60, new Atributos(2, 4, 7, 4, 3, 2), 100);
        Equipamento cajado = new Equipamento("Cajado Inicial", "Arma", 0, 0, 0, 1, 0, 0, 0);
        Equipamento vestimenta = new Equipamento("Vestimenta Inicial", "Armadura", 0, 0, 1, 1, 1, 0, 0);
        inventario.adicionarEquipamento(cajado);
        inventario.adicionarEquipamento(vestimenta);
        equipar(cajado);
        equipar(vestimenta);
        inventario.adicionarItem(new Item("Pocao de Vida", "Pocao", 15, 30, 0, 2));
        inventario.adicionarItem(new Item("Pocao de Mana", "Pocao", 18, 0, 20, 2));
    }

    @Override
    public boolean habilidade(Personagem[] grupo, Inimigo[] inimigos) {
        while (true) {
            System.out.println("\n" + Cores.magia("=== HABILIDADES DE LYRA ==="));
            System.out.println("1 - Cura Individual (8 mana)");
            System.out.println("2 - Cura em Grupo (14 mana)");
            System.out.println("0 - Voltar");
            int op = Entrada.lerInt("Escolha: ");
            if (op == 0) return false;
            if (op != 1 && op != 2) {
                System.out.println(Cores.aviso("Opcao invalida. Escolha 1, 2 ou 0."));
                continue;
            }

            if (op == 1) {
                Personagem alvo = escolherAlvoCura(grupo);
                if (alvo == null) continue;
                if (alvo.getVida() >= alvo.getVidaMaxima()) {
                    System.out.println(Cores.aviso(alvo.getNome() + " ja esta com a vida cheia."));
                    continue;
                }
                if (mana < 8) {
                    System.out.println(Cores.aviso("Mana insuficiente. Sao necessarios 8 pontos."));
                    continue;
                }
                usarMana(8);
                int antes = alvo.getVida();
                alvo.curar(18 + getInteligencia() * 2);
                System.out.println(Cores.sucesso(alvo.getNome() + " recuperou " + (alvo.getVida() - antes) + " HP."));
                return true;
            }

            boolean alguemFerido = false;
            for (Personagem p : grupo) if (p.vivo() && p.getVida() < p.getVidaMaxima()) alguemFerido = true;
            if (!alguemFerido) {
                System.out.println(Cores.aviso("Todos os personagens vivos estao com a vida cheia."));
                continue;
            }
            if (mana < 14) {
                System.out.println(Cores.aviso("Mana insuficiente. Sao necessarios 14 pontos."));
                continue;
            }
            usarMana(14);
            int cura = 12 + getInteligencia();
            for (Personagem p : grupo) if (p.vivo()) p.curar(cura);
            System.out.println(Cores.sucesso("Cura em Grupo restaurou " + cura + " HP dos aliados vivos."));
            return true;
        }
    }

    private Personagem escolherAlvoCura(Personagem[] grupo) {
        System.out.println("\nEscolha o alvo da cura:");
        int numero = 0;
        for (Personagem p : grupo) {
            if (p.vivo()) {
                numero++;
                System.out.println(numero + " - " + p.getNome() + " | HP " + p.getVida() + "/" + p.getVidaMaxima());
            }
        }
        System.out.println("0 - Voltar");
        int escolha = Entrada.lerInt("Alvo: ");
        if (escolha == 0) return null;
        numero = 0;
        for (Personagem p : grupo) {
            if (p.vivo()) {
                numero++;
                if (numero == escolha) return p;
            }
        }
        System.out.println(Cores.aviso("Alvo invalido."));
        return null;
    }
}
