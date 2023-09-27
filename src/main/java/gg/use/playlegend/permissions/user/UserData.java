package gg.use.playlegend.permissions.user;

import gg.use.playlegend.permissions.group.Group;
import gg.use.playlegend.permissions.group.GroupManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;

@Data
public class UserData {

  private final UUID uuid;

  private Group group;
  private Timestamp groupExpire;

  public UserData(UUID uuid) {
    this.uuid = uuid;
  }

  public void loadData(UserManager userManager, GroupManager groupManager) {
    try {
      // This ensures that there is a user profile to load data from
      userManager.createUser(uuid);

      // Actual loading starts here
      this.group = groupManager.getPlayerGroup(uuid);
      this.groupExpire = groupManager.getPlayerGroupExpire(uuid);

      // Group Expire Check
      if (!this.group.name().equalsIgnoreCase("default")) {
        if (this.groupExpire.toLocalDateTime().isBefore(LocalDateTime.now())) {
          this.group = groupManager.getGroup("DEFAULT");

          groupManager.setPlayerGroup(uuid, this.group.name(), null);
        }
      }
    } catch (SQLException e) {
      // Log.exception("Loading User Data", e);
      throw new RuntimeException(e);
    }
  }
}
