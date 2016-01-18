package osu;

import jaco.mp3.player.MP3Player;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * The song objects used in the game.
 * They contain the actual song mp3, the difficulties, beatmaps, and all data about the song
 * @author Jim Zhu
 * @author Yanjun Zhang
 */
public class Song {

    private String name;
    private String artist;
    private MP3Player song;
    private Image bg;
    private Image icon;
    final private String path;
    private int MODE;
    private int numOfDiff;
    private ArrayList<String> diffNames;
    private ArrayList<ArrayList<String>> highScores;
    final private ArrayList<ArrayList<Notes>> list;

    public Song(String path) throws Exception {
        this.path = path;
        diffNames = new ArrayList<>();
        list = new ArrayList<>();
        highScores = new ArrayList<>();

        //bg
        if (findFileName(path, "jpg").get(0) != null) {
            this.bg = ImageIO.read(new File(path + findFileName(path, "jpg").get(0))).getScaledInstance(1200, 700, Image.SCALE_SMOOTH);
            this.icon = ImageIO.read(new File(path + findFileName(path, "jpg").get(0))).getScaledInstance(120, 70, Image.SCALE_SMOOTH);
        } else if (findFileName(path, "png").get(0) != null) {
            this.bg = ImageIO.read(new File(path + findFileName(path, "png").get(0))).getScaledInstance(1200, 700, Image.SCALE_SMOOTH);
            this.icon = ImageIO.read(new File(path + findFileName(path, "png").get(0))).getScaledInstance(120, 70, Image.SCALE_SMOOTH);
        } else {
            this.bg = null;
        }

        ArrayList<String> osuFiles = findFileName(path, "osu");
        numOfDiff = osuFiles.size();
        BufferedReader reader;
        int diff = 0;
        for (String temp : osuFiles) {
            //highscores
            File scoreFile = new File(path + "/highscores" + diff + ".txt");
            highScores.add(new ArrayList<String>());
            if (!scoreFile.exists()) {
                scoreFile.createNewFile();
            } else {
                Scanner scanner = new Scanner(scoreFile);
                String scoreLine;
                while (scanner.hasNext()) {
                    scoreLine = scanner.nextLine();
                    highScores.get(diff).add(scoreLine);
                }
                scanner.close();
                ArrayList<Integer> tempList = new ArrayList<>();
                for (String s : highScores.get(diff)) {
                    tempList.add(Integer.parseInt(s));
                }
                Collections.sort(tempList);
                Collections.reverse(tempList);
                highScores.get(diff).clear();
                for (Integer i : tempList) {
                    highScores.get(diff).add(i.toString());
                }
            }

            reader = new BufferedReader(new FileReader(path + temp));
            String line = "";

            //General Song Data
            while (!line.contains("AudioFilename:")) {
                line = reader.readLine();
            }

            this.song = new MP3Player(new File(path + "/" + line.split(":")[1].trim()));

            while (!line.contains("Mode:")) {
                line = reader.readLine();
            }
            this.MODE = Integer.parseInt(line.split(":")[1].trim());

            while (!line.contains("Title:")) {
                line = reader.readLine();
            }
            this.name = line.split(":")[1].trim();
            if (name.length() >= 30) {
                name = name.substring(0, 25) + "...";
            }

            while (!line.contains("Artist:")) {
                line = reader.readLine();
            }
            this.artist = line.split(":")[1].trim();
            while (!line.contains("Version:")) {
                line = reader.readLine();
            }
            this.diffNames.add(line.split(":")[1].trim());
            if (diffNames.get(diff).length() >= 14) {
                diffNames.set(diff, diffNames.get(diff).substring(0, 14) + "...");
            }

            while (!reader.readLine().equals("[HitObjects]")) {
            }

            //Adding Notes
            list.add(new ArrayList<Notes>());
            if (MODE == 3) {
                while ((line = reader.readLine()) != null) {
                    if (line.split(",")[3].split(":")[0].equals("1")) {
                        list.get(diff).add(new Single((Integer.parseInt(line.split(",")[0]) == 192 ? 134 : (Integer.parseInt(line.split(",")[0])) == 320 ? 204 : (Integer.parseInt(line.split(",")[0]) == 448 ? 274 : 64)), 0, Integer.parseInt(line.split(",")[2])));
                    } else {
                        list.get(diff).add(new Slider((Integer.parseInt(line.split(",")[0]) == 192 ? 134 : (Integer.parseInt(line.split(",")[0])) == 320 ? 204 : (Integer.parseInt(line.split(",")[0]) == 448 ? 274 : 64)), 0, Integer.parseInt(line.split(",")[2]), Integer.parseInt(line.split(",")[5].split(":")[0])));
                    }
                }
            } else if (MODE == 1) {
                while ((line = reader.readLine()) != null) {
                    if (line.split(",")[3].split(":")[0].equals("5") || line.split(",")[3].split(":")[0].equals("1")) {
                        list.get(diff).add(new TaikoNote(Integer.parseInt(line.split(",")[2]) - 540, Integer.parseInt(line.split(",")[4])));
                    }
                }
            } else {
                System.out.println("This Game Mode Is Not Supported");
            }
            diff++;
        }
    }

    private ArrayList<String> findFileName(String path, String format) {
        try {
            File directory = new File(path);
            ArrayList<String> temp = new ArrayList<>();
            int count = 0;
            for (File file : directory.listFiles()) {
                if (file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length()).equalsIgnoreCase(format)) {
                    temp.add("/" + file.getName());
                    count++;
                }
            }
            if (temp.isEmpty()) {
                temp.add(null);
            }
            return temp;
        } catch (Exception e) {
        }
        return null;
    }

    public void updateScores(int diff) {
        highScores.get(diff).clear();
        File scoreFile = new File(path + "/highscores" + diff + ".txt");
        try {
            Scanner scanner = new Scanner(scoreFile);
            String scoreLine;
            while (scanner.hasNext()) {
                scoreLine = scanner.nextLine();
                highScores.get(diff).add(scoreLine);
            }
            scanner.close();
            ArrayList<Integer> tempList = new ArrayList<>();
            for (String s : highScores.get(diff)) {
                if (!"0".equals(s)) {
                    tempList.add(Integer.parseInt(s));
                }
            }
            Collections.sort(tempList);
            Collections.reverse(tempList);
            highScores.get(diff).clear();
            for (Integer i : tempList) {
                highScores.get(diff).add(i.toString());
            }
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(scoreFile, false)));
            for (String s : highScores.get(diff)) {
                writer.println(s);
            }
            writer.close();
        } catch (Exception ex) {
            Logger.getLogger(Song.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<String> getScores(int diff) {
        return highScores.get(diff);
    }

    public Image getBG() {
        return bg;
    }

    public Image getIcon() {
        return icon;
    }

    public void playSong() {
        song.play();
    }

    public void stopSong() {
        song.stop();
    }

    public void pauseSong() {
        song.pause();
    }

    public boolean isPaused() {
        return song.isPaused();
    }

    public ArrayList<Notes> getNotes(int difficulty) {
        return list.get(difficulty);
    }

    public String getDifficultyName(int index) {
        return diffNames.get(index);
    }

    public int getHitObjects(int index) {
        return list.get(index).size();
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

    public String getPath() {
        return path;
    }

    public int getMode() {
        return MODE;
    }

    public int getNumOfDifficulties() {
        return numOfDiff;
    }

}
