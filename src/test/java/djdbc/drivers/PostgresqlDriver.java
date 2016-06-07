package djdbc.drivers;

import djdbc.Driver;

import java.sql.*;

public final class PostgresqlDriver implements Driver {
  public final static PostgresqlDriver i = new PostgresqlDriver();
  private PostgresqlDriver() {}
  @Override
  public boolean detectTransactionConflict(SQLException exception) {
    return exception.getSQLState().equals("40001");
  }
}
