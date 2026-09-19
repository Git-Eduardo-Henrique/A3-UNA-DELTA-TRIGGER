package gui;

import model.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.util.*;
import util.SaveService;

public class DeltaTriggerGUI {
    private JFrame janela; private final CardLayout telas=new CardLayout(); private final JPanel raiz=new JPanel(telas);
    private Personagem[] grupo; private javax.swing.Timer timerHistoria; private int indiceLetra; private int wave=0, turnoHeroi=0; private boolean batalhaCaverna=false; private int caveWave=0;
    private Inimigo[] inimigos=new Inimigo[0]; private JPanel arenaInimigos, painelGrupo; private LogPane log; private JLabel waveLabel; private JButton proximaWave, botaoItemBatalha;
    private boolean entreOndas=false; private JLabel ouroLojaLabel;
    private Inimigo alvoSelecionado;
    // Estado visual / polimento da batalha
    private final java.util.List<JButton> botoesAcaoBatalha=new ArrayList<JButton>();
    private final Map<Inimigo,JPanel> cardsInimigos=new IdentityHashMap<Inimigo,JPanel>();
    private JPanel cardHeroiAtivo;
    private boolean animandoAcao=false;
    private boolean somLigado=true;
    private boolean animacoesLigadas=true;
    private int velocidadeHistoriaMs=28;
    private int letrasHistoriaPorTick=2;
    // Estado da nova interface de mochila/inventário
    private JDialog mochilaDialog;
    private JPanel mochilaConteudo, mochilaCentro, mochilaGrid, mochilaDetalhes, mochilaEquipamentos;
    private Personagem mochilaHeroi;
    private Object mochilaSelecionado;
    private String mochilaCategoria="TODOS";
    private boolean mochilaEmBatalha=false;
    private static final Color FUNDO=new Color(2,10,24), PAINEL=new Color(5,18,36), AZUL=new Color(36,126,255), AZULC=new Color(126,200,255), OURO=new Color(226,166,58), TEXTO=new Color(235,242,250), VERDE=new Color(80,220,150);
    private static final Font TITULO=new Font("Serif",Font.BOLD,32), NORMAL=new Font("Serif",Font.PLAIN,17);
    private class LogPane extends JTextPane {
        LogPane(){setEditable(false);setFont(new Font("Monospaced",Font.PLAIN,14));setBackground(new Color(2,12,26));setForeground(TEXTO);setBorder(new EmptyBorder(10,12,10,12));}
        public void append(String texto){
            try{
                javax.swing.text.StyledDocument doc=getStyledDocument();
                String[] nomes={"Kael","Lyra","Elyra","Fenrok","Rei Slime","Lobinho","Lobo","Slime"};
                int pos=0;
                while(pos<texto.length()){
                    int melhor=-1;String achado=null;
                    for(String n:nomes){int i=texto.indexOf(n,pos);if(i>=0&&(melhor<0||i<melhor)){melhor=i;achado=n;}}
                    if(melhor<0){doc.insertString(doc.getLength(),texto.substring(pos),estiloLog(null));break;}
                    if(melhor>pos)doc.insertString(doc.getLength(),texto.substring(pos,melhor),estiloLog(null));
                    doc.insertString(doc.getLength(),achado,estiloLog(achado));pos=melhor+achado.length();
                }
                setCaretPosition(doc.getLength());
            }catch(Exception ignored){}
        }
        private javax.swing.text.AttributeSet estiloLog(String nome){
            javax.swing.text.SimpleAttributeSet a=new javax.swing.text.SimpleAttributeSet();
            Color c=TEXTO;
            if("Kael".equals(nome))c=new Color(100,185,255);
            else if("Lyra".equals(nome))c=new Color(220,180,255);
            else if("Elyra".equals(nome))c=new Color(120,230,170);
            else if(nome!=null)c=new Color(255,120,125);
            javax.swing.text.StyleConstants.setForeground(a,c);
            if(nome!=null)javax.swing.text.StyleConstants.setBold(a,true);
            return a;
        }
    }
    private final String historia="Em um mundo fragmentado por antigas guerras, o equilíbrio entre os reinos está ameaçado.\n\nDizem que, nas profundezas das florestas do norte, algo despertou. Os lobos, antes apenas sombras nas montanhas, agora se organizam, mais fortes e mais inteligentes.\n\nKael e Lyra, dois irmãos unidos pelo mesmo propósito, partem para descobrir a origem dos ataques.\n\nE isso é apenas o começo...";

    public void iniciar(){
        janela=new JFrame("Delta Trigger — Aethoria");
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setMinimumSize(new Dimension(1120,700));
        Dimension tela=Toolkit.getDefaultToolkit().getScreenSize();
        int w=Math.min(1500,Math.max(1120,tela.width-80));
        int h=Math.min(940,Math.max(700,tela.height-100));
        janela.setSize(w,h);
        janela.setLocationRelativeTo(null);
        raiz.add(criarMenu(),"MENU");
        raiz.add(criarHistoria(),"HISTORIA");
        raiz.add(criarEscolha(),"ESCOLHA");
        janela.setContentPane(raiz);
        telas.show(raiz,"MENU");
        janela.setVisible(true);
    }
    private JPanel base(){
        JPanel p=new JPanel(new BorderLayout(14,14)){
            @Override protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setPaint(new GradientPaint(0,0,new Color(1,8,20),0,getHeight(),new Color(4,22,46)));
                g2.fillRect(0,0,getWidth(),getHeight());
                g2.dispose();
            }
        };
        p.setOpaque(false);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(116,78,30),1),
                new EmptyBorder(16,20,16,20)));
        return p;
    }
    private JLabel titulo(String s){
        JLabel l=new JLabel("  "+s+"  ",SwingConstants.CENTER);
        l.setFont(new Font("Serif",Font.BOLD,30));
        l.setForeground(AZULC);
        l.setBorder(BorderFactory.createMatteBorder(0,0,2,0,OURO));
        return l;
    }
    private JButton botao(String s){
        JButton b=new JButton(s);
        b.setFont(new Font("Serif",Font.BOLD,16));
        b.setForeground(TEXTO);
        b.setBackground(PAINEL);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(OURO,1),
                new EmptyBorder(9,16,9,16)));
        b.addMouseListener(new java.awt.event.MouseAdapter(){
            public void mouseEntered(java.awt.event.MouseEvent e){if(b.isEnabled()){b.setBackground(new Color(12,46,84));b.setForeground(Color.WHITE);}}
            public void mouseExited(java.awt.event.MouseEvent e){b.setBackground(PAINEL);b.setForeground(TEXTO);}
        });
        return b;
    }
    private File asset(String n){
        java.util.List<File> candidatos=new ArrayList<File>();
        File cwd=new File(System.getProperty("user.dir","."));

        // Caso normal: executar pela raiz do projeto.
        candidatos.add(new File(cwd,"assets/"+n));
        candidatos.add(new File("assets/"+n));

        // Compatibilidade com as versões antigas do projeto.
        candidatos.add(new File(cwd,"delta_gui/assets/"+n));
        candidatos.add(new File("delta_gui/assets/"+n));

        // Procura subindo algumas pastas.
        File atual=cwd;
        for(int i=0;i<6 && atual!=null;i++){
            candidatos.add(new File(atual,"assets/"+n));

            // Também procura em projetos-filhos quando o IntelliJ usa a pasta-pai
            // como Working Directory.
            File[] filhos=atual.listFiles();
            if(filhos!=null){
                for(File filho:filhos){
                    if(filho.isDirectory()){
                        candidatos.add(new File(filho,"assets/"+n));
                    }
                }
            }
            atual=atual.getParentFile();
        }

        // Procura relativamente ao diretório onde os .class estão sendo executados.
        try{
            java.net.URL local=DeltaTriggerGUI.class.getProtectionDomain()
                    .getCodeSource().getLocation();
            if(local!=null){
                File baseCodigo=new File(local.toURI());
                candidatos.add(new File(baseCodigo,"assets/"+n));
                File pai=baseCodigo.getParentFile();
                if(pai!=null)candidatos.add(new File(pai,"assets/"+n));
            }
        }catch(Exception ignored){}

        for(File f:candidatos){
            if(f!=null && f.isFile())return f;
        }

        // Caminho de fallback; imagem() vai retornar null se realmente não existir.
        return new File(cwd,"assets/"+n);
    }
    private ImageIcon imagem(String n,int w,int h){File f=asset(n);if(!f.exists())return null;return new ImageIcon(new ImageIcon(f.getPath()).getImage().getScaledInstance(w,h,Image.SCALE_SMOOTH));}
    private JPanel moldura(){
        JPanel p=new JPanel(new BorderLayout(8,8));
        p.setBackground(new Color(3,14,30));
        p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(104,153,220),1),new EmptyBorder(8,8,8,8)));
        return p;
    }
    private JPanel painelFundo(final String arquivo){
        File f=asset(arquivo);
        final Image fundo=f.exists()?new ImageIcon(f.getPath()).getImage():null;
        JPanel p=new JPanel(new GridBagLayout()){
            @Override protected void paintComponent(Graphics g){
                super.paintComponent(g);
                if(fundo==null)return;
                int pw=getWidth(), ph=getHeight();
                int iw=fundo.getWidth(this), ih=fundo.getHeight(this);
                if(iw<=0||ih<=0)return;
                double escala=Math.max((double)pw/iw,(double)ph/ih);
                int w=(int)Math.round(iw*escala), h=(int)Math.round(ih*escala);
                int x=(pw-w)/2, y=(ph-h)/2;
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2.drawImage(fundo,x,y,w,h,this);
                g2.setPaint(new GradientPaint(0,ph/2,new Color(0,5,15,0),0,ph,new Color(0,5,15,105)));
                g2.fillRect(0,0,pw,ph);
                g2.dispose();
            }
        };
        p.setOpaque(true);p.setBackground(FUNDO);
        return p;
    }
    private JPanel painelFlutuante(){
        JPanel p=new JPanel(new BorderLayout(10,10)){
            @Override protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setColor(new Color(2,10,24,226));
                g2.fillRoundRect(0,0,getWidth(),getHeight(),18,18);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        p.setOpaque(false);
        p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(OURO,2),new EmptyBorder(16,18,16,18)));
        return p;
    }
    private JPanel painelLivroAberto(){
        JPanel p=new JPanel(new BorderLayout(20,20)){
            @Override protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                int w=getWidth(), h=getHeight();
                g2.setColor(new Color(0,0,0,70));
                g2.fillRoundRect(8,12,w-16,h-18,28,28);
                GradientPaint gp=new GradientPaint(0,0,new Color(235,223,188),0,h,new Color(205,186,148));
                g2.setPaint(gp);
                g2.fillRoundRect(0,0,w-8,h-8,28,28);
                g2.setColor(new Color(122,90,42));
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(0,0,w-8,h-8,28,28);
                int meio=(w-8)/2;
                g2.setColor(new Color(120,94,61,70));
                g2.fillRoundRect(meio-8,12,16,h-32,14,14);
                g2.setColor(new Color(255,250,236,70));
                g2.drawLine(meio-2,16,meio-2,h-24);
                g2.drawLine(meio+2,16,meio+2,h-24);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(28,36,28,36));
        return p;
    }
    private JButton cardLocalImagem(String tituloLocal,String descricao,String arquivo){
        final Image img=asset(arquivo).exists()?new ImageIcon(asset(arquivo).getPath()).getImage():null;
        JButton b=new JButton(){
            @Override protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                int w=getWidth(), h=getHeight();
                if(img!=null){
                    int iw=img.getWidth(this), ih=img.getHeight(this);
                    if(iw>0&&ih>0){
                        double escala=Math.max((double)w/iw,(double)h/ih);
                        int dw=(int)Math.round(iw*escala), dh=(int)Math.round(ih*escala);
                        int x=(w-dw)/2, y=(h-dh)/2;
                        g2.drawImage(img,x,y,dw,dh,this);
                    }
                }else{
                    g2.setPaint(new GradientPaint(0,0,new Color(8,24,46),0,h,new Color(3,12,24)));
                    g2.fillRect(0,0,w,h);
                }
                g2.setPaint(new GradientPaint(0,0,new Color(0,4,15,65),0,h,new Color(0,4,15,190)));
                g2.fillRect(0,0,w,h);
                if(getModel().isRollover()){
                    g2.setColor(new Color(255,220,120,45));
                    g2.fillRect(0,0,w,h);
                }
                g2.setColor(OURO);
                g2.drawRect(0,0,w-1,h-1);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setOpaque(false); b.setContentAreaFilled(false); b.setBorderPainted(false); b.setFocusPainted(false); b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setLayout(new BorderLayout());
        JPanel overlay=new JPanel(new BorderLayout()); overlay.setOpaque(false); overlay.setBorder(new EmptyBorder(12,14,14,14));
        JLabel top=new JLabel(tituloLocal,SwingConstants.CENTER); top.setForeground(Color.WHITE); top.setFont(new Font("Serif",Font.BOLD,18));
        JLabel mid=new JLabel("<html><div style='text-align:center;color:#f7f1e2;'>"+descricao+"</div></html>",SwingConstants.CENTER); mid.setForeground(new Color(244,240,228)); mid.setFont(new Font("Serif",Font.PLAIN,13));
        JLabel enter=new JLabel("ENTRAR",SwingConstants.CENTER); enter.setForeground(OURO); enter.setFont(new Font("Serif",Font.BOLD,13));
        overlay.add(top,BorderLayout.NORTH); overlay.add(mid,BorderLayout.CENTER); overlay.add(enter,BorderLayout.SOUTH);
        b.add(overlay);
        return b;
    }
    private JLabel labelLocal(String html){
        JLabel l=new JLabel(html);l.setForeground(TEXTO);l.setFont(NORMAL);return l;
    }
    private void mostrarCard(String nome,JPanel tela){raiz.add(tela,nome);telas.show(raiz,nome);raiz.revalidate();raiz.repaint();}

    private void somFeedback(){if(somLigado)Toolkit.getDefaultToolkit().beep();}

    private void habilitarAcoesBatalha(boolean habilitar){
        animandoAcao=!habilitar;
        for(JButton b:botoesAcaoBatalha)b.setEnabled(habilitar);
    }

    private void mostrarTransicaoCurta(String cabecalho,String detalhe,Color destaque){
        if(!animacoesLigadas)return;
        if(janela==null||!janela.isShowing())return;
        final JDialog d=new JDialog(janela,false);
        d.setUndecorated(true);
        JPanel box=new JPanel();box.setLayout(new BoxLayout(box,BoxLayout.Y_AXIS));box.setBackground(new Color(2,10,24));
        box.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(destaque,2),new EmptyBorder(20,38,20,38)));
        JLabel a=new JLabel(cabecalho,SwingConstants.CENTER);a.setFont(new Font("Serif",Font.BOLD,28));a.setForeground(destaque);a.setAlignmentX(.5f);
        JLabel b=new JLabel(detalhe==null?"":detalhe,SwingConstants.CENTER);b.setFont(new Font("Serif",Font.PLAIN,17));b.setForeground(TEXTO);b.setAlignmentX(.5f);
        box.add(a);if(detalhe!=null&&!detalhe.isEmpty()){box.add(Box.createVerticalStrut(7));box.add(b);}
        d.setContentPane(box);d.pack();d.setLocationRelativeTo(janela);d.setAlwaysOnTop(true);d.setVisible(true);
        javax.swing.Timer t=new javax.swing.Timer(1050,e->{d.dispose();});t.setRepeats(false);t.start();
    }

    private void animarDano(final JComponent alvo,final Color cor,final Runnable depois){
        if(!animacoesLigadas){if(depois!=null)depois.run();return;}
        if(alvo==null){if(depois!=null)depois.run();return;}
        habilitarAcoesBatalha(false);
        final Point original=alvo.getLocation();
        final javax.swing.border.Border bordaOriginal=alvo.getBorder();
        final int[] passo={0};
        javax.swing.Timer t=new javax.swing.Timer(55,null);
        t.addActionListener(e->{
            passo[0]++;
            int dx=(passo[0]%2==0?5:-5);
            alvo.setLocation(original.x+dx,original.y);
            alvo.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(cor,3),new EmptyBorder(4,4,4,4)));
            alvo.repaint();
            if(passo[0]>=6){
                ((javax.swing.Timer)e.getSource()).stop();
                alvo.setLocation(original);alvo.setBorder(bordaOriginal);alvo.repaint();
                if(depois!=null)depois.run();
            }
        });
        t.start();
    }

    private void mostrarOpcoes(){
        final JDialog d=new JDialog(janela,"Opções",true);
        d.setSize(650,520);d.setMinimumSize(new Dimension(610,500));d.setLocationRelativeTo(janela);
        JPanel p=base();p.add(titulo("OPÇÕES"),BorderLayout.NORTH);

        JPanel centro=new JPanel();centro.setOpaque(false);centro.setLayout(new BoxLayout(centro,BoxLayout.Y_AXIS));
        centro.setBorder(new EmptyBorder(16,24,8,24));

        JLabel secAudio=new JLabel("ÁUDIO E EFEITOS");secAudio.setForeground(OURO);secAudio.setFont(new Font("Serif",Font.BOLD,19));secAudio.setAlignmentX(Component.LEFT_ALIGNMENT);
        JCheckBox som=new JCheckBox("Efeitos sonoros do sistema",somLigado);som.setOpaque(false);som.setForeground(TEXTO);som.setFont(NORMAL);som.setAlignmentX(Component.LEFT_ALIGNMENT);
        som.addActionListener(e->somLigado=som.isSelected());
        JCheckBox animacoes=new JCheckBox("Animações de batalha e transições",animacoesLigadas);animacoes.setOpaque(false);animacoes.setForeground(TEXTO);animacoes.setFont(NORMAL);animacoes.setAlignmentX(Component.LEFT_ALIGNMENT);
        animacoes.addActionListener(e->animacoesLigadas=animacoes.isSelected());

        JLabel secTexto=new JLabel("LEITURA E INTERFACE");secTexto.setForeground(OURO);secTexto.setFont(new Font("Serif",Font.BOLD,19));secTexto.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPanel linhaTexto=new JPanel(new BorderLayout(12,0));linhaTexto.setOpaque(false);linhaTexto.setMaximumSize(new Dimension(Integer.MAX_VALUE,42));linhaTexto.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel rotuloVel=new JLabel("Velocidade do texto da história:");rotuloVel.setForeground(TEXTO);rotuloVel.setFont(NORMAL);
        final String[] velocidades={"Lenta","Normal","Rápida","Instantânea"};
        JComboBox<String> velocidade=new JComboBox<String>(velocidades);velocidade.setFont(new Font("Serif",Font.BOLD,15));velocidade.setBackground(new Color(8,25,48));velocidade.setForeground(TEXTO);
        if(velocidadeHistoriaMs==45)velocidade.setSelectedIndex(0);else if(velocidadeHistoriaMs==15)velocidade.setSelectedIndex(2);else if(velocidadeHistoriaMs==0)velocidade.setSelectedIndex(3);else velocidade.setSelectedIndex(1);
        velocidade.addActionListener(e->{int i=velocidade.getSelectedIndex();if(i==0){velocidadeHistoriaMs=45;letrasHistoriaPorTick=1;}else if(i==1){velocidadeHistoriaMs=28;letrasHistoriaPorTick=2;}else if(i==2){velocidadeHistoriaMs=15;letrasHistoriaPorTick=3;}else{velocidadeHistoriaMs=0;letrasHistoriaPorTick=9999;}});
        linhaTexto.add(rotuloVel,BorderLayout.CENTER);linhaTexto.add(velocidade,BorderLayout.EAST);

        JCheckBox maximizar=new JCheckBox("Jogar com a janela maximizada",(janela.getExtendedState()&JFrame.MAXIMIZED_BOTH)!=0);maximizar.setOpaque(false);maximizar.setForeground(TEXTO);maximizar.setFont(NORMAL);maximizar.setAlignmentX(Component.LEFT_ALIGNMENT);
        maximizar.addActionListener(e->{if(maximizar.isSelected())janela.setExtendedState(JFrame.MAXIMIZED_BOTH);else janela.setExtendedState(JFrame.NORMAL);});

        JLabel dica=new JLabel("<html><font color='#7ec8ff'>Dica:</font> as opções valem durante esta execução do jogo. A velocidade escolhida será usada na próxima cena de história.</html>");dica.setForeground(AZULC);dica.setFont(new Font("Serif",Font.PLAIN,14));dica.setAlignmentX(Component.LEFT_ALIGNMENT);

        centro.add(secAudio);centro.add(Box.createVerticalStrut(8));centro.add(som);centro.add(Box.createVerticalStrut(7));centro.add(animacoes);
        centro.add(Box.createVerticalStrut(22));centro.add(secTexto);centro.add(Box.createVerticalStrut(10));centro.add(linhaTexto);centro.add(Box.createVerticalStrut(10));centro.add(maximizar);centro.add(Box.createVerticalStrut(18));centro.add(dica);centro.add(Box.createVerticalGlue());

        JPanel rodape=new JPanel(new FlowLayout(FlowLayout.RIGHT,10,0));rodape.setOpaque(false);
        JButton padrao=botao("RESTAURAR PADRÃO");
        padrao.addActionListener(e->{somLigado=true;animacoesLigadas=true;velocidadeHistoriaMs=28;letrasHistoriaPorTick=2;som.setSelected(true);animacoes.setSelected(true);velocidade.setSelectedIndex(1);maximizar.setSelected(false);janela.setExtendedState(JFrame.NORMAL);});
        JButton fechar=botao("SALVAR E FECHAR");fechar.addActionListener(e->d.dispose());
        rodape.add(padrao);rodape.add(fechar);
        p.add(centro,BorderLayout.CENTER);p.add(rodape,BorderLayout.SOUTH);d.setContentPane(p);d.setVisible(true);
    }

    private void mostrarCreditos(){
        final JDialog d=new JDialog(janela,"Créditos",true);d.setSize(650,520);d.setMinimumSize(new Dimension(610,500));d.setLocationRelativeTo(janela);
        JPanel p=base();p.add(titulo("CRÉDITOS"),BorderLayout.NORTH);
        JPanel c=new JPanel();c.setOpaque(false);c.setLayout(new BoxLayout(c,BoxLayout.Y_AXIS));c.setBorder(new EmptyBorder(18,34,18,34));
        JLabel logo=new JLabel(imagem("logo.png",300,138),SwingConstants.CENTER);logo.setAlignmentX(.5f);
        JLabel nome=new JLabel("DELTA TRIGGER",SwingConstants.CENTER);nome.setForeground(OURO);nome.setFont(new Font("Serif",Font.BOLD,28));nome.setAlignmentX(.5f);
        JLabel info=new JLabel("<html><center>Projeto acadêmico em Java — POO + MVC<br><br><font color='#7ec8ff'><b>Projeto e código-base:</b></font> Equipe Delta Trigger<br><font color='#7ec8ff'><b>Base original:</b></font> versão V3.1 em modo console<br><br><font color='#e2a63a' size='5'><b>Dupla da GUI</b></font><br>Você + ChatGPT (OpenAI)<br><br><font color='#9fb7cf'>Interface, integração visual, inventário, batalhas e polimento construídos em parceria.</font></center></html>",SwingConstants.CENTER);
        info.setForeground(TEXTO);info.setFont(new Font("Serif",Font.PLAIN,17));info.setAlignmentX(.5f);
        JButton fechar=botao("VOLTAR");fechar.setAlignmentX(.5f);fechar.addActionListener(e->d.dispose());
        c.add(logo);c.add(Box.createVerticalStrut(8));c.add(nome);c.add(Box.createVerticalStrut(16));c.add(info);c.add(Box.createVerticalGlue());c.add(fechar);
        p.add(c,BorderLayout.CENTER);d.setContentPane(p);d.setVisible(true);
    }

    private void carregarJogoGUI(){
        SaveService.DadosSave dados=SaveService.carregar();
        if(dados==null){JOptionPane.showMessageDialog(janela,"Não foi possível carregar o save.","Continuar",JOptionPane.ERROR_MESSAGE);return;}
        grupo=dados.grupo;wave=dados.waveFloresta;batalhaCaverna=false;caveWave=0;
        mostrarTransicaoCurta("JOGO CARREGADO",dados.local,OURO);
        if("ELDORIA".equalsIgnoreCase(dados.local))mostrarEldoria();else if("CAVERNA1".equalsIgnoreCase(dados.local))entradaCavernaGUI();else{JPanel f=criarFloresta();mostrarCard("FLORESTA",f);}
    }

    private JProgressBar barra(int valor,int maximo,Color cor,String texto){
        JProgressBar b=new JProgressBar(0,Math.max(1,maximo));
        b.setValue(Math.max(0,Math.min(valor,maximo)));
        b.setString(texto);
        b.setStringPainted(true);
        b.setForeground(cor);
        b.setBackground(new Color(15,24,38));
        b.setBorder(BorderFactory.createLineBorder(new Color(70,90,120),1));
        b.setFont(new Font("SansSerif",Font.BOLD,11));
        b.setPreferredSize(new Dimension(180,20));
        return b;
    }
    private Icon iconeObjeto(final String nome, final int tamanho){
        final String n=nome==null?"":nome.toLowerCase(Locale.ROOT);
        String arqIcon=null;
        if(n.contains("vida")||n.contains("hp"))arqIcon="icon_hp.png";
        else if(n.contains("mana"))arqIcon="icon_mp.png";
        else if(n.contains("espada"))arqIcon="icon_sword.png";
        else if(n.contains("cajado"))arqIcon="icon_staff.png";
        else if(n.contains("arco"))arqIcon="icon_bow.png";
        else if(n.contains("armadura")||n.contains("vestimenta"))arqIcon="icon_armor.png";
        else if(n.contains("bota"))arqIcon="icon_boots.png";
        else if(n.contains("chifre"))arqIcon="icon_horn.png";
        if(arqIcon!=null&&asset(arqIcon).exists())return imagem(arqIcon,tamanho,tamanho);
        return new Icon(){
            public int getIconWidth(){return tamanho;}
            public int getIconHeight(){return tamanho;}
            public void paintIcon(Component c,Graphics g,int x,int y){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(3,14,30));
                g2.fillRoundRect(x,y,tamanho,tamanho,14,14);
                g2.setColor(new Color(45,110,190));
                g2.drawRoundRect(x+1,y+1,tamanho-3,tamanho-3,14,14);
                int cx=x+tamanho/2, cy=y+tamanho/2;
                if(n.contains("vida")||n.contains("hp")){
                    g2.setColor(new Color(210,55,72));
                    g2.fillRoundRect(cx-tamanho/7,cy-tamanho/7,tamanho/3,tamanho/3,8,8);
                    g2.setColor(new Color(238,238,250));
                    g2.fillRect(cx-tamanho/16,cy-tamanho/3,tamanho/8,tamanho/6);
                    g2.setColor(new Color(240,96,115));
                    g2.fillOval(cx-tamanho/5,cy-tamanho/12,tamanho*2/5,tamanho/3);
                }else if(n.contains("mana")){
                    g2.setColor(new Color(48,130,255));
                    Polygon p=new Polygon();p.addPoint(cx,cy-tamanho/3);p.addPoint(cx-tamanho/5,cy+tamanho/6);p.addPoint(cx,cy+tamanho/3);p.addPoint(cx+tamanho/5,cy+tamanho/6);g2.fillPolygon(p);
                    g2.setColor(new Color(120,205,255));g2.drawPolygon(p);
                }else if(n.contains("espada")){
                    g2.setStroke(new BasicStroke(Math.max(3,tamanho/12f),BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
                    g2.setColor(new Color(220,228,240));g2.drawLine(cx-tamanho/4,cy+tamanho/4,cx+tamanho/4,cy-tamanho/4);
                    g2.setColor(OURO);g2.drawLine(cx-tamanho/4,cy+tamanho/7,cx-tamanho/10,cy+tamanho/3);
                    g2.setColor(new Color(120,75,38));g2.drawLine(cx-tamanho/3,cy+tamanho/3,cx-tamanho/5,cy+tamanho/5);
                }else if(n.contains("cajado")){
                    g2.setStroke(new BasicStroke(Math.max(3,tamanho/13f),BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
                    g2.setColor(new Color(132,85,44));g2.drawLine(cx-tamanho/5,cy+tamanho/3,cx+tamanho/7,cy-tamanho/4);
                    g2.setColor(AZULC);g2.fillOval(cx-tamanho/12,cy-tamanho/3,tamanho/4,tamanho/4);
                    g2.setColor(OURO);g2.drawOval(cx-tamanho/12,cy-tamanho/3,tamanho/4,tamanho/4);
                }else if(n.contains("arco")){
                    g2.setColor(new Color(176,116,52));g2.setStroke(new BasicStroke(Math.max(3,tamanho/14f)));
                    g2.drawArc(cx-tamanho/3,cy-tamanho/3,tamanho*2/3,tamanho*2/3,-70,140);
                    g2.setColor(new Color(220,228,240));g2.drawLine(cx+tamanho/5,cy-tamanho/4,cx+tamanho/5,cy+tamanho/4);
                }else if(n.contains("armadura")||n.contains("vestimenta")){
                    Polygon p=new Polygon();p.addPoint(cx-tamanho/4,cy-tamanho/4);p.addPoint(cx,cy-tamanho/6);p.addPoint(cx+tamanho/4,cy-tamanho/4);p.addPoint(cx+tamanho/3,cy+tamanho/5);p.addPoint(cx,cy+tamanho/3);p.addPoint(cx-tamanho/3,cy+tamanho/5);
                    g2.setColor(new Color(92,108,132));g2.fillPolygon(p);g2.setColor(OURO);g2.drawPolygon(p);
                    g2.setColor(AZUL);g2.fillRect(cx-tamanho/16,cy-tamanho/10,tamanho/8,tamanho/4);
                }else if(n.contains("botas")||n.contains("bota")){
                    g2.setColor(new Color(112,70,38));g2.fillRoundRect(cx-tamanho/5,cy-tamanho/4,tamanho/5,tamanho/2,6,6);g2.fillRoundRect(cx,cy-tamanho/5,tamanho/4,tamanho/2,6,6);
                    g2.setColor(OURO);g2.drawRoundRect(cx-tamanho/5,cy-tamanho/4,tamanho/5,tamanho/2,6,6);g2.drawRoundRect(cx,cy-tamanho/5,tamanho/4,tamanho/2,6,6);
                }else if(n.contains("chifre")){
                    g2.setColor(new Color(224,214,184));g2.setStroke(new BasicStroke(Math.max(4,tamanho/11f),BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
                    g2.drawArc(cx-tamanho/4,cy-tamanho/4,tamanho/2,tamanho/2,20,180);
                    g2.setColor(OURO);g2.drawArc(cx-tamanho/4+2,cy-tamanho/4+2,tamanho/2-4,tamanho/2-4,20,180);
                }else{
                    Polygon d=new Polygon();d.addPoint(cx,cy-tamanho/3);d.addPoint(cx+tamanho/4,cy);d.addPoint(cx,cy+tamanho/3);d.addPoint(cx-tamanho/4,cy);
                    g2.setColor(AZUL);g2.fillPolygon(d);g2.setColor(AZULC);g2.drawPolygon(d);
                }
                g2.dispose();
            }
        };
    }

    private boolean estaEquipado(Personagem h,Equipamento e){
        return h!=null&&e!=null&&(e==h.getArmaEquipada()||e==h.getArmaduraEquipada()||e==h.getBotasEquipadas());
    }

    private String bonusEquipamentoHtml(Equipamento e){
        StringBuilder s=new StringBuilder();
        if(e.getForca()!=0)s.append("Força +").append(e.getForca()).append("<br>");
        if(e.getDefesa()!=0)s.append("Defesa +").append(e.getDefesa()).append("<br>");
        if(e.getInteligencia()!=0)s.append("Inteligência +").append(e.getInteligencia()).append("<br>");
        if(e.getResistencia()!=0)s.append("Resistência +").append(e.getResistencia()).append("<br>");
        if(e.getVelocidade()!=0)s.append("Velocidade +").append(e.getVelocidade()).append("<br>");
        if(e.getSorte()!=0)s.append("Sorte +").append(e.getSorte()).append("<br>");
        if(s.length()==0)s.append("Sem bônus adicionais");
        return s.toString();
    }

    private Equipamento equipadoDoMesmoTipo(Personagem h,Equipamento e){
        if(h==null||e==null)return null;String tipo=e.getTipo();
        if("Arma".equalsIgnoreCase(tipo))return h.getArmaEquipada();
        if("Armadura".equalsIgnoreCase(tipo))return h.getArmaduraEquipada();
        if("Botas".equalsIgnoreCase(tipo))return h.getBotasEquipadas();return null;
    }
    private String valorDiff(int atual,int novo,String nome){int d=novo-atual;String c=d>0?"#65df9e":d<0?"#ef6b78":"#9aa9bb";String s=d>0?"+"+d:String.valueOf(d);return nome+": <font color='"+c+"'><b>"+s+"</b></font><br>";}
    private String comparacaoEquipamentoHtml(Personagem h,Equipamento novo){
        Equipamento atual=equipadoDoMesmoTipo(h,novo);if(atual==null)return "<font color='#65df9e'>Slot vazio — qualquer bônus será ganho.</font>";
        return "<b>Comparado com "+atual.getNome()+":</b><br>"+valorDiff(atual.getForca(),novo.getForca(),"Força")+valorDiff(atual.getDefesa(),novo.getDefesa(),"Defesa")+valorDiff(atual.getInteligencia(),novo.getInteligencia(),"Inteligência")+valorDiff(atual.getResistencia(),novo.getResistencia(),"Resistência")+valorDiff(atual.getVelocidade(),novo.getVelocidade(),"Velocidade")+valorDiff(atual.getSorte(),novo.getSorte(),"Sorte");
    }

    private String imagemHeroi(Personagem h){
        if(h.getNome().equals("Kael"))return "kael_portrait_v2.png";
        if(h.getNome().equals("Lyra"))return "lyra_portrait_v2.png";
        return "elyra_portrait_v2.jpg";
    }
    private String imagemHeroiCompleta(String nome){
        if("Kael".equals(nome))return "kael_full_v2.png";
        if("Lyra".equals(nome))return "lyra_full_v2.png";
        return "elyra_full_v2.jpg";
    }
    private String nomeCenarioBatalha(){
        if(batalhaCaverna) return caveWave>=4?"SALÃO REAL — REI SLIME":"CAVERNA DOS SLIMES";
        if(wave<=2)return "FLORESTA DOS LOBOS — TRILHA DOS LOBINHOS";
        if(wave<=4)return "FLORESTA DOS LOBOS — TERRITÓRIO DOS LOBOS";
        return "TERRITÓRIO DE FENROK — ARENA DO CHEFE";
    }

    private JPanel criarMenu(){
        JPanel p=painelFundo("menu_bg.jpg");p.setLayout(new BorderLayout());
        JPanel sombra=new JPanel(new GridBagLayout());sombra.setOpaque(false);
        JPanel c=painelFlutuante();c.setPreferredSize(new Dimension(470,620));c.setLayout(new BoxLayout(c,BoxLayout.Y_AXIS));
        JLabel logo=new JLabel(imagem("logo.png",400,185));logo.setAlignmentX(.5f);
        JLabel sub=new JLabel("UMA JORNADA ENTRE O DESTINO E O CAOS");sub.setForeground(OURO);sub.setFont(NORMAL);sub.setAlignmentX(.5f);
        JButton novo=botao("NOVO JOGO"),continuar=botao("CONTINUAR"),opcoes=botao("OPÇÕES"),creditos=botao("CRÉDITOS"),sair=botao("SAIR");
        for(JButton b:new JButton[]{novo,continuar,opcoes,creditos,sair}){b.setAlignmentX(.5f);b.setMaximumSize(new Dimension(360,52));}
        continuar.setEnabled(SaveService.existeSave());
        novo.addActionListener(e->iniciarHistoria());continuar.addActionListener(e->carregarJogoGUI());opcoes.addActionListener(e->mostrarOpcoes());
        creditos.addActionListener(e->mostrarCreditos());
        sair.addActionListener(e->janela.dispose());
        c.add(Box.createVerticalGlue());c.add(logo);c.add(sub);c.add(Box.createVerticalStrut(24));
        for(JButton b:new JButton[]{novo,continuar,opcoes,creditos,sair}){c.add(b);c.add(Box.createVerticalStrut(9));}
        JLabel save=new JLabel(SaveService.existeSave()?"Save encontrado — Continuar disponível":"Nenhum save encontrado",SwingConstants.CENTER);save.setForeground(new Color(135,165,198));save.setAlignmentX(.5f);c.add(Box.createVerticalStrut(10));c.add(save);c.add(Box.createVerticalGlue());
        GridBagConstraints g=new GridBagConstraints();g.gridx=0;g.gridy=0;g.weightx=1;g.weighty=1;g.anchor=GridBagConstraints.WEST;g.insets=new Insets(30,55,30,30);sombra.add(c,g);p.add(sombra,BorderLayout.CENTER);
        JLabel autoria=new JLabel("desenvolvido por Delta Trigger®  ",SwingConstants.RIGHT);autoria.setForeground(new Color(232,198,126));autoria.setFont(new Font("Serif",Font.ITALIC,14));autoria.setBorder(new EmptyBorder(0,0,12,18));p.add(autoria,BorderLayout.SOUTH);return p;
    }
    private JTextArea textoHistoria; private JButton continuarHistoria;
    private JPanel criarHistoria(){
        JPanel fundo=painelFundo("menu_bg.jpg");
        fundo.setLayout(new BorderLayout(16,16));
        fundo.setBorder(new EmptyBorder(18,20,18,20));
        fundo.add(titulo("HISTÓRIA — CRÔNICAS DE AETHORIA"),BorderLayout.NORTH);

        JPanel centro=new JPanel(new GridBagLayout());
        centro.setOpaque(false);
        JPanel livro=painelLivroAberto();
        livro.setPreferredSize(new Dimension(1080,610));

        JPanel paginas=new JPanel(new GridLayout(1,2,18,0));
        paginas.setOpaque(false);

        JPanel esquerda=new JPanel(new BorderLayout(8,8));
        esquerda.setOpaque(false);
        JLabel capitulo=new JLabel("PRÓLOGO",SwingConstants.CENTER);
        capitulo.setForeground(new Color(96,58,24));
        capitulo.setFont(new Font("Serif",Font.BOLD,28));
        JLabel subtitulo=new JLabel("<html><center>O início da jornada<br/>de Kael e Lyra</center></html>",SwingConstants.CENTER);
        subtitulo.setForeground(new Color(120,84,36));
        subtitulo.setFont(new Font("Serif",Font.PLAIN,18));
        JTextArea intro=new JTextArea("As antigas guerras deixaram cicatrizes profundas em Aethoria.\n\nAgora, algo desperta nas sombras do norte — e dois irmãos serão chamados a enfrentar o que está por vir.");
        intro.setEditable(false); intro.setLineWrap(true); intro.setWrapStyleWord(true); intro.setOpaque(false); intro.setForeground(new Color(86,58,25)); intro.setFont(new Font("Serif",Font.PLAIN,22)); intro.setBorder(new EmptyBorder(20,18,20,18));
        JLabel marca=new JLabel("✦ Delta Trigger ✦",SwingConstants.CENTER); marca.setForeground(new Color(143,97,38)); marca.setFont(new Font("Serif",Font.BOLD,20));
        esquerda.add(capitulo,BorderLayout.NORTH); esquerda.add(subtitulo,BorderLayout.CENTER); esquerda.add(intro,BorderLayout.SOUTH); esquerda.add(marca,BorderLayout.PAGE_END);

        JPanel direita=new JPanel(new BorderLayout(8,8));
        direita.setOpaque(false);
        textoHistoria=new JTextArea();
        textoHistoria.setEditable(false); textoHistoria.setLineWrap(true); textoHistoria.setWrapStyleWord(true);
        textoHistoria.setFont(new Font("Serif",Font.PLAIN,24)); textoHistoria.setForeground(new Color(72,46,18));
        textoHistoria.setOpaque(false); textoHistoria.setBorder(new EmptyBorder(20,18,20,18)); textoHistoria.setMargin(new Insets(0,0,0,0));
        JScrollPane sp=new JScrollPane(textoHistoria);
        sp.setOpaque(false); sp.getViewport().setOpaque(false); sp.setBorder(BorderFactory.createEmptyBorder());
        JLabel pagina=new JLabel("Página 1",SwingConstants.RIGHT); pagina.setForeground(new Color(143,97,38)); pagina.setFont(new Font("Serif",Font.ITALIC,16));
        direita.add(sp,BorderLayout.CENTER); direita.add(pagina,BorderLayout.SOUTH);

        paginas.add(esquerda); paginas.add(direita);
        livro.add(paginas,BorderLayout.CENTER);

        JPanel acoes=new JPanel(new FlowLayout(FlowLayout.CENTER,15,0));
        acoes.setOpaque(false);
        JButton pular=botao("MOSTRAR TEXTO TODO");
        pular.addActionListener(e->finalizarHistoria());
        continuarHistoria=botao("VIRAR A PÁGINA / CONTINUAR");
        continuarHistoria.setEnabled(false);
        continuarHistoria.addActionListener(e->telas.show(raiz,"ESCOLHA"));
        acoes.add(pular); acoes.add(continuarHistoria);
        livro.add(acoes,BorderLayout.SOUTH);

        GridBagConstraints g=new GridBagConstraints();
        g.gridx=0; g.gridy=0; g.weightx=1; g.weighty=1; g.anchor=GridBagConstraints.CENTER; g.fill=GridBagConstraints.NONE;
        centro.add(livro,g);
        fundo.add(centro,BorderLayout.CENTER);
        return fundo;
    }
    private void iniciarHistoria(){textoHistoria.setText("");indiceLetra=0;continuarHistoria.setEnabled(false);telas.show(raiz,"HISTORIA");if(timerHistoria!=null)timerHistoria.stop();if(velocidadeHistoriaMs<=0){finalizarHistoria();return;}timerHistoria=new javax.swing.Timer(velocidadeHistoriaMs,e->{if(indiceLetra<historia.length()){int n=Math.min(letrasHistoriaPorTick,historia.length()-indiceLetra);textoHistoria.append(historia.substring(indiceLetra,indiceLetra+n));indiceLetra+=n;}else{timerHistoria.stop();continuarHistoria.setEnabled(true);}});timerHistoria.start();}
    private void finalizarHistoria(){if(timerHistoria!=null)timerHistoria.stop();textoHistoria.setText(historia);continuarHistoria.setEnabled(true);}
    private JPanel criarEscolha(){JPanel p=base();p.add(titulo("ESCOLHA SEU PERSONAGEM"),BorderLayout.NORTH);JPanel cards=new JPanel(new GridLayout(1,2,24,0));cards.setOpaque(false);cards.add(cardPersonagem("KAEL","O IRMÃO — Guerreiro / Atacante","Força, disciplina e um coração que nunca desiste.",imagemHeroiCompleta("Kael"),1));cards.add(cardPersonagem("LYRA","A IRMÃ — Suporte / Mágica","Conhecimento, empatia e um poder que inspira esperança.",imagemHeroiCompleta("Lyra"),2));p.add(cards);return p;}
    private JPanel cardPersonagem(String nome,String classe,String desc,String arq,int escolha){JPanel c=new JPanel(new BorderLayout(8,8));c.setBackground(PAINEL);c.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(AZUL,2),new EmptyBorder(14,20,14,20)));JLabel n=new JLabel("<html><center><font size='7'>"+nome+"</font><br>"+classe+"</center></html>",SwingConstants.CENTER);n.setForeground(OURO);c.add(n,BorderLayout.NORTH);JLabel foto=new JLabel(imagem(arq,430,500),SwingConstants.CENTER);c.add(foto);JTextArea d=new JTextArea(desc+"\n\n"+(escolha==1?"HP 120 • Mana 30":"HP 90 • Mana 60"));d.setEditable(false);d.setOpaque(false);d.setForeground(TEXTO);d.setFont(NORMAL);d.setLineWrap(true);d.setWrapStyleWord(true);JButton b=botao("SELECIONAR "+nome);b.addActionListener(e->iniciarJogo(escolha));JPanel sul=new JPanel(new BorderLayout(5,5));sul.setOpaque(false);sul.add(d);sul.add(b,BorderLayout.SOUTH);c.add(sul,BorderLayout.SOUTH);return c;}
    private void iniciarJogo(int lider){grupo=new Personagem[]{new Guerreiro(),new Suporte()}; if(lider==2){Personagem t=grupo[0];grupo[0]=grupo[1];grupo[1]=t;} wave=0;JPanel f=criarFloresta();raiz.add(f,"FLORESTA");telas.show(raiz,"FLORESTA");}
    private JPanel criarFloresta(){JPanel p=base();p.add(titulo("FLORESTA DOS LOBOS — ÁREA NORMAL"),BorderLayout.NORTH);JLabel bg=new JLabel(imagem("forest_bg.png",900,440));bg.setBorder(BorderFactory.createLineBorder(AZUL,2));p.add(bg);JTextArea txt=new JTextArea("> Você entrou na Floresta dos Lobos.\nO som da água ecoa pelas rochas. Há pegadas recentes no caminho.\n\nObjetivo: atravesse as ondas de lobos e encontre Fenrok.");txt.setEditable(false);txt.setForeground(TEXTO);txt.setBackground(PAINEL);txt.setFont(NORMAL);txt.setRows(5);txt.setBorder(new EmptyBorder(10,12,10,12));JButton explorar=botao("EXPLORAR / INICIAR ONDAS"), inv=botao("INVENTÁRIO");explorar.addActionListener(e->{wave=0;proximaOnda();});inv.addActionListener(e->mostrarInventario());JPanel bs=new JPanel(new FlowLayout(FlowLayout.LEFT));bs.setOpaque(false);bs.add(explorar);bs.add(inv);JPanel s=new JPanel(new BorderLayout());s.setOpaque(false);s.add(txt);s.add(bs,BorderLayout.SOUTH);p.add(s,BorderLayout.SOUTH);return p;}
    private void proximaOnda(){entreOndas=false;wave++;if(wave==1)inimigos=new Inimigo[]{LoboFactory.criarLobinho(),LoboFactory.criarLobinho(),LoboFactory.criarLobinho()};else if(wave==2)inimigos=new Inimigo[]{LoboFactory.criarLobinho(),LoboFactory.criarLobinho(),LoboFactory.criarLobinho()};else if(wave==3)inimigos=new Inimigo[]{LoboFactory.criarLobo(),LoboFactory.criarLobo()};else if(wave==4)inimigos=new Inimigo[]{LoboFactory.criarLobo(),LoboFactory.criarLobo(),LoboFactory.criarLobo()};else if(wave==5)inimigos=new Inimigo[]{new Fenrok()};else{mostrarTransicao();return;}turnoHeroi=0;JPanel b=criarBatalha();raiz.add(b,"BATALHA");telas.show(raiz,"BATALHA");
        String fase=wave==5?"CHEFE — FENROK":("WAVE "+wave+" / 5");mostrarTransicaoCurta(fase,nomeCenarioBatalha(),wave==5?OURO:AZULC);
    }
    private JPanel criarBatalha(){
        alvoSelecionado=null;
        JPanel p=base();
        String cabecalho=batalhaCaverna
                ? (caveWave<4?"CAVERNA DOS SLIMES — ONDA "+caveWave+"/3":"CHEFE — REI SLIME")
                : (wave<5?"FLORESTA DOS LOBOS — WAVE "+wave+"/5":"CHEFE — FENROK, O LOBO ALFA");
        waveLabel=titulo(cabecalho);
        p.add(waveLabel,BorderLayout.NORTH);

        JPanel corpo=new JPanel(new BorderLayout(14,0));
        corpo.setOpaque(false);

        painelGrupo=new JPanel(new BorderLayout(8,8));
        painelGrupo.setOpaque(false);
        painelGrupo.setPreferredSize(new Dimension(250,560));
        corpo.add(painelGrupo,BorderLayout.WEST);

        JPanel palco=moldura();
        JLabel fase=new JLabel(nomeCenarioBatalha(),SwingConstants.CENTER);
        fase.setForeground(OURO);
        fase.setFont(new Font("Serif",Font.BOLD,17));
        palco.add(fase,BorderLayout.NORTH);

        String bg=batalhaCaverna?"slime_cave_bg.png":(wave==5?"fenrok_bg.png":(wave>=3?"wolf_bg.png":"forest_bg.png"));
        JLabel fundo=new JLabel(imagem(bg,880,315),SwingConstants.CENTER);
        fundo.setOpaque(true);
        fundo.setBackground(Color.BLACK);
        fundo.setBorder(BorderFactory.createLineBorder(wave==5||caveWave==4?OURO:AZUL,2));
        palco.add(fundo,BorderLayout.CENTER);

        arenaInimigos=new JPanel();
        arenaInimigos.setOpaque(false);
        arenaInimigos.setLayout(new GridLayout(1,Math.max(1,inimigos.length),10,0));
        arenaInimigos.setPreferredSize(new Dimension(850,185));
        palco.add(arenaInimigos,BorderLayout.SOUTH);
        corpo.add(palco,BorderLayout.CENTER);
        p.add(corpo,BorderLayout.CENTER);

        log=new LogPane();
        log.setText("A batalha começou!\nVez de "+heroiAtual().getNome()+". Selecione um inimigo e escolha uma ação.");
        JScrollPane scrollLog=new JScrollPane(log);
        scrollLog.setBorder(BorderFactory.createLineBorder(new Color(70,110,165),1));
        scrollLog.setPreferredSize(new Dimension(620,132));

        JButton atacar=botao("⚔ ATACAR"),def=botao("◆ DEFENDER"),hab=botao("✦ HABILIDADE"),inv=botao("▣ ITEM"),fugir=botao("↩ FUGIR");
        botaoItemBatalha=inv;
        botoesAcaoBatalha.clear();Collections.addAll(botoesAcaoBatalha,atacar,hab,def,inv,fugir);
        atacar.addActionListener(e->acaoAtacar());
        def.addActionListener(e->acaoDefender());
        hab.addActionListener(e->acaoHabilidade());
        inv.addActionListener(e->mostrarItensBatalha());
        fugir.addActionListener(e->{
            boolean chefe=(!batalhaCaverna&&wave==5)||(batalhaCaverna&&caveWave==4);
            if(chefe){log.append("\nO chefe bloqueia a fuga!");return;}
            telas.show(raiz,batalhaCaverna?"CAVERNA1":"FLORESTA");
        });
        proximaWave=botao("PRÓXIMA ETAPA");
        proximaWave.setVisible(false);
        proximaWave.addActionListener(e->{if(batalhaCaverna)proximaOndaCaverna();else proximaOnda();});

        JPanel acoes=new JPanel(new GridLayout(2,3,8,8));
        acoes.setOpaque(false);
        for(JButton b:new JButton[]{atacar,hab,def,inv,fugir,proximaWave})acoes.add(b);
        acoes.setPreferredSize(new Dimension(510,132));

        JPanel rodape=new JPanel(new BorderLayout(12,0));
        rodape.setOpaque(false);
        rodape.setBorder(new EmptyBorder(4,0,0,0));
        rodape.add(acoes,BorderLayout.WEST);
        rodape.add(scrollLog,BorderLayout.CENTER);
        p.add(rodape,BorderLayout.SOUTH);

        atualizarCombatentes();
        return p;
    }
    private Personagem heroiAtual(){for(int i=0;i<grupo.length;i++){Personagem h=grupo[(turnoHeroi+i)%grupo.length];if(h.vivo())return h;}return grupo[0];}
    private void atualizarCombatentes(){
        painelGrupo.removeAll();
        Personagem ativo=heroiAtual();

        JPanel cardAtivo=moldura();
        cardHeroiAtivo=cardAtivo;
        cardAtivo.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(OURO,2),new EmptyBorder(10,10,10,10)));
        JLabel turno=new JLabel("TURNO DE "+ativo.getNome().toUpperCase(),SwingConstants.CENTER);
        turno.setForeground(OURO);turno.setFont(new Font("Serif",Font.BOLD,18));
        cardAtivo.add(turno,BorderLayout.NORTH);
        JLabel foto=new JLabel(imagem(imagemHeroiCompleta(ativo.getNome()),190,330),SwingConstants.CENTER);
        cardAtivo.add(foto,BorderLayout.CENTER);
        JPanel stats=new JPanel();stats.setOpaque(false);stats.setLayout(new BoxLayout(stats,BoxLayout.Y_AXIS));
        JLabel nome=new JLabel(ativo.getNome()+"  •  Nv. "+ativo.getNivel(),SwingConstants.CENTER);nome.setForeground(TEXTO);nome.setAlignmentX(.5f);
        JProgressBar hp=barra(ativo.getVida(),ativo.getVidaMaxima(),new Color(205,58,72),"HP  "+ativo.getVida()+" / "+ativo.getVidaMaxima());
        JProgressBar mp=barra(ativo.getMana(),ativo.getManaMaxima(),new Color(40,130,245),"MP  "+ativo.getMana()+" / "+ativo.getManaMaxima());
        hp.setAlignmentX(.5f);mp.setAlignmentX(.5f);
        stats.add(nome);stats.add(Box.createVerticalStrut(6));stats.add(hp);stats.add(Box.createVerticalStrut(5));stats.add(mp);
        cardAtivo.add(stats,BorderLayout.SOUTH);
        painelGrupo.add(cardAtivo,BorderLayout.CENTER);

        JPanel equipe=moldura();
        equipe.setLayout(new GridLayout(Math.max(1,grupo.length-1),1,5,5));
        JLabel et=new JLabel("EQUIPE",SwingConstants.CENTER);et.setForeground(AZULC);et.setFont(new Font("Serif",Font.BOLD,14));
        JPanel aliadosWrap=new JPanel(new BorderLayout(3,3));aliadosWrap.setOpaque(false);aliadosWrap.add(et,BorderLayout.NORTH);
        JPanel cardsAliados=new JPanel(new GridLayout(Math.max(1,grupo.length-1),1,4,4));cardsAliados.setOpaque(false);
        for(Personagem h:grupo){
            if(h==ativo)continue;
            JPanel mini=new JPanel(new BorderLayout(7,2));mini.setBackground(new Color(5,18,36));
            mini.setBorder(BorderFactory.createLineBorder(h.vivo()?new Color(55,115,185):Color.DARK_GRAY,1));
            mini.add(new JLabel(imagem(imagemHeroi(h),58,58),SwingConstants.CENTER),BorderLayout.WEST);
            JPanel ms=new JPanel();ms.setOpaque(false);ms.setLayout(new BoxLayout(ms,BoxLayout.Y_AXIS));
            JLabel mn=new JLabel(h.getNome()+" • Nv. "+h.getNivel());mn.setForeground(h.vivo()?TEXTO:Color.GRAY);
            ms.add(mn);
            ms.add(barra(h.getVida(),h.getVidaMaxima(),new Color(205,58,72),"HP "+h.getVida()+"/"+h.getVidaMaxima()));
            ms.add(barra(h.getMana(),h.getManaMaxima(),new Color(40,130,245),"MP "+h.getMana()+"/"+h.getManaMaxima()));
            mini.add(ms,BorderLayout.CENTER);cardsAliados.add(mini);
        }
        if(grupo.length<=1){JLabel solo=new JLabel("Sem aliados",SwingConstants.CENTER);solo.setForeground(Color.GRAY);cardsAliados.add(solo);}
        aliadosWrap.add(cardsAliados,BorderLayout.CENTER);
        aliadosWrap.setPreferredSize(new Dimension(230,130));
        painelGrupo.add(aliadosWrap,BorderLayout.SOUTH);

        java.util.List<Inimigo> vivos=new ArrayList<>();
        for(Inimigo e:inimigos)if(e.vivo())vivos.add(e);
        if(alvoSelecionado==null||!alvoSelecionado.vivo())alvoSelecionado=vivos.isEmpty()?null:vivos.get(0);

        arenaInimigos.removeAll();cardsInimigos.clear();
        int qtd=Math.max(1,inimigos.length);
        int iw=qtd>=5?78:qtd==4?92:qtd==3?108:qtd==2?125:175;
        int ih=qtd>=5?82:qtd==4?100:qtd==3?118:qtd==2?132:155;
        for(Inimigo e:inimigos){
            JPanel ci=new JPanel(new BorderLayout(3,3));
            ci.setBackground(new Color(3,14,29));
            boolean selecionado=e==alvoSelecionado&&e.vivo();
            Color borda=selecionado?OURO:(e instanceof Fenrok||e.getNome().equals("Rei Slime")?new Color(165,72,220):new Color(40,105,175));
            ci.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(borda,selecionado?3:1),new EmptyBorder(5,5,5,5)));
            String arqi=e instanceof Fenrok?"fenrok.png":e.getNome().equals("Lobinho")?"lobinho_transparente.png":e.getNome().equals("Slime")?"slime_better.png":e.getNome().equals("Rei Slime")?"rei_slime.png":"lobo_transparente.png";
            JLabel img=new JLabel(imagem(arqi,iw,ih),SwingConstants.CENTER);ci.add(img,BorderLayout.CENTER);
            JPanel ei=new JPanel();ei.setOpaque(false);ei.setLayout(new BoxLayout(ei,BoxLayout.Y_AXIS));
            JLabel en=new JLabel(e.getNome(),SwingConstants.CENTER);en.setForeground(e.vivo()?TEXTO:Color.GRAY);en.setAlignmentX(.5f);
            JProgressBar ehp=barra(e.getVida(),e.getVidaMaxima(),new Color(205,58,72),e.vivo()?"HP "+e.getVida()+"/"+e.getVidaMaxima():"DERROTADO");
            ehp.setAlignmentX(.5f);ei.add(en);ei.add(ehp);
            if(selecionado){JLabel al=new JLabel("ALVO",SwingConstants.CENTER);al.setForeground(OURO);al.setFont(new Font("SansSerif",Font.BOLD,11));al.setAlignmentX(.5f);ei.add(al);}
            ci.add(ei,BorderLayout.SOUTH);
            if(e.vivo()){
                ci.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                ci.addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent ev){alvoSelecionado=e;atualizarCombatentes();if(log!=null)log.append("\nAlvo selecionado: "+e.getNome()+".");}});
            }
            cardsInimigos.put(e,ci);
            arenaInimigos.add(ci);
        }
        painelGrupo.revalidate();painelGrupo.repaint();arenaInimigos.revalidate();arenaInimigos.repaint();
    }
    private Inimigo escolherAlvo(){
        java.util.List<Inimigo> vivos=new ArrayList<>();for(Inimigo e:inimigos)if(e.vivo())vivos.add(e);
        if(vivos.isEmpty())return null;
        if(alvoSelecionado!=null&&alvoSelecionado.vivo())return alvoSelecionado;
        alvoSelecionado=vivos.get(0);
        return alvoSelecionado;
    }
    private void acaoAtacar(){
        if(animandoAcao)return;Personagem h=heroiAtual();Inimigo alvo=escolherAlvo();if(alvo==null)return;
        int d=alvo.receberDano(h.atacar());log.append("\n⚔ "+h.getNome()+" atacou "+alvo.getNome()+" e causou "+d+" de dano.");somFeedback();
        JPanel card=cardsInimigos.get(alvo);animarDano(card,new Color(230,70,80),()->fimAcao());
    }
    private void acaoDefender(){
        if(animandoAcao)return;Personagem h=heroiAtual();int antes=h.getVida();h.defender();h.curar(Math.max(3,h.getVidaMaxima()/20));int cura=h.getVida()-antes;
        log.append("\n◆ "+h.getNome()+" assume postura defensiva"+(cura>0?" e recupera "+cura+" HP.":"."));
        mostrarTransicaoCurta("DEFESA",h.getNome()+" reduziu o próximo dano"+(cura>0?" e recuperou "+cura+" HP.":"."),AZULC);fimAcao();
    }
    private void acaoHabilidade(){
        if(animandoAcao)return;
        Personagem h=heroiAtual();
        if("Kael".equals(h.getNome())){
            String[] op={"Ataque Bruto — 5 Mana","Investida de Armadura — 4 Mana","Voltar"};
            int esc=JOptionPane.showOptionDialog(janela,"Escolha a habilidade de Kael:","Habilidades",JOptionPane.DEFAULT_OPTION,JOptionPane.PLAIN_MESSAGE,null,op,op[0]);
            if(esc<0||esc==2)return;int custo=esc==0?5:4;if(h.getMana()<custo){log.append("\nMana insuficiente: são necessários "+custo+" pontos.");return;}
            Inimigo alvo=escolherAlvo();if(alvo==null)return;h.usarMana(custo);int bruto;
            if(esc==0)bruto=Math.max(1,h.getForca()*3+new Random().nextInt(5)-alvo.getDefesa()/2);
            else{bruto=Math.max(1,h.getForca()*2+h.getDefesa()/2+new Random().nextInt(4)-alvo.getDefesa()/2);h.defender();}
            int d=alvo.receberDano(bruto);log.append("\n✦ Kael usa "+(esc==0?"Ataque Bruto":"Investida de Armadura")+" em "+alvo.getNome()+" e causa "+d+" de dano."+(esc==1?" Kael também fica protegido.":""));
            somFeedback();animarDano(cardsInimigos.get(alvo),new Color(92,155,255),()->fimAcao());return;
        }
        if("Lyra".equals(h.getNome())){
            String[] op={"Cura Individual — 8 Mana","Cura em Grupo — 14 Mana","Voltar"};
            int esc=JOptionPane.showOptionDialog(janela,"Escolha a magia de Lyra:","Habilidades",JOptionPane.DEFAULT_OPTION,JOptionPane.PLAIN_MESSAGE,null,op,op[0]);
            if(esc<0||esc==2)return;
            if(esc==0){
                if(h.getMana()<8){log.append("\nMana insuficiente: são necessários 8 pontos.");return;}
                Personagem alvoCura=escolherHeroiGUI("Cura Individual — escolha o aliado");if(alvoCura==null)return;if(alvoCura.getVida()>=alvoCura.getVidaMaxima()){log.append("\nEsse aliado já está com HP máximo.");return;}
                h.usarMana(8);int antes=alvoCura.getVida();alvoCura.curar(18+h.getInteligencia()*2);int cura=alvoCura.getVida()-antes;log.append("\n✦ Lyra usa Cura Individual: "+alvoCura.getNome()+" recupera "+cura+" HP.");mostrarTransicaoCurta("CURA INDIVIDUAL","+"+cura+" HP em "+alvoCura.getNome(),VERDE);fimAcao();return;
            }
            boolean ferido=false;for(Personagem x:grupo)if(x.vivo()&&x.getVida()<x.getVidaMaxima())ferido=true;if(!ferido){log.append("\nTodos os aliados vivos já estão com HP máximo.");return;}
            if(h.getMana()<14){log.append("\nMana insuficiente: são necessários 14 pontos.");return;}h.usarMana(14);int cura=12+h.getInteligencia();for(Personagem x:grupo)if(x.vivo())x.curar(cura);log.append("\n✦ Lyra usa Cura em Grupo e restaura até "+cura+" HP de todos os aliados vivos.");mostrarTransicaoCurta("CURA EM GRUPO","O grupo foi restaurado.",VERDE);fimAcao();return;
        }
        String[] op={"Flecha Arcana — 6 Mana","Flecha de Gelo — 8 Mana","Voltar"};
        int esc=JOptionPane.showOptionDialog(janela,"Escolha a habilidade de Elyra:","Habilidades",JOptionPane.DEFAULT_OPTION,JOptionPane.PLAIN_MESSAGE,null,op,op[0]);if(esc<0||esc==2)return;int custo=esc==0?6:8;if(h.getMana()<custo){log.append("\nMana insuficiente: são necessários "+custo+" pontos.");return;}
        Inimigo alvo=escolherAlvo();if(alvo==null)return;h.usarMana(custo);int bruto=esc==0?Math.max(1,h.getForca()*2+h.getInteligencia()*2+new Random().nextInt(5)-alvo.getDefesa()/2):Math.max(1,h.getInteligencia()*3+new Random().nextInt(4)-alvo.getDefesa()/2);int d=alvo.receberDano(bruto);log.append("\n✦ Elyra usa "+(esc==0?"Flecha Arcana":"Flecha de Gelo")+" em "+alvo.getNome()+" e causa "+d+" de dano.");somFeedback();animarDano(cardsInimigos.get(alvo),new Color(92,155,255),()->fimAcao());
    }
    private void fimAcao(){
        atualizarCombatentes();
        if(!haInimigoVivo()){habilitarAcoesBatalha(true);vitoriaOnda();return;}
        int atual=turnoHeroi;int proximo=-1;
        for(int i=atual+1;i<grupo.length;i++)if(grupo[i].vivo()){proximo=i;break;}
        if(proximo>=0){turnoHeroi=proximo;if(alvoSelecionado!=null&&!alvoSelecionado.vivo())alvoSelecionado=null;atualizarCombatentes();log.append("\n→ Vez de "+heroiAtual().getNome()+".");habilitarAcoesBatalha(true);return;}
        // Todos os heróis vivos já jogaram: agora vem a fase dos inimigos.
        ataqueInimigo();
        if(!haHeroiVivo()){habilitarAcoesBatalha(false);mostrarGameOver();return;}
        turnoHeroi=0;while(turnoHeroi<grupo.length&&!grupo[turnoHeroi].vivo())turnoHeroi++;if(turnoHeroi>=grupo.length)turnoHeroi=0;
        if(alvoSelecionado!=null&&!alvoSelecionado.vivo())alvoSelecionado=null;atualizarCombatentes();log.append("\n→ Nova rodada. Vez de "+heroiAtual().getNome()+".");habilitarAcoesBatalha(true);
    }

    private void mostrarGameOver(){
        somFeedback();
        JPanel p=painelFundo(batalhaCaverna?"slime_cave_bg.png":"wolf_bg.png");p.setLayout(new GridBagLayout());
        JPanel box=painelFlutuante();box.setPreferredSize(new Dimension(560,390));
        JLabel t=new JLabel("GAME OVER",SwingConstants.CENTER);t.setForeground(new Color(235,74,88));t.setFont(new Font("Serif",Font.BOLD,42));box.add(t,BorderLayout.NORTH);
        JLabel msg=new JLabel("<html><center>O grupo foi derrotado.<br><br>Vocês podem se recuperar e tentar a área novamente.</center></html>",SwingConstants.CENTER);msg.setForeground(TEXTO);msg.setFont(new Font("Serif",Font.PLAIN,20));box.add(msg,BorderLayout.CENTER);
        JPanel bs=new JPanel(new GridLayout(0,1,8,8));bs.setOpaque(false);JButton tentar=botao("TENTAR NOVAMENTE"),menu=botao("MENU PRINCIPAL");
        tentar.addActionListener(e->{for(Personagem h:grupo)h.recuperarTudo();if(batalhaCaverna){caveWave=Math.max(0,caveWave-1);proximaOndaCaverna();}else{wave=Math.max(0,wave-1);proximaOnda();}});menu.addActionListener(e->{for(Personagem h:grupo)h.recuperarTudo();telas.show(raiz,"MENU");});bs.add(tentar);bs.add(menu);box.add(bs,BorderLayout.SOUTH);
        GridBagConstraints g=new GridBagConstraints();g.gridx=0;g.gridy=0;p.add(box,g);mostrarCard("GAMEOVER",p);
    }
    private void ataqueInimigo(){java.util.List<Personagem> vivos=new ArrayList<>();for(Personagem h:grupo)if(h.vivo())vivos.add(h);if(vivos.isEmpty())return;Random r=new Random();for(Inimigo e:inimigos)if(e.vivo()){Personagem alvo=vivos.get(r.nextInt(vivos.size()));int d=alvo.receberDano(e.atacar());log.append("\n"+e.getNome()+" ataca "+alvo.getNome()+" e causa "+d+" de dano.");if(!alvo.vivo()){log.append(" "+alvo.getNome()+" caiu!");vivos.remove(alvo);if(vivos.isEmpty())break;}}}
    private boolean haInimigoVivo(){for(Inimigo e:inimigos)if(e.vivo())return true;return false;} private boolean haHeroiVivo(){for(Personagem h:grupo)if(h.vivo())return true;return false;}
    private void vitoriaOnda(){
        entreOndas=true;
        int ouro=0;for(Inimigo e:inimigos){ouro+=e.getRecompensa();for(Item it:e.sortearDrops())grupo[0].getInventario().adicionarItem(it);Item m=e.sortearMaterial();if(m!=null)grupo[0].getInventario().adicionarItem(m);}
        grupo[0].adicionarDinheiro(ouro);log.append("\n\n★ VITÓRIA! Recompensa: $"+ouro+" e possíveis itens/materiais adicionados ao inventário.");
        somFeedback();mostrarTransicaoCurta("VITÓRIA","Recompensa: $"+ouro,OURO);
        for(JButton b:botoesAcaoBatalha)b.setEnabled(false);
        if(botaoItemBatalha!=null){botaoItemBatalha.setEnabled(true);botaoItemBatalha.setText("▣ MOCHILA / CURAR");}
        if(batalhaCaverna){
            if(caveWave==4){log.append("\nO Rei Slime foi derrotado! A Caverna 1 está concluída.");proximaWave.setText("CONCLUIR CAPÍTULO");}
            else proximaWave.setText("PRÓXIMA ONDA");
        }else if(wave==5){
            for(Personagem h:grupo)h.subirNivel();log.append("\nFenrok foi derrotado. O grupo subiu de nível e recebeu 3 pontos de atributo por personagem!");
            mostrarTransicaoCurta("LEVEL UP!","Todos receberam 3 pontos de atributo.",AZULC);proximaWave.setText("CONTINUAR PARA ELDORIA");
        }else proximaWave.setText("PRÓXIMA ONDA");
        proximaWave.setVisible(true);proximaWave.setEnabled(true);atualizarCombatentes();
    }
    private void mostrarItensBatalha(){
        abrirMochila(true);
    }

    private JPanel cabecalhoMochila(){
        JPanel topo=new JPanel(new BorderLayout(10,8));
        topo.setOpaque(false);
        JLabel t=titulo(mochilaEmBatalha?"MOCHILA — ITEM DE BATALHA":"MOCHILA / INVENTÁRIO");
        topo.add(t,BorderLayout.NORTH);
        JPanel herois=new JPanel(new FlowLayout(FlowLayout.CENTER,8,2));
        herois.setOpaque(false);
        for(Personagem h:grupo){
            JButton b=botao(h.getNome().toUpperCase());
            b.setIcon(imagem(imagemHeroi(h),38,38));
            b.setHorizontalTextPosition(SwingConstants.RIGHT);
            b.setEnabled(!mochilaEmBatalha||h==heroiAtual());
            if(h==mochilaHeroi){b.setBackground(new Color(13,55,96));b.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(OURO,2),new EmptyBorder(7,12,7,12)));}
            b.addActionListener(e->{mochilaHeroi=h;mochilaSelecionado=null;atualizarMochila();});
            herois.add(b);
        }
        topo.add(herois,BorderLayout.SOUTH);
        return topo;
    }

    private JPanel painelPerfilMochila(){
        JPanel p=moldura();
        p.setPreferredSize(new Dimension(245,565));
        JLabel nome=new JLabel("<html><center><font size='6'>"+mochilaHeroi.getNome()+"</font><br>Nível "+mochilaHeroi.getNivel()+"</center></html>",SwingConstants.CENTER);
        nome.setForeground(OURO);p.add(nome,BorderLayout.NORTH);
        p.add(new JLabel(imagem(imagemHeroi(mochilaHeroi),205,205),SwingConstants.CENTER),BorderLayout.CENTER);
        JPanel inf=new JPanel();inf.setOpaque(false);inf.setLayout(new BoxLayout(inf,BoxLayout.Y_AXIS));
        JProgressBar hp=barra(mochilaHeroi.getVida(),mochilaHeroi.getVidaMaxima(),new Color(205,58,72),"HP  "+mochilaHeroi.getVida()+" / "+mochilaHeroi.getVidaMaxima());
        JProgressBar mp=barra(mochilaHeroi.getMana(),mochilaHeroi.getManaMaxima(),new Color(40,130,245),"MP  "+mochilaHeroi.getMana()+" / "+mochilaHeroi.getManaMaxima());
        hp.setAlignmentX(.5f);mp.setAlignmentX(.5f);inf.add(hp);inf.add(Box.createVerticalStrut(6));inf.add(mp);inf.add(Box.createVerticalStrut(12));
        JLabel ouro=new JLabel("Ouro: $"+mochilaHeroi.getDinheiro(),SwingConstants.CENTER);ouro.setForeground(OURO);ouro.setAlignmentX(.5f);inf.add(ouro);inf.add(Box.createVerticalStrut(10));
        JButton attrs=botao("ATRIBUTOS");attrs.setAlignmentX(.5f);attrs.addActionListener(e->{distribuirPontosGUI();atualizarMochila();});inf.add(attrs);
        p.add(inf,BorderLayout.SOUTH);
        return p;
    }

    private JPanel slotEquip(String tituloSlot,Equipamento e){
        JPanel s=new JPanel(new BorderLayout(6,4));s.setBackground(new Color(3,14,29));s.setBorder(BorderFactory.createLineBorder(e==null?new Color(55,82,115):OURO,1));
        JLabel ic=new JLabel(e==null?iconeObjeto(tituloSlot,34):iconeObjeto(e.getNome(),34));s.add(ic,BorderLayout.WEST);
        JLabel tx=new JLabel("<html><b>"+tituloSlot+"</b><br>"+(e==null?"<font color='#74859b'>Vazio</font>":e.getNome())+"</html>");tx.setForeground(TEXTO);s.add(tx,BorderLayout.CENTER);
        return s;
    }

    private JPanel painelEquipamentosMochila(){
        JPanel p=moldura();p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));p.setPreferredSize(new Dimension(245,200));
        JLabel t=new JLabel("EQUIPADO",SwingConstants.CENTER);t.setForeground(AZULC);t.setFont(new Font("Serif",Font.BOLD,16));t.setAlignmentX(.5f);p.add(t);p.add(Box.createVerticalStrut(7));
        p.add(slotEquip("ARMA",mochilaHeroi.getArmaEquipada()));p.add(Box.createVerticalStrut(5));
        p.add(slotEquip("ARMADURA",mochilaHeroi.getArmaduraEquipada()));p.add(Box.createVerticalStrut(5));
        p.add(slotEquip("BOTAS",mochilaHeroi.getBotasEquipadas()));
        return p;
    }

    private java.util.List<Object> objetosMochila(){
        java.util.List<Object> r=new ArrayList<Object>();
        if(!"EQUIPAMENTOS".equals(mochilaCategoria)) for(Item i:mochilaHeroi.getInventario().getItens()) if(!mochilaEmBatalha||i.utilizavelEmBatalha())r.add(i);
        if(!mochilaEmBatalha&&!"CONSUMÍVEIS".equals(mochilaCategoria))r.addAll(mochilaHeroi.getInventario().getEquipamentos());
        return r;
    }

    private JButton slotMochila(Object o){
        String nome=o instanceof Item?((Item)o).getNome():((Equipamento)o).getNome();
        String extra=o instanceof Item?"x"+((Item)o).getQuantidade():(((Equipamento)o).getTipo());
        JButton b=new JButton("<html><center>"+nome+"<br><font color='#8fb8e8'>"+extra+"</font></center></html>",iconeObjeto(nome,44));
        b.setVerticalTextPosition(SwingConstants.BOTTOM);b.setHorizontalTextPosition(SwingConstants.CENTER);
        b.setForeground(TEXTO);b.setBackground(new Color(4,18,37,185));b.setFocusPainted(false);b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boolean sel=o==mochilaSelecionado;boolean eq=o instanceof Equipamento&&estaEquipado(mochilaHeroi,(Equipamento)o);
        b.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(sel?OURO:(eq?AZULC:new Color(44,91,145)),sel?3:1),new EmptyBorder(5,4,5,4)));
        b.addActionListener(e->{mochilaSelecionado=o;atualizarMochila();});
        return b;
    }

    private JPanel painelGradeMochila(){
        JPanel wrap=new JPanel(new BorderLayout(8,8));wrap.setOpaque(false);
        JPanel cats=new JPanel(new FlowLayout(FlowLayout.LEFT,7,0));cats.setOpaque(false);
        String[] nomes=mochilaEmBatalha?new String[]{"CONSUMÍVEIS"}:new String[]{"TODOS","CONSUMÍVEIS","EQUIPAMENTOS"};
        for(String c:nomes){JButton b=botao(c);b.setPreferredSize(new Dimension(150,42));b.setBackground(c.equals(mochilaCategoria)?new Color(12,50,90,210):new Color(3,18,36,165));if(c.equals(mochilaCategoria)){b.setBorder(BorderFactory.createLineBorder(OURO,2));}b.addActionListener(e->{mochilaCategoria=c;mochilaSelecionado=null;atualizarMochila();});cats.add(b);}wrap.add(cats,BorderLayout.NORTH);
        mochilaGrid=new JPanel(new GridLayout(4,5,8,8));mochilaGrid.setOpaque(false);
        java.util.List<Object> obs=objetosMochila();
        for(int i=0;i<20;i++){
            if(i<obs.size())mochilaGrid.add(slotMochila(obs.get(i)));
            else{JPanel vazio=new JPanel();vazio.setBackground(new Color(3,13,28,165));vazio.setBorder(BorderFactory.createLineBorder(new Color(30,58,92),1));mochilaGrid.add(vazio);}
        }
        JPanel mold=moldura();mold.add(mochilaGrid,BorderLayout.CENTER);
        JLabel cap=new JLabel("MOCHILA  •  "+obs.size()+" / 20 slots",SwingConstants.RIGHT);cap.setForeground(new Color(130,160,195));mold.add(cap,BorderLayout.SOUTH);
        wrap.add(mold,BorderLayout.CENTER);return wrap;
    }

    private JPanel painelDetalhesMochila(){
        JPanel p=moldura();p.setPreferredSize(new Dimension(285,565));
        if(mochilaSelecionado==null){
            JLabel v=new JLabel("<html><center><font size='5'>SELECIONE UM ITEM</font><br><br><font color='#7c93ad'>Clique em um slot da mochila<br>para ver detalhes e ações.</font></center></html>",SwingConstants.CENTER);v.setForeground(AZULC);p.add(v);return p;
        }
        String nome=mochilaSelecionado instanceof Item?((Item)mochilaSelecionado).getNome():((Equipamento)mochilaSelecionado).getNome();
        
        JPanel centro=new JPanel();centro.setOpaque(false);centro.setLayout(new BoxLayout(centro,BoxLayout.Y_AXIS));
        JLabel n=new JLabel("<html><center><font size='6'>"+nome+"</font></center></html>",SwingConstants.CENTER);n.setForeground(OURO);n.setAlignmentX(.5f);centro.add(n);centro.add(Box.createVerticalStrut(8));
        if(mochilaSelecionado instanceof Item){
            Item it=(Item)mochilaSelecionado;
            String efeito=it.getCuraHp()>0?"Recupera "+it.getCuraHp()+" HP":it.getCuraMana()>0?"Recupera "+it.getCuraMana()+" Mana":"Material / item de venda";
            JLabel d=new JLabel("<html><center>Tipo: "+it.getTipo()+"<br>Quantidade: x"+it.getQuantidade()+"<br>Valor: $"+it.getPreco()+"<br><br><font color='#9fc6ef'>"+efeito+"</font></center></html>",SwingConstants.CENTER);d.setForeground(TEXTO);d.setAlignmentX(.5f);centro.add(d);
        }else{
            Equipamento eq=(Equipamento)mochilaSelecionado;
            JLabel d=new JLabel("<html><center>Tipo: "+eq.getTipo()+"<br>Valor: $"+eq.getPreco()+"<br><br><font color='#9fc6ef'>"+bonusEquipamentoHtml(eq)+"</font>"+(estaEquipado(mochilaHeroi,eq)?"<br><br><font color='#e2a63a'><b>EQUIPADO</b></font>":"")+"<br><br><hr><b>COMPARAÇÃO</b><br>"+comparacaoEquipamentoHtml(mochilaHeroi,eq)+"</center></html>",SwingConstants.CENTER);d.setForeground(TEXTO);d.setAlignmentX(.5f);centro.add(d);
        }
        p.add(centro,BorderLayout.CENTER);
        JPanel acoes=new JPanel(new GridLayout(0,1,5,5));acoes.setOpaque(false);
        if(mochilaSelecionado instanceof Item){
            Item it=(Item)mochilaSelecionado;JButton usar=botao("USAR ITEM");usar.setEnabled(it.utilizavelEmBatalha());usar.addActionListener(e->usarItemDaMochila(it));acoes.add(usar);
        }else if(!mochilaEmBatalha){
            Equipamento eq=(Equipamento)mochilaSelecionado;boolean equipado=estaEquipado(mochilaHeroi,eq);JButton equipar=botao(equipado?"DESEQUIPAR":"EQUIPAR");equipar.addActionListener(e->{if(equipado)mochilaHeroi.desequipar(eq);else{if(!podeUsarEquipamento(mochilaHeroi,eq)){JOptionPane.showMessageDialog(mochilaDialog,mensagemClasse(mochilaHeroi,eq),"Classe incompatível",JOptionPane.WARNING_MESSAGE);return;}mochilaHeroi.equipar(eq);}somFeedback();atualizarMochila();});acoes.add(equipar);
        }
        p.add(acoes,BorderLayout.SOUTH);return p;
    }

    private void usarItemDaMochila(Item it){
        if(it==null||!it.utilizavelEmBatalha())return;
        Personagem alvo=escolherHeroiGUI("Usar "+it.getNome()+" em quem?");if(alvo==null)return;
        if(!alvo.vivo()){JOptionPane.showMessageDialog(mochilaDialog,"Esse personagem está derrotado.");return;}
        boolean util=(it.getCuraHp()>0&&alvo.getVida()<alvo.getVidaMaxima())||(it.getCuraMana()>0&&alvo.getMana()<alvo.getManaMaxima());
        if(!util){JOptionPane.showMessageDialog(mochilaDialog,"O item não teria efeito agora e não foi consumido.");return;}
        int hp0=alvo.getVida(),mp0=alvo.getMana();alvo.curar(it.getCuraHp());alvo.curarMana(it.getCuraMana());mochilaHeroi.getInventario().removerUmaUnidade(it);
        if(mochilaEmBatalha){
            if(log!=null)log.append("\n"+heroiAtual().getNome()+" usou "+it.getNome()+" em "+alvo.getNome()+" (HP +"+(alvo.getVida()-hp0)+", Mana +"+(alvo.getMana()-mp0)+").");
            mochilaDialog.dispose();if(entreOndas){atualizarCombatentes();if(log!=null)log.append("\nMochila usada entre ondas. Prepare o grupo e avance quando quiser.");}else fimAcao();
        }else atualizarMochila();
    }

    private void atualizarMochila(){
        if(mochilaDialog==null||mochilaConteudo==null)return;
        mochilaConteudo.removeAll();
        mochilaConteudo.add(cabecalhoMochila(),BorderLayout.NORTH);
        JPanel corpo=new JPanel(new BorderLayout(12,0));corpo.setOpaque(false);
        JPanel esquerda=new JPanel(new BorderLayout(8,8));esquerda.setOpaque(false);esquerda.add(painelPerfilMochila(),BorderLayout.CENTER);esquerda.add(painelEquipamentosMochila(),BorderLayout.SOUTH);corpo.add(esquerda,BorderLayout.WEST);
        corpo.add(painelGradeMochila(),BorderLayout.CENTER);corpo.add(painelDetalhesMochila(),BorderLayout.EAST);
        mochilaConteudo.add(corpo,BorderLayout.CENTER);
        JPanel sul=new JPanel(new FlowLayout(FlowLayout.RIGHT));sul.setOpaque(false);JButton fechar=botao(mochilaEmBatalha?"VOLTAR À BATALHA":"FECHAR MOCHILA");fechar.addActionListener(e->mochilaDialog.dispose());sul.add(fechar);mochilaConteudo.add(sul,BorderLayout.SOUTH);
        mochilaConteudo.revalidate();mochilaConteudo.repaint();
    }

    private void abrirMochila(boolean emBatalha){
        if(grupo==null||grupo.length==0)return;
        mochilaEmBatalha=emBatalha;mochilaCategoria=emBatalha?"CONSUMÍVEIS":"TODOS";mochilaSelecionado=null;
        if(emBatalha)mochilaHeroi=heroiAtual();
        else{boolean valido=false;if(mochilaHeroi!=null)for(Personagem h:grupo)if(h==mochilaHeroi)valido=true;if(!valido)mochilaHeroi=grupo[0];}
        mochilaDialog=new JDialog(janela,emBatalha?"Item — Batalha":"Mochila / Inventário",true);mochilaDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        Dimension sc=Toolkit.getDefaultToolkit().getScreenSize();mochilaDialog.setSize(Math.min(1260,sc.width-80),Math.min(820,sc.height-100));mochilaDialog.setMinimumSize(new Dimension(1040,680));mochilaDialog.setLocationRelativeTo(janela);
        mochilaConteudo=painelFundo("inventario_scene.jpg");mochilaConteudo.setLayout(new BorderLayout(14,14));mochilaConteudo.setBorder(new EmptyBorder(14,14,14,14));mochilaDialog.setContentPane(mochilaConteudo);atualizarMochila();mochilaDialog.setVisible(true);
    }

    private void mostrarTransicao(){JPanel p=base();p.add(titulo("TRANSIÇÃO — CAMINHO PARA ELDORIA"),BorderLayout.NORTH);JLabel bg=new JLabel(imagem("forest_bg.png",900,430));p.add(bg);JTextArea t=new JTextArea("Com Fenrok derrotado, a floresta finalmente silencia.\n\nAo longe surgem as muralhas iluminadas de Eldoria. É hora de descansar, negociar e buscar informações sobre a energia estranha.");t.setEditable(false);t.setForeground(TEXTO);t.setBackground(PAINEL);t.setFont(new Font("Serif",Font.PLAIN,21));t.setBorder(new EmptyBorder(16,20,16,20));JButton seguir=botao("ENTRAR EM ELDORIA");seguir.addActionListener(e->mostrarEldoria());JPanel sul=new JPanel(new BorderLayout());sul.setOpaque(false);sul.add(t);sul.add(seguir,BorderLayout.EAST);p.add(sul,BorderLayout.SOUTH);raiz.add(p,"TRANSICAO");telas.show(raiz,"TRANSICAO");}
    private void mostrarEldoria(){
        JPanel p=base();
        p.add(titulo("ELDORIA — CIDADE DOS VIAJANTES"),BorderLayout.NORTH);
        JPanel centro=new JPanel(new GridLayout(2,3,18,18));
        centro.setOpaque(false);

        JButton pousada=cardLocalImagem("POUSADA","Descansar e salvar o progresso.","pousada_bg.jpg");
        pousada.addActionListener(e->pousadaGUI());
        JButton loja=cardLocalImagem("LOJA","Comprar, vender e equipar itens.","loja_bg.jpg");
        loja.addActionListener(e->lojaGUI());
        JButton taverna=cardLocalImagem("TAVERNA","Conversar com viajantes e procurar pistas.","taverna_bg.jpg");
        taverna.addActionListener(e->tavernaGUI());
        JButton grupoInv=cardLocalImagem("GRUPO / INVENTÁRIO","Heróis, atributos e equipamentos.",grupo!=null&&grupo.length>=3?"grupo_inventario_bg.jpg":"menu_bg.jpg");
        grupoInv.addActionListener(e->painelGrupoInventario());
        JButton mapa=cardLocalImagem("MAPA DA JORNADA","Ver progresso e próximo objetivo.","mapa_jornada.jpg");
        mapa.addActionListener(e->mostrarMapaProgresso());
        JButton caverna=cardLocalImagem("CAVERNA DOS SLIMES","Seguir para a próxima missão.","slime_cave_bg.png");
        caverna.addActionListener(e->entradaCavernaGUI());

        centro.add(pousada); centro.add(loja); centro.add(taverna); centro.add(grupoInv); centro.add(mapa); centro.add(caverna);
        p.add(centro,BorderLayout.CENTER);

        JLabel rodape=new JLabel("Ouro do líder: $"+grupo[0].getDinheiro()+"   •   Próximo destino: Caverna dos Slimes",SwingConstants.CENTER);
        rodape.setForeground(OURO);
        rodape.setFont(new Font("Serif",Font.BOLD,20));
        rodape.setBorder(new EmptyBorder(8,0,0,0));
        p.add(rodape,BorderLayout.SOUTH);
        mostrarCard("ELDORIA",p);
    }

    private void mostrarMapaProgresso(){
        JDialog d=new JDialog(janela,"Mapa da Jornada",true);d.setSize(820,500);d.setLocationRelativeTo(janela);JPanel p=base();p.add(titulo("MAPA DA JORNADA"),BorderLayout.NORTH);
        JPanel trilha=new JPanel(new GridLayout(1,5,10,0));trilha.setOpaque(false);String[] nomes={"1. Floresta","2. Fenrok","3. Eldoria","4. Caverna","5. Rei Slime"};
        int atual=batalhaCaverna?(caveWave>=4?5:4):(wave>=5?3:Math.max(1,wave));
        for(int i=0;i<nomes.length;i++){JPanel n=moldura();JLabel l=new JLabel("<html><center><font size='5'>"+nomes[i]+"</font><br><br>"+(i+1<atual?"CONCLUÍDO":i+1==atual?"ATUAL":"BLOQUEADO")+"</center></html>",SwingConstants.CENTER);l.setForeground(i+1<atual?VERDE:i+1==atual?OURO:new Color(105,120,140));n.add(l);trilha.add(n);}p.add(trilha);
        JLabel obj=new JLabel("Objetivo atual: "+(grupo!=null&&grupo.length>=3?"Investigar a Caverna dos Slimes":"Reunir informações em Eldoria"),SwingConstants.CENTER);obj.setForeground(AZULC);obj.setFont(NORMAL);p.add(obj,BorderLayout.SOUTH);d.setContentPane(p);d.setVisible(true);
    }

    private void pousadaGUI(){
        JPanel tela=painelFundo("pousada_bg.jpg");
        JPanel menu=painelFlutuante();menu.setPreferredSize(new Dimension(390,330));
        JLabel cab=new JLabel("<html><center><font size='6' color='#e2a63a'>POUSADA</font><br><font color='#d7e8f7'>Descanse antes da próxima jornada.</font></center></html>",SwingConstants.CENTER);cab.setFont(NORMAL);menu.add(cab,BorderLayout.NORTH);
        JLabel estado=new JLabel("<html><center>Recupera completamente HP e Mana.<br>O progresso também pode ser salvo aqui.</center></html>",SwingConstants.CENTER);estado.setForeground(TEXTO);estado.setFont(NORMAL);menu.add(estado,BorderLayout.CENTER);
        JPanel botoes=new JPanel(new GridLayout(0,1,8,8));botoes.setOpaque(false);
        JButton descansar=botao("DESCANSAR — RECUPERAR HP/MP"), salvar=botao("SALVAR JOGO"), voltar=botao("VOLTAR PARA ELDORIA");
        descansar.addActionListener(e->{for(Personagem h:grupo)if(h.vivo())h.recuperarTudo();JOptionPane.showMessageDialog(janela,"O grupo descansou. HP e Mana foram restaurados.","Descanso concluído",JOptionPane.INFORMATION_MESSAGE);});
        salvar.addActionListener(e->{boolean ok=SaveService.salvar(grupo,grupo[0].getNome(),"ELDORIA",5,grupo.length>=3);JOptionPane.showMessageDialog(janela,ok?"Jogo salvo com sucesso!":"Não foi possível salvar o jogo.","Salvar",ok?JOptionPane.INFORMATION_MESSAGE:JOptionPane.ERROR_MESSAGE);});
        voltar.addActionListener(e->mostrarEldoria());
        botoes.add(descansar);botoes.add(salvar);botoes.add(voltar);menu.add(botoes,BorderLayout.SOUTH);
        GridBagConstraints g=new GridBagConstraints();g.gridx=0;g.gridy=0;g.weightx=1;g.weighty=1;g.anchor=GridBagConstraints.SOUTHEAST;g.insets=new Insets(0,0,58,70);tela.add(menu,g);
        mostrarCard("POUSADA",tela);
    }
    private Personagem escolherHeroiGUI(String titulo){String[] nomes=new String[grupo.length];for(int i=0;i<grupo.length;i++)nomes[i]=grupo[i].getNome();String s=(String)JOptionPane.showInputDialog(janela,"Escolha o personagem:",titulo,JOptionPane.PLAIN_MESSAGE,null,nomes,nomes[0]);if(s==null)return null;for(Personagem h:grupo)if(h.getNome().equals(s))return h;return null;}
    private boolean podeUsarEquipamento(Personagem h,Equipamento e){
        if(h==null||e==null)return false;if(!"Arma".equalsIgnoreCase(e.getTipo()))return true;String n=e.getNome().toLowerCase(Locale.ROOT);
        if("Kael".equals(h.getNome()))return n.contains("espada");
        if("Lyra".equals(h.getNome()))return n.contains("cajado");
        if("Elyra".equals(h.getNome()))return n.contains("arco");
        return true;
    }
    private String mensagemClasse(Personagem h,Equipamento e){return h.getNome()+" não pode usar "+e.getNome()+" porque essa arma não pertence à classe dele(a).";}
    private void lojaGUI(){
        JPanel tela=painelFundo("loja_bg.jpg");
        JPanel menu=painelFlutuante();menu.setPreferredSize(new Dimension(430,420));
        JLabel cab=new JLabel("<html><center><font size='6' color='#e2a63a'>LOJA DA LUA AZUL</font><br><font color='#d7e8f7'>Equipamentos, consumíveis e comparação de atributos.</font></center></html>",SwingConstants.CENTER);cab.setFont(NORMAL);menu.add(cab,BorderLayout.NORTH);
        ouroLojaLabel=new JLabel("OURO DO LÍDER: $"+grupo[0].getDinheiro(),SwingConstants.CENTER);ouroLojaLabel.setForeground(OURO);ouroLojaLabel.setFont(new Font("Serif",Font.BOLD,20));menu.add(ouroLojaLabel,BorderLayout.CENTER);
        JPanel botoes=new JPanel(new GridLayout(0,1,8,8));botoes.setOpaque(false);JButton balcao=botao("ABRIR BALCÃO DA LOJA"),mochila=botao("ABRIR MOCHILA"),voltar=botao("VOLTAR PARA ELDORIA");
        balcao.addActionListener(e->{abrirBalcaoLoja();if(ouroLojaLabel!=null)ouroLojaLabel.setText("OURO DO LÍDER: $"+grupo[0].getDinheiro());});mochila.addActionListener(e->abrirMochila(false));voltar.addActionListener(e->mostrarEldoria());botoes.add(balcao);botoes.add(mochila);botoes.add(voltar);menu.add(botoes,BorderLayout.SOUTH);
        GridBagConstraints g=new GridBagConstraints();g.gridx=0;g.gridy=0;g.weightx=1;g.weighty=1;g.anchor=GridBagConstraints.SOUTHEAST;g.insets=new Insets(0,0,52,65);tela.add(menu,g);mostrarCard("LOJA",tela);
    }

    private Object clonarLoja(Object o){
        if(o instanceof Item){Item i=(Item)o;return new Item(i.getNome(),i.getTipo(),i.getPreco(),i.getCuraHp(),i.getCuraMana());}
        Equipamento e=(Equipamento)o;return new Equipamento(e.getNome(),e.getTipo(),e.getPreco(),e.getForca(),e.getDefesa(),e.getInteligencia(),e.getResistencia(),e.getVelocidade(),e.getSorte());
    }
    private java.util.List<Object> estoqueLoja(){java.util.List<Object> l=new ArrayList<Object>();l.add(new Equipamento("Espada de Ferro","Arma",35,2,0,0,0,0,0));l.add(new Equipamento("Armadura de Couro","Armadura",30,0,2,0,1,0,0));l.add(new Equipamento("Cajado Simples","Arma",35,0,0,2,0,0,0));l.add(new Equipamento("Botas do Viajante","Botas",28,0,0,0,0,1,1));l.add(new Item("Pocao de Vida","Pocao",15,30,0));l.add(new Item("Pocao de Mana","Pocao",18,0,20));return l;}
    private void abrirBalcaoLoja(){
        final JDialog d=new JDialog(janela,"Loja da Lua Azul",true);d.setSize(1020,680);d.setLocationRelativeTo(janela);
        JPanel root=base();root.add(titulo("BALCÃO DA LOJA"),BorderLayout.NORTH);
        final JComboBox<String> heroiBox=new JComboBox<String>();for(Personagem h:grupo)heroiBox.addItem(h.getNome());
        final JComboBox<String> modoBox=new JComboBox<String>(new String[]{"COMPRAR","VENDER","EQUIPAR"});
        final JLabel ouroAtual=new JLabel();ouroAtual.setForeground(OURO);ouroAtual.setFont(new Font("Serif",Font.BOLD,16));
        JPanel filtros=new JPanel(new FlowLayout(FlowLayout.LEFT,10,4));filtros.setOpaque(false);JLabel a=new JLabel("Personagem:");a.setForeground(TEXTO);filtros.add(a);filtros.add(heroiBox);JLabel m=new JLabel("Ação:");m.setForeground(TEXTO);filtros.add(m);filtros.add(modoBox);filtros.add(Box.createHorizontalStrut(15));filtros.add(ouroAtual);root.add(filtros,BorderLayout.SOUTH);
        final DefaultListModel<String> lm=new DefaultListModel<String>();final JList<String> lista=new JList<String>(lm);lista.setBackground(new Color(3,14,30));lista.setForeground(TEXTO);lista.setFont(new Font("Serif",Font.PLAIN,16));lista.setSelectionBackground(new Color(18,65,112));
        final java.util.List<Object> objetos=new ArrayList<Object>();final JPanel detalhe=moldura();final JLabel detalheTexto=new JLabel("Selecione um item",SwingConstants.CENTER);detalheTexto.setForeground(TEXTO);detalhe.add(detalheTexto,BorderLayout.CENTER);final JButton agir=botao("SELECIONAR");detalhe.add(agir,BorderLayout.SOUTH);
        final Runnable[] recarregar=new Runnable[1];final Runnable[] detalhar=new Runnable[1];
        recarregar[0]=()->{Personagem h=null;for(Personagem x:grupo)if(x.getNome().equals(heroiBox.getSelectedItem()))h=x;if(h==null)return;ouroAtual.setText("Ouro: $"+h.getDinheiro());String modo=(String)modoBox.getSelectedItem();objetos.clear();lm.clear();if("COMPRAR".equals(modo))objetos.addAll(estoqueLoja());else if("VENDER".equals(modo))objetos.addAll(h.getInventario().getItens());else objetos.addAll(h.getInventario().getEquipamentos());for(Object o:objetos){String n=o instanceof Item?((Item)o).getNome()+" x"+((Item)o).getQuantidade():((Equipamento)o).getNome();int pr=o instanceof Item?((Item)o).getPreco():((Equipamento)o).getPreco();lm.addElement(n+("COMPRAR".equals(modo)?" — $"+pr:""));}if(!objetos.isEmpty())lista.setSelectedIndex(0);};
        detalhar[0]=()->{int idx=lista.getSelectedIndex();if(idx<0||idx>=objetos.size()){detalheTexto.setText("Selecione um item");return;}Personagem h=null;for(Personagem x:grupo)if(x.getNome().equals(heroiBox.getSelectedItem()))h=x;Object o=objetos.get(idx);if(o instanceof Item){Item i=(Item)o;detalheTexto.setText("<html><center><font size='6' color='#e2a63a'>"+i.getNome()+"</font><br>Tipo: "+i.getTipo()+"<br>Preço: $"+i.getPreco()+"<br>Quantidade: x"+i.getQuantidade()+"<br><br>"+(i.getCuraHp()>0?"HP +"+i.getCuraHp():i.getCuraMana()>0?"Mana +"+i.getCuraMana():"Material")+"</center></html>");}else{Equipamento e=(Equipamento)o;String compat=podeUsarEquipamento(h,e)?"<font color='#65df9e'>Compatível</font>":"<font color='#ef6b78'>Incompatível com esta classe</font>";detalheTexto.setText("<html><center><font size='6' color='#e2a63a'>"+e.getNome()+"</font><br>Tipo: "+e.getTipo()+" • $"+e.getPreco()+"<br>"+compat+"<br><br><font color='#9fc6ef'>"+bonusEquipamentoHtml(e)+"</font><br><hr>"+comparacaoEquipamentoHtml(h,e)+"</center></html>");}agir.setText((String)modoBox.getSelectedItem());};
        lista.addListSelectionListener(e->{if(!e.getValueIsAdjusting())detalhar[0].run();});modoBox.addActionListener(e->{recarregar[0].run();detalhar[0].run();});heroiBox.addActionListener(e->{recarregar[0].run();detalhar[0].run();});
        agir.addActionListener(ev->{int idx=lista.getSelectedIndex();if(idx<0||idx>=objetos.size())return;Personagem h=null;for(Personagem x:grupo)if(x.getNome().equals(heroiBox.getSelectedItem()))h=x;if(h==null)return;String modo=(String)modoBox.getSelectedItem();Object o=objetos.get(idx);
            if("COMPRAR".equals(modo)){if(o instanceof Equipamento&&!podeUsarEquipamento(h,(Equipamento)o)){JOptionPane.showMessageDialog(d,mensagemClasse(h,(Equipamento)o),"Classe incompatível",JOptionPane.WARNING_MESSAGE);return;}int preco=o instanceof Item?((Item)o).getPreco():((Equipamento)o).getPreco();if(!h.gastarDinheiro(preco)){JOptionPane.showMessageDialog(d,"Ouro insuficiente.");return;}Object novo=clonarLoja(o);if(novo instanceof Item)h.getInventario().adicionarItem((Item)novo);else h.getInventario().adicionarEquipamento((Equipamento)novo);somFeedback();JOptionPane.showMessageDialog(d,"Compra realizada!");}
            else if("VENDER".equals(modo)){if(!(o instanceof Item))return;Item i=(Item)o;int max=i.getQuantidade();SpinnerNumberModel sm=new SpinnerNumberModel(1,1,max,1);JSpinner spn=new JSpinner(sm);int ok=JOptionPane.showConfirmDialog(d,spn,"Quantidade para vender (máx. "+max+")",JOptionPane.OK_CANCEL_OPTION);if(ok!=JOptionPane.OK_OPTION)return;int qtd=(Integer)spn.getValue();int valor=Math.max(1,i.getPreco()/2);for(int q=0;q<qtd;q++){h.adicionarDinheiro(valor);h.getInventario().removerUmaUnidade(i);}somFeedback();JOptionPane.showMessageDialog(d,"Venda concluída: "+qtd+" unidade(s), +$"+(valor*qtd)+".");}
            else{if(o instanceof Equipamento){Equipamento eq=(Equipamento)o;if(!podeUsarEquipamento(h,eq)){JOptionPane.showMessageDialog(d,mensagemClasse(h,eq),"Classe incompatível",JOptionPane.WARNING_MESSAGE);return;}h.equipar(eq);somFeedback();}}
            recarregar[0].run();detalhar[0].run();});
        JPanel corpo=new JPanel(new GridLayout(1,2,12,0));corpo.setOpaque(false);JScrollPane sp=new JScrollPane(lista);sp.setBorder(BorderFactory.createLineBorder(AZUL,1));corpo.add(sp);corpo.add(detalhe);root.add(corpo,BorderLayout.CENTER);recarregar[0].run();detalhar[0].run();d.setContentPane(root);d.setVisible(true);if(ouroLojaLabel!=null)ouroLojaLabel.setText("OURO DO LÍDER: $"+grupo[0].getDinheiro());
    }

    private void comprarGUI(){Personagem h=escolherHeroiGUI("Comprar para quem?");if(h==null)return;String[] nomes={"Espada de Ferro — $35","Armadura de Couro — $30","Cajado Simples — $35","Poção de Vida — $15","Poção de Mana — $18"};String s=(String)JOptionPane.showInputDialog(janela,"Dinheiro de "+h.getNome()+": $"+h.getDinheiro(),"Comprar",JOptionPane.PLAIN_MESSAGE,null,nomes,nomes[0]);if(s==null)return;int i=Arrays.asList(nomes).indexOf(s);int[] precos={35,30,35,15,18};if(!h.gastarDinheiro(precos[i])){JOptionPane.showMessageDialog(janela,"Dinheiro insuficiente.");return;}if(i==0)h.getInventario().adicionarEquipamento(new Equipamento("Espada de Ferro","Arma",35,2,0,0,0,0,0));else if(i==1)h.getInventario().adicionarEquipamento(new Equipamento("Armadura de Couro","Armadura",30,0,2,0,1,0,0));else if(i==2)h.getInventario().adicionarEquipamento(new Equipamento("Cajado Simples","Arma",35,0,0,2,0,0,0));else if(i==3)h.getInventario().adicionarItem(new Item("Pocao de Vida","Pocao",15,30,0));else h.getInventario().adicionarItem(new Item("Pocao de Mana","Pocao",18,0,20));JOptionPane.showMessageDialog(janela,"Compra realizada: "+s.split(" —")[0]+".");}
    private void venderGUI(){Personagem h=escolherHeroiGUI("Vender item de quem?");if(h==null||h.getInventario().getItens().isEmpty()){JOptionPane.showMessageDialog(janela,"Nenhum item disponível para venda.");return;}java.util.List<Item> its=h.getInventario().getItens();String[] n=new String[its.size()];for(int i=0;i<n.length;i++)n[i]=its.get(i).descricao();String s=(String)JOptionPane.showInputDialog(janela,"Escolha o item:","Vender",JOptionPane.PLAIN_MESSAGE,null,n,n[0]);if(s==null)return;Item it=its.get(Arrays.asList(n).indexOf(s));int valor=Math.max(1,it.getPreco()/2);h.adicionarDinheiro(valor);h.getInventario().removerUmaUnidade(it);JOptionPane.showMessageDialog(janela,it.getNome()+" vendido por $"+valor+".");}
    private void equiparGUI(){Personagem h=escolherHeroiGUI("Equipar em quem?");if(h==null||h.getInventario().getEquipamentos().isEmpty()){JOptionPane.showMessageDialog(janela,"Nenhum equipamento disponível.");return;}java.util.List<Equipamento> es=h.getInventario().getEquipamentos();String[] n=new String[es.size()];for(int i=0;i<n.length;i++)n[i]=es.get(i).descricao();String s=(String)JOptionPane.showInputDialog(janela,"Escolha o equipamento:","Equipar",JOptionPane.PLAIN_MESSAGE,null,n,n[0]);if(s==null)return;Equipamento e=es.get(Arrays.asList(n).indexOf(s));if(!podeUsarEquipamento(h,e)){JOptionPane.showMessageDialog(janela,mensagemClasse(h,e));return;}h.equipar(e);JOptionPane.showMessageDialog(janela,e.getNome()+" equipado em "+h.getNome()+".");}
    private void tavernaGUI(){
        JPanel tela=painelFundo("taverna_bg.jpg");
        JPanel menu=painelFlutuante();menu.setPreferredSize(new Dimension(470,390));
        JLabel cab=new JLabel("<html><center><font size='6' color='#e2a63a'>TAVERNA</font><br><font color='#d7e8f7'>Rumores, viajantes e novas pistas.</font></center></html>",SwingConstants.CENTER);cab.setFont(NORMAL);menu.add(cab,BorderLayout.NORTH);
        JTextArea texto=new JTextArea();texto.setEditable(false);texto.setLineWrap(true);texto.setWrapStyleWord(true);texto.setOpaque(false);texto.setForeground(TEXTO);texto.setFont(NORMAL);
        if(grupo.length<3)texto.setText("Entre mercadores e aventureiros, uma elfa observa a movimentação. Ela parece interessada na mesma energia estranha investigada por Kael e Lyra.");
        else texto.setText("Elyra já faz parte do grupo. O taverneiro comenta que ruídos viscosos e uma luz azul vêm da Caverna dos Slimes.");
        menu.add(texto,BorderLayout.CENTER);
        JPanel botoes=new JPanel(new GridLayout(0,1,8,8));botoes.setOpaque(false);
        JButton conversar=botao(grupo.length<3?"CONVERSAR COM A VIAJANTE":"CONVERSAR / OUVIR RUMORES"), voltar=botao("VOLTAR PARA ELDORIA");
        conversar.addActionListener(e->{
            if(grupo.length<3){
                mostrarDialogoPersonagem("ELYRA","Também estou investigando a energia estranha que surgiu na região. Se vocês vão para a caverna, eu vou junto.","elyra_portrait_v2.jpg");
                grupo=new Personagem[]{grupo[0],grupo[1],new Elyra()};mostrarTransicaoCurta("NOVO MEMBRO","Elyra entrou no grupo — Nv. 2",OURO);
            }else JOptionPane.showMessageDialog(janela,"O taverneiro reforça o aviso: a Caverna dos Slimes está cada vez mais instável.","Rumores",JOptionPane.INFORMATION_MESSAGE);
            tavernaGUI();
        });
        voltar.addActionListener(e->mostrarEldoria());
        botoes.add(conversar);botoes.add(voltar);menu.add(botoes,BorderLayout.SOUTH);
        GridBagConstraints g=new GridBagConstraints();g.gridx=0;g.gridy=0;g.weightx=1;g.weighty=1;g.anchor=GridBagConstraints.SOUTHEAST;g.insets=new Insets(0,0,48,60);tela.add(menu,g);
        mostrarCard("TAVERNA",tela);
    }
    private void mostrarDialogoPersonagem(String nome,String texto,String retrato){
        final JDialog d=new JDialog(janela,nome,true);d.setSize(720,390);d.setLocationRelativeTo(janela);JPanel p=base();
        JPanel c=new JPanel(new BorderLayout(18,10));c.setOpaque(false);JLabel img=new JLabel(imagem(retrato,230,230),SwingConstants.CENTER);img.setBorder(BorderFactory.createLineBorder(OURO,2));c.add(img,BorderLayout.WEST);
        JPanel fala=moldura();JLabel n=new JLabel(nome);n.setForeground(OURO);n.setFont(new Font("Serif",Font.BOLD,25));fala.add(n,BorderLayout.NORTH);JTextArea t=new JTextArea(texto);t.setEditable(false);t.setLineWrap(true);t.setWrapStyleWord(true);t.setOpaque(false);t.setForeground(TEXTO);t.setFont(new Font("Serif",Font.PLAIN,19));fala.add(t);JButton ok=botao("CONTINUAR");ok.addActionListener(e->d.dispose());fala.add(ok,BorderLayout.SOUTH);c.add(fala);p.add(c);d.setContentPane(p);d.setVisible(true);
    }

    private void painelGrupoInventario(){
        JPanel tela=painelFundo(grupo!=null&&grupo.length>=3?"grupo_inventario_bg.jpg":"menu_bg.jpg");JPanel menu=painelFlutuante();menu.setPreferredSize(new Dimension(650,620));
        JLabel cab=new JLabel("<html><center><font size='6' color='#e2a63a'>GRUPO / INVENTÁRIO</font><br><font color='#d7e8f7'>Clique em um herói para abrir seus equipamentos.</font></center></html>",SwingConstants.CENTER);cab.setFont(NORMAL);menu.add(cab,BorderLayout.NORTH);
        JPanel herois=new JPanel(new GridLayout(grupo.length,1,7,7));herois.setOpaque(false);
        for(Personagem h:grupo){JButton linha=new JButton();linha.setLayout(new BorderLayout(10,4));linha.setBackground(new Color(3,14,29,235));linha.setFocusPainted(false);linha.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));linha.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(AZUL,1),new EmptyBorder(7,8,7,8)));
            linha.add(new JLabel(imagem(imagemHeroi(h),72,72)),BorderLayout.WEST);String arma=h.getArmaEquipada()==null?"—":h.getArmaEquipada().getNome();String arm=h.getArmaduraEquipada()==null?"—":h.getArmaduraEquipada().getNome();
            JLabel info=new JLabel("<html><b><font color='#e2a63a' size='5'>"+h.getNome()+"</font></b> — Nv. "+h.getNivel()+"<br>HP "+h.getVida()+"/"+h.getVidaMaxima()+" • Mana "+h.getMana()+"/"+h.getManaMaxima()+"<br><font color='#9fc6ef'>Arma: "+arma+" • Armadura: "+arm+"</font><br>Pontos disponíveis: "+h.getPontosAtributo()+"</html>");info.setForeground(TEXTO);linha.add(info,BorderLayout.CENTER);linha.addActionListener(e->{mochilaHeroi=h;abrirMochila(false);});herois.add(linha);}
        menu.add(herois,BorderLayout.CENTER);JPanel botoes=new JPanel(new GridLayout(0,1,8,8));botoes.setOpaque(false);JButton mochila=botao("ABRIR MOCHILA"),atributos=botao("DISTRIBUIR ATRIBUTOS"),mapa=botao("MAPA DA JORNADA"),voltar=botao("VOLTAR PARA ELDORIA");mochila.addActionListener(e->abrirMochila(false));atributos.addActionListener(e->{distribuirPontosGUI();painelGrupoInventario();});mapa.addActionListener(e->mostrarMapaProgresso());voltar.addActionListener(e->mostrarEldoria());botoes.add(mochila);botoes.add(atributos);botoes.add(mapa);botoes.add(voltar);menu.add(botoes,BorderLayout.SOUTH);
        GridBagConstraints g=new GridBagConstraints();g.gridx=0;g.gridy=0;g.weightx=1;g.weighty=1;g.anchor=GridBagConstraints.SOUTHEAST;g.insets=new Insets(0,0,40,45);tela.add(menu,g);mostrarCard("GRUPO_INVENTARIO",tela);
    }
    private void distribuirPontosGUI(){
        Personagem h=escolherHeroiGUI("Distribuir atributos");if(h==null)return;
        final JDialog d=new JDialog(janela,"Atributos — "+h.getNome(),true);d.setSize(470,520);d.setLocationRelativeTo(janela);JPanel root=base();root.add(titulo("ATRIBUTOS DE "+h.getNome().toUpperCase()),BorderLayout.NORTH);
        JPanel lista=new JPanel(new GridLayout(6,1,7,7));lista.setOpaque(false);String[] nomes={"Força","Defesa","Inteligência","Resistência","Velocidade","Sorte"};
        JLabel pontos=new JLabel("Pontos disponíveis: "+h.getPontosAtributo(),SwingConstants.CENTER);pontos.setForeground(OURO);pontos.setFont(new Font("Serif",Font.BOLD,20));
        Runnable rebuild=new Runnable(){public void run(){lista.removeAll();int[] vals={h.getForca(),h.getDefesa(),h.getInteligencia(),h.getResistencia(),h.getVelocidade(),h.getSorte()};for(int i=0;i<nomes.length;i++){final int op=i+1;JPanel lin=moldura();JLabel lab=new JLabel(nomes[i]+":  "+vals[i]);lab.setForeground(TEXTO);lab.setFont(new Font("Serif",Font.BOLD,18));JButton mais=botao("+");mais.setPreferredSize(new Dimension(55,36));mais.setEnabled(h.getPontosAtributo()>0);mais.addActionListener(e->{if(h.distribuirPonto(op)){pontos.setText("Pontos disponíveis: "+h.getPontosAtributo());run();}});lin.add(lab,BorderLayout.CENTER);lin.add(mais,BorderLayout.EAST);lista.add(lin);}lista.revalidate();lista.repaint();}};
        rebuild.run();root.add(lista,BorderLayout.CENTER);JPanel sul=new JPanel(new BorderLayout());sul.setOpaque(false);sul.add(pontos,BorderLayout.CENTER);JButton fechar=botao("CONCLUIR");fechar.addActionListener(e->d.dispose());sul.add(fechar,BorderLayout.SOUTH);root.add(sul,BorderLayout.SOUTH);d.setContentPane(root);d.setVisible(true);
    }
    private void entradaCavernaGUI(){JPanel p=base();p.add(titulo("CAVERNA 1 — REINO DOS SLIMES"),BorderLayout.NORTH);JLabel bg=new JLabel(imagem("slime_cave_bg.png",900,420),SwingConstants.CENTER);bg.setBorder(BorderFactory.createLineBorder(AZUL,2));p.add(bg);JTextArea t=new JTextArea("> O grupo chega à Caverna dos Slimes.\nCristais azuis iluminam as paredes e sons viscosos ecoam pelos túneis.\n\nObjetivo: atravesse 3 ondas de Slimes e derrote o Rei Slime.");t.setEditable(false);t.setLineWrap(true);t.setWrapStyleWord(true);t.setFont(NORMAL);t.setForeground(TEXTO);t.setBackground(PAINEL);t.setBorder(new EmptyBorder(12,16,12,16));JButton entrar=botao("ENTRAR NA CAVERNA"),voltar=botao("VOLTAR PARA ELDORIA");entrar.addActionListener(e->{batalhaCaverna=true;caveWave=0;proximaOndaCaverna();});voltar.addActionListener(e->telas.show(raiz,"ELDORIA"));JPanel bs=new JPanel(new FlowLayout());bs.setOpaque(false);bs.add(entrar);bs.add(voltar);JPanel sul=new JPanel(new BorderLayout());sul.setOpaque(false);sul.add(t);sul.add(bs,BorderLayout.SOUTH);p.add(sul,BorderLayout.SOUTH);raiz.add(p,"CAVERNA1");telas.show(raiz,"CAVERNA1");}
    private void proximaOndaCaverna(){entreOndas=false;caveWave++; if(caveWave==1) inimigos=new Inimigo[]{new Inimigo("Slime",32,7,2,2,8),new Inimigo("Slime",32,7,2,2,8),new Inimigo("Slime",32,7,2,2,8)}; else if(caveWave==2) inimigos=new Inimigo[]{new Inimigo("Slime",42,9,3,2,10),new Inimigo("Slime",42,9,3,2,10),new Inimigo("Slime",42,9,3,2,10),new Inimigo("Slime",42,9,3,2,10)}; else if(caveWave==3) inimigos=new Inimigo[]{new Inimigo("Slime",55,11,4,3,14),new Inimigo("Slime",55,11,4,3,14),new Inimigo("Slime",55,11,4,3,14),new Inimigo("Slime",55,11,4,3,14),new Inimigo("Slime",55,11,4,3,14)}; else if(caveWave==4) inimigos=new Inimigo[]{new Inimigo("Rei Slime",260,18,7,3,90)}; else {mostrarFinalDemo();return;} turnoHeroi=0; JPanel b=criarBatalha();raiz.add(b,"BATALHA_CAVERNA");telas.show(raiz,"BATALHA_CAVERNA");mostrarTransicaoCurta(caveWave==4?"CHEFE — REI SLIME":"ONDA "+caveWave+" / 3",nomeCenarioBatalha(),caveWave==4?OURO:AZULC);}

    private void mostrarFinalDemo(){
        batalhaCaverna=false;JPanel p=painelFundo("slime_cave_bg.png");p.setLayout(new GridBagLayout());JPanel box=painelFlutuante();box.setPreferredSize(new Dimension(720,560));
        JLabel ti=new JLabel("CAPÍTULO CONCLUÍDO",SwingConstants.CENTER);ti.setForeground(OURO);ti.setFont(new Font("Serif",Font.BOLD,38));box.add(ti,BorderLayout.NORTH);
        JTextArea t=new JTextArea("O Rei Slime se desfaz em luz azul e a caverna fica em silêncio.\n\nEntre os cristais, Elyra encontra um fragmento marcado com o símbolo Delta. A energia é semelhante àquela sentida na floresta — prova de que Fenrok não era um caso isolado.\n\nKael, Lyra e Elyra retornam a Eldoria com uma nova pista.\n\nA aventura continua...");t.setEditable(false);t.setLineWrap(true);t.setWrapStyleWord(true);t.setOpaque(false);t.setFont(new Font("Serif",Font.PLAIN,21));t.setForeground(TEXTO);t.setBorder(new EmptyBorder(16,22,16,22));box.add(t);
        JPanel bs=new JPanel(new GridLayout(1,2,10,0));bs.setOpaque(false);JButton cidade=botao("VOLTAR PARA ELDORIA"),menu=botao("MENU PRINCIPAL");cidade.addActionListener(e->mostrarEldoria());menu.addActionListener(e->telas.show(raiz,"MENU"));bs.add(cidade);bs.add(menu);box.add(bs,BorderLayout.SOUTH);GridBagConstraints g=new GridBagConstraints();g.gridx=0;g.gridy=0;p.add(box,g);somFeedback();mostrarCard("FINAL",p);
    }
    private void mostrarGrupo(){StringBuilder s=new StringBuilder();for(Personagem h:grupo)s.append(h.getNome()).append(" — Nv. ").append(h.getNivel()).append(" | HP ").append(h.getVida()).append('/').append(h.getVidaMaxima()).append(" | Mana ").append(h.getMana()).append('/').append(h.getManaMaxima()).append('\n');JOptionPane.showMessageDialog(janela,s.toString(),"Grupo",JOptionPane.INFORMATION_MESSAGE);}
    private void mostrarInventario(){abrirMochila(false);}
}
