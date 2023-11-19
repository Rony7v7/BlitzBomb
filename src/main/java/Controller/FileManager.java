package Controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(players);
        File jsonFile = new File(dataFolder, "players.json");

        try {
            FileOutputStream fos = new FileOutputStream(jsonFile);
            byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
            fos.write(bytes);
            fos.close();

        } catch (IOException e) {
            System.out.println(e);
        }

    }


    public ArrayList<Player> loadPlayers() {

        File jsonFile = new File(dataFolder, "players.json");
        ArrayList<Player> auxPlayers = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(jsonFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();
            Player[] Players = gson.fromJson(br, Player[].class);
            Collections.addAll(auxPlayers, Players);
            br.close();
        } catch (IOException e) {
            System.out.println(e);
        }
        return auxPlayers;

    }
}
