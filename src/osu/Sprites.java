package osu;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

//Taiko mode sprite animations
public class Sprites {

    protected Menu menu;
    protected int sprite = 0;
    protected int animationCounter = 0;
    protected int state;
    private Image[] idle, jump, fail;
    private Image flower;

    public Sprites(Menu menu) {
        this.menu = menu;
        try {
            flower = ImageIO.read(new File("Resources/Taiko/flower.png")).getScaledInstance(249, 167, Image.SCALE_SMOOTH);
            jump = new Image[]{ImageIO.read(new File("Resources/Taiko/pippidonclear0.png")).getScaledInstance(181, 190, Image.SCALE_SMOOTH),
                ImageIO.read(new File("Resources/Taiko/pippidonclear1.png")).getScaledInstance(181, 190, Image.SCALE_SMOOTH),
                ImageIO.read(new File("Resources/Taiko/pippidonclear2.png")).getScaledInstance(181, 190, Image.SCALE_SMOOTH),
                ImageIO.read(new File("Resources/Taiko/pippidonclear3.png")).getScaledInstance(181, 190, Image.SCALE_SMOOTH),
                ImageIO.read(new File("Resources/Taiko/pippidonclear4.png")).getScaledInstance(181, 190, Image.SCALE_SMOOTH),
                ImageIO.read(new File("Resources/Taiko/pippidonclear5.png")).getScaledInstance(181, 190, Image.SCALE_SMOOTH)};

            fail = new Image[]{ImageIO.read(new File("Resources/Taiko/pippidonfail0.png")).getScaledInstance(181, 190, Image.SCALE_SMOOTH),
                ImageIO.read(new File("Resources/Taiko/pippidonfail1.png")).getScaledInstance(181, 190, Image.SCALE_SMOOTH)};

            idle = new Image[]{ImageIO.read(new File("Resources/Taiko/pippidonidle0.png")).getScaledInstance(181, 190, Image.SCALE_SMOOTH),
                ImageIO.read(new File("Resources/Taiko/pippidonidle1.png")).getScaledInstance(181, 190, Image.SCALE_SMOOTH),
                ImageIO.read(new File("Resources/Taiko/pippidonidle2.png")).getScaledInstance(181, 190, Image.SCALE_SMOOTH),
                ImageIO.read(new File("Resources/Taiko/pippidonidle3.png")).getScaledInstance(181, 190, Image.SCALE_SMOOTH),
                ImageIO.read(new File("Resources/Taiko/pippidonidle4.png")).getScaledInstance(181, 190, Image.SCALE_SMOOTH),
                ImageIO.read(new File("Resources/Taiko/pippidonidle5.png")).getScaledInstance(181, 190, Image.SCALE_SMOOTH)};
        } catch (IOException ex) {
            Logger.getLogger(Sprites.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void jump() {
        state = 1;
        animationCounter = 0;
        sprite = 0;
    }

    public void fail() {
        state = 2;
        animationCounter = 0;
        sprite = 0;
    }

    public void tick() {
        animationCounter++;
        if (animationCounter == 40) {
            if (sprite >= 5) {
                sprite = 0;
                state = 0;
            }
            sprite++;
            animationCounter = 0;
        }
    }

    public void render(Graphics g) {
        if (menu.getSong().getMode() == 1) {
            if (state == 0) {
                g.drawImage(idle[sprite], 10, 20, null);
            } else if (state == 1) {
                g.drawImage(flower, 0, 30, null);
                g.drawImage(jump[sprite], 10, 20, null);
            } else {
                g.drawImage(fail[sprite / 3], 10, 20, null);
            }
        }
    }
}
