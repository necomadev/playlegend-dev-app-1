package gg.use.playlegend.permissions.listener.impl;

import gg.use.playlegend.permissions.listener.BukkitListener;
import gg.use.playlegend.permissions.user.UserManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements BukkitListener {

  private final UserManager userManager;

  public PlayerQuitListener(UserManager userManager) {
    this.userManager = userManager;
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent event) {
    Player player = event.getPlayer();
    this.userManager.removeUser(player.getUniqueId());
  }

}
