package gui;

import model.Guerreiro;
import model.Personagem;
import model.Suporte;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DeltaTriggerGUI {
    private JFrame janela;
    private final CardLayout telas = new CardLayout();
    private final JPanel raiz = new JPanel(telas);
    private Personagem[] grupo;
    private String lider;
    private int intro = 0;

    private static final Color FUNDO = new Color(18, 18, 22);
    private static final Color PAINEL = new Color(35, 30, 28);
    private static final Color OURO = new Color(207, 169, 92);
    private static final Color TEXTO = new Color(235, 226, 205);
    private static final Font TITULO = new Font("Serif", Font.BOLD, 34);
    private static final Font NORMAL = new Font("Serif", Font.PLAIN, 18);

    private final String[] introducao = {
            "Há muitos séculos, o reino de Aethoria enfrentou uma grande ameaça.",
            "Malakar, o Rei Demônio, liderou criaturas contra o reino.",
            "Três grandes forças foram reunidas: Força, Magia e Espírito.",
            "Essas forças criaram o Delta, um selo capaz de prender Malakar na Torre de Noxar.",
            "Séculos de paz se passaram. Então, criaturas voltaram a surgir e uma energia estranha começou a se espalhar.",
            "O poder do Delta começou a despertar. Esse despertar ficou conhecido como Delta Trigger.",
            "Em uma pequena região de Aethoria, os irmãos Kael e Lyra decidem investigar os acontecimentos."
    };

    public void iniciar() {
        janela = new JFrame("Delta Trigger — Aethoria");
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setMinimumSize(new Dimension(1050, 680));
        janela.setSize(1200, 760);
        janela.setLocationRelativeTo(null);

        raiz.setBackground(FUNDO);
        raiz.add(criarMenu(), "MENU");
        raiz.add(criarIntroducao(), "INTRO");
        raiz.add(criarEscolha(), "ESCOLHA");
        janela.setContentPane(raiz);
        telas.show(raiz, "MENU");
        janela.setVisible(true);
    }

    private JPanel base() {
        JPanel p = new JPanel(new BorderLayout(18, 18));
        p.setBackground(FUNDO);
        p.setBorder(new EmptyBorder(28, 38, 28, 38));
        return p;
    }

    private JLabel titulo(String texto) {
        JLabel l = new JLabel(texto, SwingConstants.CENTER);
        l.setFont(TITULO);
        l.setForeground(OURO);
        return l;
    }

    private JButton botao(String texto) {
        JButton b = new JButton(texto);
        b.setFont(new Font("Serif", Font.BOLD, 18));
        b.setForeground(TEXTO);
        b.setBackground(PAINEL);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(OURO, 2), new EmptyBorder(12, 24, 12, 24)));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JPanel criarMenu() {
        JPanel p = base();
        JPanel centro = new JPanel();
        centro.setOpaque(false);
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));

        JLabel t = titulo("DELTA TRIGGER");
        JLabel sub = new JLabel("A E T H O R I A", SwingConstants.CENTER);
        sub.setFont(new Font("Serif", Font.PLAIN, 19));
        sub.setForeground(TEXTO);
        t.setAlignmentX(Component.CENTER_ALIGNMENT);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton novo = botao("NOVO JOGO");
        JButton carregar = botao("CARREGAR JOGO");
        JButton sair = botao("SAIR");
        for (JButton b : new JButton[]{novo, carregar, sair}) {
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
            b.setMaximumSize(new Dimension(320, 55));
        }
        novo.addActionListener(e -> { intro = 0; atualizarIntroducao(); telas.show(raiz, "INTRO"); });
        carregar.addActionListener(e -> JOptionPane.showMessageDialog(janela,
                "O carregamento será conectado à GUI na próxima etapa.\nO SaveService original foi preservado.",
                "Delta Trigger", JOptionPane.INFORMATION_MESSAGE));
        sair.addActionListener(e -> janela.dispose());

        centro.add(Box.createVerticalGlue());
        centro.add(t); centro.add(Box.createVerticalStrut(8)); centro.add(sub);
        centro.add(Box.createVerticalStrut(70)); centro.add(novo);
        centro.add(Box.createVerticalStrut(16)); centro.add(carregar);
        centro.add(Box.createVerticalStrut(16)); centro.add(sair);
        centro.add(Box.createVerticalGlue());
        p.add(centro, BorderLayout.CENTER);
        return p;
    }

    private JTextArea textoIntro;
    private JButton avancarIntro;

    private JPanel criarIntroducao() {
        JPanel p = base();
        p.add(titulo("A LENDA DO DELTA"), BorderLayout.NORTH);
        textoIntro = new JTextArea();
        textoIntro.setEditable(false); textoIntro.setLineWrap(true); textoIntro.setWrapStyleWord(true);
        textoIntro.setFont(new Font("Serif", Font.ITALIC, 25));
        textoIntro.setForeground(TEXTO); textoIntro.setBackground(PAINEL);
        textoIntro.setBorder(new EmptyBorder(70, 80, 70, 80));
        p.add(textoIntro, BorderLayout.CENTER);

        avancarIntro = botao("CONTINUAR");
        avancarIntro.addActionListener(e -> {
            intro++;
            if (intro >= introducao.length) telas.show(raiz, "ESCOLHA");
            else atualizarIntroducao();
        });
        JButton voltar = botao("VOLTAR");
        voltar.addActionListener(e -> telas.show(raiz, "MENU"));
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        rodape.setOpaque(false); rodape.add(voltar); rodape.add(avancarIntro);
        p.add(rodape, BorderLayout.SOUTH);
        atualizarIntroducao();
        return p;
    }

    private void atualizarIntroducao() {
        if (textoIntro != null) textoIntro.setText("\n\n" + introducao[Math.min(intro, introducao.length - 1)]);
    }

    private JPanel criarEscolha() {
        JPanel p = base();
        p.add(titulo("ESCOLHA O LÍDER"), BorderLayout.NORTH);
        JPanel cards = new JPanel(new GridLayout(1, 2, 30, 0)); cards.setOpaque(false);
        cards.add(cardPersonagem("KAEL", "Guerreiro", "Linha de frente\nHP 120  •  Mana 30\nForça e resistência", 1));
        cards.add(cardPersonagem("LYRA", "Suporte", "Cura e apoio\nHP 90  •  Mana 60\nMagia e suporte", 2));
        p.add(cards, BorderLayout.CENTER);
        JButton voltar = botao("VOLTAR"); voltar.addActionListener(e -> telas.show(raiz, "MENU"));
        JPanel sul = new JPanel(new FlowLayout()); sul.setOpaque(false); sul.add(voltar); p.add(sul, BorderLayout.SOUTH);
        return p;
    }

    private JPanel cardPersonagem(String nome, String classe, String desc, int escolha) {
        JPanel c = new JPanel(new BorderLayout(10, 18)); c.setBackground(PAINEL);
        c.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(OURO, 2), new EmptyBorder(35, 35, 35, 35)));
        JLabel n = new JLabel("<html><center><font size='7'>" + nome + "</font><br><font size='5'>" + classe + "</font></center></html>", SwingConstants.CENTER);
        n.setForeground(OURO); n.setFont(NORMAL); c.add(n, BorderLayout.NORTH);
        JTextArea d = new JTextArea(desc); d.setEditable(false); d.setOpaque(false); d.setForeground(TEXTO); d.setFont(new Font("Serif", Font.PLAIN, 21)); d.setLineWrap(true); d.setWrapStyleWord(true);
        c.add(d, BorderLayout.CENTER);
        JButton selecionar = botao("ESCOLHER " + nome); selecionar.addActionListener(e -> iniciarJogo(escolha)); c.add(selecionar, BorderLayout.SOUTH);
        return c;
    }

    private void iniciarJogo(int escolha) {
        grupo = new Personagem[]{new Guerreiro(), new Suporte()};
        lider = escolha == 1 ? "Kael" : "Lyra";
        raiz.add(criarFloresta(), "FLORESTA");
        telas.show(raiz, "FLORESTA");
    }

    private JPanel criarFloresta() {
        JPanel p = base();
        p.add(titulo("FLORESTA DOS LOBOS"), BorderLayout.NORTH);

        JPanel cena = new JPanel(new BorderLayout());
        cena.setBackground(new Color(25, 39, 28));
        cena.setBorder(BorderFactory.createLineBorder(OURO, 2));
        JLabel placeholder = new JLabel("CENÁRIO / ARTE DA FLORESTA", SwingConstants.CENTER);
        placeholder.setForeground(new Color(180, 195, 170)); placeholder.setFont(new Font("Serif", Font.BOLD, 28));
        cena.add(placeholder, BorderLayout.CENTER);
        p.add(cena, BorderLayout.CENTER);

        JPanel inferior = new JPanel(new BorderLayout(15, 0)); inferior.setOpaque(false);
        JTextArea dialogo = new JTextArea("Kael e Lyra entram na floresta para investigar os acontecimentos.\nO caminho parece tranquilo, mas algo se move entre as árvores...");
        dialogo.setEditable(false); dialogo.setLineWrap(true); dialogo.setWrapStyleWord(true); dialogo.setRows(4);
        dialogo.setFont(NORMAL); dialogo.setForeground(TEXTO); dialogo.setBackground(PAINEL); dialogo.setBorder(new EmptyBorder(14, 18, 14, 18));
        inferior.add(dialogo, BorderLayout.CENTER);

        Personagem l = "Kael".equals(lider) ? grupo[0] : grupo[1];
        JLabel status = new JLabel("<html><b>" + l.getNome() + " — LÍDER</b><br>HP: " + l.getVida() + "/" + l.getVidaMaxima() + "<br>Mana: " + l.getMana() + "/" + l.getManaMaxima() + "<br>Nível: " + l.getNivel() + "</html>");
        status.setForeground(TEXTO); status.setFont(NORMAL); status.setOpaque(true); status.setBackground(PAINEL); status.setBorder(new EmptyBorder(12, 20, 12, 30));
        inferior.add(status, BorderLayout.EAST);

        JPanel acoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8)); acoes.setOpaque(false);
        JButton explorar = botao("EXPLORAR"); JButton inventario = botao("INVENTÁRIO"); JButton personagem = botao("PERSONAGEM");
        explorar.addActionListener(e -> JOptionPane.showMessageDialog(janela, "Próxima etapa: conectar as waves e a batalha à GUI."));
        inventario.addActionListener(e -> JOptionPane.showMessageDialog(janela, "Inventário gráfico será conectado ao Inventario.java."));
        personagem.addActionListener(e -> JOptionPane.showMessageDialog(janela, "Status: " + l.getNome() + " | HP " + l.getVida() + "/" + l.getVidaMaxima() + " | Mana " + l.getMana() + "/" + l.getManaMaxima()));
        acoes.add(explorar); acoes.add(inventario); acoes.add(personagem);

        JPanel sul = new JPanel(new BorderLayout()); sul.setOpaque(false); sul.add(inferior, BorderLayout.CENTER); sul.add(acoes, BorderLayout.SOUTH);
        p.add(sul, BorderLayout.SOUTH);
        return p;
    }
}
