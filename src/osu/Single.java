package osu;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;

public class Single extends Notes {

    private int length;
    private Image image;

    public Single(int x, int y, int startTime) {
        super(x, y, startTime);
        this.length = 20;
        switch (x) {
            case 64:
                image = new ImageIcon("Resources/Mania/side.png").getImage().getScaledInstance(70, 20, Image.SCALE_SMOOTH);
                break;
            case 134:
                image = new ImageIcon("Resources/Mania/middle.png").getImage().getScaledInstance(70, 20, Image.SCALE_SMOOTH);
                break;
            case 204:
                image = new ImageIcon("Resources/Mania/middle.png").getImage().getScaledInstance(70, 20, Image.SCALE_SMOOTH);
                break;
            case 274:
                image = new ImageIcon("Resources/Mania/side.png").getImage().getScaledInstance(70, 20, Image.SCALE_SMOOTH);
                break;
        }
    }

    //Movements of the note
    @Override
    public void tick() {
        if (y < 640) {
            y += TICK_DISTANCE;
        } else {
            kill();
        }
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(image, x, y, null);
      
    }

    @Override
    public Rectangle gitGood() { //Hit boxes
        return new Rectangle(x, y - 20, 70, 110);
    }

    @Override
    public Rectangle getGreat() { //Hit boxes
        return new Rectangle(x, y - 10, 70, 80);
    }

    @Override
    public Rectangle getMiss() { //Hit boxes
        return new Rectangle(x, y, 70, 95);
    }
}
