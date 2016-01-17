package osu;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter {

    private Player player;
    private Menu menu;
    private Handler handler;

    public KeyInput(Player player, Menu menu, Handler handler) {
        this.player = player;
        this.menu = menu;
        this.handler = handler;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_D) {
            player.hit(0);
        }
        if (key == KeyEvent.VK_F) {
            player.hit(1);
        }
        if (key == KeyEvent.VK_J) {
            player.hit(2);
        }
        if (key == KeyEvent.VK_K) {
            player.hit(3);
        }
        if (key == KeyEvent.VK_UP) {
            menu.setReleased(1);
        }
        if (key == KeyEvent.VK_DOWN) {
            menu.setReleased(0);
        }
        if (key == KeyEvent.VK_ENTER) {
            if (menu.getState() == State.MENU) {
                menu.startGame();
            }
        }
        if (key == KeyEvent.VK_SPACE) {
            if (menu.getState() == State.SPLASH) {
                menu.setState(State.MENU);
            }
        }

        if (key == KeyEvent.VK_ESCAPE) {
            if (menu.getState() == State.GAME) {
                menu.getSong().pauseSong();
                menu.setState(State.PAUSE);
                handler.pause();
            } else if (menu.getState() == State.PAUSE) {
                try {
                    menu.getSong().playSong();
                } catch (Exception exception) {
                }
                menu.setState(State.GAME);
                handler.unpause();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_D) {
            player.off(0);
        }
        if (key == KeyEvent.VK_F) {
            player.off(1);
        }
        if (key == KeyEvent.VK_J) {
            player.off(2);
        }
        if (key == KeyEvent.VK_K) {
            player.off(3);
        }
    }

}
