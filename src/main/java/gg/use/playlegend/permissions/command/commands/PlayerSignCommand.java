package gg.use.playlegend.permissions.command.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import gg.use.playlegend.permissions.group.GroupManager;
import gg.use.playlegend.permissions.message.impl.MessageKeys;
import gg.use.playlegend.permissions.message.impl.YamlMessageLoader;
import gg.use.playlegend.permissions.util.BukkitTaskChain;
import java.util.Objects;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

@CommandAlias("playersign|psign|sign")
public class PlayerSignCommand extends BaseCommand {

  private final Plugin plugin;
  private final GroupManager groupManager;
  private final YamlMessageLoader messageLoader;

  public PlayerSignCommand(Plugin plugin, GroupManager groupManager, YamlMessageLoader messageLoader) {
    this.plugin = plugin;
    this.groupManager = groupManager;
    this.messageLoader = messageLoader;
  }

  @Subcommand("place")
  public void onPlayerSignPlace(Player player, String target) {
    Block block = player.getTargetBlock(null, 5);

    if (block.getState() instanceof Sign sign) {
      OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(target);

      new BukkitTaskChain(this.plugin).newChain()
          .asyncFirst(() -> this.groupManager.getPlayerGroup(targetPlayer.getUniqueId()))
          .syncLast(input -> {
            if (input != null) {
              Side side = sign.getInteractableSideFor(player);
              sign.getSide(side).line(1, Component.text(Objects.requireNonNull(targetPlayer.getName())));
              sign.getSide(side).line(2, Component.text(input.prefix()));
              sign.setWaxed(true); // Disables Sign Editing
              sign.update();
              player.sendMessage(this.messageLoader.getMessage(MessageKeys.PLAYER_SIGN_SUCCESSFULLY_CREATED,
                  target));
            } else {
              player.sendMessage(this.messageLoader.getMessage(MessageKeys.PLAYER_GROUP_NOT_FOUND,
                  target));
            }
          }).execute();
    } else {
      player.sendMessage(this.messageLoader.getMessage(MessageKeys.PLAYER_SIGN_WRONG_BLOCK_TYPE));
    }
  }

}
