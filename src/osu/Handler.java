package osu;

import java.awt.Graphics;
import java.util.ArrayList;

public class Handler {

    ArrayList<Notes> notes = new ArrayList<>();
    private long ticker;
    private Status status;
    private boolean pause;
    private double ticksPerSec;
    private int mode;
    private double travelDistance;

    public Handler(Status status) {
        mode = 1;
        this.status = status;
        ticksPerSec = 200;
        travelDistance = 180000;
        ticker = -(long) (3400 - travelDistance / ticksPerSec);
        pause = false;
    }

    public void setTicksPerSec(double ticksPerSec) {
        this.ticksPerSec = ticksPerSec;
        for (int i = 0; i < notes.size(); i++) {
            if (notes.get(i) instanceof Slider) {
                ((Slider) notes.get(i)).setTicksPerSecond(ticksPerSec);
            }
        }
        ticker = -(long) (3400 - travelDistance / ticksPerSec);
    }

    public void setMode(int mode) {
        this.mode = mode;
        switch (mode) {
            case 1:
                travelDistance =90000;
                break;
            case 3:
                travelDistance = 180000;
                break;
        }
        ticker = -(long) (3400 - travelDistance / ticksPerSec);
    }

    //Manages notes' movements
    public void tick() {
        for (int i = 0; i < notes.size(); i++) {
            Notes note = notes.get(i);
            if (note.isDead()) {
                if (!note.isHit()) {
                    status.addMiss();
                    status.killCombo();
                }
                remove(note);
            } else if (ticker > note.getTime()) {
                note.tick();
            }
        }
        if (!pause) {
            ticker += 1000 / ticksPerSec;
        }
    }

    //Manages notes' drawing
    public void render(Graphics g) {
        for (int i = 0; i < notes.size(); i++) {
            Notes note = notes.get(i);
            if (!note.isDead()) {
                if (ticker > note.getTime()) {
                    note.render(g);
                }
            }
        }
    }

    public boolean isDone() {
        return notes.isEmpty();
    }

    public void pause() {
        pause = true;
    }

    public void unpause() {
        pause = false;
    }

    public void add(Notes note) {
        this.notes.add(note);
    }

    public void add(ArrayList<Notes> noteList) {
        for (Notes temp : noteList) {
            this.notes.add(temp);
        }
    }

    public void remove(Notes note) {
        this.notes.remove(note);
    }

    public void reset() {
        notes = new ArrayList<>();
        ticker = -(long) (3400 - travelDistance / ticksPerSec);
        status.reset();
    }

}
