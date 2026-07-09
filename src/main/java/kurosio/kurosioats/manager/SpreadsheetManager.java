package kurosio.kurosioats.manager;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import kurosio.kurosioats.KurosioATS;
import kurosio.kurosioats.data.ReceptionPlayer;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SpreadsheetManager {

    private final KurosioATS plugin;

    private Sheets sheets;


    public SpreadsheetManager(KurosioATS plugin) {
        this.plugin = plugin;
    }


    // Google Sheetsへ接続
    public boolean connect() {

        try {

            File credentialsFile = new File(
                    plugin.getDataFolder(),
                    plugin.getConfig().getString("spreadsheet.credentials")
            );


            GoogleCredentials credentials =
                    GoogleCredentials
                            .fromStream(new FileInputStream(credentialsFile))
                            .createScoped(Collections.singleton(
                                    "https://www.googleapis.com/auth/spreadsheets.readonly"
                            ));


            sheets = new Sheets.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance(),
                    new HttpCredentialsAdapter(credentials)
            )
                    .setApplicationName("KurosioATS")
                    .build();


            plugin.getLogger().info("Google Sheetsへ接続しました。");

            return true;


        } catch (Exception e) {

            plugin.getLogger().severe("Google Sheetsへ接続できませんでした。");
            e.printStackTrace();

            return false;

        }

    }


    public List<ReceptionPlayer> getReceptionPlayers() {

        List<ReceptionPlayer> players = new ArrayList<>();

        try {

            if (sheets == null && !connect()) {
                return players;
            }

            String spreadsheetId =
                    plugin.getConfig().getString("spreadsheet.id");

            String sheetName =
                    plugin.getConfig().getString("spreadsheet.sheet");

            ValueRange response =
                    sheets.spreadsheets()
                            .values()
                            .get(spreadsheetId, sheetName)
                            .execute();

            List<List<Object>> values =
                    response.getValues();

            if (values == null || values.size() <= 1) {
                return players;
            }

            int mcidColumn =
                    plugin.getConfig().getInt("column.mcid");

            int plotColumn =
                    plugin.getConfig().getInt("column.plot");

            for (int i = 1; i < values.size(); i++) {

                List<Object> row = values.get(i);

                if (row.size() <= Math.max(mcidColumn, plotColumn)) {
                    continue;
                }

                String mcid =
                        row.get(mcidColumn)
                                .toString()
                                .trim();

                if (mcid.isEmpty()) {
                    continue;
                }

                String plot =
                        normalizePlot(
                                row.get(plotColumn)
                                        .toString()
                        );

                players.add(
                        new ReceptionPlayer(
                                mcid,
                                plot
                        )
                );

            }

        } catch (Exception e) {

            plugin.getLogger()
                    .severe("受付情報取得に失敗しました。");

            e.printStackTrace();

        }


        return players;

    }

    private String normalizePlot(String text) {


        text = text.toUpperCase()
                .trim();

        if (text.startsWith("A")) {
            return "A";
        }

        if (text.startsWith("B")) {
            return "B";
        }

        if (text.startsWith("S")) {
            return "S";
        }

        return text;

    }

}