package gg.use.playlegend.user;

import com.zaxxer.hikari.HikariDataSource;
import gg.use.playlegend.database.SQLBuilder;
import gg.use.playlegend.database.SQLExecutor;
import gg.use.playlegend.database.table.Tables;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserManager implements UserList {

  private final Map<UUID, User> users = new HashMap<>();
  private final HikariDataSource dataSource;

  public UserManager(HikariDataSource dataSource) {
    this.dataSource = dataSource;
  }

  public void createUser(UUID uuid) throws SQLException {
    if (!userExists(uuid)) {
      try (SQLExecutor executor = new SQLExecutor(this.dataSource)) {
        SQLBuilder builder = new SQLBuilder().insert(Tables.USERS,
            "'" + uuid + "', " // UUID
                + "'DEFAULT', " // Group // TODO: Add possibility to set default group, auto-generate a default group in config.yml
                + "FROM_UNIXTIME(0)" // Group Expire
        );
        executor.executeUpdate(builder.build());
      }
    }
  }

  public boolean userExists(UUID uuid) {
    try (SQLExecutor executor = new SQLExecutor(this.dataSource)) {
      return executor.rowExists(Tables.USERS, "uuid = ?", uuid.toString());
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public User addUser(UUID uuid) {
    User user = new User(uuid);
    users.put(uuid, user);
    return user;
  }

  @Override
  public void removeUser(UUID uuid) {
    users.remove(uuid);
  }

  @Override
  public User getUser(UUID uuid) {
    return users.get(uuid);
  }
}
