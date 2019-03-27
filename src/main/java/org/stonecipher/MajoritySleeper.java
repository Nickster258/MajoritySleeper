package org.stonecipher;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.material.Bed;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.UUID;

public class MajoritySleeper extends JavaPlugin implements Listener {

    List<BeddedPlayer> beddedPlayers;

    @Override
    public void onEnable( ) {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    private void playerLeaveBed(PlayerBedLeaveEvent e) {
        beddedPlayers.remove(new BeddedPlayer(e.getPlayer(), e.getPlayer().getWorld()));
    }

    @EventHandler
    private void playerEnterBed(PlayerBedEnterEvent e) {
        int playerCount = e.getPlayer().getWorld().getPlayers().size());
        beddedPlayers.add(new BeddedPlayer(e.getPlayer(), e.getPlayer().getWorld()));
        int getWorldSleeperCount = getWorldSleeperCount(e.getPlayer().getWorld());
        if((float) (getWorldSleeperCount/playerCount) > (0.5)) {
            wakeUpSleepers(e.getPlayer().getWorld());
        }
    }

    private void wakeUpSleepers(World w) {
        List<BeddedPlayer> toRemove;
        for (BeddedPlayer bp: beddedPlayers) {
            if (bp.getWorld().equals(w)) {
                bp.wakeUpPlayer();
                toRemove.add(bp);
            }
        }
        beddedPlayers.removeAll(toRemove);
    }

    private int getWorldPlayerCount(UUID uuid) {
        return getServer().getWorld(uuid).getPlayers().size();
    }

    private int getWorldSleeperCount(World w) {
        int toReturn = 0;
        for (BeddedPlayer bp: beddedPlayers) {
            if (bp.getWorld().equals(w)) toReturn++;
        }
        return toReturn;
    }
}
