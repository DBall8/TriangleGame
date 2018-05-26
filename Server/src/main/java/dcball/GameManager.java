package dcball;

import dcball.Entities.Player;
import dcball.Singletons.Settings;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;

import java.util.Set;

public class GameManager {

    private Scene scene;

    private int width;
    private int height;
    private Pane pane;
    private KeyListener keys;

    private Thread time;
    private boolean isPaused = true;

    private Player[] players = new Player[2];

    private Group debugGroup;

    public GameManager(int width, int height, Scene scene){
        this.width = width;
        this.height = height;
        this.scene = scene;

        players[0] = new Player((short)0, 300, 300);
        players[1] = new Player((short)1, 700, 700);

        pane = new Pane();
        pane.setPrefWidth(width);
        pane.setPrefHeight(height);

        debugGroup = new Group();

        for(Player p: players){
            p.track(pane);
            if(Settings.isDebug()){
                p.addDebug(debugGroup);
            }
        }

        keys = new KeyListener(scene);

        play();
    }

    public void draw(){
        for(Player p: players){
            p.draw();
        }

    }

    public void play(){
        time = new Thread(){
            synchronized public void run(){
                if(!isPaused){
                    return;
                }
                isPaused = false;
                while(true){
                    long startTime, timeTaken, timeLeft;
                    startTime = System.currentTimeMillis(); // get start time of tick

                    // update positions
                    calculateFrame();
                    draw();

                    timeTaken = System.currentTimeMillis() - startTime; // get time taken to update
                    // time left after updating
                    timeLeft = 1000L / Settings.getInstance().FRAMERATE - timeTaken;

                    try{ // wait for amount of time left in the tick
                        if(timeLeft > 0){
                            sleep(timeLeft);
                        }
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }

        };
        time.setDaemon(true);
        time.start();
    }

    public void pause(){
        try {
            time.join();
            isPaused = true;
        } catch (InterruptedException e) {
            System.out.println("Join interrupted");
            e.printStackTrace();
        }
    }

    public void calculateFrame(){
        float timeLeft = 1.00f;
        players[0].update(keys);

        do {
            // Check if each particle hits the box boundaries (must be done first as it resets collision)
            float firstCollisionTime = timeLeft; // looks for first collision
            // reset collisions for each player
            for (Player p : players) {
                p.reset();
            }

            float temptime;
            if((temptime = players[0].checkBoundaryCollisions(timeLeft)) < firstCollisionTime){
                firstCollisionTime = temptime;
            }

            for(Player p: players){
                p.move(firstCollisionTime);
            }

            timeLeft -= firstCollisionTime;

        }while(timeLeft > 0.01f);
    }

    public Pane getVisuals(){return pane;}
    public Group getDebug(){ return debugGroup; }
}
