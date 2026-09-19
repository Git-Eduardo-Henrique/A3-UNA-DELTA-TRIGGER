package gui;

import model.Guerreiro;
import model.Inimigo;
import model.LoboFactory;
import model.Personagem;
import model.Suporte;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;

public class DeltaTriggerGUI {
    private JFrame janela;
    private final CardLayout telas = new CardLayout();
    private final JPanel raiz = new JPanel(telas);
    private Personagem[] grupo;
    private String lider;
    private Timer timerHistoria;
    private int indiceLetra;
    private Inimigo inimigoAtual;
    private JTextArea logBatalha;
    private JProgressBar hpJogador, manaJogador, hpInimigo;

    private static final Color FUNDO = new Color(4, 12, 27);
    private static final Color PAINEL = new Color(7, 19, 38);
    private static final Color AZUL = new Color(39, 132, 255);
    private static final Color AZUL_CLARO = new Color(126, 196, 255);
    private static final Color OURO = new Color(226, 166, 58);
    private static final Color TEXTO = new Color(236, 241, 248);
    private static final Font TITULO = new Font("Serif", Font.BOLD, 34);
    private static final Font NORMAL = new Font("Serif", Font.PLAIN, 18);

    private final String historia =
            "Há muitos séculos, o reino de Aethoria enfrentou uma grande ameaça.\n\n" +
            "Malakar, o Rei Demônio, liderou criaturas contra o reino. Três grandes forças foram reunidas: Força, Magia e Espírito. " +
            "Essas forças criaram o Delta, um selo capaz de prender Malakar na Torre de Noxar.\n\n" +
            "Séculos de paz se passaram. Então, criaturas voltaram a surgir e uma energia estranha começou a se espalhar. " +
            "O poder do Delta começou a despertar. Esse despertar ficou conhecido como Delta Trigger.\n\n" +
            "Em uma pequena região de Aethoria, os irmãos Kael e Lyra decidem investigar os acontecimentos...";

    public void iniciar() {
        janela = new JFrame("Delta Trigger — Aethoria");
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setMinimumSize(new Dimension(1050, 680));
        janela.setSize(1200, 760);
        janela.setLocationRelativeTo(null);
        raiz.setBackground(FUNDO);
        raiz.add(criarMenu(), "MENU");
        raiz.add(criarHistoria(), "HISTORIA");
        raiz.add(criarEscolha(), "ESCOLHA");
        janela.setContentPane(raiz);
        telas.show(raiz, "MENU");
        janela.setVisible(true);
    }

    private JPanel base() {
        JPanel p = new JPanel(new BorderLayout(18, 18));
        p.setBackground(FUNDO);
        p.setBorder(new EmptyBorder(24, 34, 24, 34));
        return p;
    }

    private JLabel titulo(String texto) {
        JLabel l = new JLabel(texto, SwingConstants.CENTER);
        l.setFont(TITULO); l.setForeground(AZUL_CLARO);
        return l;
    }

    private JButton botao(String texto) {
        JButton b = new JButton(texto);
        b.setFont(new Font("Serif", Font.BOLD, 18));
        b.setForeground(TEXTO); b.setBackground(PAINEL); b.setFocusPainted(false);
        b.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(OURO, 2), new EmptyBorder(11, 22, 11, 22)));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private ImageIcon imagem(String nome, int largura, int altura) {
        File f = new File("assets/" + nome);
        if (!f.exists()) f = new File("delta_gui/assets/" + nome);
        if (!f.exists()) return null;
        Image img = new ImageIcon(f.getPath()).getImage().getScaledInstance(largura, altura, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    private JPanel criarMenu() {
        JPanel p = base();
        JPanel centro = new JPanel(); centro.setOpaque(false); centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        JLabel logo = new JLabel(imagem("logo.png", 400, 185)); logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel t = titulo("DELTA TRIGGER"); t.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel sub = new JLabel("UMA JORNADA ENTRE O DESTINO E O CAOS", SwingConstants.CENTER);
        sub.setFont(new Font("Serif", Font.PLAIN, 17)); sub.setForeground(OURO); sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton novo = botao("NOVO JOGO"), carregar = botao("CARREGAR JOGO"), sair = botao("SAIR");
        for (JButton b : new JButton[]{novo, carregar, sair}) { b.setAlignmentX(Component.CENTER_ALIGNMENT); b.setMaximumSize(new Dimension(340, 54)); }
        novo.addActionListener(e -> iniciarHistoria());
        carregar.addActionListener(e -> JOptionPane.showMessageDialog(janela, "O SaveService original continua preservado e será ligado à GUI depois."));
        sair.addActionListener(e -> janela.dispose());
        centro.add(Box.createVerticalGlue());
        if (logo.getIcon() != null) centro.add(logo); else centro.add(t);
        centro.add(Box.createVerticalStrut(6)); centro.add(sub); centro.add(Box.createVerticalStrut(35));
        centro.add(novo); centro.add(Box.createVerticalStrut(12)); centro.add(carregar); centro.add(Box.createVerticalStrut(12)); centro.add(sair);
        centro.add(Box.createVerticalGlue()); p.add(centro, BorderLayout.CENTER);
        return p;
    }

    private JTextArea textoHistoria;
    private JButton continuarHistoria;

    private JPanel criarHistoria() {
        JPanel p = base(); p.add(titulo("A LENDA DO DELTA"), BorderLayout.NORTH);
        textoHistoria = new JTextArea(); textoHistoria.setEditable(false); textoHistoria.setLineWrap(true); textoHistoria.setWrapStyleWord(true);
        textoHistoria.setFont(new Font("Serif", Font.PLAIN, 22)); textoHistoria.setForeground(TEXTO); textoHistoria.setBackground(PAINEL);
        textoHistoria.setCaretColor(TEXTO); textoHistoria.setBorder(new EmptyBorder(48, 65, 48, 65));
        p.add(new JScrollPane(textoHistoria) {{ setBorder(BorderFactory.createLineBorder(OURO, 2)); getViewport().setBackground(PAINEL); }}, BorderLayout.CENTER);
        continuarHistoria = botao("CONTINUAR"); continuarHistoria.setEnabled(false); continuarHistoria.addActionListener(e -> telas.show(raiz, "ESCOLHA"));
        JButton pular = botao("PULAR ANIMAÇÃO"); pular.addActionListener(e -> finalizarHistoria());
        JPanel sul = new JPanel(new FlowLayout(FlowLayout.CENTER, 18, 0)); sul.setOpaque(false); sul.add(pular); sul.add(continuarHistoria); p.add(sul, BorderLayout.SOUTH);
        return p;
    }

    private void iniciarHistoria() {
        textoHistoria.setText(""); indiceLetra = 0; continuarHistoria.setEnabled(false); telas.show(raiz, "HISTORIA");
        if (timerHistoria != null) timerHistoria.stop();
        timerHistoria = new Timer(24, e -> {
            if (indiceLetra < historia.length()) {
                int bloco = Math.min(2, historia.length() - indiceLetra);
                textoHistoria.append(historia.substring(indiceLetra, indiceLetra + bloco)); indiceLetra += bloco;
                textoHistoria.setCaretPosition(textoHistoria.getDocument().getLength());
            } else { timerHistoria.stop(); continuarHistoria.setEnabled(true); }
        });
        timerHistoria.start();
    }

    private void finalizarHistoria() {
        if (timerHistoria != null) timerHistoria.stop(); textoHistoria.setText(historia); indiceLetra = historia.length(); continuarHistoria.setEnabled(true);
    }

    private JPanel criarEscolha() {
        JPanel p = base(); p.add(titulo("ESCOLHA SEU PERSONAGEM"), BorderLayout.NORTH);
        JPanel cards = new JPanel(new GridLayout(1, 2, 26, 0)); cards.setOpaque(false);
        cards.add(cardPersonagem("KAEL", "O IRMÃO — Guerreiro / Atacante", "Força, disciplina e um coração que nunca desiste.\n\nHP 120  •  Mana 30", "kael.png", 1));
        cards.add(cardPersonagem("LYRA", "A IRMÃ — Suporte / Mágica", "Conhecimento, empatia e um poder que inspira esperança.\n\nHP 90  •  Mana 60", "lyra.png", 2));
        p.add(cards, BorderLayout.CENTER);
        return p;
    }

    private JPanel cardPersonagem(String nome, String classe, String desc, String arquivoImagem, int escolha) {
        JPanel c = new JPanel(new BorderLayout(10, 10)); c.setBackground(PAINEL);
        c.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(AZUL, 2), new EmptyBorder(18, 24, 18, 24)));
        JLabel n = new JLabel("<html><center><font size='7'>" + nome + "</font><br><font size='4'>" + classe + "</font></center></html>", SwingConstants.CENTER);
        n.setForeground(OURO); c.add(n, BorderLayout.NORTH);
        JPanel meio = new JPanel(new BorderLayout(10, 10)); meio.setOpaque(false);
        JLabel foto = new JLabel(imagem(arquivoImagem, 245, 255), SwingConstants.CENTER); meio.add(foto, BorderLayout.CENTER);
        JTextArea d = new JTextArea(desc); d.setEditable(false); d.setOpaque(false); d.setForeground(TEXTO); d.setFont(new Font("Serif", Font.PLAIN, 18)); d.setLineWrap(true); d.setWrapStyleWord(true); d.setRows(4);
        meio.add(d, BorderLayout.SOUTH); c.add(meio, BorderLayout.CENTER);
        JButton selecionar = botao("SELECIONAR " + nome); selecionar.addActionListener(e -> iniciarJogo(escolha)); c.add(selecionar, BorderLayout.SOUTH);
        return c;
    }

    private void iniciarJogo(int escolha) {
        grupo = new Personagem[]{new Guerreiro(), new Suporte()}; lider = escolha == 1 ? "Kael" : "Lyra";
        raiz.add(criarFloresta(), "FLORESTA"); telas.show(raiz, "FLORESTA");
    }

    private Personagem liderAtual() { return "Kael".equals(lider) ? grupo[0] : grupo[1]; }

    private JPanel criarFloresta() {
        JPanel p = base(); p.add(titulo("FLORESTA DOS LOBOS"), BorderLayout.NORTH);
        JPanel cena = new JPanel(new BorderLayout()); cena.setBackground(new Color(7, 31, 38)); cena.setBorder(BorderFactory.createLineBorder(AZUL, 2));
        JLabel arte = new JLabel("✦  FLORESTA DOS LOBOS  ✦", SwingConstants.CENTER); arte.setForeground(AZUL_CLARO); arte.setFont(new Font("Serif", Font.BOLD, 30)); cena.add(arte, BorderLayout.CENTER); p.add(cena, BorderLayout.CENTER);
        JTextArea dialogo = new JTextArea("Você entrou na Floresta dos Lobos.\nO som da água ecoa pelas rochas. Um uivo distante pode ser ouvido ao longe.");
        dialogo.setEditable(false); dialogo.setLineWrap(true); dialogo.setWrapStyleWord(true); dialogo.setRows(3); dialogo.setFont(NORMAL); dialogo.setForeground(TEXTO); dialogo.setBackground(PAINEL); dialogo.setBorder(new EmptyBorder(12, 16, 12, 16));
        Personagem l = liderAtual(); JLabel status = new JLabel("<html><b>" + l.getNome() + " — Nv. " + l.getNivel() + "</b><br>HP: " + l.getVida() + "/" + l.getVidaMaxima() + "<br>Mana: " + l.getMana() + "/" + l.getManaMaxima() + "<br>Local: Floresta dos Lobos</html>");
        status.setForeground(TEXTO); status.setFont(NORMAL); status.setOpaque(true); status.setBackground(PAINEL); status.setBorder(new EmptyBorder(10, 20, 10, 28));
        JPanel info = new JPanel(new BorderLayout(12, 0)); info.setOpaque(false); info.add(dialogo, BorderLayout.CENTER); info.add(status, BorderLayout.EAST);
        JButton explorar = botao("EXPLORAR"), inventario = botao("INVENTÁRIO"), personagem = botao("PERSONAGEM");
        explorar.addActionListener(e -> iniciarBatalha()); inventario.addActionListener(e -> JOptionPane.showMessageDialog(janela, "Inventário gráfico entra na próxima subetapa."));
        personagem.addActionListener(e -> JOptionPane.showMessageDialog(janela, l.getNome() + " | HP " + l.getVida() + "/" + l.getVidaMaxima() + " | Mana " + l.getMana() + "/" + l.getManaMaxima()));
        JPanel acoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6)); acoes.setOpaque(false); acoes.add(explorar); acoes.add(inventario); acoes.add(personagem);
        JPanel sul = new JPanel(new BorderLayout()); sul.setOpaque(false); sul.add(info, BorderLayout.CENTER); sul.add(acoes, BorderLayout.SOUTH); p.add(sul, BorderLayout.SOUTH);
        return p;
    }

    private void iniciarBatalha() {
        inimigoAtual = LoboFactory.criarLobo();
        raiz.add(criarBatalha(), "BATALHA"); telas.show(raiz, "BATALHA");
    }

    private JPanel criarBatalha() {
        JPanel p = base(); p.add(titulo("BATALHA — LOBO SELVAGEM"), BorderLayout.NORTH);
        Personagem heroi = liderAtual();
        JPanel arena = new JPanel(new GridLayout(1, 2, 30, 0)); arena.setOpaque(false);
        arena.add(cardCombatente(heroi.getNome(), "kael".equalsIgnoreCase(heroi.getNome()) ? "kael.png" : "lyra.png", true));
        arena.add(cardCombatente(inimigoAtual.getNome(), "lobo.png", false)); p.add(arena, BorderLayout.CENTER);
        logBatalha = new JTextArea("Um lobo selvagem apareceu!\nO que " + heroi.getNome() + " vai fazer?"); logBatalha.setEditable(false); logBatalha.setLineWrap(true); logBatalha.setWrapStyleWord(true); logBatalha.setRows(4); logBatalha.setFont(NORMAL); logBatalha.setForeground(TEXTO); logBatalha.setBackground(PAINEL); logBatalha.setBorder(new EmptyBorder(10, 14, 10, 14));
        JButton atacar = botao("ATACAR"), habilidade = botao("HABILIDADE"), defender = botao("DEFENDER"), fugir = botao("FUGIR");
        atacar.addActionListener(e -> turnoAtaque(false)); defender.addActionListener(e -> turnoAtaque(true));
        habilidade.addActionListener(e -> logBatalha.append("\nAs habilidades do Model ainda usam entrada de terminal; serão convertidas para botões na próxima subetapa."));
        fugir.addActionListener(e -> telas.show(raiz, "FLORESTA"));
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 9, 6)); botoes.setOpaque(false); botoes.add(atacar); botoes.add(habilidade); botoes.add(defender); botoes.add(fugir);
        JPanel sul = new JPanel(new BorderLayout()); sul.setOpaque(false); sul.add(logBatalha, BorderLayout.CENTER); sul.add(botoes, BorderLayout.SOUTH); p.add(sul, BorderLayout.SOUTH);
        return p;
    }

    private JPanel cardCombatente(String nome, String arquivo, boolean jogador) {
        JPanel c = new JPanel(new BorderLayout(8, 8)); c.setBackground(PAINEL); c.setBorder(BorderFactory.createLineBorder(jogador ? AZUL : OURO, 2));
        JLabel n = new JLabel(nome, SwingConstants.CENTER); n.setForeground(jogador ? AZUL_CLARO : OURO); n.setFont(new Font("Serif", Font.BOLD, 24)); c.add(n, BorderLayout.NORTH);
        JLabel img = new JLabel(imagem(arquivo, jogador ? 220 : 180, 240), SwingConstants.CENTER); c.add(img, BorderLayout.CENTER);
        JPanel barras = new JPanel(new GridLayout(jogador ? 2 : 1, 1, 4, 4)); barras.setOpaque(false);
        if (jogador) {
            Personagem h = liderAtual(); hpJogador = barra(h.getVida(), h.getVidaMaxima(), "HP"); manaJogador = barra(h.getMana(), h.getManaMaxima(), "MANA"); barras.add(hpJogador); barras.add(manaJogador);
        } else { hpInimigo = barra(inimigoAtual.getVida(), inimigoAtual.getVidaMaxima(), "HP"); barras.add(hpInimigo); }
        c.add(barras, BorderLayout.SOUTH); return c;
    }

    private JProgressBar barra(int atual, int max, String texto) {
        JProgressBar b = new JProgressBar(0, max); b.setValue(atual); b.setStringPainted(true); b.setString(texto + " " + atual + "/" + max); b.setForeground(AZUL); b.setBackground(new Color(35, 40, 48)); return b;
    }

    private void turnoAtaque(boolean defendendo) {
        Personagem h = liderAtual();
        if (!h.vivo() || !inimigoAtual.vivo()) return;
        if (!defendendo) {
            int dano = inimigoAtual.receberDano(h.atacar()); logBatalha.append("\n" + h.getNome() + " atacou e causou " + dano + " de dano.");
            hpInimigo.setValue(inimigoAtual.getVida()); hpInimigo.setString("HP " + inimigoAtual.getVida() + "/" + inimigoAtual.getVidaMaxima());
            if (!inimigoAtual.vivo()) { logBatalha.append("\nVitória! O lobo foi derrotado."); return; }
        } else logBatalha.append("\n" + h.getNome() + " assume uma postura defensiva.");
        int ataque = inimigoAtual.atacar(); if (defendendo) ataque = Math.max(1, ataque / 2);
        int recebido = h.receberDano(ataque); logBatalha.append("\nO lobo atacou e causou " + recebido + " de dano.");
        hpJogador.setValue(h.getVida()); hpJogador.setString("HP " + h.getVida() + "/" + h.getVidaMaxima());
        if (!h.vivo()) logBatalha.append("\n" + h.getNome() + " caiu em batalha.");
    }
}
