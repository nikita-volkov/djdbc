package fjdbc;

import fj.data.*;
import fjdbc.demodb.DB;
import org.junit.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.concurrent.*;

import static org.junit.Assert.assertEquals;

public class DriverTests {

  @Test
  public void conflicts() throws Exception {
    for (final DB db : new DB[]{DB.postgresql(), DB.h2()}) {
      db.createSchema();
      try {
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
                  db.simulateConflict(id1, id2);
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
      } finally {
        db.dropSchema();
        db.close();
      }
    }
  }

}