import javax.swing.*;
/**
 * The GameFrame class represents the main frame of the Snake Game.
 * It sets up the window and adds the game panel where the game will be played.
 * 
 * @author Ye Htut Oo
 */
public class GameFrame extends JFrame {
    /**
     * Constructor for the GameFrame class.
     * Initializes the frame, sets up the window properties, and adds the game panel.
     */
    public GameFrame() {
        
        this.add(new GamePanel()); // add the game panel to the JFrame

        // Set up the main window frame
        this.setTitle("Snake Game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setResizable(false);
        this.pack();
        this.setVisible(true);

        // Center the window on the screen
        this.setLocationRelativeTo(null);

    }
}
