package jogodacobrinha;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;

public class TelaJogo extends JPanel implements ActionListener{

    
    private static final int LARGURA_TELA = 1300;
    private static final int ALTURA_TELA = 750;
    private static final int TAMANHO_BLOCO = 50;
    private static final int UNIDADES = LARGURA_TELA * ALTURA_TELA / (TAMANHO_BLOCO * TAMANHO_BLOCO);
    private static final int INTERVALO = 180;
    private static final String NOME_FONTE = "Ink Free";
    private final int[] eixoX = new int[UNIDADES];
    private final int[] eixoY = new int[UNIDADES];
    private int corpoCobra = 6;
    private int blocoComidos;
    private int blocoX;
    private int blocoY;
    private char direcao = 'D';
    private boolean estaRodando = false;
    private JButton botaoReiniciar;
    Timer timer;
    Random random;
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        desenharTela(g);
    }
    
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        if(estaRodando) {
            andar();
            alcancarBloco();
            validarLimites();
        }
        
        repaint();
    }
    
    public class LeitorDeTeclasAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direcao != 'D') {
                        direcao = 'E';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direcao != 'E') {
                        direcao = 'D';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direcao != 'B') {
                        direcao = 'C';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direcao != 'C') {
                        direcao = 'B';
                    }
                    break;
                default:
                    break;

            }
        }

    }
    
    TelaJogo() {
        random = new Random();
        setPreferredSize(new Dimension(LARGURA_TELA,ALTURA_TELA));
        setBackground(Color.white);
        setFocusable(true);
        addKeyListener(new LeitorDeTeclasAdapter());
        setDoubleBuffered(true);
        iniciarJogo();
    }
    
    public void iniciarJogo(){
        criarBloco();
        estaRodando = true;
        timer = new Timer(INTERVALO, this);
        timer.start();
    }
    
    private void criarBloco() {
        boolean blocoValido;

        do {
            blocoX = random.nextInt(LARGURA_TELA / TAMANHO_BLOCO) * TAMANHO_BLOCO;
            blocoY = random.nextInt(ALTURA_TELA / TAMANHO_BLOCO) * TAMANHO_BLOCO;

            blocoValido = true; 

            for (int i = 0; i < corpoCobra; i++) {
                if (eixoX[i] == blocoX && eixoY[i] == blocoY) {
                    blocoValido = false; 
                    break;
                }
            }
        } while (!blocoValido); 
    }

    
    public void desenharTela(Graphics g){
        
        if(estaRodando) {
            g.setColor(Color.red);
            g.fillOval(blocoX, blocoY, TAMANHO_BLOCO, TAMANHO_BLOCO);
            
            for(int i = 0; i < corpoCobra; i++) {
                
                if(i == 0 ) {
                    g.setColor(Color.green);
                    g.fillRect(eixoX[0], eixoY[0], TAMANHO_BLOCO,TAMANHO_BLOCO);
                } else {
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(eixoX[i], eixoY[i], TAMANHO_BLOCO, TAMANHO_BLOCO);
                }    
            }
            
            g.setColor(Color.red);
            g.setFont(new Font(NOME_FONTE, Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Pontos: " + blocoComidos,(LARGURA_TELA - metrics.stringWidth("Pontos: " + blocoComidos)) / 2, g.getFont().getSize());
        } else {
            fimDeJogo(g);
        } 
    }
    
    
    private void andar() {
        for (int i = corpoCobra; i > 0; i-- ) {
            eixoX[i] = eixoX[i - 1];
            eixoY[i] = eixoY[i - 1];
        }
        
        switch(direcao) {
            case 'C':
                eixoY[0] = eixoY[0] - TAMANHO_BLOCO;
                break;
            case 'B':
                eixoY[0] = eixoY[0] + TAMANHO_BLOCO;
                break;
            case 'E':
                eixoX[0] = eixoX[0] - TAMANHO_BLOCO;
                break;
            case 'D':
                eixoX[0] = eixoX[0] + TAMANHO_BLOCO;
                break;
            default:
                break;
        }
        
    }
    
    
    private void alcancarBloco(){
        if(eixoX[0] == blocoX && eixoY[0] == blocoY) {
            corpoCobra++;
            blocoComidos++;
            criarBloco();
        }
    }
    
    private void validarLimites(){
        
        for(int i = corpoCobra; i > 0; i--) {
            
            //Cabeça bateu no corpo?
            if(eixoX[0] == eixoX[i] && eixoY[0] == eixoY[i]){
                estaRodando = false;
                break;
            }
            
            //Toca na parede 
            if(eixoX[0] < 0 || eixoX[0] > LARGURA_TELA
               || eixoY[0] < 0 || eixoY[0] > ALTURA_TELA){
               
                estaRodando = false;
            }    
            
            if(!estaRodando){
                timer.stop();
            }
        }
    }
    
     public void fimDeJogo(Graphics g) {
        
         g.setColor(Color.red);
        g.setFont(new Font(NOME_FONTE, Font.BOLD, 40));
        FontMetrics fontePontuacao = getFontMetrics(g.getFont());
        g.drawString("Pontos: " + blocoComidos, (LARGURA_TELA - fontePontuacao.stringWidth("Pontos: " + blocoComidos)) / 2, g.getFont().getSize());
        
        g.setColor(Color.red);
        g.setFont(new Font(NOME_FONTE, Font.BOLD, 75));
        FontMetrics fonteFinal = getFontMetrics(g.getFont());
        g.drawString("\uD83D\uDE1D Fim de Jogo", (LARGURA_TELA - fonteFinal.stringWidth("\uD83D\uDE1D Fim de Jogo")) / 2, ALTURA_TELA / 2);
        
        botaoReiniciar = new JButton("Reiniciar");
        botaoReiniciar.setFont(new Font(NOME_FONTE, Font.BOLD, 30));
        botaoReiniciar.setBounds((LARGURA_TELA / 2) - 100, (ALTURA_TELA / 2) + 100, 200, 50);

        botaoReiniciar.addActionListener(e -> reiniciarJogo());

        setLayout(null); 
        add(botaoReiniciar); 
    }
     
    private void reiniciarJogo() {
        
        blocoComidos = 0;
        corpoCobra = 6;
        direcao = 'D';
        estaRodando = true;
        eixoX[0] = 0;
        eixoY[0] = 0;

        botaoReiniciar.setVisible(false); 
        
        for (int i = 0; i < UNIDADES; i++) {
        eixoX[i] = 0;
        eixoY[i] = 0;
        }
        
        iniciarJogo();
    } 
}
