package osu;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;

public class TaikoNote extends Notes {

    public enum NoteType {
        DON,
        KAT,
        BIGDON,
        BIGKAT
    }

    private Image img;
    private int intType;
    private NoteType type;

    public TaikoNote(int startTime, int type) {
        super(1200, 235, startTime);
        this.intType = type;
        if (type == 0) {
            this.type = NoteType.DON;
            img = new ImageIcon("Resources/Taiko/Don.png").getImage().getScaledInstance(100, 100, Image.SCALE_FAST);
        } else if (type == 8) {
            this.type = NoteType.KAT;
            img = new ImageIcon("Resources/Taiko/Kat.png").getImage().getScaledInstance(100, 100, Image.SCALE_FAST);
        } else if (type == 4) {
            this.type = NoteType.BIGDON;
            img = new ImageIcon("Resources/Taiko/BigDon.png").getImage().getScaledInstance(150, 150, Image.SCALE_FAST);
        } else if (type == 12) {
            this.type = NoteType.BIGKAT;
            img = new ImageIcon("Resources/Taiko/BigKat.png").getImage().getScaledInstance(150, 150, Image.SCALE_FAST);
        }
    }

    public int getType() {
        return intType;
    }

    @Override
    public void tick() {
        if (x > 50) {
            x -= 5;
        } else {
            kill();
        }
    }

    @Override
    public void render(Graphics g) {
        if (type == NoteType.BIGDON || type == NoteType.BIGKAT) {
            g.drawImage(img, x, y - 25, null);

        } else {
            g.drawImage(img, x, y, null);
        }
    }

    @Override
    public Rectangle gitGood() { //Hit boxes
        if (type == NoteType.BIGDON || type == NoteType.BIGKAT) {
            return new Rectangle(x - 45, y, 190, 100);
        }
        return new Rectangle(x - 20, y, 140, 100);
    }

    @Override
    public Rectangle getGreat() { //Hit boxes
        if (type == NoteType.BIGDON || type == NoteType.BIGKAT) {
            return new Rectangle(x - 30, y, 160, 100);
        }
        return new Rectangle(x - 10, y, 120, 100);
    }

    @Override
    public Rectangle getMiss() { //Hit boxes
        if (type == NoteType.BIGDON || type == NoteType.BIGKAT) {
            return new Rectangle(x - 47, y, 194, 100);
        }
        return new Rectangle(x - 22, y, 144, 100);
    }
}
