package jogodacobrinha;

import javax.swing.JFrame;

public class IniciarJogo extends JFrame {

    
    public static void main(String[] args) {
        new IniciarJogo();
    }
    
    IniciarJogo() {
        add(new TelaJogo());
        setTitle("Jogo da Cobrinha");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
    }

}
