package Game.Entities.Dynamic;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JOptionPane;

import Game.GameStates.State;
import Main.Handler;




/**

 * Created by AlexVR on 7/2/2018.

 */

public class Player {

    public int length;
    public boolean justAte;
    private Handler handler;
    public int moves = 0;

    public int xCoord;
    public int yCoord;

    public int moveCounter;
    public int speedCount = 5;
    public int stepCount = 0;

    public String direction;
    
    public double gameScore;
    public int score;

    public Player(Handler handler){

        this.handler = handler;
        xCoord = 0;
        yCoord = 0;
        moveCounter = 0;
        direction= "Right";
        justAte = false;
        int lenght = 1;
        gameScore = 0;
        score = 0;



    }



    public void tick(){

        moveCounter++;
        stepCount++;
        
        if(moveCounter>=speedCount) {
        	moves++;
            checkCollisionAndMove();

            moveCounter=0;

        }

        if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_UP) && !(direction == "Down")) { // backtracking eliminated

            direction="Up";

        }if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_DOWN) && !(direction == "Up")) {

            direction="Down";

        }if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_LEFT) && !(direction == "Right")) {

            direction="Left";

        }if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_RIGHT) && !(direction == "Left")) {

            direction="Right";

        }
        if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_N)){ // Anadi boton de N
        	Eat(); 
        	handler.getWorld().appleOnBoard=true;	
        }
        if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_MINUS)) {
        	speedCount = speedCount + 2;
        	
        }
        if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_EQUALS)){
        	speedCount = speedCount - 2;
        
        }

        if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_MINUS)) {

        	speedCount = speedCount + 1;

        	

        }

        if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_EQUALS)){

        	speedCount = speedCount - 1;

        }

        if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_ESCAPE)) {

        	State.setState(handler.getGame().pauseState);

        }

        if(handler.getWorld().body.size()>0) {

        	for (int bodyOfSnake = 0; bodyOfSnake < handler.getWorld().body.size(); bodyOfSnake++) {

        		if (xCoord == handler.getWorld().body.get(bodyOfSnake).x && yCoord == handler.getWorld().body.get(bodyOfSnake).y) {

        			kill();

        			JOptionPane.showMessageDialog(null, "Game Over","", JOptionPane.WARNING_MESSAGE);

                    	   System.exit(0);

                    }

        		}

        	}

    }



    public void checkCollisionAndMove(){

        handler.getWorld().playerLocation[xCoord][yCoord]=false;

        int x = xCoord;

        int y = yCoord;

        switch (direction){

            case "Left":

                if(xCoord==0){

                	
                	xCoord = handler.getWorld().GridWidthHeightPixelCount-1;

                }else{

                    xCoord--;

                }

                break;

            case "Right":

                if(xCoord==handler.getWorld().GridWidthHeightPixelCount-1){

                	
                	xCoord = 0;

                }else{

                    xCoord++;

                }

                break;

            case "Up":

                if(yCoord==0){

                    kill();

                    JOptionPane.showMessageDialog(null, "Game Over", "", JOptionPane.WARNING_MESSAGE);

                    System.exit(0);

                }else{

                    yCoord--;

                }

                break;

            case "Down":

                if(yCoord==handler.getWorld().GridWidthHeightPixelCount-1){

                    kill();

                    JOptionPane.showMessageDialog(null, "Game Over", "", JOptionPane.WARNING_MESSAGE);

                    System.exit(0);

                }else{

                    yCoord++;

                }

                break;

        }

        handler.getWorld().playerLocation[xCoord][yCoord]=true;


        if(handler.getWorld().appleLocation[xCoord][yCoord]){

            Eat();
            
            if(handler.getWorld().getApple().isGood())
            	gameScore=Math.sqrt(2*gameScore+1);
            else gameScore=-Math.sqrt(2*gameScore+1);
            speedCount = speedCount - 1;

            score = (int)gameScore;
        }



        if(!handler.getWorld().body.isEmpty()) {
        	
        	
	            handler.getWorld().playerLocation[handler.getWorld().body.getLast().x][handler.getWorld().body.getLast().y] = false;
	
	            handler.getWorld().body.removeLast();
	
	            handler.getWorld().body.addFirst(new Tail(x, y,handler));
        	
        
        }
        
        


    }



    public void render(Graphics g,Boolean[][] playeLocation){

        Random r = new Random();
        
        // implemented score
        
        int R = (int)(Math.random() * 256);

        int G = (int)(Math.random() * 256);

        int B = (int)(Math.random() * 256);
        
        Color color = new Color(R, G, B);
        
        
        int fontSize = 40;

        g.setFont(new Font("TimesRoman", Font.PLAIN, fontSize));

        g.setColor(Color.ORANGE);

        g.drawString("Score: " + score, 50, 50);
        

        for (int i = 0; i < handler.getWorld().GridWidthHeightPixelCount; i++) {

            for (int j = 0; j < handler.getWorld().GridWidthHeightPixelCount; j++) {
                g.setColor(Color.GREEN); // changed color of snake

                if(playeLocation[i][j]){

                    g.fillRect((i*handler.getWorld().GridPixelsize),
                            (j*handler.getWorld().GridPixelsize),
                            handler.getWorld().GridPixelsize,

                            handler.getWorld().GridPixelsize);

                }
                
                if(handler.getWorld().appleLocation[i][j]){
                	
                	handler.getWorld().getApple().checkBad(moves);
                 	g.setColor(handler.getWorld().getApple().appleColor);
                 	
                    g.fillRect((i*handler.getWorld().GridPixelsize),
                            (j*handler.getWorld().GridPixelsize),
                            handler.getWorld().GridPixelsize,
                            handler.getWorld().GridPixelsize);

                }

            }

        }





    }



    public void Eat(){

        int lenght = 0;
		lenght++;
        moves = 0;
        Tail tail= null;

        handler.getWorld().appleLocation[xCoord][yCoord]=false;

        handler.getWorld().appleOnBoard=false;

        switch (direction){

            case "Left":

                if( handler.getWorld().body.isEmpty()){

                    if(this.xCoord!=handler.getWorld().GridWidthHeightPixelCount-1){

                        tail = new Tail(this.xCoord+1,this.yCoord,handler);

                    }else{

                        if(this.yCoord!=0){

                            tail = new Tail(this.xCoord,this.yCoord-1,handler);

                        }else{

                            tail =new Tail(this.xCoord,this.yCoord+1,handler);

                        }

                    }

                }else{

                    if(handler.getWorld().body.getLast().x!=handler.getWorld().GridWidthHeightPixelCount-1){

                        tail=new Tail(handler.getWorld().body.getLast().x+1,this.yCoord,handler);

                    }else{

                        if(handler.getWorld().body.getLast().y!=0){

                            tail=new Tail(handler.getWorld().body.getLast().x,this.yCoord-1,handler);

                        }else{

                            tail=new Tail(handler.getWorld().body.getLast().x,this.yCoord+1,handler);



                        }

                    }



                }

                break;

            case "Right":

                if( handler.getWorld().body.isEmpty()){

                    if(this.xCoord!=0){

                        tail=new Tail(this.xCoord-1,this.yCoord,handler);

                    }else{

                        if(this.yCoord!=0){

                            tail=new Tail(this.xCoord,this.yCoord-1,handler);

                        }else{

                            tail=new Tail(this.xCoord,this.yCoord+1,handler);

                        }

                    }

                }else{

                    if(handler.getWorld().body.getLast().x!=0){

                        tail=(new Tail(handler.getWorld().body.getLast().x-1,this.yCoord,handler));

                    }else{

                        if(handler.getWorld().body.getLast().y!=0){

                            tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord-1,handler));

                        }else{

                            tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord+1,handler));

                        }

                    }



                }

                break;

            case "Up":

                if( handler.getWorld().body.isEmpty()){

                    if(this.yCoord!=handler.getWorld().GridWidthHeightPixelCount-1){

                        tail=(new Tail(this.xCoord,this.yCoord+1,handler));

                    }else{

                        if(this.xCoord!=0){

                            tail=(new Tail(this.xCoord-1,this.yCoord,handler));

                        }else{

                            tail=(new Tail(this.xCoord+1,this.yCoord,handler));

                        }

                    }

                }else{

                    if(handler.getWorld().body.getLast().y!=handler.getWorld().GridWidthHeightPixelCount-1){

                        tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord+1,handler));

                    }else{

                        if(handler.getWorld().body.getLast().x!=0){

                            tail=(new Tail(handler.getWorld().body.getLast().x-1,this.yCoord,handler));

                        }else{

                            tail=(new Tail(handler.getWorld().body.getLast().x+1,this.yCoord,handler));

                        }

                    }



                }

                break;

            case "Down":

                if( handler.getWorld().body.isEmpty()){

                    if(this.yCoord!=0){

                        tail=(new Tail(this.xCoord,this.yCoord-1,handler));

                    }else{

                        if(this.xCoord!=0){

                            tail=(new Tail(this.xCoord-1,this.yCoord,handler));

                        }else{

                            tail=(new Tail(this.xCoord+1,this.yCoord,handler));

                        } System.out.println("Tu biscochito");

                    }

                }else{

                    if(handler.getWorld().body.getLast().y!=0){

                        tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord-1,handler));

                    }else{

                        if(handler.getWorld().body.getLast().x!=0){

                            tail=(new Tail(handler.getWorld().body.getLast().x-1,this.yCoord,handler));

                        }else{

                            tail=(new Tail(handler.getWorld().body.getLast().x+1,this.yCoord,handler));

                        }

                    }



                }

                break;

        }
        if(handler.getWorld().getApple().isGood()) {
        	handler.getWorld().body.addLast(tail);

            handler.getWorld().playerLocation[tail.x][tail.y] = true;
        }
        else {
        	if(handler.getWorld().body.isEmpty()) {
        		kill();

    			JOptionPane.showMessageDialog(null, "Game Over","", JOptionPane.WARNING_MESSAGE);

                System.exit(0);
        	}
        	else {
	    		handler.getWorld().playerLocation[handler.getWorld().body.getLast().x][handler.getWorld().body.getLast().y] = false;
	    		
	            handler.getWorld().body.removeLast();
        	}
    	}
        
        

    }



    public void kill(){

        int lenght = 0;

        for (int i = 0; i < handler.getWorld().GridWidthHeightPixelCount; i++) {

            for (int j = 0; j < handler.getWorld().GridWidthHeightPixelCount; j++) {

                handler.getWorld().playerLocation[i][j]=false;

            }

        }

    }



    public boolean isJustAte() {

        return justAte;

    }


    public void setJustAte(boolean justAte) {

        this.justAte = justAte;

    }
    

}