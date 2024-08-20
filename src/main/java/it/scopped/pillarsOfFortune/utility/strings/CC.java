package it.scopped.pillarsOfFortune.utility.strings;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.title.Title;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public final class CC {

    public static Component component(String string, Object... params) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(replace(string, params)).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }

    public static String translate(Component comp) {
        return PlainTextComponentSerializer.plainText().serialize(comp);
    }

    public static List<Component> component(List<String> message) {
        return message.stream().map(CC::component).collect(Collectors.toList());
    }

    public static Component component(Player player, String message, Object... params) {
        return component(PlaceholderAPI.setPlaceholders(player, replace(message, params)));
    }

    public static String replace(String message, Object... params) {
        if (params.length % 2 != 0)
            throw new IllegalArgumentException("Parameters should be in key-value pairs.");
        for (int i = 0; i < params.length; i += 2)
            message = message.replace(params[i].toString(), params[i + 1].toString());
        return message;
    }

    public static void send(CommandSender sender, String message, Object... params) {
        sender.sendMessage((sender instanceof Player) ? component((Player)sender, message, params) : component(message, params));
    }

    public static void sendTitle(Player player, String title, String subTitle) {
        player.showTitle(
                Title.title(
                        component(player, title, new Object[0]),
                        component(player, subTitle, new Object[0]),
                        Title.Times.times(Duration.ofSeconds(1L), Duration.ofSeconds(2L), Duration.ofSeconds(1L))));
    }

    public static void sendActionBar(Player player, String text, Object... params) {
        player.sendActionBar(component(player, text, params));
    }
}