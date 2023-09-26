package gg.use.playlegend.database;

import static org.junit.jupiter.api.Assertions.*;

import gg.use.playlegend.database.SQLBuilder;
import gg.use.playlegend.database.table.Tables;
import org.junit.jupiter.api.Test;

public class SQLBuilderTest {

  @Test
  public void sqlBuilderTest() {
    SQLBuilder sqlBuilder = new SQLBuilder();
    sqlBuilder.insert(Tables.GROUPS, "'MEMBER', '[Member]'");
    assertEquals("INSERT INTO groups VALUES('MEMBER', '[Member]')", sqlBuilder.build());
  }

}
