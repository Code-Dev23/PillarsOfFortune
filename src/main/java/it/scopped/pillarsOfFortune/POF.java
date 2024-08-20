package it.scopped.pillarsOfFortune;

import it.scopped.pillarsOfFortune.arena.ArenaManager;
import lombok.Getter;
import net.pino.simpleconfig.BasicQuickConfig;
import net.pino.simpleconfig.impl.QuickConfig;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

@Getter
public final class POF extends JavaPlugin {

    @Getter
    private static POF instance;
    private Random random;

    private QuickConfig configFile, messagesFile, arenasFile;

    private ArenaManager arenaManager;

    @Override
    public void onEnable() {
        instance = this;
        initConfigs();

        initialize();
    }

    @Override
    public void onDisable() {

    }

    private void initialize() {
        this.random = new Random();
        this.arenaManager = new ArenaManager(this);
    }

    private void initConfigs() {
        this.configFile = new BasicQuickConfig();
        this.messagesFile = new BasicQuickConfig();
        this.arenasFile = new BasicQuickConfig();

        this.configFile.registerQuickConfig(this, "config.yml");
        this.messagesFile.registerQuickConfig(this, "messages.yml");
        this.arenasFile.registerQuickConfig(this, "arenas.yml");
    }
}
