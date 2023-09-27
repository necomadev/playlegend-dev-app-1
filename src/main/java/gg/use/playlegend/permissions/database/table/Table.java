package gg.use.playlegend.permissions.database.table;

public interface Table {

  /**
   * Specifies the name of the sql database table
   *
   * @return name of the sql database table name
   */
  String getName();

  /**
   * Specifies the respective sql database table schema for the given table
   *
   * @return String value of the given table schema for the database
   */
  String getSchema();

}
