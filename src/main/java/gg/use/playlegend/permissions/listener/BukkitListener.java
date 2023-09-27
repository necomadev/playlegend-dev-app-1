package gg.use.playlegend.permissions.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public interface BukkitListener extends Listener {

  default void register(Plugin plugin) {
    Bukkit.getPluginManager().registerEvents(this, plugin);
  }

  default void unregister() {
    HandlerList.unregisterAll(this);
  }

}
