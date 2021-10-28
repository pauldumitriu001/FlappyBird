import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.Random;

public class Game extends JPanel implements ActionListener, KeyListener {

    private boolean playing;
    private int score = 0;      //Score

    private final Timer timer;
    private int delay = 8;  //For timer object

    private Rectangle bird;     //Hit box of bird
    private final int DEFAULT_BIRD_POSITION = 70;   //Default x position of bird
    private int birdX = DEFAULT_BIRD_POSITION;  //Bird x-coordinate
    private int birdY = 150;    //Bird y-coordinate
    private final int BIRD_WIDTH = 30;  //Width of the bird
    private final int BIRD_HEIGHT = 30; //Height of the bird

    private final float TERMINAL_VELOCITY = 2.0f;   //Control how fast bird can fall
    private float FALLING_SPEED = 0.3f;     //speed bird is falling. Starts out with current value
    private int falling = 1;    //Controls direction of acceleration
    private final float acceleration = 0.1f;    //acceleration
    private final float JUMP_FORCE = -3.0f;

    private double move = FALLING_SPEED;    //movement vector of bird

    private final float PIPE_SPEED = -2.5f; //Speed of pipe
    private final int NUMBER_OF_PIPES = 1;  //Keep at 1 for now
    private Pipe[] pipes = new Pipe[NUMBER_OF_PIPES];

    public Game(){
        playMusic();
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(true);
        bird = new Rectangle(birdX, birdY, BIRD_WIDTH, BIRD_HEIGHT);

        generatePipe(NUMBER_OF_PIPES);
        playing = true;
        timer = new Timer(delay, this);
        timer.start();
    }

    /**
     * Handles the music being played in the background
     */
    private void playMusic(){
        try{
            File file = new File("res/background.wav");
            if(file.exists()){
                AudioInputStream ais = AudioSystem.getAudioInputStream(file);
                Clip clip = AudioSystem.getClip();
                clip.open(ais);
                clip.start();
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
            else{
                System.out.println("Cannot find audio file");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Handles noise when passing though a pipe
     */
    private void playDing(){
        try{
            File file = new File("res/ding.wav");
            if(file.exists()){
                AudioInputStream ais = AudioSystem.getAudioInputStream(file);
                Clip clip = AudioSystem.getClip();
                clip.open(ais);
                clip.start();
            }
            else{
                System.out.println("Cannot find audio file");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Play jump noise
     */
    private void playJump(){
        try{
            File file = new File("res/bruh.wav");
            if(file.exists()){
                AudioInputStream ais = AudioSystem.getAudioInputStream(file);
                Clip clip = AudioSystem.getClip();
                clip.open(ais);
                clip.start();
            }
            else{
                System.out.println("Cannot find audio file");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Play death noise
     */
    private void playDeath(){
        try{
            File file = new File("res/oof.wav");
            if(file.exists()){
                AudioInputStream ais = AudioSystem.getAudioInputStream(file);
                Clip clip = AudioSystem.getClip();
                clip.open(ais);
                clip.start();
            }
            else{
                System.out.println("Cannot find audio file");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Play bounce noise
     */
    private void playBounce(){
        try{
            File file = new File("res/vine.wav");
            if(file.exists()){
                AudioInputStream ais = AudioSystem.getAudioInputStream(file);
                Clip clip = AudioSystem.getClip();
                clip.open(ais);
                clip.start();
            }
            else{
                System.out.println("Cannot find audio file");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Paints the frame
     * @param g
     */
    public void paint(Graphics g){
        super.paintComponent(g);
        drawBackground(g);
        drawPipe(g);
        drawGrass(g);
        drawBird(g);
        drawBorder(g);

        if(playing)
            drawScore(g);
        else
            drawGameOver(g);

        g.dispose();
    }

    /**
     * Handles the drawing of the background
     * @param g
     */
    private void drawBackground(Graphics g){
        g.setColor(new Color(135, 206, 235));
        g.fillRect(0, 0, 1000, 600);
    }

    /**
     * Handels the drawing of the pipe(s)
     * @param g
     */
    private void drawPipe(Graphics g){
        g.setColor(new Color(0, 100, 0));
        for(Pipe pipe: pipes){
            if(pipe.getPipeX() + pipe.getPipeWidth() > 0){
                g.fillRect(pipe.getPipeX(), 0, pipe.getPipeWidth(), pipe.getPipeLowerY());
                g.fillRect(pipe.getPipeX(), pipe.getPipeLowerY() + pipe.getOpeningSpace(), pipe.getPipeWidth(), 600);
            }
            else {
                generatePipe(1);
            }
        }
    }

    /**
     * Handles the drawing of the grass
     * @param g
     */
    private void drawGrass(Graphics g){
        g.setColor(Color.green);
        g.fillRect(0, 500, 1000, 100);
    }

    /**
     * Handles the drawing of the bird
     * @param g
     */
    private void drawBird(Graphics g){
        g.setColor(Color.red);
        g.fillOval(birdX, birdY, 30, 30);
    }

    /**
     * Handles the drawing of the border around the window
     * @param g
     */
    private void drawBorder(Graphics g){
        g.setColor(Color.red);

        g.fillRect(0, 0, 3, 997);   //top border
        g.fillRect(0, 0, 997, 3);   //left border
        g.fillRect(997, 0, 3, 592); //right border
        g.fillRect(0, 570, 997, 3); //bottom border
    }

    /**
     * Handles the drawing of the scoreboard
     * @param g
     */
    private void drawScore(Graphics g){
        g.setColor(Color.white);
        g.setFont(new Font("Ink Free", Font.PLAIN, 50));
        g.drawString("SCORE: " + score, 400, 50);
    }

    /**
     * Handles the drawing of the game over menu
     * @param g
     */
    private void drawGameOver(Graphics g){
        g.setColor(Color.white);
        g.setFont(new Font("Ink Free", Font.PLAIN, 30));
        g.drawString("Final Score: " + score, 400, 270);
        g.drawString("Press Space to restart", 350, 300);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        if (playing) {
            //Pipe movement
            for(Pipe pipe: pipes)
                pipe.move(PIPE_SPEED);

            if(playing){
                for(Pipe pipe: pipes){
                    //Shift hit box of pipe
                    pipe.getUpper().setBounds(pipe.getPipeX(), 0, pipe.getPipeWidth(), pipe.getPipeLowerY());
                    pipe.getLower().setBounds(pipe.getPipeX(), pipe.getPipeLowerY() + pipe.getOpeningSpace(), pipe.getPipeWidth(), 600);

                    //Add score
                    if(birdX + BIRD_WIDTH == pipe.getPipeX()){
                        score++;
                        playDing();
                    }
                    //Detect collision
                    if(pipe.getUpper().intersects(bird) || pipe.getLower().intersects(bird)){
                        birdX = pipe.getPipeX() - BIRD_WIDTH;
                        gameOver();
                    }
                }
            }

            fall();
            bird.setBounds(birdX, birdY, BIRD_WIDTH, BIRD_HEIGHT);
            falling = 1;
        }
        repaint();
    }

    /**
     * Gravity pulls bird down
     */
    private void fall(){
        //Check if bird reached terminal velocity
        if(FALLING_SPEED <= TERMINAL_VELOCITY){
            FALLING_SPEED += acceleration * falling;
        }

        //Update birds y coord
        if(birdY < 470 && birdY >= 0){
            move = FALLING_SPEED;
            birdY += move;
        }
        //Keep the bird within the window
        else if(birdY < 0){
            birdY = 0;
        }
        else{   //make the bird bounce
            bounce();
            playBounce();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(playing){
            if(e.getKeyCode() == KeyEvent.VK_SPACE){
                jump();
                playJump();
            }
        }
        else{
            if(e.getKeyCode() == KeyEvent.VK_SPACE){
                restart();
            }
        }
    }

    /**
     * Handles the bounce when bird hits floor
     */
    private void bounce(){
        FALLING_SPEED = JUMP_FORCE * 1.5f;
        birdY += FALLING_SPEED;
    }

    /**
     * Handles the jump when user clicks space
     */
    private void jump(){
        FALLING_SPEED = JUMP_FORCE;
        birdY += FALLING_SPEED;
    }

    /**
     * Restarts the game
     */
    private void restart(){
        for(Pipe pipe: pipes)
            pipe.reset();

        birdX = DEFAULT_BIRD_POSITION;
        score = 0;
        playing = true;
        repaint();
    }

    /**
     * Handle the game over
     */
    private void gameOver(){
        playing = false;
        playDeath();
    }

    /**
     * Creates a new pipe
     * @param n
     */
    private void generatePipe(int n){
        Random random = new Random();
        for(int i = 0; i < n; i++){
            pipes[i] = null;
            Pipe newPipe = new Pipe(random.nextInt(150) + 30);
            pipes[i] = newPipe;
            newPipe.changeOpening(random.nextInt(200) + 100);

        }

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
