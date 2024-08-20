package it.scopped.pillarsOfFortune.game;

import it.scopped.pillarsOfFortune.arena.ArenaManager;
import it.scopped.pillarsOfFortune.arena.model.Arena;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public class GameListeners implements Listener {
    private final ArenaManager arenaManager;

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Arena arena = arenaManager.getArenaByPlayer(player);
        if(arena == null) return;

        arena.removePlayer(player);
    }

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player player) || !(event.getDamager() instanceof Player damager)) return;

        if(arenaManager.isInGame(player) || arenaManager.isInGame(damager))
            event.setCancelled(true);
    }
}