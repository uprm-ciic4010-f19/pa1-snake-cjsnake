package Game.Entities.Static;

import Main.Handler;

/**
 * Created by AlexVR on 7/2/2018.
 */
public class Apple {

    private Handler handler;

    public int xCoord;
    public int yCoord;
    public boolean rotten;

    public Apple(Handler handler,int x, int y, boolean rotten){
        this.handler=handler;
        this.xCoord=x;
        this.yCoord=y;
        this.rotten=rotten;
    }

}
