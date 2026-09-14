package util;
import model.Personagem;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class SaveService {
    public static void salvar(Personagem[] grupo) {
        try {
            PrintWriter arquivo = new PrintWriter(new FileWriter("save.txt"));
            for (Personagem p : grupo) {
                arquivo.println(p.getNome() + ";" + p.getNivel() + ";"
                        + p.getVida() + ";" + p.getMana() + ";" + p.getDinheiro());
            }
            arquivo.close();
            System.out.println("Jogo salvo em save.txt.");
        } catch (IOException e) {
            System.out.println("Erro ao salvar.");
        }
    }
}
