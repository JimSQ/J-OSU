package osu;

import java.awt.Graphics;
import java.awt.Rectangle;

    /**
     * The abstract class notes.
     * All notes in the game are of this type.
     * They all share common properties such as position, timing, speed, and state.
     */
public abstract class Notes {
    /**
     * x and y are the coordinates of the note
     * START_TIME is the time in the song when the note is supposed to appear
     * kill and hit are different states the note can be in.
     * kill occurs when either the note reaches the bottom of the field, it is no longer used
     * hit occurs if the player actually hits the note
     * frames is the number of updates the note takes to travel once
     * TICK_DISTANCE is the distance the note travels
     */
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
