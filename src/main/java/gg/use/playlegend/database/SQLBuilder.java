package gg.use.playlegend.database;

import gg.use.playlegend.database.table.Table;
import lombok.Getter;

public class SQLBuilder {

  @Getter
  private StringBuilder statement = new StringBuilder();

  public String build() {
    return statement.toString();
  }

  public SQLBuilder setStatement(String statement) {
    this.statement = new StringBuilder(statement);
    return this;
  }

  public SQLBuilder select(Table table, String columns) {
    statement.append("SELECT ").append(columns).append(" FROM ").append(table);
    return this;
  }

  public SQLBuilder insert(Table table, String columns) {
    statement.append("INSERT INTO ").append(table);
    if (!columns.isEmpty()) {
      statement.append(" VALUES(").append(columns).append(")");
    }
    return this;
  }

  public SQLBuilder insertIgnore(Table table, String columns) {
    statement.append("INSERT IGNORE INTO ").append(table);
    if (!columns.isEmpty()) {
      statement.append(" VALUES(").append(columns).append(")");
    }
    return this;
  }

  public SQLBuilder values(String values) {
    statement.append(" VALUES (").append(values).append(")");
    return this;
  }

  public SQLBuilder update(Table table, String... columns) {
    statement.append("UPDATE ").append(table).append(" SET ");
    for (int i = 0; i < columns.length; i++) {
      String column = columns[i];
      statement.append(column).append(" = ?");

      if (i + 1 < columns.length) {
        statement.append(", ");
      }
    }
    return this;
  }

  public SQLBuilder updateRaw(Table table) {
    statement.append("UPDATE ").append(table);
    return this;
  }

  public SQLBuilder set() {
    statement.append(" SET ");
    return this;
  }

  public SQLBuilder delete(Table table) {
    statement.append("DELETE FROM ").append(table);
    return this;
  }

  public SQLBuilder where(String condition) {
    if (!condition.isEmpty()) {
      statement.append(" WHERE ").append(condition);
    }
    return this;
  }

  public SQLBuilder and(String condition) {
    statement.append(" AND ").append(condition);
    return this;
  }

  public SQLBuilder or(String condition) {
    statement.append(" OR ").append(condition);
    return this;
  }

  public SQLBuilder in(String values) {
    if (!values.isEmpty()) {
      statement.append(" IN (").append(values).append(")");
    }
    return this;
  }

  public SQLBuilder orderBy(String column, boolean ascending) {
    statement.append(" ORDER BY ").append(column).append(ascending ? " ASC" : " DESC");
    return this;
  }

  public SQLBuilder limit(int limit) {
    statement.append(" LIMIT ").append(limit);
    return this;
  }

  public SQLBuilder join(Table table, String condition) {
    statement.append(" JOIN ").append(table).append(" ON ").append(condition);
    return this;
  }

  public SQLBuilder leftJoin(Table table, String condition) {
    statement.append(" LEFT JOIN ").append(table).append(" ON ").append(condition);
    return this;
  }

  public SQLBuilder increaseInteger(String field, int amount) {
    statement.append(field).append(" = ").append(field).append(" + ").append(amount);
    return this;
  }

}

