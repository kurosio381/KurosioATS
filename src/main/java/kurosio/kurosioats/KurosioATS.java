package kurosio.kurosioats;

import kurosio.kurosioats.command.ATSCommand;
import kurosio.kurosioats.command.ATSTabCompleter;
import kurosio.kurosioats.manager.FileManager;
import kurosio.kurosioats.manager.LotteryManager;
import kurosio.kurosioats.manager.SpreadsheetManager;
import kurosio.kurosioats.manager.WarpManager;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class KurosioATS extends JavaPlugin {

    private static KurosioATS instance;
    private FileManager fileManager;
    private SpreadsheetManager spreadsheetManager;
    private LotteryManager lotteryManager;
    private WarpManager warpManager;

    @Override
    public void onEnable() {

        instance = this;

        saveDefaultConfig();

        fileManager = new FileManager(this);
        spreadsheetManager = new SpreadsheetManager(this);
        lotteryManager = new LotteryManager(this);
        fileManager.createFiles();
        warpManager = new WarpManager(this);

        ATSCommand command = new ATSCommand(this);

        getCommand("ats").setExecutor(command);
        getCommand("ats").setTabCompleter(new ATSTabCompleter());

        getLogger().info("=================================");
        getLogger().info("KurosioATS Enabled");
        getLogger().info("Version : " + getDescription().getVersion());
        getLogger().info("=================================");
    }

    @Override
    public void onDisable() {
        getLogger().info("KurosioATS Disabled");
    }

    public static KurosioATS getInstance() {
        return instance;
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public SpreadsheetManager getSpreadsheetManager() {
        return spreadsheetManager;
    }

    public LotteryManager getLotteryManager() {
        return lotteryManager;
    }

    public WarpManager getWarpManager() {

        return warpManager;

    }

    public void reloadAll() {

        reloadConfig();

        fileManager.reload();

        warpManager.reload();

    }
}