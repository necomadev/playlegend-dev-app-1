package gg.use.playlegend.permissions.database;

import static org.junit.jupiter.api.Assertions.*;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class DatabaseConnectivityTest {

  private static HikariDataSource dataSource;

  @BeforeAll
  public static void setup() {
    dataSource = new HikariDataSourceInitializer(
        "127.0.0.1",
        3306,
        "playlegend_test",
        "root",
        "").getDataSource();
  }

  @Test
  public void testDatabaseConnection() {
    assertTrue(dataSource.isRunning());
  }

}
