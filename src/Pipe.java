import java.awt.*;

public class Pipe{
    private final int PIPE_WIDTH = 100;
    private int pipeX;

    private int pipeLowerY;
    private Rectangle upper;    //Upper pipe hit box
    private Rectangle lower;    //Lower pipe hit boc

    private int openingSpace = 100; //Starting opening space

    Pipe(int openingHeight){
        pipeLowerY = openingHeight;

        pipeX = 1000;   //Start outside of window
        upper = new Rectangle(pipeX, 0, PIPE_WIDTH, pipeLowerY);
        lower = new Rectangle(pipeX, pipeLowerY + openingSpace, PIPE_WIDTH, 600);
    }

    void reset(){
        pipeX = 1000;
    }

    public int getPipeX(){
        return pipeX;
    }

    public int getPipeWidth(){
        return PIPE_WIDTH;
    }

    public int getOpeningSpace(){
        return openingSpace;
    }

    public void move(float speed){
        pipeX += speed;
    }

    public void changeOpening(int opening){
        openingSpace = opening;
    }

    public int getPipeLowerY() {
        return pipeLowerY;
    }

    /**
     *
     * @return upper hit box
     */
    public Rectangle getUpper(){
        return upper;
    }

    /**
     *
     * @return lower hit box
     */
    public Rectangle getLower(){
        return lower;
    }
}
