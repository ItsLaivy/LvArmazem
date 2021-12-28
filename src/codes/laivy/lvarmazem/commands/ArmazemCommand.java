package codes.laivy.lvarmazem.commands;

import codes.laivy.lvarmazem.LvArmazem;
import codes.laivy.lvarmazem.data.Armazem;
import codes.laivy.lvarmazem.utils.GuiUtils;
import com.intellectualcrafters.plot.api.PlotAPI;
import com.intellectualcrafters.plot.object.Plot;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Random;

import static codes.laivy.lvarmazem.LvArmazem.INSTANCE;

@SuppressWarnings("unused")
public class ArmazemCommand implements CommandExecutor, Listener {

    private static final GuiUtils g = new GuiUtils();
    private static final PlotAPI api = new PlotAPI();
    private static final Random r = new Random();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (cmd.getName().equalsIgnoreCase("armazem")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                if (api.getPlot(player) != null) {
                    Plot plot = api.getPlot(player);

                    if (plot.hasOwner()) {
                        Armazem a;

                        if (Armazem.armazens.containsKey(plot.getId().toString())) {
                            a = Armazem.armazens.get(plot.getId().toString());
                        } else a = new Armazem(plot.getId().toString());

                        if (a.CACTUS == 0 &&
                                a.WHEAT_SEEDS == 0 &&
                                a.WHEAT == 0 &&
                                a.SUGAR_CANE == 0 &&
                                a.MELON == 0 &&
                                a.PUMPKIN == 0 &&
                                a.NETHER_WART == 0 &&
                                a.CARROT == 0 &&
                                a.POTATO == 0
                        ) {
                            player.sendMessage("§cO armazém desse terreno está vazio.");
                            return true;
                        }

                        player.openInventory(getArmazemInventory(a));
                    } else player.sendMessage("§cEsse terreno não possui dono.");
                } else player.sendMessage("§cVocê precisa estar em um terreno para fazer isso.");
            } else sender.sendMessage("§cSomente players podem executar esse comando.");
        }
        return true;
    }

    @EventHandler
    private void cactusGrow(BlockPhysicsEvent e) {
        if (e.getBlock().getType() == Material.CACTUS) {
            Plot plot = api.getPlot(e.getBlock().getLocation());

            if (plot != null) {
                if (!Armazem.armazens.containsKey(plot.getId().toString())) {
                    new Armazem(plot.getId().toString());
                }

                Armazem.armazens.get(plot.getId().toString()).CACTUS++;
                e.getBlock().setType(Material.AIR);
            }
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    private void blockBreak(BlockBreakEvent e) {
        Material t = e.getBlock().getType();
        if (t == Material.CROPS || t == Material.CARROT || t == Material.POTATO) {
            if (e.getBlock().getState().getData().getData() == 7) {
                Plot plot = api.getPlot(e.getBlock().getLocation());

                Bukkit.broadcastMessage(e.getBlock().getType().name());

                if (plot != null) {
                    if (!Armazem.armazens.containsKey(plot.getId().toString())) {
                        new Armazem(plot.getId().toString());
                    }

                    Armazem a = Armazem.armazens.get(plot.getId().toString());

                    if (t == Material.POTATO) {
                        for (ItemStack i : e.getBlock().getDrops()) {
                            a.POTATO += i.getAmount() + r.nextInt(4);
                        }
                    } else if (t == Material.CARROT) {
                        for (ItemStack i : e.getBlock().getDrops()) {
                            a.CARROT += i.getAmount() + r.nextInt(4);
                        }
                    } else for (ItemStack i : e.getBlock().getDrops()) {
                        if (i.getType() == Material.WHEAT) {
                            a.WHEAT += i.getAmount() + r.nextInt(4);
                        } else if (i.getType() == Material.SEEDS) {
                            a.WHEAT_SEEDS += i.getAmount() + r.nextInt(4);
                        }
                    }

                    e.setCancelled(true);
                    e.getBlock().setType(Material.AIR);
                }

            }
        } else if (t == Material.MELON_BLOCK || t == Material.PUMPKIN) {
            Plot plot = api.getPlot(e.getBlock().getLocation());

            if (plot != null) {
                if (!Armazem.armazens.containsKey(plot.getId().toString())) {
                    new Armazem(plot.getId().toString());
                }

                Armazem a = Armazem.armazens.get(plot.getId().toString());

                if (t == Material.MELON_BLOCK) {
                    for (ItemStack i : e.getBlock().getDrops()) {
                        if (i.getType() == Material.MELON) {
                            a.MELON += i.getAmount();
                        }
                    }
                } else for (ItemStack i : e.getBlock().getDrops()) {
                    a.PUMPKIN += i.getAmount();
                }

                e.setCancelled(true);
                e.getBlock().setType(Material.AIR);
            }
        }
    }

    @EventHandler
    private void clickEvent(InventoryClickEvent e) {
        if (g.checkName(e, e.getInventory(), e.getClickedInventory(), true, "Armazem ")) {
            String id = e.getClickedInventory().getName().replace("Armazem ", "");
            Armazem a = Armazem.armazens.get(id);

            if (e.getSlot() == 10) {
                if (a.CACTUS > 0) {
                    Map<Integer, ItemStack> map = e.getWhoClicked().getInventory().addItem(new ItemStack(Material.CACTUS, a.CACTUS));
                    int t = a.CACTUS;
                    a.CACTUS = 0;
                    for (Map.Entry<Integer, ItemStack> m : map.entrySet()) a.CACTUS = m.getValue().getAmount();
                }
            } else if (e.getSlot() == 11) {
                if (a.WHEAT_SEEDS > 0) {
                    Map<Integer, ItemStack> map = e.getWhoClicked().getInventory().addItem(new ItemStack(Material.SEEDS, a.WHEAT_SEEDS));
                    int t = a.WHEAT_SEEDS;
                    a.WHEAT_SEEDS = 0;
                    for (Map.Entry<Integer, ItemStack> m : map.entrySet()) a.WHEAT_SEEDS = m.getValue().getAmount();
                }
            } else if (e.getSlot() == 12) {
                if (a.WHEAT > 0) {
                    Map<Integer, ItemStack> map = e.getWhoClicked().getInventory().addItem(new ItemStack(Material.WHEAT, a.WHEAT));
                    int t = a.WHEAT;
                    a.WHEAT = 0;
                    for (Map.Entry<Integer, ItemStack> m : map.entrySet()) a.WHEAT = m.getValue().getAmount();
                }
            } else if (e.getSlot() == 13) {
                if (a.SUGAR_CANE > 0) {
                    Map<Integer, ItemStack> map = e.getWhoClicked().getInventory().addItem(new ItemStack(Material.SUGAR_CANE, a.SUGAR_CANE));
                    int t = a.SUGAR_CANE;
                    a.SUGAR_CANE = 0;
                    for (Map.Entry<Integer, ItemStack> m : map.entrySet()) a.SUGAR_CANE = m.getValue().getAmount();
                }
            } else if (e.getSlot() == 14) {
                if (a.MELON > 0) {
                    Map<Integer, ItemStack> map = e.getWhoClicked().getInventory().addItem(new ItemStack(Material.MELON, a.MELON));
                    int t = a.MELON;
                    a.MELON = 0;
                    for (Map.Entry<Integer, ItemStack> m : map.entrySet()) a.MELON = m.getValue().getAmount();
                }
            } else if (e.getSlot() == 15) {
                if (a.PUMPKIN > 0) {
                    Map<Integer, ItemStack> map = e.getWhoClicked().getInventory().addItem(new ItemStack(Material.PUMPKIN, a.PUMPKIN));
                    int t = a.PUMPKIN;
                    a.PUMPKIN = 0;
                    for (Map.Entry<Integer, ItemStack> m : map.entrySet()) a.PUMPKIN = m.getValue().getAmount();
                }
            } else if (e.getSlot() == 16) {
                if (a.CARROT > 0) {
                    Map<Integer, ItemStack> map = e.getWhoClicked().getInventory().addItem(new ItemStack(Material.CARROT, a.CARROT));
                    int t = a.CARROT;
                    a.CARROT = 0;
                    for (Map.Entry<Integer, ItemStack> m : map.entrySet()) a.CARROT = m.getValue().getAmount();
                }
            } else if (e.getSlot() == 22) {
                if (a.POTATO > 0) {
                    Map<Integer, ItemStack> map = e.getWhoClicked().getInventory().addItem(new ItemStack(Material.POTATO, a.POTATO));
                    int t = a.POTATO;
                    a.POTATO = 0;
                    for (Map.Entry<Integer, ItemStack> m : map.entrySet()) a.POTATO = m.getValue().getAmount();
                }
            } else if (e.getSlot() == 44) {
                if (!e.getWhoClicked().hasPermission("lvarmazem.sell.all")) {
                    e.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', INSTANCE.getConfig().getString("cant sell all")));
                    return;
                }

                if (INSTANCE.getEconomy() == null) {
                    return;
                }

                double price = 0;
                price += (a.CACTUS * LvArmazem.CACTUS_PRICE);
                price += (a.WHEAT_SEEDS * LvArmazem.WHEAT_SEED_PRICE);
                price += (a.WHEAT * LvArmazem.WHEAT_PRICE);
                price += (a.SUGAR_CANE * LvArmazem.SUGAR_CANE_PRICE);
                price += (a.MELON * LvArmazem.MELON_PRICE);
                price += (a.PUMPKIN * LvArmazem.PUMPKIN_PRICE);
                price += (a.CARROT * LvArmazem.CARROT_PRICE);
                price += (a.POTATO * LvArmazem.POTATO_PRICE);

                a.CACTUS = 0;
                a.WHEAT_SEEDS = 0;
                a.WHEAT = 0;
                a.SUGAR_CANE = 0;
                a.MELON = 0;
                a.PUMPKIN = 0;
                a.CARROT = 0;
                a.POTATO = 0;

                if (price > 0) {
                    e.getWhoClicked().sendMessage("§7Você vendeu todos os recursos de seu armazém e recebeu §fR$" + price);
                    INSTANCE.getEconomy().depositPlayer((OfflinePlayer) e.getWhoClicked(), price);
                }
            }

            e.getWhoClicked().openInventory(getArmazemInventory(a));
        }
    }

    public static Inventory getArmazemInventory(Armazem a) {
        Inventory inventory = Bukkit.createInventory(null, 45, "Armazem " + a.getId());

        String[] split = {
                "&2Cacto;&2cacto;CACTUS;" + a.CACTUS,
                "&aSementes;&asementes;SEEDS;" + a.WHEAT_SEEDS,
                "&6Trigo;&6trigo;WHEAT;" + a.WHEAT,
                "&aCana de açúcar;&acana de açúcar;SUGAR_CANE;" + a.SUGAR_CANE,
                "&2Melancia;&2melancia;MELON;" + a.MELON,
                "&6Abóbora;&6abóbora;PUMPKIN;" + a.PUMPKIN,
                "&6Cenoura;&6cenoura;CARROT_ITEM;" + a.CARROT,
                "&eBatata;&ebatata;POTATO_ITEM;" + a.POTATO,
        };

        int row = 10;
        for (String e : split) {
            String[] s = e.split(";");
            Material material = Material.valueOf(s[2]);

            inventory.setItem(row, g.createItem(material, null, s[0],
                    "&7Clique para coletar seus drops",
                    "&7de " + s[1] + "&7.",
                    "",
                    "&7Armazenado: &f" + s[3] + " unidades"
            ));

            row++;
            if (row == 17) row = 22;
        }

        // MENU
        // MENU
        // MENU

        for (row = 36; row < 45; row++) {
            inventory.setItem(row, g.createItem(Material.STAINED_GLASS_PANE, 15, ""));
        }
        inventory.setItem(40, g.createItem(Material.PAPER, null, "&eCréditos do Plugin",
                "&7Esse plugin foi desenvolvido por",
                "&fDanielZinh#7616",
                "",
                "&7Versão: &f" + INSTANCE.getDescription().getVersion(),
                "&fwww.laivy.codes"
        ));

        if (INSTANCE.getEconomy() != null) {
            inventory.setItem(44, g.createItem(Material.STAINED_GLASS_PANE, 5, "&eVender Tudo", "&7Clique para vender todos os", "&7itens coletados pelo armazém"));
        }

        return inventory;
    }
}
