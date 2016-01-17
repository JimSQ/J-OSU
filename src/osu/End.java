package osu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class End {

    private Menu menu;
    private Image bg;
    private Image[] buttons;
    private Image text;
    private Status status;
    private ArrayList<Image> scoreImages;
    private boolean hover, press;

    public End(Menu menu, Status status) {
        this.menu = menu;
        this.status = status;
        scoreImages = status.getScoreImages();
        hover = false;
        press = false;
        try {
            bg = ImageIO.read(new File("Resources/General/end.png"));
            buttons = new Image[]{ImageIO.read(new File("Resources/General/buttons.png")),
                ImageIO.read(new File("Resources/General/buttonshover.png")),
                ImageIO.read(new File("Resources/General/buttonson.png"))};
            text = ImageIO.read(new File("Resources/General/exit.png"));
        } catch (IOException ex) {
            Logger.getLogger(End.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void writeToFile() {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(new File(menu.getSong().getPath() + "/highscores" + menu.getDifficulty() + ".txt"), true)))) {
            writer.println(status.getScore());
        } catch (IOException ex) {
            Logger.getLogger(End.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void clearPressed() {
        press = false;
    }

    public void clearHover() {
        hover = false;
    }

    public void setHover() {
        hover = true;
    }

    public void setPress() {
        press = true;
    }

    //Draws content for end screen
    public void render(Graphics g) {
        g.drawImage(menu.getSong().getBG(), 0, 0, null);
        g.drawImage(bg, 0, 0, null);
        draw(g, (int) status.getScore(), 70, -45);
        for (int i = 0; i < 3; i++) {
            draw(g, status.getScoreStats(i), 350, 80 + 100 * i);
        }
        draw(g, status.getCombo(), 350, 380);
        draw(g, (int) status.getAccuracy(), 350, 480);
        g.setColor(Color.yellow);
        if (status.getAccuracy() != 0) {
            g.fillOval(485, 635, 6, 6);
        }

        if (press) {
            g.drawImage(buttons[2], 1000, 550, null);
        } else if (hover) {
            g.drawImage(buttons[1], 1000, 550, null);
        } else {
            g.drawImage(buttons[0], 1000, 550, null);
        }
        g.drawImage(text, 1000, 550, null);
    }

    public void draw(Graphics g, int number, int x, int y) {
        for (int i = 0; i < (number + "").length(); i++) {
            g.drawImage(scoreImages.get((int) ((number / Math.pow(10, (number + "").length() - i - 1)) % 10)), x + 70 * i, y + 95, null);
        }
    }

}
