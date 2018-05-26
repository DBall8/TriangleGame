package dcball.Entities;

public abstract class Entity {

    protected float x;
    protected float y;

    public Entity(float x, float y){
        this.x = x;
        this.y = y;
    }

    public abstract void draw();
    public void move(){}
    public abstract void move(float time);
}
