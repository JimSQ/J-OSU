package osu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class Menu {

    private State state;

    private Image menuBG;
    private Image menuOverlay;
    private Image scroll;
    private Image[] button;
    private Image[] text;
    private Image[] upDown;
    private Image[] playButton;
    private Image songBox;

    private ArrayList<Song> songs;
    private Font myFont = new Font("Arial", Font.BOLD, 12);
    private int position = 0;
    private int diff = 0;
    private boolean[] hover = new boolean[11];
    private boolean[] release = new boolean[11];
    private int animationCounter = 130;
    private boolean[] press = new boolean[11];
    private boolean loaded = false, first = true;
    private boolean hd = false;
    private boolean fl = false;

    public Menu(ArrayList<Song> songs, State state) {
        try {
            button = new Image[]{ImageIO.read(new File("Resources/General/buttons.png")),
                ImageIO.read(new File("Resources/General/buttonshover.png")),
                ImageIO.read(new File("Resources/General/buttonson.png"))};
            songBox = ImageIO.read(new File("Resources/General/songbox.png"));
            playButton = new Image[]{ImageIO.read(new File("Resources/General/playbutton.png")),
                ImageIO.read(new File("Resources/General/playbuttonhover.png")),
                ImageIO.read(new File("Resources/General/playbuttonon.png"))};
            upDown = new Image[]{ImageIO.read(new File("Resources/General/uparrowoff.png")),
                ImageIO.read(new File("Resources/General/uparrowhover.png")),
                ImageIO.read(new File("Resources/General/uparrowon.png")),
                ImageIO.read(new File("Resources/General/downarrowoff.png")),
                ImageIO.read(new File("Resources/General/downarrowhover.png")),
                ImageIO.read(new File("Resources/General/downarrowon.png"))};
            text = new Image[]{ImageIO.read(new File("Resources/General/HD.png")),
                ImageIO.read(new File("Resources/General/FL.png"))};
            menuOverlay = ImageIO.read(new File("Resources/General/menuoverlay.png"));
            menuBG = ImageIO.read(new File("Resources/General/menubg.png"));
            this.state = state;
            this.songs = songs;
        } catch (IOException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Song getSong() {
        return songs.get(position);
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public int getDifficulty() {
        return diff;
    }

    //For song list
    public void addPos() {
        if (position == songs.size() - 1) {
            position = 0;
        } else {
            position++;
        }
    }

    //For song list
    public void subPos() {
        if (position == 0) {
            position = songs.size() - 1;
        } else {
            position--;
        }

    }

    public void setPressed(int pressed) {
        press[pressed] = true;
    }

    public void setReleased(int released) {
        release[released] = true;
    }

    public void setHover(int hovered) {
        hover[hovered] = true;
    }

    public void clearPressed() {
        for (int j = 0; j < press.length; j++) {
            press[j] = false;
        }
    }

    public void clearHover() {
        for (int j = 0; j < hover.length; j++) {
            hover[j] = false;
        }
    }

    public void startGame() {
        for (int i = 0; i < songs.size(); i++) {
            songs.get(i).stopSong();
        }
        setState(State.GAME);
    }

    public void setDiff(int diff) {
        this.diff = diff;
    }

    public boolean[] getMods() {
        return new boolean[]{hd, fl};
    }

    public void setHD(boolean on) {
        hd = on;
    }

    public void setFL(boolean on) {
        fl = on;
    }

    //Draws song list
    public void drawSongBox(Graphics g, int position, int x, int y) {
        g.drawImage(songBox, x - 20, y - 40, null);
        g.drawImage(songs.get(position).getIcon(), x + 335, y - 25, null);
        g.drawString("Title: " + songs.get(position).getName(), x, y);
        g.drawString("Artist: " + songs.get(position).getArtist(), x, y + 20);
        if (songs.get(position).getMode() == 3) {
            g.drawString("Mode: Mania", x + 230, y);
        } else if (songs.get(position).getMode() == 1) {
            g.drawString("Mode: Taiko", x + 230, y);
        }
    }

    //Menu control animations
    public void tick() {
        if (first) {
            songs.get(position).playSong();
            first = false;
        }
        if (release[0]) {
            animationCounter -= 5;
        } else if (release[1]) {
            animationCounter += 5;
        }
        if (animationCounter >= 260) {
            release[1] = false;
            animationCounter = 130;
            for (int i = 0; i < songs.size(); i++) {
                songs.get(i).stopSong();
            }
            subPos();
            diff = 0;
            try {
                songs.get(position).playSong();
            } catch (Exception exception) {
            }
        } else if (animationCounter <= 0) {
            release[0] = false;
            animationCounter = 130;
            for (int i = 0; i < songs.size(); i++) {
                songs.get(i).stopSong();
            }
            addPos();
            diff = 0;
            try {
                songs.get(position).playSong();
            } catch (Exception exception) {
            }
        }
    }

    public void renderAll(Graphics g) {
        for (int i = 0; i < songs.size(); i++) {
            addPos();
            drawSongBox(g, position, i, i);
        }
    }

    public void render(Graphics g) {
        g.setFont(myFont);
        //bg
        g.drawImage(songs.get(position).getBG(), 0, 0, null);
        //g.drawImage(menuBG, 0, 0, null);

        //songboxes animation
        g.setColor(Color.white);
        if (animationCounter >= 195) {
            subPos();
            subPos();
            drawSongBox(g, position, 680, 70 + animationCounter - 130);
            addPos();
            addPos();
        } else if (animationCounter <= 65) {
            addPos();
            addPos();
            drawSongBox(g, position, 680, 590 + animationCounter - 130);
            subPos();
            subPos();
        }
        subPos();
        if (animationCounter >= 65) {
            drawSongBox(g, position, 680 - animationCounter + 130, 200 + animationCounter - 130);
        }
        addPos();
        drawSongBox(g, position, 680 - 130 + ((animationCounter < 130) ? -animationCounter + 130 : +animationCounter - 130), 330 + animationCounter - 130);
        addPos();
        if (animationCounter <= 195) {
            drawSongBox(g, position, 680 + animationCounter - 130, 460 + animationCounter - 130);
        }
        subPos();

        //overlay
        g.drawImage(menuOverlay, 0, 0, null);
        //Info
        g.setColor(Color.white);
        g.drawString(songs.get(position).getName(), 100, 33);
        g.drawString(songs.get(position).getArtist(), 100, 63);
        if (songs.get(position).getMode() == 3) {
            g.drawString("Mania", 100, 93);
        } else if (songs.get(position).getMode() == 1) {
            g.drawString("Taiko", 100, 93);
        }
        g.drawString(Integer.toString(songs.get(position).getHitObjects(diff)), 415, 33);

        //up down buttons
        if (press[0]) {
            g.drawImage(upDown[2], 820, 100, null);
        } else if (hover[0]) {
            g.drawImage(upDown[1], 820, 100, null);
        } else {
            g.drawImage(upDown[0], 820, 100, null);
        }
        if (press[1]) {
            g.drawImage(upDown[5], 820, 540, null);
        } else if (hover[1]) {
            g.drawImage(upDown[4], 820, 540, null);
        } else {
            g.drawImage(upDown[3], 820, 540, null);
        }
        //difficulty buttons
        for (int i = 0; i < songs.get(position).getNumOfDifficulties(); i++) {
            if (press[i + 2] || i == diff) {
                g.drawImage(button[2], 300, 200 + 100 * i, null);
            } else if (hover[i + 2]) {
                g.drawImage(button[1], 300, 200 + 100 * i, null);
            } else {
                g.drawImage(button[0], 300, 200 + 100 * i, null);
            }
            g.drawString(songs.get(position).getDifficultyName(i), 310, 225 + 100 * i);
        }
        //mod buttons
        if (press[8] || hd) {
            g.drawImage(button[2], 700, 590, null);
        } else if (hover[8]) {
            g.drawImage(button[1], 700, 590, null);
        } else {
            g.drawImage(button[0], 700, 590, null);
        }
        g.drawImage(text[0], 700, 590, null);
        if (press[9] || fl) {
            g.drawImage(button[2], 825, 590, null);
        } else if (hover[9]) {
            g.drawImage(button[1], 825, 590, null);
        } else {
            g.drawImage(button[0], 825, 590, null);
        }
        g.drawImage(text[1], 825, 590, null);

        //Scroll Bar
        g.drawImage(scroll, 1175, 92, null);
        g.setColor(Color.yellow);
        if (position == 0 && animationCounter > 130 || position == songs.size() - 1 && animationCounter < 130) {
        } else {
            g.fillRect(1185, 102 + (int) (480 * ((double) (position) / (double) (songs.size()))) + (int) (480 * (1 / (double) (songs.size()))) - (int) (480 * (1 / (double) (songs.size()))) * animationCounter / 130, 10, (int) (480 * (1 / (double) (songs.size()))));
        }
        //play button
        if (press[10]) {
            g.drawImage(playButton[2], 950, 600, null);
        } else if (hover[10]) {
            g.drawImage(playButton[1], 950, 600, null);
        } else {
            g.drawImage(playButton[0], 950, 600, null);
        }

        //highscore
        g.setColor(Color.white);
        for (int i = 0; i < (songs.get(position).getScores(diff).size() <= 9 ? songs.get(position).getScores(diff).size() : 9); i++) {
            g.drawString(songs.get(position).getScores(diff).get(i), 75, 245 + 34 * i);
        }
    }

    public void reset() {
        loaded = false;
        songs.get(position).stopSong();
        songs.get(position).playSong();
    }
    
    public void initialize() {
        loaded = true;
    }

    public boolean isInitialized() {
        return loaded;
    }

}
