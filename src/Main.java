import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame();

        Game game = new Game();
        frame.setBounds(0, 0, 1000, 600);
        frame.setTitle("Pauls Flappy Bird Clone");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(game);
        frame.setVisible(true);
    }
}