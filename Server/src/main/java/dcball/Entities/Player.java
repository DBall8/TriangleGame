package dcball.Entities;

import dcball.Collision;
import dcball.KeyListener;
import dcball.Singletons.Physics;
import dcball.Singletons.Settings;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public class Player extends Entity{

    private static final float WIDTH = 60;
    private static final float HEIGHT = 70;
    private static final Color P1COLOR = Color.RED;
    private static final Color P2COLOR = Color.BLUE;
    private static final Color DEFAULTCOLOR = Color.GREEN;

    private static final float MAX_VELOCITY = 10;
    private static final float ACCEL = 0.3f;
    private static final float RACCEL = 10;

    private float velocity;
    private float xvel;
    private float yvel;
    private double angle;

    private Polygon triangle;
    private Rectangle hitbox;

    private Collision tempCollision = new Collision();
    private Collision earliestCollision = new Collision();

    public Player(short pnum, float x, float y){
        super(x, y);
        velocity = 0;
        xvel = 0;

        yvel = 0;
        angle = 0;

        triangle = new Polygon();
        triangle.getPoints().addAll((double)WIDTH/2, 0.0,
                0.0, (double)HEIGHT,
                (double)WIDTH, (double)HEIGHT);

        hitbox = new Rectangle(0.0, 0.0, WIDTH, HEIGHT);
        hitbox.setFill(Color.TRANSPARENT);
        hitbox.setStroke(Color.GREEN);

        switch(pnum){
            case 0:
                triangle.setFill(P1COLOR);
                break;
            case 1:
                triangle.setFill(P2COLOR);
                break;
            default:
                triangle.setFill(DEFAULTCOLOR);
        }
    }

    public void draw(){
        triangle.translateXProperty().set(x - WIDTH/2);
        triangle.translateYProperty().set(y - HEIGHT/2);
        triangle.rotateProperty().set(angle);

        if(Settings.isDebug()){
            hitbox.translateXProperty().set(x - WIDTH/2);
            hitbox.translateYProperty().set(y - HEIGHT/2);
            hitbox.rotateProperty().set(angle);
        }
    }

    public void track(Pane entities){
        entities.getChildren().add(triangle);
    }

    public void update(KeyListener keys){

        // Get the angle the mouse pointer is from the player
        double mouseAngle =180/Math.PI * Physics.findAngle(x, y, keys.getMouseX(), keys.getMouseY());
        // Get a test factor for finding the direction to rotate in
        double test = 180/Math.PI *Math.sin((mouseAngle - angle)*Math.PI/180);

        // Rotate clockwise if the test is significantly positive
        if(test > RACCEL){
            angle += RACCEL;
        }
        // Rotate counter-clockwise if the test is significantly negative
        else if(test < -RACCEL){
            angle -= RACCEL;
        }
        // Move to the exact angle if the rotation difference is within the rotation speed
        else {
            angle += test;
        }

        if(keys.isUp() && !keys.isDown() && velocity < MAX_VELOCITY){
            if(Physics.getDistance(x, y, keys.getMouseX(), keys.getMouseY()) < HEIGHT){
                decelerate();
            }
            else{
                velocity += ACCEL;
            }
        }
        else if(!keys.isUp() && keys.isDown() && velocity > -1*MAX_VELOCITY){
            velocity -= ACCEL;
        }
        else{
            decelerate();
        }
        float angleRad = (float)(angle * Math.PI/180);
        xvel = Physics.xComponent(velocity, angleRad);
        yvel = Physics.yComponent(velocity, angleRad);


    }

    private void decelerate(){
        if(velocity > 0.5){
            velocity -= ACCEL;
        }
        else if(velocity < -0.5){
            velocity += ACCEL;
        }
        else{
            velocity = 0;
        }
    }

    public void move(float time){
        // if collision in time step, update accordingly
        if(earliestCollision.t <= time){
            x = earliestCollision.getNewX(x, xvel);
            y = earliestCollision.getNewY(y, yvel);
            if(earliestCollision.xcollide){
                xvel = 0;
            }
            if(earliestCollision.ycollide){
                yvel = 0;
            }

        }
        // otherwise update location
        else{
            x += xvel*time;
            y += yvel*time;
        }
    }


    public float checkBoundaryCollisions(float time){
        // Check if the player will collide with the boundaries of the play field
        Physics.checkBoxCollision(this, 0, 0, Settings.getInstance().WIDTH, Settings.getInstance().HEIGHT, time, tempCollision);
        if(tempCollision.t < earliestCollision.t){
            earliestCollision.copy(tempCollision);
            return tempCollision.t;
        }
        return time;
    }

    public void reset(){
        earliestCollision.reset();
        tempCollision.reset();
    }

    // GETTERS

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getXvel() {
        return xvel;
    }

    public float getYvel() {
        return yvel;
    }

    public Rectangle getHitBox(){
        return hitbox;
    }

    public void addDebug(Group g){
        g.getChildren().add(this.hitbox);
    }


}
