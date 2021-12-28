package codes.laivy.lvarmazem;

import codes.laivy.lvarmazem.commands.ArmazemCommand;
import codes.laivy.lvarmazem.data.Armazem;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class LvArmazem extends JavaPlugin {

    private Economy econ = null;

    public static LvArmazem INSTANCE;
    public static File FILE;
    public static YamlConfiguration Y;

    public static double CACTUS_PRICE;
    public static double WHEAT_SEED_PRICE;
    public static double WHEAT_PRICE;
    public static double SUGAR_CANE_PRICE;
    public static double MELON_PRICE;
    public static double PUMPKIN_PRICE;
    public static double NETHER_WART_PRICE;
    public static double CARROT_PRICE;
    public static double POTATO_PRICE;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void onEnable() {
        if (Bukkit.getPluginManager().getPlugin("PlotSquared") == null) {
            broadcastColoredMessage("&cNão foi possível inicializar o plugin pois o plugin &4PlotSquared &cnão foi encontrado.");
            setEnabled(false);
            return;
        }

        setupEconomy();
        saveDefaultConfig();
        INSTANCE = this;

        // PRICES (NEEDS VAULT)
        CACTUS_PRICE = getConfig().getDouble("cactus price per unity");
        WHEAT_SEED_PRICE = getConfig().getDouble("wheat seed price per unity");
        WHEAT_PRICE = getConfig().getDouble("wheat price per unity");
        SUGAR_CANE_PRICE = getConfig().getDouble("sugar cane price per unity");
        MELON_PRICE = getConfig().getDouble("melon price per unity");
        PUMPKIN_PRICE = getConfig().getDouble("pumpkin price per unity");
        NETHER_WART_PRICE = getConfig().getDouble("nether wart price per unity");
        CARROT_PRICE = getConfig().getDouble("carrot price per unity");
        POTATO_PRICE = getConfig().getDouble("potato price per unity");
        // PRICES (NEEDS VAULT)

        FILE = new File(getDataFolder(), "storage.data");
        FILE.getParentFile().mkdirs();
        Y = YamlConfiguration.loadConfiguration(FILE);

        getCommand("armazem").setExecutor(new ArmazemCommand());
        Bukkit.getPluginManager().registerEvents(new ArmazemCommand(), this);

        Bukkit.getScheduler().runTaskTimer(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getOpenInventory().getTopInventory() != null) {
                    if (player.getOpenInventory().getTopInventory().getName().contains("Armazem ")) {
                        String id = player.getOpenInventory().getTopInventory().getName().replace("Armazem ", "");

                        if (Armazem.armazens.containsKey(id)) {
                            player.openInventory(ArmazemCommand.getArmazemInventory(Armazem.armazens.get(id)));
                        }
                    }
                }
            }
        }, 5, 5);
    }

    @Override
    public void onDisable() {
        for (Map.Entry<String, Armazem> map : Armazem.armazens.entrySet()) {
            map.getValue().save();
        }

        try {
            Y.save(FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Economy getEconomy() {
        return econ;
    }
    private void setupEconomy() {
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) {
            broadcastColoredMessage("&cNão foi possível linkar com o Vault pois o plugin não foi encontrado, algumas funcionalidades como lojas e upgrades não funcionarão.");
            return;
        }

        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            broadcastColoredMessage("&cNão foi possível linkar com o Vault pois não foi encontrado nenhum plugin de economia");
            return;
        }
        econ = rsp.getProvider();
    }

    public void broadcastColoredMessage(String message) {
        getServer().getConsoleSender().sendMessage("§8[§6" + getDescription().getName() + "§8]§7" + " " + ChatColor.translateAlternateColorCodes('&', message));
    }

}
