package Controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;

import model.Player;

public class FileManager {

    private final File projectDir;
    private final File dataFolder;
    private final Gson gson;

    public FileManager() {
        projectDir = new File(System.getProperty("user.dir"));
        dataFolder = new File(projectDir + File.separator + "data");

        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        gson = new Gson();
    }

    public void savePlayers(ArrayList<Player> players) {
        File file = new File(dataFolder, "players.json");

        try (Writer writer = new FileWriter(file)) {
            gson.toJson(players, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Player> loadPlayers() {
        ArrayList<Player> players = new ArrayList<>();
        File file = new File(dataFolder, "players.json");

        if (file.exists()) {
            try (Reader reader = new FileReader(file)) {
                Type type = new TypeToken<ArrayList<Player>>() {}.getType();
                players = gson.fromJson(reader, type);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return players;
    }
}
