package djdbc;

import djdbc.demodb.DB;
import fj.data.Option;
import junit.framework.TestCase;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.concurrent.Semaphore;

public class DemoDBTests extends TestCase {

  private final DB db = new DB();

  public void setUp() throws SQLException {
    try {
      db.dropSchema();
    } catch (SQLException ignored) {}
    db.createSchema();
  }

  public void tearDown() throws SQLException, IOException {
    db.dropSchema();
    db.close();
  }

  public void testTransfer() throws Exception {
    int id1 = db.createAccount(new BigDecimal(0));
    int id2 = db.createAccount(new BigDecimal(0));
    db.transfer(id1, id2, new BigDecimal(1));
    assertEquals(Option.some(new BigDecimal(-1)), db.getBalance(id1));
    assertEquals(Option.some(new BigDecimal(1)), db.getBalance(id2));
    db.transfer(id1, id2, new BigDecimal(2));
    assertEquals(Option.some(new BigDecimal(-3)), db.getBalance(id1));
    assertEquals(Option.some(new BigDecimal(3)), db.getBalance(id2));
  }

  public void testConflicts() throws Exception {
    final int id1 = db.createAccount(new BigDecimal(0));
    final int id2 = db.createAccount(new BigDecimal(0));
    final Semaphore semaphore = new Semaphore(2);
    semaphore.acquire(2);
    final Runnable runnable =
      new Runnable() {
        @Override
        public void run() {
          for (int i = 0; i < 10; i++) {
            try {
              db.transferMultipleTimes(id1, id2, new BigDecimal(1), 100);
            } catch (SQLException e) {
              throw new Error(e);
            }
          }
          semaphore.release();
        }
      };
    new Thread(runnable).start();
    new Thread(runnable).start();
    semaphore.acquire(2);
    assertEquals(Option.some(new BigDecimal(2000)), db.getBalance(id2));
  }
}
