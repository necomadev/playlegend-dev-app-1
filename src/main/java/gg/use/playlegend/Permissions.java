package gg.use.playlegend;

import com.zaxxer.hikari.HikariDataSource;
import gg.use.playlegend.command.CustomCommandManager;
import gg.use.playlegend.database.HikariDataSourceInitializer;
import gg.use.playlegend.database.SQLExecutor;
import gg.use.playlegend.database.table.Table;
import gg.use.playlegend.database.table.Tables;
import gg.use.playlegend.group.GroupManager;
import gg.use.playlegend.listener.impl.PlayerChatListener;
import gg.use.playlegend.listener.impl.PlayerJoinListener;
import gg.use.playlegend.listener.impl.PlayerQuitListener;
import gg.use.playlegend.message.impl.YamlMessageLoader;
import gg.use.playlegend.user.UserManager;
import java.sql.SQLException;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Permissions extends JavaPlugin {

  private HikariDataSource dataSource;
  private GroupManager groupManager;
  private CustomCommandManager commandManager;
  private UserManager userManager;
  private YamlMessageLoader messageLoader;

  @Override
  public void onLoad() {
    this.saveConfig();
    this.saveDefaultConfig();

    this.dataSource = new HikariDataSourceInitializer(
        this.getConfig().getString("mysql.host"),
        this.getConfig().getInt("mysql.port"),
        this.getConfig().getString("mysql.database"),
        this.getConfig().getString("mysql.user"),
        this.getConfig().getString("mysql.password")).getDataSource();
    this.groupManager = new GroupManager(this.dataSource);
    this.userManager = new UserManager(this.dataSource);

    this.commandManager = new CustomCommandManager(this);
    this.messageLoader = new YamlMessageLoader(this.getConfig());
  }

  @Override
  public void onEnable() {
    Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
      try {
        this.createTables();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    });

    this.registerCommands();
    this.registerListeners();
  }

  @Override
  public void onDisable() {
    this.dataSource.close();
  }

  private void registerCommands() {
    this.commandManager.initiateCommands(this.groupManager, this.userManager, messageLoader);
  }

  private void registerListeners() {
    new PlayerJoinListener(this, this.userManager, this.groupManager).register(this);
    new PlayerQuitListener(this.userManager).register(this);
    new PlayerChatListener(this.userManager).register(this);
  }

  private void createTables() throws SQLException {
    for (Table table : Tables.values()) {
      try (SQLExecutor executor = new SQLExecutor(this.dataSource)) {
        executor.createTable(table);
      }
    }
  }
}
