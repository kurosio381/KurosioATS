package kurosio.kurosioats.util;

import kurosio.kurosioats.KurosioATS;
import org.bukkit.command.CommandSender;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

public class TextFileUtil {

    // .txtの内容を送信
    public static void send(CommandSender sender, String fileName) {

        File file = new File(KurosioATS.getInstance().getDataFolder(), fileName);

        if (!file.exists()) {
            sender.sendMessage(ChatUtil.format("&c" + fileName + " が見つかりません。"));
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {

                sender.sendMessage(ChatUtil.color(line));

            }

        } catch (IOException e) {

            sender.sendMessage(ChatUtil.format("&c" + fileName + " の読み込みに失敗しました。"));
            e.printStackTrace();

        }

    }

    public static void sendWithoutPrefix(CommandSender sender, String fileName) {

        File file = new File(
                KurosioATS.getInstance().getDataFolder(),
                fileName
        );

        if (!file.exists()) {
            return;
        }


        try {

            List<String> lines =
                    Files.readAllLines(
                            file.toPath(),
                            StandardCharsets.UTF_8
                    );


            for (String line : lines) {

                sender.sendMessage(
                        ChatUtil.color(line)
                );

            }


        } catch (IOException e) {

            e.printStackTrace();

        }

    }

}