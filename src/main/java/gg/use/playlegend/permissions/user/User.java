package gg.use.playlegend.permissions.user;

import java.util.UUID;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@Data
public class User {

  private final UUID uuid;

  private Player player;
  private UserData data;

  public User(UUID uuid) {
    this.uuid = uuid;
    this.data = new UserData(uuid);
  }
}
