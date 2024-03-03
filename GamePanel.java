import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * The GamePanel class represents the panel where the snake game will be played.
 * It extends JPanel and implements the ActionListener interface.
 * 
 * NOTE: This game uses WASD for controls.
 * 
 * @author Ye Htut Oo
 */
public class GamePanel extends JPanel implements ActionListener {
    /** Random number generator */
    private static final Random random = new Random();

    /** Timer for controlling game updates */
    private Timer timer;

    /** The time interval (in milliseconds) between each game update */
    private static final int DELAY = 120;

    /** Image displayed when the game is over */
    private Image game_over_image;

    /** Dimension of the screen */
    private static final int SCREEN_WIDTH = 600;
    private static final int SCREEN_HEIGHT = 600;
    
    /** Size of each game unit */
    private static final int UNIT_SIZE = 25;

    /** Total number of game units in the game area */
    private static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / (UNIT_SIZE * UNIT_SIZE);

    /** Flag indicating whether the game is running */
    private boolean running;
    
    /** Array storing the x-coordinates of each segment of the snake's body */
    private static int[] snake_body_x = new int[GAME_UNITS];

    /** Array storing the y-coordinates of each segment of the snake's body */
    private static int[] snake_body_y = new int[GAME_UNITS];

    /** Direction of the snake moving */
    private static char direction = 'R';

    /** Number of body parts that the snake has */
    private static int body_parts = 6;

    /** X-coordinate of the apple */
    private static int apple_x;

    /** Y-coordinate of the apple */
    private static int apple_y;

    /** Number of apple eaten */
    private static int apple_eaten;

    /** Label to display score */
    private JLabel scoreLabel;

    public GamePanel() {
        // set up game panel
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);

        // make Jcomponent KEYBOARD inputs acceptable
        this.setFocusable(true);

        // add key listener
        this.addKeyListener(new MyKeyAdapter());

        // import game over image
        game_over_image = new ImageIcon("game_over_image.png").getImage();

        // create scoreboard
        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setForeground(Color.white);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        this.add(scoreLabel);

        startGame();
    }

    /**
     * Method to start the game
     */
    public void startGame() {
        running = true;
        newApple();
        timer = new Timer(DELAY, this);
        timer.start();
    }

    /**
     * Overrides the paintComponent method of JPanel to render graphics on the panel.
     * Calls the draw method to perform the actual rendering.
     * @param g The Graphics object used for rendering.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // Call the superclass method to clear the panel
        if (running) {
            draw(g); // Call the draw method to render graphics
        }
        
        if (!running) {
            gameOver(g); // display game over screen 
        }
    }

    /**
     * Renders graphics on the panel.
     * @param g The Graphics object used for rendering.
     */
    public void draw(Graphics g) {
        // Draws the grid
        for (int i = 0; i <= SCREEN_HEIGHT/UNIT_SIZE; i++) {
            g.setColor(Color.gray);
            g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
            g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
        }

        // Draws the apple
        g.setColor(Color.red);
        g.fillOval(apple_x, apple_y, UNIT_SIZE, UNIT_SIZE);

        // Draws the snake
        for (int i = 0; i < body_parts; i++) {
            if (i == 0) {
                g.setColor(Color.white);
                g.fillRect(snake_body_x[0], snake_body_y[0], UNIT_SIZE, UNIT_SIZE);
            }
            else {
                g.setColor(new Color(random.nextInt(255), random.nextInt(255),random.nextInt(255)));
                g.fillRect(snake_body_x[i], snake_body_y[i], UNIT_SIZE, UNIT_SIZE);
            }
        }

        // Draws the scoreboard
        scoreLabel.setText("Score: " + apple_eaten);
    }

    /**
     * Renders game over screen 
     * @param g The Graphics object used for rendering.
     */
    private void gameOver(Graphics g) {
        // Draws the "Game Over" image
        int x = (SCREEN_WIDTH - game_over_image.getWidth(null)) / 2;
        int y = (SCREEN_HEIGHT - game_over_image.getHeight(null)) / 2;
        g.drawImage(game_over_image, x, y, this);
    }

    /**
     * Generates a new random position for the apple.
     */
    private void newApple() {
        apple_x = random.nextInt((int)SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
        apple_y = random.nextInt((int)SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE;
    }

    /**
     * Moves the snake body segments.
     */
    private void move() {
        // Move each body segment to the position of the preceding segment(except head)
        for (int i = body_parts; i > 0; i--) {
            snake_body_x[i] = snake_body_x[i - 1]; // Move the x-coordinate
            snake_body_y[i] = snake_body_y[i - 1]; // Move the y-coordinate
        }

        // change the head direction
        switch (direction) {
            case 'R':
                snake_body_x[0] += UNIT_SIZE;
                break;
        
            case 'L':
                snake_body_x[0] -= UNIT_SIZE;
                break;

            case 'U':
                snake_body_y[0] -= UNIT_SIZE;
                break;

            case 'D':
                snake_body_y[0] += UNIT_SIZE;
                break;
        }
    }

    /**
     * Checks if the snake has eaten the apple.
     * If the snake's head is at the same position as the apple,
     * increments the apple counter, increases the length of the snake,
     * and generates a new apple.
     */
    private void checkApple() {
        if (snake_body_x[0] == apple_x && snake_body_y[0] == apple_y) {
            apple_eaten++; // Increment the apple counter
            body_parts++; // Increase the length of the snake
            newApple(); // Generate a new apple
        }
    }

    /**
     * Checks for collisions with walls or the snake's own body.
     * If a collision is detected, sets the 'running' flag to false
     * to end the game and stops the timer.
     */
    private void checkCollision() {
        for (int i = body_parts; i > 0; i--) {
            // Check for collision with the snake's own body
            if (snake_body_x[0] == snake_body_x[i] && snake_body_y[0] == snake_body_y[i]) {
                running = false;
            }

            // Check for collision with the walls of the game area
            if (snake_body_x[0] > SCREEN_WIDTH || snake_body_x[0] < 0 || 
                snake_body_y[0] > SCREEN_HEIGHT || snake_body_y[0] < 0) {
                running = false;
            }

            // If a collision is detected, stop the timer
            if (!running) {
                timer.stop();
            }
        }
    }

    /**
     * Represents the number of times the actionPerformed method has been executed.
     */
    private static int actionPerformedCount;
    /**
     * The actionPerformed method required by the ActionListener interface.
     * It is called when an action event occurs.
     * @param e The ActionEvent object representing the action event.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // debugging message
        System.out.println("Running " + actionPerformedCount++);

        if (running) {
            move();
            checkApple();
            checkCollision();
        }
        if (!running) {
            this.setBackground(Color.white);
            scoreLabel.setForeground(Color.black);
        }

        repaint();
    }

    /**
     * The MyKeyAdapter class is a nested class that extends KeyAdapter.
     * It handles keyboard input events for the GamePanel.
     */
    private class MyKeyAdapter extends KeyAdapter {

        /**
         * The keyPressed method is called when a key is pressed.
         * It handles keyboard input events.
         * 
         * This method is used for controlling snake direction.
         * This game use WASD control system.
         * @param e The KeyEvent object representing the key event.
         */
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                
                case KeyEvent.VK_D:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;

                case KeyEvent.VK_W:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;

                case KeyEvent.VK_S:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
