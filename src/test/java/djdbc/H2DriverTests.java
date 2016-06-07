package djdbc;

import djdbc.demodb.DB;

public class H2DriverTests extends DriverTests {
  private final DB db = DB.h2();
  @Override
  DB db() {
    return db;
  }
}
