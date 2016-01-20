package osu;

import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
    /**
     * This class handles all of the mouse inputs
     */
public class MouseInput extends MouseAdapter {

    private final Menu menu;
    private final Pause pause;
    private final End end;
    private final Handler handler;
    /**
     * The constructor takes in all of the classes that require some form of mouse input
     * Most of the mouse input is needed for buttons
     * @param menu the buttons of the menu need mouse input
     * @param pause the buttons of the menu need mouse input
     * @param end the buttons of the menu need mouse input
     * @param handler the buttons of the menu need mouse input
     */
    public MouseInput(Menu menu, Pause pause, End end, Handler handler) {
        this.menu = menu;
        this.pause = pause;
        this.end = end;
        this.handler = handler;
    }

    /**
     * Scrolling the mousewheel should scroll through the list of songs in the menu
     * @param e The mouse wheel event
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.getWheelRotation() > 0) {
            menu.setReleased(0);
        } else {
            menu.setReleased(1);
        }
    }
    /**
     * Releasing the mouse click is the event that actually triggers the buttons
     * @param e The mouse release event
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        menu.clearPressed();
        if (menu.getState() == State.MENU) { //menu navigation
            if (new Rectangle(820, 100, 350, 40).contains(e.getPoint())) {
                menu.setReleased(1);
            } else if (new Rectangle(820, 540, 350, 40).contains(e.getPoint())) {
                menu.setReleased(0);
            } else if (new Rectangle(950, 600, 200, 75).contains(e.getPoint())) {
                menu.startGame();
            } else if (new Rectangle(700, 590, 125, 94).contains(e.getPoint())) {
                menu.setReleased(8);
                if (!menu.getMods()[0]) {
                    menu.setHD(true);
                } else {
                    menu.setHD(false);
                }
            } else if (new Rectangle(825, 590, 125, 94).contains(e.getPoint())) {
                menu.setReleased(9);
                if (!menu.getMods()[1]) {
                    menu.setFL(true);
                } else {
                    menu.setFL(false);
                }
            } else {
                for (int i = 0; i < menu.getSong().getNumOfDifficulties(); i++) {
                    if (new Rectangle(300, 200 + 100 * i, 125, 94).contains(e.getPoint())) {
                        menu.setDiff(i);
                        break;
                    }
                }
            }
        } else if (menu.getState() == State.PAUSE) { //pause navigation
            if (new Rectangle(550, 200, 125, 94).contains(e.getPoint())) {
                try {
                    menu.getSong().playSong();
                } catch (Exception exception) {
                }
                menu.setState(State.GAME);
            } else if (new Rectangle(550, 300, 125, 94).contains(e.getPoint())) {
                menu.setState(State.ENDSCREEN);
            } else if (new Rectangle(550, 400, 125, 94).contains(e.getPoint())) {
                handler.unpause();
                handler.unpause();
                handler.reset();
                menu.setState(State.MENU);
                menu.reset();
            }
        } else if (menu.getState() == State.ENDSCREEN) { //end screen navigation
            if (new Rectangle(1000, 550, 200, 100).contains(e.getPoint())) {
                handler.unpause();
                handler.unpause();
                handler.reset();
                menu.setState(State.MENU);
                menu.reset();
            }
        }
    }

    /**
     * Pressing down the mouse simply changes the button state to pressed.
     * It does not actually trigger the button
     * @param e The mouse pressed event
     */
    @Override
    public void mousePressed(MouseEvent e) {
        menu.clearPressed();
        pause.clearPressed();
        end.clearPressed();
        if (menu.getState() == State.MENU) {
            if (new Rectangle(820, 100, 350, 40).contains(e.getPoint())) {
                menu.setPressed(0);
            } else if (new Rectangle(820, 540, 350, 40).contains(e.getPoint())) {
                menu.setPressed(1);
            } else if (new Rectangle(950, 600, 200, 75).contains(e.getPoint())) {
                menu.setPressed(10);
            } else if (new Rectangle(700, 600, 125, 94).contains(e.getPoint())) {
                menu.setPressed(8);
            } else if (new Rectangle(825, 600, 125, 94).contains(e.getPoint())) {
                menu.setPressed(9);
            } else {
                for (int i = 0; i < menu.getSong().getNumOfDifficulties(); i++) {
                    if (new Rectangle(300, 200 + 100 * i, 125, 94).contains(e.getPoint())) {
                        menu.setPressed(i + 4);
                        break;
                    }
                }
            }
        } else if (menu.getState() == State.PAUSE) {
            if (new Rectangle(550, 200, 125, 94).contains(e.getPoint())) {
                pause.setPress(0);
            } else if (new Rectangle(550, 300, 125, 94).contains(e.getPoint())) {
                pause.setPress(1);
            } else if (new Rectangle(550, 400, 125, 94).contains(e.getPoint())) {
                pause.setPress(2);
            }
        } else if (menu.getState() == State.ENDSCREEN) {
            if (new Rectangle(1000, 550, 200, 100).contains(e.getPoint())) {
                end.setPress();
            }
        }
    }

    /**
     * Checks the location of the cursor in order to check for hovers.
     * This changes the button states to hover. 
     * @param e The mouse moved event
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        menu.clearHover();
        pause.clearHover();
        end.clearHover();
        if (menu.getState() == State.MENU) {
            if (new Rectangle(820, 100, 350, 40).contains(e.getPoint())) {
                menu.setHover(0);
            } else if (new Rectangle(820, 540, 350, 40).contains(e.getPoint())) {
                menu.setHover(1);
            } else if (new Rectangle(950, 600, 200, 75).contains(e.getPoint())) {
                menu.setHover(10);
            } else if (new Rectangle(700, 600, 125, 94).contains(e.getPoint())) {
                menu.setHover(8);
            } else if (new Rectangle(825, 600, 125, 94).contains(e.getPoint())) {
                menu.setHover(9);
            } else {
                for (int i = 0; i < menu.getSong().getNumOfDifficulties(); i++) {
                    if (new Rectangle(300, 200 + 100 * i, 125, 94).contains(e.getPoint())) {
                        menu.setHover(i + 2);
                        break;
                    }
                }
            }
        } else if (menu.getState() == State.PAUSE) {
            if (new Rectangle(550, 200, 125, 94).contains(e.getPoint())) {
                pause.setHover(0);
            } else if (new Rectangle(550, 300, 125, 94).contains(e.getPoint())) {
                pause.setHover(1);
            } else if (new Rectangle(550, 400, 125, 94).contains(e.getPoint())) {
                pause.setHover(2);
            }
        } else if (menu.getState() == State.ENDSCREEN) {
            if (new Rectangle(1000, 550, 200, 100).contains(e.getPoint())) {
                end.setHover();
            }
        }
    }
}
