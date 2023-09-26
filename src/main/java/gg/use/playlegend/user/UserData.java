package gg.use.playlegend.user;

import gg.use.playlegend.group.Group;
import gg.use.playlegend.group.GroupManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;

@Data
public class UserData {

  private final UUID uuid;

  private Group group;
  private Timestamp expireGroup;

  public UserData(UUID uuid) {
    this.uuid = uuid;
  }

  public void loadData(UserManager userManager, GroupManager groupManager) {
    try {
      // This ensures that there is a user profile to load data from
      userManager.createUser(uuid);

      // Actual loading starts here
      this.group = groupManager.getPlayerGroup(uuid);
      this.expireGroup = groupManager.getPlayerGroupExpire(uuid);

      // Group Expire Check
      if (this.expireGroup.getTime() > 0 && this.expireGroup.toLocalDateTime().isBefore(LocalDateTime.now())) {
        this.group = groupManager.getDefaultGroup();

        groupManager.setPlayerGroup(uuid, this.group.name(), null);
      }
    } catch (SQLException e) {
      // Log.exception("Loading User Data", e);
      throw new RuntimeException(e);
    }
  }
}
