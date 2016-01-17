package osu;

import jaco.mp3.player.MP3Player;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class Osu extends Canvas implements Runnable {

    public static final int WIDTH = 1200;
    public static final int HEIGHT = 700;

    private Thread thread;
    private final Window window;
    private boolean running;
    private boolean songPlay = false;
    private final boolean loaded = false;
    private boolean scores = true;
    private final Image[] countImages = new Image[]{ImageIO.read(new File("Resources/General/count1.png")),
        ImageIO.read(new File("Resources/General/count2.png")),
        ImageIO.read(new File("Resources/General/count3.png")),
        ImageIO.read(new File("Resources/General/ready.png")),
        ImageIO.read(new File("Resources/General/go.png"))};
    private final Image[] gameBGImages = new Image[]{ImageIO.read(new File("Resources/Taiko/taikobg.png")),
        ImageIO.read(new File("Resources/Taiko/taikoblock.png")).getScaledInstance(166, 188, Image.SCALE_SMOOTH),
        ImageIO.read(new File("Resources/Mania/maniabg.png"))};
    private final Image[] modImages = new Image[]{ImageIO.read(new File("Resources/Taiko/taikohd.png")),
        ImageIO.read(new File("Resources/Taiko/taikofl.png")),
        ImageIO.read(new File("Resources/Mania/maniahd.png")),
        ImageIO.read(new File("Resources/Mania/maniafl.png"))};
    private final Handler handler;
    private final ArrayList<Song> songList;
    private Song song;
    private final MP3Player mainTheme;
    private final Status status;
    private final End end;
    private final Pause pause;
    private final Player player;
    private final Splash splasherino;
    private State state;
    private final Menu menu;
    private final Sprites sprites;
    private final KeyInput keyInput;
    private final MouseInput mouseInput;
    private ArrayList<Notes> notes;
    private final double ticksPerSec = 200.0;

    private int stupidSongDelay = 0;

    public Osu() throws Exception {
        File path = new File("Songs/");
        mainTheme = new MP3Player(new File("Resources/General/mainTheme.mp3"));
        state = State.SPLASH;
        songList = new ArrayList<>();
        for (File read : path.listFiles()) {
            songList.add(new Song(read.getPath()));
        }
        menu = new Menu(songList, state);
        sprites = new Sprites(menu);
        status = new Status(sprites);
        notes = new ArrayList<>();
        pause = new Pause();
        handler = new Handler(status);
        player = new Player(handler, status);
        keyInput = new KeyInput(player, menu, handler);
        end = new End(menu, status);
        mouseInput = new MouseInput(menu, pause, end, handler);
        splasherino = new Splash(menu, player, status, sprites);
        this.addMouseListener(mouseInput);
        this.addMouseMotionListener(mouseInput);
        this.addMouseWheelListener(mouseInput);
        this.addKeyListener(keyInput);

        window = new Window(WIDTH, HEIGHT, "J-OSU!", this);
    }

    public synchronized void start() {
        thread = new Thread(this);
        thread.start();
        running = true;
    }

    public synchronized void stop() {
        try {
            thread.join();
            running = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        long lastTime = System.nanoTime();
        double ns = 1000000000 / ticksPerSec;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;

        while (running) {

            state = menu.getState();

            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                tick();
                delta--;
            }
            if (running) {
                try {
                    render();
                } catch (IOException ex) {
                    Logger.getLogger(Osu.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            frames++;
            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                frames = 0;
            }
        }
    }

    public void tick() {
        //Manages game play
        if (state == State.GAME) {
            if (!mainTheme.isStopped()) {
                mainTheme.stop();
            }
            //initial loader
            if (!menu.isInitialized()) {
                song = menu.getSong();
                notes.clear();
                for (Notes note : song.getNotes(menu.getDifficulty())) {
                    if (song.getMode() == 3) {
                        if (note instanceof Slider) {
                            notes.add(new Slider(note.getX(), note.getY(), note.getTime(), ((Slider) note).getEndTime()));
                        } else {
                            notes.add(new Single(note.getX(), note.getY(), note.getTime()));
                        }
                    } else {
                        notes.add(new TaikoNote(note.getTime(), ((TaikoNote) note).getType()));
                    }
                }
                handler.reset();
                handler.add(notes);
                handler.setTicksPerSec(ticksPerSec);
                handler.setMode(song.getMode());
                status.setMode(song.getMode());
                menu.initialize();
                scores = true;
                songPlay = false;
            }

            //song delay
            if (!songPlay && stupidSongDelay >= ticksPerSec * 3.4) {
                try {
                    song.playSong();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                songPlay = true;
                stupidSongDelay = 0;
            } else if (!songPlay) {
                stupidSongDelay++;
            }

            handler.tick();
            player.setMode(song.getMode());
            player.tick();
            status.tick();
            sprites.tick();
            if (handler.isDone()) {
                menu.setState(State.ENDSCREEN);
            }
        } else if (state == State.MENU) { //manages menu 
            if (!mainTheme.isStopped()) {
                mainTheme.stop();
            }
            stupidSongDelay = 0;
            songPlay = false;
            menu.tick();
        } else if (state == State.ENDSCREEN) { //manages end screen
            if (mainTheme.isStopped()) {
                mainTheme.play();
            }
            if (scores) {
                end.writeToFile();
                song.updateScores(menu.getDifficulty());
                scores = false;
            }
        } else if (state == State.SPLASH) { //manages title screen
            splasherino.tick();
            if (mainTheme.isStopped()) {
                mainTheme.play();
            }
        }
    }

    //Draws backgrounds depending on the states
    public void render() throws IOException {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();
        if (state == State.GAME) {
            g.drawImage(song.getBG(), 0, 0, this);
            if (song.getMode() == 1) {
                g.drawImage(gameBGImages[0], 0, 0, this);
                g.drawImage(gameBGImages[1], 0, 195, this);
            } else if (song.getMode() == 3) {
                g.drawImage(gameBGImages[2], 0, 0, this);
            }
            handler.render(g);
            sprites.render(g);
            if (song.getMode() == 1) {
                g.drawImage(gameBGImages[1], 0, 195, this);
                if (menu.getMods()[0]) {
                    g.drawImage(modImages[0], 0, 0, this);
                }
                if (menu.getMods()[1]) {
                    g.drawImage(modImages[1], 0, 0, this);
                }
            } else if (song.getMode() == 3) {
                if (menu.getMods()[0]) {
                    g.drawImage(modImages[2], 0, 0, this);
                }
                if (menu.getMods()[1]) {
                    g.drawImage(modImages[3], 0, 0, this);
                }
            }
            player.render(g);
            status.render(g);
            if (stupidSongDelay != 0) {
                if (stupidSongDelay <= ticksPerSec * 1.2) {
                    g.drawImage(countImages[3], 150, 100, this);
                } else if (stupidSongDelay <= ticksPerSec * 1.6) {
                    g.drawImage(countImages[2], 0, 0, this);
                } else if (stupidSongDelay <= ticksPerSec * 2.0) {
                    g.drawImage(countImages[1], 810, 0, this);
                } else if (stupidSongDelay <= ticksPerSec * 2.4) {
                    g.drawImage(countImages[0], 250, 0, this);
                } else if (stupidSongDelay <= ticksPerSec * 2.8) {
                    g.drawImage(countImages[4], 150, 100, this);
                }
            }
        } else if (state == State.MENU) {
            menu.render(g);
        } else if (state == State.PAUSE) {
            pause.render(g);
        } else if (state == State.ENDSCREEN) {
            end.render(g);
        } else if (state == State.SPLASH) {
            splasherino.render(g);
        }
        g.dispose();
        bs.show();
    }

    public static void main(String[] args) throws Exception {
        Osu osu = new Osu();
    }
}
