package gg.use.playlegend.permissions.command.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.Subcommand;
import gg.use.playlegend.permissions.group.GroupManager;
import gg.use.playlegend.permissions.message.impl.MessageKeys;
import gg.use.playlegend.permissions.message.impl.YamlMessageLoader;
import gg.use.playlegend.permissions.user.User;
import gg.use.playlegend.permissions.util.BukkitTaskChain;
import gg.use.playlegend.permissions.user.UserManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

@CommandAlias("permissions|perms|p")
public class PermissionsCommand extends BaseCommand {

  private final Plugin plugin;
  private final GroupManager groupManager;
  private final UserManager userManager;
  private final YamlMessageLoader messageLoader;

  public PermissionsCommand(Plugin plugin, GroupManager groupManager, UserManager userManager, YamlMessageLoader messageLoader) {
    this.plugin = plugin;
    this.groupManager = groupManager;
    this.userManager = userManager;
    this.messageLoader = messageLoader;
  }

  @Subcommand("group create")
  public void onGroupCreate(CommandSender sender, String name, String prefix) {
    new BukkitTaskChain(this.plugin).newChain()
        .asyncFirst(() -> this.groupManager.getGroup(name))
        .asyncLast(group -> {
          if (group == null) {
            try {
              this.groupManager.createGroup(name, prefix);

              sender.sendMessage(this.messageLoader.getMessage(MessageKeys.GROUP_SUCCESSFULLY_CREATED, name));
            } catch (SQLException e) {
              throw new RuntimeException("Creating group: " + name, e);
            }
          } else {
            sender.sendMessage(this.messageLoader.getMessage(MessageKeys.GROUP_ALREADY_EXISTS, name));
          }
        }).execute();
  }

  @Subcommand("group remove")
  public void onGroupRemove(CommandSender sender, String name) {
    if (name.equalsIgnoreCase("default")) {
      sender.sendMessage(this.messageLoader.getMessage(MessageKeys.DEFAULT_GROUP_CAN_NOT_BE_REMOVED));
      return;
    }
    new BukkitTaskChain(this.plugin).newChain()
        .asyncFirst(() -> this.groupManager.getGroup(name))
        .asyncLast(group -> {
          if (group != null) {
            try {
              this.groupManager.removeGroup(name);

              sender.sendMessage(this.messageLoader.getMessage(MessageKeys.GROUP_SUCCESSFULLY_REMOVED,
                  group.name()));
            } catch (SQLException e) {
              throw new RuntimeException("Removing group: " + name, e);
            }
          } else {
            sender.sendMessage(this.messageLoader.getMessage(MessageKeys.GROUP_DOES_NOT_EXIST, name));
          }
        }).execute();
  }

  @Subcommand("group set")
  public void onGroupSet(CommandSender sender, String target, String name, @Optional Duration duration) {
    OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(target);

    new BukkitTaskChain(this.plugin).newChain()
        .asyncFirst(() -> this.groupManager.getGroup(name))
        .asyncLast(group -> {
          if (group != null) {
            try {
              this.groupManager.setPlayerGroup(targetPlayer.getUniqueId(), group.name(), duration);

              sender.sendMessage(this.messageLoader.getMessage(MessageKeys.GROUP_SUCCESSFULLY_SET,
                  group.name(), target));

              if (targetPlayer.getPlayer() != null) {
                User user = this.userManager.getUser(targetPlayer.getUniqueId());
                user.getData().setGroup(group);

                targetPlayer.getPlayer().sendMessage(this.messageLoader.getMessage(MessageKeys.PLAYER_GROUP_SUCCESSFULLY_UPDATED,
                    group.name()));
              }
            } catch (SQLException e) {
              throw new RuntimeException("Setting group '" + name + "' for '" + target + "'", e);
            }
          } else {
            sender.sendMessage(this.messageLoader.getMessage(MessageKeys.GROUP_DOES_NOT_EXIST, name));
          }
        }).execute();
  }

  @Subcommand("group edit prefix")
  public void onGroupEditPrefix(CommandSender sender, String name, String newPrefix) {
    new BukkitTaskChain(this.plugin).newChain()
        .asyncFirst(() -> this.groupManager.getGroup(name))
        .asyncLast(group -> {
          if (group != null) {
            try {
              this.groupManager.editGroupAttribute(name, "prefix", newPrefix);

              sender.sendMessage(this.messageLoader.getMessage(MessageKeys.GROUP_ATTRIBUTE_SUCCESSFULLY_RENAMED,
                  group.name(),
                  "prefix",
                  newPrefix));
            } catch (SQLException e) {
              throw new RuntimeException("Editing group prefix: " + name, e);
            }
          } else {
            sender.sendMessage(this.messageLoader.getMessage(MessageKeys.GROUP_DOES_NOT_EXIST, name));
          }
        }).execute();
  }

  @Subcommand("group list")
  public void onGroupList(CommandSender sender) {
    new BukkitTaskChain(this.plugin).newChain()
        .asyncFirst(this.groupManager::getGroupList)
        .syncLast(groupList -> {
          sender.sendMessage(this.messageLoader.getMessage(MessageKeys.GROUP_LIST_TITLE,
              groupList.size()));
          groupList.forEach(group -> sender.sendMessage(" - Name: " + group.name() + ", Prefix: " + group.prefix()));
        }).execute();
  }

  @Subcommand("group info")
  public void onGroupInfo(CommandSender sender, String target) {
    OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(target);

    new BukkitTaskChain(this.plugin).newChain()
        .asyncFirst(() -> this.groupManager.getPlayerGroup(targetPlayer.getUniqueId()))
        .asyncLast(playerGroup -> {
          if (playerGroup != null) {
            Timestamp expire = this.groupManager.getPlayerGroupExpire(targetPlayer.getUniqueId());
            sender.sendMessage(this.messageLoader.getMessage(MessageKeys.PLAYER_GROUP_INFO,
                targetPlayer.getName(),
                playerGroup.name(),
                expire.toString()));
          } else {
            sender.sendMessage(this.messageLoader.getMessage(MessageKeys.PLAYER_GROUP_NOT_FOUND, target));
          }
        }).execute();
  }

}
