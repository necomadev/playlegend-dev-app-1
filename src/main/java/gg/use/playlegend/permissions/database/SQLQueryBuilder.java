package gg.use.playlegend.permissions.database;

import gg.use.playlegend.permissions.database.table.Table;
import lombok.Getter;

public class SQLQueryBuilder {

  @Getter
  private StringBuilder statement = new StringBuilder();

  public String build() {
    return statement.toString();
  }

  public SQLQueryBuilder setStatement(String statement) {
    this.statement = new StringBuilder(statement);
    return this;
  }

  public SQLQueryBuilder select(Table table, String columns) {
    statement.append("SELECT ").append(columns).append(" FROM ").append(table);
    return this;
  }

  public SQLQueryBuilder insert(Table table, String columns) {
    statement.append("INSERT INTO ").append(table);
    if (!columns.isEmpty()) {
      statement.append(" VALUES(").append(columns).append(")");
    }
    return this;
  }

  public SQLQueryBuilder insertIgnore(Table table, String columns) {
    statement.append("INSERT IGNORE INTO ").append(table);
    if (!columns.isEmpty()) {
      statement.append(" VALUES(").append(columns).append(")");
    }
    return this;
  }

  public SQLQueryBuilder values(String values) {
    statement.append(" VALUES (").append(values).append(")");
    return this;
  }

  public SQLQueryBuilder update(Table table, String... columns) {
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

  public SQLQueryBuilder updateRaw(Table table) {
    statement.append("UPDATE ").append(table);
    return this;
  }

  public SQLQueryBuilder set() {
    statement.append(" SET ");
    return this;
  }

  public SQLQueryBuilder delete(Table table) {
    statement.append("DELETE FROM ").append(table);
    return this;
  }

  public SQLQueryBuilder where(String condition) {
    if (!condition.isEmpty()) {
      statement.append(" WHERE ").append(condition);
    }
    return this;
  }

  public SQLQueryBuilder and(String condition) {
    statement.append(" AND ").append(condition);
    return this;
  }

  public SQLQueryBuilder or(String condition) {
    statement.append(" OR ").append(condition);
    return this;
  }

  public SQLQueryBuilder in(String values) {
    if (!values.isEmpty()) {
      statement.append(" IN (").append(values).append(")");
    }
    return this;
  }

  public SQLQueryBuilder orderBy(String column, boolean ascending) {
    statement.append(" ORDER BY ").append(column).append(ascending ? " ASC" : " DESC");
    return this;
  }

  public SQLQueryBuilder limit(int limit) {
    statement.append(" LIMIT ").append(limit);
    return this;
  }

  public SQLQueryBuilder join(Table table, String condition) {
    statement.append(" JOIN ").append(table).append(" ON ").append(condition);
    return this;
  }

  public SQLQueryBuilder leftJoin(Table table, String condition) {
    statement.append(" LEFT JOIN ").append(table).append(" ON ").append(condition);
    return this;
  }

  public SQLQueryBuilder increaseInteger(String field, int amount) {
    statement.append(field).append(" = ").append(field).append(" + ").append(amount);
    return this;
  }

}

