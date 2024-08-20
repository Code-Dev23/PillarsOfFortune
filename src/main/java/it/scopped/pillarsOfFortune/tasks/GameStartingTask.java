package it.scopped.pillarsOfFortune.tasks;

import it.scopped.pillarsOfFortune.POF;
import it.scopped.pillarsOfFortune.arena.model.Arena;
import lombok.RequiredArgsConstructor;
import org.bukkit.scheduler.BukkitRunnable;

@RequiredArgsConstructor
public class GameStartingTask extends BukkitRunnable {

    private final POF instance;
    private final Arena arena;
    private int i;

    public void start() {
        runTaskTimer(instance, 0L, 20L);
    }

    @Override
    public void run() {
        if(i == 0) {
            arena.start();
            this.cancel();
            return;
        }

        if(arena.getPlayers().size() < arena.getMinPlayers()) {
            arena.broadcast("&cNon ci sono abbastanza giocatori per iniziare il game.");
            this.cancel();
            return;
        }

        if(i % 10 == 0 || i <= 5) {
            arena.sendTitle("&e" + i + " secondi", "");
            arena.broadcast("&eIl game si avviera' tra " + i + " secondi...");
        }

        i--;
    }
}
