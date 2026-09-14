package view;
import model.Entrada;

public class MenuView {
    public static void titulo() {
        System.out.println("========================================");
        System.out.println("             DELTA TRIGGER");
        System.out.println("                AETHORIA");
        System.out.println("========================================");
    }

    public static int escolherLider() {
        System.out.println("\nQuem sera o lider?");
        System.out.println("1 - Kael");
        System.out.println("2 - Lyra");
        return Entrada.lerInt("Escolha: ");
    }
}
