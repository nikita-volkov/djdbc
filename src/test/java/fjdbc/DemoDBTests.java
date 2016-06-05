package fjdbc;

import fj.data.Option;
import fjdbc.demodb.DB;
import org.junit.*;

import java.math.BigDecimal;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class DemoDBTests {

  private final DB db = DB.h2();

  @Before
  public void setUp() throws SQLException {
    try {
      db.dropSchema();
    } catch (SQLException ignored) {}
    db.createSchema();
  }

  @After
  public void tearDown() throws SQLException {
    db.dropSchema();
  }

  @Test
  public void transfer() throws Exception {
    int id1 = db.createAccount(new BigDecimal(0));
    int id2 = db.createAccount(new BigDecimal(0));
    db.transfer(id1, id2, new BigDecimal(1));
    assertEquals(Option.some(new BigDecimal(-1)), db.getBalance(id1));
    assertEquals(Option.some(new BigDecimal(1)), db.getBalance(id2));
    db.transfer(id1, id2, new BigDecimal(2));
    assertEquals(Option.some(new BigDecimal(-3)), db.getBalance(id1));
    assertEquals(Option.some(new BigDecimal(3)), db.getBalance(id2));
  }

}