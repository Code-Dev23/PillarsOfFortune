package it.scopped.pillarsOfFortune.tasks;

import it.scopped.pillarsOfFortune.POF;
import it.scopped.pillarsOfFortune.arena.model.Arena;
import it.scopped.pillarsOfFortune.arena.state.GameState;
import it.scopped.pillarsOfFortune.utility.debug.Logs;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameEngineTask extends BukkitRunnable {

    private final POF instance;
    private final Arena arena;
    private final List<Material> materials;

    public GameEngineTask(POF instance, Arena arena) {
        this.instance = instance;
        this.arena = arena;
        this.materials = Stream.of(Material.values())
                .filter(Material::isItem)
                .collect(Collectors.toList());;
    }

    public void start() {
        runTaskTimerAsynchronously(instance, 0L, 5 * 20L);
    }

    @Override
    public void run() {
        if(arena.getState() != GameState.LIVE) {
            Logs.error("The is not started.");
            this.cancel();
            return;
        }

        arena.getPlayers().forEach(player -> {
            if(isInventoryFull(player)) return;
            player.getInventory().addItem(generateRandomItem());
        });
    }

    public ItemStack generateRandomItem() {
        Material randomMaterial = materials.get(instance.getRandom().nextInt(materials.size()));
        return new ItemStack(randomMaterial);
    }

    public static boolean isInventoryFull(Player player) {
        for (ItemStack item : player.getInventory().getContents())
            if (item == null)
                return false;
        return true;
    }
}