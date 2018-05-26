package dcball.Singletons;

import dcball.Collision;
import dcball.Entities.Player;

public class Physics {

    final static float T_EPSILON = 0.005f;

    public static float getDistance(float x1, float y1, float x2, float y2){
        float xdist = x1 - x2;
        float ydist = y1 - y2;
        return (float)Math.sqrt(xdist*xdist + ydist*ydist);
    }

    public static float xComponent(float mag, float angle){
        return mag*(float)Math.sin(angle);

    }

    public static float yComponent(float mag, float angle){
        return -mag*(float)Math.cos(angle);
    }

    public static float findAngle(float x1, float y1, float x2, float y2){
        float dx = -1*(x2 - x1);
        float dy = y2 - y1;
        // goal angle to turn to
        float angle = (float)Math.atan(dx/dy);

        // adjust for fact that arctan can only return a value from -90 to 90
        if(dy > 0){
            angle += Math.PI;
        }
        return angle;
    }

    // check if the Entity collides with the bounding box
    public static void checkBoxCollision(Player e, float xmin, float ymin,
                                         float xmax, float ymax, float t, Collision C){

        // right
        checkVerticalLine(e.getX(), e.getXvel(), e.getYvel(), (float)e.getHitBox().getWidth()/2, xmax, t, C);


        // left
        checkVerticalLine(e.getX(), e.getXvel(), e.getYvel(), (float)e.getHitBox().getWidth()/2, xmin, t, C);

        // up
        checkHorizontalLine(e.getY(), e.getXvel(), e.getYvel(), (float)e.getHitBox().getHeight()/2, ymin, t, C);

        // down
        checkHorizontalLine(e.getY(), e.getXvel(), e.getYvel(), (float)e.getHitBox().getHeight()/2, ymax, t, C);

    }

    // check if a particle hits a vertical line
    public static void checkVerticalLine(float xpos, float xvol, float yvol, float radius, float linex,
                                         float tmax, Collision C){
        Collision tempC = new Collision();

        if(xvol == 0){ // no collision if nothing moves
            return;
        }

        float distance; // distance from border (negative is a left move, positive is a right move)
        if(linex < xpos){
            distance = xpos - (linex + radius);
        }
        else{
            distance = xpos - (linex - radius);
        }
        distance = -1*distance;
        float timetocollision = distance/xvol;
        if(timetocollision>=0 && timetocollision<=tmax){
            tempC.t = timetocollision;
            tempC.xcollide = true; // stop x
        }

        if(tempC.t < C.t){
            C.copy(tempC);
        }

    }

    // check if a particle hits a horizontal line
    public static void checkHorizontalLine(float ypos, float xvol, float yvol, float radius, float liney,
                                           float tmax, Collision C){

        Collision tempC = new Collision();

        if(yvol == 0){ // no collision if nothing moves
            return;
        }

        float distance; // distance from border (negative is an up move, positive is a down move)
        if(liney < ypos){
            distance = ypos - (liney + radius);
        }
        else{
            distance = ypos - (liney - radius);
        }
        distance = -1*distance;
        float timetocollision = distance/yvol;
        if(timetocollision>=0 && timetocollision<=tmax){
            tempC.t = timetocollision;
            tempC.ycollide = true; // stop y
        }

        if(tempC.t < C.t){
            C.copy(tempC);
        }
    }
}
