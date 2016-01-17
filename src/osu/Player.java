package osu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

//In game control management and pictures
public class Player {

    private boolean[] keys;
    private boolean[] firstHit;
    private boolean[] isHit;
    private boolean[] sliderIsHit;
    private int[] animationCounter;
    private int mode;
    private Handler handler;
    private Status status;
    private int x, y;
    private Image imgMH;
    private Image imgMHit;
    private Image imgSH;
    private Image imgSHit;
    private Image rDon;
    private Image lDon;
    private Image rKat;
    private Image lKat;
    private Image drum;
    private Image pressLight;
    private Image[] hitExplosion;
    private AudioInputStream[] donSoundIn;
    private AudioInputStream[] katSoundIn;
    private final int CLIPSIZE = 4;
    private File donSoundFile = new File("Resources/Taiko/DonSound.wav");
    private File katSoundFile = new File("Resources/Taiko/KatSound.wav");
    private Clip[] donSound;
    private Clip[] katSound;
    private int donCycle = 0;
    private int katCycle = 0;

    public Player(Handler handler, Status status) {
        try {
            this.hitExplosion = new Image[]{ImageIO.read(new File("Resources/Mania/hitlight1.png")),
                ImageIO.read(new File("Resources/Mania/hitlight2.png")),
                ImageIO.read(new File("Resources/Mania/hitlight3.png"))};
            this.pressLight = ImageIO.read(new File("Resources/Mania/presslight.png"));

            this.drum = ImageIO.read(new File("Resources/Taiko/Drum.png"));
            this.lKat = ImageIO.read(new File("Resources/Taiko/LKat.png"));
            this.rKat = ImageIO.read(new File("Resources/Taiko/RKat.png"));
            this.lDon = ImageIO.read(new File("Resources/Taiko/LDon.png"));
            this.rDon = ImageIO.read(new File("Resources/Taiko/RDon.png"));
            this.imgSHit = ImageIO.read(new File("Resources/Mania/sideHit.png")).getScaledInstance(70, 100, Image.SCALE_SMOOTH);
            this.imgMH = ImageIO.read(new File("Resources/Mania/middleH.png")).getScaledInstance(70, 100, Image.SCALE_SMOOTH);
            this.imgMHit = ImageIO.read(new File("Resources/Mania/middleHit.png")).getScaledInstance(70, 100, Image.SCALE_SMOOTH);
            this.imgSH = ImageIO.read(new File("Resources/Mania/sideH.png")).getScaledInstance(70, 100, Image.SCALE_SMOOTH);
            this.donSoundIn = new AudioInputStream[]{AudioSystem.getAudioInputStream(donSoundFile), AudioSystem.getAudioInputStream(donSoundFile), AudioSystem.getAudioInputStream(donSoundFile), AudioSystem.getAudioInputStream(donSoundFile)};
            this.katSoundIn = new AudioInputStream[]{AudioSystem.getAudioInputStream(katSoundFile), AudioSystem.getAudioInputStream(katSoundFile), AudioSystem.getAudioInputStream(katSoundFile), AudioSystem.getAudioInputStream(katSoundFile)};
            this.donSound = new Clip[]{AudioSystem.getClip(), AudioSystem.getClip(), AudioSystem.getClip(), AudioSystem.getClip()};
            this.katSound = new Clip[]{AudioSystem.getClip(), AudioSystem.getClip(), AudioSystem.getClip(), AudioSystem.getClip()};
            for (int i = 0; i < CLIPSIZE; i++) {
                donSound[i].open(donSoundIn[i]);
                FloatControl dControl = (FloatControl) donSound[i].getControl(FloatControl.Type.MASTER_GAIN);
                //dControl.setValue(+6.0f);
                katSound[i].open(katSoundIn[i]);
                FloatControl kControl = (FloatControl) katSound[i].getControl(FloatControl.Type.MASTER_GAIN);
                //dControl.setValue(+6.0f);
            }
        } catch (Exception e) {
        }
        this.handler = handler;
        this.status = status;
        keys = new boolean[]{false, false, false, false};
        firstHit = new boolean[]{false, false, false, false};
        isHit = new boolean[]{false, false, false, false};
        animationCounter = new int[]{-1, -1, -1, -1};

    }

    public enum HitKeys {
        DON,
        KAT,
        LDON,
        LKAT,
        RDON,
        RKAT,
        D,
        F,
        J,
        K
    }

    public void setMode(int mode) {
        this.mode = mode;
        if (mode == 3) {
            this.x = 64;
            this.y = 560;
        } else if (mode == 1) {
            this.x = 15;
            this.y = 225;
        }
    }

    public void nextClip(HitKeys key) {
        if (key == HitKeys.KAT) {
            if (katCycle == CLIPSIZE - 1) {
                katCycle = 0;
            } else {
                katCycle++;
            }
        } else if (key == HitKeys.DON) {
            if (donCycle == CLIPSIZE - 1) {
                donCycle = 0;
            } else {
                donCycle++;
            }
        }
    }

    public void hit(int keyPressed) {
        keys[keyPressed] = true;
        if (mode == 1) {
            if (katCycle == 0) {
                katSound[CLIPSIZE - 1].setFramePosition(0);
            } else {
                katSound[katCycle - 1].setFramePosition(0);
            }
            if (donCycle == 0) {
                donSound[CLIPSIZE - 1].setFramePosition(0);
            } else {
                donSound[donCycle - 1].setFramePosition(0);
            }
            if (keyPressed == 0) {
                katSound[katCycle].start();
                nextClip(HitKeys.KAT);
            }
            if (keyPressed == 1) {
                donSound[donCycle].start();
                nextClip(HitKeys.DON);
            }
            if (keyPressed == 2) {
                donSound[donCycle].start();
                nextClip(HitKeys.DON);
            }
            if (keyPressed == 3) {
                katSound[katCycle].start();
                nextClip(HitKeys.KAT);
            }
        }
    }

    public void off(int keyReleased) {
        keys[keyReleased] = false;
        firstHit[keyReleased] = true;
    }

    public void tick() {
        for (int i = 0; i < 4; i++) {
            if (isHit[i] && animationCounter[i] == -1) {
                animationCounter[i] = 0;
                isHit[i] = false;
            }
            if (animationCounter[i] >= 20) {
                animationCounter[i] = -1;
            } else if (animationCounter[i] >= 0) {
                animationCounter[i]++;
            }
        }

        for (Notes note : handler.notes) {
            if (!note.isDead()) {
                if (mode == 3) {
                    for (int i = 0; i < 4; i++) {
                        if (note instanceof Slider) {
                            if (((Slider) note).getHold().intersects(gitGood()[i])) {
                                status.addSliderCombo();
                                firstHit[i] = false;
                                isHit[i] = true;
                                break;
                            } else if (((Slider) note).getGreat().intersects(gitRelease()[i])) {
                                status.addGreat();
                                note.hit();
                                note.kill();
                                break;
                            } else if (((Slider) note).gitGood().intersects(gitRelease()[i])) {
                                status.addGood();
                                note.hit();
                                note.kill();
                                break;
                            } else if (((Slider) note).getMiss().intersects(gitRelease()[i])) {
                                status.addMiss();
                                note.kill();
                                break;
                            } else if (!note.isHit() && ((Slider) note).getHold().intersects(gitRelease()[i])) {
                                status.killSliderCombo();
                                break;
                            }
                        } else if (firstHit[i]) {
                            if (note.getGreat().intersects(gitGood()[i])) {
                                status.addGreat();
                                note.kill();
                                note.hit();
                                status.addCombo();
                                firstHit[i] = false;
                                isHit[i] = true;
                                break;
                            } else if (note.gitGood().intersects(gitGood()[i])) {
                                status.addGood();
                                note.kill();
                                note.hit();
                                status.addCombo();
                                firstHit[i] = false;
                                isHit[i] = true;
                                break;
                            } else if (note.getMiss().intersects(gitGood()[i])) {
                                note.kill();
                                status.killCombo();
                                firstHit[i] = false;
                                break;
                            }
                        }
                    }
                } else if (mode == 1) {
                    switch (((TaikoNote) note).getType()) {
                        case 0:
                            for (int i = 0; i < 4; i++) {
                                if (firstHit[i]) {
                                    if (note.getGreat().intersects(gitGood()[i])) {
                                        if (i == 1 || i == 2) {
                                            status.addGreat();
                                            note.kill();
                                            note.hit();
                                            status.addCombo();
                                            firstHit[i] = false;
                                            isHit[i] = true;
                                        } else {
                                            note.kill();
                                            status.killCombo();
                                            firstHit[i] = false;
                                        }
                                        break;
                                    } else if (note.gitGood().intersects(gitGood()[i])) {
                                        if (i == 1 || i == 2) {
                                            status.addGood();
                                            note.kill();
                                            note.hit();
                                            status.addCombo();
                                            firstHit[i] = false;
                                            isHit[i] = true;
                                        } else {
                                            note.kill();
                                            status.killCombo();
                                            firstHit[i] = false;
                                        }
                                    } else if (note.getMiss().intersects(gitGood()[i])) {
                                        note.kill();
                                        status.killCombo();
                                        firstHit[i] = false;
                                        break;
                                    }
                                }
                            }
                            break;
                        case 4:
                            for (int i = 0; i < 4; i++) {
                                if (firstHit[i]) {
                                    if (note.getGreat().intersects(gitGood()[i])) {
                                        if (i == 1 || i == 2) {
                                            status.addGreat();
                                            note.kill();
                                            note.hit();
                                            status.addCombo();
                                            firstHit[i] = false;
                                            isHit[i] = true;
                                        } else {
                                            note.kill();
                                            status.killCombo();
                                            firstHit[i] = false;
                                        }
                                        break;
                                    } else if (note.gitGood().intersects(gitGood()[i])) {
                                        if (i == 1 || i == 2) {
                                            status.addGood();
                                            note.kill();
                                            note.hit();
                                            status.addCombo();
                                            firstHit[i] = false;
                                            isHit[i] = true;
                                        } else {
                                            note.kill();
                                            status.killCombo();
                                            firstHit[i] = false;
                                        }
                                    } else if (note.getMiss().intersects(gitGood()[i])) {
                                        note.kill();
                                        status.killCombo();
                                        firstHit[i] = false;
                                        break;
                                    }
                                }
                            }
                            break;
                        case 8:
                            for (int i = 0; i < 4; i++) {
                                if (firstHit[i]) {
                                    if (note.getGreat().intersects(gitGood()[i])) {
                                        if (i == 0 || i == 3) {
                                            status.addGreat();
                                            note.kill();
                                            note.hit();
                                            status.addCombo();
                                            firstHit[i] = false;
                                            isHit[i] = true;
                                        } else {
                                            note.kill();
                                            status.killCombo();
                                            firstHit[i] = false;
                                        }
                                        break;
                                    } else if (note.gitGood().intersects(gitGood()[i])) {
                                        if (i == 0 || i == 3) {
                                            status.addGood();
                                            note.kill();
                                            note.hit();
                                            status.addCombo();
                                            firstHit[i] = false;
                                            isHit[i] = true;
                                        } else {
                                            note.kill();
                                            status.killCombo();
                                            firstHit[i] = false;
                                        }
                                    } else if (note.getMiss().intersects(gitGood()[i])) {
                                        note.kill();
                                        status.killCombo();
                                        firstHit[i] = false;
                                        break;
                                    }
                                }
                            }
                            break;
                        case 12:
                            for (int i = 0; i < 4; i++) {
                                if (firstHit[i]) {
                                    if (note.getGreat().intersects(gitGood()[i])) {
                                        if (i == 0 || i == 3) {
                                            status.addGreat();
                                            note.kill();
                                            note.hit();
                                            status.addCombo();
                                            firstHit[i] = false;
                                            isHit[i] = true;
                                        } else {
                                            note.kill();
                                            status.killCombo();
                                            firstHit[i] = false;
                                        }
                                        break;
                                    } else if (note.gitGood().intersects(gitGood()[i])) {
                                        if (i == 0 || i == 3) {
                                            status.addGood();
                                            note.kill();
                                            note.hit();
                                            status.addCombo();
                                            firstHit[i] = false;
                                            isHit[i] = true;
                                        } else {
                                            note.kill();
                                            status.killCombo();
                                            firstHit[i] = false;
                                        }
                                    } else if (note.getMiss().intersects(gitGood()[i])) {
                                        note.kill();
                                        status.killCombo();
                                        firstHit[i] = false;
                                        break;
                                    }
                                }
                            }
                            break;
                    }
                    if (((TaikoNote) note).getType() == 0 || ((TaikoNote) note).getType() == 4) {
                        if (firstHit[1] || firstHit[2]) {
                            if (note.getGreat().intersects(gitGood()[1]) || note.getGreat().intersects(gitGood()[2])) {
                                status.addGreat();
                                note.kill();
                                note.hit();
                                status.addCombo();
                                firstHit[1] = false;
                                isHit[1] = true;
                                firstHit[2] = false;
                                isHit[2] = true;
                                break;
                            } else if (note.gitGood().intersects(gitGood()[1]) || note.gitGood().intersects(gitGood()[2])) {
                                status.addGood();
                                note.kill();
                                note.hit();
                                status.addCombo();
                                firstHit[1] = false;
                                isHit[1] = true;
                                firstHit[2] = false;
                                isHit[2] = true;
                                break;
                            } else if (note.getMiss().intersects(gitGood()[1]) || note.getMiss().intersects(gitGood()[2])) {
                                note.kill();
                                status.killCombo();
                                firstHit[1] = false;
                                firstHit[2] = false;
                                break;
                            }
                        }
                    } else if (((TaikoNote) note).getType() == 8 || ((TaikoNote) note).getType() == 12) {
                        if (firstHit[0] || firstHit[3]) {
                            if (note.getGreat().intersects(gitGood()[0]) || note.getGreat().intersects(gitGood()[3])) {
                                status.addGreat();
                                note.kill();
                                note.hit();
                                status.addCombo();
                                firstHit[0] = false;
                                isHit[0] = true;
                                firstHit[3] = false;
                                isHit[3] = true;
                                break;
                            } else if (note.gitGood().intersects(gitGood()[0]) || note.gitGood().intersects(gitGood()[3])) {
                                status.addGood();
                                note.kill();
                                note.hit();
                                status.addCombo();
                                firstHit[0] = false;
                                isHit[0] = true;
                                firstHit[3] = false;
                                isHit[3] = true;
                                break;
                            } else if (note.getMiss().intersects(gitGood()[0]) || note.getMiss().intersects(gitGood()[3])) {
                                note.kill();
                                status.killCombo();
                                firstHit[0] = false;
                                firstHit[3] = false;
                                break;
                            }
                        }
                    }

                }
            }
        }
    }

    public void render(Graphics g) {
        if (mode == 1) {
            g.drawImage(drum, x, y, null);
            if (keys[0]) {
                g.drawImage(lKat, x, y, null);
            }
            if (keys[1]) {
                g.drawImage(lDon, x, y, null);
            }
            if (keys[2]) {
                g.drawImage(rDon, x, y, null);
            }
            if (keys[3]) {
                g.drawImage(rKat, x, y, null);
            }
        } else if (mode == 3) {
            g.drawImage(imgSH, x, y + 20, null);
            g.drawImage(imgMH, x + 70, y + 20, null);
            g.drawImage(imgMH, x + 140, y + 20, null);
            g.drawImage(imgSH, x + 210, y + 20, null);
            g.setColor(Color.WHITE);
            g.drawRect(x, y + 5, 280, 15);
            g.setColor(Color.BLACK);
            g.drawString("D", x + 20, y + 40);
            g.drawString("F", x + 100, y + 40);
            g.drawString("J", x + 180, y + 40);
            g.drawString("K", x + 260, y + 40);
            if (keys[0]) {
                g.drawImage(imgSHit, x, y + 20, null);
                g.drawImage(pressLight, x, y - 280, null);
            }
            if (keys[1]) {
                g.drawImage(imgMHit, x + 70, y + 20, null);
                g.drawImage(pressLight, x + 70, y - 280, null);
            }
            if (keys[2]) {
                g.drawImage(imgMHit, x + 140, y + 20, null);
                g.drawImage(pressLight, x + 140, y - 280, null);
            }
            if (keys[3]) {
                g.drawImage(imgSHit, x + 210, y + 20, null);
                g.drawImage(pressLight, x + 210, y - 280, null);
            }
            for (int i = 0; i < animationCounter.length; i++) {
                if (animationCounter[i] >= 0) {
                    g.drawImage(hitExplosion[animationCounter[i] / 10], x - 65 + 70 * i, y - 80, null);
                }
            }
        }
    }

    //player hitboxes
    public Rectangle[] gitGood() {
        Rectangle[] open = new Rectangle[4];
        if (mode == 3) {
            for (int i = 0; i < 4; i++) {
                if (keys[i]) {
                    open[i] = new Rectangle(x + 70 * i, y + 40, 70, 10);
                } else {
                    open[i] = new Rectangle(0, 0, 0, 0);
                }
            }
        } else if (mode == 1) {
            for (int i = 0; i < 4; i++) {
                if (keys[i]) {
                    open[i] = new Rectangle(x + 215, y + 60, 2, 2);
                } else {
                    open[i] = new Rectangle(0, 0, 0, 0);
                }
            }
        }
        return open;
    }

    public Rectangle[] gitRelease() {
        Rectangle[] open = new Rectangle[4];
        for (int i = 0; i < 4; i++) {
            if (!keys[i]) {
                open[i] = new Rectangle(x + 70 * i, y + 40, 70, 10);
            } else {
                open[i] = new Rectangle(0, 0, 0, 0);
            }
        }
        return open;
    }
}
