package model;

public class Guerreiro extends Personagem {
    public Guerreiro(){super("Kael",120,30,new Atributos(5,5,2,4,3,3));}
    public boolean habilidade(Grupo grupo,Inimigo[] inimigos){
        while(true){System.out.println("\n"+Cores.magia("=== HABILIDADES DE KAEL ==="));System.out.println("1 - Ataque Bruto (5 mana)");System.out.println("2 - Investida de Armadura (4 mana)");System.out.println("0 - Voltar");int op=Entrada.lerInt("Escolha: ");if(op==0)return false;if(op!=1&&op!=2){System.out.println(Cores.aviso("Opcao invalida. Escolha 1, 2 ou 0."));continue;}int custo=op==1?5:4;if(mana<custo){System.out.println(Cores.aviso("Mana insuficiente. Sao necessarios "+custo+" pontos."));continue;}Inimigo alvo=Batalha.escolherInimigo(inimigos);if(alvo==null)continue;usarMana(custo);int dano=op==1?getForca()*3+random.nextInt(5)-2:getForca()*2+getDefesa()/2+random.nextInt(4)-2;Batalha.AtaqueResultado r=Batalha.calcularAtaque(this,alvo,dano);if(r.acertou){System.out.println(Cores.magia((op==1?"Ataque Bruto":"Investida de Armadura")+" causou "+r.dano+" de dano!"));if(op==2)defendendo=true;}else System.out.println(Cores.aviso(Cores.kael("Kael")+" errou o ataque!"));return true;}
    }
}
