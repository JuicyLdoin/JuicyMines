package net.juicy.mines.listener.listeners.mine.edit;

import net.juicy.api.utils.util.ItemUtil;
import net.juicy.api.utils.util.LocationUtil;
import net.juicy.mines.JuicyMinesPlugin;
import net.juicy.mines.mine.Mine;
import net.juicy.mines.mine.options.MineOptions;
import net.juicy.mines.mine.options.PatternOptions;
import net.juicy.mines.mine.pattern.MinePattern;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.time.format.DateTimeFormatter;
import java.util.*;

public class MineEditListener implements IMineEditor {

    public static final MineEditListener mineEditor = new MineEditListener();

    private final JuicyMinesPlugin plugin = JuicyMinesPlugin.getPlugin();

    private final Map<Player, Mine> mines = new HashMap<>();
    private final Map<Player, Inventory> mainInventory = new HashMap<>();

    public void createForPlayer(Player player, Mine mine) {

        Inventory inventory = getInventory(mine);

        player.openInventory(inventory);

        mainInventory.put(player, inventory);
        mines.put(player, mine);

    }

    @EventHandler
    public void onClickInMain(InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();

        if (mainInventory.containsKey(player))
            if (mainInventory.get(player).equals(event.getClickedInventory())) {

                ItemStack currentItem = event.getCurrentItem();

                if (currentItem != null) {

                    Mine mine = mines.get(player);

                    MineOptions mineOptions = mine.getMineOptions();
                    PatternOptions patternOptions = mineOptions.getPatternOptions();

                    boolean leftClick = event.isLeftClick();
                    boolean rightClick = event.isRightClick();

                    boolean shift = event.isShiftClick();

                    int slot = event.getSlot();

                    int intValue = leftClick ? (shift ? 10 : 1) : (rightClick ? (shift ? -10 : -1) : 0);

                    if (slot == 0) {

                        player.sendMessage(plugin.replace("%prefix% &fНапишите новое имя шахты в чат!"));
                        player.sendMessage(plugin.replace("%prefix% &fЧтобы отменить действие напишите &e\"Отмена\"&f!"));
                        player.closeInventory();

                        MineRenameListener.mineRename.createForPlayer(player, mine);

                        return;

                    }

                    if (slot == 7)  {

                        mineOptions.setResetTime(mineOptions.getResetTime() + intValue);

                        if (mineOptions.getResetTime() < 0)
                            mineOptions.setResetTime(0);

                        update(player);

                        return;

                    }

                    if (slot == 8)  {

                        mineOptions.setResetOn(mineOptions.getResetOn() + intValue);

                        if (mineOptions.getResetOn() < 0)
                            mineOptions.setResetOn(0);

                        update(player);

                        return;

                    }

                    if (slot == 18)  {

                        patternOptions.setAmount(patternOptions.getAmount() + intValue);

                        if (patternOptions.getAmount() < 1)
                            patternOptions.setAmount(1);

                        update(player);

                        return;

                    }

                    if (slot == 19)  {

                        patternOptions.setCycle(!patternOptions.isCycle());
                        update(player);

                        return;

                    }

                    if (slot == 22) {

                        MineBlocksListener.mineBlocks.createForPlayer(player, mine);
                        return;

                    }

                    if (slot == 25) {

                        mineOptions.setMinLocation(LocationUtil.setInt(player.getLocation()));
                        player.closeInventory();

                        player.sendMessage(plugin.replace("%prefix% &fВы установили минимальную локацию!"));

                        return;

                    }

                    if (slot == 26) {

                        mineOptions.setMaxLocation(LocationUtil.setInt(player.getLocation()));
                        player.closeInventory();

                        player.sendMessage(plugin.replace("%prefix% &fВы установили максимальую локацию!"));

                        return;

                    }

                    if (currentItem.getType().equals(Material.CLOCK))
                        if (slot >= 36 && slot <= 44) {

                            UUID patternUUID = UUID.fromString(ChatColor.stripColor(currentItem.getItemMeta().getDisplayName().split(" - ")[1]));
                            MinePattern minePattern = mine.getMineOptions().getPatternCache().getByUUID(patternUUID);

                            if (minePattern != null)
                                mine.fill(minePattern);

                            player.sendMessage(plugin.replace("%prefix% &fВы заполнили шахту с именем &e" + mine.getName() + " &fпаттерном &e" + patternUUID));
                            player.closeInventory();

                            return;

                        }


                    event.setCancelled(true);

                }
            }
    }

    public void update(Player player) {

        Inventory inventory = getInventory(mines.get(player));

        player.openInventory(inventory);
        mainInventory.put(player, inventory);

    }

    public Inventory getInventory(Mine mine) {

        MineOptions mineOptions = mine.getMineOptions();

        Inventory inventory = Bukkit.createInventory(null, 45, "§eНастройка шахты");

        for (int i = 0; i < inventory.getSize(); i++)
            inventory.setItem(i, ItemUtil.getItem(Material.GRAY_STAINED_GLASS_PANE, 1, ""));

        for (int i = 27; i < 36; i++)
            inventory.setItem(i, ItemUtil.getItem(Material.BLACK_STAINED_GLASS_PANE, 1, ""));

        int displayedPatters = 0;

        for (MinePattern minePattern : mineOptions.getPatternCache().getPatternQueue()) {

            inventory.setItem(36 + displayedPatters, ItemUtil.getItem(Material.CLOCK, 1, "§fПаттерн - §e" + minePattern.getUuid(),
                    "",
                    "§fДата генерации: §e" + minePattern.getGeneratedOn().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
                    "§fСгенерирован за: §e" + minePattern.getGeneratedPer() + "ms",
                    "",
                    "§fНажмите, чтобы заполнить шахту этим паттерном!",
                    ""));

            displayedPatters++;

        }

        inventory.setItem(0, ItemUtil.getItem(Material.NAME_TAG, 1, "§fНазвание: §e" + mine.getName(),
                "",
                "§fНажмите чтобы изменить имя!",
                ""));

        inventory.setItem(7, ItemUtil.getItem(Material.COAL, 1, "§fВремя заполнения: §e" + mineOptions.getResetTime() + "с",
                "",
                "§fНажмите §eЛКМ §fчтобы добавить (чтобы добавить §e10с §fзажмите §eSHIFT§f)",
                "§fНажмите §eПКМ §fчтобы убрать (чтобы убрать §e10с §fзажмите §eSHIFT§f)",
                "",
                "§f§lЕсли значение меньше или равно §e§l0с",
                "§f§lзаполнение по времени не будет работать!",
                ""));

        inventory.setItem(8, ItemUtil.getItem(Material.CHARCOAL, 1, "§fПроцент заполнения: §e" + mineOptions.getResetOn() + "%",
                "",
                "§fНажмите §eЛКМ §fчтобы добавить (чтобы добавить §e10% §fзажмите §eSHIFT§f)",
                "§fНажмите §eПКМ §fчтобы убрать (чтобы убрать §e10% §fзажмите §eSHIFT§f)",
                "",
                "§f§lЕсли значение меньше §e§l0%",
                "§f§lзаполнение по % не будет работать!",
                ""));

        inventory.setItem(18, ItemUtil.getItem(Material.DIAMOND, 1, "§fМаксимальное кол-во паттернов: §e" + mineOptions.getPatternOptions().getAmount(),
                "",
                "§fНажмите §eЛКМ §fчтобы добавить (чтобы добавить §e10 §fзажмите §eSHIFT§f)",
                "§fНажмите §eПКМ §fчтобы убрать (чтобы убрать §e10 §fзажмите §eSHIFT§f)",
                ""));

        inventory.setItem(19, ItemUtil.getItem(Material.LAPIS_LAZULI, 1, "§fЦикл паттернов: §e" + (mineOptions.getPatternOptions().isCycle() ? "§aВключен" : "§cВыключен"),
                "",
                "§fНажмите чтобы изменить на противоположенное значение!",
                ""));

        inventory.setItem(25, ItemUtil.getItem(Material.EMERALD, 1, "§fМинимальная локация: §e" + LocationUtil.getLocation(mineOptions.getMinLocation()),
                "",
                "§fНажмите чтобы изменить!",
                ""));

        inventory.setItem(26, ItemUtil.getItem(Material.EMERALD, 1, "§fМаксимальная локация: §e" + LocationUtil.getLocation(mineOptions.getMaxLocation()),
                "",
                "§fНажмите чтобы изменить!",
                ""));

        List<String> blocksLore = new ArrayList<>();

        blocksLore.add("");
        blocksLore.add("§fСписок блоков:");

        for (Map.Entry<Material, Float> chances : mineOptions.getBlocks().entrySet())
            blocksLore.add("§f- §e" + chances.getKey() + " §f(§e" + chances.getValue() + "%§f)");

        blocksLore.add("");
        blocksLore.add("§fНажмите чтобы открыть меню редактирования блоков!");
        blocksLore.add("");

        inventory.setItem(22, ItemUtil.getItem(Material.STONE, 1, "§eБлоки", blocksLore));

        return inventory;

    }
}