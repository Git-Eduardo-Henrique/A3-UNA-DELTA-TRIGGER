// ============================================================
// ARQUIVO: TextoService.java
// Le os textos da historia do arquivo data/textos.txt e devolve
// o texto correspondente a uma chave. Se a chave nao existir,
// devolve um texto padrao. Assim a historia fica separada do codigo.
// ============================================================

package util;

import java.io.*;
import java.util.*;

public final class TextoService {

    // Mapa com todos os textos: a chave aponta para o texto.
    private static final Map<String, String> TEXTOS = carregar();

    // Construtor privado: a classe so tem metodos estaticos.
    private TextoService() {}

    // Le o arquivo data/textos.txt e guarda os textos no mapa.
    // Cada linha deve estar no formato: CHAVE|texto.
    private static Map<String, String> carregar() {
        Map<String, String> m = new HashMap<String, String>();
        File f = new File("data/textos.txt");

        // Se o arquivo nao existe, devolve o mapa vazio.
        if (!f.exists()) return m;

        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String l;
            while ((l = br.readLine()) != null) {
                // Ignora linhas vazias e linhas de comentario (#).
                if (l.trim().isEmpty() || l.startsWith("#")) continue;

                // Separa a chave (antes do |) do texto (depois do |).
                int i = l.indexOf('|');
                if (i > 0) m.put(l.substring(0, i).trim(), l.substring(i + 1));
            }
            br.close();
        } catch (Exception e) {
            // Se der erro, simplesmente segue com o que ja foi lido.
        }
        return m;
    }

    // Devolve o texto da chave pedida ou o texto padrao se nao existir.
    public static String obter(String chave, String padrao) {
        String t = TEXTOS.get(chave);
        return t == null || t.trim().isEmpty() ? padrao : t;
    }
}
