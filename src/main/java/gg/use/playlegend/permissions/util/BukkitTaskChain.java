package gg.use.playlegend.permissions.util;


import co.aikar.taskchain.BukkitTaskChainFactory;
import co.aikar.taskchain.TaskChain;
import co.aikar.taskchain.TaskChainFactory;
import org.bukkit.plugin.Plugin;

public class BukkitTaskChain {

  private final TaskChainFactory taskChainFactory;

  public BukkitTaskChain(Plugin plugin) {
    this.taskChainFactory = BukkitTaskChainFactory.create(plugin, new BukkitAsynchronousQueue());
  }

  public <T> TaskChain<T> newChain() {
    return taskChainFactory.newChain();
  }
}
