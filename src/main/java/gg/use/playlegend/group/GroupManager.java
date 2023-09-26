package gg.use.playlegend.group;

import com.zaxxer.hikari.HikariDataSource;
import gg.use.playlegend.database.SQLBuilder;
import gg.use.playlegend.database.SQLExecutor;
import gg.use.playlegend.database.table.Tables;
import gg.use.playlegend.util.StringUtils;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;

public class GroupManager {

  private final HikariDataSource dataSource;

  @Getter
  private Group defaultGroup;

  public GroupManager(HikariDataSource dataSource) {
    this.dataSource = dataSource;
  }

  public void createGroup(String name, String prefix) throws SQLException {
    try (SQLExecutor executor = new SQLExecutor(this.dataSource)) {
      SQLBuilder builder = new SQLBuilder().insert(Tables.GROUPS, "'" + name.toUpperCase() + "', '" + prefix + "'");
      executor.executeUpdate(builder.build());
    }
  }

  public void removeGroup(String name) throws SQLException {
    try (SQLExecutor executor = new SQLExecutor(this.dataSource)) {
      SQLBuilder builder = new SQLBuilder().delete(Tables.GROUPS).where("name = ?");
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
      SQLBuilder builder = new SQLBuilder().update(Tables.GROUPS, key).where("name = ?");
      executor.executeUpdate(builder.build(), value, group.toUpperCase());
    }
  }

  public void setPlayerGroup(UUID uuid, String group, Duration duration) throws SQLException {
    try (SQLExecutor executor = new SQLExecutor(this.dataSource)) {
      Timestamp expireTime =
          duration != null ? Timestamp.from(Instant.now().plusSeconds(duration.getSeconds())) : Timestamp.valueOf(LocalDateTime.now().plusYears(100));

      SQLBuilder builder = new SQLBuilder().update(Tables.USERS, "playerGroup", "playerGroupExpire").where("uuid = ?");
      executor.executeUpdate(builder.build(), group.toUpperCase(), expireTime, uuid.toString());
    }
  }

  public Group getPlayerGroup(UUID uuid) {
    try (SQLExecutor executor = new SQLExecutor(this.dataSource)) {
      SQLBuilder builder = new SQLBuilder().select(Tables.USERS, "*").where("uuid = ?");
      executor.executeQuery(builder.build(), uuid.toString());

      if (executor.getResultSet().next()) {
        String playerGroupField = executor.getString("playerGroup");

        return this.getGroup(playerGroupField);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return null;
  }

  public Timestamp getPlayerGroupExpire(UUID uuid) {
    try (SQLExecutor executor = new SQLExecutor(this.dataSource)) {
      SQLBuilder builder = new SQLBuilder().select(Tables.USERS, "*").where("uuid = ?");
      executor.executeQuery(builder.build(), uuid.toString());

      if (executor.getResultSet().next()) {
        return executor.getTimestamp("playerGroupExpire");
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return null;
  }

  public Group getGroup(String name) {
    try (SQLExecutor executor = new SQLExecutor(this.dataSource)) {
      SQLBuilder builder = new SQLBuilder().select(Tables.GROUPS, "*").where("name = ?");
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
      SQLBuilder builder = new SQLBuilder().select(Tables.GROUPS, "*");
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

}
