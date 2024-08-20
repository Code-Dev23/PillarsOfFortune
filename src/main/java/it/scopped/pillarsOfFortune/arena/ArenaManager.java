package it.scopped.pillarsOfFortune.arena;

import it.scopped.pillarsOfFortune.POF;
import it.scopped.pillarsOfFortune.arena.model.Arena;
import it.scopped.pillarsOfFortune.arena.state.GameState;
import it.scopped.pillarsOfFortune.utility.debug.Logs;
import it.scopped.pillarsOfFortune.utility.location.LocationUtil;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ArenaManager {
    private final POF instance;
    private final List<Arena> arenas = new ArrayList<>();

    public ArenaManager(POF instance) {
        this.instance = instance;
        loadArenas();
    }

    public boolean existArena(String id) {
        for(Arena arena : arenas) {
            if(arena == null) continue;
            if(arena.getId().equals(id))
                return true;
        }
        return false;
    }

    public Arena getArenaByPlayer(Player player) {
        return arenas.stream()
                .filter(arena ->
                        arena.getPlayers().contains(player)
                                || arena.getSpectators().contains(player))
                .findFirst()
                .orElse(null);
    }

    public Arena getArenaById(String id) {
        return arenas.stream()
                .filter(arena -> arena.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Arena searchAvailableArena() {
        return arenas.stream().filter(arena -> arena != null && arena.getState() != GameState.LIVE && !arena.isFull()).findFirst().orElse(null);
    }

    public boolean isInGame(Player player) {
        return getArenaByPlayer(player) != null;
    }

    public void loadArenas() {
        ConfigurationSection section = instance.getArenasFile().getFileConfiguration().getConfigurationSection("arenas");
        if(section == null) {
            Logs.error("0 arenas loaded.");
            return;
        }

        try {
            for(String key : section.getKeys(false)) {
                List<Location> locations = new ArrayList<>();

                ConfigurationSection spawnSection = section.getConfigurationSection(key + ".spawn_locations");
                spawnSection.getKeys(false).forEach(loc ->
                        locations.add(
                                LocationUtil.deserializeLocation(
                                        spawnSection.getString(key)
                                )
                        )
                );

                arenas.add(
                        new Arena(
                                instance,
                                key,
                                section.getInt(key + ".min_players"),
                                section.getInt(key + ".max_players"),
                                LocationUtil.deserializeLocation(section.getString("center")),
                                locations
                        )
                );
            }
        } catch (Exception ex) {
            Logs.error("Error in loading arenas.");
        }
    }
}