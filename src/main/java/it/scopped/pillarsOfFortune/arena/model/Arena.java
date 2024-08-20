package it.scopped.pillarsOfFortune.arena.model;

import com.google.common.collect.Sets;
import it.scopped.pillarsOfFortune.POF;
import it.scopped.pillarsOfFortune.arena.state.GameState;
import it.scopped.pillarsOfFortune.tasks.GameEngineTask;
import it.scopped.pillarsOfFortune.utility.debug.Logs;
import it.scopped.pillarsOfFortune.utility.location.LocationUtil;
import it.scopped.pillarsOfFortune.utility.strings.CC;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
public class Arena {
    private final POF instance;
    private final String id;
    private final List<Location> locations;
    @Setter
    private Location center;
    @Setter
    private GameState state;
    private final Set<Player> players, spectators;
    @Setter
    private int minPlayers, maxPlayers;

    public Arena(POF instance, String id, int minPlayers, int maxPlayers, Location center, List<Location> locations) {
        this.instance = instance;
        this.id = id;
        this.locations = locations;
        this.center = center;

        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;

        this.players = Sets.newConcurrentHashSet();
        this.spectators = Sets.newConcurrentHashSet();

        this.state = GameState.ENABLED;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public void countdown() {

    }

    public void start() {
        if (locations.size() < players.size()) {
            Logs.warn("Not enough locations to teleport players in arena " + id);
            return;
        }

        List<Location> availableLocations = new ArrayList<>(locations);

        for (Player player : players) {
            Location location = availableLocations.remove(0);
            player.teleportAsync(location);
        }

        state = GameState.LIVE;

        new GameEngineTask(instance, this).start();

        broadcast("&e&lIl game e' iniziato!");
        sendTitle("&6COMBATTI!", "&fIl game e' iniziato.");
    }

    public boolean isFull() {
        return getPlayers().size() > maxPlayers;
    }

    public void broadcast(String message, Object... params) {
        players.forEach(p -> CC.send(p, message, params));
        spectators.forEach(p -> CC.send(p, message, params));
    }

    public void sendTitle(String title, String subTitle) {
        players.forEach(p -> CC.sendTitle(p, title, subTitle));
        spectators.forEach(p -> CC.sendTitle(p, title, subTitle));
    }

    public void sendActionBar(String message, Object... params) {
        players.forEach(p -> CC.sendActionBar(p, message, params));
        spectators.forEach(p -> CC.sendActionBar(p, message, params));
    }
}