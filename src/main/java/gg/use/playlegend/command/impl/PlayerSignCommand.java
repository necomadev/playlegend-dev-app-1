package gg.use.playlegend.command.impl;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import gg.use.playlegend.group.GroupManager;
import gg.use.playlegend.message.impl.MessageKeys;
import gg.use.playlegend.util.BukkitTaskChain;
import java.util.Objects;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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

  public PlayerSignCommand(Plugin plugin, GroupManager groupManager) {
    this.plugin = plugin;
    this.groupManager = groupManager;
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
              sign.setWaxed(true); // Disables Editing
              sign.update();
              player.sendMessage(MessageKeys.PLAYER_SIGN_SUCCESSFULLY_CREATED.getKey());
            } else {
              player.sendMessage(MessageKeys.PLAYER_GROUP_NOT_FOUND.getKey());
            }
          }).execute();
    } else {
      player.sendMessage(MessageKeys.PLAYER_SIGN_WRONG_BLOCK_TYPE.getKey());
    }
  }

}
