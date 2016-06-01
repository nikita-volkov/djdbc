package fjdbc;

import org.junit.*;

public class TransactionIsolationTests {
  @Test
  public void max1() throws Exception {
    Assert.assertEquals(
      TransactionIsolation.repeatableRead,
      TransactionIsolation.max(TransactionIsolation.readCommitted, TransactionIsolation.repeatableRead, TransactionIsolation.readUncommitted)
    );
  }
  @Test
  public void max2() throws Exception {
    Assert.assertEquals(
      TransactionIsolation.serializable,
      TransactionIsolation.max(TransactionIsolation.readCommitted, TransactionIsolation.serializable, TransactionIsolation.readUncommitted)
    );
  }
}
