package gg.use.playlegend.command;

import co.aikar.commands.BukkitCommandExecutionContext;
import co.aikar.commands.CommandContexts;
import co.aikar.commands.PaperCommandManager;
import gg.use.playlegend.command.impl.PermissionsCommand;
import gg.use.playlegend.command.impl.PlayerSignCommand;
import gg.use.playlegend.group.GroupManager;
import gg.use.playlegend.message.impl.YamlMessageLoader;
import gg.use.playlegend.user.UserManager;
import org.bukkit.plugin.Plugin;

public class CustomCommandManager extends PaperCommandManager {

  public CustomCommandManager(Plugin plugin) {
    super(plugin);
  }

  public void initiateCommands(GroupManager groupManager, UserManager userManager, YamlMessageLoader messageLoader) {
    this.registerCommand(new PermissionsCommand(this.plugin, groupManager, userManager, messageLoader));
    this.registerCommand(new PlayerSignCommand(this.plugin, groupManager));
  }

  @Override
  public synchronized CommandContexts<BukkitCommandExecutionContext> getCommandContexts() {
    if (this.contexts == null) {
      this.contexts = new CustomCommandContexts(this);
    }
    return this.contexts;
  }
}
