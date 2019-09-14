package Game.Entities.Static;

import java.awt.Color;

import Main.Handler;

/**
 * Created by AlexVR on 7/2/2018.
 */
public class Apple {

    private Handler handler;

    public int xCoord;
    public int yCoord;
    
    private int maxSteps = 100; 
    private boolean good = true;
    public Color appleColor = Color.RED;
    
    public Apple(Handler handler,int x, int y){
        this.handler=handler;
        this.xCoord=x;
        this.yCoord=y;
    }
    
    public boolean isGood() {
    	return good;
    }
    
   public void checkBad(int x) {
    	if(x > maxSteps) {
    		good = false;
    		appleColor = Color.GREEN;
    	}

    }

}

