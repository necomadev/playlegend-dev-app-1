package gg.use.playlegend.group;

import static org.junit.jupiter.api.Assertions.*;

import com.zaxxer.hikari.HikariDataSource;
import gg.use.playlegend.database.HikariDataSourceInitializer;
import gg.use.playlegend.database.SQLExecutor;
import gg.use.playlegend.database.table.Table;
import gg.use.playlegend.database.table.Tables;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class GroupTest {

  private static HikariDataSource dataSource;
  private static GroupManager groupManager;

  @BeforeAll
  public static void setup() {
    dataSource = new HikariDataSourceInitializer("127.0.0.1", 3306, "playlegend_test", "root", "").getDataSource();

    try {
      dropTables(); // Before setting up new tables, we want to delete the old ones to start over with a clean database
      setupTables();
    } catch (SQLException e) {
      throw new RuntimeException("Setting up tables", e);
    }

    groupManager = new GroupManager(dataSource);
  }

  @Test
  public void testGroupCreate() throws SQLException {
    if (!groupManager.groupExists("MEMBER")) {
      groupManager.createGroup("member", "[M]");
      Group group = groupManager.getGroup("member");
      assertEquals("MEMBER", group.name());
    }
  }

  @Test
  public void testGroupAttributeEdit() throws SQLException {
    if (groupManager.groupExists("MEMBER")) {
      groupManager.editGroupAttribute("member", "prefix", "[X]");
      Group group = groupManager.getGroup("member");
      assertEquals("[X]", group.prefix());
    }
  }

  @Test
  public void testGroupList() throws SQLException {
    groupManager.createGroup("member", "[M]");
    groupManager.createGroup("supporter", "[S]");
    groupManager.createGroup("developer", "[D]");
    List<String> groups = groupManager.getGroupList().stream().map(Group::name).sorted().toList();
    assertEquals(Arrays.asList("DEVELOPER", "MEMBER", "SUPPORTER"), groups);
  }

  @Test
  public void testGroupRemove() throws SQLException {
    if (groupManager.groupExists("MEMBER")) {
      groupManager.removeGroup("member");
      Group group = groupManager.getGroup("member");
      assertNull(group);
    }
  }

  private static void setupTables() throws SQLException {
    for (Table table : Tables.values()) {
      try (SQLExecutor executor = new SQLExecutor(dataSource)) {
        executor.createTable(table);
      }
    }
  }

  private static void dropTables() throws SQLException {
    for (Table table : Tables.values()) {
      try (SQLExecutor executor = new SQLExecutor(dataSource)) {
        executor.dropTable(table);
      }
    }
  }

}
