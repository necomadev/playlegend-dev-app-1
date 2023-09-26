package gg.use.playlegend.listener.impl;

import gg.use.playlegend.listener.BukkitListener;
import gg.use.playlegend.user.User;
import gg.use.playlegend.user.UserManager;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChatListener implements BukkitListener {

  private final UserManager userManager;

  public PlayerChatListener(UserManager userManager) {
    this.userManager = userManager;
  }

  @EventHandler
  public void onPlayerChat(AsyncPlayerChatEvent event) {
    Player player = event.getPlayer();
    User user = this.userManager.getUser(player.getUniqueId());
    event.setFormat(Color.YELLOW + user.getData().getGroup().prefix() + " " + player.getName() + "  " + Color.GRAY + event.getMessage());
  }

}
