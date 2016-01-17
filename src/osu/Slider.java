package osu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;

/**
 *
 * @author Jim
 */
public class Slider extends Notes {

    private Image image;
    public final int END_TIME;
    public int count = 0, require, length = 15;
    public double ticksPerSecond;
    public long start, current;
    public boolean remove = false;

    public Slider(int x, int y, int startTime, int endTime) {
        super(x, y, startTime);
        ticksPerSecond = 200;
        END_TIME = endTime;
        current = 0;
        require = END_TIME - START_TIME - 15;
        switch (x) {
            case 64:
                image = new ImageIcon("Resources/Mania/side.png").getImage().getScaledInstance(70, 1, Image.SCALE_SMOOTH);
                break;
            case 134:
                image = new ImageIcon("Resources/Mania/middle.png").getImage().getScaledInstance(70, 1, Image.SCALE_SMOOTH);
                break;
            case 204:
                image = new ImageIcon("Resources/Mania/middle.png").getImage().getScaledInstance(70, 1, Image.SCALE_SMOOTH);
                break;
            case 274:
                image = new ImageIcon("Resources/Mania/side.png").getImage().getScaledInstance(70, 1, Image.SCALE_SMOOTH);
                break;
        }
    }

    public int getEndTime() {
        return END_TIME;
    }

    //movements of the sliders
    @Override
    public void tick() {
        if (current > require) {
            remove = true;
            if (y + length >= 640) {
                y += TICK_DISTANCE;
                length -= TICK_DISTANCE * 2;
            } else {
                y += TICK_DISTANCE;
            }
            if (y >= 640) {
                kill();
            }
        }
        if (y + length <= 640 && !remove) {
            length += TICK_DISTANCE;
        }
        current += 1000 / ticksPerSecond;
    }

    public void setTicksPerSecond(double ticks) {
        ticksPerSecond = ticks;
    }

    @Override
    public void render(Graphics g) {
        int temp = y;
        for (int i = 0; i < length; i++) {
            g.drawImage(image, x, y, null);
            y++;
        }
        y = temp;
    }

    @Override
    public Rectangle gitGood() { //Hit boxes
        return new Rectangle(x, y - 10, 70, 80);
    }

    @Override
    public Rectangle getGreat() { //Hit boxes
        return new Rectangle(x, y - 20, 70, 70);
    }

    public Rectangle getHold() { //Hit boxes
        return new Rectangle(x, y, 70, length + 30);
    }

    @Override
    public Rectangle getMiss() { //Hit boxes
        return new Rectangle(x, y, 70, 75);
    }
}
