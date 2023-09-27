package gg.use.playlegend.permissions.command;

import co.aikar.commands.BukkitCommandExecutionContext;
import co.aikar.commands.CommandContexts;
import co.aikar.commands.PaperCommandManager;
import gg.use.playlegend.permissions.command.commands.PermissionsCommand;
import gg.use.playlegend.permissions.command.commands.PlayerSignCommand;
import gg.use.playlegend.permissions.group.GroupManager;
import gg.use.playlegend.permissions.message.impl.YamlMessageLoader;
import gg.use.playlegend.permissions.user.UserManager;
import org.bukkit.plugin.Plugin;

public class CustomCommandManager extends PaperCommandManager {

  public CustomCommandManager(Plugin plugin) {
    super(plugin);
  }

  public void initiateCommands(GroupManager groupManager, UserManager userManager, YamlMessageLoader messageLoader) {
    this.registerCommand(new PermissionsCommand(this.plugin, groupManager, userManager, messageLoader));
    this.registerCommand(new PlayerSignCommand(this.plugin, groupManager, messageLoader));
  }

  @Override
  public synchronized CommandContexts<BukkitCommandExecutionContext> getCommandContexts() {
    if (this.contexts == null) {
      this.contexts = new CustomCommandContexts(this);
    }
    return this.contexts;
  }
}
