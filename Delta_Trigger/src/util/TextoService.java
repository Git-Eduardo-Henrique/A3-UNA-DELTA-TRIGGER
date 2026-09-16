package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public final class TextoService {
    private static final Map<String, String> TEXTOS = carregar();

    private TextoService() {}

    private static Map<String, String> carregar() {
        Map<String, String> mapa = new HashMap<String, String>();
        File arquivo = new File("data/textos.txt");
        if (!arquivo.exists()) return mapa;
        try {
            BufferedReader br = new BufferedReader(new FileReader(arquivo));
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty() || linha.startsWith("#")) continue;
                int separador = linha.indexOf('|');
                if (separador <= 0) continue;
                mapa.put(linha.substring(0, separador).trim(), linha.substring(separador + 1));
            }
            br.close();
        } catch (Exception e) {
            System.out.println("Nao foi possivel carregar textos.txt. O jogo usara textos padrao.");
        }
        return mapa;
    }

    public static String obter(String chave, String padrao) {
        String texto = TEXTOS.get(chave);
        return texto == null || texto.trim().isEmpty() ? padrao : texto;
    }
}
