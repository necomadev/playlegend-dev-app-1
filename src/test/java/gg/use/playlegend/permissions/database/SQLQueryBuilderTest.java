package gg.use.playlegend.permissions.database;

import static org.junit.jupiter.api.Assertions.*;

import gg.use.playlegend.permissions.database.table.Tables;
import org.junit.jupiter.api.Test;

public class SQLQueryBuilderTest {

  @Test
  public void sqlBuilderTest() {
    SQLQueryBuilder sqlQueryBuilder = new SQLQueryBuilder();
    sqlQueryBuilder.insert(Tables.GROUPS, "'MEMBER', '[Member]'");
    assertEquals("INSERT INTO groups VALUES('MEMBER', '[Member]')", sqlQueryBuilder.build());
  }

}
