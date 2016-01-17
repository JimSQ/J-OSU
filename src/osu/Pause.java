package osu;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class Pause {

    private Image bg;
    private Image[] text;
    private Image[] buttons;
    private boolean[] hover, press;

    public Pause() {
        try {
            text = new Image[]{ImageIO.read(new File("Resources/General/Resume.png")),
                ImageIO.read(new File("Resources/General/endsong.png")),
                ImageIO.read(new File("Resources/General/Exit.png"))};
            bg = ImageIO.read(new File("Resources/General/pausebg.png"));
            buttons = new Image[]{ImageIO.read(new File("Resources/General/buttons.png")), 
                ImageIO.read(new File("Resources/General/buttonshover.png")), 
                ImageIO.read(new File("Resources/General/buttonson.png"))};
        } catch (IOException ex) {
            Logger.getLogger(Pause.class.getName()).log(Level.SEVERE, null, ex);
        }
        hover = new boolean[3];
        press = new boolean[3];
    }

    public void tick() {

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

    public void setHover(int hovered) {
        hover[hovered] = true;
    }

    public void setPress(int pressed) {
        press[pressed] = true;
    }

    public void render(Graphics g) {
        //bg
        g.drawImage(bg, 0, 0, null);

        //buttons
        for (int i = 0; i < 3; i++) {
            if (press[i]) {
                g.drawImage(buttons[2], 550, 200 + 100 * i, null);
            } else if (hover[i]) {
                g.drawImage(buttons[1], 550, 200 + 100 * i, null);
            } else {
                g.drawImage(buttons[0], 550, 200 + 100 * i, null);
            }
            g.drawImage(text[i], 550, 200 + 100 * i, null);
        }
    }
}
