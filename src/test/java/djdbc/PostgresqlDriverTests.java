package djdbc;

import djdbc.demodb.DB;

public class PostgresqlDriverTests extends DriverTests {
  private final DB db = DB.postgresql();
  @Override
  DB db() {
    return db;
  }
}
