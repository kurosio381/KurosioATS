package kurosio.kurosioats.command;

import kurosio.kurosioats.KurosioATS;
import kurosio.kurosioats.data.ReceptionPlayer;
import kurosio.kurosioats.data.ResultPlayer;
import kurosio.kurosioats.manager.LotteryManager;
import kurosio.kurosioats.util.ChatUtil;
import kurosio.kurosioats.util.TextFileUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

public class ATSCommand implements CommandExecutor {

    private final KurosioATS plugin;

    public ATSCommand(KurosioATS plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            sendHelp(sender);
            return true;
        }

        // =========================
        // /ats main
        // =========================
        if (args[0].equalsIgnoreCase("main")) {


            TextFileUtil.send(sender, "main.txt");
            return true;
        }

        // =========================
        // /ats rule
        // =========================
        if (args[0].equalsIgnoreCase("rule")) {


            TextFileUtil.send(sender, "rule.txt");
            return true;
        }

        if (args[0].equalsIgnoreCase("check")) {

            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatUtil.format("&cプレイヤーのみ実行できます。"));
                return true;
            }

            Player player = (Player) sender;

            sendCheck(player);

            return true;
        }

        // =========================
// /ats warp
// =========================
        if (args[0].equalsIgnoreCase("warp")) {

            if (!(sender instanceof Player)) {

                sender.sendMessage(
                        ChatUtil.format("&cプレイヤーのみ実行できます。")
                );

                return true;
            }

            Player player = (Player) sender;

            // /ats warp create <name>
            if (args.length >= 2
                    && args[1].equalsIgnoreCase("create")) {

                if (!player.hasPermission("ats.admin")) {

                    player.sendMessage(
                            ChatUtil.format("&c権限がありません。")
                    );

                    return true;
                }

                if (args.length != 3) {

                    player.sendMessage(
                            ChatUtil.format(
                                    "&c使用方法: /ats warp create <name>"
                            )
                    );

                    return true;
                }

                plugin.getWarpManager()
                        .createWarp(
                                args[2],
                                player.getLocation()
                        );

                player.sendMessage(
                        ChatUtil.format(
                                "&aワープ地点を作成しました: &e"
                                        + args[2]
                        )
                );

                return true;

            }

            // /ats warp set <設定名> <name1> <name2> <password>
            if (args.length >= 2
                    && args[1].equalsIgnoreCase("set")) {


                if (!player.hasPermission("ats.admin")) {

                    player.sendMessage(
                            ChatUtil.format("&c権限がありません。")
                    );

                    return true;
                }

                if (args.length != 6) {

                    player.sendMessage(
                            ChatUtil.format(
                                    "&c使用方法: /ats warp set <設定名> <name1> <name2> <password>"
                            )
                    );

                    return true;
                }

                plugin.getWarpManager()
                        .setWarp(
                                args[2],
                                args[3],
                                args[4],
                                args[5]
                        );

                player.sendMessage(
                        ChatUtil.format(
                                "&aワープ設定を作成しました。"
                        )
                );

                return true;

            }

            // /ats warp <password>
            if (args.length == 2) {

                boolean success =
                        plugin.getWarpManager()
                                .warp(
                                        player,
                                        args[1]
                                );

                if (success) {

                    player.sendMessage(
                            ChatUtil.format(
                                    "&aワープしました。"
                            )
                    );

                }

                return true;

            }

            player.sendMessage(
                    ChatUtil.format(
                            "&c使用方法: /ats warp <password>"
                    )
            );
            return true;
        }

        // =========================
// /ats sync
// =========================
        if (args[0].equalsIgnoreCase("sync")) {

            if (!sender.hasPermission("ats.admin")) {

                sender.sendMessage(
                        ChatUtil.format("&c権限がありません。")
                );

                return true;
            }


            sender.sendMessage(
                    ChatUtil.format("&eスプレッドシート同期を開始します...")
            );


            plugin.getLogger().info(
                    "========== ATS Sync Start =========="
            );


            try {


                // =========================
                // Spreadsheet取得
                // =========================

                plugin.getLogger().info(
                        "[Sync] SpreadsheetManager取得開始"
                );


                List<ReceptionPlayer> players =
                        plugin.getSpreadsheetManager()
                                .getReceptionPlayers();


                if (players == null) {

                    plugin.getLogger().severe(
                            "[Sync] getReceptionPlayers() が null"
                    );


                    sender.sendMessage(
                            ChatUtil.format(
                                    "&cスプレッドシート取得結果がnullです。"
                            )
                    );

                    return true;
                }


                plugin.getLogger().info(
                        "[Sync] 取得人数: " + players.size()
                );

                if (players.isEmpty()) {

                    plugin.getLogger().warning(
                            "[Sync] 取得データが0件です。"
                    );

                    sender.sendMessage(
                            ChatUtil.format(
                                    "&c受付情報が取得できませんでした。"
                            )
                    );

                    return true;
                }

                // =========================
                // 取得内容確認
                // =========================

                plugin.getLogger().info(
                        "[Sync] 取得データ確認開始"
                );

                int count = 1;

                for (ReceptionPlayer player : players) {

                    plugin.getLogger().info(
                            "[Sync] #" + count
                                    + " MCID="
                                    + player.getMcid()
                                    + " Wish="
                                    + player.getWish()
                    );

                    count++;

                }

                // =========================
                // 保存
                // =========================

                plugin.getLogger().info(
                        "[Sync] 保存処理開始"
                );

                plugin.getFileManager()
                        .saveReception(players);

                plugin.getLogger().info(
                        "[Sync] 保存処理完了"
                );

                sender.sendMessage(
                        ChatUtil.format(
                                "&a受付情報を同期しました。 &7("
                                        + players.size()
                                        + "件)"
                        )
                );

            } catch (Exception e) {

                plugin.getLogger().severe(
                        "[Sync] 同期中に例外発生"
                );

                plugin.getLogger().severe(
                        e.getClass().getName()
                                + ": "
                                + e.getMessage()
                );

                e.printStackTrace();

                sender.sendMessage(
                        ChatUtil.format(
                                "&c同期中にエラーが発生しました。"
                        )
                );

            } finally {

                plugin.getLogger().info(
                        "========== ATS Sync End =========="
                );

            }

            return true;
        }

        if (args[0].equalsIgnoreCase("delete")) {

            if (!sender.hasPermission("ats.admin")) {
                sender.sendMessage(
                        ChatUtil.format("&c権限がありません。")
                );
                return true;
            }

            if (args.length != 2) {
                sender.sendMessage(
                        ChatUtil.format(
                                "&c使用方法: /ats delete <result|reception>"
                        )
                );
                return true;
            }


            if (args[1].equalsIgnoreCase("result")) {

                plugin.getFileManager()
                        .clearResult();

                sender.sendMessage(
                        ChatUtil.format(
                                "&aresult.ymlを削除しました。"
                        )
                );

                return true;

            }


            if (args[1].equalsIgnoreCase("reception")) {

                plugin.getFileManager()
                        .clearReception();

                sender.sendMessage(
                        ChatUtil.format(
                                "&areception.ymlを削除しました。"
                        )
                );

                return true;

            }


            sender.sendMessage(
                    ChatUtil.format(
                            "&c指定できるのは result / reception です。"
                    )
            );

            return true;
        }

        // =========================
// /ats reload
// =========================
        if (args[0].equalsIgnoreCase("reload")) {

            if (!sender.hasPermission("ats.admin")) {

                sender.sendMessage(
                        ChatUtil.format("&c権限がありません。")
                );

                return true;
            }


            plugin.reloadAll();


            sender.sendMessage(
                    ChatUtil.format(
                            "&aATSのデータを再読み込みしました。"
                    )
            );


            return true;
        }

        // =========================
// /ats lottery
// =========================
        if (args[0].equalsIgnoreCase("lottery")) {

            if (!sender.hasPermission("ats.admin")) {

                sender.sendMessage(
                        ChatUtil.format("&c権限がありません。")
                );

                return true;
            }


            sender.sendMessage(
                    ChatUtil.format("&e抽選処理を開始します...")
            );


            try {

                plugin.getLogger().info(
                        "========== ATS Lottery Start =========="
                );


                LotteryManager lotteryManager =
                        plugin.getLotteryManager();


                plugin.getLogger().info(
                        "[Lottery] 抽選実行"
                );


                List<ResultPlayer> results =
                        lotteryManager.startLottery();


                if (results == null || results.isEmpty()) {

                    plugin.getLogger().warning(
                            "[Lottery] 結果がありません。"
                    );


                    sender.sendMessage(
                            ChatUtil.format(
                                    "&c抽選結果を作成できませんでした。"
                            )
                    );

                    return true;
                }


                plugin.getLogger().info(
                        "[Lottery] 結果人数: "
                                + results.size()
                );


                plugin.getFileManager()
                        .saveResult(results);


                plugin.getLogger().info(
                        "[Lottery] 結果保存完了"
                );


                sender.sendMessage(
                        ChatUtil.format(
                                "&a抽選が完了しました。 &7("
                                        + results.size()
                                        + "件)"
                        )
                );


            } catch (Exception e) {

                plugin.getLogger().severe(
                        "[Lottery] 抽選中に例外発生"
                );

                e.printStackTrace();


                sender.sendMessage(
                        ChatUtil.format(
                                "&c抽選中にエラーが発生しました。"
                        )
                );


            } finally {

                plugin.getLogger().info(
                        "========== ATS Lottery End =========="
                );

            }


            return true;
        }

        if (args[0].equalsIgnoreCase("status")) {

            if (!sender.hasPermission("ats.admin")) {
                sender.sendMessage(ChatUtil.format("&c権限がありません。"));
                return true;
            }

            if (args.length != 2) {
                sender.sendMessage(ChatUtil.format("&c使用方法: /ats status <BEFORE/ACCEPTING/RESULT>"));
                return true;
            }

            String status = args[1].toUpperCase();

            if (!status.equals("BEFORE")
                    && !status.equals("ACCEPTING")
                    && !status.equals("RESULT")) {

                sender.sendMessage(ChatUtil.format("&c指定できる状態"));
                sender.sendMessage(ChatUtil.color("&7BEFORE"));
                sender.sendMessage(ChatUtil.color("&7ACCEPTING"));
                sender.sendMessage(ChatUtil.color("&7RESULT"));
                return true;
            }

            plugin.getConfig().set("status", status);
            plugin.saveConfig();

            sender.sendMessage(ChatUtil.format("&aイベント状態を &e" + status + " &aへ変更しました。"));
            return true;
        }

        sender.sendMessage(ChatUtil.format("&c不明なコマンドです。"));
        sender.sendMessage(ChatUtil.color("&7/ats help をご確認ください。"));
        return true;
    }



    private void sendHelp(CommandSender sender) {
        sender.sendMessage(
                ChatUtil.color(ChatUtil.PREFIX)
        );
        sender.sendMessage(ChatUtil.color("&6&lKurosioATS コマンド集"));
        sender.sendMessage(ChatUtil.color("&a/ats help &f- コマンド一覧"));
        sender.sendMessage(ChatUtil.color("&a/ats main &f- 開催概要"));
        sender.sendMessage(ChatUtil.color("&a/ats rule &f- ルール"));
        sender.sendMessage(ChatUtil.color("&a/ats check &f- 受付・抽選状況"));
        sender.sendMessage(ChatUtil.color("&a/ats warp <password> &f- 指定場所からTPします。"));
        if (sender.hasPermission("ats.admin")) {
            sender.sendMessage(ChatUtil.color("&c【運営専用】"));
            sender.sendMessage(ChatUtil.color("&a/ats sync &f- 受付情報同期"));
            sender.sendMessage(ChatUtil.color("&a/ats status <状態> &f- 受付状態変更"));
            sender.sendMessage(ChatUtil.color("&a/ats lottery &f- 抽選開始"));
            sender.sendMessage(ChatUtil.color("&a/ats warp create <name> &f-ワープ地点を設定"));
            sender.sendMessage(ChatUtil.color("&a/ats warp set <ワープ設定名> <name1> <name2> <password> &fワープを設定"));
        }

        sender.sendMessage(ChatUtil.color("&b&m----------------------------------------"));
    }


    private void sendCheck(Player player) {

        player.sendMessage(
                ChatUtil.color(ChatUtil.PREFIX)
        );


        player.sendMessage(
                ChatUtil.color(
                        "&f[&cAプロット&f] - &a"
                                + countPlot("A")
                                + "&f人 "
                                + getReceptionStatus("A")
                )
        );


        player.sendMessage(
                ChatUtil.color(
                        "&f[&bBプロット&f] - &a"
                                + countPlot("B")
                                + "&f人 "
                                + getReceptionStatus("B")
                )
        );


        player.sendMessage(
                ChatUtil.color(
                        "&f[&6Sプロット&f] - &a"
                                + countPlot("S")
                                + "&f人 "
                                + getReceptionStatus("S")
                )
        );


        player.sendMessage(
                ChatUtil.color(
                        "&fあなたのプロット位置&f： &6&l"
                                + getPlayerLocation(player)
                )
        );

        // 結果公開済み(result.yml)のプレイヤーのみメッセージ表示
        if (plugin.getFileManager()
                .getResult()
                .contains("result." + player.getName())) {

            TextFileUtil.sendWithoutPrefix(player, "message.txt");

        }

    }

    private String getReceptionStatus(String plot) {

        String status =
                plugin.getConfig()
                        .getString("status", "BEFORE");


        if ("ACCEPTING".equalsIgnoreCase(status)) {

            return "&f<&a受付中&f>";

        }

        return "&f<&c受付停止&f>";

    }

    private String getPlayerLocation(Player player) {

        String mcid = player.getName();

        String status =
                plugin.getConfig()
                        .getString("status", "BEFORE");


        // 抽選結果が存在する場合は結果を優先
        String resultLocation =
                plugin.getFileManager()
                        .getResult()
                        .getString(
                                "result." + mcid + ".location"
                        );


        if (resultLocation != null) {

            if ("NONE".equalsIgnoreCase(resultLocation)) {
                return "&f落選";
            }

            return resultLocation;

        }


        // 結果がない場合は受付希望表示
        String wish =
                plugin.getFileManager()
                        .getReception()
                        .getString(
                                "players." + mcid + ".wish"
                        );


        if (wish == null) {

            return "&c未受付";

        }


        return wish + "&7（抽選前）";

    }


    private int countPlot(String plot) {

        int count = 0;

        if (!plugin.getFileManager()
                .getReception()
                .contains("players")) {

            return 0;
        }


        for (String mcid :
                plugin.getFileManager()
                        .getReception()
                        .getConfigurationSection("players")
                        .getKeys(false)) {


            String wish =
                    plugin.getFileManager()
                            .getReception()
                            .getString(
                                    "players." + mcid + ".wish"
                            );


            if (plot.equalsIgnoreCase(wish)) {
                count++;
            }

        }


        return count;
    }

}