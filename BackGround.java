import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.*;

public class BackGround extends JPanel implements ActionListener, KeyListener{
    int width = 288;
    int height = 512;
    double score = 0;

    Image BackgroundImg;
    Image BirdImg;
    Image topPipeImg;
    Image BottomPipeImg;
    Image BaseImg;

    int BirdX = width/8;
    int BirdY = height/2;
    int BirdWidth = 34;
    int BirdHeight = 24;

    class Bird{
        int x = BirdX;
        int y = BirdY;
        int Bwidth = BirdWidth;
        int Bheight = BirdHeight;
        Image img;

        public Bird(Image img) {
            this.img = img;
        }
        
    }

    //pipes
    int pipeX = width;
    int pipeY = 0;
    int pipeWidth = 52;
    int pipeHeight = 320;

    class Pipe {
        int x = pipeX;
        int y = pipeY;
        int width = pipeWidth;
        int height = pipeHeight;
        Image img;
        boolean passed = false;

        public Pipe(Image img) {
            this.img = img;
        }

        
    }

    // GAME LOGIC
    Bird bird;
    int velocityX = -4;
    int velocityY = 0;
    int gravity = 1;

    ArrayList<Pipe> pipes;
    Random random = new Random();

    Timer gameLoop;
    Timer placePipesTimer;

    boolean GameOver = false;

    BackGround() {
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.black);

        setFocusable(true);
        addKeyListener(this);

         //game timer
         gameLoop = new Timer(1000/60, this); //does this fx every second 60FPS
         //gameLoop.start();
         addKeyListener(this);

        //load image
        BackgroundImg = new ImageIcon(getClass().getResource("./background-night.png")).getImage();
        BirdImg = new ImageIcon(getClass().getResource("./redbird-midflap.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("./pipe-green_top.png")).getImage();
        BottomPipeImg = new ImageIcon(getClass().getResource("./pipe-green_bottom.png")).getImage();
        BaseImg = new ImageIcon(getClass().getResource("./base.png")).getImage();

        //bird
        bird = new Bird(BirdImg);
        pipes = new ArrayList<Pipe>();

        //pipes timer
        placePipesTimer = new Timer(1500,new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                placePipes();
            }
        });
        placePipesTimer.start();

       

    }

    public void placePipes() {
        //random gives value b/w 0 and 1 thus it will be between 0 and 160 
        int randomPipeY = (int) (pipeY - pipeHeight/4 - Math.random()*(pipeHeight/2));    
        int openingSpace = height/6;
        
        Pipe topPipe = new Pipe(topPipeImg);
        topPipe.y = randomPipeY;
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe(BottomPipeImg);
        bottomPipe.y = topPipe.y + pipeHeight + openingSpace;
        pipes.add(bottomPipe);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        g.drawImage(BackgroundImg, 0, 0, width, height, null); // BG
        g.drawImage(BirdImg, bird.x, bird.y, bird.Bheight, bird.Bwidth, null); //bird
        g.drawImage(BaseImg, 0, height-112, 336, 112, null); //base

        //pipes
        for(int i=0 ; i<pipes.size() ; i++){
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }

        //score display
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 25));
        if (GameOver){
            g.drawString("Game Over: " + String.valueOf((int) score), 10, 35);
        }
        else{
            g.drawString(String.valueOf((int) score), 10, 35);
        }

    }

    public void move() {
        //bird
        velocityY += gravity;
        bird.y += velocityY;
        bird.y = Math.max(bird.y, 0); // upper boundary
        bird.y = Math.min(bird.y, 400 - bird.Bheight); // Lower boundary 

        //pipe movement
        for(int i=0 ; i<pipes.size() ; i++){
            Pipe pipe = pipes.get(i);
            pipe.x += velocityX;

            if(!pipe.passed && bird.x > pipe.x + pipe.width){
                pipe.passed = true;
                score +=0.5;
            }

            if (collision(bird, pipe)) {
                GameOver = true;
            }

        }

        if (bird.y <= 0 || bird.y >= height - 112 - bird.Bheight) { // 112 accounts for the base height
            GameOver = true;
        }

    }

    public boolean collision (Bird a, Pipe b){
        return a.x < b.x + b.width &&   //a top right does nnot match with b top left
                a.x + a.Bwidth > b.x && //a top left does not match with b top right
                a.y < b.y + b.height && //a top left does not match with b top left
                a.y + a.Bheight > b.y; // a top right doe snot match with b top right
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(GameOver){
            placePipesTimer.stop();
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            velocityY = -7;

            //reset conditions

            if(GameOver){
                bird.y = BirdY;
                velocityY = 0;
                pipes.clear();
                score = 0;
                GameOver = false;
                gameLoop.start();
                placePipesTimer.start();
            }

        }
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            gameLoop.start();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
       
    }

    @Override
    public void keyReleased(KeyEvent e) {
       
    }

}