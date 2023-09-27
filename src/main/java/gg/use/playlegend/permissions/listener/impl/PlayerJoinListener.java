package gg.use.playlegend.permissions.listener.impl;

import gg.use.playlegend.permissions.group.GroupManager;
import gg.use.playlegend.permissions.listener.BukkitListener;
import gg.use.playlegend.permissions.user.User;
import gg.use.playlegend.permissions.user.UserManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

public class PlayerJoinListener implements BukkitListener {

  private final Plugin plugin;
  private final UserManager userManager;
  private final GroupManager groupManager;

  public PlayerJoinListener(Plugin plugin, UserManager userManager, GroupManager groupManager) {
    this.plugin = plugin;
    this.userManager = userManager;
    this.groupManager = groupManager;
  }

  @EventHandler
  public void onPlayerLogin(AsyncPlayerPreLoginEvent event) {
    User user = this.userManager.addUser(event.getUniqueId());

    Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> user.getData().loadData(userManager, groupManager));
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    User user = this.userManager.getUser(player.getUniqueId());

    event.joinMessage(Component.text("[+] " + user.getData().getGroup().prefix() + " " + player.getName()).color(TextColor.color(255, 255, 0)));
  }

}
