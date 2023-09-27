package gg.use.playlegend.permissions.database;

import com.zaxxer.hikari.HikariDataSource;
import gg.use.playlegend.permissions.database.table.Table;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class SQLExecutor implements AutoCloseable {

  private final Connection connection;

  private PreparedStatement preparedStatement;
  private ResultSet resultSet;

  public SQLExecutor(HikariDataSource dataSource) {
    try {
      this.connection = dataSource.getConnection();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void createStatement(String sql) throws SQLException {
    this.preparedStatement = this.connection.prepareStatement(sql);
  }

  public ResultSet executeQuery(String sql, Object... params) throws SQLException {
    if (this.preparedStatement == null) {
      this.createStatement(sql);
    }
    for (int i = 0; i < params.length; i++) {
      this.preparedStatement.setObject(i + 1, params[i]);
    }
    this.resultSet = this.preparedStatement.executeQuery();
    return resultSet;
  }

  public int executeUpdate(String sql, Object... params) throws SQLException {
    if (this.preparedStatement == null) {
      this.createStatement(sql);
    }
    for (int i = 0; i < params.length; i++) {
      this.preparedStatement.setObject(i + 1, params[i]);
    }
    return this.preparedStatement.executeUpdate();
  }

  public void createTable(Table table) throws SQLException {
    SQLQueryBuilder builder = new SQLQueryBuilder().setStatement("CREATE TABLE IF NOT EXISTS " + table + "(" + table.getSchema() + ")");
    this.executeUpdate(builder.build());
  }

  public void dropTable(Table table) throws SQLException {
    SQLQueryBuilder builder = new SQLQueryBuilder().setStatement("DROP TABLE IF EXISTS " + table);
    this.executeUpdate(builder.build());
  }

  public boolean rowExists(Table table, String condition, Object... params) throws SQLException {
    SQLQueryBuilder builder = new SQLQueryBuilder().select(table, "1").where(condition);
    return this.executeQuery(builder.build(), params).next();
  }

  public Object getObject(String column) {
    try {
      return this.resultSet.getObject(column);
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return null;
  }

  public Object getObject(int column) {
    try {
      return this.resultSet.getObject(column);
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return null;
  }

  public String getString(String column) {
    return (String) this.getObject(column);
  }

  public String getString(int column) {
    return (String) getObject(column);
  }

  public Integer getInteger(String column) {
    Object obj = getObject(column);
    if (obj instanceof BigDecimal) {
      return ((BigDecimal) obj).intValue();
    }
    return (Integer) obj;
  }

  public Long getLong(String column) {
    Object obj = getObject(column);
    if (obj instanceof BigDecimal) {
      return ((BigDecimal) obj).longValue();
    }
    return (Long) obj;
  }

  public Timestamp getTimestamp(String column) {
    return (Timestamp) getObject(column);
  }

  public Timestamp getTimestamp(int column) {
    return (Timestamp) getObject(column);
  }

  public LocalDateTime getLocalDateTime(String column) {
    return (LocalDateTime) getObject(column);
  }

  public LocalDateTime getLocalDateTime(int column) {
    return (LocalDateTime) getObject(column);
  }

  protected void closeResource(AutoCloseable resource) {
    try {
      resource.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void close() {
    if (preparedStatement != null) {
      this.closeResource(preparedStatement);
    }
    if (resultSet != null) {
      this.closeResource(resultSet);
    }
    if (connection != null) {
      this.closeResource(connection);
    }
  }
}
