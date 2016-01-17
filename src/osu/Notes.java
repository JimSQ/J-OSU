package osu;

import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class Notes {

    protected int x;
    protected int y;
    protected final int START_TIME;
    protected boolean kill, hit;
    protected int frames = 5;
    protected final int TICK_DISTANCE = 3;

    public Notes(int x, int y, int startTime) {
        this.x = x;
        this.y = y;
        this.kill = false;
        this.hit = false;
        START_TIME = startTime;
    }

    public abstract void tick();

    public abstract void render(Graphics g);
    
    public abstract Rectangle gitGood();
    
    public abstract Rectangle getGreat();
    
    public abstract Rectangle getMiss();
    
    public boolean isDead(){
        return kill;
    }
    
    public void kill(){
        kill = true;
    }
    
    public boolean isHit(){
        return hit;
    }
    
    public void hit(){
        hit = true;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    
    public int getTime() {
        return START_TIME;
    }

    public int getFrames() {
        return frames;
    }

}
