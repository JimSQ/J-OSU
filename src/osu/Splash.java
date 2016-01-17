package osu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

//Title screen
public class Splash {

    private Menu menu;
    private Image bg;
    private Image ball;
    private Image controls;
    private Player player;
    private Status status;
    private Sprites sprites;
    private int radius = -5;
    private int thumps = 1;

    public Splash(Menu menu, Player player, Status status, Sprites sprites) {
        this.menu = menu;
        this.player = player;
        this.status = status;
        this.sprites = sprites;
        try {
            bg = ImageIO.read(new File("Resources/General/splash.png"));
            controls = ImageIO.read(new File("Resources/General/controls.png"));
            ball = ImageIO.read(new File("Resources/General/ball.png"));
        } catch (IOException ex) {
            Logger.getLogger(Splash.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void tick() {
        if (radius >= 0) {
            radius += thumps;
            if (radius >= 100) {
                radius = 0;
                thumps = (int) (1 + System.currentTimeMillis() % 2);
            }
        } else {
            radius++;
        }
    }

    public void render(Graphics g) {
        g.drawImage(bg, 0, 0, null);
        g.drawImage(controls, 0, 300, null);
        if (radius >= 0) {
            g.drawImage(ball, 550 + (System.currentTimeMillis() % 2 == 0 ? 0 : radius / 4), 90 + (System.currentTimeMillis() % 2 == 0 ? radius / 4 : 0), null);
        } else {
            menu.renderAll(g);
            player.render(g);
            status.render(g);
            sprites.render(g);
            g.setColor(Color.black);
            g.fillRect(0, 0, 1200, 700);
        }
    }
}
