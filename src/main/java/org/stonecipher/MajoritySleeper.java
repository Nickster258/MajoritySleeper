package org.stonecipher;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MajoritySleeper extends JavaPlugin implements Listener {

    ArrayList<BeddedPlayer> beddedPlayers = new ArrayList<BeddedPlayer>();

    @Override
    public void onEnable( ) {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    private void playerLeave(PlayerQuitEvent e) {

    }

    @EventHandler
    private void playerLeaveBed(PlayerBedLeaveEvent e) {
        getLogger().info("LEAVING BED");
        //getLogger().info("Sleeping ratio: " + getWorldSleepRatio(e.getPlayer().getWorld()));
        beddedPlayers.remove(new BeddedPlayer(e.getPlayer(), e.getPlayer().getWorld()));
    }

    @EventHandler
    private void playerEnterBed(PlayerBedEnterEvent e) {
        Player p = e.getPlayer();
        getLogger().info("ENTERING BED");
        //World tmp = e.getPlayer().getWorld();
        beddedPlayers.add(new BeddedPlayer(p, p.getWorld()));
        //getLogger().info("Sleeping ratio: " + getWorldSleepRatio(tmp));
        e.getPlayer().sleep(e.getPlayer().getLocation(), true);
        if( getWorldSleepRatio(p.getWorld()) > 0.5) {
            wakeUpSleepers(p.getWorld());
            p.getWorld().setTime(0);
        }
    }

    private float getWorldSleepRatio(World w) {
        int playerCount = w.getPlayers().size();
        int sleepCount = getWorldSleeperCount(w);
        return ((float) sleepCount/playerCount);
    }

    private void wakeUpSleepers(World w) {
        List<BeddedPlayer> toRemove = null;
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
            if (bp.getWorld().equals(w)) {
                toReturn++;
            }
        }
        return toReturn;
        //return 1;
    }
}
