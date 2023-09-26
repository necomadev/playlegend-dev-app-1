package gg.use.playlegend.user;

import static org.junit.jupiter.api.Assertions.*;

import com.zaxxer.hikari.HikariDataSource;
import gg.use.playlegend.database.HikariDataSourceInitializer;
import gg.use.playlegend.database.SQLExecutor;
import gg.use.playlegend.database.table.Table;
import gg.use.playlegend.database.table.Tables;
import gg.use.playlegend.group.GroupManager;
import java.sql.SQLException;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class UserTest {

  private static HikariDataSource dataSource;
  private static GroupManager groupManager;
  private static UserManager userManager;

  @BeforeAll
  public static void setup() {
    dataSource = new HikariDataSourceInitializer(
        "127.0.0.1",
        3306,
        "playlegend_test",
        "root",
        ""
    ).getDataSource();

    try {
      dropTables(); // Before setting up new tables, we want to delete the old ones to start over with a clean database
      setupTables();
    } catch (SQLException e) {
      throw new RuntimeException("Setting up tables", e);
    }

    groupManager = new GroupManager(dataSource);
    userManager = new UserManager(dataSource);
  }

  @Test
  public void testLoadUserData() {
    UUID uuid = UUID.fromString("069a79f4-44e9-4726-a5be-fca90e38aaf5");
    User user = userManager.addUser(uuid);
    user.getData().loadData(userManager, groupManager);
    assertNotNull(user.getData().getExpireGroup());
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
