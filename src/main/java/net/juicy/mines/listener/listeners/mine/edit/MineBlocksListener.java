package net.juicy.mines.listener.listeners.mine.edit;

import net.juicy.api.utils.util.ItemUtil;
import net.juicy.mines.mine.Mine;
import net.juicy.mines.mine.options.MineOptions;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class MineBlocksListener implements IMineEditor {

    public static final MineBlocksListener mineBlocks = new MineBlocksListener();

    private final Map<Player, Mine> mines = new HashMap<>();
    private final Map<Player, Inventory> editBlocks = new HashMap<>();

    public void createForPlayer(Player player, Mine mine) {

        Inventory inventory = getInventory(mine);
        player.openInventory(inventory);

        editBlocks.put(player, inventory);
        mines.put(player, mine);

    }

    @EventHandler
    public void onEditBlocks(InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();

        if (editBlocks.containsKey(player))
            if (editBlocks.get(player).equals(event.getInventory())) {

                ItemStack currentItem = event.getCurrentItem();

                if (currentItem != null)
                    if (currentItem.getType().isBlock()) {

                        Material block = currentItem.getType();

                        Mine mine = mines.get(player);
                        MineOptions mineOptions = mine.getMineOptions();

                        Map<Material, Float> blocks = mineOptions.getBlocks();

                        event.setCancelled(true);

                        boolean leftClick = event.isLeftClick();
                        boolean rightClick = event.isRightClick();

                        boolean shift = event.isShiftClick();

                        boolean middleClick = event.getAction().equals(InventoryAction.CLONE_STACK);

                        if (event.getClickedInventory().equals(player.getInventory())) {

                            if (!blocks.containsKey(block))
                                blocks.put(block, shift ? 100.0f : 0.0f);


                        } else if (editBlocks.get(player).equals(event.getClickedInventory())) {

                            if (leftClick) {

                                int amount = shift ? 10 : 1;
                                blocks.put(block, blocks.get(block) + amount);

                                if (blocks.get(block) >= 100)
                                    blocks.put(block, 100.0f);

                            }

                            if (rightClick) {

                                int amount = shift ? 10 : 1;
                                blocks.put(block, blocks.get(block) - amount);

                                if (blocks.get(block) < 0)
                                    blocks.put(block, 0.0f);

                            }

                            if (middleClick)
                                blocks.remove(block);

                        }

                        update(player);

                    }
            }
    }

    public void update(Player player) {

        Inventory inventory = getInventory(mines.get(player));

        player.openInventory(inventory);
        editBlocks.put(player, inventory);

    }

    public Inventory getInventory(Mine mine) {

        MineOptions mineOptions = mine.getMineOptions();

        Inventory inventory = Bukkit.createInventory(null, 18, "??e" + mine.getName() + " ??????????");

        for (int i = 0; i < inventory.getSize(); i++)
            inventory.setItem(i, ItemUtil.getItem(Material.GRAY_STAINED_GLASS_PANE, 1, ""));

        int blocksDisplayed = 0;

        for (Map.Entry<Material, Float> blocks : mineOptions.getBlocks().entrySet()) {

            inventory.setItem(blocksDisplayed, ItemUtil.getItem(blocks.getKey(), 1, "??e" + blocks.getKey().name(),
                    "",
                    "??f????????: ??e" + blocks.getValue() + "%",
                    "",
                    "??f?????????????? ??e?????? ??f?????????? ???????????????? ???????? (?????????? ???????????????? ??e10% ??f?????????????? ??eSHIFT??f)",
                    "??f?????????????? ??e?????? ??f?????????? ???????????? ???????? (?????????? ???????????? ??e10% ??f?????????????? ??eSHIFT??f)",
                    "",
                    "??f?????????????? ??e?????? ??f?????????? ???????????? ????????!",
                    ""));

            blocksDisplayed++;

        }

        return inventory;

    }
}