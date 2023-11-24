package Controller.external;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class FileManager {

    private final File projectDir;
    private final File dataFolder;

    public FileManager() {
        projectDir = new File(System.getProperty("user.dir"));
        dataFolder = new File(projectDir + File.separator + "data");

        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

    }

    /**
     * Saves the players' ranking to a text file.
     * 
     * @param ranking the list of players' names
     */
    public void savePlayers(ArrayList<String> ranking) {
        File txtFile = new File(dataFolder, "players.txt");

        try {
            PrintWriter writer = new PrintWriter(txtFile, "UTF-8");

            for (int i = 0; i < ranking.size(); i++) {
                writer.println(ranking.get(i));
            }

            writer.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * Loads the players' data from a text file and returns them as an ArrayList of strings.
     * 
     * @return The ArrayList of strings containing the players' data.
     */
    public ArrayList<String> loadPlayers() {
        ArrayList<String> playersRank = new ArrayList<>();

        try {
            File txtFile = new File(dataFolder, "players.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(txtFile), StandardCharsets.UTF_8));

            String line;
            while ((line = br.readLine()) != null) {
                playersRank.add(line);
            }

            br.close();
        } catch (IOException e) {
            System.out.println(e);
        }
        return playersRank;
    }
}
