package djdbc.drivers;

import djdbc.Driver;

import java.sql.*;

public final class PostgresqlDriver implements Driver {
  public final static PostgresqlDriver i = new PostgresqlDriver();
  private PostgresqlDriver() {}
  @Override
  public void initialize() {
    try {
      Class.forName("org.postgresql.Driver");
    } catch (ClassNotFoundException e) {
      throw new Error("Driver classes are not in scope", e);
    }
  }
  @Override
  public boolean detectTransactionConflict(SQLException exception) {
    return exception.getSQLState().equals("40001");
  }
}
