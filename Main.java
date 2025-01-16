import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
    
        int width = 288;
        int height = 512;

        JFrame frame = new JFrame();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        BackGround bg = new BackGround();
        frame.add(bg);
        frame.pack();
        bg.requestFocus();
        frame.setVisible(true);

    }
}
