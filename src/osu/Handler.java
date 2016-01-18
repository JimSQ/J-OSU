package osu;

import java.awt.Graphics;
import java.util.ArrayList;
/**
 * The handler "handles" collection of the notes that need to be played
 *
 * @author Jim Zhu
 * @author Yanjun Zhang
 */
public class Handler {

 /**
 * Various variables needed
 * notes is the list of note that need to be played
 * ticker is a timer that ticks each time the system updates
 * status is the status object that takes in all the data
 * pause states whether the game is paused or not
 * ticksPerSec is the frequency in which the notes update
 * mode is based on which version of the game the user has selected
 * travelDistance is the amount of pixels the note travels per tick
 */
    ArrayList<Notes> notes = new ArrayList<>();
    private long ticker;
    private Status status;
    private boolean pause;
    private double ticksPerSec;
    private int mode;
    private double travelDistance;
/**
 * The constructor for the handler, initializes the variables
 *
 * @param  status score data is outputted to status
 */
    public Handler(Status status) {
        mode = 1;
        this.status = status;
        ticksPerSec = 200;
        travelDistance = 180000;
        ticker = -(long) (3400 - travelDistance / ticksPerSec);
        pause = false;
    }
/**
 * Changes the frequency of the updates
 *
 * @param  ticksPerSec the frequency to change into
 */
    public void setTicksPerSec(double ticksPerSec) {
        this.ticksPerSec = ticksPerSec;
        for (int i = 0; i < notes.size(); i++) {
            if (notes.get(i) instanceof Slider) {
                ((Slider) notes.get(i)).setTicksPerSecond(ticksPerSec);
            }
        }
        ticker = -(long) (3400 - travelDistance / ticksPerSec);
    }
/**
 * Changes the mode of the game, either taiko or osu mania
 *
 * @param  mode  the mode to change into
 */
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

/**
 * Updates the notes, changes their position, this happens once every tick
 * Checks if the notes have been hit, missed, still traveling etc.
 * Records changes to the status and changes the state of the notes accordingly
 */
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

/**
 * Draws the actual notes based on their data
 *
 * @param  g  the instance of graphics where the content is to be drawn
 */
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

/**
 * Checks if the notes have all been played
 *
 * @return returns true if the notes are done, false if they aren't
 */
    public boolean isDone() {
        return notes.isEmpty();
    }
/**
 * Checks if the game has been paused and changes the value of the variable pause to true
 */
    public void pause() {
        pause = true;
    }
/**
 * Checks if the game has been unpaused and changes the value of the variable pause to false
 */
    public void unpause() {
        pause = false;
    }
/**
 * Adds more notes into the handler to execute
 * @param note the note to be added
 */
    public void add(Notes note) {
        this.notes.add(note);
    }
/**
 * Adds an arraylist of notes into the handler to execute
 * This method is overridden to accept an arraylist instead of a single note
 * @param noteList the arraylist of notes to be added   
 */
    public void add(ArrayList<Notes> noteList) {
        for (Notes temp : noteList) {
            this.notes.add(temp);
        }
    }
/**
 * Removes notes from the execution list
 * @param note the note to be removed
 */
    public void remove(Notes note) {
        this.notes.remove(note);
    }
/**
 * Resets all variables back to their original state
 * Happens at the end of every song
 */
    public void reset() {
        notes = new ArrayList<>();
        ticker = -(long) (3400 - travelDistance / ticksPerSec);
        status.reset();
    }

}
