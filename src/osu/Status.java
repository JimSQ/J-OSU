package osu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

//Stats for the game
public class Status {

    private int goods = 0, greats = 0, misses = 0, total = 0, combo = 0, maxCombo = 0;
    private double score = 0, accuracy = 0;
    private double sliderCombo = 0;
    private int animationCounter;
    private int mode = 3;
    private boolean switchHit = false;
    private ArrayList<Image> scoreImages = new ArrayList<>();
    private ArrayList<Image> percentImages = new ArrayList<>();
    private ArrayList<Image> taikoScoreImages = new ArrayList<>();
    private ArrayList<Image> taikoPercentImages = new ArrayList<>();
    private Image miss;
    private Image good;
    private Image great;
    private Image taikoGreat;
    private Image taikoGood;
    private Image taikoMiss;
    private Sprites sprites;

    private HitType hitType = HitType.none;

    public enum HitType {

        none,
        miss,
        good,
        great
    }

    public Status(Sprites sprites) {
        this.sprites = sprites;
        try {
            this.great = ImageIO.read(new File("Resources/Mania/cool2.png"));
            this.good = ImageIO.read(new File("Resources/Mania/good2.png"));
            this.miss = ImageIO.read(new File("Resources/Mania/miss.png"));
            this.taikoGreat = ImageIO.read(new File("Resources/Taiko/hit300.png"));
            this.taikoGood = ImageIO.read(new File("Resources/Taiko/hit100.png"));
            this.taikoMiss = ImageIO.read(new File("Resources/Taiko/hit0.png"));
            for (int i = 0; i < 10; i++) {
                percentImages.add(ImageIO.read(new File("Resources/General/" + i + ".png")).getScaledInstance(50, 50, Image.SCALE_SMOOTH));
                scoreImages.add(ImageIO.read(new File("Resources/General/" + i + ".png")).getScaledInstance(75, 75, Image.SCALE_SMOOTH));
                taikoScoreImages.add(ImageIO.read(new File("Resources/Taiko/score-" + i + ".png")).getScaledInstance(58, 70, Image.SCALE_SMOOTH));
                taikoPercentImages.add(ImageIO.read(new File("Resources/Taiko/score-" + i + ".png")));

            }
        } catch (IOException ex) {
            Logger.getLogger(Status.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addSliderCombo() {
        sliderCombo++;
        if (sliderCombo % 16 == 0) {
            combo++;
            score += 200 + 200 * combo / 100;
            sliderCombo = 0;
        }
        if (combo % 50 == 0) {
            sprites.jump();
        }
    }

    public void killSliderCombo() {
        sliderCombo = 0;
        maxCombo = Math.max(maxCombo, combo);
        combo = 0;
        sprites.fail();
    }

    public void addCombo() {
        combo++;
        if (combo % 50 == 0) {
            sprites.jump();
        }
    }

    public void killCombo() {
        maxCombo = Math.max(maxCombo, combo);
        combo = 0;
        sprites.fail();
    }

    public void addScore(int score) {
        this.score += score + score * combo / 100;
    }

    public void addGood() {
        this.score += 300 + 300 * combo / 100;
        this.goods++;
        this.total++;
        this.hitType = HitType.good;
        this.switchHit = true;
    }

    public void addGreat() {
        this.score += 500 + 500 * combo / 100;
        this.greats++;
        this.total++;
        this.hitType = HitType.great;
        this.switchHit = true;
    }

    public void addMiss() {
        this.misses++;
        this.total++;
        this.hitType = HitType.miss;
        this.switchHit = true;
    }

    public void tick() {
        if (hitType == HitType.none || switchHit) {
            animationCounter = 0;
            switchHit = false;
        } else {
            animationCounter++;
        }
        if (mode == 3) {
            if (animationCounter == 60) {
                hitType = HitType.none;
                animationCounter = 0;
            }
        } else if (mode == 1) {
            if (animationCounter == 20) {
                hitType = HitType.none;
                animationCounter = 0;
            }
        }
        if (total > 0) {
            accuracy = ((double) (greats * 2 + goods) / (double) (total * 2)) * 10000;
        }
    }

    public void render(Graphics g) {
        //score
        for (int i = 0; i < (int) (Math.log10(score) + 1); i++) {
            if (mode == 3) {
                g.drawImage(scoreImages.get((int) ((score / Math.pow(10, i)) % 10)), 1100 - 70 * i, 20, null);
            } else if (mode == 1) {
                g.drawImage(taikoScoreImages.get((int) ((score / Math.pow(10, i)) % 10)), 1100 - 70 * i, 20, null);
            }
        }
        //accuracy
        for (int i = 0; i < (int) (Math.log10(accuracy) + 1); i++) {
            if (mode == 3) {
                g.drawImage(percentImages.get((int) ((accuracy / Math.pow(10, i)) % 10)), 1100 - 60 * i, 130, null);
                g.setColor(Color.black);
                g.fillOval(1031, 170, 5, 5);
            } else if (mode == 1) {
                g.drawImage(taikoPercentImages.get((int) ((accuracy / Math.pow(10, i)) % 10)), 1100 - 60 * i, 110, null);
                g.setColor(Color.black);
                g.fillOval(1031, 150, 5, 5);
            }
        };

        //combo
        for (int i = 0; i < (int) (Math.log10(combo) + 1); i++) {
            if (mode == 3) {
                int location = 130 + (int) (Math.log10(combo) + 1) * 40;
                g.drawImage(scoreImages.get((int) ((combo / Math.pow(10, i)) % 10)), location - 75 * i, 60, null);
            } else if (mode == 1) {
                int location = 30 + (int) (Math.log10(combo) + 1) * 20;
                g.drawImage(taikoPercentImages.get((int) ((combo / Math.pow(10, i)) % 10)), location - 30 * i, 253, null);
            }
        }

        //hit message
        if (mode == 3) {
            if (hitType == HitType.miss) {
                g.drawImage(miss, 80, 350 - animationCounter / 2, null);
            } else if (hitType == HitType.good) {
                g.drawImage(good, 80, 350 - animationCounter / 2, null);
            } else if (hitType == HitType.great) {
                g.drawImage(great, 80, 350 - animationCounter / 2, null);
            }
        } else if (mode == 1) {
            if (hitType == HitType.miss) {
                g.drawImage(taikoMiss, 108, 157, null);
            } else if (hitType == HitType.good) {
                g.drawImage(taikoGood, 108, 157, null);
            } else if (hitType == HitType.great) {
                g.drawImage(taikoGreat, 108, 157, null);
            }
        }
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void reset() {
        hitType = HitType.none;
        animationCounter = 0;
        mode = 0;
        accuracy = 0;
        score = 0;
        combo = 0;
        maxCombo = 0;
        goods = 0;
        greats = 0;
        misses = 0;
        total = 0;
        sliderCombo = 0;
        switchHit = false;
    }

    public int getScoreStats(int i) {
        switch (i) {
            case 0:
                return greats;
            case 1:
                return goods;
            default:
                return misses;
        }
    }

    public ArrayList<Image> getScoreImages() {
        return scoreImages;
    }

    public int getCombo() {
        return maxCombo;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public int getScore() {
        return (int) score;
    }
}
