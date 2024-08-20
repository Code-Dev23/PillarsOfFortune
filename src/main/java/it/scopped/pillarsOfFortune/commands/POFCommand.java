package it.scopped.pillarsOfFortune.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import org.bukkit.entity.Player;

@CommandAlias("pof|pillarsoffortune")
@CommandPermission("pillarsoffortune.command")
public class POFCommand extends BaseCommand {
    @Default
    public void onCommand(Player player) {

    }
}