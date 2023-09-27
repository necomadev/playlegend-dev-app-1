package gg.use.playlegend.permissions.database.table;

public enum Tables implements Table {
  USERS("users", "uuid CHAR(36) PRIMARY KEY, playerGroup CHAR(16), playerGroupExpire DATETIME"),
  GROUPS("groups", "name CHAR(16) PRIMARY KEY, prefix CHAR(16)");

  private final String name, schema;

  Tables(String name, String schema) {
    this.name = name;
    this.schema = schema;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getSchema() {
    return schema;
  }

  @Override
  public String toString() {
    return getName();
  }
}
