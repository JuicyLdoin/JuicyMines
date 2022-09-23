package net.juicy.mines.listener.listeners.mine.edit;

import net.juicy.api.utils.util.LocationUtil;
import net.juicy.mines.JuicyMinesPlugin;
import net.juicy.mines.mine.Mine;
import net.juicy.mines.mine.options.MineOptions;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.HashMap;
import java.util.Map;

public class MineLocationListener implements IMineEditor {

    public static final MineLocationListener mineLocations = new MineLocationListener();

    private final JuicyMinesPlugin plugin = JuicyMinesPlugin.getPlugin();

    private final Map<Player, Mine> mines = new HashMap<>();
    private final Map<Player, String> locations = new HashMap<>();

    public void createForPlayer(Player player, Mine mine) {}

    public void createForPlayer(Player player, Mine mine, String location) {

        mines.put(player, mine);
        locations.put(player, location);

    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        Player player = event.getPlayer();

        if (mines.containsKey(player) && locations.containsKey(player)) {

            Mine mine = mines.get(player);
            MineOptions mineOptions = mine.getMineOptions();

            String locationType = locations.get(player);

            if (locationType.equals("minLocation")) {

                mineOptions.setMinLocation(event.getBlock().getLocation());
                LocationUtil.setMinAndMaxLocations(mineOptions.getMinLocation(), mineOptions.getMaxLocation());

                player.sendMessage(plugin.replace("%prefix% &fВы установили минимальную локацию шахты!"));

            } else if (locationType.equals("maxLocation")) {

                mineOptions.setMaxLocation(event.getBlock().getLocation());
                LocationUtil.setMinAndMaxLocations(mineOptions.getMinLocation(), mineOptions.getMaxLocation());

                player.sendMessage(plugin.replace("%prefix% &fВы установили максимальную локацию шахты!"));

            }

            event.setCancelled(true);

            mines.remove(player);
            locations.remove(player);

        }
    }
}