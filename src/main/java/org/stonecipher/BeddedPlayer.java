package org.stonecipher;

import org.bukkit.World;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

public class BeddedPlayer {

    private Player player;
    private World world;

    public BeddedPlayer(Player p, World w) {
        this.player = p;
        this.world = w;
    }

    public void wakeUpPlayer() {
        if (player.isSleeping())
            player.wakeup(true);
    }

    public Player getPlayer() {
        return player;
    }

    public World getWorld() {
        return world;
    }
}
