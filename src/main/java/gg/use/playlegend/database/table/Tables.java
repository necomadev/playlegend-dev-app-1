package gg.use.playlegend.database.table;

public enum Tables implements Table {
  USERS("users", "uuid CHAR(36) PRIMARY KEY, playerGroup CHAR(16), playerGroupExpire DATETIME"),
  GROUPS("groups", "name CHAR(16) PRIMARY KEY, prefix CHAR(16)", "'DEFAULT', '[Default]'");

  private final String name, schema;
  private final String[] defaultEntries;

  Tables(String name, String schema, String... defaultEntries) {
    this.name = name;
    this.schema = schema;
    this.defaultEntries = defaultEntries;
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
  public String[] getDefaultEntries() {
    return this.defaultEntries;
  }

  @Override
  public String toString() {
    return getName();
  }
}
