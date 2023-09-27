package gg.use.playlegend.permissions.command;

import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.PaperCommandContexts;
import co.aikar.commands.PaperCommandManager;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import gg.use.playlegend.permissions.util.StringUtils;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import org.bukkit.entity.Player;

public class CustomCommandContexts extends PaperCommandContexts {

  public CustomCommandContexts(PaperCommandManager manager) {
    super(manager);

    registerContext(UUID.class, c -> {
      String input = c.popFirstArg();
      if (!StringUtils.UUID.matcher(input).matches()) {
        throw new InvalidCommandArgument();
      }
      return UUID.fromString(input);
    });

    registerContext(Duration.class, c -> {
      String time = c.getFirstArg();
      TimeUnit unit = StringUtils.TIMEUNIT_SHORTCUTS.get(time.substring(time.length() - 1));
      if (unit == null) {
        return null;
      }
      try {
        int number = Integer.parseInt(time.substring(0, time.length() - 1));
        c.popFirstArg();
        return Duration.ofSeconds(unit.toSeconds(number));
      } catch (NumberFormatException e) {
        return null;
      }
    });

    registerContext(Player[].class,
        c -> Stream.of((OnlinePlayer[]) getResolver(OnlinePlayer[].class).getContext(c))
            .map(OnlinePlayer::getPlayer).toArray(Player[]::new));
  }
}
