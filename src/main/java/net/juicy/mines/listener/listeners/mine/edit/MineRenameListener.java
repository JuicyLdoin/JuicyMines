package net.juicy.mines.listener.listeners.mine.edit;

import net.juicy.mines.JuicyMinesPlugin;
import net.juicy.mines.mine.Mine;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.Map;

public class MineRenameListener implements IMineEditor {

    public static final MineRenameListener mineRename = new MineRenameListener();

    private final JuicyMinesPlugin plugin = JuicyMinesPlugin.getPlugin();
    private final Map<Player, Mine> nameEdit = new HashMap<>();

    public void createForPlayer(Player player, Mine mine) {

        nameEdit.put(player, mine);

    }


    @EventHandler
    public void onMineRename(AsyncPlayerChatEvent event) {

        Player player = event.getPlayer();

        if (nameEdit.containsKey(player)) {

            Mine mine = nameEdit.get(player);
            String newName = event.getMessage();

            event.setCancelled(true);

            if (newName.equals("Отмена")) {

                nameEdit.remove(player);
                player.sendMessage(plugin.replace("%prefix% &fВы отменили действие!"));

            } else if (mine.getName().equals(newName))
                player.sendMessage(plugin.replace("%prefix% &fШахта уже имеет данное имя!"));
            else {

                plugin.getMineManager().rename(mine, newName);
                player.sendMessage(plugin.replace("%prefix% &fВы успешно переименовали шахту, её новое имя - &e" + newName));

                nameEdit.remove(player);

            }
        }
    }
}