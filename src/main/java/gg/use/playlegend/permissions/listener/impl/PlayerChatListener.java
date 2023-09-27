package gg.use.playlegend.permissions.listener.impl;

import gg.use.playlegend.permissions.listener.BukkitListener;
import gg.use.playlegend.permissions.user.User;
import gg.use.playlegend.permissions.user.UserManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChatListener implements BukkitListener {

  private final UserManager userManager;

  public PlayerChatListener(UserManager userManager) {
    this.userManager = userManager;
  }

  @SuppressWarnings("deprecation")
  @EventHandler
  public void onPlayerChat(AsyncPlayerChatEvent event) {
    Player player = event.getPlayer();
    User user = this.userManager.getUser(player.getUniqueId());
    event.setFormat(ChatColor.YELLOW + user.getData().getGroup().prefix() + " " + player.getName() + "  " + ChatColor.GRAY + event.getMessage());
  }

}
