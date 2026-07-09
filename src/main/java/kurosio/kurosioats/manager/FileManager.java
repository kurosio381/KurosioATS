package kurosio.kurosioats.manager;

import kurosio.kurosioats.KurosioATS;
import kurosio.kurosioats.data.ReceptionPlayer;
import kurosio.kurosioats.data.ResultPlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    private final KurosioATS plugin;

    private File receptionFile;
    private FileConfiguration receptionConfig;

    private File resultFile;
    private FileConfiguration resultConfig;


    public FileManager(KurosioATS plugin) {
        this.plugin = plugin;
    }


    public void createFiles() {

        createYaml("reception.yml");
        createYaml("result.yml");
        createText("warps.yml");

        createText("main.txt");
        createText("rule.txt");
        createText("message.txt");

        loadFiles();

    }


    private void loadFiles() {

        receptionFile =
                new File(plugin.getDataFolder(), "reception.yml");

        receptionConfig =
                YamlConfiguration.loadConfiguration(receptionFile);


        resultFile =
                new File(plugin.getDataFolder(), "result.yml");

        resultConfig =
                YamlConfiguration.loadConfiguration(resultFile);

    }


    // =========================
    // reception.yml
    // =========================

    public void saveReception(List<ReceptionPlayer> players) {

        if (plugin.getConfig().getBoolean("settings.clear-before-sync")) {
            receptionConfig.set("players", null);
        }


        for (ReceptionPlayer player : players) {

            receptionConfig.set(
                    "players." + player.getMcid() + ".wish",
                    player.getWish()
            );

        }

        saveReception();

    }


    public List<ReceptionPlayer> loadReception() {

        List<ReceptionPlayer> players = new ArrayList<>();


        if (!receptionConfig.contains("players")) {
            return players;
        }


        for (String mcid :
                receptionConfig.getConfigurationSection("players").getKeys(false)) {


            String wish =
                    receptionConfig.getString(
                            "players." + mcid + ".wish"
                    );


            players.add(
                    new ReceptionPlayer(
                            mcid,
                            wish
                    )
            );

        }


        return players;

    }


    public void saveReception() {

        try {

            receptionConfig.save(receptionFile);

        } catch (IOException e) {

            plugin.getLogger()
                    .severe("reception.yml保存失敗");

            e.printStackTrace();

        }

    }


    // =========================
    // result.yml
    // =========================

    public void saveResult(List<ResultPlayer> players) {

        if (plugin.getConfig().getBoolean("settings.clear-before-lottery")) {
            resultConfig.set("result", null);
        }


        for (ResultPlayer player : players) {

            String path =
                    "result." + player.getMcid();


            resultConfig.set(
                    path + ".wish",
                    player.getWish()
            );


            resultConfig.set(
                    path + ".location",
                    player.getLocation()
            );


            resultConfig.set(
                    path + ".win",
                    player.isWin()
            );

        }


        saveResult();

    }


    public List<ResultPlayer> loadResult() {

        List<ResultPlayer> players = new ArrayList<>();


        if (!resultConfig.contains("result")) {
            return players;
        }


        for (String mcid :
                resultConfig.getConfigurationSection("result").getKeys(false)) {


            String wish =
                    resultConfig.getString(
                            "result." + mcid + ".wish"
                    );


            String location =
                    resultConfig.getString(
                            "result." + mcid + ".location"
                    );


            boolean win =
                    resultConfig.getBoolean(
                            "result." + mcid + ".win"
                    );


            players.add(
                    new ResultPlayer(
                            mcid,
                            wish,
                            location,
                            win
                    )
            );

        }


        return players;

    }


    public void saveResult() {

        try {

            resultConfig.save(resultFile);

        } catch (IOException e) {

            plugin.getLogger()
                    .severe("result.yml保存失敗");

            e.printStackTrace();

        }

    }


    private void createYaml(String name) {

        File file =
                new File(plugin.getDataFolder(), name);


        if (!file.exists()) {

            try {

                file.createNewFile();

            } catch (IOException e) {

                e.printStackTrace();

            }

        }

    }


    private void createText(String name) {

        File file =
                new File(plugin.getDataFolder(), name);


        if (!file.exists()) {

            try {

                file.createNewFile();

            } catch (IOException e) {

                e.printStackTrace();

            }

        }

    }


    public FileConfiguration getReception() {
        return receptionConfig;
    }


    public FileConfiguration getResult() {
        return resultConfig;
    }

    // =========================
// result.yml 初期化
// =========================
    public void clearResult() {

        FileConfiguration config =
                YamlConfiguration.loadConfiguration(resultFile);

        config.set("result", null);

        try {

            config.save(resultFile);

        } catch (IOException e) {

            e.printStackTrace();

        }

    }


    // =========================
// reception.yml 初期化
// =========================
    public void clearReception() {

        FileConfiguration config =
                YamlConfiguration.loadConfiguration(receptionFile);

        config.set("players", null);

        try {

            config.save(receptionFile);

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    public void reload() {

        receptionConfig =
                YamlConfiguration.loadConfiguration(
                        receptionFile
                );


        resultConfig =
                YamlConfiguration.loadConfiguration(
                        resultFile
                );

    }
}