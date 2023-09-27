package gg.use.playlegend.permissions.group;

import com.zaxxer.hikari.HikariDataSource;
import gg.use.playlegend.permissions.database.SQLQueryBuilder;
import gg.use.playlegend.permissions.database.SQLExecutor;
import gg.use.playlegend.permissions.database.table.Tables;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GroupManager {

  private final HikariDataSource dataSource;

  public GroupManager(HikariDataSource dataSource) {
    this.dataSource = dataSource;
  }

  public void createGroup(String name, String prefix) throws SQLException {
    try (SQLExecutor executor = new SQLExecutor(this.dataSource)) {
      SQLQueryBuilder builder = new SQLQueryBuilder().insert(Tables.GROUPS, "'" + name.toUpperCase() + "', '" + prefix + "'");
      executor.executeUpdate(builder.build());
    }
  }

  public void removeGroup(String name) throws SQLException {
    try (SQLExecutor executor = new SQLExecutor(this.dataSource)) {
      SQLQueryBuilder builder = new SQLQueryBuilder().delete(Tables.GROUPS).where("name = ?");
      executor.executeUpdate(builder.build(), name.toUpperCase());
    }
  }

  public boolean groupExists(String name) throws SQLException {
    try (SQLExecutor executor = new SQLExecutor(this.dataSource)) {
      return executor.rowExists(Tables.GROUPS, "name = ?", name.toUpperCase());
    }
  }

  public void editGroupAttribute(String group, String key, String value) throws SQLException {
    try (SQLExecutor executor = new SQLExecutor(this.dataSource)) {
      SQLQueryBuilder builder = new SQLQueryBuilder().update(Tables.GROUPS, key).where("name = ?");
      executor.executeUpdate(builder.build(), value, group.toUpperCase());
    }
  }

  public void setPlayerGroup(UUID uuid, String group, Duration duration) throws SQLException {
    try (SQLExecutor executor = new SQLExecutor(this.dataSource)) {
      Timestamp expireTime =
          duration != null ? Timestamp.from(Instant.now().plusSeconds(duration.getSeconds())) : Timestamp.valueOf(LocalDateTime.now().plusYears(100));

      SQLQueryBuilder builder = new SQLQueryBuilder().update(Tables.USERS, "playerGroup", "playerGroupExpire").where("uuid = ?");
      executor.executeUpdate(builder.build(), group.toUpperCase(), expireTime.toLocalDateTime(), uuid.toString());
    }
  }

  public Group getPlayerGroup(UUID uuid) {
    try (SQLExecutor executor = new SQLExecutor(this.dataSource)) {
      SQLQueryBuilder builder = new SQLQueryBuilder().select(Tables.USERS, "*").where("uuid = ?");
      executor.executeQuery(builder.build(), uuid.toString());

      if (executor.getResultSet().next()) {
        String playerGroupField = executor.getString("playerGroup");

        return this.getGroup(playerGroupField);
      }

      // If the player's group is missing it's likely that there is no entry in the groups table
      // So we set the players group to default again to keep the user updated
      this.setPlayerGroup(uuid, "DEFAULT", null);

      return this.getGroup("DEFAULT"); // Ensures that we always get the default group
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public Timestamp getPlayerGroupExpire(UUID uuid) {
    try (SQLExecutor executor = new SQLExecutor(this.dataSource)) {
      SQLQueryBuilder builder = new SQLQueryBuilder().select(Tables.USERS, "*").where("uuid = ?");
      executor.executeQuery(builder.build(), uuid.toString());

      if (executor.getResultSet().next()) {
        Object result = executor.getObject("playerGroupExpire");

        return result instanceof LocalDateTime
            ? Timestamp.valueOf(executor.getLocalDateTime("playerGroupExpire"))
            : executor.getTimestamp("playerGroupExpire");
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return null;
  }

  public Group getGroup(String name) {
    try (SQLExecutor executor = new SQLExecutor(this.dataSource)) {
      SQLQueryBuilder builder = new SQLQueryBuilder().select(Tables.GROUPS, "*").where("name = ?");
      executor.executeQuery(builder.build(), name.toUpperCase());

      if (executor.getResultSet().next()) {
        String nameField = executor.getString("name");
        String prefixField = executor.getString("prefix");

        return new Group(nameField, prefixField);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return null;
  }

  public List<Group> getGroupList() {
    List<Group> groups = new ArrayList<>();

    try (SQLExecutor executor = new SQLExecutor(this.dataSource)) {
      SQLQueryBuilder builder = new SQLQueryBuilder().select(Tables.GROUPS, "*");
      executor.executeQuery(builder.build());

      while (executor.getResultSet().next()) {
        String nameField = executor.getString("name");
        String prefixField = executor.getString("prefix");

        groups.add(new Group(nameField, prefixField));
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return groups;
  }

  public void createDefaultGroup() throws SQLException {
    if (groupExists("DEFAULT")) {
      return;
    }
    try (SQLExecutor executor = new SQLExecutor(dataSource)) {
      SQLQueryBuilder builder = new SQLQueryBuilder().insert(Tables.GROUPS, "'DEFAULT', '[Default]'");
      executor.executeUpdate(builder.build());
    }
  }

}
