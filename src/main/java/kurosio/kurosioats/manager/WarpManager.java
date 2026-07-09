package kurosio.kurosioats.manager;

import kurosio.kurosioats.KurosioATS;
import kurosio.kurosioats.data.WarpData;
import kurosio.kurosioats.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class WarpManager {

    private final KurosioATS plugin;

    private File file;
    private FileConfiguration config;


    public WarpManager(KurosioATS plugin) {

        this.plugin = plugin;

        load();

    }


    private void load() {

        file = new File(
                plugin.getDataFolder(),
                "warps.yml"
        );


        if (!file.exists()) {

            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        config =
                YamlConfiguration.loadConfiguration(file);

    }


    public void save() {

        try {

            config.save(file);

        } catch (IOException e) {

            e.printStackTrace();

        }

    }


    // ワープ作成
    public boolean createWarp(
            String name,
            Location location
    ) {


        String path =
                "warps." + name;


        config.set(
                path + ".world",
                location.getWorld().getName()
        );

        config.set(
                path + ".x",
                location.getX()
        );

        config.set(
                path + ".y",
                location.getY()
        );

        config.set(
                path + ".z",
                location.getZ()
        );

        config.set(
                path + ".yaw",
                location.getYaw()
        );

        config.set(
                path + ".pitch",
                location.getPitch()
        );


        save();

        return true;

    }


    // ワープ取得
    public Location getWarp(String name) {


        String path =
                "warps." + name;


        if (!config.contains(path)) {
            return null;
        }


        World world =
                Bukkit.getWorld(
                        config.getString(
                                path + ".world"
                        )
                );


        if (world == null) {
            return null;
        }


        return new Location(
                world,
                config.getDouble(path + ".x"),
                config.getDouble(path + ".y"),
                config.getDouble(path + ".z"),
                (float) config.getDouble(path + ".yaw"),
                (float) config.getDouble(path + ".pitch")
        );

    }


    // warp設定作成
    public boolean setWarp(
            String name,
            String from,
            String to,
            String password
    ) {


        String path =
                "settings." + name;


        config.set(
                path + ".from",
                from
        );

        config.set(
                path + ".to",
                to
        );

        config.set(
                path + ".password",
                password
        );


        save();

        return true;

    }


    // 参加者ワープ
    public boolean warp(
            Player player,
            String password
    ) {

        if (!plugin.getFileManager()
                .getResult()
                .contains(
                        "result." + player.getName()
                )) {

            player.sendMessage(
                    ChatUtil.format(
                            "&c参加者のみ使用できます。"
                    )
            );

            return false;
        }


        if (config.getConfigurationSection("settings") == null) {

            player.sendMessage(
                    ChatUtil.format(
                            "&cワープ設定がありません。"
                    )
            );

            return false;
        }


        for (String key :
                config.getConfigurationSection("settings")
                        .getKeys(false)) {


            String path =
                    "settings." + key;


            String savedPassword =
                    config.getString(
                            path + ".password"
                    );


            if (!password.equals(savedPassword)) {
                continue;
            }


            String from =
                    config.getString(
                            path + ".from"
                    );


            Location fromLocation =
                    getWarp(from);


            if (fromLocation == null) {

                player.sendMessage(
                        ChatUtil.format(
                                "&c入口ワープが存在しません。"
                        )
                );

                return false;
            }


            // 入口判定
            if (!isNear(
                    player.getLocation(),
                    fromLocation
            )) {

                player.sendMessage(
                        ChatUtil.format(
                                "&cワープ地点に立って使用してください。"
                        )
                );

                return false;
            }


            String to =
                    config.getString(
                            path + ".to"
                    );


            Location toLocation =
                    getWarp(to);


            if (toLocation == null) {

                player.sendMessage(
                        ChatUtil.format(
                                "&cワープ先が存在しません。"
                        )
                );

                return false;
            }


            player.teleport(toLocation);

            return true;

        }


        player.sendMessage(
                ChatUtil.format(
                        "&cパスワードが違います。"
                )
        );


        return false;
    }

    private boolean isNear(
            Location a,
            Location b
    ) {

        if (!a.getWorld()
                .equals(b.getWorld())) {

            return false;
        }


        return a.distance(b) <= 2;

    }

    public void reload() {

        config =
                YamlConfiguration.loadConfiguration(
                        file
                );

    }
}