package kurosio.kurosioats.manager;

import kurosio.kurosioats.KurosioATS;
import kurosio.kurosioats.data.ReceptionPlayer;
import kurosio.kurosioats.data.ResultPlayer;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

public class LotteryManager {

    private final KurosioATS plugin;
    private final FileManager fileManager;

    public LotteryManager(KurosioATS plugin) {
        this.plugin = plugin;
        this.fileManager = plugin.getFileManager();
    }

    // 抽選開始
    public List<ResultPlayer> startLottery() {

        List<ReceptionPlayer> reception =
                fileManager.loadReception();

        List<ResultPlayer> results =
                loadExistingResults();

        Set<String> finishedPlayers = new HashSet<>();

        for (ResultPlayer result : results) {
            finishedPlayers.add(result.getMcid());
        }


        Map<String, List<ReceptionPlayer>> groups =
                new HashMap<>();


        // 未抽選者のみ分類
        for (ReceptionPlayer player : reception) {

            if (finishedPlayers.contains(player.getMcid())) {
                continue;
            }

            groups.computeIfAbsent(
                    player.getWish(),
                    k -> new ArrayList<>()
            ).add(player);

        }


        // プロットごとに抽選
        for (Map.Entry<String, List<ReceptionPlayer>> entry : groups.entrySet()) {

            String plot = entry.getKey();
            List<ReceptionPlayer> players = entry.getValue();


            // 抽選なし
            if (isExcluded(plot)) {

                for (ReceptionPlayer player : players) {

                    results.add(
                            new ResultPlayer(
                                    player.getMcid(),
                                    plot,
                                    plot,
                                    true
                            )
                    );

                }

                continue;
            }


            String maxText =
                    plugin.getConfig()
                            .getString("max." + plot);


            if (maxText == null) {
                continue;
            }


            // 無制限
            if (maxText.equalsIgnoreCase("none")) {

                for (ReceptionPlayer player : players) {

                    results.add(
                            new ResultPlayer(
                                    player.getMcid(),
                                    plot,
                                    plot,
                                    true
                            )
                    );

                }

                continue;
            }


            int max =
                    Integer.parseInt(maxText);


            // 使用済み番号取得
            Set<String> usedLocations =
                    getUsedLocations(plot, results);


            // 空き番号作成
            List<String> locations =
                    createAvailableLocations(
                            plot,
                            max,
                            usedLocations
                    );


            Collections.shuffle(players);
            Collections.shuffle(locations);


            int count =
                    Math.min(
                            players.size(),
                            locations.size()
                    );


            for (int i = 0; i < players.size(); i++) {

                ReceptionPlayer player =
                        players.get(i);


                if (i < count) {

                    results.add(
                            new ResultPlayer(
                                    player.getMcid(),
                                    plot,
                                    locations.get(i),
                                    true
                            )
                    );

                } else {

                    results.add(
                            new ResultPlayer(
                                    player.getMcid(),
                                    plot,
                                    "NONE",
                                    false
                            )
                    );

                }

            }

        }

        return results;
    }


    // 既存結果読み込み
    private List<ResultPlayer> loadExistingResults() {

        List<ResultPlayer> results =
                new ArrayList<>();

        ConfigurationSection section =
                fileManager.getResult()
                        .getConfigurationSection("result");


        if (section == null) {
            return results;
        }


        for (String mcid : section.getKeys(false)) {

            String wish =
                    section.getString(
                            mcid + ".wish",
                            ""
                    );

            String location =
                    section.getString(
                            mcid + ".location",
                            "NONE"
                    );


            results.add(
                    new ResultPlayer(
                            mcid,
                            wish,
                            location,
                            !location.equalsIgnoreCase("NONE")
                    )
            );

        }

        return results;
    }


    // 使用済みプロット取得
    private Set<String> getUsedLocations(
            String plot,
            List<ResultPlayer> results
    ) {

        Set<String> used =
                new HashSet<>();


        for (ResultPlayer result : results) {

            String location =
                    result.getLocation();


            if (location != null
                    && location.startsWith(plot + "-")) {

                used.add(location);

            }

        }

        return used;
    }


    // 空きプロット生成
    private List<String> createAvailableLocations(
            String plot,
            int max,
            Set<String> used
    ) {

        List<String> locations =
                new ArrayList<>();


        for (int i = 1; i <= max; i++) {

            String location =
                    plot + "-" + i;


            if (!used.contains(location)) {
                locations.add(location);
            }

        }

        return locations;
    }


    // 抽選除外チェック
    private boolean isExcluded(String plot) {

        List<String> exclude =
                plugin.getConfig()
                        .getStringList("lottery.exclude");


        for (String value : exclude) {

            if (value.equalsIgnoreCase(plot)) {
                return true;
            }

        }

        return false;
    }
}