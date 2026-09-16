package util;

import model.Atributos;
import model.Equipamento;
import model.Elyra;
import model.Guerreiro;
import model.Inventario;
import model.Item;
import model.Personagem;
import model.Suporte;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.Base64;

public final class SaveService {
    private static final String ARQUIVO = "save.txt";
    private static final String CHAVE = "DeltaTrigger2026";

    private SaveService() {}

    public static boolean existeSave() {
        return new File(ARQUIVO).exists();
    }

    public static boolean salvar(Personagem[] grupo, String lider, String local, int waveFloresta, boolean elyraEntrou) {
        try {
            StringBuilder dados = new StringBuilder();
            dados.append("DELTA_V31\n");
            dados.append("STATE|").append(local).append("|").append(waveFloresta).append("|").append(elyraEntrou).append("\n");
            dados.append("L|").append(lider).append("\n");

            for (int i = 0; i < grupo.length; i++) {
                Personagem p = grupo[i];
                dados.append("P|").append(i).append("|").append(p.getClass().getSimpleName()).append("|")
                        .append(p.getNivel()).append("|").append(p.getVidaMaxima()).append("|").append(p.getVida()).append("|")
                        .append(p.getManaMaxima()).append("|").append(p.getMana()).append("|").append(p.getDinheiro()).append("|")
                        .append(p.getPontosAtributo()).append("|").append(p.getExperiencia()).append("|")
                        .append(p.getAtributosBase().salvar()).append("\n");
                salvarEquipamento(dados, "W", i, p.getArmaEquipada());
                salvarEquipamento(dados, "A", i, p.getArmaduraEquipada());
                salvarEquipamento(dados, "B", i, p.getBotasEquipadas());

                for (Equipamento e : p.getInventario().getEquipamentos()) salvarEquipamentoInventario(dados, i, e);
                for (Item item : p.getInventario().getItens()) {
                    dados.append("I|").append(i).append("|").append(item.getNome()).append("|").append(item.getTipo()).append("|")
                            .append(item.getPreco()).append("|").append(item.getCuraHp()).append("|").append(item.getCuraMana()).append("|")
                            .append(item.getQuantidade()).append("\n");
                }
            }

            Files.write(Paths.get(ARQUIVO), criptografar(dados.toString()).getBytes(StandardCharsets.UTF_8));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static void salvarEquipamentoInventario(StringBuilder dados, int indice, Equipamento e) {
        dados.append("E|").append(indice).append("|").append(e.getNome()).append("|").append(e.getTipo()).append("|")
                .append(e.getPreco()).append("|").append(e.getForca()).append("|").append(e.getDefesa()).append("|")
                .append(e.getInteligencia()).append("|").append(e.getResistencia()).append("|").append(e.getVelocidade()).append("|")
                .append(e.getSorte()).append("\n");
    }

    private static void salvarEquipamento(StringBuilder dados, String registro, int indice, Equipamento e) {
        if (e == null) {
            dados.append(registro).append("|").append(indice).append("|-\n");
            return;
        }
        dados.append(registro).append("|").append(indice).append("|").append(e.getNome()).append("\n");
    }

    public static DadosSave carregar() {
        try {
            String criptografado = new String(Files.readAllBytes(Paths.get(ARQUIVO)), StandardCharsets.UTF_8);
            String texto = descriptografar(criptografado);
            String[] linhas = texto.split("\\n");
            if (linhas.length == 0 || !linhas[0].equals("DELTA_V31")) throw new Exception();

            String lider = "Kael";
            String local = "ELDORIA";
            int wave = 5;
            boolean elyra = false;
            Personagem[] grupo = new Personagem[3];

            for (String linha : linhas) {
                String[] p = linha.split("\\|", -1);
                if (p.length == 0) continue;
                if (p[0].equals("L") && p.length >= 2) lider = p[1];
                if (p[0].equals("STATE") && p.length >= 4) {
                    local = p[1];
                    wave = Integer.parseInt(p[2]);
                    elyra = Boolean.parseBoolean(p[3]);
                }
                if (p[0].equals("P") && p.length >= 12) {
                    int idx = Integer.parseInt(p[1]);
                    Personagem personagem = criarPersonagem(p[2]);
                    personagem.getInventario().getItens().clear();
                    personagem.getInventario().getEquipamentos().clear();
                    personagem.limparEquipamentosEquipados();
                    personagem.definirNivel(Integer.parseInt(p[3]));
                    personagem.definirVidaMaxima(Integer.parseInt(p[4]));
                    personagem.definirVida(Integer.parseInt(p[5]));
                    personagem.definirManaMaxima(Integer.parseInt(p[6]));
                    personagem.definirMana(Integer.parseInt(p[7]));
                    personagem.definirDinheiro(Integer.parseInt(p[8]));
                    personagem.definirPontosAtributo(Integer.parseInt(p[9]));
                    personagem.definirExperiencia(Integer.parseInt(p[10]));
                    restaurarAtributos(personagem, p[11]);
                    grupo[idx] = personagem;
                }
            }

            for (String linha : linhas) {
                String[] p = linha.split("\\|", -1);
                if (p.length == 0) continue;
                if (p[0].equals("E") && p.length >= 11) {
                    int idx = Integer.parseInt(p[1]);
                    grupo[idx].getInventario().adicionarEquipamento(equipamento(p, 2));
                } else if (p[0].equals("I") && p.length >= 8) {
                    int idx = Integer.parseInt(p[1]);
                    grupo[idx].getInventario().adicionarItem(new Item(p[2], p[3], Integer.parseInt(p[4]), Integer.parseInt(p[5]), Integer.parseInt(p[6]), Integer.parseInt(p[7])));
                }
            }

            for (String linha : linhas) {
                String[] p = linha.split("\\|", -1);
                if (p.length < 3) continue;
                if (p[0].equals("W") || p[0].equals("A") || p[0].equals("B")) {
                    if (p[2].equals("-")) continue;
                    int idx = Integer.parseInt(p[1]);
                    Equipamento equipamento = grupo[idx].getInventario().encontrarEquipamento(p[2]);
                    if (equipamento != null) grupo[idx].equipar(equipamento);
                }
            }

            int total = 0;
            for (Personagem p : grupo) if (p != null) total++;
            if (total < 2) throw new Exception();
            Personagem[] finalGrupo = new Personagem[total];
            int pos = 0;
            for (Personagem p : grupo) if (p != null) finalGrupo[pos++] = p;
            return new DadosSave(finalGrupo, lider, local, wave, elyra);
        } catch (Exception e) {
            System.out.println("O save esta corrompido ou foi criado por uma versao diferente.");
            return null;
        }
    }

    private static Personagem criarPersonagem(String classe) {
        if (classe.equals("Guerreiro")) return new Guerreiro();
        if (classe.equals("Suporte")) return new Suporte();
        if (classe.equals("Elyra")) return new Elyra();
        throw new IllegalArgumentException("Classe desconhecida");
    }

    private static void restaurarAtributos(Personagem p, String valores) {
        String[] a = valores.split(",");
        if (a.length != 6) throw new IllegalArgumentException();
        int[] atuais = {p.getAtributosBase().getForca(), p.getAtributosBase().getDefesa(), p.getAtributosBase().getInteligencia(),
                p.getAtributosBase().getResistencia(), p.getAtributosBase().getVelocidade(), p.getAtributosBase().getSorte()};
        for (int i = 0; i < 6; i++) {
            int alvo = Integer.parseInt(a[i]);
            while (atuais[i] < alvo) { p.getAtributosBase().aumentar(i + 1); atuais[i]++; }
        }
    }

    private static Equipamento equipamento(String[] p, int inicio) {
        return new Equipamento(p[inicio], p[inicio + 1], Integer.parseInt(p[inicio + 2]), Integer.parseInt(p[inicio + 3]),
                Integer.parseInt(p[inicio + 4]), Integer.parseInt(p[inicio + 5]), Integer.parseInt(p[inicio + 6]),
                Integer.parseInt(p[inicio + 7]), Integer.parseInt(p[inicio + 8]));
    }

    private static String criptografar(String texto) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, chave());
        return Base64.getEncoder().encodeToString(cipher.doFinal(texto.getBytes(StandardCharsets.UTF_8)));
    }

    private static String descriptografar(String texto) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, chave());
        return new String(cipher.doFinal(Base64.getDecoder().decode(texto)), StandardCharsets.UTF_8);
    }

    private static SecretKeySpec chave() throws Exception {
        byte[] hash = MessageDigest.getInstance("SHA-256").digest(CHAVE.getBytes(StandardCharsets.UTF_8));
        byte[] chave16 = new byte[16];
        System.arraycopy(hash, 0, chave16, 0, 16);
        return new SecretKeySpec(chave16, "AES");
    }

    public static class DadosSave {
        public final Personagem[] grupo;
        public final String lider;
        public final String local;
        public final int waveFloresta;
        public final boolean elyraEntrou;

        public DadosSave(Personagem[] grupo, String lider, String local, int waveFloresta, boolean elyraEntrou) {
            this.grupo = grupo;
            this.lider = lider;
            this.local = local;
            this.waveFloresta = waveFloresta;
            this.elyraEntrou = elyraEntrou;
        }
    }
}
